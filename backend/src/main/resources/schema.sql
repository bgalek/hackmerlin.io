create table IF NOT EXISTS leaderboard
(
    id          serial primary key,
    session     char(36)  not null,
    name        char(200) not null,
    started_at  timestamp not null,
    finished_at timestamp default CURRENT_TIMESTAMP
);

create table IF NOT EXISTS logs
(
    id          serial primary key,
    session     char(36)  not null,
    level       int not null,
    prompt      char(200) not null,
    response    char(200) not null
);