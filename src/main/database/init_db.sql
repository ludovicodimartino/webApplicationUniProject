-- ADMIN USER: mail: admin@example.com password: Admin 123
INSERT INTO assessment.account(email, name, surname, address, password, type) VALUES ('admin@example.com', 'Admin', 'Admin', 'Via casa mia, 10', 'E64B78FC3BC91BCBC7DC232BA8EC59E0', 'ADMIN');
-- STANDARD USER: mail: default@example.com password: Default123
INSERT INTO assessment.account(email, name, surname, address, password, type) VALUES ('default@example.com', 'Default', 'User', 'Via casa sua, 10', '3DBA52FCF7AFD1D73A04C9EA70DE6416', 'USER');
INSERT INTO assessment."carType"(name) VALUES ('performance');
INSERT INTO assessment."carType"(name) VALUES ('supercar');
INSERT INTO assessment."carType"(name) VALUES ('hypercar');
INSERT INTO assessment."circuitType"(name) VALUES ('grandPrix');
INSERT INTO assessment."circuitType"(name) VALUES ('tourism');
INSERT INTO assessment."circuitType"(name) VALUES ('straights');
INSERT INTO assessment."carCircuitSuitability"("carType", "circuitType") VALUES ('supercar', 'straights');
INSERT INTO assessment."carCircuitSuitability"("carType", "circuitType") VALUES ('hypercar', 'tourism');
INSERT INTO assessment."carCircuitSuitability"("carType", "circuitType") VALUES ('hypercar', 'grandPrix');
INSERT INTO assessment."carCircuitSuitability"("carType", "circuitType") VALUES ('performance', 'tourism');
INSERT INTO assessment.car(brand, model, type, horsepower, "0-100", "maxSpeed", description, available, image, imageMediaType) VALUES ('Maserati', 'GranTurismo Trofeo', 'performance', 550, 3.5, 320, 'The GranTurismo brings racetrack innovation to the road for unprecedented performance.', true, NULL, '');
INSERT INTO assessment.car(brand, model, type, horsepower, "0-100", "maxSpeed", description, available, image, imageMediaType) VALUES ('Lamborghini', 'Huracan Tecnica', 'supercar', 640, 3.2, 325, 'Combining the highest expression of Lamborghini’s power with the exclusive refinement of Italian handicraft, this series embodies authentic design and state-of-the-art mechanical technology.', true, NULL, '');
INSERT INTO assessment.car(brand, model, type, horsepower, "0-100", "maxSpeed", description, available, image, imageMediaType) VALUES ('Pagani', 'Zonda R', 'hypercar', 750, 2.7, 375, 'Flying on the wings of a wind that keeps blowing harder and faster, the Zonda R was designed for the racetrack, and from the racetrack, without limits.', true, NULL, '');
INSERT INTO assessment.circuit(name, type, length, "cornersNumber", address, description, "lapPrice", available, image, imageMediaType) VALUES ('Autodromo Nazionale Monza', 'straights', 5793, 11, 'Viale di Vedano, 5, 20900 Monza MB', 'There are a lot of straights', 20, true, NULL, '');
INSERT INTO assessment.circuit(name, type, length, "cornersNumber", address, description, "lapPrice", available, image, imageMediaType) VALUES ('Autodromo Enzo e Dino Ferrari', 'grandPrix', 4909, 19, 'Piazza Ayrton Senna da Silva, 1, 40026 Imola BO', 'The Autodromo Internazionale Enzo e Dino Ferrari is universally recognized as an extremely technical track, difficult to read, with complex curves and off-cuts: traveling at a high pace requires a professional skill level.', 30, true, NULL, '');
INSERT INTO assessment.circuit(name, type, length, "cornersNumber", address, description, "lapPrice", available, image, imageMediaType) VALUES ('Autodromo Vallelunga', 'tourism', 4085, 15, 'Via della Mola Maggiorana, 4, 00063 Campagnano di Roma RM', 'Some description.', 12, true, NULL, '');