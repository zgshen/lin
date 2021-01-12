/*
Navicat MySQL Data Transfer

Source Server         : centos-local
Source Server Version : 50731
Source Host           : 192.168.56.101:3306
Source Database       : lin0

Target Server Type    : MYSQL
Target Server Version : 50731
File Encoding         : 65001

Date: 2021-01-12 17:42:03
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for coupon_0
-- ----------------------------
DROP TABLE IF EXISTS `coupon_0`;
CREATE TABLE `coupon_0` (
  `id` bigint(20) NOT NULL,
  `coupon_code` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of coupon_0
-- ----------------------------
INSERT INTO `coupon_0` VALUES ('555799729854742528', '2161044258185515073', '2', '2021-01-12 17:09:42');
INSERT INTO `coupon_0` VALUES ('555807434052993024', '2161044441867833741', '2', '2021-01-12 17:40:19');
INSERT INTO `coupon_0` VALUES ('555807453158047744', '2161044442322361430', '2', '2021-01-12 17:40:23');