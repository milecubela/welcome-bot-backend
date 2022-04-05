CREATE TABLE users
(
    user_id   bigint NOT NULL,
    email     text   NOT NULL,
    user_role text   NOT NULL,
    PRIMARY KEY (user_id)
);