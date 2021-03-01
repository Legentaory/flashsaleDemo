/*
 Navicat Premium Data Transfer

 Source Server         : localhost_MySQL
 Source Server Type    : MySQL
 Source Server Version : 80021
 Source Host           : localhost:3306
 Source Schema         : flashsale_jiuzhang

 Target Server Type    : MySQL
 Target Server Version : 80021
 File Encoding         : 65001

 Date: 15/11/2020 13:49:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for flashsale_activity
-- ----------------------------
DROP TABLE IF EXISTS `flashsale_activity`;
CREATE TABLE `flashsale_activity`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'flash sale Id',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Name',
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
INSERT INTO `flashsale_activity` VALUES (1, 'Test1', 999, 2.88, 99.00, 0, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `flashsale_activity` VALUES (2, 'Test2', 999, 3.88, 99.00, 0, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `flashsale_activity` VALUES (3, 'Test3', 999, 8.99, 99.00, 0, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `flashsale_activity` VALUES (4, 'Test4', 999, 0.00, 99.00, 0, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `flashsale_activity` VALUES (5, 'Test5', 999, 0.00, 99.00, 0, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `flashsale_activity` VALUES (6, 'Test6', 999, 0.00, 99.00, 0, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `flashsale_activity` VALUES (7, 'Test8', 999, 0.00, 99.00, 16, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `flashsale_activity` VALUES (8, 'Test9', 999, 0.00, 99.00, 16, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `flashsale_activity` VALUES (9, 'Test10', 999, 99.99, 88.88, 1, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `flashsale_activity` VALUES (10, 'Apple iPhone 11 Pro 256GB', 999, 8769.00, 7769.00, 1, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `flashsale_activity` VALUES (11, 'Apple iPhone 12 256GB', 999, 8769.00, 7769.00, 1, '2020-11-01 19:21:20', '2020-11-20 16:50:40', 10, 0, 0);
INSERT INTO `flashsale_activity` VALUES (12, 'Apple iPhone 11 256GB', 999, 99.99, 88.88, 1, '2020-11-01 19:21:20', '2020-11-18 16:50:33', 10, 0, 0);
INSERT INTO `flashsale_activity` VALUES (19, 'iPhone12 Pro', 1001, 7888.00, 5888.00, 1, '2020-11-05 08:39:24', '2020-11-05 08:39:24', 10, 9, 0);

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
INSERT INTO `flashsale_commodity` VALUES (999, '黄金搭档中老年型', '测试分辨率：1920*1080(FHD)\n后置摄像头：1200万像素\n前置摄像头：500万像素\n核 数：其他\n频 率：以官网信息为准\n品牌： Apple\n商品名称：APPLEiPhone 6s Plus\n商品编号：1861098\n商品毛重：0.51kg\n商品产地：中国大陆\n热点：指纹识别，Apple Pay，金属机身，拍照神器\n系统：苹果（IOS）\n像素：1000-1600万\n机身内存：64GB', 999);
INSERT INTO `flashsale_commodity` VALUES (1001, 'iphone12 pro', 'iphone12 pro 非常好', 9999);

