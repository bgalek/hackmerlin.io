package pl.allegro.atm;


import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
class MerlinApiController {

    private final MerlinService merlinService;

    public MerlinApiController(MerlinService merlinService) {
        this.merlinService = merlinService;
    }

    @GetMapping(value = "/user")
    public MerlinSessionResponse level(HttpSession session) {
        return new MerlinSessionResponse(
                session.getId(),
                merlinService.getCurrentLevel(session),
                merlinService.getMaxLevel()
        );
    }

    @PostMapping(value = "/question", consumes = "text/plain")
    public ResponseEntity<String> level(HttpSession session, @RequestBody String prompt) {
        if (prompt.length() > 150) throw new IllegalArgumentException("Prompt too long");
        int currentLevel = merlinService.getCurrentLevel(session);
        String response = merlinService.respond(currentLevel, prompt);
        return ResponseEntity.ok(response.substring(0, 100));
    }

    @PostMapping(value = "/submit", consumes = "text/plain")
    public ResponseEntity<MerlinSessionResponse> submit(HttpSession session, @RequestBody String password) {
        if (password.length() > 150) throw new IllegalArgumentException("Password too long");
        if (merlinService.checkSecret(session, password)) {
            merlinService.advanceLevel(session);
            return ResponseEntity.ok(new MerlinSessionResponse(
                    session.getId(),
                    merlinService.getCurrentLevel(session),
                    merlinService.getMaxLevel()
            ));
        }
        return ResponseEntity.badRequest().build();
    }

    record MerlinSessionResponse(String id, int currentLevel, int maxLevel) {
    }
}
