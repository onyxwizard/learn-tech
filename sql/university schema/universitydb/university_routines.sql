-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: university
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping routines for database 'university'
--
/*!50003 DROP FUNCTION IF EXISTS `calculate_age` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `calculate_age`(dob DATE) RETURNS int
    DETERMINISTIC
BEGIN
    RETURN TIMESTAMPDIFF(YEAR, dob, CURDATE());
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `generate_date_of_birth` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `generate_date_of_birth`() RETURNS date
    DETERMINISTIC
BEGIN
    DECLARE random_year INT;
    DECLARE random_month INT;
    DECLARE random_day INT;
    
    -- Generate birth years between 1998 and 2005 (18-25 years old)
    SET random_year = 1998 + FLOOR(RAND() * 8);
    SET random_month = 1 + FLOOR(RAND() * 12);
    
    -- Handle different days per month
    IF random_month IN (1,3,5,7,8,10,12) THEN
        SET random_day = 1 + FLOOR(RAND() * 31);
    ELSEIF random_month IN (4,6,9,11) THEN
        SET random_day = 1 + FLOOR(RAND() * 30);
    ELSE
        -- February
        SET random_day = 1 + FLOOR(RAND() * 28);
    END IF;
    
    RETURN CONCAT(random_year, '-', LPAD(random_month, 2, '0'), '-', LPAD(random_day, 2, '0'));
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `generate_email` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `generate_email`(first_name VARCHAR(50), last_name VARCHAR(50), year INT) RETURNS varchar(100) CHARSET utf8mb4
    DETERMINISTIC
BEGIN
    DECLARE email VARCHAR(100);
    DECLARE random_num INT;
    
    SET random_num = FLOOR(RAND() * 1000);
    SET email = LOWER(CONCAT(
        SUBSTRING(first_name, 1, 1),
        last_name,
        year,
        random_num,
        '@university.edu'
    ));
    
    RETURN email;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `generate_indian_phone` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `generate_indian_phone`() RETURNS varchar(15) CHARSET utf8mb4
    DETERMINISTIC
BEGIN
    DECLARE prefix VARCHAR(5);
    DECLARE number_part VARCHAR(10);
    
    SET prefix = ELT(FLOOR(1 + RAND() * 5), '+91-7', '+91-8', '+91-9', '+91-6', '+91-7');
    SET number_part = LPAD(FLOOR(RAND() * 1000000000), 9, '0');
    
    RETURN CONCAT(prefix, number_part);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `generate_phone` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `generate_phone`() RETURNS varchar(15) CHARSET utf8mb4
    DETERMINISTIC
BEGIN
    RETURN CONCAT('+91-', LPAD(FLOOR(RAND() * 9000000000) + 1000000000, 10, '0'));
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `generate_student_name` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `generate_student_name`(gender VARCHAR(10)) RETURNS varchar(100) CHARSET utf8mb4
    DETERMINISTIC
BEGIN
    DECLARE first_name VARCHAR(50);
    DECLARE last_name VARCHAR(50);
    
    -- Indian first names based on gender
    IF gender = 'Male' THEN
        SET first_name = ELT(FLOOR(1 + RAND() * 20), 
            'Aarav', 'Vivaan', 'Aditya', 'Vihaan', 'Arjun', 'Sai', 'Reyansh', 'Atharv', 
            'Dhruv', 'Krishna', 'Kian', 'Kabir', 'Rudra', 'Veer', 'Aryan', 'Mohammed',
            'Rohan', 'Siddharth', 'Yash', 'Pranav'
        );
    ELSEIF gender = 'Female' THEN
        SET first_name = ELT(FLOOR(1 + RAND() * 20), 
            'Saanvi', 'Ananya', 'Diya', 'Aadhya', 'Ishita', 'Myra', 'Avni', 'Anika', 
            'Pari', 'Navya', 'Riya', 'Sara', 'Aarohi', 'Anvi', 'Kiara', 'Prisha',
            'Zara', 'Sara', 'Anaya', 'Advika'
        );
    ELSE
        SET first_name = ELT(FLOOR(1 + RAND() * 20), 
            'Alex', 'Taylor', 'Jordan', 'Riley', 'Casey', 'Jamie', 'Morgan', 'Quinn',
            'Dakota', 'Skyler', 'Rowan', 'Avery', 'Cameron', 'Peyton', 'Blake', 'Drew',
            'Finley', 'Harley', 'Kai', 'Marley'
        );
    END IF;
    
    -- Indian last names
    SET last_name = ELT(FLOOR(1 + RAND() * 25), 
        'Sharma', 'Verma', 'Gupta', 'Singh', 'Kumar', 'Reddy', 'Patel', 'Mishra', 
        'Jain', 'Nair', 'Menon', 'Pillai', 'Chatterjee', 'Banerjee', 'Mukherjee', 
        'Das', 'Roy', 'Yadav', 'Jha', 'Pandey', 'Thakur', 'Choudhary', 'Rao', 
        'Naidu', 'Iyer'
    );
    
    RETURN CONCAT(first_name, ' ', last_name);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `random_date` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `random_date`(start_date DATE, end_date DATE) RETURNS date
    DETERMINISTIC
BEGIN
    RETURN DATE_ADD(start_date, INTERVAL FLOOR(RAND() * DATEDIFF(end_date, start_date)) DAY);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `generate_100_students` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `generate_100_students`()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE dept_count INT;
    DECLARE dept_id INT;
    DECLARE subject_count INT;
    DECLARE subject_id INT;
    DECLARE gender_val ENUM('Male', 'Female', 'Other');
    DECLARE first_name_part VARCHAR(50);
    DECLARE last_name_part VARCHAR(50);
    DECLARE full_name VARCHAR(100);
    DECLARE dob DATE;
    DECLARE age_val INT;
    DECLARE enroll_year INT;
    DECLARE city_state_record VARCHAR(200);
    DECLARE city_val VARCHAR(50);
    DECLARE state_val VARCHAR(50);
    DECLARE email_val VARCHAR(100);
    
    -- Get department count
    SELECT COUNT(*) INTO dept_count FROM Department;
    
    WHILE i <= 100 DO
        -- Random gender (70% Male, 29% Female, 1% Other)
        SET gender_val = CASE 
            WHEN RAND() < 0.7 THEN 'Male'
            WHEN RAND() < 0.99 THEN 'Female'
            ELSE 'Other'
        END;
        
        -- Generate name
        IF gender_val = 'Male' THEN
            SET first_name_part = ELT(FLOOR(1 + RAND() * 20), 
                'Aarav', 'Vivaan', 'Aditya', 'Vihaan', 'Arjun', 'Sai', 'Reyansh', 'Atharv', 
                'Dhruv', 'Krishna', 'Kian', 'Kabir', 'Rudra', 'Veer', 'Aryan', 'Mohammed',
                'Rohan', 'Siddharth', 'Yash', 'Pranav');
        ELSEIF gender_val = 'Female' THEN
            SET first_name_part = ELT(FLOOR(1 + RAND() * 20), 
                'Saanvi', 'Ananya', 'Diya', 'Aadhya', 'Ishita', 'Myra', 'Avni', 'Anika', 
                'Pari', 'Navya', 'Riya', 'Sara', 'Aarohi', 'Anvi', 'Kiara', 'Prisha',
                'Zara', 'Sara', 'Anaya', 'Advika');
        ELSE
            SET first_name_part = ELT(FLOOR(1 + RAND() * 15),
                'Alex', 'Taylor', 'Jordan', 'Riley', 'Casey', 'Jamie', 'Morgan', 'Quinn',
                'Dakota', 'Skyler', 'Rowan', 'Avery', 'Cameron', 'Peyton', 'Blake');
        END IF;
        
        SET last_name_part = ELT(FLOOR(1 + RAND() * 25), 
            'Sharma', 'Verma', 'Gupta', 'Singh', 'Kumar', 'Reddy', 'Patel', 'Mishra', 
            'Jain', 'Nair', 'Menon', 'Pillai', 'Chatterjee', 'Banerjee', 'Mukherjee', 
            'Das', 'Roy', 'Yadav', 'Jha', 'Pandey', 'Thakur', 'Choudhary', 'Rao', 
            'Naidu', 'Iyer');
        
        SET full_name = CONCAT(first_name_part, ' ', last_name_part);
        
        -- Generate date of birth and calculate age
        SET dob = generate_date_of_birth();
        SET age_val = calculate_age(dob);
        
        -- Random department (ensure we have subjects in that department)
        SET dept_id = FLOOR(1 + RAND() * dept_count);
        
        -- Get random subject from the selected department
        SELECT COUNT(*) INTO subject_count 
        FROM Subject 
        WHERE department_id = dept_id;
        
        IF subject_count > 0 THEN
            SELECT s.subject_id INTO subject_id 
            FROM Subject s 
            WHERE s.department_id = dept_id 
            ORDER BY RAND() 
            LIMIT 1;
        ELSE
            SET subject_id = NULL;
        END IF;
        
        -- Random city and state
        SELECT CONCAT(city, '|', state) INTO city_state_record 
        FROM indian_cities 
        ORDER BY RAND() 
        LIMIT 1;
        
        SET city_val = SUBSTRING_INDEX(city_state_record, '|', 1);
        SET state_val = SUBSTRING_INDEX(city_state_record, '|', -1);
        
        -- Enrollment year (2020-2023)
        SET enroll_year = 2020 + FLOOR(RAND() * 4);
        
        -- Generate email
        SET email_val = LOWER(CONCAT(
            SUBSTRING(first_name_part, 1, 1),
            last_name_part,
            enroll_year,
            LPAD(FLOOR(RAND() * 1000), 3, '0'),
            '@university.edu'
        ));
        
        -- Insert the student
        INSERT INTO Student (
            id_number,
            enrollment_id,
            name,
            gender,
            date_of_birth,
            age,
            course_id,
            semester,
            academic_year,
            department_id,
            state,
            city,
            country,
            phone_number,
            email,
            enrollment_date
        ) VALUES (
            CONCAT('UNIV', LPAD(i, 6, '0')),
            CONCAT('EN', enroll_year, 'CS', LPAD(i, 4, '0')),
            full_name,
            gender_val,
            dob,
            age_val,
            subject_id,
            1 + FLOOR(RAND() * 8), -- Random semester 1-8
            CONCAT(enroll_year, '-', enroll_year+1),
            dept_id,
            state_val,
            city_val,
            'India',
            generate_indian_phone(),
            email_val,
            CONCAT(enroll_year, '-08-', LPAD(1 + FLOOR(RAND() * 15), 2, '0'))
        );
        
        SET i = i + 1;
    END WHILE;
    
    SELECT '100 students generated successfully!' as message;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-27 21:35:48
