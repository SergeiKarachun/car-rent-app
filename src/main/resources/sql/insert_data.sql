INSERT INTO brand (id, name)
VALUES (1, 'Audi'),
       (2, 'Bentley'),
       (3, 'BMW'),
       (4, 'Ferrari'),
       (5, 'Ford'),
       (6, 'Honda'),
       (7, 'Kia'),
       (8, 'Mercedes'),
       (9, 'Nissan'),
       (10, 'Porsche'),
       (11, 'Renault'),
       (12, 'Skoda'),
       (13, 'Tesla'),
       (14, 'Toyota'),
       (15, 'Volkswagen');
SELECT SETVAL('brand_id_seq', (SELECT MAX(id) FROM brand));


INSERT INTO model (id, brand_id, name, transmission, engine_type)
VALUES (1, (SELECT b.id FROM brand b WHERE b.name = 'Audi'), 'A6', 'AUTOMATIC', 'DIESEL'),
       (2, (SELECT b.id FROM brand b WHERE b.name = 'Audi'), 'A8', 'AUTOMATIC', 'DIESEL'),
       (3, (SELECT b.id FROM brand b WHERE b.name = 'BMW'), '525', 'AUTOMATIC', 'DIESEL'),
       (4, (SELECT b.id FROM brand b WHERE b.name = 'BMW'), 'M5', 'AUTOMATIC', 'FUEL'),
       (5, (SELECT b.id FROM brand b WHERE b.name = 'Ferrari'), '458', 'AUTOMATIC', 'FUEL'),
       (6, (SELECT b.id FROM brand b WHERE b.name = 'Ford'), 'Mustang', 'AUTOMATIC', 'FUEL'),
       (7, (SELECT b.id FROM brand b WHERE b.name = 'Honda'), 'CR-V', 'AUTOMATIC', 'FUEL'),
       (8, (SELECT b.id FROM brand b WHERE b.name = 'Honda'), 'Civic', 'AUTOMATIC', 'FUEL'),
       (9, (SELECT b.id FROM brand b WHERE b.name = 'Honda'), 'Accord', 'AUTOMATIC', 'FUEL'),
       (10, (SELECT b.id FROM brand b WHERE b.name = 'Kia'), 'Ceed', 'MANUAL', 'DIESEL'),
       (11, (SELECT b.id FROM brand b WHERE b.name = 'Kia'), 'Sportage', 'AUTOMATIC', 'FUEL'),
       (12, (SELECT b.id FROM brand b WHERE b.name = 'Mercedes'), 'E200', 'MANUAL', 'DIESEL'),
       (13, (SELECT b.id FROM brand b WHERE b.name = 'Mercedes'), 'E63AMG', 'AUTOMATIC', 'FUEL'),
       (14, (SELECT b.id FROM brand b WHERE b.name = 'Mercedes'), 'S300', 'AUTOMATIC', 'DIESEL'),
       (15, (SELECT b.id FROM brand b WHERE b.name = 'Nissan'), 'Almera', 'MANUAL', 'FUEL'),
       (16, (SELECT b.id FROM brand b WHERE b.name = 'Nissan'), 'Navara', 'MANUAL', 'DIESEL'),
       (17, (SELECT b.id FROM brand b WHERE b.name = 'Porsche'), '911', 'AUTOMATIC', 'FUEL'),
       (18, (SELECT b.id FROM brand b WHERE b.name = 'Porsche'), 'Panamera', 'AUTOMATIC', 'FUEL'),
       (19, (SELECT b.id FROM brand b WHERE b.name = 'Porsche'), 'Taycan', 'AUTOMATIC', 'ELECTRIC'),
       (20, (SELECT b.id FROM brand b WHERE b.name = 'Renault'), 'Logan', 'MANUAL', 'FUEL'),
       (21, (SELECT b.id FROM brand b WHERE b.name = 'Renault'), 'Duster', 'MANUAL', 'FUEL'),
       (22, (SELECT b.id FROM brand b WHERE b.name = 'Skoda'), 'Rapid', 'MANUAL', 'DIESEL'),
       (23, (SELECT b.id FROM brand b WHERE b.name = 'Skoda'), 'Superb', 'AUTOMATIC', 'DIESEL'),
       (24, (SELECT b.id FROM brand b WHERE b.name = 'Tesla'), 'Model S', 'AUTOMATIC', 'ELECTRIC'),
       (25, (SELECT b.id FROM brand b WHERE b.name = 'Tesla'), 'Model X', 'AUTOMATIC', 'ELECTRIC'),
       (26, (SELECT b.id FROM brand b WHERE b.name = 'Toyota'), 'RAV4', 'MANUAL', 'FUEL'),
       (27, (SELECT b.id FROM brand b WHERE b.name = 'Volkswagen'), 'Polo', 'MANUAL', 'FUEL'),
       (28, (SELECT b.id FROM brand b WHERE b.name = 'Volkswagen'), 'Passat', 'AUTOMATIC', 'DIESEL'),
       (29, (SELECT b.id FROM brand b WHERE b.name = 'Volkswagen'), 'Golf', 'AUTOMATIC', 'FUEL'),
       (30, (SELECT b.id FROM brand b WHERE b.name = 'Volkswagen'), 'Touareg', 'AUTOMATIC', 'FUEL');
SELECT SETVAL('model_id_seq', (SELECT MAX(id) FROM model));


INSERT INTO category (id, name, price)
VALUES (1, 'ECONOMY', 50),
       (2, 'STANDART', 80),
       (3, 'SPORT', 120),
       (4, 'BUSINESS', 150),
       (5, 'LUXURY', 200);
SELECT SETVAL('category_id_seq', (SELECT MAX(id) FROM category));


INSERT INTO users (id, username, email, password, role)
VALUES (1, 'admin', 'admin@gmail.com', 'admin', 'ADMIN'),
       (2, 'client', 'client@gmail.com', 'client', 'CLIENT');
SELECT SETVAL('users_id_seq', (SELECT MAX(id) FROM users));

INSERT INTO user_details (id, user_id, name, surname, address, phone, birthday, registration_date)
VALUES (1, (SELECT u.id FROM users u WHERE email = 'admin@gmail.com'), 'Sergey', 'Ivanov', 'Minsk',
        '+37533 1234567', '1990-08-10 00:00:00', now()),
       (2, (SELECT u.id FROM users u WHERE email = 'client@gmail.com'), 'Petr', 'Petrov', 'Minsk',
        '+37533 2345678', '1989-02-05 00:00:00', now());
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
       (5, (SELECT b.id FROM brand b WHERE name = 'Ferrari'),
        (SELECT m.id FROM model m WHERE name = '458'), (SELECT c.id FROM category c WHERE name = 'LUXURY'),
        'RED', '2018', '1326BH-7', 'TRUHGS9W0B3681847'),
       (6, (SELECT b.id FROM brand b WHERE name = 'Ford'),
        (SELECT m.id FROM model m WHERE name = 'Mustang'), (SELECT c.id FROM category c WHERE name = 'SPORT'),
        'RED', '2019', '2632KP-7', 'WAUYJB132AR7P6468'),
       (7, (SELECT b.id FROM brand b WHERE name = 'Honda'),
        (SELECT m.id FROM model m WHERE name = 'CR-V'), (SELECT c.id FROM category c WHERE name = 'STANDART'),
        'BLACK', '2020', '9784HP-7', 'WA14DPLAFH0NW9773'),
       (8, (SELECT b.id FROM brand b WHERE name = 'Honda'),
        (SELECT m.id FROM model m WHERE name = 'Civic'), (SELECT c.id FROM category c WHERE name = 'ECONOMY'),
        'WHITE', '2020', '1234KL-7', 'SADLDTB0XT02H3163'),
       (9, (SELECT b.id FROM brand b WHERE name = 'Honda'),
        (SELECT m.id FROM model m WHERE name = 'Accord'), (SELECT c.id FROM category c WHERE name = 'STANDART'),
        'BLACK', '2020', '7514BB-7', 'SAJRV6N4MX36E0069'),
       (10, (SELECT b.id FROM brand b WHERE name = 'Kia'),
        (SELECT m.id FROM model m WHERE name = 'Ceed'), (SELECT c.id FROM category c WHERE name = 'ECONOMY'),
        'GREEN', '2022', '7656AB-7', 'SAJKRM7FGLYCV3181'),
       (11, (SELECT b.id FROM brand b WHERE name = 'Kia'),
        (SELECT m.id FROM model m WHERE name = 'Sportage'), (SELECT c.id FROM category c WHERE name = 'STANDART'),
        'WHITE', '2020', '7213AB-7', 'SDB11VRH0JZ9R1430'),
       (12, (SELECT b.id FROM brand b WHERE name = 'Mercedes'),
        (SELECT m.id FROM model m WHERE name = 'E200'), (SELECT c.id FROM category c WHERE name = 'STANDART'),
        'GREY', '2021', '4594AB-7', 'SDB94S7XVFFBD1481'),
       (13, (SELECT b.id FROM brand b WHERE name = 'Mercedes'),
        (SELECT m.id FROM model m WHERE name = 'E63AMG'), (SELECT c.id FROM category c WHERE name = 'SPORT'),
        'RED', '2020', '1594AB-7', 'WP0Z4W4SBYEDR6606'),
       (14, (SELECT b.id FROM brand b WHERE name = 'Mercedes'),
        (SELECT m.id FROM model m WHERE name = 'S300'), (SELECT c.id FROM category c WHERE name = 'BUSINESS'),
        'BLACK', '2020', '6594AB-7', 'WP0RR7EBL6J3Y5833'),
       (15, (SELECT b.id FROM brand b WHERE name = 'Nissan'),
        (SELECT m.id FROM model m WHERE name = 'Almera'), (SELECT c.id FROM category c WHERE name = 'ECONOMY'),
        'SILVER', '2018', '5594AB-7', 'WP07R829T68856850'),
       (16, (SELECT b.id FROM brand b WHERE name = 'Nissan'),
        (SELECT m.id FROM model m WHERE name = 'Navara'), (SELECT c.id FROM category c WHERE name = 'STANDART'),
        'SILVER', '2017', '766HI-7', 'WP1L4B4RFL4NZ4691'),
       (17, (SELECT b.id FROM brand b WHERE name = 'Porsche'),
        (SELECT m.id FROM model m WHERE name = '911'), (SELECT c.id FROM category c WHERE name = 'SPORT'),
        'RED', '2020', '1603HN-7', 'TW11VF08P9H7U2325'),
       (18, (SELECT b.id FROM brand b WHERE name = 'Porsche'),
        (SELECT m.id FROM model m WHERE name = 'Panamera'), (SELECT c.id FROM category c WHERE name = 'BUSINESS'),
        'BLACK', '2022', '6882PK-7', 'VNKSH5NTSC58M5782'),
       (19, (SELECT b.id FROM brand b WHERE name = 'Porsche'),
        (SELECT m.id FROM model m WHERE name = 'Taycan'), (SELECT c.id FROM category c WHERE name = 'LUXURY'),
        'WHITE', '2021', '9875BN-7', 'SB189VYHTTVK95304'),
       (20, (SELECT b.id FROM brand b WHERE name = 'Renault'),
        (SELECT m.id FROM model m WHERE name = 'Logan'), (SELECT c.id FROM category c WHERE name = 'ECONOMY'),
        'SILVER', '2020', '5452HU-7', 'VNKATDR7VN0DF0135'),
       (21, (SELECT b.id FROM brand b WHERE name = 'Renault'),
        (SELECT m.id FROM model m WHERE name = 'Duster'), (SELECT c.id FROM category c WHERE name = 'STANDART'),
        'WHITE', '2020', '6568JK-7', 'SB1885SDCT2MT2431'),
       (22, (SELECT b.id FROM brand b WHERE name = 'Skoda'),
        (SELECT m.id FROM model m WHERE name = 'Rapid'), (SELECT c.id FROM category c WHERE name = 'ECONOMY'),
        'BLACK', '2022', '9879DS-7', 'VNK3P74XJ20ES4546'),
       (23, (SELECT b.id FROM brand b WHERE name = 'Skoda'),
        (SELECT m.id FROM model m WHERE name = 'Superb'), (SELECT c.id FROM category c WHERE name = 'BUSINESS'),
        'BLACK', '2018', '4656YU-7', 'SB1BDM98EK0114129'),
       (24, (SELECT b.id FROM brand b WHERE name = 'Tesla'),
        (SELECT m.id FROM model m WHERE name = 'Model S'), (SELECT c.id FROM category c WHERE name = 'SPORT'),
        'RED', '2019', '1324YU-7', 'VNKRE876C4K1K3940'),
       (25, (SELECT b.id FROM brand b WHERE name = 'Tesla'),
        (SELECT m.id FROM model m WHERE name = 'Model X'), (SELECT c.id FROM category c WHERE name = 'BUSINESS'),
        'RED', '2022', '1232UI-7', 'TW11AGAPP2WDR9845'),
       (26, (SELECT b.id FROM brand b WHERE name = 'Toyota'),
        (SELECT m.id FROM model m WHERE name = 'RAV4'), (SELECT c.id FROM category c WHERE name = 'STANDART'),
        'RED', '2021', '8656KL-7', 'SB1E72SUFKH0X3650'),
       (27, (SELECT b.id FROM brand b WHERE name = 'Volkswagen'),
        (SELECT m.id FROM model m WHERE name = 'Polo'), (SELECT c.id FROM category c WHERE name = 'ECONOMY'),
        'BLACK', '2019', '6566JK-7', 'VNKA5EZM4F58S0785'),
       (28, (SELECT b.id FROM brand b WHERE name = 'Volkswagen'),
        (SELECT m.id FROM model m WHERE name = 'Passat'), (SELECT c.id FROM category c WHERE name = 'STANDART'),
        'SILVER', '2018', '3299KL-7', 'TW1FXJS3P86LA0986'),
       (29, (SELECT b.id FROM brand b WHERE name = 'Volkswagen'),
        (SELECT m.id FROM model m WHERE name = 'Golf'), (SELECT c.id FROM category c WHERE name = 'SPORT'),
        'RED', '2019', '6523LP-7', 'XLE2V9KGMW68Y8297'),
       (30, (SELECT b.id FROM brand b WHERE name = 'Volkswagen'),
        (SELECT m.id FROM model m WHERE name = 'Touareg'), (SELECT c.id FROM category c WHERE name = 'BUSINESS'),
        'BLACK', '2019', '6455KL-7', 'YS21X7GGNX2ZJ1935');
SELECT SETVAL('car_id_seq', (SELECT MAX(id) FROM car));



INSERT INTO orders (id, user_id, car_id, date, passport, order_status, sum)
VALUES (1, 2, 30, TO_TIMESTAMP('2022-07-10 11:30:00', 'YYYY-MM-DD HH24:MI:SS'),
        'MP1234567', 'APPROVED', 150),
       (2, 2, 22, TO_TIMESTAMP('2022-09-28 12:30:00', 'YYYY-MM-DD HH24:MI:SS'),
        'MP1234567', 'CONFIRMATION', 200),
       (3, 2, 17, TO_TIMESTAMP('2022-12-01 09:30:00', 'YYYY-MM-DD HH24:MI:SS'),
        'MP1234567', 'COMPLETED', 300);
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
VALUES (1, 2, TO_TIMESTAMP('2022-10-01 12:35:00', 'YYYY-MM-DD HH24:MI:SS'),
        'speeding fine', 50),
       (2, 3, TO_TIMESTAMP('2022-12-12 21:15:00', 'YYYY-MM-DD HH24:MI:SS'),
        'broke wheel', 25);
SELECT SETVAL('accident_id_seq', (SELECT MAX(id) FROM accident));
