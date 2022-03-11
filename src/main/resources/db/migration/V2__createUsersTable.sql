CREATE TABLE users
(
    user_id bigint NOT NULL,
    email varchar(50) NOT NULL,
    PRIMARY KEY(user_id)
);

INSERT INTO users(user_id, email)
values(1, "mile.cubela1@gmail.com");