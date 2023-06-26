-- Adminer 4.8.1 MySQL 5.7.33 dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

CREATE DATABASE `transport` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `transport`;

CREATE TABLE `check_report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `msisdn` varchar(255) DEFAULT NULL,
  `sendto` varchar(20) DEFAULT NULL,
  `msg` varchar(255) DEFAULT NULL,
  `created_at` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;


CREATE TABLE `msisdn` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `msisdn` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2002 DEFAULT CHARSET=latin1;


CREATE TABLE `transport` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `msisdn` varchar(255) DEFAULT NULL,
  `sendto` varchar(20) DEFAULT NULL,
  `keyword` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1837997 DEFAULT CHARSET=latin1;


-- 2023-06-26 09:57:20
