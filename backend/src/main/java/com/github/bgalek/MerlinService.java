package com.github.bgalek;

import com.azure.ai.openai.OpenAIClient;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.bgalek.levels.MerlinLevel;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

class MerlinService {
    private static final Logger logger = getLogger(MerlinService.class);
    private final OpenAIClient openAIClient;
    private final MerlinLevelRepository merlinLevelRepository;
    private final MerlinLeaderboardRepository merlinLeaderboardRepository;
    private final MerlinLogger merlinLogger;
    private final List<String> merlinPasswords;
    private final Cache<String, String> cache = Caffeine.newBuilder()
            .maximumSize(500)
            .expireAfterWrite(Duration.ofMinutes(1))
            .build();

    MerlinService(OpenAIClient openAIClient,
                  MerlinLevelRepository merlinLevelRepository,
                  MerlinLeaderboardRepository merlinLeaderboardRepository,
                  MerlinLogger merlinLogger,
                  List<String> merlinPasswords) {
        this.openAIClient = openAIClient;
        this.merlinLevelRepository = merlinLevelRepository;
        this.merlinLeaderboardRepository = merlinLeaderboardRepository;
        this.merlinLogger = merlinLogger;
        this.merlinPasswords = merlinPasswords;
    }

    String respond(HttpSession httpSession, int currentLevel, String prompt) {
        MerlinLevel level = merlinLevelRepository.getLevel(currentLevel);
        if (level.inputFilter(prompt)) return level.inputFilterResponse();
        String currentSessionSecret = getCurrentSessionPassword(httpSession, currentLevel);
        return cache.get(getCacheKey(httpSession.getId(), currentLevel, prompt), key ->
                openAIClient.getChatCompletions(level.getModel(), level.prompt(prompt, currentSessionSecret))
                        .getChoices()
                        .stream()
                        .map(it -> it.getMessage().getContent())
                        .findFirst()
                        .filter(output -> !level.outputFilter(output, currentSessionSecret))
                        .orElse(level.outputFilterResponse()));
    }

    boolean checkSecret(HttpSession httpSession, String secret) {
        int currentLevel = getCurrentLevel(httpSession);
        return getCurrentSessionPassword(httpSession, currentLevel).equalsIgnoreCase(secret);
    }

    String advanceLevel(HttpSession httpSession) {
        int currentLevel = getCurrentLevel(httpSession);
        MerlinLevel merlinLevel = merlinLevelRepository.getLevel(currentLevel);
        httpSession.setAttribute("level", currentLevel + 1);
        if (currentLevel == getMaxLevel()) {
            saveLeaderboardEntry(httpSession);
        }
        return merlinLevel.getLevelFinishedResponse();
    }

    int getCurrentLevel(HttpSession session) {
        return Optional.ofNullable(session.getAttribute("level")).map(x -> (Integer) x).orElse(1);
    }

    int getMaxLevel() {
        return merlinLevelRepository.count();
    }

    void saveLeaderboardEntry(HttpSession session) {
        try {
            merlinLeaderboardRepository.addEntry(session.getId(), Instant.ofEpochMilli(session.getCreationTime()));
        } catch (Exception e) {
            logger.error("Failed to save leaderboard entry", e);
        }
    }

    void submitName(HttpSession session, String name) {
        try {
            session.setAttribute("submittedName", name);
            merlinLeaderboardRepository.addName(session.getId(), name);
        } catch (Exception e) {
            logger.error("Failed to log attempt", e);
        }
    }

    void logAttempt(String session, int currentLevel, String prompt, String response) {
        try {
            merlinLogger.logAttempt(session, currentLevel, prompt, response);
        } catch (Exception e) {
            logger.error("Failed to log attempt", e);
        }
    }

    Set<LeaderboardEntry> getLeaderboard() {
        return merlinLeaderboardRepository.getLeaderboard();
    }

    private String getCurrentSessionPassword(HttpSession httpSession, int currentLevel) {
        Random random = new Random(httpSession.getId().hashCode());
        for (int i = 1; i < currentLevel; i++) {
            random.nextInt(merlinPasswords.size());
        }
        int passwordIndex = random.nextInt(merlinPasswords.size());
        return merlinPasswords.get(passwordIndex).toUpperCase(Locale.ROOT);
    }

    private static String getCacheKey(String httpSession, int currentLevel, String prompt) {
        return "%s-%d-%s".formatted(httpSession, currentLevel, prompt);
    }

    record LeaderboardEntry(String session, String name, Instant startedAt, Instant finishedAt) {
    }
}
