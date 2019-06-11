/*
 Navicat Premium Data Transfer

 Source Server         : Local_3306_root
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : localhost:3306
 Source Schema         : well_data

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 01/06/2019 18:40:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_notify_channel
-- ----------------------------
DROP TABLE IF EXISTS `t_notify_channel`;
CREATE TABLE `t_notify_channel` (
  `channel_id` bigint(30) NOT NULL COMMENT '主键',
  `channel_name` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '名字',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `state` tinyint(2) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`channel_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知通道表';

-- ----------------------------
-- Records of t_notify_channel
-- ----------------------------
BEGIN;
INSERT INTO `t_notify_channel` VALUES (1129650133014663170, '异常通知', '2019-05-18 15:29:14', 1);
COMMIT;

-- ----------------------------
-- Table structure for t_notify_channel_user
-- ----------------------------
DROP TABLE IF EXISTS `t_notify_channel_user`;
CREATE TABLE `t_notify_channel_user` (
  `user_id` bigint(30) NOT NULL COMMENT '主键',
  `channel_id` bigint(30) DEFAULT '0' COMMENT '通道Id',
  `full_name` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '名字',
  `open_id` varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT 'openId',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `state` tinyint(2) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知用户表';

-- ----------------------------
-- Records of t_notify_channel_user
-- ----------------------------
BEGIN;
INSERT INTO `t_notify_channel_user` VALUES (1, 1129650133014663170, '异常通知', 'ooPE56M2H3HOuAiilxOGqu3CM-hE', '2019-05-18 15:30:13', 1);
COMMIT;

-- ----------------------------
-- Table structure for t_notify_msg
-- ----------------------------
DROP TABLE IF EXISTS `t_notify_msg`;
CREATE TABLE `t_notify_msg` (
  `msg_id` bigint(30) NOT NULL COMMENT '主键',
  `title` varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '标题',
  `content` text COLLATE utf8mb4_unicode_ci COMMENT '内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `state` tinyint(2) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`msg_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知消息表';

-- ----------------------------
-- Table structure for t_sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_menu`;
CREATE TABLE `t_sys_menu` (
  `menu_id` bigint(30) NOT NULL COMMENT '主键',
  `parent_id` bigint(30) DEFAULT '0' COMMENT '父Id',
  `name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '名称',
  `icon` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '图标',
  `url` varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '路径',
  `perms` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '授权标识',
  `type` tinyint(2) DEFAULT '0' COMMENT '类型 1目录 2菜单 3按钮',
  `sort` tinyint(2) DEFAULT '1' COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `state` tinyint(2) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统菜单表';

-- ----------------------------
-- Records of t_sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `t_sys_menu` VALUES (1115589060586364929, 0, '系统管理', 'setting', '', '', 1, 1, '2019-04-09 20:15:33', '2019-05-17 10:00:24', 1);
INSERT INTO `t_sys_menu` VALUES (1115589060632502273, 1115589060586364929, '菜单管理', 'menu', 'sys/menu', '', 2, 1, '2019-04-09 20:15:33', '2019-05-17 10:00:24', 1);
INSERT INTO `t_sys_menu` VALUES (1115589060666056706, 1115589060632502273, '添加', '', '', 'sys:menu:save', 3, 1, '2019-04-09 20:15:33', '2019-05-17 10:00:24', 1);
INSERT INTO `t_sys_menu` VALUES (1115589060703805441, 1115589060632502273, '删除', '', '', 'sys:menu:remove', 3, 2, '2019-04-09 20:15:33', '2019-05-17 10:00:24', 1);
INSERT INTO `t_sys_menu` VALUES (1115589060733165569, 1115589060632502273, '修改', '', '', 'sys:menu:modify', 3, 3, '2019-04-09 20:15:33', '2019-05-17 10:00:24', 1);
INSERT INTO `t_sys_menu` VALUES (1115589060762525697, 1115589060632502273, '查询', '', '', 'sys:menu:info,sys:menu:list', 3, 4, '2019-04-09 20:15:33', '2019-05-17 10:00:24', 1);
INSERT INTO `t_sys_menu` VALUES (1115589060791885825, 1115589060586364929, '用户管理', 'my', 'sys/user', '', 2, 2, '2019-04-09 20:15:33', '2019-05-17 10:00:24', 1);
INSERT INTO `t_sys_menu` VALUES (1115589060833828866, 1115589060791885825, '添加', '', '', 'sys:user:save', 3, 1, '2019-04-09 20:15:33', '2019-05-17 10:00:24', 1);
INSERT INTO `t_sys_menu` VALUES (1115589060863188994, 1115589060791885825, '删除', '', '', 'sys:user:remove', 3, 2, '2019-04-09 20:15:33', '2019-05-17 10:00:24', 1);
INSERT INTO `t_sys_menu` VALUES (1115589060888354818, 1115589060791885825, '修改', '', '', 'sys:user:modify', 3, 3, '2019-04-09 20:15:33', '2019-05-17 10:00:24', 1);
INSERT INTO `t_sys_menu` VALUES (1115589060913520642, 1115589060791885825, '查询', '', '', 'sys:user:info,sys:user:list', 3, 4, '2019-04-09 20:15:33', '2019-05-17 10:00:24', 1);
INSERT INTO `t_sys_menu` VALUES (1115589060938686466, 1115589060586364929, '角色管理', 'el-icon-menu', 'sys/role', '', 2, 3, '2019-04-09 20:15:33', '2019-05-17 10:00:24', 1);
INSERT INTO `t_sys_menu` VALUES (1115589060972240898, 1115589060938686466, '添加', '', '', 'sys:role:save', 3, 1, '2019-04-09 20:15:33', '2019-05-17 10:00:24', 1);
INSERT INTO `t_sys_menu` VALUES (1115589061009989633, 1115589060938686466, '删除', '', '', 'sys:role:remove', 3, 2, '2019-04-09 20:15:33', '2019-05-17 10:00:24', 1);
INSERT INTO `t_sys_menu` VALUES (1115589061039349761, 1115589060938686466, '修改', '', '', 'sys:role:modify', 3, 3, '2019-04-09 20:15:33', '2019-05-17 10:00:24', 1);
INSERT INTO `t_sys_menu` VALUES (1115589061072904194, 1115589060938686466, '查询', '', '', 'sys:role:info,sys:role:list', 3, 4, '2019-04-09 20:15:33', '2019-05-17 10:00:25', 1);
COMMIT;

-- ----------------------------
-- Table structure for t_sys_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role`;
CREATE TABLE `t_sys_role` (
  `role_id` bigint(30) NOT NULL COMMENT '角色Id',
  `role_name` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '角色名称',
  `role_sign` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '角色标识',
  `remark` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `state` tinyint(2) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统角色表';

-- ----------------------------
-- Records of t_sys_role
-- ----------------------------
BEGIN;
INSERT INTO `t_sys_role` VALUES (1115913151000342529, '普通管理员', 'admin', '', '2019-04-10 17:43:23', '2019-05-17 11:22:03', 1);
INSERT INTO `t_sys_role` VALUES (1116156809247068162, '123', '123', '', '2019-04-11 09:51:35', '2019-05-29 22:29:05', 1);
COMMIT;

-- ----------------------------
-- Table structure for t_sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role_menu`;
CREATE TABLE `t_sys_role_menu` (
  `rm_id` bigint(30) NOT NULL COMMENT '主键',
  `role_id` bigint(30) DEFAULT '0' COMMENT '角色Id',
  `menu_id` bigint(30) DEFAULT '0' COMMENT '菜单Id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `state` tinyint(2) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`rm_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色与菜单关联表';

-- ----------------------------
-- Records of t_sys_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `t_sys_role_menu` VALUES (1129225579406499841, 1115913151000342529, 1115589060913520642, '2019-05-17 11:22:13', '2019-05-17 11:22:13', 1);
INSERT INTO `t_sys_role_menu` VALUES (1129225579452637185, 1115913151000342529, 1115589061072904194, '2019-05-17 11:22:13', '2019-05-17 11:22:13', 1);
INSERT INTO `t_sys_role_menu` VALUES (1129225579465220098, 1115913151000342529, -888, '2019-05-17 11:22:13', '2019-05-17 11:22:13', 1);
INSERT INTO `t_sys_role_menu` VALUES (1129225579477803009, 1115913151000342529, 1115589060586364929, '2019-05-17 11:22:13', '2019-05-17 11:22:13', 1);
INSERT INTO `t_sys_role_menu` VALUES (1129225579490385922, 1115913151000342529, 1115589060791885825, '2019-05-17 11:22:13', '2019-05-17 11:22:13', 1);
INSERT INTO `t_sys_role_menu` VALUES (1129225579507163137, 1115913151000342529, 1115589060938686466, '2019-05-17 11:22:13', '2019-05-17 11:22:13', 1);
INSERT INTO `t_sys_role_menu` VALUES (1133742056096657409, 1116156809247068162, 1115589060913520642, '2019-05-29 22:29:05', '2019-05-29 22:29:05', 1);
INSERT INTO `t_sys_role_menu` VALUES (1133742056117628930, 1116156809247068162, -888, '2019-05-29 22:29:05', '2019-05-29 22:29:05', 1);
INSERT INTO `t_sys_role_menu` VALUES (1133742056142794754, 1116156809247068162, 1115589060586364929, '2019-05-29 22:29:05', '2019-05-29 22:29:05', 1);
INSERT INTO `t_sys_role_menu` VALUES (1133742056167960577, 1116156809247068162, 1115589060791885825, '2019-05-29 22:29:05', '2019-05-29 22:29:05', 1);
COMMIT;

-- ----------------------------
-- Table structure for t_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user`;
CREATE TABLE `t_sys_user` (
  `user_id` bigint(30) NOT NULL COMMENT '主键',
  `user_name` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '名字',
  `avatar` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '头像',
  `mobile` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '手机号',
  `salt` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '盐值',
  `password` varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '密码',
  `identity` tinyint(2) DEFAULT '1' COMMENT '身份标识',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `state` tinyint(2) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- ----------------------------
-- Records of t_sys_user
-- ----------------------------
BEGIN;
INSERT INTO `t_sys_user` VALUES (1112636920632954881, '张小康', '', '18820216400', 'bjnQQn8gqDHx', '0ab6711389693a8d8733a4de44b915cdc891b3630b57fc8fc8dd6951ee1e4b45', 2, '2019-04-10 17:44:24', '2019-05-17 10:23:13', 1);
INSERT INTO `t_sys_user` VALUES (1115913408618688514, 'zxk175', '', '18820216401', 'V51sv5Bz17nN', '7b359849a27d70cde6c22c8b1de000bdc8211ee6e6afd3d454cea8760ee73168', 1, '2019-04-10 17:44:24', '2019-05-17 10:23:13', 1);
COMMIT;

-- ----------------------------
-- Table structure for t_sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user_role`;
CREATE TABLE `t_sys_user_role` (
  `ur_id` bigint(30) NOT NULL COMMENT '主键',
  `user_id` bigint(30) unsigned DEFAULT '0' COMMENT '用户Id',
  `role_id` bigint(30) unsigned DEFAULT '0' COMMENT '角色Id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `state` tinyint(2) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`ur_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户与角色关联表';

-- ----------------------------
-- Records of t_sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `t_sys_user_role` VALUES (1115913408840986626, 1115913408618688514, 1115913151000342529, '2019-04-10 17:44:24', '2019-04-10 17:44:24', 1);
COMMIT;

-- ----------------------------
-- Table structure for t_sys_user_token
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user_token`;
CREATE TABLE `t_sys_user_token` (
  `token_id` bigint(30) NOT NULL COMMENT '主键',
  `user_id` bigint(30) DEFAULT NULL COMMENT '用户Id',
  `token` varchar(100) DEFAULT '' COMMENT 'token',
  `expire_time` varchar(30) DEFAULT NULL COMMENT '过期时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`token_id`) USING BTREE,
  UNIQUE KEY `token` (`token`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户Token表';

SET FOREIGN_KEY_CHECKS = 1;
