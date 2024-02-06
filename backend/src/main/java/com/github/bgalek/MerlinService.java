package com.github.bgalek;

import com.azure.ai.openai.OpenAIClient;
import com.github.bgalek.levels.MerlinLevel;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.slf4j.LoggerFactory.getLogger;

public class MerlinService {
    private static final Logger logger = getLogger(MerlinService.class);
    private final OpenAIClient openAIClient;
    private final MerlinLevelRepository merlinLevelRepository;
    private final List<String> merlinPasswords;

    public MerlinService(OpenAIClient openAiClient,
                         MerlinLevelRepository merlinLevelRepository,
                         List<String> merlinPasswords) {
        this.openAIClient = openAiClient;
        this.merlinLevelRepository = merlinLevelRepository;
        this.merlinPasswords = merlinPasswords;
    }

    public String respond(HttpSession httpSession, int currentLevel, String prompt) {
        MerlinLevel level = merlinLevelRepository.getLevel(currentLevel);
        if (level.inputFilter(prompt)) return level.inputFilterResponse();
        String currentSessionSecret = getCurrentSessionPassword(httpSession, currentLevel);
        return openAIClient.getChatCompletions(level.getModel(), level.prompt(prompt, currentSessionSecret))
                .getChoices()
                .stream()
                .map(it -> it.getMessage().getContent())
                .findFirst()
                .filter(output -> !level.outputFilter(output, currentSessionSecret))
                .orElse(level.outputFilterResponse());
    }

    public boolean checkSecret(HttpSession httpSession, String secret) {
        int currentLevel = getCurrentLevel(httpSession);
        return getCurrentSessionPassword(httpSession, currentLevel).equalsIgnoreCase(secret);
    }

    public String advanceLevel(HttpSession httpSession) {
        int currentLevel = getCurrentLevel(httpSession);
        MerlinLevel merlinLevel = merlinLevelRepository.getLevel(currentLevel);
        httpSession.setAttribute("level", currentLevel + 1);
        if (currentLevel == getMaxLevel()) {
            httpSession.setAttribute("finished", true);
            logger.info(
                    "We have a winner! Session: {}, Duration: {}",
                    httpSession.getId(),
                    Duration.ofMillis(System.currentTimeMillis() - httpSession.getCreationTime()).toString()
            );
        }
        return merlinLevel.getLevelFinishedResponse();
    }

    public int getCurrentLevel(HttpSession session) {
        return Optional.ofNullable(session.getAttribute("level")).map(x -> (Integer) x).orElse(1);
    }

    public int getMaxLevel() {
        return merlinLevelRepository.count();
    }

    private String getCurrentSessionPassword(HttpSession httpSession, int currentLevel) {
        Random random = new Random(httpSession.getId().hashCode());
        for (int i = 1; i < currentLevel; i++) {
            random.nextInt(merlinPasswords.size());
        }
        int passwordIndex = random.nextInt(merlinPasswords.size());
        return merlinPasswords.get(passwordIndex);
    }
}
