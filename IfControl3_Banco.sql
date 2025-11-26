CREATE DATABASE  IF NOT EXISTS `ifcontrol3` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `ifcontrol3`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: ifcontrol3
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Table structure for table `acao`
--

DROP TABLE IF EXISTS `acao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `acao` (
  `idAcao` int NOT NULL AUTO_INCREMENT,
  `idUser` int NOT NULL,
  `nSala` int DEFAULT NULL,
  `tipoAcao` varchar(20) NOT NULL,
  `dataAcao` date NOT NULL,
  `horaAcao` time NOT NULL,
  `statusAcao` tinyint(1) NOT NULL,
  `login` varchar(45) NOT NULL,
  PRIMARY KEY (`idAcao`),
  KEY `idUser_idx` (`idUser`),
  KEY `nSala_idx` (`nSala`),
  CONSTRAINT `idUser` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`),
  CONSTRAINT `nSala` FOREIGN KEY (`nSala`) REFERENCES `sala` (`nSala`)
) ENGINE=InnoDB AUTO_INCREMENT=891 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acao`
--

LOCK TABLES `acao` WRITE;
/*!40000 ALTER TABLE `acao` DISABLE KEYS */;
/*!40000 ALTER TABLE `acao` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `agendamento`
--

DROP TABLE IF EXISTS `agendamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `agendamento` (
  `id` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(50) NOT NULL,
  `autor` varchar(50) NOT NULL,
  `dataInicio` date NOT NULL,
  `dataFim` date NOT NULL,
  `hAtiv` time NOT NULL,
  `hDesat` time NOT NULL,
  `statusAgendamento` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `agendamento`
--

LOCK TABLES `agendamento` WRITE;
/*!40000 ALTER TABLE `agendamento` DISABLE KEYS */;
/*!40000 ALTER TABLE `agendamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `codir`
--

DROP TABLE IF EXISTS `codir`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `codir` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cod` varchar(200) NOT NULL,
  `funcao` varchar(20) NOT NULL,
  `dispositivo_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `funcao` (`funcao`),
  KEY `dispositivo_id` (`dispositivo_id`),
  CONSTRAINT `codir_ibfk_1` FOREIGN KEY (`dispositivo_id`) REFERENCES `dis` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `codir`
--

LOCK TABLES `codir` WRITE;
/*!40000 ALTER TABLE `codir` DISABLE KEYS */;
/*!40000 ALTER TABLE `codir` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conjuntodis`
--

DROP TABLE IF EXISTS `conjuntodis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `conjuntodis` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sala_id` int NOT NULL,
  `dis_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `sala_id` (`sala_id`),
  KEY `dis_id` (`dis_id`),
  CONSTRAINT `conjuntoDis_ibfk_1` FOREIGN KEY (`sala_id`) REFERENCES `sala` (`nSala`) ON DELETE CASCADE,
  CONSTRAINT `conjuntoDis_ibfk_2` FOREIGN KEY (`dis_id`) REFERENCES `dis` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conjuntodis`
--

LOCK TABLES `conjuntodis` WRITE;
/*!40000 ALTER TABLE `conjuntodis` DISABLE KEYS */;
/*!40000 ALTER TABLE `conjuntodis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diasdasemana`
--

DROP TABLE IF EXISTS `diasdasemana`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `diasdasemana` (
  `id` int NOT NULL AUTO_INCREMENT,
  `agendamento_id` int NOT NULL,
  `dia` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `agendamento_id` (`agendamento_id`),
  CONSTRAINT `diasdasemana_ibfk_1` FOREIGN KEY (`agendamento_id`) REFERENCES `agendamento` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diasdasemana`
--

LOCK TABLES `diasdasemana` WRITE;
/*!40000 ALTER TABLE `diasdasemana` DISABLE KEYS */;
/*!40000 ALTER TABLE `diasdasemana` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dis`
--

DROP TABLE IF EXISTS `dis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dis` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tipo` varchar(5) NOT NULL,
  `modelo` varchar(100) DEFAULT NULL,
  `marca` varchar(50) NOT NULL,
  `config` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dis`
--

LOCK TABLES `dis` WRITE;
/*!40000 ALTER TABLE `dis` DISABLE KEYS */;
/*!40000 ALTER TABLE `dis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dispositivosagendamento`
--

DROP TABLE IF EXISTS `dispositivosagendamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dispositivosagendamento` (
  `id` int NOT NULL AUTO_INCREMENT,
  `agendamento_id` int NOT NULL,
  `dispositivo` varchar(2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `agendamento_id` (`agendamento_id`),
  CONSTRAINT `dispositivosagendamento_ibfk_1` FOREIGN KEY (`agendamento_id`) REFERENCES `agendamento` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dispositivosagendamento`
--

LOCK TABLES `dispositivosagendamento` WRITE;
/*!40000 ALTER TABLE `dispositivosagendamento` DISABLE KEYS */;
/*!40000 ALTER TABLE `dispositivosagendamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nsalaagendamento`
--

DROP TABLE IF EXISTS `nsalaagendamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nsalaagendamento` (
  `id` int NOT NULL AUTO_INCREMENT,
  `agendamento_id` int NOT NULL,
  `nSala` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `agendamento_id` (`agendamento_id`),
  CONSTRAINT `nsalaagendamento_ibfk_1` FOREIGN KEY (`agendamento_id`) REFERENCES `agendamento` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nsalaagendamento`
--

LOCK TABLES `nsalaagendamento` WRITE;
/*!40000 ALTER TABLE `nsalaagendamento` DISABLE KEYS */;
/*!40000 ALTER TABLE `nsalaagendamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nsaladispositivo`
--

DROP TABLE IF EXISTS `nsaladispositivo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nsaladispositivo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `dispositivo_id` int NOT NULL,
  `nSala` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `dispositivo_id` (`dispositivo_id`),
  CONSTRAINT `nsaladispositivo_ibfk_1` FOREIGN KEY (`dispositivo_id`) REFERENCES `dis` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nsaladispositivo`
--

LOCK TABLES `nsaladispositivo` WRITE;
/*!40000 ALTER TABLE `nsaladispositivo` DISABLE KEYS */;
/*!40000 ALTER TABLE `nsaladispositivo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sala`
--

DROP TABLE IF EXISTS `sala`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sala` (
  `nSala` int NOT NULL,
  `est_Luzes` tinyint(1) DEFAULT NULL,
  `est_Ar` tinyint(1) DEFAULT NULL,
  `temperatura` double DEFAULT NULL,
  `umidade` double DEFAULT NULL,
  `temp_Ar` int DEFAULT NULL,
  `presenca` tinyint(1) DEFAULT NULL,
  `horaAtivacao` time DEFAULT NULL,
  `horaDesativacao` time DEFAULT NULL,
  `est_sala` tinyint(1) DEFAULT NULL,
  `est_datashow` tinyint(1) DEFAULT NULL,
  `conexao` tinyint(1) DEFAULT NULL,
  `ip` varchar(20) NOT NULL,
  PRIMARY KEY (`nSala`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sala`
--

LOCK TABLES `sala` WRITE;
/*!40000 ALTER TABLE `sala` DISABLE KEYS */;
INSERT INTO `sala` VALUES (1,NULL,NULL,24,16,NULL,1,NULL,NULL,0,NULL,1,'10.0.0.57');
/*!40000 ALTER TABLE `sala` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `idUser` int NOT NULL AUTO_INCREMENT,
  `siap` int NOT NULL,
  `login` varchar(20) NOT NULL,
  `senha` varchar(20) NOT NULL,
  `nome` varchar(50) NOT NULL,
  PRIMARY KEY (`idUser`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (15,1,'jdoe','senha123','John Doe');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-26 14:13:13
