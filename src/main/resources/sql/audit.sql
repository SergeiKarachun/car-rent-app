--accident
ALTER TABLE orders
    ADD COLUMN modified_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN modified_by VARCHAR(32),
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN created_by VARCHAR(32);


--orders
ALTER TABLE accident
    ADD COLUMN modified_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN modified_by VARCHAR(32),
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN created_by VARCHAR(32);

--rental_time
ALTER TABLE rental_time
    ADD COLUMN modified_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN modified_by VARCHAR(32),
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN created_by VARCHAR(32);

--users
ALTER TABLE users
    ADD COLUMN modified_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN modified_by VARCHAR(32),
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN created_by VARCHAR(32);

--user_details
ALTER TABLE user_details
    ADD COLUMN modified_at TIMESTAMP NOT NULL DEFAULT now(),
    ADD COLUMN modified_by VARCHAR(32),
    ADD COLUMN created_at TIMESTAMP,
    ADD COLUMN created_by VARCHAR(32);






