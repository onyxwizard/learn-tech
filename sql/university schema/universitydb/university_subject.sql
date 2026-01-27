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
-- Table structure for table `subject`
--

DROP TABLE IF EXISTS `subject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subject` (
  `subject_id` int NOT NULL AUTO_INCREMENT,
  `subject_name` varchar(100) NOT NULL,
  `department_id` int DEFAULT NULL,
  `credits` int DEFAULT '3',
  PRIMARY KEY (`subject_id`),
  UNIQUE KEY `unique_subject_dept` (`subject_name`,`department_id`),
  KEY `department_id` (`department_id`),
  CONSTRAINT `subject_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `department` (`department_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subject`
--

LOCK TABLES `subject` WRITE;
/*!40000 ALTER TABLE `subject` DISABLE KEYS */;
INSERT INTO `subject` VALUES (1,'Engineering Mathematics - I',1,4),(2,'Engineering Physics',1,4),(3,'Engineering Chemistry',1,4),(4,'Programming in C',1,4),(5,'Engineering Graphics',1,3),(6,'Basic Electrical Engineering',1,3),(7,'Engineering Mathematics - II',1,4),(8,'Data Structures',1,4),(9,'Digital Logic Design',1,4),(10,'Object Oriented Programming',1,4),(11,'Discrete Mathematics',1,3),(12,'Environmental Science',1,2),(13,'Database Management Systems',1,4),(14,'Computer Organization',1,4),(15,'Operating Systems',1,4),(16,'Theory of Computation',1,3),(17,'Software Engineering',1,3),(18,'Python Programming',1,3),(19,'Design and Analysis of Algorithms',1,4),(20,'Computer Networks',1,4),(21,'Microprocessors',1,4),(22,'Web Technologies',1,3),(23,'Machine Learning Fundamentals',1,3),(24,'Java Programming',1,3),(25,'Engineering Mathematics - I',2,4),(26,'Engineering Physics',2,4),(27,'Fundamentals of IT',2,4),(28,'Programming Fundamentals',2,4),(29,'Digital Electronics',2,3),(30,'Communication Skills',2,2),(31,'Engineering Mathematics - II',2,4),(32,'Data Structures and Algorithms',2,4),(33,'Web Design and Development',2,4),(34,'Object Oriented Programming with Java',2,4),(35,'Discrete Structures',2,3),(36,'Database Concepts',2,3),(37,'Database Management Systems',2,4),(38,'Computer Networks',2,4),(39,'Operating Systems',2,4),(40,'Software Engineering',2,3),(41,'Probability and Statistics',2,3),(42,'Python for Data Science',2,3),(43,'Information Security',2,4),(44,'Cloud Computing',2,4),(45,'Mobile Application Development',2,4),(46,'Data Analytics',2,3),(47,'Internet of Things',2,3),(48,'Cyber Security Fundamentals',2,3),(49,'Engineering Mathematics - I',3,4),(50,'Engineering Physics',3,4),(51,'Engineering Chemistry',3,4),(52,'Basics of Electronics',3,4),(53,'Engineering Graphics',3,3),(54,'Workshop Practice',3,2),(55,'Engineering Mathematics - II',3,4),(56,'Network Analysis',3,4),(57,'Electronic Devices',3,4),(58,'Digital Electronics',3,4),(59,'Signals and Systems',3,3),(60,'C Programming for ECE',3,3),(61,'Analog Circuits',3,4),(62,'Electromagnetic Theory',3,4),(63,'Communication Systems',3,4),(64,'Microprocessors and Microcontrollers',3,3),(65,'Control Systems',3,3),(66,'Electronic Measurements',3,3),(67,'Digital Communication',3,4),(68,'VLSI Design',3,4),(69,'Antenna and Wave Propagation',3,4),(70,'Digital Signal Processing',3,3),(71,'Optical Communication',3,3),(72,'Embedded Systems',3,3),(73,'Engineering Mathematics - I',4,4),(74,'Engineering Physics',4,4),(75,'Engineering Chemistry',4,4),(76,'Engineering Mechanics',4,4),(77,'Engineering Graphics',4,3),(78,'Workshop Technology',4,2),(79,'Engineering Mathematics - II',4,4),(80,'Engineering Thermodynamics',4,4),(81,'Mechanics of Solids',4,4),(82,'Manufacturing Processes',4,4),(83,'Fluid Mechanics',4,3),(84,'Material Science',4,3),(85,'Heat Transfer',4,4),(86,'Theory of Machines',4,4),(87,'Machine Drawing',4,4),(88,'Kinematics of Machinery',4,3),(89,'Engineering Materials',4,3),(90,'Metrology and Measurements',4,3),(91,'Dynamics of Machinery',4,4),(92,'Machine Design',4,4),(93,'CAD/CAM',4,4),(94,'Automobile Engineering',4,3),(95,'Refrigeration and Air Conditioning',4,3),(96,'Industrial Engineering',4,3),(97,'Engineering Mechanics',5,4),(98,'Building Materials',5,3),(99,'Surveying',5,4),(100,'Structural Analysis',5,4),(101,'Geotechnical Engineering',5,4),(102,'Transportation Engineering',5,4),(103,'Environmental Engineering',5,3),(104,'Construction Management',5,3),(105,'Hydraulics',5,4),(106,'Concrete Technology',5,3),(107,'Design of Structures',5,4),(108,'Foundation Engineering',5,3),(109,'Circuit Theory',6,4),(110,'Electromagnetic Fields',6,4),(111,'Electrical Machines',6,4),(112,'Power Systems',6,4),(113,'Control Systems',6,4),(114,'Power Electronics',6,4),(115,'Digital Signal Processing',6,3),(116,'Renewable Energy Systems',6,3),(117,'Microprocessors',6,4),(118,'High Voltage Engineering',6,3),(119,'Smart Grid Technology',6,3),(120,'Electric Drives',6,3);
/*!40000 ALTER TABLE `subject` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-27 21:35:48
