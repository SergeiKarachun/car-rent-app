--Brand
CREATE TABLE IF NOT EXISTS brands
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

--Model
CREATE TABLE IF NOT EXISTS models
(
    id           SERIAL PRIMARY KEY,
    brand_id     BIGINT       NOT NULL,
    name         VARCHAR(255) NOT NULL UNIQUE,
    transmission VARCHAR(128),
    engine_type  VARCHAR(128),
    CONSTRAINT models_brands_fk
        FOREIGN KEY (brand_id) REFERENCES brands (id)
            ON UPDATE CASCADE ON DELETE SET NULL
);

--Category
CREATE TABLE IF NOT EXISTS categories
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE DEFAULT 'economy'
);

--User
CREATE TABLE IF NOT EXISTS users
(
    id       SERIAL PRIMARY KEY,
    login    VARCHAR(255) NOT NULL UNIQUE,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(32)  NOT NULL DEFAULT 'user'
);

--Car
CREATE TABLE IF NOT EXISTS cars
(
    id          SERIAL PRIMARY KEY,
    brand_id    BIGINT,
    model_id    BIGINT,
    category_id BIGINT,
    color       VARCHAR(255),
    year        SMALLINT,
    car_number  VARCHAR(16),
    vin         VARCHAR(255)   NOT NULL UNIQUE,
    repaired    BOOLEAN                                     DEFAULT 'TRUE',
    image       TEXT,
    price       NUMERIC(10, 2) NOT NULL CHECK ( price > 0 ) DEFAULT '50',
    CONSTRAINT car_brand_fk
        FOREIGN KEY (brand_id) REFERENCES brands (id)
            ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT car_model_fk
        FOREIGN KEY (model_id) REFERENCES models (id)
            ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT car_category_fk
        FOREIGN KEY (category_id) REFERENCES categories (id)
            ON UPDATE CASCADE ON DELETE SET NULL
);

--Order
CREATE TABLE IF NOT EXISTS orders
(
    id           SERIAL PRIMARY KEY,
    user_id      BIGINT         NOT NULL,
    car_id       BIGINT         NOT NULL,
    date         TIMESTAMP      NOT NULL DEFAULT now(),
    passport     VARCHAR(128)   NOT NULL,
    order_status VARCHAR(32)    NOT NULL,
    sum          NUMERIC(10, 2) NOT NULL,
    CONSTRAINT orders_users_fk
        FOREIGN KEY (user_id) REFERENCES users (id)
            ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT orders_cars_fk
        FOREIGN KEY (car_id) REFERENCES cars (id)
            ON UPDATE CASCADE ON DELETE SET NULL
);

--Accident
CREATE TABLE IF NOT EXISTS accident
(
    id            SERIAL PRIMARY KEY,
    order_id      BIGINT    NOT NULL,
    accident_date TIMESTAMP NOT NULL,
    description   TEXT,
    damage        NUMERIC(10, 2),
    CONSTRAINT accident_order_fk
        FOREIGN KEY (order_id) REFERENCES orders (id)
            ON UPDATE CASCADE ON DELETE SET NULL
);

--CarRentalTime
CREATE TABLE IF NOT EXISTS car_rental_time
(
    id                SERIAL PRIMARY KEY,
    order_id          BIGINT    NOT NULL UNIQUE,
    start_rental_date TIMESTAMP NOT NULL,
    end_rental_date   TIMESTAMP NOT NULL,
    CONSTRAINT carrentaltime_orders_fk
        FOREIGN KEY (order_id) REFERENCES orders (id)
            ON UPDATE CASCADE ON DELETE SET NULL
);

--UserDetail
CREATE TABLE IF NOT EXISTS user_details
(
    id                SERIAL PRIMARY KEY,
    user_id           BIGINT       NOT NULL,
    name              VARCHAR(128) NOT NULL,
    surname           VARCHAR(128) NOT NULL,
    address           VARCHAR(255) NOT NULL,
    phone             VARCHAR(32)  NOT NULL,
    birthday          TIMESTAMP    NOT NULL,
    registration_date TIMESTAMP    NOT NULL DEFAULT now(),
    CONSTRAINT userdetails_user_fk
        FOREIGN KEY (user_id) REFERENCES users (id)
            ON UPDATE CASCADE ON DELETE SET NULL
);

--DriverLicense
CREATE TABLE IF NOT EXISTS driver_license
(
    id              SERIAL PRIMARY KEY,
    user_details_id BIGINT      NOT NULL,
    number          varchar(32) NOT NULL UNIQUE,
    issue_date      TIMESTAMP   NOT NULL,
    expired_date    TIMESTAMP   NOT NULL,
    CONSTRAINT driverlicense_user_details_fk
        FOREIGN KEY (user_details_id) REFERENCES user_details (id)
            ON UPDATE CASCADE ON DELETE SET NULL
);

-- rollback drop all;
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
