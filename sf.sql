# Host: localhost  (Version: 5.5.47)
# Date: 2017-06-12 17:26:18
# Generator: MySQL-Front 5.3  (Build 4.234)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "department"
#

DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `reimbursementname` varchar(255) DEFAULT '' COMMENT '科室名字',
  `reimbursementuser` varchar(255) DEFAULT '' COMMENT '用户名',
  `password` varchar(255) DEFAULT '' COMMENT '密码',
  `time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `tel` varchar(255) DEFAULT '',
  `power` varchar(255) DEFAULT '' COMMENT 'su管理员ks科室',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='科室表';

#
# Data for table "department"
#

/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES (1,'骨科1','1','1','2017-05-16 11:38:29','1','ks'),(2,'骨科2','2','2','2017-05-17 10:41:57','2','ks'),(3,'骨科3','3','3','2017-05-17 10:42:34','3','ks');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;

#
# Structure for table "info"
#

DROP TABLE IF EXISTS `info`;
CREATE TABLE `info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `companyinfo` varchar(255) DEFAULT '',
  `softinfo` varchar(255) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

#
# Data for table "info"
#

/*!40000 ALTER TABLE `info` DISABLE KEYS */;
INSERT INTO `info` VALUES (1,'青岛灵基网络科技有限公司','科室随访软件系统');
/*!40000 ALTER TABLE `info` ENABLE KEYS */;

#
# Structure for table "patienthistory"
#

DROP TABLE IF EXISTS `patienthistory`;
CREATE TABLE `patienthistory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `zhenduan` varchar(255) DEFAULT '' COMMENT '诊断',
  `zhusu` varchar(255) DEFAULT '' COMMENT '主诉',
  `zkjc` varchar(255) DEFAULT '' COMMENT '专科检查',
  `zlfs` varchar(255) DEFAULT '' COMMENT '治疗方式',
  `img` varchar(255) DEFAULT '' COMMENT '辅助检查（图片）',
  `jws` text COMMENT '既往史',
  `patientid` varchar(255) DEFAULT '' COMMENT '患者id',
  `departmentid` varchar(255) DEFAULT '' COMMENT '所属科室id',
  `time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '治疗时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='患者病史表';

#
# Data for table "patienthistory"
#

/*!40000 ALTER TABLE `patienthistory` DISABLE KEYS */;
/*!40000 ALTER TABLE `patienthistory` ENABLE KEYS */;

#
# Structure for table "patientinfo"
#

DROP TABLE IF EXISTS `patientinfo`;
CREATE TABLE `patientinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT '' COMMENT '名字',
  `sex` int(11) DEFAULT '0' COMMENT '0男1女',
  `age` varchar(255) DEFAULT '' COMMENT '年龄',
  `number` varchar(255) DEFAULT '' COMMENT '住院号',
  `address` varchar(255) DEFAULT '' COMMENT '住址',
  `tel` varchar(11) DEFAULT '0' COMMENT '电话',
  `marryed` varchar(255) DEFAULT '' COMMENT '婚姻状态',
  `time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `reimbursement` varchar(255) DEFAULT '' COMMENT '报销方式1社保2农保3工伤4商报5自费',
  `departmentid` varchar(255) DEFAULT '' COMMENT '所属科室id',
  `doct` varchar(255) DEFAULT '' COMMENT '主治医师',
  `zhenduan` varchar(255) DEFAULT '' COMMENT '诊断',
  `zhusu` varchar(255) DEFAULT '' COMMENT '主诉',
  `zkjc` varchar(255) DEFAULT '' COMMENT '专科检查',
  `zlfs` varchar(255) DEFAULT '' COMMENT '治疗方式',
  `img0` varchar(255) DEFAULT '' COMMENT '辅助检查（图片）',
  `img1` varchar(255) DEFAULT '' COMMENT '辅助检查（图片）',
  `img2` varchar(255) DEFAULT '' COMMENT '辅助检查（图片）',
  `jws` text COMMENT '既往史',
  `zltime` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '治疗时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=42 DEFAULT CHARSET=utf8 COMMENT='患者信息表';

#
# Data for table "patientinfo"
#

/*!40000 ALTER TABLE `patientinfo` DISABLE KEYS */;
/*!40000 ALTER TABLE `patientinfo` ENABLE KEYS */;

#
# Structure for table "patientrecord"
#

DROP TABLE IF EXISTS `patientrecord`;
CREATE TABLE `patientrecord` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sfmode` int(11) DEFAULT '0' COMMENT '0电话随访1门诊复诊',
  `time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fs` varchar(255) DEFAULT '' COMMENT '随访医生',
  `istrue` int(11) DEFAULT '0' COMMENT '0需要随访1随访结束',
  `patientid` varchar(255) DEFAULT NULL COMMENT '患者id',
  `departmentid` varchar(255) DEFAULT '' COMMENT '所属科室id',
  `stime` datetime DEFAULT '0000-00-00 00:00:00' COMMENT '随访到期时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=59 DEFAULT CHARSET=utf8 COMMENT='随访记录表';

#
# Data for table "patientrecord"
#

/*!40000 ALTER TABLE `patientrecord` DISABLE KEYS */;
/*!40000 ALTER TABLE `patientrecord` ENABLE KEYS */;

#
# Structure for table "reimbursement"
#

DROP TABLE IF EXISTS `reimbursement`;
CREATE TABLE `reimbursement` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='报销';

#
# Data for table "reimbursement"
#

/*!40000 ALTER TABLE `reimbursement` DISABLE KEYS */;
INSERT INTO `reimbursement` VALUES (1,'社保'),(2,'农保'),(3,'工伤'),(4,'商报'),(5,'自费');
/*!40000 ALTER TABLE `reimbursement` ENABLE KEYS */;
