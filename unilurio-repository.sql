/*
Navicat MySQL Data Transfer

Source Server         : localMysql
Source Server Version : 50550
Source Host           : localhost:3306
Source Database       : unilurio-repository

Target Server Type    : MYSQL
Target Server Version : 50550
File Encoding         : 65001

Date: 2020-04-09 09:29:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `category`
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES ('1', 'Documentos de Gestão');
INSERT INTO `category` VALUES ('2', 'Estudos e Relatórios');
INSERT INTO `category` VALUES ('3', 'Legislação');
INSERT INTO `category` VALUES ('4', 'Financiamentos  Concedidos');
INSERT INTO `category` VALUES ('5', 'Documentação Interna');
INSERT INTO `category` VALUES ('6', 'Outros');

-- ----------------------------
-- Table structure for `document`
-- ----------------------------
DROP TABLE IF EXISTS `document`;
CREATE TABLE `document` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `category` int(11) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `datecreate` date DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `google_id` varchar(255) DEFAULT NULL,
  `parent` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `category` (`category`),
  KEY `type` (`type`),
  KEY `document_ibfk_3` (`parent`),
  CONSTRAINT `document_ibfk_3` FOREIGN KEY (`parent`) REFERENCES `document` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `document_ibfk_1` FOREIGN KEY (`category`) REFERENCES `category` (`id`),
  CONSTRAINT `document_ibfk_2` FOREIGN KEY (`type`) REFERENCES `type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=152 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of document
-- ----------------------------
INSERT INTO `document` VALUES ('145', 'Regulamento Pedagogico', '2012', '5', 'https://drive.google.com/open?id=1UjPuN5OTe0hBfZsG3EUD1FXCcli9gpe4', null, '1', '1UjPuN5OTe0hBfZsG3EUD1FXCcli9gpe4', null);
INSERT INTO `document` VALUES ('146', 'Regulamento FEUL.pdf', '2020', '6', 'https://drive.google.com/open?id=1KHOkzDu1KmxOdmqoe0UDEg4X3V-UMhH_', '2020-04-09', '1', '1KHOkzDu1KmxOdmqoe0UDEg4X3V-UMhH_', null);
INSERT INTO `document` VALUES ('147', 'Estatuto do Pessoal de Instituições Públicas de Ensino Superior.pdf', '2020', '6', 'https://drive.google.com/open?id=1SzTApxRFwoF5DZx2Lede1RBjHiLTX4eR', '2020-04-09', '1', '1SzTApxRFwoF5DZx2Lede1RBjHiLTX4eR', null);
INSERT INTO `document` VALUES ('148', 'regfae(1).pdf', '2020', '6', 'https://drive.google.com/open?id=19MHBfKn-mLhf3b8d99lpI0zH7Vhaut2L', '2020-04-09', '1', '19MHBfKn-mLhf3b8d99lpI0zH7Vhaut2L', null);
INSERT INTO `document` VALUES ('149', 'Estatuto UniLurio 2006.pdf', '2020', '6', 'https://drive.google.com/open?id=1yOZHUKVi9DtX78X5KTOiuKZxpYEjYcZV', '2020-04-09', '1', '1yOZHUKVi9DtX78X5KTOiuKZxpYEjYcZV', null);
INSERT INTO `document` VALUES ('150', 'Unilurio 2020 Versão FINAL.pdf', '2020', '6', 'https://drive.google.com/open?id=1WL9w4DBg6HBuDlFcNDRQskx-5iDifUSa', '2020-04-09', '1', '1WL9w4DBg6HBuDlFcNDRQskx-5iDifUSa', null);
INSERT INTO `document` VALUES ('151', 'Estatutos da UniLurio.pdf', '2020', '6', 'https://drive.google.com/open?id=1fM1RwR4SyI3dgTyYMTp0LCtExSHJvoRB', '2020-04-09', '1', '1fM1RwR4SyI3dgTyYMTp0LCtExSHJvoRB', null);

-- ----------------------------
-- Table structure for `document_tag`
-- ----------------------------
DROP TABLE IF EXISTS `document_tag`;
CREATE TABLE `document_tag` (
  `document` int(11) NOT NULL DEFAULT '0',
  `tag` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`document`,`tag`),
  KEY `tag` (`tag`),
  CONSTRAINT `document_tag_ibfk_1` FOREIGN KEY (`document`) REFERENCES `document` (`id`),
  CONSTRAINT `document_tag_ibfk_2` FOREIGN KEY (`tag`) REFERENCES `tag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of document_tag
-- ----------------------------

-- ----------------------------
-- Table structure for `tag`
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tag` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of tag
-- ----------------------------

-- ----------------------------
-- Table structure for `type`
-- ----------------------------
DROP TABLE IF EXISTS `type`;
CREATE TABLE `type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of type
-- ----------------------------
INSERT INTO `type` VALUES ('1', 'pdf', null);
INSERT INTO `type` VALUES ('2', 'doc', null);
INSERT INTO `type` VALUES ('3', 'excel', null);
INSERT INTO `type` VALUES ('4', 'word', null);
INSERT INTO `type` VALUES ('5', 'txt', null);
