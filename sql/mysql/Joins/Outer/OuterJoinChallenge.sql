/**
 * @author onyxwizard
 * @date 02-02-2026
 */

show DATABASES;
use solar_system_advanced;

SHOW TABLES;
show INDEX FROM planet;
show INDEX FROM ring;

-- List ALL planets, showing ring count even if zero
SELECT 
    p.planet_name,
    COUNT(r.ring_id) as ring_count,
    GROUP_CONCAT(r.ring_name) as ring_names
FROM planet p
LEFT JOIN ring r ON p.planet_id = r.planet_id
GROUP BY p.planet_id
ORDER BY ring_count DESC;


SELECT * 
FROM planet p
LEFT JOIN ring r
ON p.planet_id = r.planet_id
ORDER BY p.planet_id;

-- Show ALL rings with their planet info
SELECT 
    r.ring_name,
    p.planet_name,
    r.inner_radius_km,
    r.outer_radius_km
FROM planet p
RIGHT JOIN ring r ON p.planet_id = r.planet_id
ORDER BY p.planet_name, r.inner_radius_km;


-- Show all planets with their ring count AND moon count
SELECT 
    p.planet_name,
    pt.type_name,
    COUNT(DISTINCT r.ring_id) as ring_count,
    COUNT(DISTINCT m.moon_id) as moon_count
FROM planet p
LEFT JOIN planet_type pt ON p.type_id = pt.type_id
LEFT JOIN ring r ON p.planet_id = r.planet_id
LEFT JOIN moon m ON p.planet_id = m.planet_id
GROUP BY p.planet_id, p.planet_name, pt.type_name
ORDER BY p.planet_name;

-- Find planets with NO rings AND NO moons
SELECT 
    p.planet_name,
    pt.type_name
FROM planet p
LEFT JOIN planet_type pt ON p.type_id = pt.type_id
LEFT JOIN ring r ON p.planet_id = r.planet_id
LEFT JOIN moon m ON p.planet_id = m.planet_id
WHERE r.ring_id IS NULL 
    AND m.moon_id IS NULL
ORDER BY p.planet_name;



-- Create complete celestial body catalog (Planets, Rings, Moons)
SELECT 
    'Planet' as body_type,
    p.planet_name as name,
    NULL as associated_with,
    pt.type_name as details
FROM planet p
LEFT JOIN planet_type pt ON p.type_id = pt.type_id

UNION ALL

SELECT 
    'Ring' as body_type,
    r.ring_name as name,
    p.planet_name as associated_with,
    CONCAT('Thickness: ', r.thickness_km, 'km') as details
FROM ring r
LEFT JOIN planet p ON r.planet_id = p.planet_id

UNION ALL

SELECT 
    'Moon' as body_type,
    m.moon_name as name,
    p.planet_name as associated_with,
    CONCAT('Diameter: ', m.diameter_km, 'km') as details
FROM moon m
LEFT JOIN planet p ON m.planet_id = p.planet_id
ORDER BY body_type, name;


-- Show all missions, even if they have no observations
SELECT 
    m.mission_name,
    m.launch_year,
    m.organization,
    p.planet_name as target_planet,
    COUNT(mo.observation_id) as observation_count,
    GROUP_CONCAT(mo.observation_type) as observation_types
FROM mission m
LEFT JOIN planet p ON m.target_planet_id = p.planet_id
LEFT JOIN mission_observation mo ON m.mission_id = mo.mission_id
GROUP BY m.mission_id, m.mission_name, m.launch_year, m.organization, p.planet_name
ORDER BY m.launch_year;


-- Show ALL planets and their discovery info (if any)
SELECT 
    p.planet_name,
    d.discoverer_name,
    d.discovery_year,
    d.discovery_method,
    CASE 
        WHEN d.discovery_year < 0 THEN 'Ancient'
        WHEN d.discovery_year < 1600 THEN 'Pre-Telescope'
        WHEN d.discovery_year < 1900 THEN 'Classical'
        ELSE 'Modern'
    END as discovery_era
FROM planet p
LEFT JOIN discovery d ON p.planet_id = d.celestial_body_id 
    AND d.celestial_body_type = 'Planet'
ORDER BY d.discovery_year;


-- Complete planetary analysis with NULL handling
SELECT 
    p.planet_name,
    pt.type_name,
    COALESCE(COUNT(DISTINCT r.ring_id), 0) as ring_count,
    COALESCE(COUNT(DISTINCT m.moon_id), 0) as moon_count,
    COALESCE(COUNT(DISTINCT mo.mission_id), 0) as mission_count,
    CASE 
        WHEN COUNT(DISTINCT r.ring_id) > 0 AND COUNT(DISTINCT m.moon_id) > 0 
        THEN 'Has both rings and moons'
        WHEN COUNT(DISTINCT r.ring_id) > 0 
        THEN 'Has rings only'
        WHEN COUNT(DISTINCT m.moon_id) > 0 
        THEN 'Has moons only'
        ELSE 'No rings or moons'
    END as characteristics
FROM planet p
LEFT JOIN planet_type pt ON p.type_id = pt.type_id
LEFT JOIN ring r ON p.planet_id = r.planet_id
LEFT JOIN moon m ON p.planet_id = m.planet_id
LEFT JOIN mission_observation mo ON p.planet_id = mo.planet_id
GROUP BY p.planet_id, p.planet_name, pt.type_name
ORDER BY p.planet_name;
