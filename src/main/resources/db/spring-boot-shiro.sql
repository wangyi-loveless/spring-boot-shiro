/*
SQLyog Trial v12.12 (32 bit)
MySQL - 5.6.21-log : Database - spring-boot-shiro
*********************************************************************
*/

CREATE DATABASE `spring-boot-shiro` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;

USE `spring-boot-shiro`;

/*Table structure for table `area` */

CREATE TABLE `area` (
  `id` bigint(20) NOT NULL,
  `name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `nameIndex` (`name`),
  KEY `pidIndex` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `area` */

/*Table structure for table `menu` */

CREATE TABLE `menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL,
  `sequence` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `url` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nameIndex` (`name`),
  KEY `pidIndex` (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `menu` */

insert  into `menu`(`id`,`name`,`pid`,`sequence`,`status`,`url`) values (1,'系统管理',0,1,1,''),(2,'用户管理',1,1,1,'/view/user'),(4,'菜单管理',1,3,1,'/view/menu'),(5,'服务管理',0,2,1,NULL),(6,'服务一',5,1,1,'/view/service1'),(7,'服务二',5,2,1,'/view/service2'),(23,'角色管理',1,2,1,'/view/role');

/*Table structure for table `menu_operation` */

CREATE TABLE `menu_operation` (
  `menu_id` bigint(20) NOT NULL,
  `operation_id` bigint(20) NOT NULL,
  PRIMARY KEY (`menu_id`,`operation_id`),
  KEY `FKq143yil52fug1coubu8npl4j` (`operation_id`),
  CONSTRAINT `FKq143yil52fug1coubu8npl4j` FOREIGN KEY (`operation_id`) REFERENCES `menu` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `menu_operation` */

/*Table structure for table `operation` */

CREATE TABLE `operation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `codeIndex` (`code`),
  KEY `nameIndex` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `operation` */

insert  into `operation`(`id`,`code`,`name`) values (1,'add','新增'),(2,'delete','删除'),(3,'edit','修改'),(4,'query','查询');

/*Table structure for table `organization` */

CREATE TABLE `organization` (
  `id` bigint(20) NOT NULL,
  `area_id` bigint(20) DEFAULT NULL,
  `code` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `codeIndex` (`code`),
  KEY `nameIndex` (`name`),
  KEY `areaIndex` (`area_id`),
  KEY `pidIndex` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `organization` */

/*Table structure for table `role` */

CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `roleNameIndex` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `role` */

insert  into `role`(`id`,`role_name`) values (1,'admin'),(2,'manager'),(3,'normal'),(4,'test');

/*Table structure for table `role_menu` */

CREATE TABLE `role_menu` (
  `menu_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`menu_id`,`role_id`),
  KEY `FKqyvxw2gg2qk0wld3bqfcb58vq` (`role_id`),
  CONSTRAINT `FKfg4e2mb2318tph615gh44ll3` FOREIGN KEY (`menu_id`) REFERENCES `menu` (`id`),
  CONSTRAINT `FKqyvxw2gg2qk0wld3bqfcb58vq` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `role_menu` */

/*Table structure for table `trash` */

CREATE TABLE `trash` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `capacity` double NOT NULL,
  `classification` varchar(8) COLLATE utf8_bin NOT NULL,
  `code` varchar(20) COLLATE utf8_bin NOT NULL,
  `color` varchar(64) COLLATE utf8_bin NOT NULL,
  `community_code` varchar(20) COLLATE utf8_bin NOT NULL,
  `dimension` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `longitude` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `remarks` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `supplier_code` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `codeIndex` (`code`),
  KEY `colorIndex` (`color`),
  KEY `classificationIndex` (`classification`),
  KEY `communityCodeIndex` (`community_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `trash` */

/*Table structure for table `trash_launch` */

CREATE TABLE `trash_launch` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `classification` varchar(8) COLLATE utf8_bin DEFAULT NULL,
  `launch_date_time` bigint(20) DEFAULT NULL,
  `qr_code` varchar(10) COLLATE utf8_bin NOT NULL,
  `weight` double NOT NULL,
  `trash_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `qrCodeIndex` (`qr_code`),
  KEY `FKgcgf1cy8sf4hsjphqqfiggbk5` (`trash_id`),
  CONSTRAINT `FKgcgf1cy8sf4hsjphqqfiggbk5` FOREIGN KEY (`trash_id`) REFERENCES `trash` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `trash_launch` */

/*Table structure for table `trash_status` */

CREATE TABLE `trash_status` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `door_status` varchar(4) COLLATE utf8_bin DEFAULT NULL,
  `pcb_temperature` double NOT NULL,
  `record_date_time` bigint(20) DEFAULT NULL,
  `residual_capacity` double NOT NULL,
  `temperature` double NOT NULL,
  `trash_status` varchar(4) COLLATE utf8_bin DEFAULT NULL,
  `trash_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `doorStatusIndex` (`door_status`),
  KEY `trashStatusIndex` (`trash_status`),
  KEY `recordDateTimeIndex` (`record_date_time`),
  KEY `FK70cs7e1v92mckd2jemdce54ci` (`trash_id`),
  CONSTRAINT `FK70cs7e1v92mckd2jemdce54ci` FOREIGN KEY (`trash_id`) REFERENCES `trash` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `trash_status` */

insert  into `trash_status`(`id`,`door_status`,`pcb_temperature`,`record_date_time`,`residual_capacity`,`temperature`,`trash_status`,`trash_id`) values (1,'1',40.23,1535534319628,23.09,35,'1',NULL);

/*Table structure for table `user` */

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_balance` double DEFAULT NULL,
  `credit_rating` int(11) DEFAULT NULL,
  `gender` varchar(4) COLLATE utf8_bin DEFAULT NULL,
  `head_url` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `nick_name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `openid` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `phone_no` varchar(24) COLLATE utf8_bin DEFAULT NULL,
  `point` bigint(20) DEFAULT NULL,
  `salt` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `username` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `usernameUniqueIndex` (`username`),
  UNIQUE KEY `openidUniqueIndex` (`openid`),
  UNIQUE KEY `phoneNoUniqueIndex` (`phone_no`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `user` */

insert  into `user`(`id`,`account_balance`,`credit_rating`,`gender`,`head_url`,`nick_name`,`openid`,`password`,`phone_no`,`point`,`salt`,`username`) values (1,NULL,NULL,NULL,NULL,NULL,NULL,'e82fd21cad812a7c3c3109b3db09d000',NULL,NULL,'7db42eeaeec25aae6e113f09566fcf3f','admin'),(2,NULL,NULL,NULL,NULL,NULL,NULL,'123456',NULL,NULL,NULL,'manager'),(3,NULL,NULL,NULL,NULL,NULL,NULL,'123456',NULL,NULL,NULL,'normal');

/*Table structure for table `user_purchase` */

CREATE TABLE `user_purchase` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `buy_date_time` bigint(20) DEFAULT NULL,
  `qr_code` varchar(128) COLLATE utf8_bin NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `qrCodeIndex` (`qr_code`),
  KEY `FKij7al2h9mgxkahue6llaw0lo0` (`user_id`),
  CONSTRAINT `FKij7al2h9mgxkahue6llaw0lo0` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `user_purchase` */

/*Table structure for table `user_role` */

CREATE TABLE `user_role` (
  `role_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_id`,`user_id`),
  KEY `FK859n2jvi8ivhui0rl0esws6o` (`user_id`),
  CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `user_role` */

insert  into `user_role`(`role_id`,`user_id`) values (1,1),(2,2),(3,3);
