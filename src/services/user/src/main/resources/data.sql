INSERT INTO public.image (id, created_by, created_date, last_modified_by, last_modified_date, name) VALUES (1, 'igor', '2024-05-24 14:46:28.153522 +00:00', 'igor', '2024-05-24 14:46:28.153522 +00:00', 'd08e937b-1c07-4a79-a0ba-72a39bc676b3_1716561977.jpg');
INSERT INTO public.image (id, created_by, created_date, last_modified_by, last_modified_date, name) VALUES (2, 'igor', '2024-06-11 12:11:32.597090 +00:00', 'igor', '2024-06-11 12:11:32.597090 +00:00', '17c9ba7e-4668-4a6d-a57e-d0306e49d975_1718107892.jpg');
INSERT INTO public.image (id, created_by, created_date, last_modified_by, last_modified_date, name) VALUES (3, 'igor', '2024-06-11 12:11:36.671645 +00:00', 'igor', '2024-06-11 12:11:36.671645 +00:00', '8aa73bd4-8cdc-418c-83cd-2eceb2cdb3b0_1718107896.jpg');
INSERT INTO public.image (id, created_by, created_date, last_modified_by, last_modified_date, name) VALUES (4, 'igor', '2024-06-11 12:11:51.098475 +00:00', 'igor', '2024-06-11 12:11:51.098475 +00:00', '7ca03dd2-13fc-4f87-bbbc-87df5e2b4859_1718107911.profileImage');
INSERT INTO public.image (id, created_by, created_date, last_modified_by, last_modified_date, name) VALUES (5, 'igor', '2024-06-11 12:11:59.139475 +00:00', 'igor', '2024-06-11 12:11:59.139475 +00:00', 'c824503b-0be4-4113-b43b-5e30abd8361b_1718107919.png');
INSERT INTO public.image (id, created_by, created_date, last_modified_by, last_modified_date, name) VALUES (6, 'igor', '2024-06-11 12:12:19.722312 +00:00', 'igor', '2024-06-11 12:12:19.722312 +00:00', 'b5b30d21-2361-4e6c-b468-c15098dedf3c_1718107939.jpg');
INSERT INTO public.image (id, created_by, created_date, last_modified_by, last_modified_date, name) VALUES (52, 'igi.l.1999@gmail.com', '2024-08-24 16:54:32.401724 +00:00', 'igi.l.1999@gmail.com', '2024-08-24 16:54:32.401724 +00:00', '6783444f-0193-458e-bbfe-0ffe63a4da58_1724518472.jpg');


INSERT INTO public.eventer_user (id, created_by, created_date, last_modified_by, last_modified_date, password, username, image_id, city, name) VALUES (402, 'SYSTEM', '2024-05-08 15:01:29.319354 +00:00', 'igi.l.1999@gmail.com', '2024-08-24 16:54:32.403301 +00:00', '$argon2id$v=19$m=16384,t=2,p=1$KoqBLZaFUt7Ljxyu6r2NjQ$YrzPF/TGiO/esyQ5OktaLuoLyJ8IrD52uBzQpKLK9rA', 'igi.l.1999@gmail.com', 52, 'Novi Sad', 'Igor Lukic');



INSERT INTO public.category_subscription (id, category_id, user_id) VALUES (1, 3, 402);
INSERT INTO public.category_subscription (id, category_id, user_id) VALUES (108, 1, 402);
INSERT INTO public.category_subscription (id, category_id, user_id) VALUES (152, 4, 402);


INSERT INTO public.comment (id, created_by, created_date, last_modified_by, last_modified_date, event_id, text, user_id) VALUES (403, 'igi.l.1999@gmail.com', '2024-09-08 10:09:17.258545 +00:00', 'igi.l.1999@gmail.com', '2024-09-08 10:09:17.258545 +00:00', 2, 'Mogu da dodam komentar', 402);
INSERT INTO public.comment (id, created_by, created_date, last_modified_by, last_modified_date, event_id, text, user_id) VALUES (402, 'igi.l.1999@gmail.com', '2024-09-08 10:06:17.891108 +00:00', 'igi.l.1999@gmail.com', '2024-09-08 10:09:28.024149 +00:00', 2, 'Mogu da izemenim komentar', 402);


INSERT INTO public.event_subscription (id, created_by, created_date, last_modified_by, last_modified_date, event_id, user_id) VALUES (53, 'igor', '2024-07-20 13:27:59.635621 +00:00', 'igor', '2024-07-20 13:27:59.635621 +00:00', 2, 402);
INSERT INTO public.event_subscription (id, created_by, created_date, last_modified_by, last_modified_date, event_id, user_id) VALUES (102, 'igi.l.1999@gmail.com', '2024-08-12 14:03:29.895930 +00:00', 'igi.l.1999@gmail.com', '2024-08-12 14:03:29.895930 +00:00', 14, 402);
INSERT INTO public.event_subscription (id, created_by, created_date, last_modified_by, last_modified_date, event_id, user_id) VALUES (152, 'igi.l.1999@gmail.com', '2024-09-08 09:24:20.767501 +00:00', 'igi.l.1999@gmail.com', '2024-09-08 09:24:20.767501 +00:00', 5, 402);
INSERT INTO public.event_subscription (id, created_by, created_date, last_modified_by, last_modified_date, event_id, user_id) VALUES (202, 'igi.l.1999@gmail.com', '2024-09-08 09:39:14.119309 +00:00', 'igi.l.1999@gmail.com', '2024-09-08 09:39:14.119309 +00:00', 3, 402);
INSERT INTO public.event_subscription (id, created_by, created_date, last_modified_by, last_modified_date, event_id, user_id) VALUES (203, 'igi.l.1999@gmail.com', '2024-09-08 09:39:51.481610 +00:00', 'igi.l.1999@gmail.com', '2024-09-08 09:39:51.481610 +00:00', 0, 402);