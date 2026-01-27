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
-- Table structure for table `professor`
--

DROP TABLE IF EXISTS `professor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `professor` (
  `professor_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `department_id` int DEFAULT NULL,
  `subject_id` int DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `phone_number` varchar(15) DEFAULT NULL,
  `hire_date` date DEFAULT NULL,
  PRIMARY KEY (`professor_id`),
  KEY `department_id` (`department_id`),
  KEY `subject_id` (`subject_id`),
  KEY `idx_prof_name` (`name`),
  CONSTRAINT `professor_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `department` (`department_id`) ON DELETE CASCADE,
  CONSTRAINT `professor_ibfk_2` FOREIGN KEY (`subject_id`) REFERENCES `subject` (`subject_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `professor`
--

LOCK TABLES `professor` WRITE;
/*!40000 ALTER TABLE `professor` DISABLE KEYS */;
INSERT INTO `professor` VALUES (1,'Dr. Rajesh Kumar',1,1,'rajesh.kumar@university.edu','+91-5955576013','2010-09-21'),(2,'Dr. Meena Sharma',1,2,'meena.sharma@university.edu','+91-2068389339','2010-02-20'),(3,'Dr. Arun Patel',1,4,'arun.patel@university.edu','+91-3699215815','2008-09-25'),(4,'Dr. Priya Singh',1,8,'priya.singh@university.edu','+91-6669598921','2009-10-20'),(5,'Prof. Vikram Reddy',1,13,'vikram.reddy@university.edu','+91-3429439668','2014-08-12'),(6,'Prof. Anjali Gupta',1,14,'anjali.gupta@university.edu','+91-5132965885','2014-06-21'),(7,'Prof. Sanjay Verma',1,15,'sanjay.verma@university.edu','+91-3661397296','2015-08-22'),(8,'Prof. Neha Joshi',1,19,'neha.joshi@university.edu','+91-5369858158','2015-08-24'),(9,'Mr. Rahul Desai',1,20,'rahul.desai@university.edu','+91-6074731824','2020-06-02'),(10,'Ms. Sneha Kapoor',1,21,'sneha.kapoor@university.edu','+91-4091183740','2020-01-22'),(11,'Mr. Amit Trivedi',1,3,'amit.trivedi@university.edu','+91-5059907138','2020-12-10'),(12,'Ms. Pooja Nair',1,7,'pooja.nair@university.edu','+91-5317584452','2022-03-29'),(13,'Dr. Suresh Iyer',1,10,'suresh.iyer@university.edu','+91-1494942841','2017-07-06'),(14,'Dr. Kavita Menon',1,16,'kavita.menon@university.edu','+91-7545656406','2018-12-23'),(15,'Mr. Deepak Rao',1,22,'deepak.rao@university.edu','+91-3912988644','2022-03-25'),(16,'Dr. Sunil Nair',2,36,'sunil.nair@university.edu','+91-6435879081','2010-01-18'),(17,'Dr. Anitha Pillai',2,38,'anitha.pillai@university.edu','+91-6064807240','2011-06-07'),(18,'Prof. Manoj Kumar',2,43,'manoj.kumar@university.edu','+91-3308428329','2016-08-29'),(19,'Prof. Shweta Das',2,44,'shweta.das@university.edu','+91-8276875585','2015-05-09'),(20,'Ms. Ritu Sharma',2,45,'ritu.sharma@university.edu','+91-1484783648','2020-02-22'),(21,'Mr. Karthik Reddy',2,42,'karthik.reddy@university.edu','+91-7719011531','2021-08-26'),(22,'Dr. Venkatesh Iyer',2,47,'venkatesh.iyer@university.edu','+91-9726926789','2012-11-24'),(23,'Prof. Lakshmi Menon',2,42,'lakshmi.menon@university.edu','+91-7312620027','2017-07-28'),(24,'Mr. Rohit Gupta',2,25,'rohit.gupta@university.edu','+91-9009152468','2021-08-07'),(25,'Ms. Anjali Chatterjee',2,26,'anjali.chatterjee@university.edu','+91-5010007137','2018-12-29'),(26,'Dr. Prakash Singh',2,27,'prakash.singh@university.edu','+91-7645595967','2010-12-15'),(27,'Prof. Meera Patel',2,38,'meera.patel@university.edu','+91-1409809477','2018-09-04'),(28,'Dr. Gopalakrishnan Nair',3,63,'gopalakrishnan.nair@university.edu','+91-2899724903','2004-04-12'),(29,'Dr. Sharmila Banerjee',3,68,'sharmila.banerjee@university.edu','+91-6406490360','2009-10-21'),(30,'Prof. Ravi Shankar',3,59,'ravi.shankar@university.edu','+91-5625795213','2014-12-02'),(31,'Prof. Sunita Rao',3,72,'sunita.rao@university.edu','+91-4231698292','2015-01-19'),(32,'Mr. Aditya Joshi',3,49,'aditya.joshi@university.edu','+91-8981098758','2018-04-18'),(33,'Ms. Priyanka Reddy',3,50,'priyanka.reddy@university.edu','+91-2494053747','2020-06-04'),(34,'Dr. K. S. Nambiar',3,53,'ks.nambiar@university.edu','+91-8442358393','2010-12-04'),(35,'Prof. Arjun Menon',3,70,'arjun.menon@university.edu','+91-6439350623','2016-04-19'),(36,'Ms. Divya Sharma',3,65,'divya.sharma@university.edu','+91-4624848016','2020-06-15'),(37,'Mr. Varun Kumar',3,65,'varun.kumar@university.edu','+91-2941282010','2020-07-12'),(38,'Dr. Malini Sundaram',3,68,'malini.sundaram@university.edu','+91-8227806222','2009-07-06'),(39,'Prof. Suresh Iyengar',3,59,'suresh.iyengar@university.edu','+91-2108793314','2017-07-24'),(40,'Mr. Rajeev Nair',3,62,'rajeev.nair@university.edu','+91-6242323477','2023-11-16'),(41,'Ms. Ananya Das',3,70,'ananya.das@university.edu','+91-2680444936','2017-09-08'),(42,'Dr. R. K. Sharma',4,80,'rk.sharma@university.edu','+91-3314284513','2006-07-28'),(43,'Dr. S. P. Singh',4,92,'sp.singh@university.edu','+91-2847403823','2005-08-31'),(44,'Prof. Vijay Kumar',4,81,'vijay.kumar@university.edu','+91-6333293941','2014-10-02'),(45,'Prof. Geeta Verma',4,79,'geeta.verma@university.edu','+91-1514097535','2011-07-09'),(46,'Mr. Alok Patel',4,96,'alok.patel@university.edu','+91-7680786030','2018-12-24'),(47,'Ms. Nisha Reddy',4,93,'nisha.reddy@university.edu','+91-6436511622','2019-03-19'),(48,'Dr. Ashok Joshi',4,95,'ashok.joshi@university.edu','+91-3946882999','2009-06-03'),(49,'Prof. Kavita Singh',4,76,'kavita.singh@university.edu','+91-3549884548','2014-01-16'),(50,'Mr. Ramesh Gupta',4,74,'ramesh.gupta@university.edu','+91-7791253898','2021-12-28'),(51,'Ms. Swati Nair',4,87,'swati.nair@university.edu','+91-7148770282','2022-04-11'),(52,'Dr. P. S. Namboothiri',4,94,'ps.namboothiri@university.edu','+91-4390166579','2007-01-28'),(53,'Dr. S. Ramanathan',5,103,'s.ramanathan@university.edu','+91-8243355172','2009-05-19'),(54,'Dr. Anjali Deshpande',5,107,'anjali.deshpande@university.edu','+91-1678417027','2010-10-23'),(55,'Prof. Rajiv Kapoor',5,100,'rajiv.kapoor@university.edu','+91-7586686736','2014-07-21'),(56,'Prof. Meenakshi Iyer',5,106,'meenakshi.iyer@university.edu','+91-2127227131','2015-10-17'),(57,'Mr. Vikas Sharma',5,102,'vikas.sharma@university.edu','+91-9881210427','2019-07-09'),(58,'Ms. Radhika Menon',5,108,'radhika.menon@university.edu','+91-7139186673','2020-08-06'),(59,'Dr. K. Venkataraman',5,102,'k.venkataraman@university.edu','+91-3593255617','2010-07-23'),(60,'Prof. Shalini Gupta',5,103,'shalini.gupta@university.edu','+91-9069238820','2014-08-04'),(61,'Mr. Sameer Joshi',5,97,'sameer.joshi@university.edu','+91-8042655899','2023-03-28'),(62,'Ms. Anuradha Rao',5,104,'anuradha.rao@university.edu','+91-5456593354','2018-10-05'),(63,'Dr. M. S. Reddy',6,112,'ms.reddy@university.edu','+91-5061513119','2006-12-19'),(64,'Dr. Latha Nair',6,111,'latha.nair@university.edu','+91-2059411566','2005-12-26'),(65,'Prof. Sanjay Verma',6,119,'sanjay.verma@university.edu','+91-6331947841','2014-02-23'),(66,'Prof. Anjali Kapoor',6,118,'anjali.kapoor@university.edu','+91-9801162204','2013-12-01'),(67,'Mr. Rakesh Kumar',6,113,'rakesh.kumar@university.edu','+91-9101342070','2019-06-08'),(68,'Ms. Shruti Sharma',6,120,'shruti.sharma@university.edu','+91-2613602511','2020-03-19'),(69,'Dr. P. K. Menon',6,111,'pk.menon@university.edu','+91-6435966513','2012-07-21'),(70,'Prof. Ritu Das',6,111,'ritu.das@university.edu','+91-4816487215','2016-09-19'),(71,'Mr. Deepak Singh',6,116,'deepak.singh@university.edu','+91-2370593645','2020-06-10'),(72,'Ms. Preeti Nair',6,117,'preeti.nair@university.edu','+91-1914078920','2020-07-25'),(73,'Dr. S. Venkateswaran',6,117,'s.venkateswaran@university.edu','+91-9232894465','2011-04-03');
/*!40000 ALTER TABLE `professor` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-27 21:35:47
