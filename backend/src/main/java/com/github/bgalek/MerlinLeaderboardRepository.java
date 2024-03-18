package com.github.bgalek;

import org.springframework.jdbc.core.simple.JdbcClient;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.util.Set;

class MerlinLeaderboardRepository {
    private final JdbcClient jdbcClient;

    MerlinLeaderboardRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    void addEntry(String session, Instant sessionStart) {
        this.jdbcClient.sql("INSERT INTO leaderboard (session, started_at) VALUES (:session, :startedAt)")
                .param("session", session, Types.VARCHAR)
                .param("startedAt", Timestamp.from(sessionStart), Types.TIMESTAMP)
                .update();
    }

    void addName(String session, String name) {
        this.jdbcClient.sql("UPDATE leaderboard set name = :name where session = :session")
                .param("name", name, Types.VARCHAR)
                .param("session", session, Types.VARCHAR)
                .update();
    }

    Set<MerlinService.LeaderboardEntry> getLeaderboard() {
        return this.jdbcClient
                .sql("SELECT * FROM leaderboard ORDER BY finished_at LIMIT 100")
                .query((rs, rowNum) -> new MerlinService.LeaderboardEntry(
                        rs.getString("session"),
                        rs.getString("name"),
                        rs.getTimestamp("started_at").toInstant(),
                        rs.getTimestamp("finished_at").toInstant())
                )
                .set();
    }

}
