-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: imageportal
-- ------------------------------------------------------
-- Server version	8.0.41

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
-- Table structure for table `images_info`
--

DROP TABLE IF EXISTS `images_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `images_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `image_name` varchar(255) DEFAULT NULL,
  `image_status` enum('active','deleted','inactive') DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `priority` int DEFAULT NULL,
  `product_category` enum('banners','icon','promotion') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `images_info`
--

LOCK TABLES `images_info` WRITE;
/*!40000 ALTER TABLE `images_info` DISABLE KEYS */;
INSERT INTO `images_info` VALUES (1,'<p><span class=\"ql-size-huge\">gdjsjdnsds</span></p>','Test 1','active','https://imagedelivery.net/ZY5OwFLlTE2ePHl_IE20jg/e8222d70-3e43-4e0b-9950-7243114f8200/public','Aviator',1,'icon'),(2,'<ul><li>Yest</li><li>REst</li><li><strong>67</strong></li></ul>','Test 2','active','https://imagedelivery.net/ZY5OwFLlTE2ePHl_IE20jg/6f0006a6-3e25-4e3a-c930-d86dbde01200/public','Aviator',2,'banners'),(3,'<p>jbjbjb</p>','tesrtyy','active','https://imagedelivery.net/ZY5OwFLlTE2ePHl_IE20jg/001585c2-2337-475d-52bd-6df2e3c7c900/public','jjknj',7,'banners'),(4,'<p><u class=\"ql-size-large\"> Hakibets</u></p><ol><li><u class=\"ql-size-large\">Jetbros</u></li></ol><ul><li><u class=\"ql-size-large\">yurty</u></li></ul>','Test 2','active','https://imagedelivery.net/ZY5OwFLlTE2ePHl_IE20jg/16fb4f21-c8b7-459b-79d2-b6b17efe6b00/public','Test 1',4,'promotion'),(5,'<p class=\"ql-align-center\"><strong class=\"ql-size-large\"><em>Isak Condescemnding</em></strong></p>','Isak Celebration','active','https://imagedelivery.net/ZY5OwFLlTE2ePHl_IE20jg/d9b36e97-1ece-44e3-d7e3-d535c6e79900/public','Celebration',4,'banners'),(6,'<p>sjkdn sjdnsjds</p>','Treokn','active','https://imagedelivery.net/ZY5OwFLlTE2ePHl_IE20jg/dec216d2-014a-404a-cf07-9e15506d9c00/public','jdnsdns',4,'promotion'),(7,'<p>jbjkbn knknl</p>','ojojih','inactive','https://imagedelivery.net/ZY5OwFLlTE2ePHl_IE20jg/0e8048af-1ca0-43c5-6313-c243dac9c600/public','ugubj',8,'icon');
/*!40000 ALTER TABLE `images_info` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-03 21:48:20
