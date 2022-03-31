CREATE TABLE users
(
    user_id bigint NOT NULL,
    email text NOT NULL,
    user_role text NOT NULL,
    PRIMARY KEY(user_id)
);

INSERT INTO users(user_id, email, user_role)
values(1, 'mile.cubela1@gmail.com', 'ADMIN'),
       (2, 'tarearnaut@gmail.com', 'ADMIN'),
       (3, 'thegepic@gmail.com', 'ADMIN'),
       (4, 'salkobalic3@gmail.com’s', 'ADMIN'),
       (5, 'selma.cokljat.95@gmail.com', 'ADMIN');