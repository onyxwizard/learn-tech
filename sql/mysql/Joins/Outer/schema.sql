/**
 * @author onyxwizard
 * @date 02-02-2026
 */


-- Create the database
CREATE DATABASE IF NOT EXISTS solar_system_advanced;
USE solar_system_advanced;

-- Table 1: Planet Types
CREATE TABLE planet_type (
    type_id INT PRIMARY KEY AUTO_INCREMENT,
    type_name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
);

-- Table 2: Planets - FIXED DATA TYPES for large numbers
CREATE TABLE planet (
    planet_id INT PRIMARY KEY AUTO_INCREMENT,
    planet_name VARCHAR(50) NOT NULL UNIQUE,
    type_id INT,
    diameter_km DECIMAL(12,2),
    mass_kg DECIMAL(38,0),  -- Changed to handle very large numbers
    distance_from_sun_km DECIMAL(12,2),
    orbital_period_days DECIMAL(10,2),
    has_atmosphere BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (type_id) REFERENCES planet_type(type_id)
);

-- Table 3: Rings
CREATE TABLE ring (
    ring_id INT PRIMARY KEY AUTO_INCREMENT,
    planet_id INT,
    ring_name VARCHAR(100),
    inner_radius_km DECIMAL(15,2),
    outer_radius_km DECIMAL(15,2),
    thickness_km DECIMAL(10,2),
    composition VARCHAR(100),
    discovered_year INT,
    FOREIGN KEY (planet_id) REFERENCES planet(planet_id) ON DELETE CASCADE
);

-- Table 4: Moons
CREATE TABLE moon (
    moon_id INT PRIMARY KEY AUTO_INCREMENT,
    moon_name VARCHAR(100) NOT NULL,
    planet_id INT,
    diameter_km DECIMAL(10,2),
    orbital_period_days DECIMAL(10,2),
    discovered_year INT,
    has_atmosphere BOOLEAN DEFAULT FALSE,
    is_geologically_active BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (planet_id) REFERENCES planet(planet_id) ON DELETE CASCADE
);

-- Table 5: Discoveries
CREATE TABLE discovery (
    discovery_id INT PRIMARY KEY AUTO_INCREMENT,
    celestial_body_type ENUM('Planet', 'Ring', 'Moon') NOT NULL,
    celestial_body_id INT, -- References planet_id, ring_id, or moon_id
    discoverer_name VARCHAR(100),
    discovery_year INT,
    discovery_method VARCHAR(100),
    notes TEXT
);

-- Table 6: Missions
CREATE TABLE mission (
    mission_id INT PRIMARY KEY AUTO_INCREMENT,
    mission_name VARCHAR(100) NOT NULL,
    launch_year INT,
    organization VARCHAR(100),
    target_planet_id INT,
    status ENUM('Planned', 'Active', 'Completed', 'Failed') DEFAULT 'Planned',
    FOREIGN KEY (target_planet_id) REFERENCES planet(planet_id)
);

-- Table 7: Mission Observations (Many-to-Many between Missions and Planets/Moons)
CREATE TABLE mission_observation (
    observation_id INT PRIMARY KEY AUTO_INCREMENT,
    mission_id INT,
    planet_id INT NULL,
    moon_id INT NULL,
    observation_date DATE,
    observation_type VARCHAR(100),
    notes TEXT,
    FOREIGN KEY (mission_id) REFERENCES mission(mission_id),
    FOREIGN KEY (planet_id) REFERENCES planet(planet_id),
    FOREIGN KEY (moon_id) REFERENCES moon(moon_id),
    CHECK (planet_id IS NOT NULL OR moon_id IS NOT NULL) -- At least one target
);

-- Indexes for better performance
CREATE INDEX idx_planet_type ON planet(type_id);
CREATE INDEX idx_ring_planet ON ring(planet_id);
CREATE INDEX idx_moon_planet ON moon(planet_id);
CREATE INDEX idx_discovery_year ON discovery(discovery_year);
CREATE INDEX idx_mission_target ON mission(target_planet_id);
CREATE INDEX idx_observation_mission ON mission_observation(mission_id);
CREATE INDEX idx_observation_date ON mission_observation(observation_date);

-- Insert Planet Types
INSERT INTO planet_type (type_name, description) VALUES
('Terrestrial', 'Rocky planets with solid surfaces'),
('Gas Giant', 'Massive planets composed mainly of hydrogen and helium'),
('Ice Giant', 'Planets composed mostly of elements heavier than hydrogen and helium'),
('Dwarf Planet', 'Celestial bodies orbiting the Sun that are not satellites');

-- Insert Planets with CORRECT mass values (no scientific notation)
INSERT INTO planet (planet_name, type_id, diameter_km, mass_kg, distance_from_sun_km, orbital_period_days, has_atmosphere) VALUES
-- Mercury
('Mercury', 1, 4879.40, 330110000000000000000000, 57910000.00, 88.00, FALSE),

-- Venus
('Venus', 1, 12104.00, 4867500000000000000000000, 108200000.00, 224.70, TRUE),

-- Earth
('Earth', 1, 12756.00, 5972370000000000000000000, 149600000.00, 365.26, TRUE),

-- Mars
('Mars', 1, 6792.00, 641710000000000000000000, 227900000.00, 687.00, TRUE),

-- Jupiter (FIXED: 1.8982e27 = 1898200000000000000000000000)
('Jupiter', 2, 142984.00, 1898200000000000000000000000, 778300000.00, 4333.00, TRUE),

-- Saturn (FIXED: 5.6834e26 = 568340000000000000000000000)
('Saturn', 2, 120536.00, 568340000000000000000000000, 1427000000.00, 10759.00, TRUE),

-- Uranus (FIXED: 8.6810e25 = 86810000000000000000000000)
('Uranus', 3, 51118.00, 86810000000000000000000000, 2871000000.00, 30688.00, TRUE),

-- Neptune (FIXED: 1.02413e26 = 102413000000000000000000000)
('Neptune', 3, 49528.00, 102413000000000000000000000, 4497100000.00, 60182.00, TRUE),

-- Pluto (FIXED: 1.303e22 = 13030000000000000000000)
('Pluto', 4, 2376.60, 13030000000000000000000, 5906380000.00, 90560.00, TRUE);

-- Insert Rings
INSERT INTO ring (planet_id, ring_name, inner_radius_km, outer_radius_km, thickness_km, composition, discovered_year) VALUES
-- Jupiter's rings
(5, 'Main Ring', 92000.00, 122500.00, 30.00, 'Dust particles', 1979),
(5, 'Halo Ring', 100000.00, 122000.00, 12500.00, 'Microscopic dust', 1979),
(5, 'Gossamer Rings', 128000.00, 226000.00, 1000.00, 'Dust from moons', 1979),

-- Saturn's rings (famous ones)
(6, 'D Ring', 66900.00, 74510.00, 0.01, 'Ice particles', 1980),
(6, 'C Ring', 74658.00, 92000.00, 5.00, 'Water ice', 1850),
(6, 'B Ring', 92000.00, 117580.00, 10.00, 'Water ice with impurities', 1610),
(6, 'A Ring', 122170.00, 136775.00, 20.00, 'Ice and rock', 1675),
(6, 'F Ring', 140180.00, 140350.00, 0.10, 'Ice particles', 1979),

-- Uranus's rings
(7, 'Zeta Ring', 38000.00, 40000.00, 1.00, 'Dust', 1986),
(7, '6 Ring', 41837.00, 42000.00, 1.50, 'Dark material', 1986),
(7, '5 Ring', 42234.00, 42300.00, 2.00, 'Dust', 1986),

-- Neptune's rings
(8, 'Galle Ring', 41900.00, 43000.00, 0.10, 'Dust', 1989),
(8, 'Le Verrier Ring', 53200.00, 57000.00, 0.10, 'Dust', 1989),
(8, 'Adams Ring', 62932.00, 63000.00, 0.10, 'Dust with arcs', 1989);

-- Insert Moons (selective list for practice)
INSERT INTO moon (moon_name, planet_id, diameter_km, orbital_period_days, discovered_year, has_atmosphere, is_geologically_active) VALUES
-- Earth's Moon
('Moon', 3, 3474.80, 27.32, -9999, FALSE, FALSE),

-- Mars Moons
('Phobos', 4, 22.40, 0.32, 1877, FALSE, FALSE),
('Deimos', 4, 12.40, 1.26, 1877, FALSE, FALSE),

-- Jupiter's Major Moons (Galilean moons + one other)
('Io', 5, 3643.20, 1.77, 1610, FALSE, TRUE),
('Europa', 5, 3121.60, 3.55, 1610, TRUE, TRUE),
('Ganymede', 5, 5268.20, 7.15, 1610, TRUE, TRUE),
('Callisto', 5, 4820.60, 16.69, 1610, TRUE, FALSE),

-- Saturn's Major Moons
('Mimas', 6, 396.40, 0.94, 1789, FALSE, FALSE),
('Enceladus', 6, 504.20, 1.37, 1789, TRUE, TRUE),
('Titan', 6, 5149.50, 15.95, 1655, TRUE, TRUE),
('Rhea', 6, 1527.60, 4.52, 1672, TRUE, FALSE),

-- Uranus's Major Moons
('Miranda', 7, 471.60, 1.41, 1948, FALSE, TRUE),
('Ariel', 7, 1157.80, 2.52, 1851, FALSE, TRUE),
('Titania', 7, 1576.80, 8.71, 1787, FALSE, TRUE),

-- Neptune's Major Moons
('Triton', 8, 2706.80, 5.88, 1846, TRUE, TRUE),

-- Pluto's Moons
('Charon', 9, 1212.00, 6.39, 1978, FALSE, FALSE);

-- Insert Discoveries
INSERT INTO discovery (celestial_body_type, celestial_body_id, discoverer_name, discovery_year, discovery_method, notes) VALUES
-- Planets
('Planet', 1, 'Unknown', -3000, 'Visual observation', 'Known since antiquity'),
('Planet', 2, 'Unknown', -3000, 'Visual observation', 'Known since antiquity'),
('Planet', 3, 'Unknown', -3000, 'Visual observation', 'Known since antiquity'),
('Planet', 4, 'Unknown', -2000, 'Visual observation', 'Known to ancient Egyptians'),
('Planet', 5, 'Unknown', -700, 'Visual observation', 'Known to Babylonians'),
('Planet', 6, 'Unknown', -700, 'Visual observation', 'Known to Assyrians'),
('Planet', 7, 'William Herschel', 1781, 'Telescope', 'First planet discovered with telescope'),
('Planet', 8, 'Johann Galle', 1846, 'Mathematical prediction', 'Predicted by Urbain Le Verrier'),
('Planet', 9, 'Clyde Tombaugh', 1930, 'Photographic plates', 'Originally classified as planet'),

-- Moons
('Moon', 1, 'Unknown', -9999, 'Visual observation', 'Known since prehistory'),
('Moon', 2, 'Asaph Hall', 1877, 'Telescope', 'Named after Greek god of fear'),
('Moon', 3, 'Asaph Hall', 1877, 'Telescope', 'Named after Greek god of terror'),
('Moon', 4, 'Galileo Galilei', 1610, 'Telescope', 'First observed with telescope'),
('Moon', 5, 'Galileo Galilei', 1610, 'Telescope', 'Galilean moon'),

-- Rings
('Ring', 8, 'Christiaan Huygens', 1655, 'Telescope', 'First to describe ring around Saturn'),
('Ring', 11, 'Voyager 2', 1986, 'Space probe', 'Discovered during Uranus flyby');

-- Insert Missions
INSERT INTO mission (mission_name, launch_year, organization, target_planet_id, status) VALUES
('Voyager 1', 1977, 'NASA', 5, 'Completed'),
('Voyager 2', 1977, 'NASA', 8, 'Completed'),
('Galileo', 1989, 'NASA', 5, 'Completed'),
('Cassini-Huygens', 1997, 'NASA/ESA/ASI', 6, 'Completed'),
('New Horizons', 2006, 'NASA', 9, 'Completed'),
('Juno', 2011, 'NASA', 5, 'Active'),
('Mars Reconnaissance Orbiter', 2005, 'NASA', 4, 'Active'),
('Perseverance Rover', 2020, 'NASA', 4, 'Active'),
('Europa Clipper', 2024, 'NASA', 5, 'Planned');

-- Insert Mission Observations
INSERT INTO mission_observation (mission_id, planet_id, moon_id, observation_date, observation_type, notes) VALUES
-- Voyager 1 observations
(1, 5, NULL, '1979-03-05', 'Flyby', 'First close-up images of Jupiter'),
(1, NULL, 4, '1979-03-05', 'Volcano imaging', 'Discovered active volcanoes on Io'),

-- Voyager 2 observations
(2, 5, NULL, '1979-07-09', 'Flyby', 'Jupiter observations'),
(2, 8, NULL, '1989-08-25', 'Flyby', 'Neptune close approach'),

-- Galileo observations
(3, 5, NULL, '1995-12-07', 'Orbital insertion', 'Became first Jupiter orbiter'),
(3, NULL, 5, '1996-01-01', 'Surface study', 'Studied Europa''s icy surface'),

-- Cassini observations
(4, 6, NULL, '2004-07-01', 'Orbital insertion', 'Entered Saturn orbit'),
(4, NULL, 9, '2005-01-14', 'Landing', 'Huygens probe landed on Titan'),

-- New Horizons observations
(5, 9, NULL, '2015-07-14', 'Flyby', 'First Pluto flyby'),

-- Juno observations
(6, 5, NULL, '2016-07-04', 'Orbital insertion', 'Polar orbit around Jupiter'),

-- Mars missions
(7, 4, NULL, '2006-03-10', 'Orbital insertion', 'High-resolution imaging of Mars'),
(8, 4, NULL, '2021-02-18', 'Landing', 'Perseverance rover landing in Jezero Crater');