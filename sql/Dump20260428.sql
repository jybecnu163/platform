-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: cmdb
-- ------------------------------------------------------
-- Server version	8.0.35

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
-- Table structure for table `applications`
--

DROP TABLE IF EXISTS `applications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `applications` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `code` varchar(100) NOT NULL,
  `owner` varchar(100) DEFAULT NULL,
  `repo_url` varchar(500) DEFAULT NULL,
  `biz_line` varchar(100) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `applications`
--

LOCK TABLES `applications` WRITE;
/*!40000 ALTER TABLE `applications` DISABLE KEYS */;
INSERT INTO `applications` VALUES (1,'订单服务','order-service','张三','order-service:latest','交易','2026-04-27 05:38:54'),(2,'用户服务','user-service','李四','https://git.example.com/user-service.git','用户','2026-04-27 05:47:33'),(3,'商品服务','product-service','王五','https://git.example.com/product-service.git','商品','2026-04-27 05:47:33');
/*!40000 ALTER TABLE `applications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `deploy_strategies`
--

DROP TABLE IF EXISTS `deploy_strategies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `deploy_strategies` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `app_id` bigint NOT NULL,
  `env_id` bigint NOT NULL,
  `strategy_type` varchar(50) DEFAULT 'BATCH',
  `batch_config` json DEFAULT NULL,
  `health_check_url` varchar(500) DEFAULT NULL,
  `auto_promote` tinyint(1) DEFAULT '0',
  `rollback_enabled` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `app_id` (`app_id`),
  KEY `env_id` (`env_id`),
  CONSTRAINT `deploy_strategies_ibfk_1` FOREIGN KEY (`app_id`) REFERENCES `applications` (`id`),
  CONSTRAINT `deploy_strategies_ibfk_2` FOREIGN KEY (`env_id`) REFERENCES `environments` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deploy_strategies`
--

LOCK TABLES `deploy_strategies` WRITE;
/*!40000 ALTER TABLE `deploy_strategies` DISABLE KEYS */;
INSERT INTO `deploy_strategies` VALUES (1,1,4,'BATCH','[\"10%\", \"30%\", \"100%\"]','/api/health/check',1,1),(2,1,3,'ROLLING','[\"20%\"]','/health',0,1),(3,2,6,'BATCH','[\"100%\"]','/api/health',0,1),(4,1,1,'BATCH','[\"100%\"]','/health',1,1),(5,1,2,'BATCH','[\"50%\", \"100%\"]','/api/health',0,1);
/*!40000 ALTER TABLE `deploy_strategies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `deploy_tasks`
--

DROP TABLE IF EXISTS `deploy_tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `deploy_tasks` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `app_id` bigint NOT NULL,
  `env_id` bigint NOT NULL,
  `version` varchar(100) NOT NULL,
  `package_url` varchar(500) DEFAULT NULL,
  `status` enum('PENDING','RUNNING','BATCH_IN_PROGRESS','PAUSED','COMPLETED','FAILED','ROLLING_BACK','ROLLBACK_COMPLETED') DEFAULT 'PENDING',
  `rollback_to_version` varchar(100) DEFAULT NULL,
  `creator` varchar(100) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `app_id` (`app_id`),
  KEY `env_id` (`env_id`),
  CONSTRAINT `deploy_tasks_ibfk_1` FOREIGN KEY (`app_id`) REFERENCES `applications` (`id`),
  CONSTRAINT `deploy_tasks_ibfk_2` FOREIGN KEY (`env_id`) REFERENCES `environments` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deploy_tasks`
--

LOCK TABLES `deploy_tasks` WRITE;
/*!40000 ALTER TABLE `deploy_tasks` DISABLE KEYS */;
INSERT INTO `deploy_tasks` VALUES (1,1,4,'v1.2.3','http://localhost:8081/download/order-service-1.0.1.jar','PENDING','v1.2.2','张三','2026-04-27 05:47:33','2026-04-28 09:11:02'),(2,1,3,'v1.3.0-rc1','http://localhost:8081/download/order-service-1.0.1.jar','BATCH_IN_PROGRESS',NULL,'张三','2026-04-27 04:47:33','2026-04-28 09:11:02'),(3,2,6,'v2.0.1','http://localhost:8081/download/order-service-1.0.1.jar','COMPLETED','v2.0.0','李四','2026-04-26 05:47:33','2026-04-28 09:11:02'),(4,1,1,'1.0.0','http://localhost:8081/download/order-service-1.0.1.jar','PENDING',NULL,NULL,'2026-04-27 06:54:08','2026-04-28 09:11:02'),(5,1,1,'1.0.0','http://localhost:8081/download/order-service-1.0.1.jar','PENDING',NULL,NULL,'2026-04-27 06:59:00','2026-04-28 09:11:02'),(6,1,1,'1.0.0','http://localhost:8081/download/order-service-1.0.1.jar','PENDING',NULL,NULL,'2026-04-27 07:02:17','2026-04-28 09:11:02'),(7,1,1,'1.0.0','http://localhost:8081/download/order-service-1.0.1.jar','PENDING',NULL,NULL,'2026-04-27 07:04:06','2026-04-28 09:11:02'),(8,1,1,'1.0.0','http://localhost:8081/download/order-service-1.0.1.jar','PENDING',NULL,NULL,'2026-04-27 07:06:12','2026-04-28 09:11:02'),(9,1,1,'1.0.0','http://localhost:8081/download/order-service-1.0.1.jar','COMPLETED',NULL,NULL,'2026-04-27 07:10:39','2026-04-28 09:11:02'),(10,1,1,'1.3.0','http://localhost:8081/download/order-service-1.0.1.jar','FAILED',NULL,NULL,'2026-04-27 08:01:39','2026-04-28 09:11:02'),(11,1,1,'1.2.0','http://localhost:8081/download/order-service-1.0.1.jar','FAILED',NULL,NULL,'2026-04-27 08:04:10','2026-04-28 09:11:02'),(12,1,1,'1.0.2','http://localhost:8081/download/order-service-1.0.1.jar','FAILED',NULL,NULL,'2026-04-27 08:11:45','2026-04-28 09:11:02'),(13,1,1,'1.0.3','http://localhost:8081/download/order-service-1.0.1.jar','FAILED',NULL,NULL,'2026-04-27 08:16:33','2026-04-28 09:11:02'),(14,1,1,'1.0.3','http://localhost:8081/download/order-service-1.0.1.jar','FAILED',NULL,NULL,'2026-04-27 08:17:58','2026-04-28 09:11:02'),(15,1,1,'1.0.2','http://localhost:8081/download/order-service-1.0.1.jar','FAILED',NULL,NULL,'2026-04-27 08:25:23','2026-04-28 09:11:02'),(16,1,1,'1.0.3','http://localhost:8081/download/order-service-1.0.1.jar','FAILED',NULL,NULL,'2026-04-27 08:30:10','2026-04-28 09:11:02'),(17,1,1,'1.0.3','http://localhost:8081/download/order-service-1.0.1.jar','BATCH_IN_PROGRESS',NULL,NULL,'2026-04-27 08:39:11','2026-04-28 09:11:02'),(18,1,1,'1.0.3','http://localhost:8081/download/order-service-1.0.1.jar','BATCH_IN_PROGRESS',NULL,NULL,'2026-04-27 08:42:13','2026-04-28 09:11:02'),(19,1,1,'1.0.3','http://localhost:8081/download/order-service-1.0.1.jar','FAILED',NULL,NULL,'2026-04-27 08:52:08','2026-04-28 09:11:02'),(20,1,1,'1.0.3','http://localhost:8081/download/order-service-1.0.1.jar','FAILED',NULL,NULL,'2026-04-27 08:53:26','2026-04-28 09:11:02'),(21,1,1,'1.0.3','http://localhost:8081/download/order-service-1.0.1.jar','FAILED',NULL,NULL,'2026-04-27 08:55:17','2026-04-28 09:11:02'),(22,1,1,'1.0.3','http://localhost:8081/download/order-service-1.0.1.jar','COMPLETED',NULL,NULL,'2026-04-27 09:03:43','2026-04-28 09:11:02'),(23,1,1,'1.0.2','http://localhost:8081/download/order-service-1.0.1.jar','COMPLETED',NULL,NULL,'2026-04-27 17:39:59','2026-04-28 09:11:02'),(24,1,1,'1.0.2','http://localhost:8081/download/order-service-1.0.1.jar','FAILED',NULL,NULL,'2026-04-28 06:46:27','2026-04-28 09:11:02');
/*!40000 ALTER TABLE `deploy_tasks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `environments`
--

DROP TABLE IF EXISTS `environments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `environments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `app_id` bigint NOT NULL,
  `env_type` enum('DEV','TEST','PRE','PROD') NOT NULL,
  `nacos_namespace` varchar(200) DEFAULT NULL,
  `scaling_group_id` varchar(200) DEFAULT NULL,
  `isolation` varchar(50) DEFAULT 'SHARED',
  PRIMARY KEY (`id`),
  KEY `app_id` (`app_id`),
  CONSTRAINT `environments_ibfk_1` FOREIGN KEY (`app_id`) REFERENCES `applications` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `environments`
--

LOCK TABLES `environments` WRITE;
/*!40000 ALTER TABLE `environments` DISABLE KEYS */;
INSERT INTO `environments` VALUES (1,1,'DEV','051c1cce-2566-4887-a9a4-d9455b64d6d8',NULL,'SHARED'),(2,1,'TEST','test',NULL,'SHARED'),(3,1,'PRE','pre',NULL,'SHARED'),(4,1,'PROD','prod','sg-order-prod','EXCLUSIVE'),(5,2,'DEV','051c1cce-2566-4887-a9a4-d9455b64d6d8',NULL,'SHARED'),(6,2,'PROD','prod','sg-user-prod','EXCLUSIVE'),(7,3,'DEV','051c1cce-2566-4887-a9a4-d9455b64d6d8',NULL,'SHARED');
/*!40000 ALTER TABLE `environments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instances`
--

DROP TABLE IF EXISTS `instances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `instances` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `env_id` bigint NOT NULL,
  `ip` varchar(50) NOT NULL,
  `hostname` varchar(100) DEFAULT NULL,
  `status` enum('ONLINE','OFFLINE','FAULT') DEFAULT 'ONLINE',
  `agent_version` varchar(50) DEFAULT NULL,
  `last_heartbeat` timestamp NULL DEFAULT NULL,
  `app_version` varchar(50) DEFAULT NULL,
  `metadata` json DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `env_id` (`env_id`),
  CONSTRAINT `instances_ibfk_1` FOREIGN KEY (`env_id`) REFERENCES `environments` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instances`
--

LOCK TABLES `instances` WRITE;
/*!40000 ALTER TABLE `instances` DISABLE KEYS */;
INSERT INTO `instances` VALUES (1,1,'10.0.1.101','dev-order-01','ONLINE','1.0.0','2026-04-27 05:47:33','1.0.0-dev','{\"cpu\": 4, \"mem\": \"8G\"}'),(2,1,'10.0.1.102','dev-order-02','ONLINE','1.0.0','2026-04-27 05:47:33','1.0.0-dev','{\"cpu\": 4, \"mem\": \"8G\"}'),(3,4,'10.0.4.11','prod-order-01','ONLINE','1.0.0','2026-04-27 05:47:33','v1.2.2','{\"cpu\": 8, \"mem\": \"16G\"}'),(4,4,'10.0.4.12','prod-order-02','ONLINE','1.0.0','2026-04-27 05:47:33','v1.2.2','{\"cpu\": 8, \"mem\": \"16G\"}'),(5,4,'10.0.4.13','prod-order-03','ONLINE','1.0.0','2026-04-27 05:47:33','v1.2.2','{\"cpu\": 8, \"mem\": \"16G\"}'),(6,4,'10.0.4.14','prod-order-04','ONLINE','1.0.0','2026-04-27 05:47:33','v1.2.2','{\"cpu\": 8, \"mem\": \"16G\"}'),(7,5,'10.0.1.201','dev-user-01','ONLINE','1.0.0','2026-04-27 05:47:33','2.0.1-SNAPSHOT','{\"cpu\": 2, \"mem\": \"4G\"}'),(8,6,'10.0.4.21','prod-user-01','ONLINE','1.0.0','2026-04-27 05:47:33','2.0.0','{\"cpu\": 8, \"mem\": \"16G\"}'),(9,6,'10.0.4.22','prod-user-02','ONLINE','1.0.0','2026-04-27 05:47:33','2.0.0','{\"cpu\": 8, \"mem\": \"16G\"}'),(10,7,'10.0.1.301','dev-product-01','FAULT','1.0.0','2026-04-27 05:37:33','0.9.0','{\"cpu\": 2, \"mem\": \"4G\"}');
/*!40000 ALTER TABLE `instances` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scaling_history`
--

DROP TABLE IF EXISTS `scaling_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `scaling_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `env_id` bigint NOT NULL,
  `action` enum('SCALE_OUT','SCALE_IN') DEFAULT NULL,
  `old_count` int DEFAULT NULL,
  `new_count` int DEFAULT NULL,
  `triggered_by` varchar(50) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `detail` json DEFAULT NULL,
  `started_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `finished_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `env_id` (`env_id`),
  CONSTRAINT `scaling_history_ibfk_1` FOREIGN KEY (`env_id`) REFERENCES `environments` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scaling_history`
--

LOCK TABLES `scaling_history` WRITE;
/*!40000 ALTER TABLE `scaling_history` DISABLE KEYS */;
INSERT INTO `scaling_history` VALUES (1,4,'SCALE_OUT',4,6,'POLICY','SUCCESS','{\"added_ips\": [\"10.0.4.15\", \"10.0.4.16\"]}','2026-04-27 03:47:33','2026-04-27 04:47:33');
/*!40000 ALTER TABLE `scaling_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scaling_policies`
--

DROP TABLE IF EXISTS `scaling_policies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `scaling_policies` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `env_id` bigint NOT NULL,
  `metric` varchar(50) DEFAULT 'cpu',
  `threshold_up` int DEFAULT NULL,
  `threshold_down` int DEFAULT NULL,
  `scale_out_count` int DEFAULT '1',
  `scale_in_count` int DEFAULT '1',
  `duration` int DEFAULT NULL COMMENT '持续分钟',
  `cooldown` int DEFAULT NULL COMMENT '冷却秒',
  `min_instances` int DEFAULT '1',
  `max_instances` int DEFAULT '50',
  PRIMARY KEY (`id`),
  KEY `env_id` (`env_id`),
  CONSTRAINT `scaling_policies_ibfk_1` FOREIGN KEY (`env_id`) REFERENCES `environments` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scaling_policies`
--

LOCK TABLES `scaling_policies` WRITE;
/*!40000 ALTER TABLE `scaling_policies` DISABLE KEYS */;
INSERT INTO `scaling_policies` VALUES (1,4,'cpu',70,30,2,1,3,300,3,10),(2,6,'cpu',75,25,1,1,5,300,2,6);
/*!40000 ALTER TABLE `scaling_policies` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-28 17:17:13
