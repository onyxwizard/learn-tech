/**
 * @author onyxwizard
 * @date 07-02-2026
 */

use library_challenge;
SHOW TABLES;
SELECT * FROM books;

DELIMITER $$
CREATE FUNCTION book_genre_count(
  book_genre varchar(30)
)
returns INT
DETERMINISTIC
BEGIN
  DECLARE gen_count INT;

  SET gen_count = (SELECT 
    COUNT(*)
  FROM
    books
  WHERE books.genre = book_genre
  LIMIT 10
  );
  return (gen_count);
END $$
DELIMITER;

SELECT book_genre_count('');