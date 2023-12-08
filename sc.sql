/*
 Navicat Premium Data Transfer

 Source Server         : 阿里云DB
 Source Server Type    : MySQL
 Source Server Version : 50743
 Source Host           : 47.103.113.75:3306
 Source Schema         : sc

 Target Server Type    : MySQL
 Target Server Version : 50743
 File Encoding         : 65001

 Date: 08/12/2023 14:54:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `permission_id` int(8) NOT NULL,
  `permission_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `permission_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`permission_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (1, 'sys:queryUser', '查询用户', '/getUser');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` int(8) NOT NULL AUTO_INCREMENT,
  `account` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '账号',
  `user_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户密码',
  `last_login_time` datetime(0) DEFAULT NULL COMMENT '上一次登录时间',
  `enabled` tinyint(1) DEFAULT 1 COMMENT '账号是否可用。默认为1（可用）',
  `account_not_expired` tinyint(1) DEFAULT 1 COMMENT '是否过期。默认为1（没有过期）',
  `account_not_locked` tinyint(1) DEFAULT 1 COMMENT '账号是否锁定。默认为1（没有锁定）',
  `credentials_not_expired` tinyint(1) DEFAULT NULL COMMENT '证书（密码）是否过期。默认为1（没有过期）',
  `create_time` datetime(0) DEFAULT NULL,
  `update_time` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '888', '小张', '$2a$10$2mO7/KcswzO3SQU7TX3fiOfkypjdOn3tLBezV/tf2IJXdQu1BpxK2', '2023-08-16 09:45:53', 1, 1, 1, 1, '2023-08-09 17:49:20', '2023-08-09 17:49:22');

-- ----------------------------
-- Table structure for sys_user_permission_relation
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_permission_relation`;
CREATE TABLE `sys_user_permission_relation`  (
  `user_permission_relation_id` int(8) NOT NULL,
  `user_id` int(8) DEFAULT NULL,
  `permission_id` int(8) DEFAULT NULL,
  PRIMARY KEY (`user_permission_relation_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户权限关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_permission_relation
-- ----------------------------
INSERT INTO `sys_user_permission_relation` VALUES (1, 1, 1);

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '密码',
  `gender` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '性别',
  `phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '电话',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮箱',
  `avatar` blob COMMENT '头像',
  `test` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '测试列',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1245 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES (1, 'admin', '123456', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `user_info` VALUES (2, 'a', '1', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `user_info` VALUES (3, '4', '4', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `user_info` VALUES (11, '1', '123', 'women', '13301234566', '1889900@163.com', NULL, NULL);
INSERT INTO `user_info` VALUES (67, '2', '2', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `user_info` VALUES (1225, '123456', '123456', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `user_info` VALUES (1234, 'Lierick', 'e10adc3949ba59abbe56e057f20f883e', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `user_info` VALUES (1237, '0', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `user_info` VALUES (1238, '5', '111111', 'men', '12', '123@qq.com', NULL, NULL);
INSERT INTO `user_info` VALUES (1239, '7', '1234', 'men', NULL, NULL, NULL, NULL);
INSERT INTO `user_info` VALUES (1242, '3', '11111', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `user_info` VALUES (1243, '6', '123', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `user_info` VALUES (1244, 'iraina', '12345', NULL, NULL, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
