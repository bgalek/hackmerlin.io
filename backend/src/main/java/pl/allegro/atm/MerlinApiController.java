package pl.allegro.atm;


import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api")
class MerlinApiController {

    private final MerlinService merlinService;
    private static final Logger logger = LoggerFactory.getLogger(MerlinApiController.class);

    public MerlinApiController(MerlinService merlinService) {
        this.merlinService = merlinService;
    }

    @GetMapping(value = "/user")
    public MerlinSessionResponse level(HttpSession session) {
        return new MerlinSessionResponse(
                session.getId(),
                merlinService.getCurrentLevel(session),
                merlinService.getMaxLevel(),
                Optional.ofNullable(System.getenv("FLY_ALLOC_ID")).orElse("local"));
    }

    @PostMapping(value = "/question", consumes = "text/plain")
    public ResponseEntity<String> level(HttpSession session, @RequestBody(required = false) String prompt) {
        if (Strings.isBlank(prompt)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Prompt is required");
        if (prompt.length() > 150) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Prompt too long");
        int currentLevel = merlinService.getCurrentLevel(session);
        String response = merlinService.respond(currentLevel, prompt);
        logger.info("Session: {}, Level: {}, Prompt: {}, Response: {}", session.getId().substring(0, 5), currentLevel, prompt, response);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/submit", consumes = "text/plain")
    public ResponseEntity<MerlinSessionResponse> submit(HttpSession session, @RequestBody String password) {
        if (password.length() > 20) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password too long");
        if (merlinService.checkSecret(session, password)) {
            String levelFinishedMessage = merlinService.advanceLevel(session);
            return ResponseEntity.ok(new MerlinSessionResponse(
                    session.getId(),
                    merlinService.getCurrentLevel(session),
                    merlinService.getMaxLevel(),
                    levelFinishedMessage,
                    Optional.ofNullable(System.getenv("FLY_ALLOC_ID")).orElse("local")
            ));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping(value = "/reset")
    public ResponseEntity<Void> reset(HttpSession session) {
        session.invalidate();
        return ResponseEntity.accepted().build();
    }

    record MerlinSessionResponse(String id, int currentLevel, int maxLevel, String finishedMessage, String instanceId) {
        MerlinSessionResponse(String id, int currentLevel, int maxLevel, String instanceId) {
            this(id, currentLevel, maxLevel, null, instanceId);
        }
    }
}
