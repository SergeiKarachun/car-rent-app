--liquibase formatted sql


--changeset sergei:1
ALTER TABLE orders
    ADD COLUMN modified_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN modified_by VARCHAR(32),
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN created_by VARCHAR(32);

--changeset sergei:2
ALTER TABLE accident
    ADD COLUMN modified_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN modified_by VARCHAR(32),
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN created_by VARCHAR(32);

--changeset sergei:3
ALTER TABLE rental_time
    ADD COLUMN modified_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN modified_by VARCHAR(32),
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN created_by VARCHAR(32);

--changeset sergei:4
ALTER TABLE users
    ADD COLUMN modified_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN modified_by VARCHAR(32),
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN created_by VARCHAR(32);

--changeset sergei:5
ALTER TABLE user_details
    ADD COLUMN modified_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN modified_by VARCHAR(32),
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN created_by VARCHAR(32);







