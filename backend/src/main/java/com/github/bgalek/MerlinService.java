package com.github.bgalek;

import com.github.bgalek.levels.MerlinLevel;
import com.theokanning.openai.service.OpenAiService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

public class MerlinService {
    private static final Logger logger = getLogger(MerlinService.class);
    private final OpenAiService openAiService;
    private final MerlinLevelRepository merlinLevelRepository;

    public MerlinService(OpenAiService openAiService, MerlinLevelRepository merlinLevelRepository) {
        this.openAiService = openAiService;
        this.merlinLevelRepository = merlinLevelRepository;
    }

    public String respond(int currentLevel, String prompt) {
        MerlinLevel level = merlinLevelRepository.getLevel(currentLevel);
        if (level.inputFilter(prompt)) return level.inputFilterResponse();
        return openAiService.createChatCompletion(level.prompt(prompt))
                .getChoices()
                .stream()
                .map(it -> it.getMessage().getContent())
                .findFirst()
                .filter(output -> !level.outputFilter(output))
                .orElse(level.outputFilterResponse());
    }

    public boolean checkSecret(HttpSession httpSession, String secret) {
        int currentLevel = getCurrentLevel(httpSession);
        MerlinLevel merlinLevel = merlinLevelRepository.getLevel(currentLevel);
        return merlinLevel.getSecret().equalsIgnoreCase(secret);
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
}
