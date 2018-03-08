-- phpMyAdmin SQL Dump
-- version 4.6.6
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Aug 12, 2017 at 01:10 PM
-- Server version: 10.1.20-MariaDB
-- PHP Version: 7.0.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `id2477883_file_tracker`
--
CREATE DATABASE IF NOT EXISTS `id2477883_file_tracker` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `id2477883_file_tracker`;

-- --------------------------------------------------------

--
-- Table structure for table `agent`
--
DROP TABLE IF EXISTS `agent`;
CREATE TABLE `agent` (
  `id_agent` int(11) NOT NULL,
  `user_name` text NOT NULL,
  `password` text NOT NULL,
  `student_number` text NOT NULL,
  `full_name` text NOT NULL,
  `admin` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `agent`
--

INSERT INTO `agent` (`id_agent`, `user_name`, `password`, `student_number`, `full_name`, `admin`) VALUES
(1, 'admin', 'admin', '123456789', 'Administrative User', 1),
(24, 'joshoa', '12345', '0114300163', 'joshoa sampang', 0),
(25, 'test', 'test', '123456', 'test account', 0),
(26, 'Jomer', 'Jomer101894', '0113300588', 'Jomer Tiglao', 0),
(27, 'Charlesuser', 'pass', '124352', 'test account reyes', 0);

-- --------------------------------------------------------

--
-- Table structure for table `borrowed`
--
DROP TABLE IF EXISTS `borrowed`;
CREATE TABLE `borrowed` (
  `id` int(11) NOT NULL,
  `borrower` text,
  `item` text,
  `agent_id` int(11) NOT NULL,
  `date_created` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `borrowed`
--

INSERT INTO `borrowed` (`id`, `borrower`, `item`, `agent_id`, `date_created`) VALUES
(17, 'test', 'Format: QR_CODE\nContents: Book Type: Engineering Book\nBook title: COMMUNICATIONS ENGINEERING REVIEWER (Second Edition)\nAuthor: Romano Q. Neyra \nRaw bytes: (136 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 25, '2017-08-09 18:51:15'),
(27, 'Charlesuser', 'Format: QR_CODE\nContents: :\nbook title:\nfile tracker using qr code\n (book 1 of 3)\n\nname:\ncharles martin m reyes\njomer tiglao\njoshoa sampang\n\n\n\n---------------------------------------------------\nRaw bytes: (194 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 27, '2017-08-11 14:56:52');

--
-- Triggers `borrowed`
--
DELIMITER $$
CREATE TRIGGER `adjust_time_borrowed` BEFORE INSERT ON `borrowed` FOR EACH ROW SET NEW.date_created = DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 8 HOUR)
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `password`
--
DROP TABLE IF EXISTS `password`;
CREATE TABLE `password` (
  `password` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `password`
--

INSERT INTO `password` (`password`) VALUES
('spcf');

-- --------------------------------------------------------

--
-- Table structure for table `returned`
--
DROP TABLE IF EXISTS `returned`;
CREATE TABLE `returned` (
  `id` int(11) NOT NULL,
  `borrower` text NOT NULL,
  `item` text NOT NULL,
  `agent_id` int(11) NOT NULL,
  `date_created` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `returned`
--

INSERT INTO `returned` (`id`, `borrower`, `item`, `agent_id`, `date_created`) VALUES
(2, 'Jomer', 'Format: QR_CODE\nContents: PROJECTOR 3\nRaw bytes: (19 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 26, '2017-08-10 19:24:03'),
(3, 'Charlesuser', 'Format: QR_CODE\nContents: PROJECTOR 3\nRaw bytes: (19 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 27, '2017-08-11 14:38:36'),
(4, 'Charlesuser', 'Format: QR_CODE\nContents: Book Type:    Thesis Book  \nBook Title:     Basketball Scoreboard with Android-Based Display Controller (2)\nAuthors:         Ervin John M. Luberisco\n                        Jovit P. Porras\n                        Kristha Iza V. Valdez\n                        Robin Royce T. Yap\nRaw bytes: (324 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 27, '2017-08-11 14:39:09'),
(5, 'Charlesuser', 'Format: QR_CODE\nContents: Book Type:	                       On-The-Job Training Book\nName of Student:                Micheal Angelo L. Mallari\nCourse:                                  BS Electronics Engineering\nSchool Year:                         2016-2017\nRaw bytes: (274 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 27, '2017-08-11 14:40:06'),
(6, 'Charlesuser', 'Format: QR_CODE\nContents: book title:\nfile tracker using qr code\n (book 1 of 3)\n\nname:\ncharles martin m reyes\njomer tiglao\njoshoa sampang\nRaw bytes: (136 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 27, '2017-08-11 14:52:00'),
(7, 'Charlesuser', 'Format: QR_CODE\nContents: book title:\nfile tracker using qr code\n (book 1 of 3)\n\nname:\ncharles martin m reyes\njomer tiglao\njoshoa sampang\n\n\n\n---------------------------------------------------\nRaw bytes: (194 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 27, '2017-08-11 14:52:50'),
(8, 'Charlesuser', 'Format: QR_CODE\nContents: -----------------\nbook title:\nfile tracker using qr code\n (book 1 of 3)\n\nname:\ncharles martin m reyes\njomer tiglao\njoshoa sampang\n\n\n\n---------------------------------------------------\nRaw bytes: (194 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 27, '2017-08-11 14:53:43'),
(9, 'Charlesuser', 'Format: QR_CODE\nContents: :\n\nbook title:\nfile tracker using qr code\n (book 1 of 3)\n\nname:\ncharles martin m reyes\njomer tiglao\njoshoa sampang\n\n\n\n---------------------------------------------------\nRaw bytes: (194 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 27, '2017-08-11 14:55:38'),
(10, 'Charlesuser', 'Format: QR_CODE\nContents: book title:\nfile tracker using qr code\n (book 1 of 3)\n\nname:\ncharles martin m reyes\njomer tiglao\njoshoa sampang\n\n\n\n---------------------------------------------------\nRaw bytes: (194 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 27, '2017-08-11 14:56:16');

--
-- Triggers `returned`
--
DELIMITER $$
CREATE TRIGGER `adjust_time_returned` BEFORE INSERT ON `returned` FOR EACH ROW SET NEW.date_created = DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 8 HOUR)
$$
DELIMITER ;

-- --------------------------------------------------------

-- --------------------------------------------------------

--
-- Table structure for table `transaction_logs`
--
DROP TABLE IF EXISTS `transaction_logs`;
CREATE TABLE `transaction_logs` (
  `id` int(11) NOT NULL,
  `item` text NOT NULL,
  `agent_id` int(11) NOT NULL,
  `date_created` text,
  `borrowed` tinyint(1) NOT NULL DEFAULT '0',
  `returned` tinyint(1) NOT NULL DEFAULT '0',
  `reference_id` int(11) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `transaction_logs`
--

INSERT INTO `transaction_logs` (`id`, `item`, `agent_id`, `date_created`, `borrowed`, `returned`, `reference_id`, `deleted`) VALUES
(19, 'Format: QR_CODE\nContents: Book Type: Engineering Book\nBook title: COMMUNICATIONS ENGINEERING REVIEWER (Second Edition)\nAuthor: Romano Q. Neyra \nRaw bytes: (136 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 25, '2017-08-09 18:51:15', 1, 0, 17, 0),
(20, 'Format: QR_CODE\nContents: PROJECTOR 3\nRaw bytes: (19 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 26, '2017-08-10 19:23:19', 1, 0, 18, 0),
(21, 'Format: QR_CODE\nContents: PROJECTOR 3\nRaw bytes: (19 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 26, '2017-08-10 19:24:03', 0, 0, 2, 0),
(22, 'Format: QR_CODE\nContents: PROJECTOR 3\nRaw bytes: (19 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 27, '2017-08-11 14:35:20', 1, 0, 19, 0),
(23, 'Format: QR_CODE\nContents: Book Type:    Thesis Book  \nBook Title:     Basketball Scoreboard with Android-Based Display Controller (2)\nAuthors:         Ervin John M. Luberisco\n                        Jovit P. Porras\n                        Kristha Iza V. Valdez\n                        Robin Royce T. Yap\nRaw bytes: (324 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 27, '2017-08-11 14:36:19', 1, 0, 20, 1),
(24, 'Format: QR_CODE\nContents: Book Type:	                       On-The-Job Training Book\nName of Student:                Micheal Angelo L. Mallari\nCourse:                                  BS Electronics Engineering\nSchool Year:                         2016-2017\nRaw bytes: (274 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 27, '2017-08-11 14:37:06', 1, 0, 21, 0),
(25, 'Format: QR_CODE\nContents: PROJECTOR 3\nRaw bytes: (19 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 27, '2017-08-11 14:38:36', 0, 0, 3, 0),
(26, 'Format: QR_CODE\nContents: Book Type:    Thesis Book  \nBook Title:     Basketball Scoreboard with Android-Based Display Controller (2)\nAuthors:         Ervin John M. Luberisco\n                        Jovit P. Porras\n                        Kristha Iza V. Valdez\n                        Robin Royce T. Yap\nRaw bytes: (324 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 27, '2017-08-11 14:39:09', 0, 0, 4, 0),
(27, 'Format: QR_CODE\nContents: Book Type:	                       On-The-Job Training Book\nName of Student:                Micheal Angelo L. Mallari\nCourse:                                  BS Electronics Engineering\nSchool Year:                         2016-2017\nRaw bytes: (274 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 27, '2017-08-11 14:40:06', 0, 0, 5, 0),
(28, 'Format: QR_CODE\nContents: book title:\nfile tracker using qr code\n (book 1 of 3)\n\nname:\ncharles martin m reyes\njomer tiglao\njoshoa sampang\nRaw bytes: (136 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 27, '2017-08-11 14:51:22', 1, 0, 22, 0),
(29, 'Format: QR_CODE\nContents: book title:\nfile tracker using qr code\n (book 1 of 3)\n\nname:\ncharles martin m reyes\njomer tiglao\njoshoa sampang\nRaw bytes: (136 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 27, '2017-08-11 14:52:00', 0, 0, 6, 0),
(30, 'Format: QR_CODE\nContents: book title:\nfile tracker using qr code\n (book 1 of 3)\n\nname:\ncharles martin m reyes\njomer tiglao\njoshoa sampang\n\n\n\n---------------------------------------------------\nRaw bytes: (194 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 27, '2017-08-11 14:52:24', 1, 0, 23, 0),
(31, 'Format: QR_CODE\nContents: book title:\nfile tracker using qr code\n (book 1 of 3)\n\nname:\ncharles martin m reyes\njomer tiglao\njoshoa sampang\n\n\n\n---------------------------------------------------\nRaw bytes: (194 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 27, '2017-08-11 14:52:50', 0, 0, 7, 0),
(32, 'Format: QR_CODE\nContents: -----------------\nbook title:\nfile tracker using qr code\n (book 1 of 3)\n\nname:\ncharles martin m reyes\njomer tiglao\njoshoa sampang\n\n\n\n---------------------------------------------------\nRaw bytes: (194 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 27, '2017-08-11 14:53:07', 1, 0, 24, 0),
(33, 'Format: QR_CODE\nContents: -----------------\nbook title:\nfile tracker using qr code\n (book 1 of 3)\n\nname:\ncharles martin m reyes\njomer tiglao\njoshoa sampang\n\n\n\n---------------------------------------------------\nRaw bytes: (194 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 27, '2017-08-11 14:53:43', 0, 0, 8, 0),
(34, 'Format: QR_CODE\nContents: :\n\nbook title:\nfile tracker using qr code\n (book 1 of 3)\n\nname:\ncharles martin m reyes\njomer tiglao\njoshoa sampang\n\n\n\n---------------------------------------------------\nRaw bytes: (194 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 27, '2017-08-11 14:54:25', 1, 0, 25, 0),
(35, 'Format: QR_CODE\nContents: :\n\nbook title:\nfile tracker using qr code\n (book 1 of 3)\n\nname:\ncharles martin m reyes\njomer tiglao\njoshoa sampang\n\n\n\n---------------------------------------------------\nRaw bytes: (194 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 27, '2017-08-11 14:55:38', 0, 0, 9, 0),
(36, 'Format: QR_CODE\nContents: book title:\nfile tracker using qr code\n (book 1 of 3)\n\nname:\ncharles martin m reyes\njomer tiglao\njoshoa sampang\n\n\n\n---------------------------------------------------\nRaw bytes: (194 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 27, '2017-08-11 14:55:56', 1, 0, 26, 0),
(37, 'Format: QR_CODE\nContents: book title:\nfile tracker using qr code\n (book 1 of 3)\n\nname:\ncharles martin m reyes\njomer tiglao\njoshoa sampang\n\n\n\n---------------------------------------------------\nRaw bytes: (194 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 27, '2017-08-11 14:56:16', 0, 0, 10, 0),
(38, 'Format: QR_CODE\nContents: :\nbook title:\nfile tracker using qr code\n (book 1 of 3)\n\nname:\ncharles martin m reyes\njomer tiglao\njoshoa sampang\n\n\n\n---------------------------------------------------\nRaw bytes: (194 bytes)\nOrientation: null\nEC level: L\nBarcode image: null\n', 27, '2017-08-11 14:56:52', 1, 0, 27, 0);

--
-- Triggers `transaction_logs`
--
DELIMITER $$
CREATE TRIGGER `adjust_time_transaction_logs` BEFORE INSERT ON `transaction_logs` FOR EACH ROW SET NEW.date_created = DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 8 HOUR)
$$
DELIMITER ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `agent`
--
ALTER TABLE `agent`
  ADD PRIMARY KEY (`id_agent`);

--
-- Indexes for table `borrowed`
--
ALTER TABLE `borrowed`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `returned`
--
ALTER TABLE `returned`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `transaction_logs`
--
ALTER TABLE `transaction_logs`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `agent`
--
ALTER TABLE `agent`
  MODIFY `id_agent` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;
--
-- AUTO_INCREMENT for table `borrowed`
--
ALTER TABLE `borrowed`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;
--
-- AUTO_INCREMENT for table `returned`
--
ALTER TABLE `returned`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
--
-- AUTO_INCREMENT for table `transaction_logs`
--
ALTER TABLE `transaction_logs`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=39;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
