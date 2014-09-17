# ************************************************************
# Sequel Pro SQL dump
# Version 4096
#
# http://www.sequelpro.com/
# http://code.google.com/p/sequel-pro/
#
# Host: localhost (MySQL 5.6.15)
# Database: atombox
# Generation Time: 2014-09-17 20:08:53 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table image_groups
# ------------------------------------------------------------

DROP TABLE IF EXISTS `image_groups`;

CREATE TABLE "image_groups" (
  "id" char(36) CHARACTER SET ascii NOT NULL DEFAULT '',
  "title" varchar(50) DEFAULT NULL,
  "owner_user_id" char(36) CHARACTER SET ascii DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "owner_user_id" ("owner_user_id"),
  CONSTRAINT "image_groups_ibfk_1" FOREIGN KEY ("owner_user_id") REFERENCES "users" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);



# Dump of table images
# ------------------------------------------------------------

DROP TABLE IF EXISTS `images`;

CREATE TABLE "images" (
  "id" char(36) CHARACTER SET ascii NOT NULL DEFAULT '',
  "group_id" char(36) CHARACTER SET ascii DEFAULT NULL,
  "title" varchar(255) DEFAULT NULL,
  "description" text,
  "date_created" datetime DEFAULT NULL,
  "date_shot" datetime DEFAULT NULL,
  "original_file_name" varchar(255) DEFAULT NULL,
  "remote_path" varchar(255) DEFAULT NULL,
  "remote_file_name" varchar(255) DEFAULT NULL,
  "width_pixels" int(11) DEFAULT NULL,
  "height_pixels" int(11) DEFAULT NULL,
  "content_type" varchar(40) DEFAULT NULL,
  "bits_per_channel" tinyint(4) DEFAULT NULL,
  "channel_count" tinyint(4) DEFAULT NULL,
  "orientation" tinyint(1) DEFAULT NULL,
  "latitude" double DEFAULT NULL,
  "longitude" double DEFAULT NULL,
  "source_make" varchar(50) DEFAULT NULL,
  "source_model" varchar(50) DEFAULT NULL,
  PRIMARY KEY ("id"),
  KEY "group_id" ("group_id"),
  CONSTRAINT "images_ibfk_1" FOREIGN KEY ("group_id") REFERENCES "image_groups" ("id") ON DELETE CASCADE ON UPDATE CASCADE
);



# Dump of table users
# ------------------------------------------------------------

DROP TABLE IF EXISTS `users`;

CREATE TABLE "users" (
  "id" char(36) CHARACTER SET ascii NOT NULL DEFAULT '',
  "email" varchar(50) DEFAULT NULL,
  "password" char(40) CHARACTER SET ascii DEFAULT NULL,
  PRIMARY KEY ("id")
);




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
