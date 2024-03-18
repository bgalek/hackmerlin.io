package com.github.bgalek;

import org.springframework.jdbc.core.simple.JdbcClient;

class MerlinLogger {
    private final JdbcClient jdbcClient;

    MerlinLogger(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    void logAttempt(String session, int currentLevel, String prompt, String response) {
        String sql = "INSERT INTO logs (session, level, prompt, response) VALUES (:session, :level, :prompt, :response)";
        this.jdbcClient.sql(sql)
                .param("session", session)
                .param("level", currentLevel)
                .param("prompt", prompt)
                .param("response", response)
                .update();
    }
}
