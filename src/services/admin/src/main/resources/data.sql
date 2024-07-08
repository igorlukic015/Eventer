INSERT INTO event_category (id, name, description, created_by, created_date, last_modified_by, last_modified_date)
VALUES
    (1, 'Koncert', 'Muzika koja se izvodi uživo', 'SYSTEM', '2024-02-17 18:58:44.385514+00', 'SYSTEM', '2024-02-17 18:58:44.385514+00'),
    (2, 'Film', 'Premijere filmova', 'SYSTEM', '2024-02-17 18:58:44.385514+00', 'SYSTEM', '2024-02-17 18:58:44.385514+00'),
    (3, 'Galerija', 'Umetničke izložbe i događaji u galerijama', 'SYSTEM', '2024-02-17 18:58:44.385514+00', 'SYSTEM', '2024-02-17 18:58:44.385514+00'),
    (4, 'Pozorište', 'Predstave u pozorištima', 'SYSTEM', '2024-02-17 18:58:44.385514+00', 'SYSTEM', '2024-02-17 18:58:44.385514+00'),
    (5, 'Radionica', 'Edukativne radionice', 'SYSTEM', '2024-02-17 18:58:44.385514+00', 'SYSTEM', '2024-02-17 18:58:44.385514+00');


INSERT INTO event (id, title, description, location, date, weather_condition_availability, created_by, created_date, last_modified_by, last_modified_date)
VALUES
    (1, 'Rok koncert', 'Veče roka', 'Novi Sad', '2024-08-10T19:00:00Z', 'CLEAR', 'system', now(), 'system', now()),
    (2, 'Umetnička izložba', 'Otvaranje izložbe moderne umetnosti', 'Belgrade', '2024-07-15T10:00:00Z', 'RAIN;CLEAR;DRIZZLE;SNOW', 'system', now(), 'system', now()),
    (3, 'Film na otvorenom', 'Veče gledanja starih filmova', 'Novi Sad', '2024-07-20T20:00:00Z', 'CLEAR', 'system', now(), 'system', now()),
    (4, 'Šekspirova predstava', 'Hamlet u pozorištu', 'Belgrade', '2024-08-01T18:00:00Z', 'RAIN;CLEAR;DRIZZLE;SNOW', 'system', now(), 'system', now()),
    (5, 'Radionica fotografije', 'Fotografisanje za početnike', 'Valjevo', '2024-07-12T09:00:00Z', 'RAIN;CLEAR;DRIZZLE;SNOW', 'system', now(), 'system', now()),
    (6, 'Džez festival', 'Džez muzika uživo', 'Šabac', '2024-07-30T17:00:00Z', 'CLEAR', 'system', now(), 'system', now()),
    (7, 'Filmski festival naučne fantastike', 'Gledanje klasičnih filmova naučne fantastike', 'Novi Sad', '2024-08-05T15:00:00Z', 'RAIN;CLEAR;DRIZZLE;SNOW', 'system', now(), 'system', now()),
    (8, 'Galerija skulptura', 'Izložba skulptura domaćih umetnika', 'Novi Sad', '2024-07-25T14:00:00Z', 'RAIN;CLEAR;DRIZZLE;SNOW', 'system', now(), 'system', now()),
    (9, 'Stendap komedija', 'Veče stendap komedije', 'Kragujevac', '2024-08-08T19:30:00Z', 'RAIN;CLEAR;DRIZZLE;SNOW', 'system', now(), 'system', now()),
    (10, 'Radionica kuvanja', 'Početnička radionica italijanske kuhinje', 'Novi Sad', '2024-07-18T11:00:00Z', 'RAIN;CLEAR;DRIZZLE;SNOW', 'system', now(), 'system', now()),
    (11, 'Koncert klasične muzike', 'Orkestarski performans', 'Belgrade', '2024-07-22T20:00:00Z', 'CLEAR', 'system', now(), 'system', now()),
    (12, 'Premijera dokumentarnog filma', 'Premijera dokumentarnog filma o životinjama', 'Novi Sad', '2024-08-02T16:00:00Z', 'RAIN;CLEAR;DRIZZLE;SNOW', 'system', now(), 'system', now()),
    (13, 'Umetnički vašar', 'Vašar domaćih umetnika', 'Novi Sad', '2024-07-19T09:00:00Z', 'RAIN;CLEAR;DRIZZLE;SNOW', 'system', now(), 'system', now()),
    (14, 'Dramska predstava', 'Predstava mladih glumaca', 'Novi Sad', '2024-08-03T19:00:00Z', 'RAIN;CLEAR;DRIZZLE;SNOW', 'system', now(), 'system', now()),
    (15, 'Radionica plesa', 'Napredna radionica plesa', 'Novi Sad', '2024-07-14T10:00:00Z', 'RAIN;CLEAR;DRIZZLE;SNOW', 'system', now(), 'system', now());

INSERT INTO linked_categories (event_id, event_category_id)
VALUES
    (1, 1), (1, 2), -- Rock Concert -> Concerts, Movies
    (2, 3), -- Art Exhibition -> Galleries
    (3, 2), -- Outdoor Movie -> Movies
    (4, 4), -- Shakespeare Play -> Theater
    (5, 5), -- Photography Workshop -> Workshops
    (6, 1), -- Jazz Festival -> Concerts
    (7, 2), -- Science Fiction Film Festival -> Movies
    (8, 3), -- Sculpture Gallery -> Galleries
    (9, 4), -- Improv Comedy Night -> Theater
    (10, 5), -- Cooking Workshop -> Workshops
    (11, 1), -- Classical Music Concert -> Concerts
    (12, 2), -- Documentary Film Screening -> Movies
    (13, 3), -- Local Art Fair -> Galleries
    (14, 4), -- Drama Play -> Theater
    (15, 5); -- Dance Workshop -> Workshops




INSERT INTO admin (id, created_by, created_date, last_modified_by, last_modified_date, password, role, username)
VALUES
    (2, 'SYSTEM', '2024-02-22 12:33:34.92752+00', 'SYSTEM', '2024-02-22 12:33:34.92752+00', '$argon2id$v=19$m=16384,t=2,p=1$is6L2tFxTOZn9N3FarXz+Q$wKWGMCkpAs3HfgXPwyW/S/yvsXKrOaOpvuJu2MpAfeQ', 'EVENT_MANAGER', 'admin2'),
    (1, 'SYSTEM', '2024-02-22 12:31:19.929776+00', 'SYSTEM', '2024-02-22 12:31:19.929776+00', '$argon2id$v=19$m=16384,t=2,p=1$e7SKFC/g5KAx27CNFEnkFg$IhHwtOY4xapr+o0Yp7e4iFcyGK56POdnw++5SteOpxs', 'ADMINISTRATOR', 'superadmin');


INSERT INTO image (created_date, event_id, id, last_modified_date, created_by, last_modified_by, name)
VALUES
    ('2024-07-08 15:56:58.555439 +00:00', 1, 1, '2024-07-08 15:56:58.555439 +00:00', 'admin2', 'admin2', '054e218c-b34e-447f-864a-e0f5f2a1f35f_1720454218.jpg'),
    ('2024-07-08 15:56:58.555439 +00:00', 1, 2, '2024-07-08 15:56:58.555439 +00:00', 'admin2', 'admin2', '98216f56-4866-4de7-91d8-86effb9247f9.jpg'),
    ('2024-07-08 15:56:58.555439 +00:00', 2, 3, '2024-07-08 15:56:58.555439 +00:00', 'admin2', 'admin2', '52bb1e25-53d8-469a-99d7-69329011d3af.jpg'),
    ('2024-07-08 15:56:58.555439 +00:00', 2, 4, '2024-07-08 15:56:58.555439 +00:00', 'admin2', 'admin2', '78c2ede5-9cae-49a5-b1c1-c81d74266a57.jpg'),
    ('2024-07-08 15:56:58.555439 +00:00', 3, 5, '2024-07-08 15:56:58.555439 +00:00', 'admin2', 'admin2', 'd1965774-2844-440a-a437-572e72abb398.jpg'),
    ('2024-07-08 15:56:58.555439 +00:00', 4, 6, '2024-07-08 15:56:58.555439 +00:00', 'admin2', 'admin2', '5c132320-ff73-4551-be57-6cc643408e2f.jpg'),
    ('2024-07-08 15:56:58.555439 +00:00', 5, 7, '2024-07-08 15:56:58.555439 +00:00', 'admin2', 'admin2', '70d990d4-6f54-41a0-9ab7-3ca0e2fbf0aa.jpg'),
    ('2024-07-08 15:56:58.555439 +00:00', 6, 8, '2024-07-08 15:56:58.555439 +00:00', 'admin2', 'admin2', '1de3c75a-fd68-4a2c-9f62-b6fac4d48b53.jpg'),
    ('2024-07-08 15:56:58.555439 +00:00', 7, 9, '2024-07-08 15:56:58.555439 +00:00', 'admin2', 'admin2', 'd1965774-2844-440a-a437-572e72abb398.jpg'),
    ('2024-07-08 15:56:58.555439 +00:00', 8, 10, '2024-07-08 15:56:58.555439 +00:00', 'admin2', 'admin2', '52bb1e25-53d8-469a-99d7-69329011d3af.jpg'),
    ('2024-07-08 15:56:58.555439 +00:00', 9, 11, '2024-07-08 15:56:58.555439 +00:00', 'admin2', 'admin2', '10b5c619-129d-4282-bb78-e5bcb3cb81f7.jpg'),
    ('2024-07-08 15:56:58.555439 +00:00', 10, 12, '2024-07-08 15:56:58.555439 +00:00', 'admin2', 'admin2', '70d990d4-6f54-41a0-9ab7-3ca0e2fbf0aa.jpg'),
    ('2024-07-08 15:56:58.555439 +00:00', 11, 13, '2024-07-08 15:56:58.555439 +00:00', 'admin2', 'admin2', '98216f56-4866-4de7-91d8-86effb9247f9.jpg'),
    ('2024-07-08 15:56:58.555439 +00:00', 12, 14, '2024-07-08 15:56:58.555439 +00:00', 'admin2', 'admin2', '78c2ede5-9cae-49a5-b1c1-c81d74266a57.jpg'),
    ('2024-07-08 15:56:58.555439 +00:00', 13, 15, '2024-07-08 15:56:58.555439 +00:00', 'admin2', 'admin2', 'd1965774-2844-440a-a437-572e72abb398.jpg');

