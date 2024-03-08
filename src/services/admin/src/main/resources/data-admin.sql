--
-- PostgreSQL database dump
--

-- Dumped from database version 16.1 (Debian 16.1-1.pgdg110+1)
-- Dumped by pg_dump version 16.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Data for Name: admin; Type: TABLE DATA; Schema: public; Owner: eventer_admin
--

INSERT INTO public.admin VALUES (2, 'SYSTEM', '2024-02-22 12:33:34.92752+00', 'SYSTEM', '2024-02-22 12:33:34.92752+00', '$argon2id$v=19$m=16384,t=2,p=1$is6L2tFxTOZn9N3FarXz+Q$wKWGMCkpAs3HfgXPwyW/S/yvsXKrOaOpvuJu2MpAfeQ', 'EVENT_MANAGER', 'admin2');
INSERT INTO public.admin VALUES (52, 'SYSTEM', '2024-02-22 12:34:44.185799+00', 'SYSTEM', '2024-02-22 12:34:44.185799+00', '$argon2id$v=19$m=16384,t=2,p=1$TeDxEChghIkQexSUHKOQWA$UMBG1PdQjwbjDHqk0kxgYNgNapLJMODNGNx48H0//+0', 'EVENT_MANAGER', 'admin3');
INSERT INTO public.admin VALUES (1, 'SYSTEM', '2024-02-22 12:31:19.929776+00', 'SYSTEM', '2024-02-22 12:31:19.929776+00', '$argon2id$v=19$m=16384,t=2,p=1$e7SKFC/g5KAx27CNFEnkFg$IhHwtOY4xapr+o0Yp7e4iFcyGK56POdnw++5SteOpxs', 'ADMINISTRATOR', 'superadmin');
INSERT INTO public.admin VALUES (102, 'SYSTEM', '2024-02-22 13:40:03.049079+00', 'SYSTEM', '2024-02-22 13:40:03.049079+00', '$argon2id$v=19$m=16384,t=2,p=1$mleMBwCeeVAslE+r6rTkHQ$7IAm+yJHi5KscnxfKPgIjedcQmG2K9wJCZykQFR/5Y0', 'EVENT_MANAGER', 'admin4');
INSERT INTO public.admin VALUES (152, 'SYSTEM', '2024-02-22 13:41:06.057011+00', 'SYSTEM', '2024-02-22 13:41:06.057011+00', '$argon2id$v=19$m=16384,t=2,p=1$OQPJuzvkeIwEcTKwYp9r+A$cRNZkR2QcayGbCjxNRVubYCzPx7e2Okr0lbWAjeH1kU', 'EVENT_MANAGER', 'admin5');


--
-- Data for Name: event; Type: TABLE DATA; Schema: public; Owner: eventer_admin
--

INSERT INTO public.event VALUES (552, 'SYSTEM', '2024-02-18 14:47:21.0694+00', 'SYSTEM', '2024-02-18 14:47:21.0694+00', 'Test event 3', 'Novi Sad', 'Event 3', 'CLEAR', '2024-02-18 12:10:03+00');
INSERT INTO public.event VALUES (602, 'SYSTEM', '2024-02-18 14:51:45.409793+00', 'SYSTEM', '2024-02-18 14:51:45.409793+00', 'Test event 3', 'Novi Sad', 'Event 3', 'CLEAR', '2024-02-18 12:10:03+00');


--
-- Data for Name: event_category; Type: TABLE DATA; Schema: public; Owner: eventer_admin
--

INSERT INTO public.event_category VALUES (1, 'SYSTEM', '2024-02-17 18:58:44.385514+00', 'SYSTEM', '2024-02-17 18:58:44.385514+00', 'Test category 1', 'Category 1');
INSERT INTO public.event_category VALUES (2, 'SYSTEM', '2024-02-17 18:58:53.54879+00', 'SYSTEM', '2024-02-17 18:58:53.54879+00', 'Test category 2', 'Category 2');
INSERT INTO public.event_category VALUES (52, 'admin2', '2024-02-23 09:45:09.358829+00', 'admin2', '2024-02-23 09:45:09.358829+00', 'Test category 4', 'Category 4');
INSERT INTO public.event_category VALUES (102, 'admin2', '2024-02-28 13:52:40.881118+00', 'admin2', '2024-02-28 13:52:40.881118+00', 'Test category 5', 'Category 5');


--
-- Data for Name: image; Type: TABLE DATA; Schema: public; Owner: eventer_admin
--

INSERT INTO public.image VALUES (1253, 'SYSTEM', '2024-02-18 14:47:21.05991+00', 'SYSTEM', '2024-02-18 14:47:21.05991+00', 'f45509f4-cd4c-42b5-9520-93290269166e_1708267640.jpg', 552);
INSERT INTO public.image VALUES (1252, 'SYSTEM', '2024-02-18 14:47:21.032636+00', 'SYSTEM', '2024-02-18 14:47:21.032636+00', '026bcfb6-c89b-41d0-9467-88382b97b233_1708267640.jpg', 552);
INSERT INTO public.image VALUES (1302, 'SYSTEM', '2024-02-18 14:51:45.376249+00', 'SYSTEM', '2024-02-18 14:51:45.376249+00', '2a24eff5-e6c6-43ea-be83-6ed983ca92f6_1708267905.jpg', 602);
INSERT INTO public.image VALUES (1303, 'SYSTEM', '2024-02-18 14:51:45.40086+00', 'SYSTEM', '2024-02-18 14:51:45.40086+00', '2be0ba43-8e95-40f5-8cad-d5e5d67a0e28_1708267905.jpg', 602);


--
-- Data for Name: linked_categories; Type: TABLE DATA; Schema: public; Owner: eventer_admin
--

INSERT INTO public.linked_categories VALUES (552, 1);
INSERT INTO public.linked_categories VALUES (602, 1);


--
-- Name: admin_seq; Type: SEQUENCE SET; Schema: public; Owner: eventer_admin
--

SELECT pg_catalog.setval('public.admin_seq', 201, true);


--
-- Name: event_category_seq; Type: SEQUENCE SET; Schema: public; Owner: eventer_admin
--

SELECT pg_catalog.setval('public.event_category_seq', 151, true);


--
-- Name: event_seq; Type: SEQUENCE SET; Schema: public; Owner: eventer_admin
--

SELECT pg_catalog.setval('public.event_seq', 651, true);


--
-- Name: image_seq; Type: SEQUENCE SET; Schema: public; Owner: eventer_admin
--

SELECT pg_catalog.setval('public.image_seq', 1351, true);


--
-- PostgreSQL database dump complete
--

