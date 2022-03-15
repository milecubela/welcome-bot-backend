CREATE TABLE users
(
    user_id bigint NOT NULL,
    email varchar(50) NOT NULL,
    user_role varchar(50) NOT NULL,
    PRIMARY KEY(user_id)
);

INSERT INTO users(user_id, email, user_role)
values(1, 'mile.cubela1@gmail.com', 'ADMIN');