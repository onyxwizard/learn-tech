-- Create the database
CREATE DATABASE IF NOT EXISTS solar_system;
USE solar_system;

-- Create the planet table
CREATE TABLE planet (
    planet_id INT PRIMARY KEY,
    planet_name VARCHAR(50) NOT NULL
);

-- Create the ring table
CREATE TABLE ring (
    planet_id INT PRIMARY KEY,
    ring_tot INT NOT NULL,
    FOREIGN KEY (planet_id)
        REFERENCES planet (planet_id)
);

-- Insert data into planet table
INSERT INTO planet (planet_id, planet_name) VALUES
(1, 'Mercury'),
(2, 'Venus'),
(3, 'Earth'),
(4, 'Mars'),
(5, 'Jupiter'),
(6, 'Saturn'),
(7, 'Uranus'),
(8, 'Neptune');

-- Insert data into ring table
INSERT INTO ring (planet_id, ring_tot) VALUES
(5, 3),
(6, 7),
(7, 13),
(8, 6);
