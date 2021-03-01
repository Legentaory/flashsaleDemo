/*
 Navicat Premium Data Transfer

 Source Server         : localhost_MySQL
 Source Server Type    : MySQL
 Source Server Version : 80021
 Source Host           : localhost:3306
 Source Schema         : flash_sale_demo

 Target Server Type    : MySQL
 Target Server Version : 80021
 File Encoding         : 65001

 Date: 15/11/2020 15:45:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for flashsale_activity
-- ----------------------------
DROP TABLE IF EXISTS `flashsale_activity`;
CREATE TABLE `flashsale_activity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'flash sale Id',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'name',
  `commodity_id` bigint NOT NULL,
  `full_price` decimal(10, 2) NOT NULL COMMENT 'full price',
  `sale_price` decimal(10, 2) NOT NULL COMMENT 'sale price',
  `activity_status` int NOT NULL DEFAULT 0 COMMENT 'status，0:not available 1:normal',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT 'start time',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT 'end time',
  `total_stock` bigint UNSIGNED NOT NULL COMMENT 'total stock',
  `available_stock` int NOT NULL COMMENT 'available stock',
  `lock_stock` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT 'locked stock',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id`(`id`, `name`, `commodity_id`) USING BTREE,
  INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of flashsale_activity
-- ----------------------------
INSERT INTO `flashsale_activity` VALUES (1, 'test1', 999, 2.88, 99.00, 0, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `flashsale_activity` VALUES (2, 'test2', 999, 3.88, 99.00, 0, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `flashsale_activity` VALUES (3, 'test3', 999, 8.99, 99.00, 0, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `flashsale_activity` VALUES (4, 'test4', 999, 0.00, 99.00, 0, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `flashsale_activity` VALUES (5, 'test5', 999, 99.99, 88.88, 1, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `flashsale_activity` VALUES (6, 'test6', 999, 8769.00, 7769.00, 1, '2020-11-01 19:21:20', NULL, 0, 0, 0);


-- ----------------------------
-- Table structure for flashsale_commodity
-- ----------------------------
DROP TABLE IF EXISTS `flashsale_commodity`;
CREATE TABLE `flashsale_commodity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `commodity_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `commodity_desc` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `commodity_price` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1002 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of flashsale_commodity
-- ----------------------------
INSERT INTO `flashsale_commodity` VALUES (11, '11', '11', 100);
INSERT INTO `flashsale_commodity` VALUES (12, '11', '11', 100);
INSERT INTO `flashsale_commodity` VALUES (13, 'AIphone', 'resolution：1920*1080(FHD)\nback cam：12mpx\nfront cam：5mpx\ncores ：2  \nRefresh rate：60 Hz\nBrand： Apple\n name： APPLE AIPhone 100 Plus\nserial No：1861098\nweight：0.11kg\nmade in： Mainland china\nhighlight：Actully I have no idea\nSystem：Big AISir\nROM：64GB', 999);
INSERT INTO `flashsale_commodity` VALUES (14, 'iphone12 pro', 'iphone12 pro', 9999);

-- ----------------------------
-- Table structure for flashsale_order
-- ----------------------------
DROP TABLE IF EXISTS `flashsale_order`;
CREATE TABLE `flashsale_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `order_status` int NOT NULL,
  `flashsale_activity_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `order_amount` decimal(10, 0) UNSIGNED NOT NULL,
  `create_time` datetime(0) NOT NULL,
  `pay_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of flashsale_order
-- ----------------------------
INSERT INTO `flashsale_order` VALUES (1, '524743559841189888', 1, 19, 1234, 5888, '2020-11-13 07:44:40', NULL);
INSERT INTO `flashsale_order` VALUES (2, '524744128538480640', 2, 19, 1234, 5888, '2020-11-13 07:46:55', '2020-11-13 08:01:19');

-- ----------------------------
-- Table structure for flashsale_user
-- ----------------------------
DROP TABLE IF EXISTS `flashsale_user`;
CREATE TABLE `flashsale_user`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '地址',
  `phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of flashsale_user
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
