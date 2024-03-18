package com.github.bgalek;


import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
class MerlinApiController {

    private final MerlinService merlinService;

    public MerlinApiController(MerlinService merlinService) {
        this.merlinService = merlinService;
    }

    @GetMapping(value = "/user")
    MerlinSessionResponse level(HttpSession session) {
        return new MerlinSessionResponse(
                session.getId(),
                merlinService.getCurrentLevel(session),
                merlinService.getMaxLevel(),
                null,
                Optional.ofNullable(session.getAttribute("submittedName")).map(Object::toString).orElse(null)
        );
    }

    @PostMapping(value = "/question", consumes = "text/plain")
    ResponseEntity<String> level(HttpSession session, @RequestBody(required = false) String prompt) {
        if (prompt.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Prompt is required");
        if (prompt.length() > 150) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Prompt too long");
        int currentLevel = merlinService.getCurrentLevel(session);
        String response = merlinService.respond(session, currentLevel, prompt);
        merlinService.logAttempt(session.getId(), currentLevel, prompt, response);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/submit", consumes = "text/plain")
    ResponseEntity<MerlinSessionResponse> submit(HttpSession session, @RequestBody String password) {
        if (password.length() > 20) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password too long");
        if (password.isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required");
        if (merlinService.checkSecret(session, password)) {
            String levelFinishedMessage = merlinService.advanceLevel(session);
            return ResponseEntity.ok(new MerlinSessionResponse(
                    session.getId(),
                    merlinService.getCurrentLevel(session),
                    merlinService.getMaxLevel(),
                    levelFinishedMessage
            ));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/leaderboard")
    ResponseEntity<Set<LeaderboardEntryResponse>> leaderboard() {
        var body = merlinService.getLeaderboard()
                .stream()
                .map(it -> new LeaderboardEntryResponse(
                        it.session().substring(0, 5),
                        it.name(),
                        it.startedAt(),
                        it.finishedAt(),
                        Duration.between(it.startedAt(), it.finishedAt()).toMillis())
                ).collect(Collectors.toSet());
        return ResponseEntity.ok().body(body);
    }

    @PostMapping(value = "/leaderboard/submit")
    ResponseEntity<Void> submitToLeaderboard(HttpSession session, @RequestBody MerlinLeaderboardRequest leaderboardRequest) {
        if (merlinService.getCurrentLevel(session) <= merlinService.getMaxLevel()) {
            return ResponseEntity.badRequest().build();
        }
        merlinService.submitName(session, leaderboardRequest.name);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/reset")
    ResponseEntity<Void> reset(HttpSession session) {
        session.invalidate();
        return ResponseEntity.accepted().build();
    }

    record MerlinSessionResponse(
            String id,
            int currentLevel,
            int maxLevel,
            String finishedMessage,
            String submittedName
    ) {
        MerlinSessionResponse(String id, int currentLevel, int maxLevel, String finishedMessage) {
            this(id, currentLevel, maxLevel, finishedMessage, null);
        }
    }

    record MerlinLeaderboardRequest(String name) {
    }

    record LeaderboardEntryResponse(String id, String name, Instant startedAt, Instant finishedAt, long durationInMilliseconds) {
    }
}
