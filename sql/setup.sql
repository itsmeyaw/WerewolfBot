CREATE TABLE discordserver
(
    snowflake BIGINT PRIMARY KEY UNIQUE,
    prefix    VARCHAR(20) NOT NULL
);

CREATE TABLE quotes
(
    id          BIGSERIAL PRIMARY KEY,
    snowflake   BIGINT,
    quote       VARCHAR(255) NOT NULL,
    quote_maker VARCHAR(255) NOT NULL,
    FOREIGN KEY (snowflake) REFERENCES discordserver ON DELETE CASCADE
);

