--
-- PostgreSQL database dump
--

-- Dumped from database version 16.0 (Debian 16.0-1.pgdg120+1)
-- Dumped by pg_dump version 16.0

-- Started on 2023-11-12 13:47:38 UTC

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 215 (class 1259 OID 16384)
-- Name: item_list_entry; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.item_list_entry (
    id bigint NOT NULL,
    quantity bigint NOT NULL,
    item_id bigint,
    order_id bigint,
    warehouse_id bigint
);


ALTER TABLE public.item_list_entry OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 16387)
-- Name: item_list_entry_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.item_list_entry_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.item_list_entry_seq OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 16388)
-- Name: items; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.items (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    size bigint NOT NULL
);


ALTER TABLE public.items OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 16391)
-- Name: items_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.items_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.items_seq OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16392)
-- Name: orders; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.orders (
    id bigint NOT NULL,
    created_by uuid,
    from_warehouse_id bigint,
    to_warehouse_id bigint
);


ALTER TABLE public.orders OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16395)
-- Name: orders_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.orders_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.orders_seq OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16396)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id uuid NOT NULL,
    full_name character varying(255) NOT NULL,
    username character varying(255) NOT NULL,
    warehouse_id bigint,
    roles character varying(255) NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 16401)
-- Name: warehouses; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.warehouses (
    id bigint NOT NULL,
    capacity bigint NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.warehouses OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 16404)
-- Name: warehouses_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.warehouses_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.warehouses_seq OWNER TO postgres;

--
-- TOC entry 3384 (class 0 OID 16384)
-- Dependencies: 215
-- Data for Name: item_list_entry; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.item_list_entry (id, quantity, item_id, order_id, warehouse_id) FROM stdin;
1	10	1	\N	\N
2	100	2	\N	2
3	20	3	\N	2
4	100	4	\N	2
5	15	5	\N	2
6	50	6	\N	2
7	20	7	\N	3
8	10	8	\N	3
9	30	9	\N	3
10	200	10	\N	4
11	120	11	\N	4
12	100	12	\N	4
13	50	13	\N	4
14	100	14	\N	5
15	50	15	\N	5
16	200	16	\N	5
\.


--
-- TOC entry 3386 (class 0 OID 16388)
-- Dependencies: 217
-- Data for Name: items; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.items (id, name, size) FROM stdin;
1	Cola	10
2	Budvar	2
3	Borovicka klasik	3
4	Piwko	2
5	Becherovka	3
6	Jadierka	1
7	Koleso	100
8	Volant	10
9	Stierac	5
10	Siberia	1
11	Velo	1
12	LM	2
13	Camel modre	2
14	Strepsils	2
15	Escapele	2
16	Mucosolvan	1
\.


--
-- TOC entry 3388 (class 0 OID 16392)
-- Dependencies: 219
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.orders (id, created_by, from_warehouse_id, to_warehouse_id) FROM stdin;
\.


--
-- TOC entry 3390 (class 0 OID 16396)
-- Dependencies: 221
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, full_name, username, warehouse_id, roles) FROM stdin;
e44d3e07-7d97-452f-9adc-84ded0db2d8c	Ivan Administrator	admin	\N	SCOPE_openid;SCOPE_profile;SCOPE_email;ROLE_default-roles-invetory_system;ROLE_offline_access;ROLE_ADMIN;ROLE_uma_authorization
29f7ba35-ad60-4a45-a461-71b60b495bf6	Samuel Virag	feshak	\N	SCOPE_openid;SCOPE_profile;SCOPE_email;ROLE_default-roles-invetory_system;ROLE_offline_access;ROLE_uma_authorization
\.


--
-- TOC entry 3391 (class 0 OID 16401)
-- Dependencies: 222
-- Data for Name: warehouses; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.warehouses (id, capacity, name) FROM stdin;
2	1000	Lahodky
3	5000	Autodiely
4	850	Tabak
5	500	Lekaren
\.


--
-- TOC entry 3398 (class 0 OID 0)
-- Dependencies: 216
-- Name: item_list_entry_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.item_list_entry_seq', 51, true);


--
-- TOC entry 3399 (class 0 OID 0)
-- Dependencies: 218
-- Name: items_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.items_seq', 51, true);


--
-- TOC entry 3400 (class 0 OID 0)
-- Dependencies: 220
-- Name: orders_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.orders_seq', 1, false);


--
-- TOC entry 3401 (class 0 OID 0)
-- Dependencies: 223
-- Name: warehouses_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.warehouses_seq', 51, true);


--
-- TOC entry 3223 (class 2606 OID 16406)
-- Name: item_list_entry item_list_entry_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item_list_entry
    ADD CONSTRAINT item_list_entry_pkey PRIMARY KEY (id);


--
-- TOC entry 3225 (class 2606 OID 16408)
-- Name: items items_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.items
    ADD CONSTRAINT items_pkey PRIMARY KEY (id);


--
-- TOC entry 3227 (class 2606 OID 16410)
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);


--
-- TOC entry 3229 (class 2606 OID 16412)
-- Name: users uk_r43af9ap4edm43mmtq01oddj6; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username);


--
-- TOC entry 3231 (class 2606 OID 16414)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 3233 (class 2606 OID 16416)
-- Name: warehouses warehouses_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.warehouses
    ADD CONSTRAINT warehouses_pkey PRIMARY KEY (id);


--
-- TOC entry 3237 (class 2606 OID 16417)
-- Name: orders fk1udrqm8ovcbg00o1crajw5igm; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fk1udrqm8ovcbg00o1crajw5igm FOREIGN KEY (to_warehouse_id) REFERENCES public.warehouses(id);


--
-- TOC entry 3234 (class 2606 OID 16422)
-- Name: item_list_entry fk3u09frv4xf2xu64wtls3svlpq; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item_list_entry
    ADD CONSTRAINT fk3u09frv4xf2xu64wtls3svlpq FOREIGN KEY (order_id) REFERENCES public.orders(id);


--
-- TOC entry 3235 (class 2606 OID 16427)
-- Name: item_list_entry fkeop2ckflwfvkgkaoao5kg2g9n; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item_list_entry
    ADD CONSTRAINT fkeop2ckflwfvkgkaoao5kg2g9n FOREIGN KEY (warehouse_id) REFERENCES public.warehouses(id);


--
-- TOC entry 3238 (class 2606 OID 16432)
-- Name: orders fkhi0nkwblr0uclsh3h1egia1w5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fkhi0nkwblr0uclsh3h1egia1w5 FOREIGN KEY (from_warehouse_id) REFERENCES public.warehouses(id);


--
-- TOC entry 3240 (class 2606 OID 16437)
-- Name: users fkpfeijlxk6hivbyuvedan09sqk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT fkpfeijlxk6hivbyuvedan09sqk FOREIGN KEY (warehouse_id) REFERENCES public.warehouses(id);


--
-- TOC entry 3236 (class 2606 OID 16442)
-- Name: item_list_entry fks3fghrj7r4cdxdgv0k44wi90a; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.item_list_entry
    ADD CONSTRAINT fks3fghrj7r4cdxdgv0k44wi90a FOREIGN KEY (item_id) REFERENCES public.items(id);


--
-- TOC entry 3239 (class 2606 OID 16447)
-- Name: orders fktjwuphstqm46uffgc7l1r27a9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fktjwuphstqm46uffgc7l1r27a9 FOREIGN KEY (created_by) REFERENCES public.users(id);


-- Completed on 2023-11-12 13:47:38 UTC

--
-- PostgreSQL database dump complete
--

