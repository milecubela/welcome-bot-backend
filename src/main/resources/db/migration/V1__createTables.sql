CREATE TABLE messages
(
    message_id bigint NOT NULL,
    title      text   NOT NULL,
    text       text   NOT NULL,
    created_at DATE   NULL,
    PRIMARY KEY (message_id)
);

CREATE TABLE schedules
(
    schedule_id        bigint   NOT NULL,
    run_date           datetime NULL,
    is_repeat          BOOLEAN  NOT NULL,
    is_active          BOOLEAN  NOT NULL,
    scheduler_interval SMALLINT NULL,
    created_at         DATE     NULL,
    message_id         bigint   NOT NULL,
    next_run           datetime null,
    channel            text     NULL,
    PRIMARY KEY (schedule_id),
    FOREIGN KEY (message_id) REFERENCES messages (message_id)
);

CREATE TABLE triggers
(
    trigger_id    bigint NOT NULL,
    message_id    bigint NOT NULL,
    channel       text   NOT NULL,
    is_active     bit DEFAULT (0),
    trigger_event int,
    PRIMARY KEY (trigger_id),
    FOREIGN KEY (message_id) REFERENCES messages (message_id)
);