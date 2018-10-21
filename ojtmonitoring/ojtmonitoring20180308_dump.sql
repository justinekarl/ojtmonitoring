-- MySQL dump 10.13  Distrib 5.7.21, for Linux (x86_64)
--
-- Host: localhost    Database: ojtmonitoring
-- ------------------------------------------------------
-- Server version	5.7.21-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `company_ojt`
--

DROP TABLE IF EXISTS `company_ojt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `company_ojt` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `company_id` int(11) DEFAULT NULL,
  `approved_by_teacher_id` int(11) DEFAULT NULL,
  `log_date_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `accepted` tinyint(1) DEFAULT '0',
  `accepted_by_company_id` int(11) DEFAULT NULL,
  `accepted_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company_ojt`
--

LOCK TABLES `company_ojt` WRITE;
/*!40000 ALTER TABLE `company_ojt` DISABLE KEYS */;
INSERT INTO `company_ojt` VALUES (1,1,8,4,'2018-03-07 13:17:56',1,8,'2018-03-08'),(2,2,8,4,'2018-03-07 13:17:56',1,8,'2018-03-08'),(3,3,9,5,'2018-03-07 16:56:32',1,9,'2018-03-08');
/*!40000 ALTER TABLE `company_ojt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company_profile`
--

DROP TABLE IF EXISTS `company_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `company_profile` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `description` text,
  `moa_certified` tinyint(1) DEFAULT '0',
  `does_provide_allowance` tinyint(1) DEFAULT '0',
  `allowance` double DEFAULT NULL,
  `ojt_number` int(11) DEFAULT NULL,
  `college` text,
  `log_date_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company_profile`
--

LOCK TABLES `company_profile` WRITE;
/*!40000 ALTER TABLE `company_profile` DISABLE KEYS */;
INSERT INTO `company_profile` VALUES (1,6,'test description company computer company',1,1,1234,2,'Computer Studies','2018-03-07 12:26:14'),(2,7,'This company is an educational company!',1,1,2222,3,'Education','2018-03-07 12:26:49'),(3,8,'This is the best I.T. company evah!!',1,0,0,4,'Computer Studies','2018-03-07 12:35:18'),(4,9,'This company is better than the other company',0,0,0,7,'Education','2018-03-07 12:37:30');
/*!40000 ALTER TABLE `company_profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `educational_background`
--

DROP TABLE IF EXISTS `educational_background`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `educational_background` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `resume_details_id` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `name` text,
  `address` text,
  `from_date` date DEFAULT NULL,
  `to_date` date DEFAULT NULL,
  `log_date_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `educational_background`
--

LOCK TABLES `educational_background` WRITE;
/*!40000 ALTER TABLE `educational_background` DISABLE KEYS */;
INSERT INTO `educational_background` VALUES (1,1,0,'a','b',NULL,NULL,'2018-03-07 12:28:22'),(2,1,1,'C','d',NULL,NULL,'2018-03-07 12:28:22'),(3,1,2,'e','f',NULL,NULL,'2018-03-07 12:28:22'),(4,2,0,'uy','iyu',NULL,NULL,'2018-03-07 12:29:19'),(5,2,1,'iuy','iuy',NULL,NULL,'2018-03-07 12:29:19'),(6,2,2,'iuy','iuy',NULL,NULL,'2018-03-07 12:29:19'),(7,3,0,'t','t',NULL,NULL,'2018-03-07 12:30:06'),(8,3,1,'t','t',NULL,NULL,'2018-03-07 12:30:06'),(9,3,2,'t','t',NULL,NULL,'2018-03-07 12:30:06');
/*!40000 ALTER TABLE `educational_background` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resume_details`
--

DROP TABLE IF EXISTS `resume_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resume_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `accomplishments` text,
  `interests` text,
  `approved` tinyint(1) DEFAULT '0',
  `ojt_hours_needed` double DEFAULT NULL,
  `updated_by_teacher_id` int(11) DEFAULT NULL,
  `teacher_notes` text,
  `company_accepted` tinyint(1) DEFAULT '0',
  `log_date_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resume_details`
--

LOCK TABLES `resume_details` WRITE;
/*!40000 ALTER TABLE `resume_details` DISABLE KEYS */;
INSERT INTO `resume_details` VALUES (1,1,'test accomplishments 1234556787','Interst edajshdak asdkla a\ndakjsdla \nasd kanldsa\nsadklajlsjdad\nasdalkja\n',1,NULL,4,NULL,0,'2018-03-07 12:28:22'),(2,2,'iuy','iuyi',1,NULL,4,NULL,0,'2018-03-07 12:29:19'),(3,3,'t','t',1,NULL,5,NULL,0,'2018-03-07 12:30:06');
/*!40000 ALTER TABLE `resume_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resume_references`
--

DROP TABLE IF EXISTS `resume_references`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resume_references` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `resume_details_id` int(11) DEFAULT NULL,
  `name` text,
  `address` text,
  `phone_number` text,
  `occupation` text,
  `log_date_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resume_references`
--

LOCK TABLES `resume_references` WRITE;
/*!40000 ALTER TABLE `resume_references` DISABLE KEYS */;
INSERT INTO `resume_references` VALUES (1,1,'aa','cc','22','rwerw','2018-03-07 12:28:22'),(2,1,'ww','wwe','3939','qweqeqw','2018-03-07 12:28:22'),(3,1,'qwe','ewe','937','we','2018-03-07 12:28:22'),(4,2,'iuy','iy','489','iuy','2018-03-07 12:29:19'),(5,2,'iuy','iuy','49','iuy','2018-03-07 12:29:19'),(6,2,'iuy','iuy','489','iy','2018-03-07 12:29:19'),(7,3,'t','t','8','t','2018-03-07 12:30:06'),(8,3,'t','t','8','t','2018-03-07 12:30:06'),(9,3,'t','t','8','t','2018-03-07 12:30:06');
/*!40000 ALTER TABLE `resume_references` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_company_selected`
--

DROP TABLE IF EXISTS `student_company_selected`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `student_company_selected` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `company_id` int(11) DEFAULT NULL,
  `log_date_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_company_selected`
--

LOCK TABLES `student_company_selected` WRITE;
/*!40000 ALTER TABLE `student_company_selected` DISABLE KEYS */;
INSERT INTO `student_company_selected` VALUES (1,1,1,'2018-03-07 12:52:53'),(2,1,3,'2018-03-07 12:52:53'),(3,2,3,'2018-03-07 12:54:56'),(4,3,4,'2018-03-07 13:48:43');
/*!40000 ALTER TABLE `student_company_selected` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_notif`
--

DROP TABLE IF EXISTS `student_notif`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `student_notif` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `message` text,
  `deleted` tinyint(1) DEFAULT '0',
  `deleted_date` date DEFAULT NULL,
  `log_date_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_notif`
--

LOCK TABLES `student_notif` WRITE;
/*!40000 ALTER TABLE `student_notif` DISABLE KEYS */;
INSERT INTO `student_notif` VALUES (4,2,'Congratulations! You were accepted as an OJT for Company : computer company 1 ',0,NULL,'2018-03-07 16:52:00'),(5,1,'Congratulations! You were accepted as an OJT for Company : computer company 1 ',0,NULL,'2018-03-07 16:54:58'),(6,2,'Congratulations! You were accepted as an OJT for Company : computer company 1 ',0,NULL,'2018-03-07 16:54:58'),(7,3,'Congratulations! You were accepted as an OJT for Company : educ company 1 ',0,NULL,'2018-03-07 17:00:09');
/*!40000 ALTER TABLE `student_notif` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_ojt_attendance_log`
--

DROP TABLE IF EXISTS `student_ojt_attendance_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `student_ojt_attendance_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `student_id` int(11) DEFAULT NULL,
  `company_id` int(11) DEFAULT NULL,
  `login_date` text,
  `logout_date` text,
  `scan_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `login` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_ojt_attendance_log`
--

LOCK TABLES `student_ojt_attendance_log` WRITE;
/*!40000 ALTER TABLE `student_ojt_attendance_log` DISABLE KEYS */;
INSERT INTO `student_ojt_attendance_log` VALUES (1,2,8,'2018/03/08 00:00:46','2018/03/08 00:05:33','2018-03-07 16:00:48',0),(2,1,8,'2018/03/08 00:03:50',NULL,'2018-03-07 16:03:52',1),(3,3,9,'2018/03/08 01:01:30',NULL,'2018-03-07 17:01:32',1);
/*!40000 ALTER TABLE `student_ojt_attendance_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` text,
  `address` text,
  `phonenumber` text,
  `studentnumber` text,
  `teachernumber` text,
  `email` text,
  `department` text,
  `college` text,
  `username` text,
  `password` text,
  `accounttype` int(11) DEFAULT NULL,
  `log_date_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'student','address','123456','1234',NULL,'a@a.com',NULL,'Computer Studies','student','12345',1,'2018-03-07 12:21:43'),(2,'student1','addressaa','222222222','222',NULL,'b@b.cp,',NULL,'Computer Studies','student1','12345',1,'2018-03-07 12:22:19'),(3,'student2','add 12314','9038402983402984','38383838',NULL,'r@r.com',NULL,'Education','student2','12345',1,'2018-03-07 12:22:55'),(4,'computer teacher',NULL,'32323233',NULL,'32323233',NULL,'I.T.','Computer Studies','compteacher','12345',2,'2018-03-07 12:25:11'),(5,'educ teacher',NULL,'22222222222',NULL,'22222222222',NULL,'Math','Education','educteacher','12345',2,'2018-03-07 12:25:39'),(6,'computer company','test address','22222222222',NULL,NULL,NULL,'I.T.',NULL,'compcompany','12345',3,'2018-03-07 12:26:14'),(7,'educ company','adddd school','4353535',NULL,NULL,NULL,'Educational ',NULL,'educcompany','12345',3,'2018-03-07 12:26:49'),(8,'computer company 1','test computer company1','333333333333',NULL,NULL,NULL,'I.T.',NULL,'comcompany1','12345',3,'2018-03-07 12:35:18'),(9,'educ company 1','schoolllll','3-4-3-3-43-43-4',NULL,NULL,NULL,'Educational Company',NULL,'educcompany1','12345',3,'2018-03-07 12:37:30');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `work_experience`
--

DROP TABLE IF EXISTS `work_experience`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `work_experience` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `resume_details_id` int(11) DEFAULT NULL,
  `company_name` text,
  `address` text,
  `job_description` text,
  `duties_responsibilities` text,
  `log_date_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `work_experience`
--

LOCK TABLES `work_experience` WRITE;
/*!40000 ALTER TABLE `work_experience` DISABLE KEYS */;
INSERT INTO `work_experience` VALUES (1,1,'A','B','C','D','2018-03-07 12:28:22'),(2,1,'E','F','G','H','2018-03-07 12:28:22'),(3,2,'iuy','yiuy','iuy','iuy','2018-03-07 12:29:19'),(4,2,'iuy','iuy','uyi','uy','2018-03-07 12:29:19'),(5,3,'t','t','t','t','2018-03-07 12:30:06'),(6,3,'t','t','t','t','2018-03-07 12:30:06');
/*!40000 ALTER TABLE `work_experience` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-03-08  1:05:53
