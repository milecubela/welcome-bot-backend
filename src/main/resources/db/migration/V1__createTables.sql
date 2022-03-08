CREATE TABLE messages
(
    message_id bigint NOT NULL,
    title varchar(50) NOT NULL,
    text varchar(50) NOT NULL,
    created_at DATE NULL,
    PRIMARY KEY(message_id)
);

CREATE  TABLE schedules
(
    schedule_id bigint NOT NULL,
    run_date DATE NULL,
    is_repeat BOOLEAN NOT NULL,
    is_active BOOLEAN NOT NULL,
    created_at DATE NULL,
    message_id bigint NOT NULL,
    PRIMARY KEY (schedule_id),
    FOREIGN KEY (message_id) REFERENCES messages(message_id)
);

CREATE TABLE triggers
(
    trigger_id bigint NOT NULL,
    message_id bigint NOT NULL,
    channel varchar(50) NOT NULL,
    is_active BOOLEAN NOT NULL,
    created_at DATE NULL,
    PRIMARY KEY (trigger_id),
    FOREIGN KEY (message_id) REFERENCES messages(message_id)
)