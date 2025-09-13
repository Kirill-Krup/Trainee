-- liquibase formatted sql

-- changeset Kirill:create-users-table
CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    surname    VARCHAR(255) NOT NULL,
    birth_date DATE,
    email      VARCHAR(255) NOT NULL UNIQUE
);
-- rollback DROP TABLE users;

-- changeset Kirill:create-card-info-table
CREATE TABLE card_info
(
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT       NOT NULL,
    number          VARCHAR(19)  NOT NULL UNIQUE,
    holder          VARCHAR(255) NOT NULL,
    expiration_date DATE,
    FOREIGN KEY (user_id) REFERENCES users (id)
);
-- rollback DROP TABLE card_info;

-- changeset Kirill:add-indexes
CREATE INDEX idx_card_info_user_id ON card_info (user_id);
-- rollback DROP INDEX idx_card_info_user_id ON card_info;