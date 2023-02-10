/*
 Source Server         : rule_engine
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : engine

 Target Server Type    : MySQL
 Target Server Version : 80031

 Date: 10/02/2023 12:40:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for expression
-- ----------------------------
DROP TABLE IF EXISTS `expression`;
CREATE TABLE `expression` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '规则表达式ID',
  `exp` text COLLATE utf8mb3_bin NOT NULL COMMENT '规则表达式',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `update_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;

SET FOREIGN_KEY_CHECKS = 1;
