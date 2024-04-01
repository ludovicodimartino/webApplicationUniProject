--
-- PostgreSQL database dump
--

-- Dumped from database version 16.2
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
-- Name: assessment; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA assessment;


ALTER SCHEMA assessment OWNER TO postgres;

--
-- Name: accountType; Type: TYPE; Schema: assessment; Owner: postgres
--

CREATE TYPE assessment."accountType" AS ENUM (
    'USER',
    'ADMIN'
);


ALTER TYPE assessment."accountType" OWNER TO postgres;

--
-- Name: car_circuit_suitability_function(); Type: FUNCTION; Schema: assessment; Owner: postgres
--

CREATE FUNCTION assessment.car_circuit_suitability_function() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
IF (
   SELECT COUNT(*) as result
   FROM assessment."carCircuitSuitability" AS a
   WHERE a."carType" = (
	   SELECT c."type"
	   FROM assessment."car" AS c
	   WHERE c."brand" = NEW."carBrand" AND c."model" = NEW."carModel"
   ) AND a."circuitType" = (
	   SELECT g."type"
	   FROM assessment."circuit" AS g
	   WHERE g."name" = NEW."circuit"
   )
  ) < 1 THEN
  RAISE EXCEPTION 'Car and Circuit are not suitable';
END IF;
RETURN NEW;
END;$$;


ALTER FUNCTION assessment.car_circuit_suitability_function() OWNER TO postgres;

--
-- Name: car_circuit_suitability_function(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.car_circuit_suitability_function() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
IF (
   SELECT COUNT(*) as result
   FROM assessment."carCircuitSuitability" AS a
   WHERE a."carType" = (
	   SELECT c."type"
	   FROM assessment."car" AS c
	   WHERE c."brand" = NEW."carBrand" AND c."model" = NEW."carModel"
   ) AND a."circuitType" = (
	   SELECT g."type"
	   FROM assessment."circuit" AS g
	   WHERE g."name" = NEW."circuit"
   )
  ) < 1 THEN
  RAISE EXCEPTION 'Car and Circuit are not suitable';
END IF;
RETURN NEW;
END;
$$;


ALTER FUNCTION public.car_circuit_suitability_function() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: account; Type: TABLE; Schema: assessment; Owner: postgres
--

CREATE TABLE assessment.account (
    id integer NOT NULL,
    name character varying(100) NOT NULL,
    surname character varying(100) NOT NULL,
    address text NOT NULL,
    email text NOT NULL,
    password text NOT NULL,
    type assessment."accountType" NOT NULL
);


ALTER TABLE assessment.account OWNER TO postgres;

--
-- Name: account_id_seq; Type: SEQUENCE; Schema: assessment; Owner: postgres
--

ALTER TABLE assessment.account ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME assessment.account_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 1000000000
    CACHE 1
);


--
-- Name: car; Type: TABLE; Schema: assessment; Owner: postgres
--

CREATE TABLE assessment.car (
    brand character varying(100) NOT NULL,
    model character varying(100) NOT NULL,
    type character varying(100) NOT NULL,
    horsepower integer NOT NULL,
    "0-100" character varying(10) NOT NULL,
    "maxSpeed" character varying(10) NOT NULL,
    description text NOT NULL
);


ALTER TABLE assessment.car OWNER TO postgres;

--
-- Name: carCircuitSuitability; Type: TABLE; Schema: assessment; Owner: postgres
--

CREATE TABLE assessment."carCircuitSuitability" (
    "carType" character varying(100) NOT NULL,
    "circuitType" character varying(100) NOT NULL
);


ALTER TABLE assessment."carCircuitSuitability" OWNER TO postgres;

--
-- Name: carType; Type: TABLE; Schema: assessment; Owner: postgres
--

CREATE TABLE assessment."carType" (
    name character varying(100) NOT NULL
);


ALTER TABLE assessment."carType" OWNER TO postgres;

--
-- Name: circuit; Type: TABLE; Schema: assessment; Owner: postgres
--

CREATE TABLE assessment.circuit (
    name character varying(100) NOT NULL,
    type character varying(100) NOT NULL,
    length character varying(100) NOT NULL,
    "cornersNumver" integer NOT NULL,
    address text NOT NULL,
    description text NOT NULL,
    "lapPrice" integer NOT NULL
);


ALTER TABLE assessment.circuit OWNER TO postgres;

--
-- Name: circuitType; Type: TABLE; Schema: assessment; Owner: postgres
--

CREATE TABLE assessment."circuitType" (
    name character varying(100) NOT NULL
);


ALTER TABLE assessment."circuitType" OWNER TO postgres;

--
-- Name: favourite; Type: TABLE; Schema: assessment; Owner: postgres
--

CREATE TABLE assessment.favourite (
    circuit character varying(100) NOT NULL,
    "carBrand" character varying(100) NOT NULL,
    "carModel" character varying(100) NOT NULL,
    "user" integer NOT NULL,
    "createdAt" timestamp with time zone NOT NULL
);


ALTER TABLE assessment.favourite OWNER TO postgres;

--
-- Name: order; Type: TABLE; Schema: assessment; Owner: postgres
--

CREATE TABLE assessment."order" (
    account integer NOT NULL,
    date date NOT NULL,
    "carBrand" character varying(100),
    "carModel" character varying(100),
    circuit character varying(100),
    "createdAt" timestamp with time zone NOT NULL,
    "nLaps" integer NOT NULL,
    price integer NOT NULL
);


ALTER TABLE assessment."order" OWNER TO postgres;

--
-- Data for Name: account; Type: TABLE DATA; Schema: assessment; Owner: postgres
--

COPY assessment.account (id, name, surname, address, email, password, type) FROM stdin;
\.


--
-- Data for Name: car; Type: TABLE DATA; Schema: assessment; Owner: postgres
--

COPY assessment.car (brand, model, type, horsepower, "0-100", "maxSpeed", description) FROM stdin;
\.


--
-- Data for Name: carCircuitSuitability; Type: TABLE DATA; Schema: assessment; Owner: postgres
--

COPY assessment."carCircuitSuitability" ("carType", "circuitType") FROM stdin;
\.


--
-- Data for Name: carType; Type: TABLE DATA; Schema: assessment; Owner: postgres
--

COPY assessment."carType" (name) FROM stdin;
\.


--
-- Data for Name: circuit; Type: TABLE DATA; Schema: assessment; Owner: postgres
--

COPY assessment.circuit (name, type, length, "cornersNumver", address, description, "lapPrice") FROM stdin;
\.


--
-- Data for Name: circuitType; Type: TABLE DATA; Schema: assessment; Owner: postgres
--

COPY assessment."circuitType" (name) FROM stdin;
\.


--
-- Data for Name: favourite; Type: TABLE DATA; Schema: assessment; Owner: postgres
--

COPY assessment.favourite (circuit, "carBrand", "carModel", "user", "createdAt") FROM stdin;
\.


--
-- Data for Name: order; Type: TABLE DATA; Schema: assessment; Owner: postgres
--

COPY assessment."order" (account, date, "carBrand", "carModel", circuit, "createdAt", "nLaps", price) FROM stdin;
\.


--
-- Name: account_id_seq; Type: SEQUENCE SET; Schema: assessment; Owner: postgres
--

SELECT pg_catalog.setval('assessment.account_id_seq', 1, true);


--
-- Name: account account_pkey; Type: CONSTRAINT; Schema: assessment; Owner: postgres
--

ALTER TABLE ONLY assessment.account
    ADD CONSTRAINT account_pkey PRIMARY KEY (id);


--
-- Name: carCircuitSuitability carCircuitSuitability_pkey; Type: CONSTRAINT; Schema: assessment; Owner: postgres
--

ALTER TABLE ONLY assessment."carCircuitSuitability"
    ADD CONSTRAINT "carCircuitSuitability_pkey" PRIMARY KEY ("carType", "circuitType");


--
-- Name: carType carType_pkey; Type: CONSTRAINT; Schema: assessment; Owner: postgres
--

ALTER TABLE ONLY assessment."carType"
    ADD CONSTRAINT "carType_pkey" PRIMARY KEY (name);


--
-- Name: car car_pkey; Type: CONSTRAINT; Schema: assessment; Owner: postgres
--

ALTER TABLE ONLY assessment.car
    ADD CONSTRAINT car_pkey PRIMARY KEY (brand, model);


--
-- Name: circuitType circuitType_pkey; Type: CONSTRAINT; Schema: assessment; Owner: postgres
--

ALTER TABLE ONLY assessment."circuitType"
    ADD CONSTRAINT "circuitType_pkey" PRIMARY KEY (name);


--
-- Name: circuit circuit_pkey; Type: CONSTRAINT; Schema: assessment; Owner: postgres
--

ALTER TABLE ONLY assessment.circuit
    ADD CONSTRAINT circuit_pkey PRIMARY KEY (name);


--
-- Name: favourite favourite_pkey; Type: CONSTRAINT; Schema: assessment; Owner: postgres
--

ALTER TABLE ONLY assessment.favourite
    ADD CONSTRAINT favourite_pkey PRIMARY KEY (circuit, "carBrand", "carModel", "user");


--
-- Name: order order_pkey; Type: CONSTRAINT; Schema: assessment; Owner: postgres
--

ALTER TABLE ONLY assessment."order"
    ADD CONSTRAINT order_pkey PRIMARY KEY (account, date);


--
-- Name: favourite check_car_type_suitability_trigger; Type: TRIGGER; Schema: assessment; Owner: postgres
--

CREATE TRIGGER check_car_type_suitability_trigger BEFORE INSERT ON assessment.favourite FOR EACH ROW EXECUTE FUNCTION assessment.car_circuit_suitability_function();


--
-- Name: order check_car_type_suitability_trigger; Type: TRIGGER; Schema: assessment; Owner: postgres
--

CREATE TRIGGER check_car_type_suitability_trigger BEFORE INSERT ON assessment."order" FOR EACH ROW EXECUTE FUNCTION assessment.car_circuit_suitability_function();


--
-- Name: carCircuitSuitability carCircuitSuitability_carType_fkey; Type: FK CONSTRAINT; Schema: assessment; Owner: postgres
--

ALTER TABLE ONLY assessment."carCircuitSuitability"
    ADD CONSTRAINT "carCircuitSuitability_carType_fkey" FOREIGN KEY ("carType") REFERENCES assessment."carType"(name);


--
-- Name: carCircuitSuitability carCircuitSuitability_circuitType_fkey; Type: FK CONSTRAINT; Schema: assessment; Owner: postgres
--

ALTER TABLE ONLY assessment."carCircuitSuitability"
    ADD CONSTRAINT "carCircuitSuitability_circuitType_fkey" FOREIGN KEY ("circuitType") REFERENCES assessment."circuitType"(name);


--
-- Name: car circuit_type_fkey; Type: FK CONSTRAINT; Schema: assessment; Owner: postgres
--

ALTER TABLE ONLY assessment.car
    ADD CONSTRAINT circuit_type_fkey FOREIGN KEY (type) REFERENCES assessment."carType"(name);


--
-- Name: circuit circuit_type_fkey; Type: FK CONSTRAINT; Schema: assessment; Owner: postgres
--

ALTER TABLE ONLY assessment.circuit
    ADD CONSTRAINT circuit_type_fkey FOREIGN KEY (type) REFERENCES assessment."circuitType"(name);


--
-- Name: favourite favourite_car_fkey; Type: FK CONSTRAINT; Schema: assessment; Owner: postgres
--

ALTER TABLE ONLY assessment.favourite
    ADD CONSTRAINT favourite_car_fkey FOREIGN KEY ("carBrand", "carModel") REFERENCES assessment.car(brand, model) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: favourite favourite_circuit_fkey; Type: FK CONSTRAINT; Schema: assessment; Owner: postgres
--

ALTER TABLE ONLY assessment.favourite
    ADD CONSTRAINT favourite_circuit_fkey FOREIGN KEY (circuit) REFERENCES assessment.circuit(name) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: favourite favourite_user_fkey; Type: FK CONSTRAINT; Schema: assessment; Owner: postgres
--

ALTER TABLE ONLY assessment.favourite
    ADD CONSTRAINT favourite_user_fkey FOREIGN KEY ("user") REFERENCES assessment.account(id);


--
-- Name: order order_account_fkey; Type: FK CONSTRAINT; Schema: assessment; Owner: postgres
--

ALTER TABLE ONLY assessment."order"
    ADD CONSTRAINT order_account_fkey FOREIGN KEY (account) REFERENCES assessment.account(id) NOT VALID;


--
-- Name: order order_car_fkey; Type: FK CONSTRAINT; Schema: assessment; Owner: postgres
--

ALTER TABLE ONLY assessment."order"
    ADD CONSTRAINT order_car_fkey FOREIGN KEY ("carBrand", "carModel") REFERENCES assessment.car(brand, model) ON UPDATE CASCADE ON DELETE SET NULL NOT VALID;


--
-- Name: order order_circuit_fkey; Type: FK CONSTRAINT; Schema: assessment; Owner: postgres
--

ALTER TABLE ONLY assessment."order"
    ADD CONSTRAINT order_circuit_fkey FOREIGN KEY (circuit) REFERENCES assessment.circuit(name) ON UPDATE CASCADE ON DELETE SET NULL NOT VALID;


--
-- PostgreSQL database dump complete
--

