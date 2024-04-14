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
-- Name: assessment; Type: SCHEMA; Schema: -; Owner: wacaruser
--

CREATE SCHEMA assessment;


ALTER SCHEMA assessment OWNER TO wacaruser;

--
-- Name: accountType; Type: TYPE; Schema: assessment; Owner: wacaruser
--

CREATE TYPE assessment."accountType" AS ENUM (
    'USER',
    'ADMIN'
);


ALTER TYPE assessment."accountType" OWNER TO wacaruser;

--
-- Name: check_car_function(); Type: FUNCTION; Schema: assessment; Owner: wacaruser
--

CREATE FUNCTION assessment.check_car_function() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
IF (NEW."horsepower") < 0 THEN
	RAISE EXCEPTION 'Horsepower cannot be negative';
END IF;
IF (NEW."0-100") < 0 THEN
	RAISE EXCEPTION 'Acceleration cannot be negative';
END IF;
IF (NEW."maxSpeed") < 0 THEN
	RAISE EXCEPTION 'Max speed cannot be negative';
END IF;
RETURN NEW;
END;$$;


ALTER FUNCTION assessment.check_car_function() OWNER TO wacaruser;

--
-- Name: check_circuit_function(); Type: FUNCTION; Schema: assessment; Owner: wacaruser
--

CREATE FUNCTION assessment.check_circuit_function() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
IF (NEW."lapPrice") < 0 THEN
	RAISE EXCEPTION 'Price for a lap cannot be negative';
END IF;
IF (NEW."cornersNumber") < 0 THEN
	RAISE EXCEPTION 'Number of corners cannot be negative';
END IF;
IF (NEW."length") < 0 THEN
	RAISE EXCEPTION 'Circuit length cannot be negative';
END IF;
RETURN NEW;
END$$;


ALTER FUNCTION assessment.check_circuit_function() OWNER TO wacaruser;

--
-- Name: check_order_function(); Type: FUNCTION; Schema: assessment; Owner: wacaruser
--

CREATE FUNCTION assessment.check_order_function() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
IF (NEW."nLaps") < 0 THEN
	RAISE EXCEPTION 'Number of laps cannot be negative';
END IF;
IF (
	SELECT c."lapPrice" * NEW."nLaps"
	FROM assessment."circuit" AS c
	WHERE c."name" = NEW."circuit"
	) <> NEW."price" THEN
	RAISE EXCEPTION 'Price is not correct';
END IF;
IF (
	SELECT a."type"
	FROM assessment."account" as a
	WHERE a."email" = NEW.account
	) <> 'USER' THEN
	RAISE EXCEPTION 'Admin cannot create an order';
END IF;
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


ALTER FUNCTION assessment.check_order_function() OWNER TO wacaruser;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: account; Type: TABLE; Schema: assessment; Owner: wacaruser
--

CREATE TABLE assessment.account (
    email text NOT NULL,
    name character varying(100) NOT NULL,
    surname character varying(100) NOT NULL,
    address text NOT NULL,
    password text NOT NULL,
    type assessment."accountType" NOT NULL
);


ALTER TABLE assessment.account OWNER TO wacaruser;

--
-- Name: car; Type: TABLE; Schema: assessment; Owner: wacaruser
--

CREATE TABLE assessment.car (
    brand character varying(100) NOT NULL,
    model character varying(100) NOT NULL,
    type character varying(100) NOT NULL,
    horsepower integer NOT NULL,
    "0-100" numeric(10,2) NOT NULL,
    "maxSpeed" integer NOT NULL,
    description text NOT NULL,
    available boolean NOT NULL,
    image bytea,
    imageMediaType text
);


ALTER TABLE assessment.car OWNER TO wacaruser;

--
-- Name: carCircuitSuitability; Type: TABLE; Schema: assessment; Owner: wacaruser
--

CREATE TABLE assessment."carCircuitSuitability" (
    "carType" character varying(100) NOT NULL,
    "circuitType" character varying(100) NOT NULL
);


ALTER TABLE assessment."carCircuitSuitability" OWNER TO wacaruser;

--
-- Name: carType; Type: TABLE; Schema: assessment; Owner: wacaruser
--

CREATE TABLE assessment."carType" (
    name character varying(100) NOT NULL
);


ALTER TABLE assessment."carType" OWNER TO wacaruser;

--
-- Name: circuit; Type: TABLE; Schema: assessment; Owner: wacaruser
--

CREATE TABLE assessment.circuit (
    name character varying(100) NOT NULL,
    type character varying(100) NOT NULL,
    length integer NOT NULL,
    "cornersNumber" integer NOT NULL,
    address text NOT NULL,
    description text NOT NULL,
    "lapPrice" integer NOT NULL,
    available boolean NOT NULL,
    image bytea,
    imageMediaType text
);


ALTER TABLE assessment.circuit OWNER TO wacaruser;

--
-- Name: circuitType; Type: TABLE; Schema: assessment; Owner: wacaruser
--

CREATE TABLE assessment."circuitType" (
    name character varying(100) NOT NULL
);


ALTER TABLE assessment."circuitType" OWNER TO wacaruser;

--
-- Name: favourite; Type: TABLE; Schema: assessment; Owner: wacaruser
--

CREATE TABLE assessment.favourite (
    circuit character varying(100) NOT NULL,
    "carBrand" character varying(100) NOT NULL,
    "carModel" character varying(100) NOT NULL,
    "createdAt" timestamp with time zone NOT NULL,
    account text NOT NULL
);


ALTER TABLE assessment.favourite OWNER TO wacaruser;

--
-- Name: order; Type: TABLE; Schema: assessment; Owner: wacaruser
--

CREATE TABLE assessment."order" (
    account text NOT NULL,
    date date NOT NULL,
    "carBrand" character varying(100) NOT NULL,
    "carModel" character varying(100) NOT NULL,
    circuit character varying(100) NOT NULL,
    "createdAt" timestamp with time zone NOT NULL,
    "nLaps" integer NOT NULL,
    price integer NOT NULL    
);


ALTER TABLE assessment."order" OWNER TO wacaruser;

--
-- Data for Name: account; Type: TABLE DATA; Schema: assessment; Owner: wacaruser
--

COPY assessment.account (email, name, surname, address, password, type) FROM stdin;
\.


--
-- Data for Name: car; Type: TABLE DATA; Schema: assessment; Owner: wacaruser
--

COPY assessment.car (brand, model, type, horsepower, "0-100", "maxSpeed", description, available, image) FROM stdin;
\.


--
-- Data for Name: carCircuitSuitability; Type: TABLE DATA; Schema: assessment; Owner: wacaruser
--

COPY assessment."carCircuitSuitability" ("carType", "circuitType") FROM stdin;
\.


--
-- Data for Name: carType; Type: TABLE DATA; Schema: assessment; Owner: wacaruser
--

COPY assessment."carType" (name) FROM stdin;
\.


--
-- Data for Name: circuit; Type: TABLE DATA; Schema: assessment; Owner: wacaruser
--

COPY assessment.circuit (name, type, length, "cornersNumber", address, description, "lapPrice", available, image) FROM stdin;
\.


--
-- Data for Name: circuitType; Type: TABLE DATA; Schema: assessment; Owner: wacaruser
--

COPY assessment."circuitType" (name) FROM stdin;
\.


--
-- Data for Name: favourite; Type: TABLE DATA; Schema: assessment; Owner: wacaruser
--

COPY assessment.favourite (circuit, "carBrand", "carModel", "createdAt", account) FROM stdin;
\.


--
-- Data for Name: order; Type: TABLE DATA; Schema: assessment; Owner: wacaruser
--

COPY assessment."order" (date, "carBrand", "carModel", circuit, "createdAt", "nLaps", price, account) FROM stdin;
\.


--
-- Name: account account_pkey; Type: CONSTRAINT; Schema: assessment; Owner: wacaruser
--

ALTER TABLE ONLY assessment.account
    ADD CONSTRAINT account_pkey PRIMARY KEY (email);


--
-- Name: carCircuitSuitability carCircuitSuitability_pkey; Type: CONSTRAINT; Schema: assessment; Owner: wacaruser
--

ALTER TABLE ONLY assessment."carCircuitSuitability"
    ADD CONSTRAINT "carCircuitSuitability_pkey" PRIMARY KEY ("carType", "circuitType");


--
-- Name: carType carType_pkey; Type: CONSTRAINT; Schema: assessment; Owner: wacaruser
--

ALTER TABLE ONLY assessment."carType"
    ADD CONSTRAINT "carType_pkey" PRIMARY KEY (name);


--
-- Name: car car_pkey; Type: CONSTRAINT; Schema: assessment; Owner: wacaruser
--

ALTER TABLE ONLY assessment.car
    ADD CONSTRAINT car_pkey PRIMARY KEY (brand, model);


--
-- Name: circuitType circuitType_pkey; Type: CONSTRAINT; Schema: assessment; Owner: wacaruser
--

ALTER TABLE ONLY assessment."circuitType"
    ADD CONSTRAINT "circuitType_pkey" PRIMARY KEY (name);


--
-- Name: circuit circuit_pkey; Type: CONSTRAINT; Schema: assessment; Owner: wacaruser
--

ALTER TABLE ONLY assessment.circuit
    ADD CONSTRAINT circuit_pkey PRIMARY KEY (name);


--
-- Name: favourite favourite_pk; Type: CONSTRAINT; Schema: assessment; Owner: wacaruser
--

ALTER TABLE ONLY assessment.favourite
    ADD CONSTRAINT favourite_pk PRIMARY KEY (account, "carModel", "carBrand", circuit);


--
-- Name: order order_pkey; Type: CONSTRAINT; Schema: assessment; Owner: wacaruser
--

ALTER TABLE ONLY assessment."order"
    ADD CONSTRAINT order_pkey PRIMARY KEY (account, date);


--
-- Name: car check_car_trigger; Type: TRIGGER; Schema: assessment; Owner: wacaruser
--

CREATE TRIGGER check_car_trigger BEFORE INSERT ON assessment.car FOR EACH ROW EXECUTE FUNCTION assessment.check_car_function();


--
-- Name: circuit check_circuit_trigger; Type: TRIGGER; Schema: assessment; Owner: wacaruser
--

CREATE TRIGGER check_circuit_trigger BEFORE INSERT ON assessment.circuit FOR EACH ROW EXECUTE FUNCTION assessment.check_circuit_function();


--
-- Name: order check_order_trigger; Type: TRIGGER; Schema: assessment; Owner: wacaruser
--

CREATE TRIGGER check_order_trigger BEFORE INSERT ON assessment."order" FOR EACH ROW EXECUTE FUNCTION assessment.check_order_function();


--
-- Name: carCircuitSuitability carCircuitSuitability_carType_fkey; Type: FK CONSTRAINT; Schema: assessment; Owner: wacaruser
--

ALTER TABLE ONLY assessment."carCircuitSuitability"
    ADD CONSTRAINT "carCircuitSuitability_carType_fkey" FOREIGN KEY ("carType") REFERENCES assessment."carType"(name);


--
-- Name: carCircuitSuitability carCircuitSuitability_circuitType_fkey; Type: FK CONSTRAINT; Schema: assessment; Owner: wacaruser
--

ALTER TABLE ONLY assessment."carCircuitSuitability"
    ADD CONSTRAINT "carCircuitSuitability_circuitType_fkey" FOREIGN KEY ("circuitType") REFERENCES assessment."circuitType"(name);


--
-- Name: car circuit_type_fkey; Type: FK CONSTRAINT; Schema: assessment; Owner: wacaruser
--

ALTER TABLE ONLY assessment.car
    ADD CONSTRAINT circuit_type_fkey FOREIGN KEY (type) REFERENCES assessment."carType"(name);


--
-- Name: circuit circuit_type_fkey; Type: FK CONSTRAINT; Schema: assessment; Owner: wacaruser
--

ALTER TABLE ONLY assessment.circuit
    ADD CONSTRAINT circuit_type_fkey FOREIGN KEY (type) REFERENCES assessment."circuitType"(name);


--
-- Name: favourite favourite_account_fkey; Type: FK CONSTRAINT; Schema: assessment; Owner: wacaruser
--

ALTER TABLE ONLY assessment.favourite
    ADD CONSTRAINT favourite_account_fkey FOREIGN KEY (account) REFERENCES assessment.account(email) NOT VALID;


--
-- Name: favourite favourite_car_fkey; Type: FK CONSTRAINT; Schema: assessment; Owner: wacaruser
--

ALTER TABLE ONLY assessment.favourite
    ADD CONSTRAINT favourite_car_fkey FOREIGN KEY ("carBrand", "carModel") REFERENCES assessment.car(brand, model) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: favourite favourite_circuit_fkey; Type: FK CONSTRAINT; Schema: assessment; Owner: wacaruser
--

ALTER TABLE ONLY assessment.favourite
    ADD CONSTRAINT favourite_circuit_fkey FOREIGN KEY (circuit) REFERENCES assessment.circuit(name) ON UPDATE CASCADE ON DELETE SET NULL;


--
-- Name: order order_account_fkey; Type: FK CONSTRAINT; Schema: assessment; Owner: wacaruser
--

ALTER TABLE ONLY assessment."order"
    ADD CONSTRAINT order_account_fkey FOREIGN KEY (account) REFERENCES assessment.account(email) NOT VALID;


--
-- Name: order order_car_fkey; Type: FK CONSTRAINT; Schema: assessment; Owner: wacaruser
--

ALTER TABLE ONLY assessment."order"
    ADD CONSTRAINT order_car_fkey FOREIGN KEY ("carBrand", "carModel") REFERENCES assessment.car(brand, model) ON UPDATE CASCADE NOT VALID;


--
-- Name: order order_circuit_fkey; Type: FK CONSTRAINT; Schema: assessment; Owner: wacaruser
--

ALTER TABLE ONLY assessment."order"
    ADD CONSTRAINT order_circuit_fkey FOREIGN KEY (circuit) REFERENCES assessment.circuit(name) ON UPDATE CASCADE NOT VALID;

--
-- PostgreSQL database dump complete
--

