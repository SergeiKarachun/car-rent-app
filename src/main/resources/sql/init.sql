-- rollback drop all;
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

--Brand
CREATE TABLE IF NOT EXISTS brand
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

--Model
CREATE TABLE IF NOT EXISTS model
(
    id           BIGSERIAL PRIMARY KEY,
    brand_id     BIGINT       NOT NULL,
    name         VARCHAR(255) NOT NULL UNIQUE,
    transmission VARCHAR(128),
    engine_type  VARCHAR(128),
    CONSTRAINT model_brand_fk
        FOREIGN KEY (brand_id) REFERENCES brand (id)
            ON UPDATE CASCADE ON DELETE SET NULL
);

--Category
CREATE TABLE IF NOT EXISTS category
(
    id    BIGSERIAL PRIMARY KEY,
    name  VARCHAR(255)   NOT NULL UNIQUE              DEFAULT 'ECONOMY',
    price NUMERIC(10, 2) NOT NULL CHECK ( price > 0 ) DEFAULT '50'
);

--User
CREATE TABLE IF NOT EXISTS users
(
    id       BIGSERIAL PRIMARY KEY,
    name     VARCHAR(255) NOT NULL UNIQUE,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(32)  NOT NULL DEFAULT 'user'
);

--Car
CREATE TABLE IF NOT EXISTS car
(
    id          BIGSERIAL PRIMARY KEY,
    brand_id    BIGINT,
    model_id    BIGINT,
    category_id BIGINT,
    color       VARCHAR(255),
    year        INTEGER,
    car_number  VARCHAR(16),
    vin         VARCHAR(255) NOT NULL UNIQUE,
    repaired    BOOLEAN DEFAULT 'TRUE',
    image       TEXT,
    CONSTRAINT car_brand_fk
        FOREIGN KEY (brand_id) REFERENCES brand (id)
            ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT car_model_fk
        FOREIGN KEY (model_id) REFERENCES model (id)
            ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT car_category_fk
        FOREIGN KEY (category_id) REFERENCES category (id)
            ON UPDATE CASCADE ON DELETE SET NULL
);

--Order
CREATE TABLE IF NOT EXISTS orders
(
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT         NOT NULL,
    car_id       BIGINT         NOT NULL,
    date         DATE      NOT NULL DEFAULT now(),
    passport     VARCHAR(128)   NOT NULL,
    order_status VARCHAR(32)    NOT NULL,
    sum          NUMERIC(10, 2) NOT NULL,
    CONSTRAINT order_user_fk
        FOREIGN KEY (user_id) REFERENCES users (id)
            ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT order_car_fk
        FOREIGN KEY (car_id) REFERENCES car (id)
            ON UPDATE CASCADE ON DELETE SET NULL
);

--Accident
CREATE TABLE IF NOT EXISTS accident
(
    id            BIGSERIAL PRIMARY KEY,
    order_id      BIGINT    NOT NULL,
    accident_date TIMESTAMP NOT NULL,
    description   TEXT,
    damage        NUMERIC(10, 2),
    CONSTRAINT accident_order_fk
        FOREIGN KEY (order_id) REFERENCES orders (id)
            ON UPDATE CASCADE ON DELETE SET NULL
);

--CarRentalTime
CREATE TABLE IF NOT EXISTS rental_time
(
    id                BIGSERIAL PRIMARY KEY,
    order_id          BIGINT    NOT NULL UNIQUE,
    start_rental_date TIMESTAMP NOT NULL,
    end_rental_date   TIMESTAMP NOT NULL,
    CONSTRAINT rentaltime_order_fk
        FOREIGN KEY (order_id) REFERENCES orders (id)
            ON UPDATE CASCADE ON DELETE SET NULL
);

--UserDetail
CREATE TABLE IF NOT EXISTS user_details
(
    id                BIGSERIAL PRIMARY KEY,
    user_id           BIGINT       NOT NULL,
    name              VARCHAR(128) NOT NULL,
    surname           VARCHAR(128) NOT NULL,
    address           VARCHAR(255) NOT NULL,
    phone             VARCHAR(32)  NOT NULL,
    birthday          DATE    NOT NULL,
    registration_date DATE    NOT NULL DEFAULT now(),
    CONSTRAINT userdetails_user_fk
        FOREIGN KEY (user_id) REFERENCES users (id)
            ON UPDATE CASCADE ON DELETE SET NULL
);

--DriverLicense
CREATE TABLE IF NOT EXISTS driver_license
(
    id              BIGSERIAL PRIMARY KEY,
    user_details_id BIGINT      NOT NULL,
    number          varchar(32) NOT NULL UNIQUE,
    issue_date      DATE   NOT NULL,
    expiration_date DATE   NOT NULL,
    CONSTRAINT driverlicense_userdetails_fk
        FOREIGN KEY (user_details_id) REFERENCES user_details (id)
            ON UPDATE CASCADE ON DELETE SET NULL
);


