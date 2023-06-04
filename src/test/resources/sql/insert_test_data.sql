INSERT INTO brand (id, name)
VALUES (1, 'Audi'),
       (2, 'BMW'),
       (3, 'Skoda'),
       (4, 'Tesla');
SELECT SETVAL('brand_id_seq', (SELECT MAX(id) FROM brand));


INSERT INTO model (id, brand_id, name, transmission, engine_type)
VALUES (1, (SELECT b.id FROM brand b WHERE b.name = 'Audi'), 'A6', 'MANUAL', 'DIESEL'),
       (2, (SELECT b.id FROM brand b WHERE b.name = 'Audi'), 'A8', 'AUTOMATIC', 'DIESEL'),
       (3, (SELECT b.id FROM brand b WHERE b.name = 'BMW'), '525', 'AUTOMATIC', 'DIESEL'),
       (4, (SELECT b.id FROM brand b WHERE b.name = 'BMW'), 'M5', 'AUTOMATIC', 'FUEL'),
       (5, (SELECT b.id FROM brand b WHERE b.name = 'Skoda'), 'Rapid', 'MANUAL', 'DIESEL'),
       (6, (SELECT b.id FROM brand b WHERE b.name = 'Skoda'), 'Superb', 'MANUAL', 'FUEL'),
       (7, (SELECT b.id FROM brand b WHERE b.name = 'Tesla'), 'Model S', 'AUTOMATIC', 'ELECTRIC'),
       (8, (SELECT b.id FROM brand b WHERE b.name = 'Tesla'), 'Model X', 'AUTOMATIC', 'ELECTRIC');
SELECT SETVAL('model_id_seq', (SELECT MAX(id) FROM model));


INSERT INTO category (id, name, price)
VALUES (1, 'ECONOMY', 50),
       (2, 'STANDART', 80),
       (3, 'SPORT', 120),
       (4, 'BUSINESS', 150),
       (5, 'LUXURY', 200);
SELECT SETVAL('category_id_seq', (SELECT MAX(id) FROM category));


INSERT INTO users (id, username, email, password, role)
VALUES (1, 'admin', 'admin@gmail.com', '{noop}admin', 'ADMIN'),
       (2, 'client', 'client@gmail.com', '{noop}client', 'CLIENT');
SELECT SETVAL('users_id_seq', (SELECT MAX(id) FROM users));

INSERT INTO user_details (id, user_id, name, surname, address, phone, birthday, registration_date)
VALUES (1, (SELECT u.id FROM users u WHERE email = 'admin@gmail.com'), 'Sergey', 'Ivanov', 'Minsk',
        '+37533 1234567', '1990-08-10 00:00:00', '2022-09-22 00:00:00'),
       (2, (SELECT u.id FROM users u WHERE email = 'client@gmail.com'), 'Petr', 'Petrov', 'Minsk',
        '+37533 2345678', '1989-02-05 00:00:00', '2021-04-18 00:00:00');
SELECT SETVAL('user_details_id_seq', (SELECT MAX(id) FROM user_details));

INSERT INTO driver_license (id, user_details_id, number, issue_date, expiration_date)
VALUES (1, (SELECT u.id FROM user_details u WHERE phone = '+37533 1234567'), 'AB12345',
        '2015-03-02', '2025-03-01'),
       (2, (SELECT u.id FROM user_details u WHERE phone = '+37533 2345678'), 'AB23456',
        '2014-12-02', '2024-12-01');
SELECT SETVAL('driver_license_id_seq', (SELECT MAX(id) FROM driver_license));

INSERT INTO car (id, brand_id, model_id, category_id, color, year, car_number, vin)
VALUES (1, (SELECT b.id FROM brand b WHERE name = 'Audi'),
        (SELECT m.id FROM model m WHERE name = 'A6'), (SELECT c.id FROM category c WHERE name = 'STANDART'),
        'BLACK', '2020', '7594AB-7', '1D8GT58KX8W109715'),
       (2, (SELECT b.id FROM brand b WHERE name = 'Audi'),
        (SELECT m.id FROM model m WHERE name = 'A8'), (SELECT c.id FROM category c WHERE name = 'BUSINESS'),
        'BLACK', '2021', '7256AC-7', '1GNDS13S282151069'),
       (3, (SELECT b.id FROM brand b WHERE name = 'BMW'),
        (SELECT m.id FROM model m WHERE name = '525'), (SELECT c.id FROM category c WHERE name = 'STANDART'),
        'GREY', '2019', '7234AB-7', '1LNHM94R89G601547'),
       (4, (SELECT b.id FROM brand b WHERE name = 'BMW'),
        (SELECT m.id FROM model m WHERE name = 'M5'), (SELECT c.id FROM category c WHERE name = 'SPORT'),
        'YELLOW', '2021', '1864CK-7', 'VF9KZXCY6RTXV8781'),
       (5, (SELECT b.id FROM brand b WHERE name = 'Skoda'),
        (SELECT m.id FROM model m WHERE name = 'Rapid'), (SELECT c.id FROM category c WHERE name = 'ECONOMY'),
        'BLACK', '2022', '9879DS-7', 'VNK3P74XJ20ES4546'),
       (6, (SELECT b.id FROM brand b WHERE name = 'Skoda'),
        (SELECT m.id FROM model m WHERE name = 'Superb'), (SELECT c.id FROM category c WHERE name = 'BUSINESS'),
        'BLACK', '2018', '4656YU-7', 'SB1BDM98EK0114129'),
       (7, (SELECT b.id FROM brand b WHERE name = 'Tesla'),
        (SELECT m.id FROM model m WHERE name = 'Model S'), (SELECT c.id FROM category c WHERE name = 'SPORT'),
        'RED', '2019', '1324YU-7', 'VNKRE876C4K1K3940'),
       (8, (SELECT b.id FROM brand b WHERE name = 'Tesla'),
        (SELECT m.id FROM model m WHERE name = 'Model X'), (SELECT c.id FROM category c WHERE name = 'LUXURY'),
        'RED', '2022', '1232UI-7', 'TW11AGAPP2WDR9845');
SELECT SETVAL('car_id_seq', (SELECT MAX(id) FROM car));



INSERT INTO orders (id, user_id, car_id, date, passport, order_status, sum)
VALUES (1, 1, 1, TO_TIMESTAMP('2022-07-10 11:30:00', 'YYYY-MM-DD HH24:MI:SS'),
        'MP1234567', 'APPROVED', 150),
       (2, 2, 3, TO_TIMESTAMP('2022-09-28 12:30:00', 'YYYY-MM-DD HH24:MI:SS'),
        'MP1234567', 'CONFIRMATION', 200),
       (3, 1, 5, TO_TIMESTAMP('2022-12-01 09:30:00', 'YYYY-MM-DD HH24:MI:SS'),
        'MP1234567', 'COMPLETED', 300),
       (4, 2, 3, TO_TIMESTAMP('2022-12-01 19:30:00', 'YYYY-MM-DD HH24:MI:SS'),
        'MP1234567', 'COMPLETED', 100);
SELECT SETVAL('orders_id_seq', (SELECT MAX(id) FROM orders));


INSERT INTO rental_time (id, order_id, start_rental_date, end_rental_date)
VALUES (1, 1,
        TO_TIMESTAMP('2022-07-11 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
        TO_TIMESTAMP('2022-07-12 23:59:00', 'YYYY-MM-DD HH24:MI:SS')),
       (2, 2,
        TO_TIMESTAMP('2022-10-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
        TO_TIMESTAMP('2022-10-02 23:59:00', 'YYYY-MM-DD HH24:MI:SS')),
       (3, 3,
        TO_TIMESTAMP('2022-12-12 00:00:00', 'YYYY-MM-DD HH24:MI:SS'),
        TO_TIMESTAMP('2022-12-13 23:59:00', 'YYYY-MM-DD HH24:MI:SS'));
SELECT SETVAL('rental_time_id_seq', (SELECT MAX(id) FROM rental_time));

INSERT INTO accident (id, order_id, accident_date, description, damage)
VALUES (1, 1, TO_TIMESTAMP('2022-10-01 12:35:00', 'YYYY-MM-DD HH24:MI:SS'),
        'speeding fine', 50),
       (2, 3, TO_TIMESTAMP('2022-12-12 21:15:00', 'YYYY-MM-DD HH24:MI:SS'),
        'broke wheel', 25);
SELECT SETVAL('accident_id_seq', (SELECT MAX(id) FROM accident));
