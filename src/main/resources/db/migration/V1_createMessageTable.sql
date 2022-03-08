CREATE TABLE messages
(
    id int NOT NULL,
    title varchar(50) NOT NULL,
    text varchar(50) NOT NULL,
        PRIMARY KEY(id)
)

-- CREATE TABLE messages
-- (
--     messageId bigint NOT NULL,
--     title varchar(50) NOT NULL,
--     text varchar(50) NOT NULL,
--     PRIMARY KEY(messageId)
-- );
--
-- CREATE  TABLE schedules
-- (
--     scheduleId bigint NOT NULL,
--     runDate DATE NOT NULL,
--     isRepeat BOOLEAN NOT NULL,
--     isActive BOOLEAN NOT NULL,
--     createdAt DATE NOT NULL,
--     messageId bigint NOT NULL,
--     PRIMARY KEY (scheduleId),
--     FOREIGN KEY (messageId) REFERENCES messages(messageId)
-- );
--
-- CREATE TABLE triggers
-- (
--     triggerId bigint NOT NULL,
--     messageId bigint NOT NULL,
--     channel varchar(50) NOT NULL,
--     isActive BOOLEAN NOT NULL,
--     createdAT DATE NOT NULL,
--     CONSTRAINT PRIMARY KEY (triggerId),
--     FOREIGN KEY (messageId) REFERENCES messages(messageId)
-- )
--
