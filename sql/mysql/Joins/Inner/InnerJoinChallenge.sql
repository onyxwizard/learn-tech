/**
 * @author onyxwizard
 * @date 02-02-2026
 */

USE solar_system;

-- Task 1 Solution:
SELECT
  p.planet_name,
  r.ring_tot
FROM
  planet p
  INNER JOIN ring r ON p.planet_id = r.planet_id;

-- Task 2 Solution:
SELECT
  p.planet_name,
  r.ring_tot
FROM
  planet p
  INNER JOIN ring r ON p.planet_id = r.planet_id
WHERE
  r.ring_tot > 5;

-- Task 3 Solution:
SELECT
  AVG(r.ring_tot) AS avg_rings
FROM
  planet p
  INNER JOIN ring r ON p.planet_id = r.planet_id;

-- Task 4 Solution:
SELECT
  p.planet_name,
  r.ring_tot
FROM
  planet p
  INNER JOIN ring r ON p.planet_id = r.planet_id
WHERE
  NOT(
    p.planet_name IN ('Jupiter', 'Saturn') AND
    r.ring_tot BETWEEN 5 AND 10
  );