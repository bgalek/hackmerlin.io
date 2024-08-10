create table IF NOT EXISTS leaderboard
(
    id          serial primary key,
    session     char(36)  not null,
    name        char(200) not null,
    started_at  timestamp not null,
    finished_at timestamp not null
);
