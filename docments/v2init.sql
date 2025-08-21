/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80033 (8.0.33)
 Source Host           : localhost:3306
 Source Schema         : lz-picture

 Target Server Type    : MySQL
 Target Server Version : 80033 (8.0.33)
 File Encoding         : 65001

 Date: 21/08/2025 21:55:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ai_conversation_log_info
-- ----------------------------
DROP TABLE IF EXISTS `ai_conversation_log_info`;
CREATE TABLE `ai_conversation_log_info`  (
  `conversation_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '对话记录编号',
  `session_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会话编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `input_text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户输入文本',
  `output_text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'AI返回文本',
  `request_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '请求时间',
  `response_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '响应时间',
  `tokens_used` int NOT NULL DEFAULT 0 COMMENT '消耗Tokens数量',
  `points_used` int NOT NULL DEFAULT 0 COMMENT '消耗积分',
  `conversation_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态（0=成功 1=失败）',
  `ai_status_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型返回码',
  `fail_reason` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '失败原因',
  `conversation_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '对话类型（0文本 1图片）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`conversation_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_session`(`session_id` ASC) USING BTREE,
  INDEX `idx_conversation_type`(`conversation_type` ASC) USING BTREE,
  INDEX `idx_request_time`(`request_time` ASC) USING BTREE,
  CONSTRAINT `ai_conversation_log_info_ibfk_1` FOREIGN KEY (`session_id`) REFERENCES `ai_conversation_session_info` (`session_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `ai_conversation_log_info_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI对话明细记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ai_conversation_log_info
-- ----------------------------

-- ----------------------------
-- Table structure for ai_conversation_session_info
-- ----------------------------
DROP TABLE IF EXISTS `ai_conversation_session_info`;
CREATE TABLE `ai_conversation_session_info`  (
  `session_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会话编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `conversation_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'AI会话编号',
  `session_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对话名称',
  `tokens_total_used` int NOT NULL DEFAULT 0 COMMENT '累计消耗Tokens',
  `points_total_used` int NOT NULL DEFAULT 0 COMMENT '累计消耗积分',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户IP地址',
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '平台',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除（0正常 1删除）',
  PRIMARY KEY (`session_id`) USING BTREE,
  INDEX `idx_user_session`(`user_id` ASC, `session_id` ASC) USING BTREE,
  CONSTRAINT `ai_conversation_session_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'AI会话管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ai_conversation_session_info
-- ----------------------------

-- ----------------------------
-- Table structure for ai_generate_log_info
-- ----------------------------
DROP TABLE IF EXISTS `ai_generate_log_info`;
CREATE TABLE `ai_generate_log_info`  (
  `log_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '记录编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `model_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型KEY',
  `model_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型类型',
  `input_file` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '输入文件',
  `prompt` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '提示词',
  `negative_prompt` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '负向提示词',
  `seed` float NULL DEFAULT NULL COMMENT '随机种子',
  `numbers` int NULL DEFAULT NULL COMMENT '数量',
  `input_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '输入参数',
  `task_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务编号',
  `output_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '返回结果',
  `file_urls` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '文件地址',
  `width` int NULL DEFAULT NULL COMMENT '宽度',
  `height` int NULL DEFAULT NULL COMMENT '高度',
  `file_size` bigint NOT NULL DEFAULT 0 COMMENT '文件大小',
  `request_time` datetime NOT NULL COMMENT '请求时间',
  `request_duration` bigint NULL DEFAULT NULL COMMENT '请求时长',
  `price_used` decimal(5, 2) NOT NULL DEFAULT 0.00 COMMENT '价格',
  `points_used` int NOT NULL DEFAULT 0 COMMENT '消耗的积分',
  `target_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参考对象',
  `log_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态',
  `ai_status_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型返回码',
  `fail_reason` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '失败原因',
  `has_public` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '是否发布',
  `has_statistics` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '是否统计',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户IP地址',
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '平台',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除',
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `fk_generate_user_id_log`(`user_id` ASC) USING BTREE,
  INDEX `fk_generate_model_key_log`(`model_key` ASC) USING BTREE,
  CONSTRAINT `fk_generate_model_key_log` FOREIGN KEY (`model_key`) REFERENCES `ai_model_params_info` (`model_key`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_generate_user_id_log` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户生成记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ai_model_params_info
-- ----------------------------
DROP TABLE IF EXISTS `ai_model_params_info`;
CREATE TABLE `ai_model_params_info`  (
  `model_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型编号',
  `model_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型KEY',
  `model_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型名称',
  `model_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型类型',
  `model` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型',
  `model_label` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `api_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `api_key` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '安全密钥',
  `secret_key` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '安全KEY',
  `price_use` decimal(5, 2) NOT NULL COMMENT '价格',
  `model_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型参数',
  `model_description` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型介绍',
  `usage_count` int NOT NULL DEFAULT 0 COMMENT '使用次数',
  `points_earned` bigint NOT NULL DEFAULT 0 COMMENT '赚取积分',
  `points_need` int NULL DEFAULT 0 COMMENT '积分',
  `extend_config` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '扩展配置',
  `params_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '状态',
  `order_num` int NOT NULL DEFAULT 10 COMMENT '排序',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`model_id`) USING BTREE,
  UNIQUE INDEX `ai_model_params_info_pk`(`model_key` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '模型参数表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ai_official_usage_log_info
-- ----------------------------
DROP TABLE IF EXISTS `ai_official_usage_log_info`;
CREATE TABLE `ai_official_usage_log_info`  (
  `log_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '记录编号',
  `user_id` bigint NULL DEFAULT NULL COMMENT '管理员编号',
  `model_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型编号',
  `operation_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作类型（如：data_analysis）',
  `input_params` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '输入参数（JSON格式）',
  `output_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '模型返回结果（JSON/Text格式）',
  `request_time` datetime NOT NULL COMMENT '请求时间',
  `request_duration` bigint NULL DEFAULT NULL COMMENT '请求时长（毫秒）',
  `tokens_used` int NOT NULL DEFAULT 0 COMMENT '消耗Tokens数量',
  `log_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '状态（0成功 1失败）',
  `ai_status_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型返回状态码',
  `fail_reason` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '失败原因',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除（0正常 1删除）',
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_operation_type`(`operation_type` ASC) USING BTREE,
  INDEX `idx_request_time`(`request_time` ASC) USING BTREE,
  INDEX `idx_status`(`log_status` ASC) USING BTREE,
  CONSTRAINT `ai_official_usage_log_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '官方AI操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ai_prompt_info
-- ----------------------------
DROP TABLE IF EXISTS `ai_prompt_info`;
CREATE TABLE `ai_prompt_info`  (
  `info_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '编号',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '提示内容',
  `prompt_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态',
  `order_num` int NOT NULL DEFAULT 10 COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`info_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '提示词信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ai_prompt_info
-- ----------------------------
INSERT INTO `ai_prompt_info` VALUES ('3869825925e941fb915c12f4299e1604', '午后心事', '女子裹浅杏羊绒开衫倚坐窗边，晨光穿透蕾丝帘形成朦胧光斑。湿发系灰绿真丝缎带，水珠沿脖颈滑入锁骨沟。左手轻捻泛黄书页，纸页边缘浮现金色尘埃，颊边被风吹起的窗纱营造动态柔焦前景。', '0', 1, 'admin', '2025-08-14 17:37:36', NULL, NULL, NULL);
INSERT INTO `ai_prompt_info` VALUES ('593b8808bbde4eefa53031f1663c52c6', '末日救赎', '废土风服饰，亚洲少女，粗糙的材质，破旧的粗纺的麻织物，头戴兜帽且蒙面，机械美学，废旧煤矿工厂，以灰暗色调为主，低饱和土黄色，冲击力和叛逆感， 末日美学，怪诞美学，艺术作品，逆光，胶片摄影，专业摄影作品。突出光影，救赎的样子。', '0', 1, 'admin', '2025-08-14 17:36:07', NULL, NULL, NULL);
INSERT INTO `ai_prompt_info` VALUES ('82a1d9c1d07f4b9492d7d03355e2fc4c', '粘土蛋糕师', '黏土动画风格的场景，一位橡皮泥制作的面包师在粉色调的户外环境中，开心地站在小桌旁装饰一个超大蛋糕。', '0', 1, 'admin', '2025-08-14 17:33:17', NULL, NULL, NULL);
INSERT INTO `ai_prompt_info` VALUES ('9788f87c966241ae91c0f2f78bc5e062', '情绪影片', '电影质感，胶片颗粒，非常规独特构图，大师光影，艺术氛围浓厚，哈苏，近景面部特写，妩媚，凌乱，小瑕疵，高清，情绪表达，叙事风格，视觉艺术，获奖作品。女大。', '0', 1, 'admin', '2025-08-14 17:34:52', NULL, NULL, NULL);
INSERT INTO `ai_prompt_info` VALUES ('d52b0fb18d7a4e8e8f96eee98f6c7b96', '未来战役', '下一代高科技机器人士兵的电影剧照，戴着黑金色面罩，手持发光的黑金色武器，头上有发光的黑金色光环，在夜晚，LED灯、霓虹灯、宇宙飞船、高对比度、动态、虚幻的引擎5、高度细节、光影艺术，超高分辨率、32K UHD、最好的质量，杰作。', '0', 1, 'admin', '2025-08-14 17:33:44', NULL, NULL, NULL);
INSERT INTO `ai_prompt_info` VALUES ('d85d0d2801ee44769265967a093d6c07', '蓝调梦核', '低饱和绿色色调，风吹过草地，长焦镜头特写，俯视视角，蓝调时刻，画面寂静而孤独，画面里一个极小的人，治愈系，胶片感，粗颗粒质感，杂志封面，获奖摄影作品。', '0', 1, 'admin', '2025-08-14 17:34:17', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for ai_user_usage_log_info
-- ----------------------------
DROP TABLE IF EXISTS `ai_user_usage_log_info`;
CREATE TABLE `ai_user_usage_log_info`  (
  `log_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '记录编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `model_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型编号',
  `input_params` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '输入参数',
  `output_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '返回结果',
  `request_time` datetime NOT NULL COMMENT '请求时间',
  `request_duration` bigint NULL DEFAULT NULL COMMENT '请求时长（毫秒）',
  `tokens_used` int NOT NULL DEFAULT 0 COMMENT '消耗Tokens数量',
  `points_used` int NOT NULL DEFAULT 0 COMMENT '消耗积分',
  `usage_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '使用类型（0AI扩图 1AI编辑 2AI搜索）',
  `target_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目标编号',
  `log_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态（0成功 1失败 2超时）',
  `ai_status_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模型返回码',
  `fail_reason` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '失败原因',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户IP地址',
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '平台',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除（0正常 1删除）',
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `idx_usage_type`(`usage_type` ASC) USING BTREE,
  INDEX `idx_status`(`log_status` ASC) USING BTREE,
  INDEX `idx_user_model`(`user_id` ASC, `model_id` ASC) USING BTREE,
  CONSTRAINT `ai_user_usage_log_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户AI使用记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ai_user_usage_log_info
-- ----------------------------

-- ----------------------------
-- Table structure for c_config_info
-- ----------------------------
DROP TABLE IF EXISTS `c_config_info`;
CREATE TABLE `c_config_info`  (
  `config_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `config_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置名称',
  `config_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置键名',
  `config_value` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置键值',
  `config_type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '配置类型（1值 2文件）',
  `config_is_in` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '是否内置',
  `order_num` int NOT NULL DEFAULT 10 COMMENT '配置排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE,
  UNIQUE INDEX `uk_c_config_info_config_name`(`config_name` ASC) USING BTREE,
  UNIQUE INDEX `uk_c_config_info_config_key`(`config_key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 79 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '配置信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of c_config_info
-- ----------------------------
INSERT INTO `c_config_info` VALUES (1, 'test', 'test', 'test', '1', '0', 1, 'admin', '2025-02-28 17:04:27', 'admin', '2025-02-28 22:34:39', '2131231');
INSERT INTO `c_config_info` VALUES (2, 'testFile', 'testFile', '/profile/upload/2025/02/28/pexels-photo-14471335_20250228173740A001.jpeg', '2', '0', 0, 'admin', '2025-02-28 17:37:50', 'admin', '2025-02-28 23:11:04', '31231');
INSERT INTO `c_config_info` VALUES (23, '登录注册是否需要验证码', 'user:login:captchaEnabled', 'true', '1', '0', 0, 'admin', '2025-03-18 23:00:32', 'admin', '2025-03-18 23:08:55', '是true 否不是ture就行');
INSERT INTO `c_config_info` VALUES (24, '空间最大文件数', 'picture:space:maxCount', '300', '1', '0', 1, 'admin', '2025-03-31 16:59:06', 'admin', '2025-03-31 16:59:30', NULL);
INSERT INTO `c_config_info` VALUES (25, '空间最大容量', 'picture:space:maxSize', '1', '1', '0', 1, 'admin', '2025-03-31 17:00:21', 'admin', '2025-07-30 15:29:46', '最大容量，单位GB');
INSERT INTO `c_config_info` VALUES (26, '封面图片压缩倍率百分比', 'picture:space:avatar:p', '60', '1', '0', 1, 'admin', '2025-04-02 17:23:37', 'admin', '2025-06-28 18:19:23', '封面图片压缩倍率百分比，[1,1000]小于100为缩小，大于100为放大。用户封面、表格处使用');
INSERT INTO `c_config_info` VALUES (27, '用户最大团队空间数', 'picture:space:max:1', '10', '1', '0', 2, 'admin', '2025-04-08 11:40:41', NULL, NULL, NULL);
INSERT INTO `c_config_info` VALUES (28, '用户最大个人空间数', 'picture:space:max:2', '10', '1', '0', 2, 'admin', '2025-04-08 11:41:06', NULL, NULL, NULL);
INSERT INTO `c_config_info` VALUES (29, '图片积分最大值', 'picture:points:max', '1000', '1', '0', 0, 'admin', '2025-04-09 01:00:46', NULL, NULL, NULL);
INSERT INTO `c_config_info` VALUES (30, '图片积分最小值', 'picture:points:min', '10', '1', '0', 0, 'admin', '2025-04-09 01:01:15', NULL, NULL, NULL);
INSERT INTO `c_config_info` VALUES (31, '首页图片固定高度', 'picture:index:height', '300', '1', '1', 1, 'admin', '2025-04-10 11:50:19', 'admin', '2025-06-14 02:07:18', NULL);
INSERT INTO `c_config_info` VALUES (32, '首页图片压缩倍率百分比', 'picture:index:p', '60', '1', '0', 1, 'admin', '2025-04-10 15:05:08', 'admin', '2025-08-19 23:56:33', NULL);
INSERT INTO `c_config_info` VALUES (33, '用户查看自己原图密钥时间', 'picture:look:original:timeout', '10', '1', '0', 1, 'admin', '2025-04-23 23:23:23', 'admin', '2025-04-24 00:15:31', NULL);
INSERT INTO `c_config_info` VALUES (34, '行为和浏览记录封面压缩倍率百分比', 'picture:cover:p', '20', '1', '0', 2, 'admin', '2025-05-22 16:38:26', 'admin', '2025-05-29 00:53:56', NULL);
INSERT INTO `c_config_info` VALUES (35, '账户密码免校验时间', 'points:account:verify:password:timeout', '3600', '1', '0', 0, 'admin', '2025-05-24 20:17:06', 'admin', '2025-05-24 20:18:52', '时间单位秒，必须整数');
INSERT INTO `c_config_info` VALUES (36, '图片下载官方分成比例', 'picture:download:official:proportion', '0.2', '1', '0', 0, 'admin', '2025-05-24 23:38:10', NULL, NULL, NULL);
INSERT INTO `c_config_info` VALUES (37, '图片下载空间分成比例', 'picture:download:space:proportion', '0.3', '1', '0', 0, 'admin', '2025-05-24 23:39:10', NULL, NULL, NULL);
INSERT INTO `c_config_info` VALUES (38, '图片浏览记录推荐分类分数占比', 'picture:recommend:view:category', '1', '1', '0', 2, 'admin', '2025-06-06 22:19:35', 'admin', '2025-06-12 00:11:21', NULL);
INSERT INTO `c_config_info` VALUES (39, '图片浏览记录推荐标签分数占比', 'picture:recommend:view:tag', '2', '1', '0', 2, 'admin', '2025-06-06 22:20:04', NULL, NULL, NULL);
INSERT INTO `c_config_info` VALUES (40, '图片浏览记录推荐时间衰减', 'picture:recommend:view:time:decay', '0.5', '1', '0', 2, 'admin', '2025-06-06 22:37:23', 'admin', '2025-08-19 19:56:36', '小数百分比，最多两位');
INSERT INTO `c_config_info` VALUES (41, '图片下载记录推荐分类分数占比', 'picture:recommend:download:category', '1', '1', '0', 2, 'admin', '2025-06-06 22:19:35', NULL, NULL, NULL);
INSERT INTO `c_config_info` VALUES (42, '图片下载记录推荐标签分数占比', 'picture:recommend:download:tag', '2', '1', '0', 2, 'admin', '2025-06-06 22:20:04', NULL, NULL, NULL);
INSERT INTO `c_config_info` VALUES (43, '图片下载记录推荐时间衰减', 'picture:recommend:download:time:decay', '0.8', '1', '0', 2, 'admin', '2025-06-06 22:37:23', 'admin', '2025-06-08 22:01:00', '小数百分比，最多两位');
INSERT INTO `c_config_info` VALUES (44, '图片行为记录推荐分类分数占比', 'picture:recommend:behavior:category', '1', '1', '0', 2, 'admin', '2025-06-06 22:19:35', NULL, NULL, NULL);
INSERT INTO `c_config_info` VALUES (45, '图片行为记录推荐标签分数占比', 'picture:recommend:behavior:tag', '2', '1', '0', 2, 'admin', '2025-06-06 22:20:04', NULL, NULL, NULL);
INSERT INTO `c_config_info` VALUES (46, '图片行为记录推荐时间衰减', 'picture:recommend:behavior:time:decay', '0.7', '1', '0', 2, 'admin', '2025-06-06 22:37:23', 'admin', '2025-08-19 19:56:31', '小数百分比，最多两位');
INSERT INTO `c_config_info` VALUES (47, '图片推荐超时时间', 'picture:recommend:timeout', '600', '1', '0', 2, 'admin', '2025-06-06 23:59:54', NULL, NULL, '单位秒');
INSERT INTO `c_config_info` VALUES (49, '图片推荐不可重复超时时间', 'picture:recommend:not:repeat:timeout', '5', '1', '0', 2, 'admin', '2025-06-07 00:01:39', NULL, NULL, '单位秒');
INSERT INTO `c_config_info` VALUES (50, '图片浏览记录推荐查询数量', 'picture:recommend:view:limit', '500', '1', '0', 2, 'admin', '2025-06-06 22:37:23', 'admin', '2025-06-06 22:38:00', '小数百分比，最多两位');
INSERT INTO `c_config_info` VALUES (51, '图片行为记录推荐查询数量', 'picture:recommend:behavior:limit', '200', '1', '0', 2, 'admin', '2025-06-06 22:37:23', 'admin', '2025-06-06 22:38:00', '小数百分比，最多两位');
INSERT INTO `c_config_info` VALUES (52, '图片下载记录推荐查询数量', 'picture:recommend:download:limit', '100', '1', '0', 2, 'admin', '2025-06-06 22:37:23', 'admin', '2025-06-06 22:38:00', '小数百分比，最多两位');
INSERT INTO `c_config_info` VALUES (53, '图片推荐分类返回最大值', 'picture:recommend:category:max', '5000', '1', '0', 2, 'admin', '2025-06-11 18:05:27', 'admin', '2025-06-11 18:07:22', '每个批次返回最大数\n\n\n      // 1. 分批查询 - 每次最多3个分类\n        int batchSize = 3;\n        Set<String> resultSet = new HashSet<>(512);\n        int processedBatches = 0;\n        final int maxBatches = 10; // 最多处理10批（约30个分类）');
INSERT INTO `c_config_info` VALUES (54, '图片推荐标签返回最大值', 'picture:recommend:tag:max', '5000', '1', '0', 2, 'admin', '2025-06-11 18:06:25', 'admin', '2025-06-11 18:07:26', '每批次返回最大数\n\n        int batchSize = 3; // 标签批处理使用更大批大小\n        Set<String> resultSet = new HashSet<>(1024);\n        final int maxBatches = 8; // 最大批次减少');
INSERT INTO `c_config_info` VALUES (55, '图片推荐分类返回权重', 'picture:recommend:category:weight', '0.3', '1', '0', 2, 'admin', '2025-06-12 00:10:04', 'admin', '2025-06-12 00:11:49', '图片推荐分类返回权重，与图片推荐标签权重相加要等于1');
INSERT INTO `c_config_info` VALUES (56, '图片推荐标签返回权重', 'picture:recommend:tag:weight', '0.7', '1', '0', 2, 'admin', '2025-06-12 00:12:25', 'admin', '2025-06-12 00:12:32', '图片推荐分类返回权重，与图片推荐标签权重相加要等于1');
INSERT INTO `c_config_info` VALUES (57, '图片推荐下载缓存间隔时间', 'picture:recommend:download:cache:timeout', '600', '1', '0', 2, 'admin', '2025-06-13 20:57:13', 'admin', '2025-06-13 21:07:20', '缓存时间，用于判断如果用户行为过多且不超过阈值时不更新用户推荐模型');
INSERT INTO `c_config_info` VALUES (58, '图片推荐下载缓存数阈值', 'picture:recommend:download:cache:threshold', '5', '1', '0', 2, 'admin', '2025-06-13 20:57:53', 'admin', '2025-06-13 21:08:16', '阈值，用于判断用户如果当前阈值过高超过阈值更新用户推荐模型');
INSERT INTO `c_config_info` VALUES (59, '图片推荐浏览缓存间隔时间', 'picture:recommend:view:cache:timeout', '1800', '1', '0', 2, 'admin', '2025-06-13 20:57:13', NULL, NULL, NULL);
INSERT INTO `c_config_info` VALUES (60, '图片推荐浏览缓存数阈值', 'picture:recommend:view:cache:threshold', '20', '1', '0', 2, 'admin', '2025-06-13 20:57:53', NULL, NULL, NULL);
INSERT INTO `c_config_info` VALUES (61, '图片推荐行为缓存间隔时间', 'picture:recommend:behavior:cache:timeout', '1200', '1', '0', 2, 'admin', '2025-06-13 20:57:13', NULL, NULL, NULL);
INSERT INTO `c_config_info` VALUES (62, '图片推荐行为缓存数阈值', 'picture:recommend:behavior:cache:threshold', '10', '1', '0', 2, 'admin', '2025-06-13 20:57:53', NULL, NULL, NULL);
INSERT INTO `c_config_info` VALUES (63, '图片举报提示内容', 'picture:report:content', '如果图片有违规的地方，可直接举报，如若严重，例如侵权，请联系微信：SpringSun_YY', '1', '1', 3, 'admin', '2025-06-17 17:48:50', NULL, NULL, NULL);
INSERT INTO `c_config_info` VALUES (64, '空间扩容-数量', 'picture:space:dilatation:count', '1', '1', '1', 3, 'admin', '2025-06-28 19:03:36', NULL, NULL, '空间文件总数扩容使用积分：1积分/1数量');
INSERT INTO `c_config_info` VALUES (65, '空间扩容-容量', 'picture:space:dilatation:size', '1000', '1', '1', 3, 'admin', '2025-06-28 19:04:13', 'admin', '2025-06-28 19:07:20', '空间容量总数扩容使用积分：1000积分/1GB');
INSERT INTO `c_config_info` VALUES (66, '空间扩容-人数', 'picture:space:dilatation:member', '100', '1', '1', 3, 'admin', '2025-06-28 19:05:12', 'admin', '2025-06-28 19:07:26', '空间人数总数扩容使用积分：100积分/1人');
INSERT INTO `c_config_info` VALUES (67, '图片热门统计用户行为分数-浏览', 'picture:statistics:hot:behavior:score:view', '1', '1', '0', 4, 'admin', '2025-07-16 16:51:03', 'admin', '2025-07-18 00:25:39', NULL);
INSERT INTO `c_config_info` VALUES (68, '图片热门统计用户行为分数-下载', 'picture:statistics:hot:behavior:score:download', '20', '1', '0', 4, 'admin', '2025-07-16 16:51:03', NULL, NULL, NULL);
INSERT INTO `c_config_info` VALUES (69, '图片热门统计用户行为分数-点赞', 'picture:statistics:hot:behavior:score:like', '3', '1', '0', 4, 'admin', '2025-07-16 16:51:03', NULL, NULL, NULL);
INSERT INTO `c_config_info` VALUES (70, '图片热门统计用户行为分数-收藏', 'picture:statistics:hot:behavior:score:collect', '6', '1', '0', 4, 'admin', '2025-07-16 16:51:03', NULL, NULL, NULL);
INSERT INTO `c_config_info` VALUES (71, '图片热门统计用户行为分数-转发', 'picture:statistics:hot:behavior:score:share', '9', '1', '0', 4, 'admin', '2025-07-16 16:51:03', NULL, NULL, NULL);
INSERT INTO `c_config_info` VALUES (72, '图片热门统计日排行', 'picture:statistics:hot:day:rank', '5000', '1', '0', 4, 'admin', '2025-07-19 00:07:58', NULL, NULL, NULL);
INSERT INTO `c_config_info` VALUES (73, '图片热门统计周排行', 'picture:statistics:hot:week:rank', '5000', '1', '0', 4, 'admin', '2025-07-19 00:07:58', NULL, NULL, NULL);
INSERT INTO `c_config_info` VALUES (74, '图片热门统计月排行', 'picture:statistics:hot:month:rank', '5000', '1', '0', 4, 'admin', '2025-07-19 00:07:58', NULL, NULL, NULL);
INSERT INTO `c_config_info` VALUES (75, '图片热门统计年排行', 'picture:statistics:hot:year:rank', '5000', '1', '0', 4, 'admin', '2025-07-19 00:07:58', NULL, NULL, NULL);
INSERT INTO `c_config_info` VALUES (76, '图片热门统计总排行', 'picture:statistics:hot:total:rank', '10000', '1', '0', 4, 'admin', '2025-07-19 00:07:58', NULL, NULL, NULL);
INSERT INTO `c_config_info` VALUES (77, '图片分类AIID', 'picture:category:ai:id', 'C_AI', '1', '0', 4, 'admin', '2025-08-16 18:38:20', NULL, NULL, NULL);
INSERT INTO `c_config_info` VALUES (78, '首页背景图片', 'index:bg:image', '/picture/public/bg/bg.jpg', '1', '0', 0, 'admin', '2025-08-21 15:47:22', 'admin', '2025-08-21 15:53:16', '首页背景图');

-- ----------------------------
-- Table structure for c_file_log_info
-- ----------------------------
DROP TABLE IF EXISTS `c_file_log_info`;
CREATE TABLE `c_file_log_info`  (
  `log_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '日志编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `target_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目标对象',
  `target_content` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目标内容',
  `file_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件路径',
  `file_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件类型',
  `log_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态',
  `log_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '日志类型（0图片 1空间封面 2头像）',
  `is_compress` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '是否压缩（0是 1否）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `delete_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'IP地址',
  `device_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '平台',
  `ip_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP属地',
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `c_file_log_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文件日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for c_i18n_key_info
-- ----------------------------
DROP TABLE IF EXISTS `c_i18n_key_info`;
CREATE TABLE `c_i18n_key_info`  (
  `key_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `key_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '键',
  `order_num` int NULL DEFAULT NULL COMMENT '显示顺序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`key_id`) USING BTREE,
  UNIQUE INDEX `uk_c_i18n_key_info_key_name`(`key_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '国际化键名表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of c_i18n_key_info
-- ----------------------------
INSERT INTO `c_i18n_key_info` VALUES (1, 'test', 1, 'admin', '2025-02-28 23:07:25', NULL, NULL, NULL);
INSERT INTO `c_i18n_key_info` VALUES (3, 'test1', 0, 'admin', '2025-02-28 23:08:27', NULL, '2025-02-28 23:10:18', NULL);

-- ----------------------------
-- Table structure for c_i18n_locale_info
-- ----------------------------
DROP TABLE IF EXISTS `c_i18n_locale_info`;
CREATE TABLE `c_i18n_locale_info`  (
  `locale_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `locale_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '国家地区',
  `locale` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '简称',
  `locale_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '状态（0正常 1隐藏）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`locale_id`) USING BTREE,
  UNIQUE INDEX `uk_c_i18n_locale_info_locale`(`locale` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '国际化国家表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of c_i18n_locale_info
-- ----------------------------
INSERT INTO `c_i18n_locale_info` VALUES (1, '中国', 'zh-CN', '0', 'admin', '2025-02-28 22:52:58', 'admin', '2025-05-28 22:13:35', '21');
INSERT INTO `c_i18n_locale_info` VALUES (4, '英语', 'cn', '0', 'admin', '2025-02-28 22:54:11', 'admin', '2025-02-28 22:54:33', '312');

-- ----------------------------
-- Table structure for c_i18n_message_info
-- ----------------------------
DROP TABLE IF EXISTS `c_i18n_message_info`;
CREATE TABLE `c_i18n_message_info`  (
  `message_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `message_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '键',
  `locale` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '简称',
  `message` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`message_id`) USING BTREE,
  UNIQUE INDEX `uk_c_i18n_message_info_key_locale`(`message_key` ASC, `locale` ASC) USING BTREE,
  INDEX `fk_c_i18n_message_info_locale`(`locale` ASC) USING BTREE,
  CONSTRAINT `fk_c_i18n_message_info_locale` FOREIGN KEY (`locale`) REFERENCES `c_i18n_locale_info` (`locale`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '国际化信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of c_i18n_message_info
-- ----------------------------
INSERT INTO `c_i18n_message_info` VALUES (1, 'test', 'cn', 'testCN', 'admin', '2025-03-01 13:01:14', 'admin', '2025-03-01 13:02:15', 'ces');
INSERT INTO `c_i18n_message_info` VALUES (7, 'test', 'zh-CN', 'testZh', 'admin', '2025-03-01 13:01:53', NULL, NULL, NULL);
INSERT INTO `c_i18n_message_info` VALUES (8, 'test1', 'cn', 'test1Cn', 'admin', '2025-03-01 13:18:51', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for c_inform_template_info
-- ----------------------------
DROP TABLE IF EXISTS `c_inform_template_info`;
CREATE TABLE `c_inform_template_info`  (
  `template_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `template_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模版名称',
  `template_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模版KEY',
  `locale` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '语言（默认zh-CN）',
  `channel` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '渠道',
  `template_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模版类型（1=短信 2=邮件 3=站内通知 4=APP推送 5=微信模板）',
  `service_template_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务商模版ID',
  `service_sign_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '服务商签名',
  `inform_title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '通知标题',
  `extend_config` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '扩展配置',
  `template_version` int NOT NULL COMMENT '版本',
  `template_version_history` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '历史版本',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
  `example` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '事例',
  `variables` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '变量列表',
  `template_image` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模版样式图',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态（0=待审核 1=已启用 2=已禁用 3=审核失败）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`template_id`) USING BTREE,
  UNIQUE INDEX `uk_template_name_locale`(`template_name` ASC, `locale` ASC, `template_type` ASC) USING BTREE,
  INDEX `fk_inform_template_locale`(`locale` ASC) USING BTREE,
  INDEX `idx_template_status`(`status` ASC) USING BTREE,
  INDEX `idx_template_type`(`template_type` ASC) USING BTREE,
  CONSTRAINT `fk_inform_template_locale` FOREIGN KEY (`locale`) REFERENCES `c_i18n_locale_info` (`locale`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '通知模版表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of c_inform_template_info
-- ----------------------------
INSERT INTO `c_inform_template_info` VALUES (8, '测试', 'TEST', 'zh-CN', '阿里云', '1', 'ALY', 'ALY', NULL, NULL, 2, '{1:\"{\\\"channel\\\":\\\"阿里云\\\",\\\"content\\\":\\\"你好我是${userName}，你好吗${to}\\\",\\\"example\\\":\\\"你好我是YY，你好吗XC\\\",\\\"locale\\\":\\\"zh\\\",\\\"serviceSignName\\\":\\\"ALY\\\",\\\"serviceTemplateId\\\":\\\"ALY\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":8,\\\"templateName\\\":\\\"测试\\\",\\\"templateType\\\":\\\"1\\\",\\\"templateVersion\\\":1,\\\"variables\\\":\\\"{\\\\n    \\\\\\\"userName\\\\\\\":\\\\\\\"YY\\\\\\\",\\\\n    \\\\\\\"to\\\\\\\":\\\\\\\"XC\\\\\\\"\\\\n}\\\"}\",2:\"{\\\"channel\\\":\\\"阿里云\\\",\\\"content\\\":\\\"你好我是${userName}，你好吗${to}，哈哈\\\",\\\"example\\\":\\\"你好我是YY，你好吗XC，哈哈\\\",\\\"locale\\\":\\\"zh\\\",\\\"serviceSignName\\\":\\\"ALY\\\",\\\"serviceTemplateId\\\":\\\"ALY\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":8,\\\"templateImage\\\":\\\"/profile/upload/2025/03/16/20250310_20250316150619A001.png\\\",\\\"templateName\\\":\\\"测试\\\",\\\"templateType\\\":\\\"1\\\",\\\"templateVersion\\\":2,\\\"updateBy\\\":\\\"admin\\\",\\\"updateTime\\\":\\\"2025-03-16 15:06:24\\\",\\\"variables\\\":\\\"{\\\\n    \\\\\\\"userName\\\\\\\":\\\\\\\"YY\\\\\\\",\\\\n    \\\\\\\"to\\\\\\\":\\\\\\\"XC\\\\\\\"\\\\n}\\\"}\"}', '你好我是${userName},${to}你好![20250310.png](/dev-api/profile/upload/2025/05/26/20250310_20250526232603A010.png)\n\n![](/profile/upload/2025/05/29/20250310_20250529040349A076.png)\n![](/profile/upload/2025/05/29/pexels-photo-14471335_20250529040349A077.jpeg)\n![](/profile/upload/2025/05/29/R-A_20250529040349A078.jpg)\n![](/profile/upload/2025/05/29/wallhaven-exm8xk_20250529040349A079.jpg)\n', '你好我是YY,XC你好![20250310.png](/dev-api/profile/upload/2025/05/26/20250310_20250526232603A010.png)\n\n![](/profile/upload/2025/05/29/20250310_20250529040349A076.png)\n![](/profile/upload/2025/05/29/pexels-photo-14471335_20250529040349A077.jpeg)\n![](/profile/upload/2025/05/29/R-A_20250529040349A078.jpg)\n![](/profile/upload/2025/05/29/wallhaven-exm8xk_20250529040349A079.jpg)\n', '{\n    \"userName\":\"YY\",\n    \"to\":\"XC\"\n}', '/profile/upload/2025/05/26/20250310_20250526221246A001.png', '0', 'admin', '2025-03-16 15:05:59', 'admin', '2025-05-29 04:19:41', '321312');
INSERT INTO `c_inform_template_info` VALUES (11, '测试', 'login_sms_code', 'zh-CN', '阿里云', '2', 'ALI', 'ALY', '短信登录通知', NULL, 1, '{1:\"{\\\"channel\\\":\\\"阿里云\\\",\\\"content\\\":\\\"你好我是${userName}，你好吗${to}\\\",\\\"example\\\":\\\"你好我是YY，你好吗XC\\\",\\\"locale\\\":\\\"zh\\\",\\\"serviceSignName\\\":\\\"ALY\\\",\\\"serviceTemplateId\\\":\\\"ALI\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":11,\\\"templateImage\\\":\\\"/profile/upload/2025/03/16/20250310_20250316151927A001.png\\\",\\\"templateName\\\":\\\"测试\\\",\\\"templateType\\\":\\\"2\\\",\\\"templateVersion\\\":1,\\\"variables\\\":\\\"{\\\\n    \\\\\\\"userName\\\\\\\":\\\\\\\"YY\\\\\\\",\\\\n    \\\\\\\"to\\\\\\\":\\\\\\\"XC\\\\\\\"\\\\n}\\\"}\"}', '你好我是${userName}，你好吗${to}', '你好我是YY，你好吗XC', '{\n    \"userName\":\"YY\",\n    \"to\":\"XC\"\n}', '', '0', 'admin', '2025-03-16 15:21:23', 'admin', '2025-05-28 01:21:19', NULL);
INSERT INTO `c_inform_template_info` VALUES (12, ' 荔枝云图库登录验证码', 'sms_login_code', 'zh-CN', '阿里云', '1', 'SMS_483940269', '荔枝软件开发工作室短信', NULL, NULL, 5, '{1:\"{\\\"channel\\\":\\\"阿里云\\\",\\\"content\\\":\\\"用户您好，您的登录验证码为${code}，有效时间5分钟，感谢支持！\\\",\\\"example\\\":\\\"用户您好，您的登录验证码为1226，有效时间5分钟，感谢支持！\\\",\\\"locale\\\":\\\"zh\\\",\\\"serviceSignName\\\":\\\" 荔枝开发阶段短信服务\\\",\\\"serviceTemplateId\\\":\\\"SMS_480850068\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":12,\\\"templateName\\\":\\\" 荔枝云图库登录验证码\\\",\\\"templateType\\\":\\\"1\\\",\\\"templateVersion\\\":1,\\\"variables\\\":\\\"{\\\\n\\\\\\\"code\\\\\\\":\\\\\\\"1226\\\\\\\"\\\\n}\\\"}\",2:\"{\\\"channel\\\":\\\"阿里云\\\",\\\"content\\\":\\\"用户您好，您的登录验证码为${code}，有效时间5分钟，感谢支持！\\\",\\\"example\\\":\\\"用户您好，您的登录验证码为1226，有效时间5分钟，感谢支持！\\\",\\\"locale\\\":\\\"zh\\\",\\\"serviceSignName\\\":\\\" 荔枝开发阶段短信服务\\\",\\\"serviceTemplateId\\\":\\\"SMS_480850068\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":12,\\\"templateKey\\\":\\\"lz_login_sms_code\\\",\\\"templateName\\\":\\\" 荔枝云图库登录验证码\\\",\\\"templateType\\\":\\\"1\\\",\\\"templateVersion\\\":2,\\\"updateBy\\\":\\\"admin\\\",\\\"updateTime\\\":\\\"2025-04-18 22:25:39\\\",\\\"variables\\\":\\\"{\\\\n\\\\\\\"code\\\\\\\":\\\\\\\"1226\\\\\\\"\\\\n}\\\"}\",3:\"{\\\"channel\\\":\\\"阿里云\\\",\\\"content\\\":\\\"用户您好，您的登录验证码为${code}，有效时间5分钟，感谢支持！\\\",\\\"example\\\":\\\"用户您好，您的登录验证码为1226，有效时间5分钟，感谢支持！\\\",\\\"locale\\\":\\\"zh\\\",\\\"serviceSignName\\\":\\\" 荔枝开发阶段短信服务\\\",\\\"serviceTemplateId\\\":\\\"SMS_480850068\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":12,\\\"templateKey\\\":\\\"lz_login_sms_code\\\",\\\"templateName\\\":\\\" 荔枝云图库登录验证码\\\",\\\"templateType\\\":\\\"1\\\",\\\"templateVersion\\\":3,\\\"updateBy\\\":\\\"admin\\\",\\\"updateTime\\\":\\\"2025-04-18 22:26:03\\\",\\\"variables\\\":\\\"{\\\\n\\\\\\\"code\\\\\\\":\\\\\\\"1226\\\\\\\"\\\\n}\\\"}\",4:\"{\\\"channel\\\":\\\"阿里云\\\",\\\"content\\\":\\\"用户您好，您的登录验证码为${code}，有效时间5分钟，感谢支持！\\\",\\\"example\\\":\\\"用户您好，您的登录验证码为1226，有效时间5分钟，感谢支持！\\\",\\\"locale\\\":\\\"zh\\\",\\\"serviceSignName\\\":\\\" 荔枝开发阶段短信服务\\\",\\\"serviceTemplateId\\\":\\\"SMS_480850068\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":12,\\\"templateKey\\\":\\\"login_sms_code\\\",\\\"templateName\\\":\\\" 荔枝云图库登录验证码\\\",\\\"templateType\\\":\\\"1\\\",\\\"templateVersion\\\":4,\\\"updateBy\\\":\\\"admin\\\",\\\"updateTime\\\":\\\"2025-04-18 22:38:58\\\",\\\"variables\\\":\\\"{\\\\n\\\\\\\"code\\\\\\\":\\\\\\\"1226\\\\\\\"\\\\n}\\\"}\",5:\"{\\\"channel\\\":\\\"阿里云\\\",\\\"content\\\":\\\"用户您好，您的登录验证码为${code}，有效时间5分钟，感谢支持！\\\",\\\"example\\\":\\\"用户您好，您的登录验证码为1226，有效时间5分钟，感谢支持！\\\",\\\"locale\\\":\\\"zh\\\",\\\"serviceSignName\\\":\\\"荔枝开发阶段短信服务\\\",\\\"serviceTemplateId\\\":\\\"SMS_480850068\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":12,\\\"templateKey\\\":\\\"sms_login_code\\\",\\\"templateName\\\":\\\" 荔枝云图库登录验证码\\\",\\\"templateType\\\":\\\"1\\\",\\\"templateVersion\\\":5,\\\"updateBy\\\":\\\"admin\\\",\\\"updateTime\\\":\\\"2025-04-19 00:06:43\\\",\\\"variables\\\":\\\"{\\\\n\\\\\\\"code\\\\\\\":\\\\\\\"1226\\\\\\\"\\\\n}\\\"}\"}', '用户您好，您的登录验证码为${code}，有效时间5分钟，感谢支持！', '用户您好，您的登录验证码为1226，有效时间5分钟，感谢支持！', '{\n\"code\":\"1226\"\n}', NULL, '0', 'admin', '2025-03-19 08:49:33', 'admin', '2025-07-30 00:11:05', NULL);
INSERT INTO `c_inform_template_info` VALUES (13, '荔枝云图库注册验证码', 'sms_register_code', 'zh-CN', '阿里云', '1', 'SMS_483940269', '荔枝软件开发工作室短信', NULL, NULL, 4, '{1:\"{\\\"channel\\\":\\\"阿里云\\\",\\\"content\\\":\\\"用户您好，您的登录验证码为${code}，有效时间5分钟，感谢支持！\\\",\\\"example\\\":\\\"用户您好，您的登录验证码为123456，有效时间5分钟，感谢支持！\\\",\\\"locale\\\":\\\"zh\\\",\\\"serviceSignName\\\":\\\"荔枝云图库注册验证码\\\",\\\"serviceTemplateId\\\":\\\"SMS_484005111\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":13,\\\"templateKey\\\":\\\"sms_register_code\\\",\\\"templateName\\\":\\\"荔枝云图库注册验证码\\\",\\\"templateType\\\":\\\"1\\\",\\\"templateVersion\\\":1,\\\"variables\\\":\\\"{\\\\\\\"code\\\\\\\":\\\\\\\"123456\\\\\\\"}\\\"}\",2:\"{\\\"channel\\\":\\\"阿里云\\\",\\\"content\\\":\\\"用户您好，您的登录验证码为${code}，有效时间5分钟，感谢支持！\\\",\\\"example\\\":\\\"用户您好，您的登录验证码为123456，有效时间5分钟，感谢支持！\\\",\\\"locale\\\":\\\"zh\\\",\\\"serviceSignName\\\":\\\"荔枝开发阶段短信服务\\\",\\\"serviceTemplateId\\\":\\\"SMS_484005111\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":13,\\\"templateKey\\\":\\\"sms_register_code\\\",\\\"templateName\\\":\\\"荔枝云图库注册验证码\\\",\\\"templateType\\\":\\\"1\\\",\\\"templateVersion\\\":2,\\\"updateBy\\\":\\\"admin\\\",\\\"updateTime\\\":\\\"2025-04-19 00:13:33\\\",\\\"variables\\\":\\\"{\\\\\\\"code\\\\\\\":\\\\\\\"123456\\\\\\\"}\\\"}\",3:\"{\\\"channel\\\":\\\"阿里云\\\",\\\"content\\\":\\\"尊敬的用户，您正在校验敏感操作，验证码为：${code}，请立即验证，感谢支持（若非本人请勿泄露验证码）。\\\",\\\"example\\\":\\\"尊敬的用户，您正在校验敏感操作，验证码为：123456，请立即验证，感谢支持（若非本人请勿泄露验证码）。\\\",\\\"locale\\\":\\\"zh\\\",\\\"serviceSignName\\\":\\\"荔枝开发阶段短信服务\\\",\\\"serviceTemplateId\\\":\\\"SMS_484135277\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":13,\\\"templateKey\\\":\\\"sms_register_code\\\",\\\"templateName\\\":\\\"荔枝云图库注册验证码\\\",\\\"templateType\\\":\\\"1\\\",\\\"templateVersion\\\":3,\\\"updateBy\\\":\\\"admin\\\",\\\"updateTime\\\":\\\"2025-04-22 08:59:01\\\",\\\"variables\\\":\\\"{\\\\\\\"code\\\\\\\":\\\\\\\"123456\\\\\\\"}\\\"}\",4:\"{\\\"channel\\\":\\\"阿里云\\\",\\\"content\\\":\\\"尊敬的用户，您正在校验敏感操作，验证码为：${code}，请立即验证，感谢支持（若非本人请勿泄露验证码）。\\\",\\\"example\\\":\\\"尊敬的用户，您正在校验敏感操作，验证码为：123456，请立即验证，感谢支持（若非本人请勿泄露验证码）。\\\",\\\"locale\\\":\\\"zh\\\",\\\"serviceSignName\\\":\\\"荔枝软件开发工作室短信\\\",\\\"serviceTemplateId\\\":\\\"SMS_483940269\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":13,\\\"templateKey\\\":\\\"sms_register_code\\\",\\\"templateName\\\":\\\"荔枝云图库注册验证码\\\",\\\"templateType\\\":\\\"1\\\",\\\"templateVersion\\\":4,\\\"updateBy\\\":\\\"admin\\\",\\\"updateTime\\\":\\\"2025-04-22 09:09:16\\\",\\\"variables\\\":\\\"{\\\\\\\"code\\\\\\\":\\\\\\\"123456\\\\\\\"}\\\"}\"}', '尊敬的用户，您正在校验敏感操作，验证码为：${code}，请立即验证，感谢支持（若非本人请勿泄露验证码）。', '尊敬的用户，您正在校验敏感操作，验证码为：123456，请立即验证，感谢支持（若非本人请勿泄露验证码）。', '{\"code\":\"123456\"}', NULL, '0', 'admin', '2025-04-19 00:04:55', 'admin', '2025-04-22 09:09:17', NULL);
INSERT INTO `c_inform_template_info` VALUES (14, '忘记密码验证码', 'sms_forget_password_code', 'zh-CN', '阿里云', '1', 'SMS_483940269', '荔枝软件开发工作室短信', NULL, NULL, 2, '{1:\"{\\\"channel\\\":\\\"阿里云\\\",\\\"content\\\":\\\"尊敬的用户，您正在校验敏感操作，验证码为：${code}，请立即验证，感谢支持（若非本人请勿泄露验证码）。\\\",\\\"example\\\":\\\"尊敬的用户，您正在校验敏感操作，验证码为：123456，请立即验证，感谢支持（若非本人请勿泄露验证码）。\\\",\\\"locale\\\":\\\"zh\\\",\\\"serviceSignName\\\":\\\"荔枝开发阶段短信服务\\\",\\\"serviceTemplateId\\\":\\\"SMS_480850068\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":14,\\\"templateKey\\\":\\\"sms_forget_password_code\\\",\\\"templateName\\\":\\\"忘记密码验证码\\\",\\\"templateType\\\":\\\"1\\\",\\\"templateVersion\\\":1,\\\"variables\\\":\\\"{\\\\\\\"code\\\\\\\":\\\\\\\"123456\\\\\\\"}\\\"}\",2:\"{\\\"channel\\\":\\\"阿里云\\\",\\\"content\\\":\\\"尊敬的用户，您正在校验敏感操作，验证码为：${code}，请立即验证，感谢支持（若非本人请勿泄露验证码）。\\\",\\\"example\\\":\\\"尊敬的用户，您正在校验敏感操作，验证码为：123456，请立即验证，感谢支持（若非本人请勿泄露验证码）。\\\",\\\"locale\\\":\\\"zh-CN\\\",\\\"serviceSignName\\\":\\\"荔枝软件开发工作室短信\\\",\\\"serviceTemplateId\\\":\\\"SMS_483940269\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":14,\\\"templateKey\\\":\\\"sms_forget_password_code\\\",\\\"templateName\\\":\\\"忘记密码验证码\\\",\\\"templateType\\\":\\\"1\\\",\\\"templateVersion\\\":2,\\\"updateBy\\\":\\\"admin\\\",\\\"updateTime\\\":\\\"2025-07-30 00:15:36\\\",\\\"variables\\\":\\\"{\\\\\\\"code\\\\\\\":\\\\\\\"123456\\\\\\\"}\\\"}\"}', '尊敬的用户，您正在校验敏感操作，验证码为：${code}，请立即验证，感谢支持（若非本人请勿泄露验证码）。', '尊敬的用户，您正在校验敏感操作，验证码为：123456，请立即验证，感谢支持（若非本人请勿泄露验证码）。', '{\"code\":\"123456\"}', NULL, '0', 'admin', '2025-04-23 16:36:19', 'admin', '2025-07-30 00:15:36', NULL);
INSERT INTO `c_inform_template_info` VALUES (15, '用户注册消息成功消息发送', 'user_register', 'zh-CN', NULL, '3', NULL, NULL, '尊敬的用户，您已在LZ-Picture注册成功，欢迎使用LZ-Picture，希望您的使用开心。', NULL, 4, '{1:\"{\\\"content\\\":\\\"尊敬的用户${userName}，您已在LZ-Picture注册成功，欢迎使用LZ-Picture\\\\n![pexels-photo-14471335.jpeg](/dev-api/profile/upload/2025/05/27/pexels-photo-14471335_20250527091430A001.jpeg)\\\\n\\\\n\\\",\\\"example\\\":\\\"尊敬的用户YY，您已在LZ-Picture注册成功，欢迎使用LZ-Picture\\\\n![pexels-photo-14471335.jpeg](/dev-api/profile/upload/2025/05/27/pexels-photo-14471335_20250527091430A001.jpeg)\\\\n\\\\n\\\",\\\"locale\\\":\\\"cn\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":15,\\\"templateKey\\\":\\\"USER_REGISTER\\\",\\\"templateName\\\":\\\"用户注册消息成功消息发送\\\",\\\"templateType\\\":\\\"3\\\",\\\"templateVersion\\\":1,\\\"variables\\\":\\\"{\\\\n   \\\\\\\"userName\\\\\\\":\\\\\\\"YY\\\\\\\"\\\\n}\\\"}\",2:\"{\\\"content\\\":\\\"尊敬的用户${userName}，您已在LZ-Picture注册成功，欢迎使用LZ-Picture,希望你使用开心。\\\\n![pexels-photo-14471335.jpeg](/dev-api/profile/upload/2025/05/27/pexels-photo-14471335_20250527091430A001.jpeg)\\\\n\\\\n\\\",\\\"example\\\":\\\"尊敬的用户YY，您已在LZ-Picture注册成功，欢迎使用LZ-Picture,希望你使用开心。\\\\n![pexels-photo-14471335.jpeg](/dev-api/profile/upload/2025/05/27/pexels-photo-14471335_20250527091430A001.jpeg)\\\\n\\\\n\\\",\\\"locale\\\":\\\"cn\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":15,\\\"templateKey\\\":\\\"USER_REGISTER\\\",\\\"templateName\\\":\\\"用户注册消息成功消息发送\\\",\\\"templateType\\\":\\\"3\\\",\\\"templateVersion\\\":2,\\\"updateBy\\\":\\\"admin\\\",\\\"updateTime\\\":\\\"2025-05-27 09:15:05\\\",\\\"variables\\\":\\\"{\\\\n   \\\\\\\"userName\\\\\\\":\\\\\\\"YY\\\\\\\"\\\\n}\\\"}\",3:\"{\\\"content\\\":\\\"尊敬的用户${userName}，您已在LZ-Picture注册成功，欢迎使用LZ-Picture,希望你使用开心。\\\\n![pexels-photo-14471335.jpeg](/dev-api/profile/upload/2025/05/27/pexels-photo-14471335_20250527091430A001.jpeg)\\\\n\\\\n\\\",\\\"example\\\":\\\"尊敬的用户YY，您已在LZ-Picture注册成功，欢迎使用LZ-Picture,希望你使用开心。\\\\n![pexels-photo-14471335.jpeg](/dev-api/profile/upload/2025/05/27/pexels-photo-14471335_20250527091430A001.jpeg)\\\\n\\\\n\\\",\\\"informTitle\\\":\\\"尊敬的用户，您已在LZ-Picture注册成功，欢迎使用LZ-Picture，开开心心\\\",\\\"locale\\\":\\\"cn\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":15,\\\"templateKey\\\":\\\"USER_REGISTER\\\",\\\"templateName\\\":\\\"用户注册消息成功消息发送\\\",\\\"templateType\\\":\\\"3\\\",\\\"templateVersion\\\":3,\\\"updateBy\\\":\\\"admin\\\",\\\"updateTime\\\":\\\"2025-05-27 09:18:26\\\",\\\"variables\\\":\\\"{\\\\n   \\\\\\\"userName\\\\\\\":\\\\\\\"YY\\\\\\\"\\\\n}\\\"}\",4:\"{\\\"content\\\":\\\"尊敬的用户${userName}，您已在LZ-Picture注册成功，欢迎使用LZ-Picture,希望你使用开心。\\\\n![pexels-photo-14471335.jpeg](/dev-api/profile/upload/2025/05/27/pexels-photo-14471335_20250527091430A001.jpeg)\\\\n\\\\n\\\",\\\"example\\\":\\\"尊敬的用户YY，您已在LZ-Picture注册成功，欢迎使用LZ-Picture,希望你使用开心。\\\\n![pexels-photo-14471335.jpeg](/dev-api/profile/upload/2025/05/27/pexels-photo-14471335_20250527091430A001.jpeg)\\\\n\\\\n\\\",\\\"informTitle\\\":\\\"尊敬的用户，您已在LZ-Picture注册成功，欢迎使用LZ-Picture\\\",\\\"locale\\\":\\\"cn\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":15,\\\"templateKey\\\":\\\"USER_REGISTER\\\",\\\"templateName\\\":\\\"用户注册消息成功消息发送\\\",\\\"templateType\\\":\\\"3\\\",\\\"templateVersion\\\":4,\\\"updateBy\\\":\\\"admin\\\",\\\"updateTime\\\":\\\"2025-05-27 09:19:00\\\",\\\"variables\\\":\\\"{\\\\n   \\\\\\\"userName\\\\\\\":\\\\\\\"YY\\\\\\\"\\\\n}\\\"}\"}', '![](/profile/upload/2025/07/30/50b936c6136898cf57689ab4be404ea1_20250730202114A002.jpg)\n尊敬的用户${userName}，您已在LZ-Picture注册成功，欢迎使用LZ-Picture,希望您使用开心。\n荔枝云图库是一个在线的云图库，您可以在我们的平台上面寻找优质的图片资源，可以同您的团队一起管理您的图片，更可以使用AI编辑图片，在使用的过程中，如果您遇到bug可以提交至客服，如果有好的想法也可以推荐给开发团队。\n\n\n', '![](/profile/upload/2025/07/30/50b936c6136898cf57689ab4be404ea1_20250730202114A002.jpg)\n尊敬的用户YY，您已在LZ-Picture注册成功，欢迎使用LZ-Picture,希望您使用开心。\n荔枝云图库是一个在线的云图库，您可以在我们的平台上面寻找优质的图片资源，可以同您的团队一起管理您的图片，更可以使用AI编辑图片，在使用的过程中，如果您遇到bug可以提交至客服，如果有好的想法也可以推荐给开发团队。\n\n\n', '{\n   \"userName\":\"YY\"\n}', NULL, '0', 'admin', '2025-05-27 09:14:43', 'admin', '2025-07-30 20:21:24', NULL);
INSERT INTO `c_inform_template_info` VALUES (16, '用户购买套餐充值成功', 'buy_package_success', 'zh-CN', NULL, '3', NULL, NULL, '尊敬的用户，您的套餐已经购买成功，请查看您的积分充值记录', NULL, 2, '{1:\"{\\\"content\\\":\\\"尊敬的用户${userName}，您的套餐${packageName}已经购买成功，支付${buyerPayAmount}元，积分到账为${points}详细请查看请查看您的积分充值记录。\\\\n感谢您的购买，积分可以下载图片、使用AI编辑图片等等功能。\\\\n\\\\n\\\",\\\"example\\\":\\\"尊敬的用户YY，您的套餐基础100元充值套餐已经购买成功，支付99.89元，积分到账为10000详细请查看请查看您的积分充值记录。\\\\n感谢您的购买，积分可以下载图片、使用AI编辑图片等等功能。\\\\n\\\\n\\\",\\\"informTitle\\\":\\\"尊敬的用户，您的套餐已经购买成功，请查看您的积分充值记录\\\",\\\"locale\\\":\\\"zh-CN\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":16,\\\"templateKey\\\":\\\"buy_package_success\\\",\\\"templateName\\\":\\\"用户购买套餐充值成功\\\",\\\"templateType\\\":\\\"3\\\",\\\"templateVersion\\\":1,\\\"variables\\\":\\\"{\\\\n   \\\\\\\"userName\\\\\\\":\\\\\\\"YY\\\\\\\",\\\\n   \\\\\\\"packageName\\\\\\\":\\\\\\\"基础100元充值套餐\\\\\\\",\\\\n   \\\\\\\"points\\\\\\\":\\\\\\\"10000\\\\\\\",\\\\n   \\\\\\\"buyerPayAmount\\\\\\\":\\\\\\\"99.89\\\\\\\"\\\\n}\\\"}\",2:\"{\\\"content\\\":\\\"尊敬的用户，您的套餐${packageName}已经购买成功，支付${buyerPayAmount}元，积分到账为${points}详细请查看请查看您的积分充值记录。\\\\n感谢您的购买，积分可以下载图片、使用AI编辑图片等等功能。\\\\n\\\\n\\\",\\\"example\\\":\\\"尊敬的用户，您的套餐基础100元充值套餐已经购买成功，支付99.89元，积分到账为10000详细请查看请查看您的积分充值记录。\\\\n感谢您的购买，积分可以下载图片、使用AI编辑图片等等功能。\\\\n\\\\n\\\",\\\"informTitle\\\":\\\"尊敬的用户，您的套餐已经购买成功，请查看您的积分充值记录\\\",\\\"locale\\\":\\\"zh-CN\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":16,\\\"templateKey\\\":\\\"buy_package_success\\\",\\\"templateName\\\":\\\"用户购买套餐充值成功\\\",\\\"templateType\\\":\\\"3\\\",\\\"templateVersion\\\":2,\\\"updateBy\\\":\\\"admin\\\",\\\"updateTime\\\":\\\"2025-05-28 22:33:09\\\",\\\"variables\\\":\\\"{\\\\n   \\\\\\\"userName\\\\\\\":\\\\\\\"YY\\\\\\\",\\\\n   \\\\\\\"packageName\\\\\\\":\\\\\\\"基础100元充值套餐\\\\\\\",\\\\n   \\\\\\\"points\\\\\\\":\\\\\\\"10000\\\\\\\",\\\\n   \\\\\\\"buyerPayAmount\\\\\\\":\\\\\\\"99.89\\\\\\\"\\\\n}\\\"}\"}', '尊敬的用户，您的套餐${packageName}已经购买成功，支付${buyerPayAmount}元，积分到账为${points}详细请查看请查看您的积分充值记录。\n感谢您的购买，积分可以下载图片、使用AI编辑图片等等功能。\n> 购买套餐：${packageName}\n> 到账积分：${points}\n> 实付金额：${totalAmount}\n> 支付金额：${buyerPayAmount}\n> 时间：${createTime}\n', '尊敬的用户，您的套餐基础100元充值套餐已经购买成功，支付99.89元，积分到账为10000详细请查看请查看您的积分充值记录。\n感谢您的购买，积分可以下载图片、使用AI编辑图片等等功能。\n> 购买套餐：基础100元充值套餐\n> 到账积分：10000\n> 实付金额：100\n> 支付金额：99.89\n> 时间：2025-05-26 10:11:12\n', '{\n   \"userName\":\"YY\",\n   \"packageName\":\"基础100元充值套餐\",\n   \"points\":\"10000\",\n   \"buyerPayAmount\":\"99.89\",\n   \"totalAmount\":\"100\",\n   \"createTime\":\"2025-05-26 10:11:12\"\n}', NULL, '0', 'admin', '2025-05-28 22:28:32', 'admin', '2025-05-29 16:08:53', NULL);
INSERT INTO `c_inform_template_info` VALUES (17, '用户查看原图成功', 'download_picture', 'zh-CN', NULL, '3', NULL, NULL, '尊敬的用户，您已下成功查看原图，之后可以在您的下载记录中免费查看此图片', NULL, 2, '{1:\"{\\\"content\\\":\\\"**尊敬的用户，您已下载图片成功，之后可以在您的下载记录中免费下载此图片。**\\\\n\\\\n> 消耗积分：${points}\\\\n> 图片名称：${pictureName}\\\\n> 下载时间：${createTime}\\\\n\\\\n\\\",\\\"example\\\":\\\"**尊敬的用户，您已下载图片成功，之后可以在您的下载记录中免费下载此图片。**\\\\n\\\\n> 消耗积分：100\\\\n> 图片名称：懒羊羊呀懒羊羊\\\\n> 下载时间：2025-05-25 10:14:15\\\\n\\\\n\\\",\\\"informTitle\\\":\\\"尊敬的用户，您已下载图片成功，之后可以在您的下载记录中免费下载此图片\\\",\\\"locale\\\":\\\"zh-CN\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":17,\\\"templateKey\\\":\\\"download_picture\\\",\\\"templateName\\\":\\\"用户下载图片成功\\\",\\\"templateType\\\":\\\"3\\\",\\\"templateVersion\\\":1,\\\"variables\\\":\\\"{\\\\n\\\\\\\"points\\\\\\\":100,\\\\n\\\\\\\"pictureName\\\\\\\":\\\\\\\"懒羊羊呀懒羊羊\\\\\\\",\\\\n\\\\\\\"createTime\\\\\\\":\\\\\\\"2025-05-25 10:14:15\\\\\\\"\\\\n}\\\"}\",2:\"{\\\"content\\\":\\\"**尊敬的用户，您已成功查看原图，之后可以在您的下载记录中免费查看此图片。**\\\\n\\\\n> 消耗积分：${points}\\\\n> 图片名称：${pictureName}\\\\n> 下载时间：${createTime}\\\\n\\\\n## 热门图片推荐\\\\n![](/profile/upload/2025/05/29/20250310_20250529170800A001.png)\\\\n![](/profile/upload/2025/05/29/pexels-photo-14471335_20250529170800A002.jpeg)\\\\n![](/profile/upload/2025/05/29/R-A_20250529170800A003.jpg)\\\\n![](/profile/upload/2025/05/29/wallhaven-exm8xk_20250529170800A004.jpg)\\\\n![](/profile/upload/2025/05/29/wallhaven-m3wx3m_20250529170800A005.jpg)\\\\n\\\",\\\"example\\\":\\\"**尊敬的用户，您已成功查看原图，之后可以在您的下载记录中免费查看此图片。**\\\\n\\\\n> 消耗积分：100\\\\n> 图片名称：懒羊羊呀懒羊羊\\\\n> 下载时间：2025-05-25 10:14:15\\\\n\\\\n## 热门图片推荐\\\\n![](/profile/upload/2025/05/29/20250310_20250529170800A001.png)\\\\n![](/profile/upload/2025/05/29/pexels-photo-14471335_20250529170800A002.jpeg)\\\\n![](/profile/upload/2025/05/29/R-A_20250529170800A003.jpg)\\\\n![](/profile/upload/2025/05/29/wallhaven-exm8xk_20250529170800A004.jpg)\\\\n![](/profile/upload/2025/05/29/wallhaven-m3wx3m_20250529170800A005.jpg)\\\\n\\\",\\\"informTitle\\\":\\\"尊敬的用户，您已下成功查看原图，之后可以在您的下载记录中免费查看此图片\\\",\\\"locale\\\":\\\"zh-CN\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":17,\\\"templateKey\\\":\\\"download_picture\\\",\\\"templateName\\\":\\\"用户下载图片成功\\\",\\\"templateType\\\":\\\"3\\\",\\\"templateVersion\\\":2,\\\"updateBy\\\":\\\"admin\\\",\\\"updateTime\\\":\\\"2025-07-25 22:25:17\\\",\\\"variables\\\":\\\"{\\\\n\\\\\\\"points\\\\\\\":100,\\\\n\\\\\\\"pictureName\\\\\\\":\\\\\\\"懒羊羊呀懒羊羊\\\\\\\",\\\\n\\\\\\\"createTime\\\\\\\":\\\\\\\"2025-05-25 10:14:15\\\\\\\"\\\\n}\\\"}\"}', '**尊敬的用户，您已成功查看原图，之后可以在您的下载记录中免费查看此图片。**\n\n> 消耗积分：${points}\n> 图片名称：${pictureName}\n> 下载时间：${createTime}\n\n## 热门图片推荐\n![](/profile/upload/2025/05/29/20250310_20250529170800A001.png)\n![](/profile/upload/2025/05/29/pexels-photo-14471335_20250529170800A002.jpeg)\n![](/profile/upload/2025/05/29/R-A_20250529170800A003.jpg)\n![](/profile/upload/2025/05/29/wallhaven-exm8xk_20250529170800A004.jpg)\n![](/profile/upload/2025/05/29/wallhaven-m3wx3m_20250529170800A005.jpg)\n', '**尊敬的用户，您已成功查看原图，之后可以在您的下载记录中免费查看此图片。**\n\n> 消耗积分：100\n> 图片名称：懒羊羊呀懒羊羊\n> 下载时间：2025-05-25 10:14:15\n\n## 热门图片推荐\n![](/profile/upload/2025/05/29/20250310_20250529170800A001.png)\n![](/profile/upload/2025/05/29/pexels-photo-14471335_20250529170800A002.jpeg)\n![](/profile/upload/2025/05/29/R-A_20250529170800A003.jpg)\n![](/profile/upload/2025/05/29/wallhaven-exm8xk_20250529170800A004.jpg)\n![](/profile/upload/2025/05/29/wallhaven-m3wx3m_20250529170800A005.jpg)\n', '{\n\"points\":100,\n\"pictureName\":\"懒羊羊呀懒羊羊\",\n\"createTime\":\"2025-05-25 10:14:15\"\n}', NULL, '0', 'admin', '2025-05-28 22:47:43', 'admin', '2025-07-25 22:25:37', NULL);
INSERT INTO `c_inform_template_info` VALUES (18, '图片查看原图作者分成积分提醒', 'download_picture_author_proportion', 'zh-CN', NULL, '3', NULL, NULL, '尊敬的创作者，您的图片已被查看原图，这是对您的积分分成，详细可以查看积分使用记录。', NULL, 2, '{1:\"{\\\"content\\\":\\\"尊敬的创作者，您的图片已被下载，这是对您的积分分成，详细可以查看积分使用记录。\\\\n> 图片名称：${pictureName}\\\\n> 获得积分：${points}\\\\n> 时间：${createTime}\\\",\\\"example\\\":\\\"尊敬的创作者，您的图片已被下载，这是对您的积分分成，详细可以查看积分使用记录。\\\\n> 图片名称：YY\\\\n> 获得积分：10000\\\\n> 时间：2025-05-26 10:11:12\\\",\\\"informTitle\\\":\\\"尊敬的创作者，您的图片已被下载，这是对您的积分分成，详细可以查看积分使用记录。\\\",\\\"locale\\\":\\\"zh-CN\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":18,\\\"templateKey\\\":\\\"download_picture_author_proportion\\\",\\\"templateName\\\":\\\"图片下载作者分成积分提醒\\\",\\\"templateType\\\":\\\"3\\\",\\\"templateVersion\\\":1,\\\"variables\\\":\\\"{\\\\n   \\\\\\\"pictureName\\\\\\\":\\\\\\\"YY\\\\\\\",\\\\n   \\\\\\\"points\\\\\\\":\\\\\\\"10000\\\\\\\",\\\\n   \\\\\\\"createTime\\\\\\\":\\\\\\\"2025-05-26 10:11:12\\\\\\\"\\\\n}\\\"}\",2:\"{\\\"content\\\":\\\"尊敬的创作者，您的图片已被查看原图，这是对您的积分分成，详细可以查看积分使用记录。\\\\n> 图片名称：${pictureName}\\\\n> 获得积分：${points}\\\\n> 时间：${createTime}\\\\n\\\\n![123](/profile/upload/2025/05/29/20250310_20250529172604A028.png)\\\\n![](/profile/upload/2025/05/29/20250310_20250529172816A029.png)\\\\n![](/profile/upload/2025/05/29/pexels-photo-14471335_20250529172817A030.jpeg)\\\\n![](/profile/upload/2025/05/29/R-A_20250529172817A031.jpg)\\\\n![](/profile/upload/2025/05/29/wallhaven-exm8xk_20250529172817A032.jpg)\\\\n![](/profile/upload/2025/05/29/wallhaven-m3wx3m_20250529172817A033.jpg)\\\\n\\\",\\\"example\\\":\\\"尊敬的创作者，您的图片已被查看原图，这是对您的积分分成，详细可以查看积分使用记录。\\\\n> 图片名称：YY\\\\n> 获得积分：10000\\\\n> 时间：2025-05-26 10:11:12\\\\n\\\\n![123](/profile/upload/2025/05/29/20250310_20250529172604A028.png)\\\\n![](/profile/upload/2025/05/29/20250310_20250529172816A029.png)\\\\n![](/profile/upload/2025/05/29/pexels-photo-14471335_20250529172817A030.jpeg)\\\\n![](/profile/upload/2025/05/29/R-A_20250529172817A031.jpg)\\\\n![](/profile/upload/2025/05/29/wallhaven-exm8xk_20250529172817A032.jpg)\\\\n![](/profile/upload/2025/05/29/wallhaven-m3wx3m_20250529172817A033.jpg)\\\\n\\\",\\\"informTitle\\\":\\\"尊敬的创作者，您的图片已被查看原图，这是对您的积分分成，详细可以查看积分使用记录。\\\",\\\"locale\\\":\\\"zh-CN\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":18,\\\"templateKey\\\":\\\"download_picture_author_proportion\\\",\\\"templateName\\\":\\\"图片查看原图作者分成积分提醒\\\",\\\"templateType\\\":\\\"3\\\",\\\"templateVersion\\\":2,\\\"updateBy\\\":\\\"admin\\\",\\\"updateTime\\\":\\\"2025-07-25 22:26:17\\\",\\\"variables\\\":\\\"{\\\\n   \\\\\\\"pictureName\\\\\\\":\\\\\\\"YY\\\\\\\",\\\\n   \\\\\\\"points\\\\\\\":\\\\\\\"10000\\\\\\\",\\\\n   \\\\\\\"createTime\\\\\\\":\\\\\\\"2025-05-26 10:11:12\\\\\\\"\\\\n}\\\"}\"}', '尊敬的创作者，您的图片已被查看原图，这是对您的积分分成，详细可以查看积分使用记录。\n> 图片名称：${pictureName}\n> 获得积分：${points}\n> 时间：${createTime}\n\n![123](/profile/upload/2025/05/29/20250310_20250529172604A028.png)\n![](/profile/upload/2025/05/29/20250310_20250529172816A029.png)\n![](/profile/upload/2025/05/29/pexels-photo-14471335_20250529172817A030.jpeg)\n![](/profile/upload/2025/05/29/R-A_20250529172817A031.jpg)\n![](/profile/upload/2025/05/29/wallhaven-exm8xk_20250529172817A032.jpg)\n![](/profile/upload/2025/05/29/wallhaven-m3wx3m_20250529172817A033.jpg)\n', '尊敬的创作者，您的图片已被查看原图，这是对您的积分分成，详细可以查看积分使用记录。\n> 图片名称：YY\n> 获得积分：10000\n> 时间：2025-05-26 10:11:12\n\n![123](/profile/upload/2025/05/29/20250310_20250529172604A028.png)\n![](/profile/upload/2025/05/29/20250310_20250529172816A029.png)\n![](/profile/upload/2025/05/29/pexels-photo-14471335_20250529172817A030.jpeg)\n![](/profile/upload/2025/05/29/R-A_20250529172817A031.jpg)\n![](/profile/upload/2025/05/29/wallhaven-exm8xk_20250529172817A032.jpg)\n![](/profile/upload/2025/05/29/wallhaven-m3wx3m_20250529172817A033.jpg)\n', '{\n   \"pictureName\":\"YY\",\n   \"points\":\"10000\",\n   \"createTime\":\"2025-05-26 10:11:12\"\n}', NULL, '0', 'admin', '2025-05-29 16:09:30', 'admin', '2025-07-25 22:26:17', NULL);
INSERT INTO `c_inform_template_info` VALUES (19, '账号重置密码', 'sms_account_reset_password_code', 'zh-CN', '阿里云', '1', 'SMS_483940269', '荔枝软件开发工作室短信', NULL, NULL, 2, '{1:\"{\\\"channel\\\":\\\"阿里云\\\",\\\"content\\\":\\\"尊敬的用户，您正在校验敏感操作，验证码为：${code}，请立即验证，感谢支持（若非本人请勿泄露验证码）。\\\",\\\"example\\\":\\\"尊敬的用户，您正在校验敏感操作，验证码为：123456，请立即验证，感谢支持（若非本人请勿泄露验证码）。\\\",\\\"locale\\\":\\\"zh\\\",\\\"serviceSignName\\\":\\\"荔枝开发阶段短信服务\\\",\\\"serviceTemplateId\\\":\\\"SMS_480850068\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":14,\\\"templateKey\\\":\\\"sms_forget_password_code\\\",\\\"templateName\\\":\\\"忘记密码验证码\\\",\\\"templateType\\\":\\\"1\\\",\\\"templateVersion\\\":1,\\\"variables\\\":\\\"{\\\\\\\"code\\\\\\\":\\\\\\\"123456\\\\\\\"}\\\"}\",2:\"{\\\"channel\\\":\\\"阿里云\\\",\\\"content\\\":\\\"尊敬的用户，您正在校验敏感操作，验证码为：${code}，请立即验证，感谢支持（若非本人请勿泄露验证码）。\\\",\\\"example\\\":\\\"尊敬的用户，您正在校验敏感操作，验证码为：123456，请立即验证，感谢支持（若非本人请勿泄露验证码）。\\\",\\\"locale\\\":\\\"zh-CN\\\",\\\"serviceSignName\\\":\\\"荔枝软件开发工作室短信\\\",\\\"serviceTemplateId\\\":\\\"SMS_483940269\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":19,\\\"templateKey\\\":\\\"sms_account_reset_password_code\\\",\\\"templateName\\\":\\\"账号重置密码\\\",\\\"templateType\\\":\\\"1\\\",\\\"templateVersion\\\":2,\\\"updateBy\\\":\\\"admin\\\",\\\"updateTime\\\":\\\"2025-07-30 00:15:11\\\",\\\"variables\\\":\\\"{\\\\\\\"code\\\\\\\":\\\\\\\"123456\\\\\\\"}\\\"}\"}', '尊敬的用户，您正在校验敏感操作，验证码为：${code}，请立即验证，感谢支持（若非本人请勿泄露验证码）。', '尊敬的用户，您正在校验敏感操作，验证码为：123456，请立即验证，感谢支持（若非本人请勿泄露验证码）。', '{\"code\":\"123456\"}', NULL, '0', 'admin', '2025-04-23 16:36:19', 'admin', '2025-07-30 00:15:12', NULL);
INSERT INTO `c_inform_template_info` VALUES (20, '用户图片举报成功', 'picture_report_success', 'zh-CN', NULL, '3', NULL, NULL, '尊敬的用户，您的举报已经成立，详细请查看详情', NULL, 1, '{1:\"{\\\"content\\\":\\\"尊敬的用户${userName}，您举报的图片已经成立，感谢您的举报。\\\\n> 用户：${userName}\\\\n> 目标：${targetContent}\\\\n> 举报类型：${reportType}\\\",\\\"example\\\":\\\"尊敬的用户YY，您举报的图片已经成立，感谢您的举报。\\\\n> 用户：YY\\\\n> 目标：懒羊羊4k壁纸\\\\n> 举报类型：图片\\\",\\\"informTitle\\\":\\\"尊敬的用户，您的举报已经成立，详细请查看详情\\\",\\\"locale\\\":\\\"zh-CN\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":20,\\\"templateKey\\\":\\\"picture_report_success\\\",\\\"templateName\\\":\\\"用户图片举报成功\\\",\\\"templateType\\\":\\\"3\\\",\\\"templateVersion\\\":1,\\\"variables\\\":\\\"{\\\\n      \\\\\\\"userName\\\\\\\":\\\\\\\"YY\\\\\\\",\\\\n      \\\\\\\"targetContent\\\\\\\":\\\\\\\"懒羊羊4k壁纸\\\\\\\",\\\\n      \\\\\\\"reportType\\\\\\\":\\\\\\\"图片\\\\\\\"\\\\n}\\\"}\"}', '尊敬的用户${userName}，您举报的图片已经成立，感谢您的举报。\n> 用户：${userName}\n> 目标类型：${targetType}\n> 目标：${targetContent}\n> 举报类型：${reportType}\n> 审核信息：${reviewMessage}', '尊敬的用户YY，您举报的图片已经成立，感谢您的举报。\n> 用户：YY\n> 目标类型：图片\n> 目标：懒羊羊4k壁纸\n> 举报类型：侵权\n> 审核信息：该图片确实已经侵权·，已处理', '{\n      \"userName\":\"YY\",\n      \"targetContent\":\"懒羊羊4k壁纸\",\n      \"targetType\":\"图片\",\n      \"reportType\":\"侵权\",\n      \"reviewMessage\":\"该图片确实已经侵权·，已处理\"\n}', NULL, '0', 'admin', '2025-06-17 21:49:05', 'admin', '2025-06-17 22:36:19', NULL);
INSERT INTO `c_inform_template_info` VALUES (21, '用户图片被举报举报通知', 'picture_report_remind', 'zh-CN', NULL, '3', NULL, NULL, '尊敬的用户，您的图片已被举报且已成立，请查看详情。', NULL, 1, '{1:\"{\\\"content\\\":\\\"尊敬的用户${userName}，您的图片已被举报且已成立，请根据平台要求上传发布平台，如若被举报成立的次数过多，平台将会封禁您对应的权限。\\\\n> 用户：${userName}\\\\n> 举报类型：${targetType}\\\\n> 被举报内容：${targetContent}\\\\n> 举报类型：${reportType}\\\",\\\"example\\\":\\\"尊敬的用户YY，您的图片已被举报且已成立，请根据平台要求上传发布平台，如若被举报成立的次数过多，平台将会封禁您对应的权限。\\\\n> 用户：YY\\\\n> 举报类型：图片\\\\n> 被举报内容：懒羊羊4k壁纸\\\\n> 举报类型：侵权\\\",\\\"locale\\\":\\\"zh-CN\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":21,\\\"templateKey\\\":\\\"picture_report_remind\\\",\\\"templateName\\\":\\\"用户图片被举报举报通知\\\",\\\"templateType\\\":\\\"3\\\",\\\"templateVersion\\\":1,\\\"variables\\\":\\\"{\\\\n      \\\\\\\"userName\\\\\\\":\\\\\\\"YY\\\\\\\",\\\\n      \\\\\\\"targetContent\\\\\\\":\\\\\\\"懒羊羊4k壁纸\\\\\\\",\\\\n      \\\\\\\"targetType\\\\\\\":\\\\\\\"图片\\\\\\\",\\\\n      \\\\\\\"reportType\\\\\\\":\\\\\\\"侵权\\\\\\\"\\\\n}\\\"}\"}', '尊敬的用户${userName}，您的图片已被举报且已成立，请根据平台要求上传发布平台，如若被举报成立的次数过多，平台将会封禁您对应的权限。\n> 用户：${userName}\n> 举报类型：${targetType}\n> 被举报内容：${targetContent}\n> 举报类型：${reportType}\n> 审核信息：${reviewMessage}', '尊敬的用户YY，您的图片已被举报且已成立，请根据平台要求上传发布平台，如若被举报成立的次数过多，平台将会封禁您对应的权限。\n> 用户：YY\n> 举报类型：图片\n> 被举报内容：懒羊羊4k壁纸\n> 举报类型：侵权\n> 审核信息：该图片确实已经侵权·，已处理', '{\n      \"userName\":\"YY\",\n      \"targetContent\":\"懒羊羊4k壁纸\",\n      \"targetType\":\"图片\",\n      \"reportType\":\"侵权\",\n      \"reviewMessage\":\"该图片确实已经侵权·，已处理\"\n}', NULL, '0', 'admin', '2025-06-17 21:55:37', 'admin', '2025-06-17 23:09:40', NULL);
INSERT INTO `c_inform_template_info` VALUES (22, '图片申请审核', 'picture_apply_review', 'zh-CN', NULL, '3', NULL, NULL, '尊敬的用户，您的图片已经审核，请查看详细信息。', NULL, 1, '{1:\"{\\\"content\\\":\\\"尊敬的用户，您的图片已经审核，请查看详细信息。\\\\n>图片编号：${pictureId}\\\\n>图片名称：${pictureName}\\\\n>申请类型：${applyTime}\\\\n>申请理由：${applyReson}\\\\n>审核状态：${reviewStatue}\\\\n>审核描述: ${reviewMessage}\\\\n>审核时间：${reviewTime}\\\",\\\"example\\\":\\\"尊敬的用户，您的图片已经审核，请查看详细信息。\\\\n>图片编号：123456\\\\n>图片名称：夕阳下的海岸\\\\n>申请类型：2025-06-25 14:30:00\\\\n>申请理由：用于毕业设计展示项目\\\\n>审核状态：已通过\\\\n>审核描述: 图片质量良好，符合平台规范。\\\\n>审核时间：2025-06-26 10:15:00\\\",\\\"informTitle\\\":\\\"尊敬的用户，您的图片已经审核，请查看详细信息。\\\",\\\"locale\\\":\\\"zh-CN\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":22,\\\"templateKey\\\":\\\"picture_apply_review\\\",\\\"templateName\\\":\\\"图片申请审核\\\",\\\"templateType\\\":\\\"3\\\",\\\"templateVersion\\\":1,\\\"variables\\\":\\\"{\\\\n  \\\\\\\"pictureId\\\\\\\": \\\\\\\"123456\\\\\\\",\\\\n  \\\\\\\"pictureName\\\\\\\": \\\\\\\"夕阳下的海岸\\\\\\\",\\\\n  \\\\\\\"applyTime\\\\\\\": \\\\\\\"2025-06-25 14:30:00\\\\\\\",\\\\n  \\\\\\\"applyReson\\\\\\\": \\\\\\\"用于毕业设计展示项目\\\\\\\",\\\\n  \\\\\\\"reviewStatue\\\\\\\": \\\\\\\"已通过\\\\\\\",\\\\n  \\\\\\\"reviewMessage\\\\\\\": \\\\\\\"图片质量良好，符合平台规范。\\\\\\\",\\\\n  \\\\\\\"reviewTime\\\\\\\": \\\\\\\"2025-06-26 10:15:00\\\\\\\"\\\\n}\\\\n\\\"}\"}', '尊敬的用户，您的图片已经审核，请查看详细信息。\n>图片编号：${pictureId}\n>图片名称：${pictureName}\n>申请类型：${applyType}\n>申请时间：${applyTime}\n>申请理由：${applyReason}\n>审核状态：${reviewStatue}\n>审核描述: ${reviewMessage}\n>审核时间：${reviewTime}\n感谢您的支持！', '尊敬的用户，您的图片已经审核，请查看详细信息。\n>图片编号：123456\n>图片名称：夕阳下的海岸\n>申请类型：原创\n>申请时间：2025-06-25 14:30:00\n>申请理由：用于毕业设计展示项目\n>审核状态：已通过\n>审核描述: 图片质量良好，符合平台规范。\n>审核时间：2025-06-26 10:15:00\n感谢您的支持！', '{\n  \"pictureId\": \"123456\",\n  \"pictureName\": \"夕阳下的海岸\",\n  \"applyType\":\"原创\",\n  \"applyTime\": \"2025-06-25 14:30:00\",\n  \"applyReason\": \"用于毕业设计展示项目\",\n  \"reviewStatue\": \"已通过\",\n  \"reviewMessage\": \"图片质量良好，符合平台规范。\",\n  \"reviewTime\": \"2025-06-26 10:15:00\"\n}\n', NULL, '0', 'admin', '2025-06-26 18:49:40', 'admin', '2025-06-26 18:58:14', NULL);
INSERT INTO `c_inform_template_info` VALUES (23, '扩容成功通知', 'picture_space_dilatation_success', 'zh-CN', NULL, '3', NULL, NULL, '尊敬的用户，您的空间已经扩容成功', NULL, 1, '{1:\"{\\\"content\\\":\\\"尊敬的用户，您的空间已经扩容成功，扩容成功您的空间可获得更多的使用权益。\\\\n>空间名称：${spaceName}\\\\n>扩容类型：${dilatationType}\\\\n>扩容单价：${dilatationUnit}\\\\n>扩容总数：${dilatationTotal}\\\\n>消耗积分：${ponintsTotal}\\\\n>时间:${createTime}\\\",\\\"example\\\":\\\"尊敬的用户，您的空间已经扩容成功，扩容成功您的空间可获得更多的使用权益。\\\\n>空间名称：我的私人空间\\\\n>扩容类型：容量扩容\\\\n>扩容单价：10积分/GB\\\\n>扩容总数：5GB\\\\n>消耗积分：50\\\\n>时间:2025-06-26 18:45:00\\\",\\\"informTitle\\\":\\\"尊敬的用户，您的空间已经扩容成功\\\",\\\"locale\\\":\\\"zh-CN\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":23,\\\"templateKey\\\":\\\"picture:dilatation:success\\\",\\\"templateName\\\":\\\"扩容成功通知\\\",\\\"templateType\\\":\\\"3\\\",\\\"templateVersion\\\":1,\\\"variables\\\":\\\"{\\\\n  \\\\\\\"spaceName\\\\\\\": \\\\\\\"我的私人空间\\\\\\\",\\\\n  \\\\\\\"dilatationType\\\\\\\": \\\\\\\"容量扩容\\\\\\\",\\\\n  \\\\\\\"dilatationUnit\\\\\\\": \\\\\\\"10积分/GB\\\\\\\",\\\\n  \\\\\\\"dilatationTotal\\\\\\\": \\\\\\\"5GB\\\\\\\",\\\\n  \\\\\\\"ponintsTotal\\\\\\\": \\\\\\\"50\\\\\\\",\\\\n  \\\\\\\"createTime\\\\\\\": \\\\\\\"2025-06-26 18:45:00\\\\\\\"\\\\n}\\\\n\\\"}\"}', '尊敬的用户，您的空间已经扩容成功，扩容成功您的空间可获得更多的使用权益。\n>空间名称：${spaceName}\n>扩容类型：${dilatationType}\n>扩容单价：${dilatationUnit}\n>扩容总数：${dilatationTotal}\n>消耗积分：${pointsTotal}\n>时间:${createTime}', '尊敬的用户，您的空间已经扩容成功，扩容成功您的空间可获得更多的使用权益。\n>空间名称：我的私人空间\n>扩容类型：容量扩容\n>扩容单价：10积分/GB\n>扩容总数：5GB\n>消耗积分：50\n>时间:2025-06-26 18:45:00', '{\n  \"spaceName\": \"我的私人空间\",\n  \"dilatationType\": \"容量扩容\",\n  \"dilatationUnit\": \"10积分/GB\",\n  \"dilatationTotal\": \"5GB\",\n  \"pointsTotal\": \"50\",\n  \"createTime\": \"2025-06-26 18:45:00\"\n}\n', NULL, '0', 'admin', '2025-06-28 19:52:30', 'admin', '2025-07-01 00:49:52', NULL);
INSERT INTO `c_inform_template_info` VALUES (24, '团队空间邀请', 'picture_space_invitation', 'zh-CN', NULL, '3', NULL, NULL, '尊敬的用户，您有一份团队空间邀请，请前往邀请记录查看', NULL, 1, '{1:\"{\\\"content\\\":\\\"尊敬的用户，您有一份团队空间邀请，详细信息请前往团队空间邀请记录查看。\\\\n>邀请编号：${invitationId}\\\\n>空间名称：${spaceName}\\\\n>邀请角色：${roleType}\\\\n>邀请时间：${createTime}\\\",\\\"example\\\":\\\"尊敬的用户，您有一份团队空间邀请，详细信息请前往团队空间邀请记录查看。\\\\n>邀请编号：INV-20250626002\\\\n>空间名称：前端开发协作空间\\\\n>邀请角色：管理员\\\\n>邀请时间：2025-06-26 21:10:00\\\",\\\"informTitle\\\":\\\"尊敬的用户，您有一份团队空间邀请，请前往邀请记录查看\\\",\\\"locale\\\":\\\"zh-CN\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":24,\\\"templateKey\\\":\\\"picture_space_invitation\\\",\\\"templateName\\\":\\\"团队空间邀请\\\",\\\"templateType\\\":\\\"3\\\",\\\"templateVersion\\\":1,\\\"variables\\\":\\\"{\\\\n  \\\\\\\"invitationId\\\\\\\": \\\\\\\"INV-20250626002\\\\\\\",\\\\n  \\\\\\\"spaceName\\\\\\\": \\\\\\\"前端开发协作空间\\\\\\\",\\\\n  \\\\\\\"roleType\\\\\\\": \\\\\\\"管理员\\\\\\\",\\\\n  \\\\\\\"createTime\\\\\\\": \\\\\\\"2025-06-26 21:10:00\\\\\\\"\\\\n}\\\\n\\\"}\"}', '尊敬的用户，您有一份团队空间邀请，详细信息请前往团队空间邀请记录查看。\n>邀请编号：${invitationId}\n>空间名称：${spaceName}\n>邀请角色：${roleType}\n>邀请时间：${createTime}', '尊敬的用户，您有一份团队空间邀请，详细信息请前往团队空间邀请记录查看。\n>邀请编号：INV-20250626002\n>空间名称：前端开发协作空间\n>邀请角色：管理员\n>邀请时间：2025-06-26 21:10:00', '{\n  \"invitationId\": \"INV-20250626002\",\n  \"spaceName\": \"前端开发协作空间\",\n  \"roleType\": \"管理员\",\n  \"createTime\": \"2025-06-26 21:10:00\"\n}\n', NULL, '0', 'admin', '2025-07-01 00:50:20', NULL, NULL, NULL);
INSERT INTO `c_inform_template_info` VALUES (25, '团队空间邀请进入空间', 'picture_space_invitation_success', 'zh-CN', NULL, '3', NULL, NULL, '尊敬的用户，您邀请的用户已进入您的空间，请前往查看', NULL, 1, '{1:\"{\\\"content\\\":\\\"尊敬的用户，用户${userName}于${joinTime}进入空间${spaceName}。\\\\n>邀请编号：${invitationId}\\\\n>空间名称：${spaceName}\\\\n>邀请角色：${roleType}\\\\n>邀请时间：${createTime}\\\",\\\"example\\\":\\\"尊敬的用户，用户YY于2025-06-26 21:10:00进入空间前端开发协作空间。\\\\n>邀请编号：INV-20250626002\\\\n>空间名称：前端开发协作空间\\\\n>邀请角色：管理员\\\\n>邀请时间：2025-06-26 21:10:00\\\",\\\"informTitle\\\":\\\"尊敬的用户，您邀请的用户已进入您的空间，请前往查看\\\",\\\"locale\\\":\\\"zh-CN\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":25,\\\"templateKey\\\":\\\"picture_space_invitation_join\\\",\\\"templateName\\\":\\\"团队空间邀请进入空间\\\",\\\"templateType\\\":\\\"3\\\",\\\"templateVersion\\\":1,\\\"variables\\\":\\\"{\\\\n \\\\\\\"userName\\\\\\\":\\\\\\\"YY\\\\\\\",\\\\n  \\\\\\\"invitationId\\\\\\\": \\\\\\\"INV-20250626002\\\\\\\",\\\\n  \\\\\\\"spaceName\\\\\\\": \\\\\\\"前端开发协作空间\\\\\\\",\\\\n  \\\\\\\"roleType\\\\\\\": \\\\\\\"管理员\\\\\\\",\\\\n  \\\\\\\"createTime\\\\\\\": \\\\\\\"2025-06-26 21:10:00\\\\\\\",\\\\n\\\\\\\"joinTime\\\\\\\": \\\\\\\"2025-06-26 21:10:00\\\\\\\"\\\\n}\\\"}\"}', '尊敬的用户，用户${userName}于${joinTime}进入空间${spaceName}。\n>邀请编号：${invitationId}\n>空间名称：${spaceName}\n>邀请角色：${roleType}\n>邀请时间：${createTime}', '尊敬的用户，用户YY于2025-06-26 21:10:00进入空间前端开发协作空间。\n>邀请编号：INV-20250626002\n>空间名称：前端开发协作空间\n>邀请角色：管理员\n>邀请时间：2025-06-26 21:10:00', '{\n \"userName\":\"YY\",\n  \"invitationId\": \"INV-20250626002\",\n  \"spaceName\": \"前端开发协作空间\",\n  \"roleType\": \"管理员\",\n  \"createTime\": \"2025-06-26 21:10:00\",\n\"joinTime\": \"2025-06-26 21:10:00\"\n}', NULL, '0', 'admin', '2025-07-01 00:54:16', 'admin', '2025-07-01 00:55:39', NULL);
INSERT INTO `c_inform_template_info` VALUES (26, '退出团队空间', 'picture_space_invitation_delete', 'zh-CN', NULL, '3', NULL, NULL, '用户已退出团队空间，请查看详情', NULL, 1, '{1:\"{\\\"content\\\":\\\"用户${userName}于${createTime}退出空间${spaceName}\\\\n>用户名称：${userName}\\\\n>空间名称：${spaceName}\\\\n>邀请角色：${roleType}\\\\n>加入时间:${joinTime}\\\",\\\"example\\\":\\\"用户YY于2025-06-26 21:10:00退出空间前端开发协作空间\\\\n>用户名称：YY\\\\n>空间名称：前端开发协作空间\\\\n>邀请角色：管理员\\\\n>加入时间:2025-06-26 21:10:00\\\",\\\"informTitle\\\":\\\"用户已退出团队空间，请查看详情\\\",\\\"locale\\\":\\\"zh-CN\\\",\\\"status\\\":\\\"0\\\",\\\"templateId\\\":26,\\\"templateKey\\\":\\\"picture_space_invitation_delete\\\",\\\"templateName\\\":\\\"退出团队空间\\\",\\\"templateType\\\":\\\"3\\\",\\\"templateVersion\\\":1,\\\"variables\\\":\\\"{\\\\n \\\\\\\"userName\\\\\\\":\\\\\\\"YY\\\\\\\",\\\\n  \\\\\\\"invitationId\\\\\\\": \\\\\\\"INV-20250626002\\\\\\\",\\\\n  \\\\\\\"spaceName\\\\\\\": \\\\\\\"前端开发协作空间\\\\\\\",\\\\n  \\\\\\\"roleType\\\\\\\": \\\\\\\"管理员\\\\\\\",\\\\n  \\\\\\\"createTime\\\\\\\": \\\\\\\"2025-06-26 21:10:00\\\\\\\",\\\\n\\\\\\\"joinTime\\\\\\\": \\\\\\\"2025-06-26 21:10:00\\\\\\\"\\\\n}\\\"}\"}', '用户${userName}于${createTime}退出空间${spaceName}\n>用户名称：${userName}\n>空间名称：${spaceName}\n>邀请角色：${roleType}\n>加入时间：${joinTime}', '用户YY于2025-06-26 21:10:00退出空间前端开发协作空间\n>用户名称：YY\n>空间名称：前端开发协作空间\n>邀请角色：管理员\n>加入时间：2025-06-26 21:10:00', '{\n \"userName\":\"YY\",\n  \"spaceName\": \"前端开发协作空间\",\n  \"roleType\": \"管理员\",\n  \"createTime\": \"2025-06-26 21:10:00\",\n\"joinTime\": \"2025-06-26 21:10:00\"\n}', NULL, '0', 'admin', '2025-07-01 01:08:11', 'admin', '2025-07-01 01:16:43', NULL);
INSERT INTO `c_inform_template_info` VALUES (27, '图片参考生成图片作者分成积分提醒', 'ai_generate_reference_image_remind', 'zh-CN', NULL, '3', NULL, NULL, '尊敬的创作者，您的图片被用户引用，这是对您的积分分成，详细可以查看积分使用记录。', NULL, 1, '', '尊敬的创作者，您的图片已被其他用户引用，这是对您的积分分成，详细可以查看积分使用记录。\n> 图片名称：${pictureName}\n> 获得积分：${points}\n> 时间：${createTime}\n\n![123](/profile/upload/2025/05/29/20250310_20250529172604A028.png)\n![](/profile/upload/2025/05/29/20250310_20250529172816A029.png)\n![](/profile/upload/2025/05/29/pexels-photo-14471335_20250529172817A030.jpeg)\n![](/profile/upload/2025/05/29/R-A_20250529172817A031.jpg)\n![](/profile/upload/2025/05/29/wallhaven-exm8xk_20250529172817A032.jpg)\n![](/profile/upload/2025/05/29/wallhaven-m3wx3m_20250529172817A033.jpg)\n', '尊敬的创作者，您的图片已被查看原图，这是对您的积分分成，详细可以查看积分使用记录。\n> 图片名称：YY\n> 获得积分：10000\n> 时间：2025-05-26 10:11:12\n\n![123](/profile/upload/2025/05/29/20250310_20250529172604A028.png)\n![](/profile/upload/2025/05/29/20250310_20250529172816A029.png)\n![](/profile/upload/2025/05/29/pexels-photo-14471335_20250529172817A030.jpeg)\n![](/profile/upload/2025/05/29/R-A_20250529172817A031.jpg)\n![](/profile/upload/2025/05/29/wallhaven-exm8xk_20250529172817A032.jpg)\n![](/profile/upload/2025/05/29/wallhaven-m3wx3m_20250529172817A033.jpg)\n', '{\n   \"pictureName\":\"YY\",\n   \"points\":\"10000\",\n   \"createTime\":\"2025-05-26 10:11:12\"\n}', NULL, '0', 'admin', '2025-05-29 16:09:30', 'admin', '2025-07-25 22:26:17', NULL);

-- ----------------------------
-- Table structure for c_menu_info
-- ----------------------------
DROP TABLE IF EXISTS `c_menu_info`;
CREATE TABLE `c_menu_info`  (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `parent_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '父菜单',
  `order_num` int NULL DEFAULT NULL COMMENT '显示顺序',
  `path` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由地址',
  `component` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由名称',
  `menu_address` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '显示位置',
  `is_frame` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '是否外链',
  `is_cache` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '是否缓存',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单类型',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '是否显示',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '菜单状态',
  `perms` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE,
  UNIQUE INDEX `menu_name`(`menu_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of c_menu_info
-- ----------------------------
INSERT INTO `c_menu_info` VALUES (1, '图库信息', '0', 1, 'picture', NULL, NULL, 'picture', '2', '1', '1', 'M', '0', '0', 'picture', 'space', 'admin', '2025-03-31 00:20:04', 'admin', '2025-07-30 16:41:08', NULL);
INSERT INTO `c_menu_info` VALUES (2, '我的空间', '1', 0, 'space', 'picture/space/space', NULL, 'PictureSapce', '2', '1', '0', 'C', '0', '0', 'picture:space', '', 'admin', '2025-03-31 00:22:32', 'admin', '2025-07-30 16:41:03', NULL);
INSERT INTO `c_menu_info` VALUES (4, '图片上传', '1', 0, 'upload', 'picture/picture/pictureUpdate', NULL, 'PictureUpload', '2', '1', '0', 'C', '0', '0', 'picture:upload', '', 'admin', '2025-04-01 15:01:57', 'admin', '2025-07-30 16:41:13', NULL);
INSERT INTO `c_menu_info` VALUES (5, '添加空间', '2', 0, NULL, NULL, NULL, NULL, '1', '1', '1', 'B', '0', '0', 'picture:space:add', NULL, 'admin', '2025-04-02 09:00:42', 'admin', '2025-04-04 22:45:22', NULL);
INSERT INTO `c_menu_info` VALUES (6, '更新空间', '2', 1, NULL, NULL, NULL, NULL, '1', '1', '1', 'B', '0', '0', 'picture:space:update', NULL, 'admin', '2025-04-04 22:45:03', 'admin', '2025-04-08 23:17:13', NULL);
INSERT INTO `c_menu_info` VALUES (7, '文件夹', '1', 3, 'space/spaceFolder', 'picture/space/spaceFolder', NULL, 'PictureSpaceFolder', '1', '1', '1', 'C', '0', '0', 'picture:space:spaceFolder', 'folder', 'admin', '2025-04-07 17:30:29', 'admin', '2025-07-21 02:56:49', NULL);
INSERT INTO `c_menu_info` VALUES (8, '添加文件夹', '7', 0, NULL, NULL, NULL, NULL, '1', '1', '1', 'B', '0', '0', 'picture:space:spaceFolder:add', NULL, 'admin', '2025-04-07 22:02:16', NULL, NULL, '新增文件夹');
INSERT INTO `c_menu_info` VALUES (9, '删除文件夹', '7', 0, NULL, NULL, NULL, NULL, '1', '1', '1', 'B', '0', '0', 'picture:space:spaceFolder:delete', NULL, 'admin', '2025-04-08 00:17:31', NULL, NULL, NULL);
INSERT INTO `c_menu_info` VALUES (10, '更新文件夹', '7', 0, NULL, NULL, NULL, NULL, '1', '1', '1', 'B', '0', '0', 'picture:space:spaceFolder:update', NULL, 'admin', '2025-04-08 00:18:08', NULL, NULL, NULL);
INSERT INTO `c_menu_info` VALUES (11, '查看图片分类', '4', 0, NULL, NULL, NULL, NULL, '1', '1', '1', 'B', '0', '0', 'picture:pictureCategoryInfo', NULL, 'admin', '2025-04-08 15:02:10', 'admin', '2025-04-08 15:02:16', NULL);
INSERT INTO `c_menu_info` VALUES (12, '查看标签', '4', 1, NULL, NULL, NULL, NULL, '1', '1', '1', 'B', '0', '0', 'picture:tag', NULL, 'admin', '2025-04-09 00:05:57', 'admin', '2025-04-09 00:25:22', '图片标签');
INSERT INTO `c_menu_info` VALUES (13, '图片详情', '4', 0, NULL, NULL, NULL, NULL, '1', '1', '1', 'B', '0', '0', 'picture:upload:detail', NULL, 'admin', '2025-04-11 16:52:10', NULL, NULL, NULL);
INSERT INTO `c_menu_info` VALUES (14, '行为记录', '1', 3, 'picture/userBehaviorInfo', NULL, NULL, NULL, '1', '1', '1', 'T', '0', '0', 'picture:userBehaviorInfo', NULL, 'admin', '2025-04-14 16:50:27', 'admin', '2025-05-23 18:21:31', '行为记录');
INSERT INTO `c_menu_info` VALUES (15, '新增行为记录', '14', 0, NULL, NULL, NULL, NULL, '1', '1', '1', 'B', '0', '0', 'picture:userBehaviorInfo:add', NULL, 'admin', '2025-04-14 16:53:36', NULL, NULL, NULL);
INSERT INTO `c_menu_info` VALUES (16, '图片下载', '4', 0, NULL, NULL, NULL, NULL, '1', '1', '1', 'B', '0', '0', 'picture:download', NULL, 'admin', '2025-04-16 23:03:18', 'admin', '2025-04-24 15:08:44', NULL);
INSERT INTO `c_menu_info` VALUES (17, '个人中心', '0', 0, 'userinfo', 'user/userinfo', NULL, 'userinfo', '3', '1', '0', 'C', '0', '0', 'userinfo', 'user', 'admin', '2025-04-17 11:05:04', 'admin', '2025-07-14 22:02:28', NULL);
INSERT INTO `c_menu_info` VALUES (18, 'AI扩图', '4', 5, NULL, NULL, NULL, NULL, '1', '1', '1', 'F', '1', '1', 'picture:ai:outPainting:createTask', NULL, 'admin', '2025-04-23 18:04:06', 'admin', '2025-07-30 00:29:56', NULL);
INSERT INTO `c_menu_info` VALUES (19, '图片列表', '4', 7, NULL, NULL, NULL, NULL, '1', '1', '1', 'F', '0', '0', 'picture:list', NULL, 'admin', '2025-04-23 21:42:54', 'admin', '2025-05-23 21:20:35', '查看图片列表、用户浏览记录、查看空间、查看用户等操作');
INSERT INTO `c_menu_info` VALUES (20, '图片编辑', '1', 2, 'pictureEdit', 'picture/picture/pictureEdit', NULL, 'PictureEdit', '1', '1', '0', 'C', '0', '0', 'picture:picture:pictureEdit', NULL, 'admin', '2025-04-24 15:10:19', 'admin', '2025-07-21 02:56:41', NULL);
INSERT INTO `c_menu_info` VALUES (21, '积分充值', '0', 2, 'points', 'points/points', NULL, 'Points', '2', '1', '0', 'C', '0', '0', 'points', 'points', 'admin', '2025-05-13 08:50:45', 'admin', '2025-07-14 22:11:38', '积分充值、充值记录');
INSERT INTO `c_menu_info` VALUES (22, '支付套餐', '21', 0, 'points/payment', 'points/payment', NULL, 'PointsPayment', '1', '1', '0', 'B', '0', '0', 'points:payment', NULL, 'admin', '2025-05-13 15:49:37', 'admin', '2025-05-13 22:22:31', NULL);
INSERT INTO `c_menu_info` VALUES (23, '我的消息', '44', 1, 'inform', 'user/inform/informList', NULL, 'inform', '3', '1', '0', 'C', '0', '0', 'inform', 'inform', 'admin', '2025-05-27 01:31:44', 'admin', '2025-07-26 19:01:35', NULL);
INSERT INTO `c_menu_info` VALUES (25, '搜图上传', '1', 1, 'pictureSearchUpload', 'picture/picture/pictureSearchUpload', NULL, 'pictureSearchUpload', '2', '1', '0', 'C', '0', '0', 'picture:pictureSearchUpload', NULL, 'admin', '2025-06-12 09:32:36', 'admin', '2025-07-21 02:48:22', NULL);
INSERT INTO `c_menu_info` VALUES (26, '图片管理', '0', 2, 'pictureManage', NULL, NULL, NULL, '3', '1', '1', 'M', '0', '0', 'picture:manage', 'picture', 'admin', '2025-06-18 17:33:33', 'admin', '2025-06-18 17:50:22', NULL);
INSERT INTO `c_menu_info` VALUES (27, '我的图片', '26', 1, 'pictureManage/pictureTable', 'picture/picture/pictureTable', NULL, 'pictureTable', '3', '1', '0', 'C', '0', '0', 'picture:picture:pictureTable', 'picture', 'admin', '2025-06-18 17:36:42', 'admin', '2025-07-30 16:44:12', NULL);
INSERT INTO `c_menu_info` VALUES (28, '图片申请公开', '4', 6, NULL, NULL, NULL, NULL, '1', '1', '1', 'B', '0', '0', 'picture:upload:apply', NULL, 'admin', '2025-06-26 20:18:36', 'admin', '2025-06-26 20:18:42', NULL);
INSERT INTO `c_menu_info` VALUES (29, '空间管理', '0', 3, 'spaceManage', NULL, NULL, 'spaceManage', '3', '1', '1', 'M', '0', '0', 'space:manage', 'space', 'admin', '2025-06-28 16:46:19', 'admin', '2025-07-21 02:25:23', NULL);
INSERT INTO `c_menu_info` VALUES (30, '个人空间', '29', 0, 'spacePersonalTable', 'picture/space/spacePersonalTable', NULL, 'spacePersonalTable', '3', '1', '0', 'C', '0', '0', 'space:manage:personal:table', 'space', 'admin', '2025-06-28 16:49:03', 'admin', '2025-07-21 02:57:23', NULL);
INSERT INTO `c_menu_info` VALUES (31, '空间扩容', '30', 2, NULL, NULL, NULL, NULL, '1', '1', '1', 'B', '0', '0', 'space:manage:dilatation', NULL, 'admin', '2025-06-28 20:07:51', NULL, NULL, NULL);
INSERT INTO `c_menu_info` VALUES (32, '团队空间', '29', 1, 'spaceTeamTable', 'picture/space/spaceTeamTable', NULL, 'spaceTeamTable', '3', '1', '0', 'C', '0', '0', 'space:manage:team:table', 'team', 'admin', '2025-06-29 18:05:37', 'admin', '2025-07-21 02:57:33', NULL);
INSERT INTO `c_menu_info` VALUES (33, '邀请空间成员', '29', 1, 'invitation', 'picture/space/spaceInvitationInfoTable', NULL, 'spaceInvitationInfoTable', '3', '1', '0', 'C', '0', '0', 'space:invitation', 'invitation', 'admin', '2025-06-29 19:26:19', 'admin', '2025-07-30 16:42:46', NULL);
INSERT INTO `c_menu_info` VALUES (34, '空间成员', '29', 3, 'member', 'picture/space/spaceMemberInfoTable', NULL, 'spaceMemberInfoTable', '1', '1', '1', 'C', '0', '0', 'space:member', NULL, 'admin', '2025-06-30 23:29:29', 'admin', '2025-07-21 02:57:49', NULL);
INSERT INTO `c_menu_info` VALUES (35, '空间图片', '29', 2, 'pictureTeamTable', 'picture/picture/pictureTeamTable', NULL, 'pictureTeamTable', '1', '1', '0', 'C', '0', '0', 'picture:picture:pictureTeamTable', NULL, 'admin', '2025-07-01 19:51:28', 'admin', '2025-07-21 02:57:45', NULL);
INSERT INTO `c_menu_info` VALUES (44, '通知公告', '0', 1, 'notice', NULL, NULL, 'notice', '3', '1', '1', 'M', '0', '0', 'notice', 'notice', 'admin', '2025-07-26 19:01:23', NULL, NULL, NULL);
INSERT INTO `c_menu_info` VALUES (45, '平台公告', '44', 2, 'notice', 'user/inform/noticeList', NULL, 'noticeList', '3', '1', '0', 'C', '0', '0', 'notice', 'trumpet', 'admin', '2025-07-26 19:05:16', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for c_notice_info
-- ----------------------------
DROP TABLE IF EXISTS `c_notice_info`;
CREATE TABLE `c_notice_info`  (
  `notice_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告编号',
  `locale` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '语言 默认zh-CN',
  `platform` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知平台',
  `notice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告类型',
  `is_exhibit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '是否展示',
  `notice_title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告内容',
  `order_num` int NOT NULL COMMENT '排序',
  `notice_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告状态',
  `user_id` bigint NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `fk_notice_info_locale`(`locale` ASC) USING BTREE,
  CONSTRAINT `fk_notice_info_locale` FOREIGN KEY (`locale`) REFERENCES `c_i18n_locale_info` (`locale`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of c_notice_info
-- ----------------------------
INSERT INTO `c_notice_info` VALUES ('1949049745460826112', 'zh-CN', '0', '1', '1', '欢迎使用LZ-Picture', '  欢迎您使用LZ-Picture，在我们平台您不仅可以云管理自己的图片，更可以分享自己的图片，感谢您的访问。当用户访问您的资源之后，您可以获得积分奖励，如果您是空间的作者，本平台也有给您奖励哦，后续我们会陆续推出出AI模块，您可以用积分使用我的AI哦。', 0, '1', 1, '2025-08-01 00:00:00', '2025-08-31 00:00:00', NULL);

-- ----------------------------
-- Table structure for c_permission_info
-- ----------------------------
DROP TABLE IF EXISTS `c_permission_info`;
CREATE TABLE `c_permission_info`  (
  `permission_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `permission_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限名称',
  `parent_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '父菜单',
  `order_num` int NULL DEFAULT NULL COMMENT '显示顺序',
  `permission` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限标识',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '是否使用（0正常 1关闭）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`permission_id`) USING BTREE,
  UNIQUE INDEX `uk_c_permission_info_permission_name`(`permission_name` ASC) USING BTREE,
  UNIQUE INDEX `uk_c_permission_info_permission`(`permission` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1895390476631150603 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '权限信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of c_permission_info
-- ----------------------------
INSERT INTO `c_permission_info` VALUES (1, 'test', '0', 0, 'test', '0', 'admin', '2025-02-28 16:07:24', NULL, '2025-02-28 16:12:11', '312312321321312阿达是的21');
INSERT INTO `c_permission_info` VALUES (1895390476631150592, 'test_0', '1', 1, 'test_0', '1', 'admin', '2025-02-02 00:00:00', NULL, NULL, NULL);
INSERT INTO `c_permission_info` VALUES (1895390476631150593, 'test_1', '1', 1, 'test_1', '1', 'admin', '2025-02-02 00:00:00', NULL, NULL, NULL);
INSERT INTO `c_permission_info` VALUES (1895390476631150594, 'test_2', '1', 1, 'test_2', '1', 'admin', '2025-02-02 00:00:00', NULL, NULL, NULL);
INSERT INTO `c_permission_info` VALUES (1895390476631150595, 'test_3', '1', 1, 'test_3', '1', 'admin', '2025-02-02 00:00:00', NULL, NULL, NULL);
INSERT INTO `c_permission_info` VALUES (1895390476631150596, 'test_4', '1', 1, 'test_4', '1', 'admin', '2025-02-02 00:00:00', NULL, NULL, NULL);
INSERT INTO `c_permission_info` VALUES (1895390476631150597, 'test_5', '1', 1, 'test_5', '1', 'admin', '2025-02-02 00:00:00', NULL, NULL, NULL);
INSERT INTO `c_permission_info` VALUES (1895390476631150598, 'test_6', '1', 1, 'test_6', '1', 'admin', '2025-02-02 00:00:00', NULL, NULL, NULL);
INSERT INTO `c_permission_info` VALUES (1895390476631150599, 'test_7', '1', 1, 'test_7', '1', 'admin', '2025-02-02 00:00:00', NULL, NULL, NULL);
INSERT INTO `c_permission_info` VALUES (1895390476631150601, 'test_9', '1', 1, 'test_9', '1', 'admin', '2025-02-02 00:00:00', NULL, NULL, NULL);
INSERT INTO `c_permission_info` VALUES (1895390476631150602, '更新', '0', 1, 'update', '0', 'admin', '2025-03-08 13:46:00', NULL, NULL, '1');

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `table_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `tpl_web_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '前端模板类型（element-ui模版 element-plus模版）',
  `package_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 64 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成业务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `column_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint NULL DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 985 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成业务表字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for p_picture_apply_info
-- ----------------------------
DROP TABLE IF EXISTS `p_picture_apply_info`;
CREATE TABLE `p_picture_apply_info`  (
  `apply_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '申请编号',
  `picture_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片编号',
  `picture_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片名称',
  `thumbnail_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '缩略图 URL',
  `apply_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '申请类型',
  `apply_reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '申请理由',
  `apply_image` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '证明图片',
  `apply_file` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '证明文件',
  `contact` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系方式',
  `points_need` int NOT NULL DEFAULT 10 COMMENT '所需积分',
  `price_need` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '所需金额',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `review_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '审核状态',
  `review_message` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审核信息',
  `review_user_id` bigint NULL DEFAULT NULL COMMENT '审核人编号',
  `review_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  PRIMARY KEY (`apply_id`) USING BTREE,
  INDEX `fk_picture_apply_picture_id`(`picture_id` ASC) USING BTREE,
  INDEX `fk_picture_apply_user_id`(`user_id` ASC) USING BTREE,
  INDEX `fk_picture_apply_review_user_id`(`review_user_id` ASC) USING BTREE,
  CONSTRAINT `fk_picture_apply_picture_id` FOREIGN KEY (`picture_id`) REFERENCES `p_picture_info` (`picture_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_picture_apply_review_user_id` FOREIGN KEY (`review_user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_picture_apply_user_id` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '图片申请信息表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for p_picture_category_info
-- ----------------------------
DROP TABLE IF EXISTS `p_picture_category_info`;
CREATE TABLE `p_picture_category_info`  (
  `category_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类编号',
  `parent_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '父级分类编号',
  `ancestors` varchar(1280) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '祖级列表',
  `cover_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面图URL',
  `category_icon` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面图标',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `order_num` int NOT NULL DEFAULT 1 COMMENT '显示顺序',
  `category_desc` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类描述',
  `category_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '分类状态（0正常 1关闭）',
  `category_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '分类类型',
  `query_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '查询状态（0是 1否）',
  `usage_count` bigint NOT NULL DEFAULT 0 COMMENT '使用次数',
  `look_count` bigint NOT NULL DEFAULT 0 COMMENT '查看次数',
  `download_count` bigint NOT NULL DEFAULT 0 COMMENT '下载次数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除标记（0否 1是）',
  PRIMARY KEY (`category_id`) USING BTREE,
  INDEX `parent_id`(`parent_id` ASC) USING BTREE,
  INDEX `idx_usage_count`(`usage_count` ASC) USING BTREE,
  INDEX `idx_look_count`(`look_count` ASC) USING BTREE,
  INDEX `idx_download_count`(`download_count` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '图片分类信息表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for p_picture_comment_info
-- ----------------------------
DROP TABLE IF EXISTS `p_picture_comment_info`;
CREATE TABLE `p_picture_comment_info`  (
  `comment_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `parent_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '父级评论编号',
  `picture_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片编号',
  `category_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片分类',
  `tags` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片标签（格式：\"标签1\",\"标签2\"）',
  `content` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评论内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  `like_count` int NOT NULL DEFAULT 0 COMMENT '点赞数',
  `ip_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评论者IP属地',
  `picture_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评论图片URL',
  `comment_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '评论状态（0正常 1异常）',
  PRIMARY KEY (`comment_id`) USING BTREE,
  INDEX `idx_comment_user`(`user_id` ASC) USING BTREE,
  INDEX `idx_comment_picture`(`picture_id` ASC) USING BTREE,
  INDEX `idx_comment_category`(`category_id` ASC) USING BTREE,
  INDEX `idx_comment_parent`(`parent_id` ASC) USING BTREE,
  CONSTRAINT `fk_comment_category` FOREIGN KEY (`category_id`) REFERENCES `p_picture_category_info` (`category_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_comment_parent` FOREIGN KEY (`parent_id`) REFERENCES `p_picture_comment_info` (`comment_id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `fk_comment_picture` FOREIGN KEY (`picture_id`) REFERENCES `p_picture_info` (`picture_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `chk_like_count` CHECK (`like_count` >= 0)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '图片评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of p_picture_comment_info
-- ----------------------------

-- ----------------------------
-- Table structure for p_picture_comment_like_info
-- ----------------------------
DROP TABLE IF EXISTS `p_picture_comment_like_info`;
CREATE TABLE `p_picture_comment_like_info`  (
  `like_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '点赞编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `picture_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片编号',
  `target_cover` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面URL',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
  PRIMARY KEY (`like_id`) USING BTREE,
  UNIQUE INDEX `uk_comment_user_picture`(`user_id` ASC, `picture_id` ASC) USING BTREE,
  INDEX `idx_comment_like_user`(`user_id` ASC) USING BTREE,
  INDEX `idx_comment_like_picture`(`picture_id` ASC) USING BTREE,
  CONSTRAINT `fk_comment_like_picture` FOREIGN KEY (`picture_id`) REFERENCES `p_picture_info` (`picture_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_comment_like_user` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '图片点赞记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of p_picture_comment_like_info
-- ----------------------------

-- ----------------------------
-- Table structure for p_picture_download_log_info
-- ----------------------------
DROP TABLE IF EXISTS `p_picture_download_log_info`;
CREATE TABLE `p_picture_download_log_info`  (
  `download_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '下载编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `picture_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片编号',
  `category_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片分类',
  `picture_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片名称',
  `thumbnail_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '缩略图URL',
  `tags` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片标签（格式：\"标签1\",\"标签2\"）',
  `space_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '空间编号',
  `points_cost` int NOT NULL DEFAULT 0 COMMENT '消耗积分',
  `points_author_gain` int NOT NULL DEFAULT 0 COMMENT '作者分成积分',
  `points_official_gain` int NOT NULL DEFAULT 0 COMMENT '平台分成积分',
  `points_space_gain` int NULL DEFAULT 0 COMMENT '空间分成积分',
  `author_proportion` double(10, 2) NULL DEFAULT NULL COMMENT '作者分成比例',
  `official_proportion` double(10, 2) NULL DEFAULT NULL COMMENT '官方分成比例',
  `space_proportion` double(10, 2) NULL DEFAULT NULL COMMENT '空间分成比例',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下载时间',
  `download_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '下载状态（1失败 0成功）',
  `fail_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '失败原因',
  `download_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '下载类型（0查看 1下载 2批量下载',
  `refer_source` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '来源（0其他 1详情 2分享）',
  `has_statistics` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '是否统计（0否 1是）',
  `score` decimal(5, 2) NOT NULL COMMENT '分数',
  `ip_addr` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'IP地址',
  `ip_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP属地',
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '平台',
  PRIMARY KEY (`download_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `space_id`(`space_id` ASC) USING BTREE,
  INDEX `idx_download_time`(`create_time` ASC) USING BTREE,
  INDEX `idx_download_status`(`download_status` ASC) USING BTREE,
  INDEX `idx_picture`(`picture_id` ASC) USING BTREE,
  CONSTRAINT `p_picture_download_log_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `p_picture_download_log_info_ibfk_2` FOREIGN KEY (`picture_id`) REFERENCES `p_picture_info` (`picture_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `p_picture_download_log_info_ibfk_3` FOREIGN KEY (`space_id`) REFERENCES `p_space_info` (`space_id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '图片下载记录表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for p_picture_info
-- ----------------------------
DROP TABLE IF EXISTS `p_picture_info`;
CREATE TABLE `p_picture_info`  (
  `picture_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片编号',
  `picture_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片URL',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片名称',
  `introduction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '简介',
  `category_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类编号',
  `pic_size` bigint NULL DEFAULT NULL COMMENT '图片体积（字节）',
  `pic_width` int NULL DEFAULT 0 COMMENT '图片宽度',
  `pic_height` int NULL DEFAULT 0 COMMENT '图片高度',
  `pic_scale` double NULL DEFAULT 0 COMMENT '宽高比例',
  `pic_format` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片格式',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '上传用户编号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `publish_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `picture_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片状态（0公共 1私有）',
  `thumbnail_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '缩略图URL',
  `look_count` bigint NOT NULL DEFAULT 0 COMMENT '查看次数',
  `collect_count` bigint NOT NULL DEFAULT 0 COMMENT '收藏次数',
  `like_count` bigint NOT NULL DEFAULT 0 COMMENT '点赞次数',
  `share_count` bigint NOT NULL DEFAULT 0 COMMENT '分享次数',
  `download_count` bigint NOT NULL DEFAULT 0 COMMENT '下载次数',
  `space_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属空间编号',
  `folder_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属文件夹编号',
  `upload_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '上传类型',
  `more_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '更多信息',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除（0否 1是）',
  `deleted_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`picture_id`) USING BTREE,
  INDEX `category_id`(`category_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_space_folder`(`space_id` ASC, `folder_id` ASC) USING BTREE,
  INDEX `idx_picture_status`(`picture_status` ASC) USING BTREE,
  INDEX `p_picture_info_download_count_index`(`download_count` DESC) USING BTREE,
  INDEX `p_picture_info_look_count_index`(`look_count` ASC) USING BTREE,
  INDEX `p_picture_info_collect_count_index`(`collect_count` ASC) USING BTREE,
  INDEX `p_picture_info_like_count_index`(`like_count` ASC) USING BTREE,
  INDEX `p_picture_info_share_count_index`(`share_count` ASC) USING BTREE,
  CONSTRAINT `p_picture_info_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `p_picture_category_info` (`category_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `p_picture_info_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `p_picture_info_ibfk_3` FOREIGN KEY (`space_id`) REFERENCES `p_space_info` (`space_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '图片详细信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for p_picture_recommend_info
-- ----------------------------
DROP TABLE IF EXISTS `p_picture_recommend_info`;
CREATE TABLE `p_picture_recommend_info`  (
  `recommend_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '推荐编号',
  `category_scores` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '分类分数',
  `top_categories` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '高兴趣分类',
  `normalized_category_scores` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '归一化分类分数',
  `tag_scores` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '标签分数',
  `top_tags` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '高兴趣标签',
  `normalized_tag_scores` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '归一化标签分数',
  `more_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '更多信息',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目标对象',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`recommend_id`) USING BTREE,
  INDEX `idx_picture_recommend_user`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_picture_recommend_user` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户图片推荐模型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for p_picture_tag_info
-- ----------------------------
DROP TABLE IF EXISTS `p_picture_tag_info`;
CREATE TABLE `p_picture_tag_info`  (
  `tag_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签编号',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名称',
  `tags_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '标签状态',
  `tag_desc` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签描述',
  `usage_count` bigint NOT NULL DEFAULT 0 COMMENT '使用次数',
  `look_count` bigint NOT NULL DEFAULT 0 COMMENT '查看次数',
  `download_count` bigint NOT NULL DEFAULT 0 COMMENT '下载次数',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`tag_id`) USING BTREE,
  UNIQUE INDEX `uk_tag_name`(`name` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_usage_count`(`usage_count` ASC) USING BTREE,
  INDEX `idx_look_count`(`look_count` ASC) USING BTREE,
  INDEX `idx_download_count`(`download_count` ASC) USING BTREE,
  CONSTRAINT `p_picture_tag_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '图片标签信息表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for p_search_log_info
-- ----------------------------
DROP TABLE IF EXISTS `p_search_log_info`;
CREATE TABLE `p_search_log_info`  (
  `search_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '搜索记录编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户编号',
  `keyword` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '搜索关键词',
  `search_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '搜索类型（0图片 1空间 2用户）',
  `refer_source` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '搜索来源（0首页 1推荐 2搜索页 3AI推荐 4历史搜索）',
  `search_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '搜索状态（0成功 1失败）',
  `fail_reason` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '失败原因',
  `result_count` int NOT NULL DEFAULT 0 COMMENT '返回数量',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '搜索时间',
  `search_duration` int NULL DEFAULT 0 COMMENT '搜索时长（毫秒）',
  `device_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '平台',
  `ip_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP属地',
  `ip_addr` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP地址',
  PRIMARY KEY (`search_id`) USING BTREE,
  INDEX `idx_search_user`(`user_id` ASC) USING BTREE,
  INDEX `idx_search_type`(`search_type` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  CONSTRAINT `fk_search_log_user` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户搜索记录表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for p_space_dilatation_info
-- ----------------------------
DROP TABLE IF EXISTS `p_space_dilatation_info`;
CREATE TABLE `p_space_dilatation_info`  (
  `dilatation_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '申请编号',
  `dilatation_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '扩容KEY',
  `space_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '空间编号',
  `space_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '空间名称',
  `thumbnail_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '缩略图 URL',
  `dilatation_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '扩容类型',
  `dilatation_unit` int NOT NULL COMMENT '扩容单价',
  `dilatation_total` int NOT NULL COMMENT '扩容总数',
  `points_total` int NULL DEFAULT 0 COMMENT '消耗积分',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`dilatation_id`) USING BTREE,
  INDEX `fk_space_dilatation_space_id`(`space_id` ASC) USING BTREE,
  INDEX `fk_space_dilatation_user_id_dilatation`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_space_dilatation_space_id` FOREIGN KEY (`space_id`) REFERENCES `p_space_info` (`space_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_space_dilatation_user_id_dilatation` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '空间扩容信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of p_space_dilatation_info
-- ----------------------------

-- ----------------------------
-- Table structure for p_space_folder_info
-- ----------------------------
DROP TABLE IF EXISTS `p_space_folder_info`;
CREATE TABLE `p_space_folder_info`  (
  `folder_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件夹编号',
  `space_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '空间编号',
  `parent_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '父文件夹编号',
  `ancestors` varchar(1280) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '祖级列表',
  `folder_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件夹名称',
  `full_path` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '完整路径（格式：/文件夹名1/文件夹名2/）',
  `folder_level` tinyint UNSIGNED NOT NULL DEFAULT 1 COMMENT '层级',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `sort_order` tinyint NOT NULL DEFAULT 0 COMMENT '排序权重',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`folder_id`) USING BTREE,
  UNIQUE INDEX `uk_folder_unique`(`space_id` ASC, `parent_id` ASC, `folder_name` ASC) USING BTREE,
  INDEX `parent_id`(`parent_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_full_path`(`full_path`(200) ASC) USING BTREE,
  INDEX `idx_ancestors`(`ancestors`(200) ASC) USING BTREE,
  CONSTRAINT `p_space_folder_info_ibfk_1` FOREIGN KEY (`space_id`) REFERENCES `p_space_info` (`space_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `p_space_folder_info_ibfk_3` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '空间文件夹表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for p_space_info
-- ----------------------------
DROP TABLE IF EXISTS `p_space_info`;
CREATE TABLE `p_space_info`  (
  `space_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '空间编号',
  `space_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '空间名称',
  `space_avatar` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '空间封面URL',
  `max_size` bigint NULL DEFAULT 1073741824 COMMENT '最大容量（字节）',
  `max_count` bigint NULL DEFAULT 1000 COMMENT '最大文件数',
  `total_size` bigint NULL DEFAULT 0 COMMENT '已用容量（字节）',
  `total_count` bigint NULL DEFAULT 0 COMMENT '文件总数',
  `look_count` bigint NOT NULL DEFAULT 0 COMMENT '查看次数',
  `collect_count` bigint NOT NULL DEFAULT 0 COMMENT '收藏次数',
  `download_count` bigint NOT NULL DEFAULT 0 COMMENT '下载次数',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属用户',
  `space_desc` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '空间描述',
  `space_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '空间状态',
  `space_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '空间类型（0个人 1团队 2官方）',
  `member_limit` int NULL DEFAULT 10 COMMENT '成员上限',
  `current_members` int NULL DEFAULT 0 COMMENT '当前成员数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后上传时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除（0否 1是）',
  `deleted_time` datetime NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`space_id`) USING BTREE,
  UNIQUE INDEX `uk_space_name`(`space_name` ASC, `user_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_space_type`(`space_type` ASC) USING BTREE,
  INDEX `idx_is_deleted`(`is_delete` ASC) USING BTREE,
  INDEX `idx_space_status`(`space_status` ASC) USING BTREE,
  CONSTRAINT `p_space_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '空间信息表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for p_space_invitation_info
-- ----------------------------
DROP TABLE IF EXISTS `p_space_invitation_info`;
CREATE TABLE `p_space_invitation_info`  (
  `invitation_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邀请编号',
  `space_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '空间编号',
  `space_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '空间名称',
  `space_avatar` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '空间封面URL',
  `role_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邀请角色（0创建者 1管理员 2编辑者 3浏览者）',
  `invitation_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '邀请状态（0待同意 1同意 2拒绝 3过期）',
  `invitation_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邀请链接（短链或唯一标识）',
  `invitation` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '邀请理由',
  `invitation_user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邀请人编号',
  `expire_time` datetime NOT NULL COMMENT '过期时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '被邀请用户编号',
  PRIMARY KEY (`invitation_id`) USING BTREE,
  UNIQUE INDEX `uk_invitation_url`(`invitation_url` ASC) USING BTREE,
  INDEX `space_id`(`space_id` ASC) USING BTREE,
  INDEX `invitation_user_id`(`invitation_user_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_invitation_status`(`invitation_status` ASC) USING BTREE,
  INDEX `idx_expire_time`(`expire_time` ASC) USING BTREE,
  CONSTRAINT `p_space_invitation_info_ibfk_1` FOREIGN KEY (`space_id`) REFERENCES `p_space_info` (`space_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `p_space_invitation_info_ibfk_2` FOREIGN KEY (`invitation_user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `p_space_invitation_info_ibfk_3` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '空间成员邀请记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for p_space_member_info
-- ----------------------------
DROP TABLE IF EXISTS `p_space_member_info`;
CREATE TABLE `p_space_member_info`  (
  `member_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '成员编号',
  `space_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '空间编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `role_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色（0创建者 1管理员 2编辑者 3浏览者）',
  `last_active_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后操作时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `inviter_user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邀请人编号',
  `join_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '加入方式（0邀请）',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`member_id`) USING BTREE,
  INDEX `space_id`(`space_id` ASC) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `inviter_user_id`(`inviter_user_id` ASC) USING BTREE,
  INDEX `idx_role_type`(`role_type` ASC) USING BTREE,
  INDEX `idx_join_type`(`join_type` ASC) USING BTREE,
  CONSTRAINT `p_space_member_info_ibfk_1` FOREIGN KEY (`space_id`) REFERENCES `p_space_info` (`space_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `p_space_member_info_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `p_space_member_info_ibfk_3` FOREIGN KEY (`inviter_user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '空间成员信息表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for p_statistics_info
-- ----------------------------
DROP TABLE IF EXISTS `p_statistics_info`;
CREATE TABLE `p_statistics_info`  (
  `statistics_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '统计编号',
  `type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '统计类型',
  `statistics_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '统计名称',
  `common_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '公共KEY',
  `statistics_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'KEY',
  `stages` int NULL DEFAULT NULL COMMENT '期数',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '统计内容',
  `extend_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '统计内容',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`statistics_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '统计信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for p_user_behavior_info
-- ----------------------------
DROP TABLE IF EXISTS `p_user_behavior_info`;
CREATE TABLE `p_user_behavior_info`  (
  `behavior_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '转发编号',
  `behavior_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '行为类型',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `target_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目标类型',
  `target_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目标对象',
  `target_content` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目标内容',
  `score` decimal(5, 2) NOT NULL COMMENT '分数',
  `share_link` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分享链接',
  `category_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片分类',
  `space_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '空间',
  `tags` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片标签',
  `target_cover` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '转发时间',
  `has_statistics` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '是否统计（0否 1是）',
  `device_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '平台',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `ip_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP属地',
  PRIMARY KEY (`behavior_id`) USING BTREE,
  INDEX `p_user_behavior_info_user_id_target_type_create_time_index`(`user_id` ASC, `target_type` ASC, `create_time` DESC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户行为表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for p_user_report_info
-- ----------------------------
DROP TABLE IF EXISTS `p_user_report_info`;
CREATE TABLE `p_user_report_info`  (
  `report_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '举报编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `report_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '举报类型',
  `target_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目标类型（0图片 1用户 2空间）',
  `target_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目标对象编号',
  `target_cover` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面快照（图片URL/用户头像URL/空间封面URL）',
  `target_content` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目标内容',
  `reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '举报原因',
  `contact` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系方式',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '举报时间',
  `review_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '审核状态（0待审核 1通过 2拒绝）',
  `review_message` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审核信息',
  `review_user_id` bigint NULL DEFAULT NULL COMMENT '审核人编号',
  `review_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `device_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '平台',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `ip_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP属地',
  PRIMARY KEY (`report_id`) USING BTREE,
  INDEX `idx_report_user`(`user_id` ASC) USING BTREE,
  INDEX `idx_target`(`target_type` ASC, `target_id` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  CONSTRAINT `fk_report_user` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户举报信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for p_user_view_log_info
-- ----------------------------
DROP TABLE IF EXISTS `p_user_view_log_info`;
CREATE TABLE `p_user_view_log_info`  (
  `view_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '浏览记录编号',
  `user_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户编号',
  `target_type` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '目标类型',
  `target_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '目标对象',
  `target_content` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '目标内容',
  `score` decimal(5, 2) NOT NULL COMMENT '分数',
  `category_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '图片分类',
  `space_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '空间',
  `tags` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '图片标签',
  `target_cover` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '封面',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '查看时间',
  `has_statistics` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0' COMMENT '是否统计（0否 1是）',
  `device_id` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '平台',
  `ip_addr` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `ip_address` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'IP属地',
  PRIMARY KEY (`view_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户浏览记录表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for po_account_info
-- ----------------------------
DROP TABLE IF EXISTS `po_account_info`;
CREATE TABLE `po_account_info`  (
  `account_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账户编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `password` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付密码',
  `salt` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '加密方式',
  `points_earned` bigint NOT NULL DEFAULT 0 COMMENT '赚取总积分',
  `points_used` bigint NOT NULL DEFAULT 0 COMMENT '使用总积分',
  `recharge_amount` decimal(18, 2) NOT NULL DEFAULT 0.00 COMMENT '充值总金额（元）',
  `account_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '账户状态（0正常 1异常 2禁用）',
  `points_balance` bigint NOT NULL DEFAULT 0 COMMENT '积分余额',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除（0否 1是）',
  PRIMARY KEY (`account_id`) USING BTREE,
  UNIQUE INDEX `uk_po_account_user`(`user_id` ASC) USING BTREE,
  INDEX `idx_po_account_status`(`account_status` ASC) USING BTREE,
  CONSTRAINT `fk_po_account_user` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '积分账户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for po_error_log_info
-- ----------------------------
DROP TABLE IF EXISTS `po_error_log_info`;
CREATE TABLE `po_error_log_info`  (
  `error_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '异常编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户编号',
  `order_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单类型',
  `method_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付方式',
  `third_party` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '第三方支付平台',
  `third_party_order` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '第三方支付平台订单号',
  `error_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '异常类型',
  `error_code` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '返回Code',
  `error_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '返回Msg',
  `payment_extend` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '额外信息',
  `related_order_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '相关订单编号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '异常记录时间',
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '平台',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'IP地址',
  `ip_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP属地',
  `resolve_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '解决状态',
  `resolve_time` datetime NULL DEFAULT NULL COMMENT '解决时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`error_id`) USING BTREE,
  INDEX `fk_po_error_user`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_po_error_user` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '异常捕获日志表' ROW_FORMAT = Dynamic;

--------
-- Table structure for po_payment_method_info
-- ----------------------------
DROP TABLE IF EXISTS `po_payment_method_info`;
CREATE TABLE `po_payment_method_info`  (
  `method_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付方式编号',
  `method_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `third_party` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '第三方支付平台',
  `method_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类型',
  `api_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付接口URL',
  `merchant_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商户号',
  `app_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '应用编号',
  `secret_key` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '秘钥',
  `contact_information` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系方式',
  `extend_config` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '扩展配置',
  `method_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '状态（0使用 1未使用）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`method_id`) USING BTREE,
  INDEX `idx_method_type`(`method_type` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付方式表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of po_payment_method_info
-- ----------------------------

-- ----------------------------
-- Table structure for po_payment_order_info
-- ----------------------------
DROP TABLE IF EXISTS `po_payment_order_info`;
CREATE TABLE `po_payment_order_info`  (
  `order_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `order_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单类型',
  `order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '订单状态',
  `payment_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付方式',
  `total_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '订单总金额',
  `buyer_pay_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '实付金额',
  `receipt_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '实收金额',
  `discount_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '平台优惠金额',
  `third_party` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '第三方支付平台',
  `third_user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '第三方用户编号',
  `third_party_order` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '第三方支付平台订单号',
  `payment_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `payment_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '支付状态',
  `payment_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付返回Code',
  `payment_msg` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付返回Msg',
  `payment_extend` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '支付返回额外信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '平台',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'IP地址',
  `ip_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP属地',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`order_id`) USING BTREE,
  INDEX `fk_payment_order_user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_payment_order_user_id` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for po_points_recharge_info
-- ----------------------------
DROP TABLE IF EXISTS `po_points_recharge_info`;
CREATE TABLE `po_points_recharge_info`  (
  `recharge_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '充值记录编号',
  `package_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '套餐编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `order_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `total_count` int NOT NULL COMMENT '总数',
  `points_count` int NOT NULL DEFAULT 0 COMMENT '充值积分数量',
  `bonus_count` int NULL DEFAULT 0 COMMENT '赠送数量',
  `price_count` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '充值金额',
  `buyer_pay_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '实付金额',
  `recharge_count` int NOT NULL DEFAULT 0 COMMENT '数量',
  `payment_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付方式',
  `third_party` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '第三方支付平台',
  `third_party_order` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '第三方支付平台订单号',
  `recharge_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '充值状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '充值时间',
  `arrival_time` datetime NULL DEFAULT NULL COMMENT '到账时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '平台',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'IP地址',
  `ip_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP属地',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除',
  PRIMARY KEY (`recharge_id`) USING BTREE,
  INDEX `fk_points_recharge_user_id`(`user_id` ASC) USING BTREE,
  INDEX `fk_points_recharge_order_id`(`order_id` ASC) USING BTREE,
  INDEX `fk_points_recharge_package_id`(`package_id` ASC) USING BTREE,
  CONSTRAINT `fk_points_recharge_order_id` FOREIGN KEY (`order_id`) REFERENCES `po_payment_order_info` (`order_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_points_recharge_package_id` FOREIGN KEY (`package_id`) REFERENCES `po_points_recharge_package_info` (`package_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_points_recharge_user_id` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '充值记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for po_points_recharge_package_info
-- ----------------------------
DROP TABLE IF EXISTS `po_points_recharge_package_info`;
CREATE TABLE `po_points_recharge_package_info`  (
  `package_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '套餐编号',
  `package_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '套餐名称',
  `price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '套餐价格',
  `points` int NOT NULL DEFAULT 0 COMMENT '套餐积分数量',
  `points_bonus` int NULL DEFAULT 0 COMMENT '套餐赠送积分',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '套餐描述',
  `is_long_term` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '是否长期（0是 1否）',
  `sort_order` tinyint NOT NULL COMMENT '排序权重',
  `start_time` datetime NULL DEFAULT NULL COMMENT '套餐生效时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '套餐结束时间',
  `package_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '套餐状态（0正常 1失效）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`package_id`) USING BTREE,
  UNIQUE INDEX `uk_package_name`(`package_name` ASC) USING BTREE,
  INDEX `idx_status`(`package_status` ASC) USING BTREE,
  INDEX `idx_time_range`(`start_time` ASC, `end_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '充值积分套餐表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of po_points_recharge_package_info
-- ----------------------------
INSERT INTO `po_points_recharge_package_info` VALUES ('1921957895054954496', '新开业充值套餐', 100.00, 10000, 10000, '充值一百赠送一百', '1', 10, '2025-05-13 00:00:00', '2025-05-29 00:00:00', '2', '2025-05-12 23:57:25', '2025-07-30 17:22:24', '挺好的');
INSERT INTO `po_points_recharge_package_info` VALUES ('1922130564807266304', '基础100元充值套餐', 100.00, 10000, 1000, '基础一百元充值套餐', '0', 3, NULL, NULL, '1', '2025-05-13 11:23:33', '2025-07-30 17:22:07', NULL);
INSERT INTO `po_points_recharge_package_info` VALUES ('1922131084775133184', '基础1元充值套餐', 1.00, 100, 0, '基础一元充值套餐', '0', 0, NULL, NULL, '1', '2025-05-13 11:25:37', NULL, NULL);
INSERT INTO `po_points_recharge_package_info` VALUES ('1922134745060347904', '基础10元充值套餐', 10.00, 1000, 100, '基础十元充值套餐', '0', 2, NULL, NULL, '1', '2025-05-13 11:40:09', '2025-07-30 17:21:57', NULL);
INSERT INTO `po_points_recharge_package_info` VALUES ('1922134902128644096', '基础1000元充值套餐', 1000.00, 100000, 10000, '基础一千元充值套餐', '0', 5, NULL, NULL, '1', '2025-05-13 11:40:47', '2025-07-30 17:23:20', NULL);
INSERT INTO `po_points_recharge_package_info` VALUES ('1922135143502450688', '活动6元充值套餐', 6.00, 600, 100, '活动推荐6元充值套餐，赠送100积分', '1', 10, '2025-05-13 00:00:00', '2025-05-22 00:00:00', '2', '2025-05-13 11:41:44', '2025-07-30 17:22:33', NULL);
INSERT INTO `po_points_recharge_package_info` VALUES ('1950486925496422400', '基础6元充值套餐', 6.00, 600, 60, NULL, '0', 1, NULL, NULL, '1', '2025-07-30 17:21:36', '2025-07-30 17:21:51', NULL);

-- ----------------------------
-- Table structure for po_points_usage_log_info
-- ----------------------------
DROP TABLE IF EXISTS `po_points_usage_log_info`;
CREATE TABLE `po_points_usage_log_info`  (
  `log_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '记录编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `give_user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '给予用户编号',
  `log_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '日志类型（0充值 1消费 2提成 3提现）',
  `usage_type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '使用类型（0下载图片 1AI服务）',
  `target_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目标编号',
  `points_before` int NOT NULL DEFAULT 0 COMMENT '使用前积分',
  `points_used` int NOT NULL DEFAULT 0 COMMENT '消费积分',
  `points_after` int NOT NULL DEFAULT 0 COMMENT '使用后积分',
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '平台',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'IP地址',
  `ip_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP属地',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除（0否 1是）',
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `idx_points_usage_usage_type`(`usage_type` ASC) USING BTREE,
  INDEX `idx_points_usage_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_points_usage_give_user_id`(`give_user_id` ASC) USING BTREE,
  INDEX `idx_target`(`target_id` ASC) USING BTREE,
  CONSTRAINT `fk_give_user_id` FOREIGN KEY (`give_user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `po_points_usage_log_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '积分使用记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for po_risk_control_log_info
-- ----------------------------
DROP TABLE IF EXISTS `po_risk_control_log_info`;
CREATE TABLE `po_risk_control_log_info`  (
  `log_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '风控日志编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `method_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付方式',
  `risk_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '风险类型',
  `risk_level` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '风险等级（0低 1中 2高）',
  `risk_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '风险描述',
  `action_taken` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '采取措施',
  `action_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '措施时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '平台（Web/APP）',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'IP地址',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_risk_level`(`risk_level` ASC) USING BTREE,
  INDEX `idx_risk_type`(`risk_type` ASC) USING BTREE,
  INDEX `idx_action_time`(`action_time` ASC) USING BTREE,
  CONSTRAINT `po_risk_control_log_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '风控日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of po_risk_control_log_info
-- ----------------------------

-- ----------------------------
-- Table structure for po_withdrawal_order_info
-- ----------------------------
DROP TABLE IF EXISTS `po_withdrawal_order_info`;
CREATE TABLE `po_withdrawal_order_info`  (
  `withdrawal_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '提现订单编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户编号',
  `points_withdrawal` int NOT NULL DEFAULT 0 COMMENT '提现积分',
  `amount_withdrawal` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '提现金额',
  `platform_fee` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '平台抽成金额',
  `user_received_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '用户实际收到金额',
  `withdrawal_method` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '提现方式（0支付宝 1微信）',
  `withdrawal_account` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '提现账户',
  `withdrawal_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '提现状态（0待处理 1完成 2失败 3超时）',
  `withdrawal_platform_order` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '提现平台订单号',
  `transaction_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '交易编号',
  `review_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '审核状态（0待审核 1同意 2拒绝）',
  `review_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `review_user_id` bigint NULL DEFAULT NULL COMMENT '审核人编号',
  `review_remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审核建议',
  `accomplish_time` datetime NULL DEFAULT NULL COMMENT '完成时间',
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '平台',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'IP地址',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `fail_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '提现失败原因',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除（0否 1是）',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`withdrawal_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_withdrawal_status`(`withdrawal_status` ASC) USING BTREE,
  INDEX `idx_review_status`(`review_status` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  CONSTRAINT `po_withdrawal_order_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户提现记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of po_withdrawal_order_info
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `blob_data` blob NULL COMMENT '存放持久化Trigger对象',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'Blob类型的触发器表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `calendar_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '日历名称',
  `calendar` blob NOT NULL COMMENT '存放持久化calendar对象',
  PRIMARY KEY (`sched_name`, `calendar_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '日历信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `cron_expression` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'cron表达式',
  `time_zone_id` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '时区',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'Cron类型的触发器表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `entry_id` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度器实例id',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `instance_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度器实例名',
  `fired_time` bigint NOT NULL COMMENT '触发的时间',
  `sched_time` bigint NOT NULL COMMENT '定时器制定的时间',
  `priority` int NOT NULL COMMENT '优先级',
  `state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态',
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务组名',
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否并发',
  `requests_recovery` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '是否接受恢复执行',
  PRIMARY KEY (`sched_name`, `entry_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '已触发的触发器表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务组名',
  `description` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '相关介绍',
  `job_class_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '执行任务类名称',
  `is_durable` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '是否持久化',
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '是否并发',
  `is_update_data` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '是否更新数据',
  `requests_recovery` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '是否接受恢复执行',
  `job_data` blob NULL COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务详细信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `lock_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '悲观锁名称',
  PRIMARY KEY (`sched_name`, `lock_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '存储的悲观锁信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  PRIMARY KEY (`sched_name`, `trigger_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '暂停的触发器表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `instance_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '实例名称',
  `last_checkin_time` bigint NOT NULL COMMENT '上次检查时间',
  `checkin_interval` bigint NOT NULL COMMENT '检查间隔时间',
  PRIMARY KEY (`sched_name`, `instance_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '调度器状态表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `repeat_count` bigint NOT NULL COMMENT '重复的次数统计',
  `repeat_interval` bigint NOT NULL COMMENT '重复的间隔时间',
  `times_triggered` bigint NOT NULL COMMENT '已经触发的次数',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '简单触发器的信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `str_prop_1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'String类型的trigger的第一个参数',
  `str_prop_2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'String类型的trigger的第二个参数',
  `str_prop_3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'String类型的trigger的第三个参数',
  `int_prop_1` int NULL DEFAULT NULL COMMENT 'int类型的trigger的第一个参数',
  `int_prop_2` int NULL DEFAULT NULL COMMENT 'int类型的trigger的第二个参数',
  `long_prop_1` bigint NULL DEFAULT NULL COMMENT 'long类型的trigger的第一个参数',
  `long_prop_2` bigint NULL DEFAULT NULL COMMENT 'long类型的trigger的第二个参数',
  `dec_prop_1` decimal(13, 4) NULL DEFAULT NULL COMMENT 'decimal类型的trigger的第一个参数',
  `dec_prop_2` decimal(13, 4) NULL DEFAULT NULL COMMENT 'decimal类型的trigger的第二个参数',
  `bool_prop_1` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Boolean类型的trigger的第一个参数',
  `bool_prop_2` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Boolean类型的trigger的第二个参数',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '同步机制的行锁表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '触发器的名字',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '触发器所属组的名字',
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_job_details表job_name的外键',
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_job_details表job_group的外键',
  `description` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '相关介绍',
  `next_fire_time` bigint NULL DEFAULT NULL COMMENT '上一次触发时间（毫秒）',
  `prev_fire_time` bigint NULL DEFAULT NULL COMMENT '下一次触发时间（默认为-1表示不触发）',
  `priority` int NULL DEFAULT NULL COMMENT '优先级',
  `trigger_state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '触发器状态',
  `trigger_type` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '触发器的类型',
  `start_time` bigint NOT NULL COMMENT '开始时间',
  `end_time` bigint NULL DEFAULT NULL COMMENT '结束时间',
  `calendar_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '日程表名称',
  `misfire_instr` smallint NULL DEFAULT NULL COMMENT '补偿执行的策略',
  `job_data` blob NULL COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  INDEX `sched_name`(`sched_name` ASC, `job_name` ASC, `job_group` ASC) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '触发器详细信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 109 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '参数配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2025-02-28 14:53:19', '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2025-02-28 14:53:19', '', NULL, '初始化密码 123456');
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2025-02-28 14:53:19', '', NULL, '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `sys_config` VALUES (4, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'false', 'Y', 'admin', '2025-02-28 14:53:19', 'admin', '2025-03-14 17:24:50', '是否开启验证码功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', '2025-02-28 14:53:19', '', NULL, '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (6, '用户登录-黑名单列表', 'sys.login.blackIPList', '', 'Y', 'admin', '2025-02-28 14:53:19', '', NULL, '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');
INSERT INTO `sys_config` VALUES (100, '图片展示压缩率', 'picture:p', '60', 'Y', 'admin', '2025-04-10 15:44:30', 'admin', '2025-04-10 17:35:34', NULL);
INSERT INTO `sys_config` VALUES (101, '图片文字水印', 'picture:watermark:text', 'springsun.online', 'Y', 'admin', '2025-04-10 17:09:05', 'admin', '2025-04-10 17:12:05', NULL);
INSERT INTO `sys_config` VALUES (102, '图片水印文字平台透明度', 'picture:watermark:text:pp', '100', 'Y', 'admin', '2025-04-10 17:10:10', 'admin', '2025-07-31 19:52:56', NULL);
INSERT INTO `sys_config` VALUES (103, '图片水印文字用户透明度', 'picture:watermark:text:pu', '40', 'Y', 'admin', '2025-04-10 17:10:44', 'admin', '2025-07-29 23:50:02', NULL);
INSERT INTO `sys_config` VALUES (104, '图片文字水印平台大小', 'picture:watermark:text:ps', '60', 'Y', 'admin', '2025-04-10 17:17:11', 'admin', '2025-07-31 20:06:01', '图片款/参数键值');
INSERT INTO `sys_config` VALUES (105, '图片文字水印平台大小', 'picture:watermark:text:us', '50', 'Y', 'admin', '2025-04-10 17:17:48', 'admin', '2025-04-10 17:18:31', '图片高/参数键值');
INSERT INTO `sys_config` VALUES (106, '图片AI文字水印', 'picture:watermark:text:ai', 'LZAI', 'Y', 'admin', '2025-08-11 00:02:21', '', NULL, NULL);
INSERT INTO `sys_config` VALUES (107, '图片AI文字透明度', 'picture:watermark:text:ai:p', '10', 'Y', 'admin', '2025-08-11 00:03:06', '', NULL, NULL);
INSERT INTO `sys_config` VALUES (108, '图片AI文字大小', 'picture:watermark:text:ai:s', '60', 'Y', 'admin', '2025-08-11 00:04:09', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 200 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '0', '若依科技', 0, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-02-28 14:53:16', '', NULL);
INSERT INTO `sys_dept` VALUES (101, 100, '0,100', '深圳总公司', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-02-28 14:53:16', '', NULL);
INSERT INTO `sys_dept` VALUES (102, 100, '0,100', '长沙分公司', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-02-28 14:53:16', '', NULL);
INSERT INTO `sys_dept` VALUES (103, 101, '0,100,101', '研发部门', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-02-28 14:53:16', '', NULL);
INSERT INTO `sys_dept` VALUES (104, 101, '0,100,101', '市场部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-02-28 14:53:16', '', NULL);
INSERT INTO `sys_dept` VALUES (105, 101, '0,100,101', '测试部门', 3, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-02-28 14:53:16', '', NULL);
INSERT INTO `sys_dept` VALUES (106, 101, '0,100,101', '财务部门', 4, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-02-28 14:53:16', '', NULL);
INSERT INTO `sys_dept` VALUES (107, 101, '0,100,101', '运维部门', 5, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-02-28 14:53:16', '', NULL);
INSERT INTO `sys_dept` VALUES (108, 102, '0,100,102', '市场部门', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-02-28 14:53:16', '', NULL);
INSERT INTO `sys_dept` VALUES (109, 102, '0,100,102', '财务部门', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', '2025-02-28 14:53:16', '', NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 380 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '默认分组');
INSERT INTO `sys_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '系统分组');
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '通知');
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '公告');
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '关闭状态');
INSERT INTO `sys_dict_data` VALUES (18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '其他操作');
INSERT INTO `sys_dict_data` VALUES (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '新增操作');
INSERT INTO `sys_dict_data` VALUES (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '修改操作');
INSERT INTO `sys_dict_data` VALUES (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '删除操作');
INSERT INTO `sys_dict_data` VALUES (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '授权操作');
INSERT INTO `sys_dict_data` VALUES (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '导出操作');
INSERT INTO `sys_dict_data` VALUES (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '导入操作');
INSERT INTO `sys_dict_data` VALUES (25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '强退操作');
INSERT INTO `sys_dict_data` VALUES (26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '生成操作');
INSERT INTO `sys_dict_data` VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '清空操作');
INSERT INTO `sys_dict_data` VALUES (28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (100, 0, '值', '1', 'c_config_type', NULL, 'default', 'N', '0', 'admin', '2024-12-20 22:07:07', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (101, 0, '文件', '2', 'c_config_type', NULL, 'default', 'N', '0', 'admin', '2024-12-20 22:07:16', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (117, 0, '正常', '0', 'c_locale_status', NULL, 'default', 'N', '0', 'admin', '2024-12-20 22:07:07', '', NULL, '正常');
INSERT INTO `sys_dict_data` VALUES (118, 1, '隐藏', '1', 'c_locale_status', NULL, 'default', 'N', '0', 'admin', '2024-12-20 22:07:07', '', NULL, '隐藏');
INSERT INTO `sys_dict_data` VALUES (122, 0, '正常', '0', 'c_permission_status', NULL, 'default', 'N', '0', 'admin', '2024-12-20 22:07:07', '', NULL, '正常');
INSERT INTO `sys_dict_data` VALUES (123, 1, '隐藏', '1', 'c_permission_status', NULL, 'default', 'N', '0', 'admin', '2024-12-20 22:07:07', '', NULL, '关闭');
INSERT INTO `sys_dict_data` VALUES (124, 0, '已启用', '0', 'c_template_status', NULL, 'success', 'N', '0', 'admin', '2025-03-14 17:32:32', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (125, 1, '已禁用', '1', 'c_template_status', NULL, 'warning', 'N', '0', 'admin', '2025-03-14 17:32:44', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (126, 1, '短信', '1', 'c_template_type', NULL, 'default', 'N', '0', 'admin', '2025-03-14 17:33:26', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (127, 2, '邮件', '2', 'c_template_type', NULL, 'default', 'N', '0', 'admin', '2025-03-14 17:33:26', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (128, 3, '站内通知', '3', 'c_template_type', NULL, 'default', 'N', '0', 'admin', '2025-03-14 17:33:26', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (129, 4, 'APP推送', '5', 'c_template_type', NULL, 'default', 'N', '0', 'admin', '2025-03-14 17:33:26', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (130, 5, '微信', '6', 'c_template_type', NULL, 'default', 'N', '0', 'admin', '2025-03-14 17:33:26', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (131, 99, '其他', '0', 'c_template_type', NULL, 'default', 'N', '0', 'admin', '2025-03-14 17:33:26', 'admin', '2025-07-26 19:22:41', NULL);
INSERT INTO `sys_dict_data` VALUES (132, 0, '阿里云', '阿里云', 'c_template_channel', NULL, 'default', 'N', '0', 'admin', '2025-03-14 17:38:19', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (133, 1, '腾讯云', '腾讯云', 'c_template_channel', NULL, 'default', 'N', '0', 'admin', '2025-03-14 17:38:28', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (134, 0, '正常', '0', 'common_delete', NULL, 'primary', 'N', '0', 'admin', '2025-03-17 12:57:26', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (135, 1, '删除', '1', 'common_delete', NULL, 'warning', 'N', '0', 'admin', '2025-03-17 12:57:34', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (136, 0, '正常', '0', 'u_user_status', NULL, 'primary', 'N', '0', 'admin', '2025-03-17 12:59:17', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (137, 1, '异常', '1', 'u_user_status', NULL, 'warning', 'N', '0', 'admin', '2025-03-17 12:59:29', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (138, 2, '禁用', '2', 'u_user_status', NULL, 'danger', 'N', '0', 'admin', '2025-03-17 12:59:39', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (139, 0, '未知', '0', 'u_user_sex', NULL, 'default', 'N', '0', 'admin', '2025-03-17 13:00:32', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (140, 1, '男', '1', 'u_user_sex', NULL, 'primary', 'N', '0', 'admin', '2025-03-17 13:00:39', 'admin', '2025-03-17 13:00:44', NULL);
INSERT INTO `sys_dict_data` VALUES (141, 2, '女', '2', 'u_user_sex', NULL, 'success', 'N', '0', 'admin', '2025-03-17 13:00:53', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (142, 0, '封禁中', '0', 'u_banned_permission_status', NULL, 'danger', 'N', '0', 'admin', '2025-03-17 13:01:55', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (143, 1, '结束', '1', 'u_banned_permission_status', NULL, 'warning', 'N', '0', 'admin', '2025-03-17 13:02:04', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (144, 0, '关注', '0', 'u_relation_type', NULL, 'default', 'N', '0', 'admin', '2025-03-17 13:02:51', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (145, 1, '互关', '1', 'u_relation_type', NULL, 'primary', 'N', '0', 'admin', '2025-03-17 13:02:59', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (146, 2, '拉黑', '2', 'u_relation_type', NULL, 'info', 'N', '0', 'admin', '2025-03-17 13:03:10', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (147, 0, '微信', '1', 'u_binding_type', NULL, 'default', 'N', '0', 'admin', '2025-03-17 13:04:30', 'admin', '2025-03-17 13:07:12', NULL);
INSERT INTO `sys_dict_data` VALUES (148, 0, '待发送', '0', 'u_inform_status', NULL, 'default', 'N', '0', 'admin', '2025-03-17 13:05:34', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (149, 1, '已发送', '1', 'u_inform_status', NULL, 'primary', 'N', '0', 'admin', '2025-03-17 13:05:46', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (150, 2, '发送失败', '2', 'u_inform_status', NULL, 'danger', 'N', '0', 'admin', '2025-03-17 13:05:55', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (151, 3, '撤回', '3', 'u_inform_status', NULL, 'warning', 'N', '0', 'admin', '2025-03-17 13:06:22', 'admin', '2025-03-17 13:06:34', NULL);
INSERT INTO `sys_dict_data` VALUES (152, 2, '微信', '2', 'u_login_type', NULL, 'default', 'N', '0', 'admin', '2025-03-17 13:08:09', 'admin', '2025-04-12 00:03:38', NULL);
INSERT INTO `sys_dict_data` VALUES (153, 0, '成功', '0', 'u_login_status', NULL, 'primary', 'N', '0', 'admin', '2025-03-17 13:09:54', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (154, 0, '失败', '1', 'u_login_status', NULL, 'warning', 'N', '0', 'admin', '2025-03-17 13:10:03', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (155, 0, '未读', '0', 'u_inform_is_read', NULL, 'default', 'N', '0', 'admin', '2025-03-17 13:11:55', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (156, 1, '已读', '1', 'u_inform_is_read', NULL, 'primary', 'N', '0', 'admin', '2025-03-17 13:12:04', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (157, 0, '正常', '0', 'po_account_status', NULL, 'success', 'N', '0', 'admin', '2025-03-24 17:11:53', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (158, 1, '异常', '1', 'po_account_status', NULL, 'warning', 'N', '0', 'admin', '2025-03-24 17:12:05', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (159, 2, '禁用', '2', 'po_account_status', NULL, 'danger', 'N', '0', 'admin', '2025-03-24 17:12:15', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (160, 0, '线上', '0', 'po_method_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 17:15:19', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (161, 1, '线下', '1', 'po_method_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 17:15:39', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (162, 0, '使用', '0', 'po_method_status', NULL, 'success', 'N', '0', 'admin', '2025-03-24 17:19:30', 'admin', '2025-03-24 17:19:34', NULL);
INSERT INTO `sys_dict_data` VALUES (163, 1, '未使用', '1', 'po_method_status', NULL, 'warning', 'N', '0', 'admin', '2025-03-24 17:19:42', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (164, 0, '待支付', '0', 'po_order_status', NULL, 'primary', 'N', '0', 'admin', '2025-03-24 17:23:43', 'admin', '2025-05-17 16:03:34', NULL);
INSERT INTO `sys_dict_data` VALUES (165, 1, '支付成功', '1', 'po_order_status', NULL, 'success', 'N', '0', 'admin', '2025-03-24 17:23:43', 'admin', '2025-05-17 16:03:30', NULL);
INSERT INTO `sys_dict_data` VALUES (166, 2, '支付失败', '2', 'po_order_status', NULL, 'danger', 'N', '0', 'admin', '2025-03-24 17:23:43', 'admin', '2025-05-17 16:03:18', NULL);
INSERT INTO `sys_dict_data` VALUES (167, 3, '超时', '3', 'po_order_status', NULL, 'info', 'N', '0', 'admin', '2025-03-24 17:23:43', 'admin', '2025-05-17 16:03:25', NULL);
INSERT INTO `sys_dict_data` VALUES (168, 0, '积分充值', '0', 'po_order_type', NULL, 'primary', 'N', '0', 'admin', '2025-03-24 17:26:26', 'admin', '2025-05-19 23:44:25', NULL);
INSERT INTO `sys_dict_data` VALUES (169, 1, '空间扩容', '1', 'po_order_type', NULL, 'success', 'N', '0', 'admin', '2025-03-24 17:26:34', 'admin', '2025-05-19 23:44:47', NULL);
INSERT INTO `sys_dict_data` VALUES (170, 0, '待支付', '0', 'po_payment_status', NULL, 'default', 'N', '0', 'admin', '2025-03-24 17:29:46', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (171, 1, '支付成功', '1', 'po_payment_status', NULL, 'default', 'N', '0', 'admin', '2025-03-24 17:29:46', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (172, 2, '支付失败', '2', 'po_payment_status', NULL, 'default', 'N', '0', 'admin', '2025-03-24 17:29:46', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (173, 3, '已取消', '3', 'po_payment_status', NULL, 'default', 'N', '0', 'admin', '2025-03-24 17:29:46', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (174, 0, '未开始', '0', 'po_package_status', NULL, 'default', 'N', '0', 'admin', '2025-03-24 17:36:10', 'admin', '2025-03-24 17:38:08', NULL);
INSERT INTO `sys_dict_data` VALUES (175, 1, '正常', '1', 'po_package_status', NULL, 'primary', 'N', '0', 'admin', '2025-03-24 17:36:23', 'admin', '2025-03-24 17:38:01', NULL);
INSERT INTO `sys_dict_data` VALUES (176, 2, '失效', '2', 'po_package_status', NULL, 'warning', 'N', '0', 'admin', '2025-03-24 17:38:17', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (177, 0, '是', '0', 'po_package_is_long_term', NULL, 'success', 'N', '0', 'admin', '2025-03-24 17:40:29', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (178, 1, '否', '1', 'po_package_is_long_term', NULL, 'warning', 'N', '0', 'admin', '2025-03-24 17:40:36', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (179, 0, '待支付', '0', 'po_recharge_status', NULL, 'primary', 'N', '0', 'admin', '2025-03-24 17:29:46', 'admin', '2025-05-17 16:04:41', NULL);
INSERT INTO `sys_dict_data` VALUES (180, 1, '支付成功', '1', 'po_recharge_status', NULL, 'success', 'N', '0', 'admin', '2025-03-24 17:29:46', 'admin', '2025-05-17 16:04:52', NULL);
INSERT INTO `sys_dict_data` VALUES (181, 2, '支付失败', '2', 'po_recharge_status', NULL, 'danger', 'N', '0', 'admin', '2025-03-24 17:29:46', 'admin', '2025-05-17 16:05:02', NULL);
INSERT INTO `sys_dict_data` VALUES (182, 3, '超时', '3', 'po_recharge_status', NULL, 'info', 'N', '0', 'admin', '2025-03-24 17:29:46', 'admin', '2025-05-17 16:05:28', NULL);
INSERT INTO `sys_dict_data` VALUES (183, 0, '低', '0', 'po_risk_level', NULL, 'default', 'N', '0', 'admin', '2025-03-24 17:50:52', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (184, 1, '中', '1', 'po_risk_level', NULL, 'warning', 'N', '0', 'admin', '2025-03-24 17:51:01', 'admin', '2025-03-24 17:51:19', NULL);
INSERT INTO `sys_dict_data` VALUES (185, 2, '高', '2', 'po_risk_level', NULL, 'danger', 'N', '0', 'admin', '2025-03-24 17:51:12', 'admin', '2025-03-24 17:51:15', NULL);
INSERT INTO `sys_dict_data` VALUES (186, 0, '支付宝', '0', 'po_withdrawal_method', NULL, 'default', 'N', '0', 'admin', '2025-03-24 17:55:20', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (187, 1, '微信', '1', 'po_withdrawal_method', NULL, 'default', 'N', '0', 'admin', '2025-03-24 17:55:28', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (188, 0, '待审核', '0', 'po_approval_status', NULL, 'default', 'N', '0', 'admin', '2025-03-24 17:56:13', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (189, 1, '同意', '1', 'po_approval_status', NULL, 'primary', 'N', '0', 'admin', '2025-03-24 17:56:23', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (190, 1, '拒绝', '1', 'po_approval_status', NULL, 'warning', 'N', '0', 'admin', '2025-03-24 17:56:31', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (191, 0, '查看原图', '0', 'po_points_usage_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 17:57:22', 'admin', '2025-07-25 23:03:25', NULL);
INSERT INTO `sys_dict_data` VALUES (192, 0, '开启', '0', 'ai_model_params_status', NULL, 'primary', 'N', '0', 'admin', '2025-03-24 18:01:02', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (193, 1, '关闭', '1', 'ai_model_params_status', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:01:07', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (194, 0, '文生图', '1', 'ai_model_params_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:01:45', 'admin', '2025-08-08 21:27:57', NULL);
INSERT INTO `sys_dict_data` VALUES (195, 0, '请求中', '0', 'ai_log_status', NULL, 'primary', 'N', '0', 'admin', '2025-03-24 18:03:34', 'admin', '2025-08-08 21:32:29', NULL);
INSERT INTO `sys_dict_data` VALUES (196, 1, '成功', '1', 'ai_log_status', NULL, 'success', 'N', '0', 'admin', '2025-03-24 18:03:45', 'admin', '2025-08-08 21:32:38', NULL);
INSERT INTO `sys_dict_data` VALUES (197, 2, '失败', '2', 'ai_log_status', NULL, 'danger', 'N', '0', 'admin', '2025-03-24 18:03:53', 'admin', '2025-08-08 21:32:47', NULL);
INSERT INTO `sys_dict_data` VALUES (198, 0, 'AI搜图', '0', 'ai_log_usage_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:05:04', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (199, 0, '数据分析', '0', 'ai_official_usage_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:06:18', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (200, 0, '成功', '0', 'ai_conversation_status', NULL, 'success', 'N', '0', 'admin', '2025-03-24 18:03:34', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (201, 1, '失败', '1', 'ai_conversation_status', NULL, 'danger', 'N', '0', 'admin', '2025-03-24 18:03:45', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (202, 2, '超时', '2', 'ai_conversation_status', NULL, 'warning', 'N', '0', 'admin', '2025-03-24 18:03:53', 'admin', '2025-03-24 18:03:57', NULL);
INSERT INTO `sys_dict_data` VALUES (203, 0, '文本', '0', 'ai_conversation_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:09:10', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (204, 1, '图片', '1', 'ai_conversation_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:09:16', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (205, 0, '官方', '0', 'p_space_oss_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 19:23:08', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (206, 1, '阿里云', '1', 'p_space_oss_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 19:24:04', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (207, 0, '官方', '0', 'p_space_type', NULL, 'success', 'N', '0', 'admin', '2025-03-24 19:25:44', 'admin', '2025-04-08 11:25:23', NULL);
INSERT INTO `sys_dict_data` VALUES (208, 1, '团队', '1', 'p_space_type', NULL, 'primary', 'N', '0', 'admin', '2025-03-24 19:25:52', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (209, 2, '个人', '2', 'p_space_type', NULL, 'success', 'N', '0', 'admin', '2025-03-24 19:26:02', 'admin', '2025-04-08 11:25:41', NULL);
INSERT INTO `sys_dict_data` VALUES (210, 0, '创建者', '0', 'p_space_role', NULL, 'default', 'N', '0', 'admin', '2025-03-24 19:27:08', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (211, 1, '编辑者', '1', 'p_space_role', NULL, 'default', 'N', '0', 'admin', '2025-03-24 19:27:08', 'admin', '2025-07-01 22:10:16', NULL);
INSERT INTO `sys_dict_data` VALUES (212, 2, '浏览者', '2', 'p_space_role', NULL, 'default', 'N', '0', 'admin', '2025-03-24 19:27:08', 'admin', '2025-07-01 22:10:22', NULL);
INSERT INTO `sys_dict_data` VALUES (214, 0, '待同意', '0', 'p_space_invitation_status', NULL, 'default', 'N', '0', 'admin', '2025-03-24 19:29:33', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (215, 1, '同意', '1', 'p_space_invitation_status', NULL, 'primary', 'N', '0', 'admin', '2025-03-24 19:29:33', 'admin', '2025-03-24 19:30:14', NULL);
INSERT INTO `sys_dict_data` VALUES (216, 2, '拒绝', '2', 'p_space_invitation_status', NULL, 'danger', 'N', '0', 'admin', '2025-03-24 19:29:33', 'admin', '2025-03-24 19:30:19', NULL);
INSERT INTO `sys_dict_data` VALUES (217, 3, '过期', '3', 'p_space_invitation_status', NULL, 'warning', 'N', '0', 'admin', '2025-03-24 19:29:33', 'admin', '2025-03-24 19:30:23', NULL);
INSERT INTO `sys_dict_data` VALUES (219, 0, '是', '0', 'p_category_query_status', NULL, 'primary', 'N', '0', 'admin', '2025-03-24 19:42:59', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (220, 0, '否', '1', 'p_category_query_status', NULL, 'info', 'N', '0', 'admin', '2025-03-24 19:43:05', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (221, 0, '正常', '0', 'p_category_status', NULL, 'primary', 'N', '0', 'admin', '2025-03-24 19:43:15', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (222, 1, '关闭', '1', 'p_category_status', NULL, 'info', 'N', '0', 'admin', '2025-03-24 19:43:25', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (223, 0, '正常', '0', 'p_tag_status', NULL, 'primary', 'N', '0', 'admin', '2025-03-24 19:44:13', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (224, 1, '禁止', '1', 'p_tag_status', NULL, 'warning', 'N', '0', 'admin', '2025-03-24 19:44:21', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (225, 0, '公共', '0', 'p_space_status', NULL, 'primary', 'N', '0', 'admin', '2025-03-24 19:52:43', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (226, 1, '私有', '1', 'p_space_status', NULL, 'info', 'N', '0', 'admin', '2025-03-24 19:52:52', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (227, 0, '公共', '0', 'p_picture_status', NULL, 'primary', 'N', '0', 'admin', '2025-03-24 19:56:20', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (228, 0, '私有', '1', 'p_picture_status', NULL, 'info', 'N', '0', 'admin', '2025-03-24 19:56:27', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (229, 0, '待审核', '0', 'p_picture_review_status', NULL, 'default', 'N', '0', 'admin', '2025-03-24 20:03:18', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (230, 1, '同意', '1', 'p_picture_review_status', NULL, 'default', 'N', '0', 'admin', '2025-03-24 20:03:18', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (231, 2, '拒绝', '2', 'p_picture_review_status', NULL, 'default', 'N', '0', 'admin', '2025-03-24 20:03:18', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (232, 0, '是', '0', 'p_download_is_free', NULL, 'primary', 'N', '0', 'admin', '2025-03-24 20:09:01', 'admin', '2025-03-24 20:09:05', NULL);
INSERT INTO `sys_dict_data` VALUES (233, 1, '否', '1', 'p_download_is_free', NULL, 'info', 'N', '0', 'admin', '2025-03-24 20:09:16', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (234, 0, '查看', '0', 'p_download_type', NULL, 'primary', 'N', '0', 'admin', '2025-03-24 20:20:37', 'admin', '2025-06-06 16:28:16', NULL);
INSERT INTO `sys_dict_data` VALUES (235, 1, '下载', '1', 'p_download_type', NULL, 'success', 'N', '0', 'admin', '2025-03-24 20:20:37', 'admin', '2025-06-06 16:28:05', NULL);
INSERT INTO `sys_dict_data` VALUES (236, 2, '批量下载', '2', 'p_download_type', NULL, 'warning', 'N', '0', 'admin', '2025-03-24 20:20:37', 'admin', '2025-06-06 16:28:09', NULL);
INSERT INTO `sys_dict_data` VALUES (237, 0, '成功', '0', 'p_download_status', NULL, 'success', 'N', '0', 'admin', '2025-03-24 18:03:34', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (238, 1, '失败', '1', 'p_download_status', NULL, 'danger', 'N', '0', 'admin', '2025-03-24 18:03:45', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (239, 0, '其他', '0', 'p_download_refer_source', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:03:45', 'admin', '2025-03-24 20:28:03', NULL);
INSERT INTO `sys_dict_data` VALUES (240, 1, '详情', '1', 'p_download_refer_source', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:03:45', 'admin', '2025-03-24 20:28:07', NULL);
INSERT INTO `sys_dict_data` VALUES (241, 2, '分享', '2', 'p_download_refer_source', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:03:45', 'admin', '2025-03-24 20:28:10', NULL);
INSERT INTO `sys_dict_data` VALUES (242, 0, '图片', '0', 'p_search_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:03:45', 'admin', '2025-03-24 20:28:03', NULL);
INSERT INTO `sys_dict_data` VALUES (243, 1, '空间', '1', 'p_search_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:03:45', 'admin', '2025-03-24 20:28:03', NULL);
INSERT INTO `sys_dict_data` VALUES (244, 2, '用户', '2', 'p_search_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:03:45', 'admin', '2025-03-24 20:28:03', NULL);
INSERT INTO `sys_dict_data` VALUES (245, 0, '成功', '0', 'p_search_status', NULL, 'success', 'N', '0', 'admin', '2025-03-24 18:03:34', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (246, 1, '失败', '1', 'p_search_status', NULL, 'danger', 'N', '0', 'admin', '2025-03-24 18:03:45', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (247, 0, '首页', '0', 'p_search_refer_source', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:03:45', 'admin', '2025-03-24 20:28:03', NULL);
INSERT INTO `sys_dict_data` VALUES (248, 1, '推荐', '1', 'p_search_refer_source', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:03:45', 'admin', '2025-03-24 20:28:03', NULL);
INSERT INTO `sys_dict_data` VALUES (249, 2, '搜索页', '2', 'p_search_refer_source', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:03:45', 'admin', '2025-03-24 20:28:03', NULL);
INSERT INTO `sys_dict_data` VALUES (250, 3, 'AI推荐', '3', 'p_search_refer_source', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:03:45', 'admin', '2025-03-24 20:28:03', NULL);
INSERT INTO `sys_dict_data` VALUES (251, 4, '历史搜索', '4', 'p_search_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:03:45', 'admin', '2025-03-24 20:28:03', NULL);
INSERT INTO `sys_dict_data` VALUES (252, 0, '查看图片', '0', 'p_action_log_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:03:45', 'admin', '2025-03-24 20:28:03', NULL);
INSERT INTO `sys_dict_data` VALUES (253, 1, '搜藏', '1', 'p_action_log_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:03:45', 'admin', '2025-03-24 20:28:03', NULL);
INSERT INTO `sys_dict_data` VALUES (254, 2, '下载', '2', 'p_action_log_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:03:45', 'admin', '2025-03-24 20:28:03', NULL);
INSERT INTO `sys_dict_data` VALUES (255, 3, '关注用户', '3', 'p_action_log_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:03:45', 'admin', '2025-03-24 20:28:03', NULL);
INSERT INTO `sys_dict_data` VALUES (256, 4, '查看空间', '4', 'p_action_log_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:03:45', 'admin', '2025-03-24 20:28:03', NULL);
INSERT INTO `sys_dict_data` VALUES (257, 0, '图片', '0', 'p_action_log_target_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:03:45', 'admin', '2025-03-24 20:28:03', NULL);
INSERT INTO `sys_dict_data` VALUES (258, 1, '空间', '1', 'p_action_log_target_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:03:45', 'admin', '2025-03-24 20:28:03', NULL);
INSERT INTO `sys_dict_data` VALUES (259, 2, '用户', '2', 'p_action_log_target_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:03:45', 'admin', '2025-03-24 20:28:03', NULL);
INSERT INTO `sys_dict_data` VALUES (260, 0, '图片', '0', 'p_view_log_target_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:03:45', 'admin', '2025-03-24 20:28:03', NULL);
INSERT INTO `sys_dict_data` VALUES (261, 1, '空间', '1', 'p_view_log_target_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:03:45', 'admin', '2025-03-24 20:28:03', NULL);
INSERT INTO `sys_dict_data` VALUES (262, 2, '用户', '2', 'p_view_log_target_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:03:45', 'admin', '2025-03-24 20:28:03', NULL);
INSERT INTO `sys_dict_data` VALUES (263, 0, '图片', '0', 'p_report_target_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:03:45', 'admin', '2025-03-24 20:28:03', NULL);
INSERT INTO `sys_dict_data` VALUES (264, 1, '空间', '1', 'p_report_target_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:03:45', 'admin', '2025-03-24 20:28:03', NULL);
INSERT INTO `sys_dict_data` VALUES (265, 2, '用户', '2', 'p_report_target_type', NULL, 'default', 'N', '0', 'admin', '2025-03-24 18:03:45', 'admin', '2025-03-24 20:28:03', NULL);
INSERT INTO `sys_dict_data` VALUES (266, 0, '待审核', '0', 'p_report_review_status', NULL, 'default', 'N', '0', 'admin', '2025-03-24 20:03:18', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (267, 1, '同意', '1', 'p_report_review_status', NULL, 'default', 'N', '0', 'admin', '2025-03-24 20:03:18', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (268, 2, '拒绝', '2', 'p_report_review_status', NULL, 'default', 'N', '0', 'admin', '2025-03-24 20:03:18', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (269, 0, '正常', '0', 'p_comment_status', NULL, 'primary', 'N', '0', 'admin', '2025-03-24 23:11:27', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (270, 1, '异常', '1', 'p_comment_status', NULL, 'warning', 'N', '0', 'admin', '2025-03-24 23:11:35', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (271, 0, '未处理', '0', 'po_error_resolve_status', NULL, 'default', 'N', '0', 'admin', '2025-03-25 20:20:41', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (272, 1, '处理中', '1', 'po_error_resolve_status', NULL, 'primary', 'N', '0', 'admin', '2025-03-25 20:20:55', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (273, 2, '已解决', '2', 'po_error_resolve_status', NULL, 'success', 'N', '0', 'admin', '2025-03-25 20:21:03', 'admin', '2025-03-25 20:21:07', NULL);
INSERT INTO `sys_dict_data` VALUES (274, 0, '目录', 'M', 'c_menu_type', NULL, 'default', 'N', '0', 'admin', '2024-12-20 22:07:07', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (275, 1, '菜单', 'C', 'c_menu_type', NULL, 'default', 'N', '0', 'admin', '2024-12-20 22:07:07', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (276, 2, '按钮', 'B', 'c_menu_type', NULL, 'default', 'N', '0', 'admin', '2024-12-20 22:07:07', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (277, 3, '功能', 'F', 'c_menu_type', NULL, 'default', 'N', '0', 'admin', '2024-12-20 22:07:07', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (278, 4, 'Tabs', 'T', 'c_menu_type', NULL, 'default', 'N', '0', 'admin', '2024-12-20 22:07:07', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (279, 0, '否', '1', 'c_menu_is_frame', NULL, 'default', 'N', '0', 'admin', '2024-12-20 22:07:07', '', NULL, '否');
INSERT INTO `sys_dict_data` VALUES (280, 1, '是', '0', 'c_menu_is_frame', NULL, 'default', 'N', '0', 'admin', '2024-12-20 22:07:07', '', NULL, '是');
INSERT INTO `sys_dict_data` VALUES (281, 0, '不缓存', '1', 'c_menu_is_chache', NULL, 'default', 'N', '0', 'admin', '2024-12-20 22:07:07', '', NULL, '不缓存');
INSERT INTO `sys_dict_data` VALUES (282, 1, '缓存', '0', 'c_menu_is_chache', NULL, 'default', 'N', '0', 'admin', '2024-12-20 22:07:07', '', NULL, '缓存');
INSERT INTO `sys_dict_data` VALUES (283, 0, '正常', '0', 'c_menu_status', NULL, 'default', 'N', '0', 'admin', '2024-12-20 22:07:07', '', NULL, '正常');
INSERT INTO `sys_dict_data` VALUES (284, 1, '关闭', '1', 'c_menu_status', NULL, 'default', 'N', '0', 'admin', '2024-12-20 22:07:07', 'admin', '2025-04-08 15:02:46', '隐藏');
INSERT INTO `sys_dict_data` VALUES (285, 0, '不显示', '1', 'c_menu_address', NULL, 'default', 'N', '0', 'admin', '2024-12-20 22:07:07', '', NULL, '不显示');
INSERT INTO `sys_dict_data` VALUES (286, 1, '导航', '2', 'c_menu_address', NULL, 'default', 'N', '0', 'admin', '2024-12-20 22:07:07', '', NULL, '导航');
INSERT INTO `sys_dict_data` VALUES (287, 2, '侧边(右侧)', '3', 'c_menu_address', NULL, 'default', 'N', '0', 'admin', '2024-12-20 22:07:07', 'admin', '2025-04-17 09:57:00', '侧边');
INSERT INTO `sys_dict_data` VALUES (288, 3, '侧边（左侧）', '4', 'c_menu_address', NULL, 'default', 'N', '0', 'admin', '2024-12-20 22:07:07', 'admin', '2025-04-17 09:57:10', '页内tabs');
INSERT INTO `sys_dict_data` VALUES (291, 0, '显示', '0', 'c_menu_visible', NULL, 'default', 'N', '0', 'admin', '2024-12-20 22:07:07', 'admin', '2025-04-01 09:15:08', '显示');
INSERT INTO `sys_dict_data` VALUES (292, 1, '不显示', '1', 'c_menu_visible', NULL, 'default', 'N', '0', 'admin', '2024-12-20 22:07:07', 'admin', '2025-04-01 09:15:12', '不显示');
INSERT INTO `sys_dict_data` VALUES (293, 0, '是', '0', 'c_config_is_in', NULL, 'primary', 'N', '0', 'admin', '2025-03-31 16:47:08', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (294, 1, '否', '1', 'c_config_is_in', NULL, 'default', 'N', '0', 'admin', '2025-03-31 16:47:14', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (295, 0, '图片', '0', 'p_category_type', NULL, 'default', 'N', '0', 'admin', '2025-04-08 09:09:31', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (296, 1, '短信', '1', 'u_login_type', NULL, 'success', 'N', '0', 'admin', '2025-04-12 00:03:52', 'admin', '2025-05-23 00:28:43', NULL);
INSERT INTO `sys_dict_data` VALUES (297, 0, '账密', '0', 'u_login_type', NULL, 'primary', 'N', '0', 'admin', '2025-04-12 00:04:05', 'admin', '2025-05-23 00:28:40', NULL);
INSERT INTO `sys_dict_data` VALUES (298, 0, '点赞', '0', 'p_user_behavior_type', NULL, 'success', 'N', '0', 'admin', '2025-04-12 21:26:46', 'admin', '2025-05-22 23:39:33', NULL);
INSERT INTO `sys_dict_data` VALUES (299, 1, '收藏', '1', 'p_user_behavior_type', NULL, 'primary', 'N', '0', 'admin', '2025-04-12 21:26:58', 'admin', '2025-05-22 23:39:37', NULL);
INSERT INTO `sys_dict_data` VALUES (300, 2, '转发', '2', 'p_user_behavior_type', NULL, 'danger', 'N', '0', 'admin', '2025-04-12 21:27:04', 'admin', '2025-05-22 23:40:02', NULL);
INSERT INTO `sys_dict_data` VALUES (301, 0, '图片', '0', 'p_user_behavior_target_type', NULL, 'success', 'N', '0', 'admin', '2025-04-12 21:33:32', 'admin', '2025-05-22 23:29:30', NULL);
INSERT INTO `sys_dict_data` VALUES (302, 1, '空间', '1', 'p_user_behavior_target_type', NULL, 'primary', 'N', '0', 'admin', '2025-04-12 21:33:39', 'admin', '2025-05-22 23:29:23', NULL);
INSERT INTO `sys_dict_data` VALUES (303, 2, '用户', '2', 'p_user_behavior_target_type', NULL, 'info', 'N', '0', 'admin', '2025-04-12 21:33:45', 'admin', '2025-05-22 23:29:35', NULL);
INSERT INTO `sys_dict_data` VALUES (304, 0, '冗余', '0', 'c_file_log_status', NULL, 'default', 'N', '0', 'admin', '2025-04-24 17:35:15', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (305, 1, '正常', '1', 'c_file_log_status', NULL, 'primary', 'N', '0', 'admin', '2025-04-24 17:35:25', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (306, 2, '已删除', '2', 'c_file_log_status', NULL, 'info', 'N', '0', 'admin', '2025-04-24 17:35:33', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (307, 0, '官方', '0', 'c_file_log_oss_type', NULL, 'success', 'N', '0', 'admin', '2025-05-10 22:11:05', 'admin', '2025-05-10 22:11:10', NULL);
INSERT INTO `sys_dict_data` VALUES (308, 1, '阿里云', '1', 'c_file_log_oss_type', NULL, 'default', 'N', '0', 'admin', '2025-05-10 22:11:20', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (309, 0, '图片', '0', 'c_file_log_type', NULL, 'default', 'N', '0', 'admin', '2025-05-10 22:12:48', 'admin', '2025-05-10 23:01:58', NULL);
INSERT INTO `sys_dict_data` VALUES (311, 1, '封面', '1', 'c_file_log_type', NULL, 'default', 'N', '0', 'admin', '2025-05-10 22:13:11', 'admin', '2025-05-31 15:27:39', NULL);
INSERT INTO `sys_dict_data` VALUES (312, 2, '头像', '2', 'c_file_log_type', NULL, 'default', 'N', '0', 'admin', '2025-05-10 22:13:18', 'admin', '2025-05-10 23:06:39', NULL);
INSERT INTO `sys_dict_data` VALUES (313, 0, '是', '0', 'c_file_log_is_compress', NULL, 'default', 'N', '0', 'admin', '2025-05-10 23:42:52', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (314, 1, '否', '1', 'c_file_log_is_compress', NULL, 'default', 'N', '0', 'admin', '2025-05-10 23:42:58', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (315, 4, '已取消', '4', 'po_order_status', NULL, 'warning', 'N', '0', 'admin', '2025-05-17 16:03:02', 'admin', '2025-05-17 16:03:09', NULL);
INSERT INTO `sys_dict_data` VALUES (316, 4, '已取消', '4', 'po_recharge_status', NULL, 'warning', 'N', '0', 'admin', '2025-05-17 16:05:25', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (317, 0, '支付宝', '0', 'po_payment_type', NULL, 'primary', 'N', '0', 'admin', '2025-05-17 16:43:42', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (318, 1, '微信', '1', 'po_payment_type', NULL, 'success', 'N', '0', 'admin', '2025-05-17 16:43:50', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (319, 0, '支付失败', '1', 'po_error_log_type', NULL, 'default', 'N', '0', 'admin', '2025-05-19 11:54:22', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (320, 0, '签名失效', '2', 'po_error_log_type', NULL, 'default', 'N', '0', 'admin', '2025-05-19 11:54:49', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (321, 0, '充值', '0', 'po_points_usage_log_type', NULL, 'success', 'N', '0', 'admin', '2025-05-23 23:20:54', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (322, 1, '消费', '1', 'po_points_usage_log_type', NULL, 'primary', 'N', '0', 'admin', '2025-05-23 23:21:04', 'admin', '2025-05-23 23:21:10', NULL);
INSERT INTO `sys_dict_data` VALUES (323, 2, '提成', '2', 'po_points_usage_log_type', NULL, 'warning', 'N', '0', 'admin', '2025-05-23 23:21:22', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (324, 3, '提现', '3', 'po_points_usage_log_type', NULL, 'danger', 'N', '0', 'admin', '2025-05-23 23:21:37', 'admin', '2025-05-23 23:21:42', NULL);
INSERT INTO `sys_dict_data` VALUES (325, 0, '否', '0', 'common_has_statistics', NULL, 'info', 'N', '0', 'admin', '2025-05-25 21:19:29', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (326, 1, '是', '1', 'common_has_statistics', NULL, 'success', 'N', '0', 'admin', '2025-05-25 21:19:35', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (327, 3, '图片模块', 'PICTURE', 'sys_job_group', NULL, 'info', 'N', '0', 'admin', '2025-05-26 00:08:11', 'admin', '2025-05-26 00:08:20', NULL);
INSERT INTO `sys_dict_data` VALUES (328, 4, '积分模块', 'POINTS', 'sys_job_group', NULL, 'primary', 'N', '0', 'admin', '2025-05-26 00:08:35', 'admin', '2025-05-26 00:08:40', NULL);
INSERT INTO `sys_dict_data` VALUES (329, 0, '通知', '0', 'u_inform_type', NULL, 'primary', 'N', '0', 'admin', '2025-05-27 00:38:20', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (330, 1, '公告', '1', 'u_inform_type', NULL, 'success', 'N', '0', 'admin', '2025-05-27 00:38:29', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (331, 0, '侵权', '0', 'p_report_type', NULL, 'primary', 'N', '0', 'admin', '2025-06-17 16:12:50', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (332, 1, '血腥暴力', '1', 'p_report_type', NULL, 'success', 'N', '0', 'admin', '2025-06-17 16:12:58', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (333, 2, '涉黄', '2', 'p_report_type', NULL, 'info', 'N', '0', 'admin', '2025-06-17 16:13:05', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (334, 0, '待审核', '0', 'p_picture_apply_status', NULL, 'default', 'N', '0', 'admin', '2025-03-24 20:03:18', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (335, 1, '同意', '1', 'p_picture_apply_status', NULL, 'default', 'N', '0', 'admin', '2025-03-24 20:03:18', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (336, 2, '拒绝', '2', 'p_picture_apply_status', NULL, 'default', 'N', '0', 'admin', '2025-03-24 20:03:18', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (337, 0, '原创作品', '0', 'p_picture_apply_type', NULL, 'success', 'N', '0', 'admin', '2025-06-18 14:23:03', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (338, 1, '转载资源', '1', 'p_picture_apply_type', NULL, 'primary', 'N', '0', 'admin', '2025-06-18 14:23:26', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (339, 2, '无版权资源', '2', 'p_picture_apply_type', NULL, 'default', 'N', '0', 'admin', '2025-06-18 14:23:39', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (340, 3, '图片申请文件', '3', 'c_file_log_type', NULL, 'info', 'N', '0', 'admin', '2025-06-26 17:16:36', 'admin', '2025-06-26 17:16:44', NULL);
INSERT INTO `sys_dict_data` VALUES (341, 4, '图片申请图', '4', 'c_file_log_type', NULL, 'info', 'N', '0', 'admin', '2025-06-26 17:16:56', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (342, 0, '容量扩容', '0', 'p_space_dilatation_type', NULL, 'primary', 'N', '0', 'admin', '2025-06-28 00:51:53', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (343, 1, '数量扩容', '1', 'p_space_dilatation_type', NULL, 'primary', 'N', '0', 'admin', '2025-06-28 00:52:06', 'admin', '2025-06-28 00:52:11', NULL);
INSERT INTO `sys_dict_data` VALUES (344, 2, '人数扩容', '2', 'p_space_dilatation_type', NULL, 'success', 'N', '0', 'admin', '2025-06-28 00:52:20', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (345, 1, '空间扩容', '1', 'po_points_usage_type', NULL, 'info', 'N', '0', 'admin', '2025-06-28 21:03:51', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (346, 1, '邀请', '1', 'p_space_join_type', NULL, 'primary', 'N', '0', 'admin', '2025-06-29 16:24:34', 'admin', '2025-06-29 16:34:06', NULL);
INSERT INTO `sys_dict_data` VALUES (347, 0, '创建者', '0', 'p_space_join_type', NULL, 'default', 'N', '0', 'admin', '2025-06-29 16:34:01', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (348, 4, '已取消', '4', 'p_space_invitation_status', NULL, 'warning', 'N', '0', 'admin', '2025-06-29 19:41:05', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (349, 1, '分钟', '1', 'p_statistics_type', NULL, 'default', 'N', '0', 'admin', '2025-07-17 23:36:40', 'admin', '2025-07-20 18:15:50', NULL);
INSERT INTO `sys_dict_data` VALUES (350, 2, '日', '2', 'p_statistics_type', NULL, 'default', 'N', '0', 'admin', '2025-07-17 23:36:57', 'admin', '2025-07-17 23:37:25', NULL);
INSERT INTO `sys_dict_data` VALUES (351, 4, '月', '4', 'p_statistics_type', NULL, 'default', 'N', '0', 'admin', '2025-07-17 23:37:04', 'admin', '2025-07-20 18:14:54', NULL);
INSERT INTO `sys_dict_data` VALUES (352, 5, '年', '5', 'p_statistics_type', NULL, 'default', 'N', '0', 'admin', '2025-07-17 23:37:15', 'admin', '2025-07-20 18:14:59', NULL);
INSERT INTO `sys_dict_data` VALUES (353, 3, '周', '3', 'p_statistics_type', NULL, 'default', 'N', '0', 'admin', '2025-07-20 18:15:10', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (354, 6, '总', '6', 'p_statistics_type', NULL, 'default', 'N', '0', 'admin', '2025-07-24 23:42:08', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (355, 0, '全部', '0', 'u_notice_platform', NULL, 'default', 'N', '0', 'admin', '2025-07-26 17:08:31', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (356, 1, 'Web', '1', 'u_notice_platform', NULL, 'primary', 'N', '0', 'admin', '2025-07-26 17:08:42', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (357, 2, '小程序', '2', 'u_notice_platform', NULL, 'default', 'N', '0', 'admin', '2025-07-26 17:08:53', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (358, 99, '其他', '0', 'u_notice_type', NULL, 'default', 'N', '0', 'admin', '2025-07-26 17:10:28', 'admin', '2025-07-26 19:25:09', NULL);
INSERT INTO `sys_dict_data` VALUES (359, 1, '平台推送', '1', 'u_notice_type', NULL, 'primary', 'N', '0', 'admin', '2025-07-26 17:10:36', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (360, 2, '用户须知', '2', 'u_notice_type', NULL, 'success', 'N', '0', 'admin', '2025-07-26 17:10:46', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (361, 0, '否', '0', 'u_notice_is_exhibit', NULL, 'default', 'N', '0', 'admin', '2025-07-26 17:11:55', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (362, 1, '是', '1', 'u_notice_is_exhibit', NULL, 'success', 'N', '0', 'admin', '2025-07-26 17:12:02', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (363, 0, '隐藏', '0', 'u_notice_status', NULL, 'default', 'N', '0', 'admin', '2025-07-26 17:12:55', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (364, 1, '展示', '1', 'u_notice_status', NULL, 'success', 'N', '0', 'admin', '2025-07-26 17:13:04', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (365, 2, '图生图', '2', 'ai_model_params_type', NULL, 'default', 'N', '0', 'admin', '2025-08-08 21:28:06', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (366, 3, '超时', '3', 'ai_log_status', NULL, 'warning', 'N', '0', 'admin', '2025-08-08 21:32:57', 'admin', '2025-08-08 21:33:02', NULL);
INSERT INTO `sys_dict_data` VALUES (367, 5, 'AI生成', '5', 'c_file_log_type', NULL, 'default', 'N', '0', 'admin', '2025-08-12 17:47:42', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (368, 2, 'AI生成', '2', 'po_points_usage_type', NULL, 'success', 'N', '0', 'admin', '2025-08-14 00:27:46', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (369, 0, '使用', '0', 'ai_prompt_status', NULL, 'primary', 'N', '0', 'admin', '2025-08-14 17:09:59', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (370, 1, '未使用', '1', 'ai_prompt_status', NULL, 'default', 'N', '0', 'admin', '2025-08-14 17:12:40', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (371, 1, '手动上传', '1', 'p_picture_upload_type', NULL, 'default', 'N', '0', 'admin', '2025-08-16 15:06:01', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (372, 2, 'AI生成', '2', 'p_picture_upload_type', NULL, 'primary', 'N', '0', 'admin', '2025-08-16 15:06:13', 'admin', '2025-08-16 15:06:21', NULL);
INSERT INTO `sys_dict_data` VALUES (373, 3, 'API导入', '3', 'p_picture_upload_type', NULL, 'info', 'N', '0', 'admin', '2025-08-16 15:06:32', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (374, 4, '退回', '4', 'po_points_usage_log_type', NULL, 'info', 'N', '0', 'admin', '2025-08-16 17:00:04', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (375, 0, '已发布', '0', 'ai_generate_has_public', NULL, 'success', 'N', '0', 'admin', '2025-08-16 19:14:53', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (376, 1, '未发布', '1', 'ai_generate_has_public', NULL, 'info', 'N', '0', 'admin', '2025-08-16 19:15:03', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (377, 0, 'AI生成', '1', 'p_category_type', NULL, 'info', 'N', '0', 'admin', '2025-08-16 19:33:19', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (378, 3, 'AI生成', '3', 'p_picture_apply_type', NULL, 'info', 'N', '0', 'admin', '2025-08-16 20:00:18', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (379, 4, 'AI模块', 'AI', 'sys_job_group', NULL, 'default', 'N', '0', 'admin', '2025-08-21 17:10:15', 'admin', '2025-08-21 17:10:24', NULL);

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 212 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '任务状态列表');
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '任务分组列表');
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2025-02-28 14:53:19', '', NULL, '登录状态列表');
INSERT INTO `sys_dict_type` VALUES (100, '配置类型', 'c_config_type', '0', 'admin', '2024-12-20 22:04:06', '', NULL, '1：值，2：文件');
INSERT INTO `sys_dict_type` VALUES (104, '权限状态', 'c_permission_status', '0', 'admin', '2024-12-20 22:24:04', '', NULL, '0：正常，1：关闭');
INSERT INTO `sys_dict_type` VALUES (106, '国际化地区状态', 'c_locale_status', '0', 'admin', '2024-12-20 22:26:49', 'admin', '2025-02-28 15:27:30', '0：正常，1：隐藏');
INSERT INTO `sys_dict_type` VALUES (107, '模版状态', 'c_template_status', '0', 'admin', '2025-03-14 17:32:06', '', NULL, ' 0已启用，1=已禁用');
INSERT INTO `sys_dict_type` VALUES (108, '通知模版类型', 'c_template_type', '0', 'admin', '2025-03-14 17:33:15', 'admin', '2025-03-17 13:14:50', '和消息发送共用');
INSERT INTO `sys_dict_type` VALUES (109, '模版渠道', 'c_template_channel', '0', 'admin', '2025-03-14 17:38:02', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (111, '公共删除状态', 'common_delete', '0', 'admin', '2025-03-17 12:57:13', '', NULL, '0正常1删除');
INSERT INTO `sys_dict_type` VALUES (112, '用户状态', 'u_user_status', '0', 'admin', '2025-03-17 12:58:48', 'admin', '2025-03-17 12:59:02', '0正常 1异常 2禁用');
INSERT INTO `sys_dict_type` VALUES (113, '用户性别', 'u_user_sex', '0', 'admin', '2025-03-17 13:00:22', '', NULL, '0未知1男2女');
INSERT INTO `sys_dict_type` VALUES (114, '用户封禁状态', 'u_banned_permission_status', '0', 'admin', '2025-03-17 13:01:37', '', NULL, '0封禁中 1结束');
INSERT INTO `sys_dict_type` VALUES (115, '关系类型', 'u_relation_type', '0', 'admin', '2025-03-17 13:02:36', '', NULL, '0关注 1互关 2拉黑');
INSERT INTO `sys_dict_type` VALUES (116, '用户绑定类型', 'u_binding_type', '0', 'admin', '2025-03-17 13:04:15', 'admin', '2025-03-17 13:07:37', '绑定类型的值最好和登录类型相同');
INSERT INTO `sys_dict_type` VALUES (117, '用户通知状态', 'u_inform_status', '0', 'admin', '2025-03-17 13:05:19', '', NULL, '0=待发送，1=已发送，2=发送失败，3=已撤回');
INSERT INTO `sys_dict_type` VALUES (118, '登录类型', 'u_login_type', '0', 'admin', '2025-03-17 13:07:59', '', NULL, '绑定类型的值最好和登录类型相同');
INSERT INTO `sys_dict_type` VALUES (119, '登录状态', 'u_login_status', '0', 'admin', '2025-03-17 13:09:33', '', NULL, '0成功 1失败');
INSERT INTO `sys_dict_type` VALUES (120, '用户通知是否已读', 'u_inform_is_read', '0', 'admin', '2025-03-17 13:11:42', '', NULL, '0未读 1已读');
INSERT INTO `sys_dict_type` VALUES (121, '账户状态', 'po_account_status', '0', 'admin', '2025-03-24 17:11:27', '', NULL, '0正常 1异常 2禁用');
INSERT INTO `sys_dict_type` VALUES (122, '支付方式类型', 'po_method_type', '0', 'admin', '2025-03-24 17:15:08', '', NULL, '0线上 1线下');
INSERT INTO `sys_dict_type` VALUES (123, '支付方式状态', 'po_method_status', '0', 'admin', '2025-03-24 17:19:05', 'admin', '2025-03-25 20:31:23', '0使用 1未使用');
INSERT INTO `sys_dict_type` VALUES (124, '支付订单状态', 'po_order_status', '0', 'admin', '2025-03-24 17:23:34', '', NULL, '0待支付 1支付成功 2支付失败 3已退款');
INSERT INTO `sys_dict_type` VALUES (125, '支付订单类型', 'po_order_type', '0', 'admin', '2025-03-24 17:26:14', '', NULL, '0充值 1消费');
INSERT INTO `sys_dict_type` VALUES (126, '支付订单支付状态', 'po_payment_status', '0', 'admin', '2025-03-24 17:29:06', '', NULL, '0待支付 1支付成功 2支付失败 3已取消');
INSERT INTO `sys_dict_type` VALUES (127, '充值积分套餐状态', 'po_package_status', '0', 'admin', '2025-03-24 17:35:56', 'admin', '2025-03-24 17:37:41', '0未开始 1正常 2失效');
INSERT INTO `sys_dict_type` VALUES (128, '充值积分套餐是否长期', 'po_package_is_long_term', '0', 'admin', '2025-03-24 17:39:17', 'admin', '2025-03-24 17:39:29', '0是 1否');
INSERT INTO `sys_dict_type` VALUES (129, '充值记录状态', 'po_recharge_status', '0', 'admin', '2025-03-24 17:43:03', '', NULL, '0成功 1失败 2取消 3超时');
INSERT INTO `sys_dict_type` VALUES (130, '风控日志等级', 'po_risk_level', '0', 'admin', '2025-03-24 17:49:29', '', NULL, '0低 1中 2高');
INSERT INTO `sys_dict_type` VALUES (131, '提现记录提现状态', 'po_withdrawal_status', '0', 'admin', '2025-03-24 17:52:27', '', NULL, '0待处理 1已完成 2失败 3超时');
INSERT INTO `sys_dict_type` VALUES (132, '提现记录提现方式', 'po_withdrawal_method', '0', 'admin', '2025-03-24 17:55:10', '', NULL, '0支付宝 1微信');
INSERT INTO `sys_dict_type` VALUES (133, '提现记录审核状态', 'po_approval_status', '0', 'admin', '2025-03-24 17:55:55', 'admin', '2025-03-24 17:56:03', '0待审核 1同意 2拒绝');
INSERT INTO `sys_dict_type` VALUES (134, '积分使用记录类型', 'po_points_usage_type', '0', 'admin', '2025-03-24 17:57:11', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (135, '模型参数状态', 'ai_model_params_status', '0', 'admin', '2025-03-24 18:00:50', '', NULL, '0开启 1关闭');
INSERT INTO `sys_dict_type` VALUES (136, '模型参数类型', 'ai_model_params_type', '0', 'admin', '2025-03-24 18:01:37', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (137, 'ai使用记录状态', 'ai_log_status', '0', 'admin', '2025-03-24 18:03:19', '', NULL, '0成功 1失败 2超时');
INSERT INTO `sys_dict_type` VALUES (138, 'ai使用记录类型', 'ai_log_usage_type', '0', 'admin', '2025-03-24 18:04:40', '', NULL, 'ai_log_usage_type');
INSERT INTO `sys_dict_type` VALUES (139, '官方AI使用记录类型', 'ai_official_usage_type', '0', 'admin', '2025-03-24 18:06:11', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (140, '对话记录状态', 'ai_conversation_status', '0', 'admin', '2025-03-24 18:07:30', 'admin', '2025-03-24 18:07:35', '状态：0成功 1失败 2超时');
INSERT INTO `sys_dict_type` VALUES (141, 'ai对话类型', 'ai_conversation_type', '0', 'admin', '2025-03-24 18:08:57', 'admin', '2025-03-24 18:09:03', '0文本、1图片');
INSERT INTO `sys_dict_type` VALUES (142, '空间存储类型', 'p_space_oss_type', '0', 'admin', '2025-03-24 19:22:57', '', NULL, '0官方 1自定义');
INSERT INTO `sys_dict_type` VALUES (143, '空间类型', 'p_space_type', '0', 'admin', '2025-03-24 19:25:27', '', NULL, 'space_type');
INSERT INTO `sys_dict_type` VALUES (144, '空间角色', 'p_space_role', '0', 'admin', '2025-03-24 19:26:54', '', NULL, '0创建者 1管理员 2编辑者 3浏览者');
INSERT INTO `sys_dict_type` VALUES (145, '空间邀请状态', 'p_space_invitation_status', '0', 'admin', '2025-03-24 19:29:13', '', NULL, '0待同意 1同意 2拒绝 3过期');
INSERT INTO `sys_dict_type` VALUES (146, '空间加入方式', 'p_space_join_type', '0', 'admin', '2025-03-24 19:31:10', '', NULL, '0邀请。。。便于后续扩展，当前仅邀请');
INSERT INTO `sys_dict_type` VALUES (147, '图片分类状态', 'p_category_status', '0', 'admin', '2025-03-24 19:42:06', '', NULL, '0正常 1关闭');
INSERT INTO `sys_dict_type` VALUES (148, '图片分类查询状态', 'p_category_query_status', '0', 'admin', '2025-03-24 19:42:43', '', NULL, '0是 1否');
INSERT INTO `sys_dict_type` VALUES (149, '图片标签状态', 'p_tag_status', '0', 'admin', '2025-03-24 19:43:52', '', NULL, '0正常 1禁止');
INSERT INTO `sys_dict_type` VALUES (150, '空间状态', 'p_space_status', '0', 'admin', '2025-03-24 19:52:29', '', NULL, '0公共 1私有');
INSERT INTO `sys_dict_type` VALUES (151, '图片状态', 'p_picture_status', '0', 'admin', '2025-03-24 19:56:07', '', NULL, '0公共 1私有');
INSERT INTO `sys_dict_type` VALUES (152, '图片审核状态', 'p_picture_review_status', '0', 'admin', '2025-03-24 20:03:03', '', NULL, '0待审核; 1通过; 2拒绝');
INSERT INTO `sys_dict_type` VALUES (153, '图片下载是否免费', 'p_download_is_free', '0', 'admin', '2025-03-24 20:06:16', '', NULL, '0 否，1 是');
INSERT INTO `sys_dict_type` VALUES (154, '图片下载方式', 'p_download_type', '0', 'admin', '2025-03-24 20:20:22', '', NULL, '0手动 1API 2批量');
INSERT INTO `sys_dict_type` VALUES (155, '图片下载状态', 'p_download_status', '0', 'admin', '2025-03-24 20:24:15', '', NULL, '1失败 0成功');
INSERT INTO `sys_dict_type` VALUES (156, '图片下载来源', 'p_download_refer_source', '0', 'admin', '2025-03-24 20:25:57', 'admin', '2025-03-24 20:28:36', '0详情 1分享');
INSERT INTO `sys_dict_type` VALUES (157, '搜索记录类型', 'p_search_type', '0', 'admin', '2025-03-24 20:30:56', 'admin', '2025-03-24 20:37:10', '0图片 1空间 2用户');
INSERT INTO `sys_dict_type` VALUES (158, '搜索状态', 'p_search_status', '0', 'admin', '2025-03-24 20:33:28', '', NULL, '0成功 1失败');
INSERT INTO `sys_dict_type` VALUES (159, '搜索来源', 'p_search_refer_source', '0', 'admin', '2025-03-24 20:35:19', '', NULL, '0首页 1推荐 2搜索页 3AI推荐 4历史搜索');
INSERT INTO `sys_dict_type` VALUES (160, '行为日志行为类型', 'p_action_log_type', '0', 'admin', '2025-03-24 20:38:43', 'admin', '2025-03-24 20:43:54', '0点击 1收藏 2下载 3关注用户 4查看空间');
INSERT INTO `sys_dict_type` VALUES (161, '行为日志目标对象', 'p_action_log_target_type', '0', 'admin', '2025-03-24 20:41:02', '', NULL, '0图片 1空间 2用户');
INSERT INTO `sys_dict_type` VALUES (162, '用户浏览记录目标对象', 'p_view_log_target_type', '0', 'admin', '2025-03-24 20:44:35', '', NULL, '0图片 1空间 2用户');
INSERT INTO `sys_dict_type` VALUES (163, '用户举报目标对象', 'p_report_target_type', '0', 'admin', '2025-03-24 20:45:45', '', NULL, '0图片 1空间 2用户');
INSERT INTO `sys_dict_type` VALUES (164, '用户举报审核状态', 'p_report_review_status', '0', 'admin', '2025-03-24 20:48:32', '', NULL, '0待审核; 1通过; 2拒绝');
INSERT INTO `sys_dict_type` VALUES (165, '图片评论状态', 'p_comment_status', '0', 'admin', '2025-03-24 23:11:09', '', NULL, '0正常 1异常');
INSERT INTO `sys_dict_type` VALUES (166, '积分异常解决状态', 'po_error_resolve_status', '0', 'admin', '2025-03-25 20:20:29', '', NULL, '0未处理 1处理中 2已解决');
INSERT INTO `sys_dict_type` VALUES (181, '菜单类型', 'c_menu_type', '0', 'admin', '2024-12-20 22:17:52', '', NULL, 'M：目录，C：菜单，B：按钮，F：功能，T：tabs');
INSERT INTO `sys_dict_type` VALUES (182, '菜单是否外链', 'c_menu_is_frame', '0', 'admin', '2024-12-20 22:19:59', 'admin', '2024-12-20 22:23:22', '1：否，0：是');
INSERT INTO `sys_dict_type` VALUES (183, '菜单是否缓存', 'c_menu_is_chache', '0', 'admin', '2024-12-20 22:22:13', 'admin', '2024-12-20 22:23:29', '1：不缓存，0：缓存');
INSERT INTO `sys_dict_type` VALUES (184, '菜单状态', 'c_menu_status', '0', 'admin', '2024-12-20 22:24:04', '', NULL, '0：正常，1：隐藏');
INSERT INTO `sys_dict_type` VALUES (185, '菜单位置', 'c_menu_address', '0', 'admin', '2024-12-20 22:25:00', '', NULL, '1：不显示 2：导航，3侧边，4：页内tabs');
INSERT INTO `sys_dict_type` VALUES (186, '菜单是否显示', 'c_menu_visible', '0', 'admin', '2024-12-20 23:01:02', 'admin', '2025-04-08 01:11:03', '0显示1不显示');
INSERT INTO `sys_dict_type` VALUES (187, '用户配置是否内置', 'c_config_is_in', '0', 'admin', '2025-03-31 16:46:57', '', NULL, '0是 1否');
INSERT INTO `sys_dict_type` VALUES (188, '图片分类类型', 'p_category_type', '0', 'admin', '2025-04-08 09:09:19', '', NULL, '0图片');
INSERT INTO `sys_dict_type` VALUES (189, '图片用户行为类型', 'p_user_behavior_type', '0', 'admin', '2025-04-12 21:26:36', '', NULL, '0点赞 1收藏 2转发');
INSERT INTO `sys_dict_type` VALUES (190, '图片用户行为目标类型', 'p_user_behavior_target_type', '0', 'admin', '2025-04-12 21:33:22', '', NULL, '0图片 1空间 2用户');
INSERT INTO `sys_dict_type` VALUES (191, '文件日志状态', 'c_file_log_status', '0', 'admin', '2025-04-24 17:35:04', '', NULL, '0冗余,1正常,1已删除');
INSERT INTO `sys_dict_type` VALUES (192, '文件日志存储类型', 'c_file_log_oss_type', '0', 'admin', '2025-05-10 22:09:34', '', NULL, '0官方 1阿里云');
INSERT INTO `sys_dict_type` VALUES (193, '文件日志类型', 'c_file_log_type', '0', 'admin', '2025-05-10 22:10:54', '', NULL, '0原图 1压缩图 2空间封面 3头像');
INSERT INTO `sys_dict_type` VALUES (194, '文件日志是否压缩', 'c_file_log_is_compress', '0', 'admin', '2025-05-10 23:07:05', '', NULL, '0是 1否');
INSERT INTO `sys_dict_type` VALUES (195, '支付方式', 'po_payment_type', '0', 'admin', '2025-05-17 16:43:31', '', NULL, '0支付宝 1微信');
INSERT INTO `sys_dict_type` VALUES (196, '订单异常捕获类型', 'po_error_log_type', '0', 'admin', '2025-05-19 11:53:43', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (197, '积分使用记录日志类型', 'po_points_usage_log_type', '0', 'admin', '2025-05-23 22:47:35', '', NULL, '0充值 1消费 2提成 3提现');
INSERT INTO `sys_dict_type` VALUES (198, '公共是否统计', 'common_has_statistics', '0', 'admin', '2025-05-25 21:02:50', '', NULL, '0否 1是');
INSERT INTO `sys_dict_type` VALUES (199, '用户通知类型', 'u_inform_type', '0', 'admin', '2025-05-27 00:38:06', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (200, '用户举报类型', 'p_report_type', '0', 'admin', '2025-06-17 16:12:33', '', NULL, '0侵权 1血腥暴力 2涉黄');
INSERT INTO `sys_dict_type` VALUES (201, '图片申请类型', 'p_picture_apply_type', '0', 'admin', '2025-06-17 23:49:58', '', NULL, '0原创作品 1转载资源 2无版权资源');
INSERT INTO `sys_dict_type` VALUES (202, '图片申请信息状态', 'p_picture_apply_status', '0', 'admin', '2025-06-17 23:54:34', '', NULL, '0待审核; 1通过; 2拒绝');
INSERT INTO `sys_dict_type` VALUES (203, '空间扩容类型', 'p_space_dilatation_type', '0', 'admin', '2025-06-28 00:51:40', 'admin', '2025-06-28 19:12:23', '0容量扩容 1数量扩容 2人数扩容');
INSERT INTO `sys_dict_type` VALUES (204, '图片模块统计类型', 'p_statistics_type', '0', 'admin', '2025-07-17 23:36:25', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (205, '用户公告平台', 'u_notice_platform', '0', 'admin', '2025-07-26 17:08:06', '', NULL, '0全部 1web');
INSERT INTO `sys_dict_type` VALUES (206, '用户公告类型', 'u_notice_type', '0', 'admin', '2025-07-26 17:10:16', '', NULL, '0其他1平台推送 2用户须知');
INSERT INTO `sys_dict_type` VALUES (207, '公告是否展示', 'u_notice_is_exhibit', '0', 'admin', '2025-07-26 17:11:15', '', NULL, '0否 1是');
INSERT INTO `sys_dict_type` VALUES (208, '用户公告状态', 'u_notice_status', '0', 'admin', '2025-07-26 17:12:45', '', NULL, '0隐藏 1展示');
INSERT INTO `sys_dict_type` VALUES (209, '提示词状态', 'ai_prompt_status', '0', 'admin', '2025-08-14 17:09:42', '', NULL, '提示词状态 0使用 1未使用');
INSERT INTO `sys_dict_type` VALUES (210, '图片上传类型', 'p_picture_upload_type', '0', 'admin', '2025-08-16 15:05:49', '', NULL, '1手动上传 2AI生成 3API导入');
INSERT INTO `sys_dict_type` VALUES (211, 'AI生成是否发布', 'ai_generate_has_public', '0', 'admin', '2025-08-16 19:14:43', '', NULL, '0已发布1未发布');

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `job_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 108 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '定时任务调度表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES (1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '0/10 * * * * ?', '3', '1', '1', 'admin', '2025-02-28 14:53:20', '', NULL, '');
INSERT INTO `sys_job` VALUES (2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', 'admin', '2025-02-28 14:53:20', '', NULL, '');
INSERT INTO `sys_job` VALUES (3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1', 'admin', '2025-02-28 14:53:20', '', NULL, '');
INSERT INTO `sys_job` VALUES (100, '自动删除文件日志', 'SYSTEM', 'fileLogTask.autoDeleteFile(1000,1)', '0 0 0/6 * * ?', '3', '1', '1', 'admin', '2025-05-11 18:19:48', 'admin', '2025-08-05 21:39:09', '');
INSERT INTO `sys_job` VALUES (101, '定时更新过期积分充值订单', 'SYSTEM', 'pointsTask.autoUpdateExpiredOrder(6)', '0/3 * * * * ?', '3', '1', '1', 'admin', '2025-05-19 11:20:05', 'admin', '2025-07-28 22:15:42', '');
INSERT INTO `sys_job` VALUES (102, '定时更新图片浏览记录', 'PICTURE', 'pictureTask.autoUpdateUserViewLogInfo', '0 0/1 * * * ?', '3', '1', '0', 'admin', '2025-05-26 00:25:43', 'admin', '2025-08-18 19:13:35', '');
INSERT INTO `sys_job` VALUES (103, '定时更新图片下载记录', 'PICTURE', 'pictureTask.autoUpdatePictureDownloadLogInfo', '0 0/1 * * * ?', '3', '1', '0', 'admin', '2025-05-26 14:33:03', '', '2025-08-18 19:13:37', '');
INSERT INTO `sys_job` VALUES (104, '定时更新用户行为记录', 'PICTURE', 'pictureTask.autoUpdateUserBehaviorInfo', '0 0/1 * * * ?', '3', '1', '0', 'admin', '2025-05-26 15:22:13', '', '2025-08-17 15:18:25', '');
INSERT INTO `sys_job` VALUES (105, '定时统计图片热门-天', 'PICTURE', 'pictureTask.autoStatisticsPictureByDay', '0 0/5 * * * ?', '3', '1', '0', 'admin', '2025-07-19 02:08:55', 'admin', '2025-07-22 20:24:50', '');
INSERT INTO `sys_job` VALUES (106, '定时更新充值套餐开始与过期', 'POINTS', 'pointsTask.autoUpdatePointsRechargePackageInfo', '0 1 0 * * ?', '3', '1', '1', 'admin', '2025-07-25 22:02:39', 'admin', '2025-08-05 21:39:18', '');
INSERT INTO `sys_job` VALUES (107, '自动执行生成更新任务', 'AI', 'aiTask.autoExecuteUpdateTask', '0 0 0/6 * * ?', '3', '1', '0', 'admin', '2025-08-21 17:11:49', '', '2025-08-21 17:11:53', '');

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log`  (
  `job_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '日志信息',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '异常信息',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24867 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '定时任务调度日志表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor`  (
  `info_id` bigint NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作系统',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '提示消息',
  `login_time` datetime NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `idx_sys_logininfor_s`(`status` ASC) USING BTREE,
  INDEX `idx_sys_logininfor_lt`(`login_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1098 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统访问记录' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '路由名称',
  `is_frame` int NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2358 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 1, 'system', NULL, '', '', 1, 0, 'M', '0', '0', '', 'system', 'admin', '2025-02-28 14:53:16', '', NULL, '系统管理目录');
INSERT INTO `sys_menu` VALUES (2, '系统监控', 0, 2, 'monitor', NULL, '', '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', '2025-02-28 14:53:16', '', NULL, '系统监控目录');
INSERT INTO `sys_menu` VALUES (3, '系统工具', 0, 3, 'tool', NULL, '', '', 1, 0, 'M', '0', '0', '', 'tool', 'admin', '2025-02-28 14:53:16', '', NULL, '系统工具目录');
INSERT INTO `sys_menu` VALUES (4, '若依官网', 0, 99, 'http://ruoyi.vip', NULL, '', '', 0, 0, 'M', '1', '1', '', 'guide', 'admin', '2025-02-28 14:53:16', 'admin', '2025-02-28 15:09:17', '若依官网地址');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', '2025-02-28 14:53:16', '', NULL, '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin', '2025-02-28 14:53:16', '', NULL, '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', '2025-02-28 14:53:16', '', NULL, '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin', '2025-02-28 14:53:16', '', NULL, '部门管理菜单');
INSERT INTO `sys_menu` VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', '', 1, 0, 'C', '0', '0', 'system:post:list', 'post', 'admin', '2025-02-28 14:53:17', '', NULL, '岗位管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin', '2025-02-28 14:53:17', '', NULL, '字典管理菜单');
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit', 'admin', '2025-02-28 14:53:17', '', NULL, '参数设置菜单');
INSERT INTO `sys_menu` VALUES (107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', '', 1, 0, 'C', '0', '0', 'system:notice:list', 'message', 'admin', '2025-02-28 14:53:17', '', NULL, '通知公告菜单');
INSERT INTO `sys_menu` VALUES (108, '日志管理', 1, 9, 'log', '', '', '', 1, 0, 'M', '0', '0', '', 'log', 'admin', '2025-02-28 14:53:17', '', NULL, '日志管理菜单');
INSERT INTO `sys_menu` VALUES (109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', '', 1, 0, 'C', '0', '0', 'monitor:online:list', 'online', 'admin', '2025-02-28 14:53:17', '', NULL, '在线用户菜单');
INSERT INTO `sys_menu` VALUES (110, '定时任务', 2, 2, 'job', 'monitor/job/index', '', '', 1, 0, 'C', '0', '0', 'monitor:job:list', 'job', 'admin', '2025-02-28 14:53:17', '', NULL, '定时任务菜单');
INSERT INTO `sys_menu` VALUES (111, '数据监控', 2, 3, 'druid', 'monitor/druid/index', '', '', 1, 0, 'C', '0', '0', 'monitor:druid:list', 'druid', 'admin', '2025-02-28 14:53:17', '', NULL, '数据监控菜单');
INSERT INTO `sys_menu` VALUES (112, '服务监控', 2, 4, 'server', 'monitor/server/index', '', '', 1, 0, 'C', '0', '0', 'monitor:server:list', 'server', 'admin', '2025-02-28 14:53:17', '', NULL, '服务监控菜单');
INSERT INTO `sys_menu` VALUES (113, '缓存监控', 2, 5, 'cache', 'monitor/cache/index', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis', 'admin', '2025-02-28 14:53:17', '', NULL, '缓存监控菜单');
INSERT INTO `sys_menu` VALUES (114, '缓存列表', 2, 6, 'cacheList', 'monitor/cache/list', '', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis-list', 'admin', '2025-02-28 14:53:17', '', NULL, '缓存列表菜单');
INSERT INTO `sys_menu` VALUES (115, '表单构建', 3, 1, 'build', 'tool/build/index', '', '', 1, 0, 'C', '0', '0', 'tool:build:list', 'build', 'admin', '2025-02-28 14:53:17', '', NULL, '表单构建菜单');
INSERT INTO `sys_menu` VALUES (116, '代码生成', 3, 2, 'gen', 'tool/gen/index', '', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code', 'admin', '2025-02-28 14:53:17', '', NULL, '代码生成菜单');
INSERT INTO `sys_menu` VALUES (117, '系统接口', 3, 3, 'swagger', 'tool/swagger/index', '', '', 1, 0, 'C', '0', '0', 'tool:swagger:list', 'swagger', 'admin', '2025-02-28 14:53:17', '', NULL, '系统接口菜单');
INSERT INTO `sys_menu` VALUES (500, '操作日志', 108, 1, 'operlog', 'monitor/operlog/index', '', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list', 'form', 'admin', '2025-02-28 14:53:17', '', NULL, '操作日志菜单');
INSERT INTO `sys_menu` VALUES (501, '登录日志', 108, 2, 'logininfor', 'monitor/logininfor/index', '', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', 'logininfor', 'admin', '2025-02-28 14:53:17', '', NULL, '登录日志菜单');
INSERT INTO `sys_menu` VALUES (1000, '用户查询', 100, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1001, '用户新增', 100, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1002, '用户修改', 100, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1003, '用户删除', 100, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1004, '用户导出', 100, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1005, '用户导入', 100, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1006, '重置密码', 100, 7, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1007, '角色查询', 101, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1008, '角色新增', 101, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1009, '角色修改', 101, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1010, '角色删除', 101, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1011, '角色导出', 101, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1012, '菜单查询', 102, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1013, '菜单新增', 102, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1014, '菜单修改', 102, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1015, '菜单删除', 102, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1016, '部门查询', 103, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1017, '部门新增', 103, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1018, '部门修改', 103, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1019, '部门删除', 103, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1020, '岗位查询', 104, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1021, '岗位新增', 104, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1022, '岗位修改', 104, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1023, '岗位删除', 104, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1024, '岗位导出', 104, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1025, '字典查询', 105, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1026, '字典新增', 105, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1027, '字典修改', 105, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1028, '字典删除', 105, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1029, '字典导出', 105, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1030, '参数查询', 106, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1031, '参数新增', 106, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1032, '参数修改', 106, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1033, '参数删除', 106, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1034, '参数导出', 106, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1035, '公告查询', 107, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1036, '公告新增', 107, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1037, '公告修改', 107, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1038, '公告删除', 107, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1039, '操作查询', 500, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1040, '操作删除', 500, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1041, '日志导出', 500, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1042, '登录查询', 501, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1043, '登录删除', 501, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1044, '日志导出', 501, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1045, '账户解锁', 501, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:unlock', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1046, '在线查询', 109, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1047, '批量强退', 109, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', '2025-02-28 14:53:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1048, '单条强退', 109, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', '2025-02-28 14:53:18', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1049, '任务查询', 110, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', 'admin', '2025-02-28 14:53:18', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1050, '任务新增', 110, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', 'admin', '2025-02-28 14:53:18', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1051, '任务修改', 110, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', 'admin', '2025-02-28 14:53:18', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1052, '任务删除', 110, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', 'admin', '2025-02-28 14:53:18', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1053, '状态修改', 110, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin', '2025-02-28 14:53:18', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1054, '任务导出', 110, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', 'admin', '2025-02-28 14:53:18', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1055, '生成查询', 116, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', '2025-02-28 14:53:18', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1056, '生成修改', 116, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', '2025-02-28 14:53:18', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1057, '生成删除', 116, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', '2025-02-28 14:53:18', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1058, '导入代码', 116, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', '2025-02-28 14:53:18', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1059, '预览代码', 116, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', '2025-02-28 14:53:18', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1060, '生成代码', 116, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', '2025-02-28 14:53:18', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2000, '用户配置', 0, 5, 'userConfigManage', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'config', 'admin', '2025-02-28 15:11:45', 'admin', '2025-03-18 19:21:03', '');
INSERT INTO `sys_menu` VALUES (2001, '配置信息', 2000, 0, 'configInfo', 'config/configInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'config:configInfo:list', '#', 'admin', '2025-02-28 15:44:44', 'admin', '2025-04-24 17:40:32', '配置信息菜单');
INSERT INTO `sys_menu` VALUES (2002, '配置信息查询', 2001, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:configInfo:query', '#', 'admin', '2025-02-28 15:44:45', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2003, '配置信息新增', 2001, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:configInfo:add', '#', 'admin', '2025-02-28 15:44:45', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2004, '配置信息修改', 2001, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:configInfo:edit', '#', 'admin', '2025-02-28 15:44:45', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2005, '配置信息删除', 2001, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:configInfo:remove', '#', 'admin', '2025-02-28 15:44:45', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2006, '配置信息导出', 2001, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:configInfo:export', '#', 'admin', '2025-02-28 15:44:45', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2007, '国际化键名', 2000, 4, 'i18nKeyInfo', 'config/i18nKeyInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'config:i18nKeyInfo:list', '#', 'admin', '2025-02-28 15:44:45', 'admin', '2025-04-24 17:41:03', '国际化键名菜单');
INSERT INTO `sys_menu` VALUES (2008, '国际化键名查询', 2007, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:i18nKeyInfo:query', '#', 'admin', '2025-02-28 15:44:45', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2009, '国际化键名新增', 2007, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:i18nKeyInfo:add', '#', 'admin', '2025-02-28 15:44:45', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2010, '国际化键名修改', 2007, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:i18nKeyInfo:edit', '#', 'admin', '2025-02-28 15:44:45', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2011, '国际化键名删除', 2007, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:i18nKeyInfo:remove', '#', 'admin', '2025-02-28 15:44:45', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2012, '国际化键名导出', 2007, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:i18nKeyInfo:export', '#', 'admin', '2025-02-28 15:44:45', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2013, '国际化国家', 2000, 3, 'i18nLocaleInfo', 'config/i18nLocaleInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'config:i18nLocaleInfo:list', '#', 'admin', '2025-02-28 15:44:45', 'admin', '2025-04-24 17:40:57', '国际化国家菜单');
INSERT INTO `sys_menu` VALUES (2014, '国际化国家查询', 2013, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:i18nLocaleInfo:query', '#', 'admin', '2025-02-28 15:44:45', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2015, '国际化国家新增', 2013, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:i18nLocaleInfo:add', '#', 'admin', '2025-02-28 15:44:45', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2016, '国际化国家修改', 2013, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:i18nLocaleInfo:edit', '#', 'admin', '2025-02-28 15:44:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2017, '国际化国家删除', 2013, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:i18nLocaleInfo:remove', '#', 'admin', '2025-02-28 15:44:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2018, '国际化国家导出', 2013, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:i18nLocaleInfo:export', '#', 'admin', '2025-02-28 15:44:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2019, '国际化信息', 2000, 5, 'i18nMessageInfo', 'config/i18nMessageInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'config:i18nMessageInfo:list', '#', 'admin', '2025-02-28 15:44:46', 'admin', '2025-04-24 17:41:10', '国际化信息菜单');
INSERT INTO `sys_menu` VALUES (2020, '国际化信息查询', 2019, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:i18nMessageInfo:query', '#', 'admin', '2025-02-28 15:44:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2021, '国际化信息新增', 2019, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:i18nMessageInfo:add', '#', 'admin', '2025-02-28 15:44:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2022, '国际化信息修改', 2019, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:i18nMessageInfo:edit', '#', 'admin', '2025-02-28 15:44:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2023, '国际化信息删除', 2019, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:i18nMessageInfo:remove', '#', 'admin', '2025-02-28 15:44:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2024, '国际化信息导出', 2019, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:i18nMessageInfo:export', '#', 'admin', '2025-02-28 15:44:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2031, '通知模版查询', 2036, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:informTemplateInfo:query', '#', 'admin', '2025-03-14 17:41:39', 'admin', '2025-03-31 00:06:46', '');
INSERT INTO `sys_menu` VALUES (2032, '通知模版新增', 2036, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:informTemplateInfo:add', '#', 'admin', '2025-03-14 17:41:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2033, '通知模版修改', 2036, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:informTemplateInfo:edit', '#', 'admin', '2025-03-14 17:41:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2034, '通知模版删除', 2036, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:informTemplateInfo:remove', '#', 'admin', '2025-03-14 17:41:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2035, '通知模版导出', 2036, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:informTemplateInfo:export', '#', 'admin', '2025-03-14 17:41:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2036, '通知模版', 2000, 2, 'informTemplateInfo', 'config/informTemplateInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'config:informTemplateInfo:list', '#', 'admin', '2025-03-14 17:46:20', 'admin', '2025-04-24 17:40:43', '通知模版菜单');
INSERT INTO `sys_menu` VALUES (2037, '用户管理', 0, 6, 'userInfoManage', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'peoples', 'admin', '2025-03-17 12:55:12', 'admin', '2025-03-18 19:20:48', '');
INSERT INTO `sys_menu` VALUES (2038, '用户封禁权限', 2037, 1, 'bannedPermissionInfo', 'user/bannedPermissionInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'user:bannedPermissionInfo:list', '#', 'admin', '2025-03-17 13:34:54', '', NULL, '用户封禁权限菜单');
INSERT INTO `sys_menu` VALUES (2039, '用户封禁权限查询', 2038, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:bannedPermissionInfo:query', '#', 'admin', '2025-03-17 13:34:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2040, '用户封禁权限新增', 2038, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:bannedPermissionInfo:add', '#', 'admin', '2025-03-17 13:34:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2041, '用户封禁权限修改', 2038, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:bannedPermissionInfo:edit', '#', 'admin', '2025-03-17 13:34:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2042, '用户封禁权限删除', 2038, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:bannedPermissionInfo:remove', '#', 'admin', '2025-03-17 13:34:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2043, '用户封禁权限导出', 2038, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:bannedPermissionInfo:export', '#', 'admin', '2025-03-17 13:34:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2044, '用户通知记录', 2037, 1, 'informInfo', 'user/informInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'user:informInfo:list', '#', 'admin', '2025-03-17 13:34:55', '', NULL, '用户通知记录菜单');
INSERT INTO `sys_menu` VALUES (2045, '用户通知记录查询', 2044, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:informInfo:query', '#', 'admin', '2025-03-17 13:34:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2046, '用户通知记录新增', 2044, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:informInfo:add', '#', 'admin', '2025-03-17 13:34:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2047, '用户通知记录修改', 2044, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:informInfo:edit', '#', 'admin', '2025-03-17 13:34:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2048, '用户通知记录删除', 2044, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:informInfo:remove', '#', 'admin', '2025-03-17 13:34:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2049, '用户通知记录导出', 2044, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:informInfo:export', '#', 'admin', '2025-03-17 13:34:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2050, '用户登录日志', 2037, 1, 'loginLogInfo', 'user/loginLogInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'user:loginLogInfo:list', '#', 'admin', '2025-03-17 13:34:55', '', NULL, '用户登录日志菜单');
INSERT INTO `sys_menu` VALUES (2051, '用户登录日志查询', 2050, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:loginLogInfo:query', '#', 'admin', '2025-03-17 13:34:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2052, '用户登录日志新增', 2050, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:loginLogInfo:add', '#', 'admin', '2025-03-17 13:34:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2053, '用户登录日志修改', 2050, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:loginLogInfo:edit', '#', 'admin', '2025-03-17 13:34:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2054, '用户登录日志删除', 2050, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:loginLogInfo:remove', '#', 'admin', '2025-03-17 13:34:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2055, '用户登录日志导出', 2050, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:loginLogInfo:export', '#', 'admin', '2025-03-17 13:34:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2056, '用户第三方账号绑定', 2037, 1, 'userBindingInfo', 'user/userBindingInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'user:userBindingInfo:list', '#', 'admin', '2025-03-17 13:34:55', '', NULL, '用户第三方账号绑定菜单');
INSERT INTO `sys_menu` VALUES (2057, '用户第三方账号绑定查询', 2056, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:userBindingInfo:query', '#', 'admin', '2025-03-17 13:34:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2058, '用户第三方账号绑定新增', 2056, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:userBindingInfo:add', '#', 'admin', '2025-03-17 13:34:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2059, '用户第三方账号绑定修改', 2056, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:userBindingInfo:edit', '#', 'admin', '2025-03-17 13:34:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2060, '用户第三方账号绑定删除', 2056, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:userBindingInfo:remove', '#', 'admin', '2025-03-17 13:34:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2061, '用户第三方账号绑定导出', 2056, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:userBindingInfo:export', '#', 'admin', '2025-03-17 13:34:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2062, '用户好友关系', 2037, 1, 'userFriendInfo', 'user/userFriendInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'user:userFriendInfo:list', '#', 'admin', '2025-03-17 13:34:56', '', NULL, '用户好友关系菜单');
INSERT INTO `sys_menu` VALUES (2063, '用户好友关系查询', 2062, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:userFriendInfo:query', '#', 'admin', '2025-03-17 13:34:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2064, '用户好友关系新增', 2062, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:userFriendInfo:add', '#', 'admin', '2025-03-17 13:34:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2065, '用户好友关系修改', 2062, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:userFriendInfo:edit', '#', 'admin', '2025-03-17 13:34:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2066, '用户好友关系删除', 2062, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:userFriendInfo:remove', '#', 'admin', '2025-03-17 13:34:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2067, '用户好友关系导出', 2062, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:userFriendInfo:export', '#', 'admin', '2025-03-17 13:34:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2068, '用户信息', 2037, 1, 'userInfo', 'user/userInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'user:userInfo:list', '#', 'admin', '2025-03-17 13:34:56', '', NULL, '用户信息菜单');
INSERT INTO `sys_menu` VALUES (2069, '用户信息查询', 2068, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:userInfo:query', '#', 'admin', '2025-03-17 13:34:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2070, '用户信息新增', 2068, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:userInfo:add', '#', 'admin', '2025-03-17 13:34:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2071, '用户信息修改', 2068, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:userInfo:edit', '#', 'admin', '2025-03-17 13:34:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2072, '用户信息删除', 2068, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:userInfo:remove', '#', 'admin', '2025-03-17 13:34:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2073, '用户信息导出', 2068, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:userInfo:export', '#', 'admin', '2025-03-17 13:34:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2074, '用户关系', 2037, 1, 'userRelationInfo', 'user/userRelationInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'user:userRelationInfo:list', '#', 'admin', '2025-03-17 13:34:56', '', NULL, '用户关系菜单');
INSERT INTO `sys_menu` VALUES (2075, '用户关系查询', 2074, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:userRelationInfo:query', '#', 'admin', '2025-03-17 13:34:57', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2076, '用户关系新增', 2074, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:userRelationInfo:add', '#', 'admin', '2025-03-17 13:34:57', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2077, '用户关系修改', 2074, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:userRelationInfo:edit', '#', 'admin', '2025-03-17 13:34:57', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2078, '用户关系删除', 2074, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:userRelationInfo:remove', '#', 'admin', '2025-03-17 13:34:57', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2079, '用户关系导出', 2074, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'user:userRelationInfo:export', '#', 'admin', '2025-03-17 13:34:57', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2080, '图片模块', 0, 7, 'pictureManage', NULL, NULL, '', 1, 0, 'M', '0', '0', NULL, 'education', 'admin', '2025-03-24 23:00:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2081, '图片分类信息', 2080, 0, 'pictureCategoryInfo', 'picture/pictureCategoryInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'picture:pictureCategoryInfo:list', '#', 'admin', '2025-03-25 00:05:08', 'admin', '2025-04-12 21:51:00', '图片分类信息菜单');
INSERT INTO `sys_menu` VALUES (2082, '图片分类信息查询', 2081, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureCategoryInfo:query', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2083, '图片分类信息新增', 2081, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureCategoryInfo:add', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2084, '图片分类信息修改', 2081, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureCategoryInfo:edit', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2085, '图片分类信息删除', 2081, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureCategoryInfo:remove', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2086, '图片分类信息导出', 2081, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureCategoryInfo:export', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2087, '图片评论', 2080, 12, 'pictureCommentInfo', 'picture/pictureCommentInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'picture:pictureCommentInfo:list', '#', 'admin', '2025-03-25 00:05:08', 'admin', '2025-06-03 22:06:20', '图片评论菜单');
INSERT INTO `sys_menu` VALUES (2088, '图片评论查询', 2087, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureCommentInfo:query', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2089, '图片评论新增', 2087, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureCommentInfo:add', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2090, '图片评论修改', 2087, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureCommentInfo:edit', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2091, '图片评论删除', 2087, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureCommentInfo:remove', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2092, '图片评论导出', 2087, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureCommentInfo:export', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2093, '评论点赞记录', 2080, 13, 'pictureCommentLikeInfo', 'picture/pictureCommentLikeInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'picture:pictureCommentLikeInfo:list', '#', 'admin', '2025-03-25 00:05:08', 'admin', '2025-06-03 22:06:24', '评论点赞记录菜单');
INSERT INTO `sys_menu` VALUES (2094, '评论点赞记录查询', 2093, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureCommentLikeInfo:query', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2095, '评论点赞记录新增', 2093, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureCommentLikeInfo:add', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2096, '评论点赞记录修改', 2093, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureCommentLikeInfo:edit', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2097, '评论点赞记录删除', 2093, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureCommentLikeInfo:remove', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2098, '评论点赞记录导出', 2093, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureCommentLikeInfo:export', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2111, '图片信息', 2080, 2, 'pictureInfo', 'picture/pictureInfo/index', '', '', 1, 0, 'C', '0', '0', 'picture:pictureInfo:list', '#', 'admin', '2025-03-25 00:05:08', 'admin', '2025-07-29 00:30:16', '图片信息菜单');
INSERT INTO `sys_menu` VALUES (2112, '图片信息查询', 2111, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureInfo:query', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2113, '图片信息新增', 2111, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureInfo:add', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2114, '图片信息修改', 2111, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureInfo:edit', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2115, '图片信息删除', 2111, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureInfo:remove', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2116, '图片信息导出', 2111, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureInfo:export', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2129, '图片标签信息', 2080, 4, 'pictureTagInfo', 'picture/pictureTagInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'picture:pictureTagInfo:list', '#', 'admin', '2025-03-25 00:05:08', 'admin', '2025-06-03 22:05:24', '图片标签信息菜单');
INSERT INTO `sys_menu` VALUES (2130, '图片标签信息查询', 2129, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureTagInfo:query', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2131, '图片标签信息新增', 2129, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureTagInfo:add', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2132, '图片标签信息修改', 2129, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureTagInfo:edit', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2133, '图片标签信息删除', 2129, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureTagInfo:remove', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2134, '图片标签信息导出', 2129, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureTagInfo:export', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2135, '图片标签关联', 2080, 5, 'pictureTagRelInfo', 'picture/pictureTagRelInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'picture:pictureTagRelInfo:list', '#', 'admin', '2025-03-25 00:05:08', 'admin', '2025-05-24 23:03:16', '图片标签关联菜单');
INSERT INTO `sys_menu` VALUES (2136, '图片标签关联查询', 2135, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureTagRelInfo:query', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2137, '图片标签关联新增', 2135, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureTagRelInfo:add', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2138, '图片标签关联修改', 2135, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureTagRelInfo:edit', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2139, '图片标签关联删除', 2135, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureTagRelInfo:remove', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2140, '图片标签关联导出', 2135, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureTagRelInfo:export', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2141, '用户搜索记录', 2080, 9, 'searchLogInfo', 'picture/searchLogInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'picture:searchLogInfo:list', '#', 'admin', '2025-03-25 00:05:08', 'admin', '2025-05-24 23:03:37', '用户搜索记录菜单');
INSERT INTO `sys_menu` VALUES (2142, '用户搜索记录查询', 2141, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:searchLogInfo:query', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2143, '用户搜索记录新增', 2141, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:searchLogInfo:add', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2144, '用户搜索记录修改', 2141, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:searchLogInfo:edit', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2145, '用户搜索记录删除', 2141, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:searchLogInfo:remove', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2146, '用户搜索记录导出', 2141, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:searchLogInfo:export', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2147, '空间文件夹', 2080, 3, 'spaceFolderInfo', 'picture/spaceFolderInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'picture:spaceFolderInfo:list', '#', 'admin', '2025-03-25 00:05:08', 'admin', '2025-06-03 22:05:08', '空间文件夹菜单');
INSERT INTO `sys_menu` VALUES (2148, '空间文件夹查询', 2147, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:spaceFolderInfo:query', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2149, '空间文件夹新增', 2147, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:spaceFolderInfo:add', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2150, '空间文件夹修改', 2147, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:spaceFolderInfo:edit', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2151, '空间文件夹删除', 2147, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:spaceFolderInfo:remove', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2152, '空间文件夹导出', 2147, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:spaceFolderInfo:export', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2153, '空间信息', 2080, 1, 'spaceInfo', 'picture/spaceInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'picture:spaceInfo:list', '#', 'admin', '2025-03-25 00:05:08', 'admin', '2025-05-24 23:02:57', '空间信息菜单');
INSERT INTO `sys_menu` VALUES (2154, '空间信息查询', 2153, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:spaceInfo:query', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2155, '空间信息新增', 2153, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:spaceInfo:add', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2156, '空间信息修改', 2153, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:spaceInfo:edit', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2157, '空间信息删除', 2153, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:spaceInfo:remove', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2158, '空间信息导出', 2153, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:spaceInfo:export', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2159, '空间成员邀请记录', 2080, 11, 'spaceInvitationInfo', 'picture/spaceInvitationInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'picture:spaceInvitationInfo:list', '#', 'admin', '2025-03-25 00:05:08', 'admin', '2025-06-03 22:06:15', '空间成员邀请记录菜单');
INSERT INTO `sys_menu` VALUES (2160, '空间成员邀请记录查询', 2159, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:spaceInvitationInfo:query', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2161, '空间成员邀请记录新增', 2159, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:spaceInvitationInfo:add', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2162, '空间成员邀请记录修改', 2159, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:spaceInvitationInfo:edit', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2163, '空间成员邀请记录删除', 2159, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:spaceInvitationInfo:remove', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2164, '空间成员邀请记录导出', 2159, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:spaceInvitationInfo:export', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2165, '空间成员信息', 2080, 10, 'spaceMemberInfo', 'picture/spaceMemberInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'picture:spaceMemberInfo:list', '#', 'admin', '2025-03-25 00:05:08', 'admin', '2025-06-03 22:06:11', '空间成员信息菜单');
INSERT INTO `sys_menu` VALUES (2166, '空间成员信息查询', 2165, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:spaceMemberInfo:query', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2167, '空间成员信息新增', 2165, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:spaceMemberInfo:add', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2168, '空间成员信息修改', 2165, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:spaceMemberInfo:edit', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2169, '空间成员信息删除', 2165, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:spaceMemberInfo:remove', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2170, '空间成员信息导出', 2165, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:spaceMemberInfo:export', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2177, '用户举报信息', 2080, 14, 'userReportInfo', 'picture/userReportInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'picture:userReportInfo:list', '#', 'admin', '2025-03-25 00:05:08', 'admin', '2025-06-03 22:06:32', '用户举报信息菜单');
INSERT INTO `sys_menu` VALUES (2178, '用户举报信息查询', 2177, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:userReportInfo:query', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2179, '用户举报信息新增', 2177, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:userReportInfo:add', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2180, '用户举报信息修改', 2177, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:userReportInfo:edit', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2181, '用户举报信息删除', 2177, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:userReportInfo:remove', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2182, '用户举报信息导出', 2177, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:userReportInfo:export', '#', 'admin', '2025-03-25 00:05:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2189, 'AI模块', 0, 8, 'aiManage', NULL, NULL, '', 1, 0, 'M', '0', '0', NULL, 'textarea', 'admin', '2025-03-25 19:38:11', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2190, 'AI对话明细记录', 2189, 3, 'conversationLogInfo', 'ai/conversationLogInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'ai:conversationLogInfo:list', '#', 'admin', '2025-03-25 20:01:45', 'admin', '2025-08-08 21:45:13', 'AI对话明细记录菜单');
INSERT INTO `sys_menu` VALUES (2191, 'AI对话明细记录查询', 2190, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:conversationLogInfo:query', '#', 'admin', '2025-03-25 20:01:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2192, 'AI对话明细记录新增', 2190, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:conversationLogInfo:add', '#', 'admin', '2025-03-25 20:01:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2193, 'AI对话明细记录修改', 2190, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:conversationLogInfo:edit', '#', 'admin', '2025-03-25 20:01:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2194, 'AI对话明细记录删除', 2190, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:conversationLogInfo:remove', '#', 'admin', '2025-03-25 20:01:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2195, 'AI对话明细记录导出', 2190, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:conversationLogInfo:export', '#', 'admin', '2025-03-25 20:01:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2196, 'AI会话管理', 2189, 5, 'conversationSessionInfo', 'ai/conversationSessionInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'ai:conversationSessionInfo:list', '#', 'admin', '2025-03-25 20:01:46', 'admin', '2025-08-08 21:45:39', 'AI会话管理菜单');
INSERT INTO `sys_menu` VALUES (2197, 'AI会话管理查询', 2196, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:conversationSessionInfo:query', '#', 'admin', '2025-03-25 20:01:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2198, 'AI会话管理新增', 2196, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:conversationSessionInfo:add', '#', 'admin', '2025-03-25 20:01:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2199, 'AI会话管理修改', 2196, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:conversationSessionInfo:edit', '#', 'admin', '2025-03-25 20:01:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2200, 'AI会话管理删除', 2196, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:conversationSessionInfo:remove', '#', 'admin', '2025-03-25 20:01:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2201, 'AI会话管理导出', 2196, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:conversationSessionInfo:export', '#', 'admin', '2025-03-25 20:01:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2202, 'AI模型参数配置', 2189, 0, 'modelParamsInfo', 'ai/modelParamsInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'ai:modelParamsInfo:list', '#', 'admin', '2025-03-25 20:01:46', 'admin', '2025-08-08 21:44:58', 'AI模型参数配置菜单');
INSERT INTO `sys_menu` VALUES (2203, 'AI模型参数配置查询', 2202, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:modelParamsInfo:query', '#', 'admin', '2025-03-25 20:01:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2204, 'AI模型参数配置新增', 2202, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:modelParamsInfo:add', '#', 'admin', '2025-03-25 20:01:46', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2205, 'AI模型参数配置修改', 2202, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:modelParamsInfo:edit', '#', 'admin', '2025-03-25 20:01:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2206, 'AI模型参数配置删除', 2202, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:modelParamsInfo:remove', '#', 'admin', '2025-03-25 20:01:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2207, 'AI模型参数配置导出', 2202, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:modelParamsInfo:export', '#', 'admin', '2025-03-25 20:01:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2208, '官方AI操作日志', 2189, 6, 'officialUsageLogInfo', 'ai/officialUsageLogInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'ai:officialUsageLogInfo:list', '#', 'admin', '2025-03-25 20:01:47', 'admin', '2025-08-08 21:45:33', '官方AI操作日志菜单');
INSERT INTO `sys_menu` VALUES (2209, '官方AI操作日志查询', 2208, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:officialUsageLogInfo:query', '#', 'admin', '2025-03-25 20:01:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2210, '官方AI操作日志新增', 2208, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:officialUsageLogInfo:add', '#', 'admin', '2025-03-25 20:01:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2211, '官方AI操作日志修改', 2208, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:officialUsageLogInfo:edit', '#', 'admin', '2025-03-25 20:01:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2212, '官方AI操作日志删除', 2208, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:officialUsageLogInfo:remove', '#', 'admin', '2025-03-25 20:01:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2213, '官方AI操作日志导出', 2208, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:officialUsageLogInfo:export', '#', 'admin', '2025-03-25 20:01:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2214, '用户AI使用记录', 2189, 4, 'userUsageLogInfo', 'ai/userUsageLogInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'ai:userUsageLogInfo:list', '#', 'admin', '2025-03-25 20:01:47', 'admin', '2025-08-08 21:45:20', '用户AI使用记录菜单');
INSERT INTO `sys_menu` VALUES (2215, '用户AI使用记录查询', 2214, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:userUsageLogInfo:query', '#', 'admin', '2025-03-25 20:01:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2216, '用户AI使用记录新增', 2214, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:userUsageLogInfo:add', '#', 'admin', '2025-03-25 20:01:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2217, '用户AI使用记录修改', 2214, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:userUsageLogInfo:edit', '#', 'admin', '2025-03-25 20:01:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2218, '用户AI使用记录删除', 2214, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:userUsageLogInfo:remove', '#', 'admin', '2025-03-25 20:01:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2219, '用户AI使用记录导出', 2214, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:userUsageLogInfo:export', '#', 'admin', '2025-03-25 20:01:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2220, '积分模块', 0, 9, 'pointsManage', NULL, NULL, '', 1, 0, 'M', '0', '0', '', 'input', 'admin', '2025-03-25 20:16:25', 'admin', '2025-03-25 21:01:40', '');
INSERT INTO `sys_menu` VALUES (2221, '积分账户', 2220, 0, 'accountInfo', 'points/accountInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'points:accountInfo:list', '#', 'admin', '2025-03-25 20:59:49', 'admin', '2025-05-17 17:38:14', '积分账户菜单');
INSERT INTO `sys_menu` VALUES (2222, '积分账户查询', 2221, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:accountInfo:query', '#', 'admin', '2025-03-25 20:59:49', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2223, '积分账户新增', 2221, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:accountInfo:add', '#', 'admin', '2025-03-25 20:59:49', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2224, '积分账户修改', 2221, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:accountInfo:edit', '#', 'admin', '2025-03-25 20:59:49', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2225, '积分账户删除', 2221, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:accountInfo:remove', '#', 'admin', '2025-03-25 20:59:49', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2226, '积分账户导出', 2221, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:accountInfo:export', '#', 'admin', '2025-03-25 20:59:49', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2227, '异常捕获', 2220, 7, 'errorLogInfo', 'points/errorLogInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'points:errorLogInfo:list', '#', 'admin', '2025-03-25 20:59:49', 'admin', '2025-05-17 17:39:36', '异常捕获菜单');
INSERT INTO `sys_menu` VALUES (2228, '异常捕获查询', 2227, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:errorLogInfo:query', '#', 'admin', '2025-03-25 20:59:49', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2229, '异常捕获新增', 2227, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:errorLogInfo:add', '#', 'admin', '2025-03-25 20:59:49', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2230, '异常捕获修改', 2227, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:errorLogInfo:edit', '#', 'admin', '2025-03-25 20:59:49', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2231, '异常捕获删除', 2227, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:errorLogInfo:remove', '#', 'admin', '2025-03-25 20:59:49', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2232, '异常捕获导出', 2227, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:errorLogInfo:export', '#', 'admin', '2025-03-25 20:59:50', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2233, '支付方式', 2220, 8, 'paymentMethodInfo', 'points/paymentMethodInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'points:paymentMethodInfo:list', '#', 'admin', '2025-03-25 20:59:50', 'admin', '2025-05-17 17:39:40', '支付方式菜单');
INSERT INTO `sys_menu` VALUES (2234, '支付方式查询', 2233, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:paymentMethodInfo:query', '#', 'admin', '2025-03-25 20:59:50', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2235, '支付方式新增', 2233, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:paymentMethodInfo:add', '#', 'admin', '2025-03-25 20:59:50', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2236, '支付方式修改', 2233, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:paymentMethodInfo:edit', '#', 'admin', '2025-03-25 20:59:50', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2237, '支付方式删除', 2233, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:paymentMethodInfo:remove', '#', 'admin', '2025-03-25 20:59:50', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2238, '支付方式导出', 2233, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:paymentMethodInfo:export', '#', 'admin', '2025-03-25 20:59:50', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2239, '支付订单', 2220, 2, 'paymentOrderInfo', 'points/paymentOrderInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'points:paymentOrderInfo:list', '#', 'admin', '2025-03-25 20:59:50', 'admin', '2025-05-17 17:38:25', '支付订单菜单');
INSERT INTO `sys_menu` VALUES (2240, '支付订单查询', 2239, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:paymentOrderInfo:query', '#', 'admin', '2025-03-25 20:59:50', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2241, '支付订单新增', 2239, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:paymentOrderInfo:add', '#', 'admin', '2025-03-25 20:59:50', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2242, '支付订单修改', 2239, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:paymentOrderInfo:edit', '#', 'admin', '2025-03-25 20:59:50', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2243, '支付订单删除', 2239, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:paymentOrderInfo:remove', '#', 'admin', '2025-03-25 20:59:50', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2244, '支付订单导出', 2239, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:paymentOrderInfo:export', '#', 'admin', '2025-03-25 20:59:50', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2245, '积分充值记录', 2220, 3, 'pointsRechargeInfo', 'points/pointsRechargeInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'points:pointsRechargeInfo:list', '#', 'admin', '2025-03-25 20:59:50', 'admin', '2025-05-17 17:39:01', '积分充值记录菜单');
INSERT INTO `sys_menu` VALUES (2246, '积分充值记录查询', 2245, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:pointsRechargeInfo:query', '#', 'admin', '2025-03-25 20:59:50', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2247, '积分充值记录新增', 2245, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:pointsRechargeInfo:add', '#', 'admin', '2025-03-25 20:59:50', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2248, '积分充值记录修改', 2245, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:pointsRechargeInfo:edit', '#', 'admin', '2025-03-25 20:59:51', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2249, '积分充值记录删除', 2245, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:pointsRechargeInfo:remove', '#', 'admin', '2025-03-25 20:59:51', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2250, '积分充值记录导出', 2245, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:pointsRechargeInfo:export', '#', 'admin', '2025-03-25 20:59:51', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2251, '充值积分套餐', 2220, 1, 'pointsRechargePackageInfo', 'points/pointsRechargePackageInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'points:pointsRechargePackageInfo:list', '#', 'admin', '2025-03-25 20:59:51', '', NULL, '充值积分套餐菜单');
INSERT INTO `sys_menu` VALUES (2252, '充值积分套餐查询', 2251, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:pointsRechargePackageInfo:query', '#', 'admin', '2025-03-25 20:59:51', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2253, '充值积分套餐新增', 2251, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:pointsRechargePackageInfo:add', '#', 'admin', '2025-03-25 20:59:51', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2254, '充值积分套餐修改', 2251, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:pointsRechargePackageInfo:edit', '#', 'admin', '2025-03-25 20:59:51', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2255, '充值积分套餐删除', 2251, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:pointsRechargePackageInfo:remove', '#', 'admin', '2025-03-25 20:59:51', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2256, '充值积分套餐导出', 2251, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:pointsRechargePackageInfo:export', '#', 'admin', '2025-03-25 20:59:51', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2263, '风控日志', 2220, 6, 'riskControlLogInfo', 'points/riskControlLogInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'points:riskControlLogInfo:list', '#', 'admin', '2025-03-25 20:59:51', 'admin', '2025-05-17 17:39:28', '风控日志菜单');
INSERT INTO `sys_menu` VALUES (2264, '风控日志查询', 2263, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:riskControlLogInfo:query', '#', 'admin', '2025-03-25 20:59:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2265, '风控日志新增', 2263, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:riskControlLogInfo:add', '#', 'admin', '2025-03-25 20:59:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2266, '风控日志修改', 2263, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:riskControlLogInfo:edit', '#', 'admin', '2025-03-25 20:59:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2267, '风控日志删除', 2263, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:riskControlLogInfo:remove', '#', 'admin', '2025-03-25 20:59:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2268, '风控日志导出', 2263, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:riskControlLogInfo:export', '#', 'admin', '2025-03-25 20:59:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2269, '用户提现记录', 2220, 5, 'withdrawalOrderInfo', 'points/withdrawalOrderInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'points:withdrawalOrderInfo:list', '#', 'admin', '2025-03-25 20:59:52', 'admin', '2025-05-17 17:39:22', '用户提现记录菜单');
INSERT INTO `sys_menu` VALUES (2270, '用户提现记录查询', 2269, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:withdrawalOrderInfo:query', '#', 'admin', '2025-03-25 20:59:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2271, '用户提现记录新增', 2269, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:withdrawalOrderInfo:add', '#', 'admin', '2025-03-25 20:59:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2272, '用户提现记录修改', 2269, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:withdrawalOrderInfo:edit', '#', 'admin', '2025-03-25 20:59:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2273, '用户提现记录删除', 2269, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:withdrawalOrderInfo:remove', '#', 'admin', '2025-03-25 20:59:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2274, '用户提现记录导出', 2269, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:withdrawalOrderInfo:export', '#', 'admin', '2025-03-25 20:59:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2275, '菜单信息', 2000, 1, 'menuInfo', 'config/menuInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'config:menuInfo:list', '#', 'admin', '2025-03-30 23:51:37', '', NULL, '菜单信息菜单');
INSERT INTO `sys_menu` VALUES (2276, '菜单信息查询', 2275, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:menuInfo:query', '#', 'admin', '2025-03-30 23:51:37', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2277, '菜单信息新增', 2275, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:menuInfo:add', '#', 'admin', '2025-03-30 23:51:37', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2278, '菜单信息修改', 2275, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:menuInfo:edit', '#', 'admin', '2025-03-30 23:51:37', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2279, '菜单信息删除', 2275, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:menuInfo:remove', '#', 'admin', '2025-03-30 23:51:37', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2280, '菜单信息导出', 2275, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:menuInfo:export', '#', 'admin', '2025-03-30 23:51:37', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2281, '用户行为', 2080, 7, 'userBehaviorInfo', 'picture/userBehaviorInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'picture:userBehaviorInfo:list', '#', 'admin', '2025-04-12 21:43:39', 'admin', '2025-06-03 22:05:47', '用户行为菜单');
INSERT INTO `sys_menu` VALUES (2282, '用户行为查询', 2281, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:userBehaviorInfo:query', '#', 'admin', '2025-04-12 21:43:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2283, '用户行为新增', 2281, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:userBehaviorInfo:add', '#', 'admin', '2025-04-12 21:43:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2284, '用户行为修改', 2281, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:userBehaviorInfo:edit', '#', 'admin', '2025-04-12 21:43:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2285, '用户行为删除', 2281, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:userBehaviorInfo:remove', '#', 'admin', '2025-04-12 21:43:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2286, '用户行为导出', 2281, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:userBehaviorInfo:export', '#', 'admin', '2025-04-12 21:43:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2287, '用户浏览记录', 2080, 8, 'userViewLogInfo', 'picture/userViewLogInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'picture:userViewLogInfo:list', '#', 'admin', '2025-04-12 21:43:40', 'admin', '2025-06-03 22:05:55', '用户浏览记录菜单');
INSERT INTO `sys_menu` VALUES (2288, '用户浏览记录查询', 2287, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:userViewLogInfo:query', '#', 'admin', '2025-04-12 21:43:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2289, '用户浏览记录新增', 2287, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:userViewLogInfo:add', '#', 'admin', '2025-04-12 21:43:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2290, '用户浏览记录修改', 2287, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:userViewLogInfo:edit', '#', 'admin', '2025-04-12 21:43:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2291, '用户浏览记录删除', 2287, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:userViewLogInfo:remove', '#', 'admin', '2025-04-12 21:43:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2292, '用户浏览记录导出', 2287, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:userViewLogInfo:export', '#', 'admin', '2025-04-12 21:43:40', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2293, '文件日志', 2000, 6, 'fileLogInfo', 'config/fileLogInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'config:fileLogInfo:list', '#', 'admin', '2025-04-24 17:39:54', 'admin', '2025-04-24 17:41:17', '文件日志菜单');
INSERT INTO `sys_menu` VALUES (2294, '文件日志查询', 2293, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:fileLogInfo:query', '#', 'admin', '2025-04-24 17:39:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2295, '文件日志新增', 2293, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:fileLogInfo:add', '#', 'admin', '2025-04-24 17:39:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2296, '文件日志修改', 2293, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:fileLogInfo:edit', '#', 'admin', '2025-04-24 17:39:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2297, '文件日志删除', 2293, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:fileLogInfo:remove', '#', 'admin', '2025-04-24 17:39:55', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2298, '文件日志导出', 2293, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:fileLogInfo:export', '#', 'admin', '2025-04-24 17:39:56', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2299, '积分使用记录', 2220, 4, 'pointsUsageLogInfo', 'points/pointsUsageLogInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'points:pointsUsageLogInfo:list', '#', 'admin', '2025-05-23 23:00:46', 'admin', '2025-05-23 23:03:29', '积分使用记录菜单');
INSERT INTO `sys_menu` VALUES (2300, '积分使用记录查询', 2299, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:pointsUsageLogInfo:query', '#', 'admin', '2025-05-23 23:00:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2301, '积分使用记录新增', 2299, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:pointsUsageLogInfo:add', '#', 'admin', '2025-05-23 23:00:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2302, '积分使用记录修改', 2299, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:pointsUsageLogInfo:edit', '#', 'admin', '2025-05-23 23:00:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2303, '积分使用记录删除', 2299, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:pointsUsageLogInfo:remove', '#', 'admin', '2025-05-23 23:00:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2304, '积分使用记录导出', 2299, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'points:pointsUsageLogInfo:export', '#', 'admin', '2025-05-23 23:00:47', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2305, '图片下载记录', 2080, 6, 'pictureDownloadLogInfo', 'picture/pictureDownloadLogInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'picture:pictureDownloadLogInfo:list', '#', 'admin', '2025-05-24 23:01:51', 'admin', '2025-06-03 22:05:35', '图片下载记录菜单');
INSERT INTO `sys_menu` VALUES (2306, '图片下载记录查询', 2305, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureDownloadLogInfo:query', '#', 'admin', '2025-05-24 23:01:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2307, '图片下载记录新增', 2305, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureDownloadLogInfo:add', '#', 'admin', '2025-05-24 23:01:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2308, '图片下载记录修改', 2305, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureDownloadLogInfo:edit', '#', 'admin', '2025-05-24 23:01:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2309, '图片下载记录删除', 2305, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureDownloadLogInfo:remove', '#', 'admin', '2025-05-24 23:01:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2310, '图片下载记录导出', 2305, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureDownloadLogInfo:export', '#', 'admin', '2025-05-24 23:01:53', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2311, '用户图片推荐模型', 2080, 16, 'pictureRecommendInfo', 'picture/pictureRecommendInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'picture:pictureRecommendInfo:list', '#', 'admin', '2025-06-10 00:38:27', 'admin', '2025-06-17 23:59:45', '用户图片推荐模型菜单');
INSERT INTO `sys_menu` VALUES (2312, '用户图片推荐模型查询', 2311, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureRecommendInfo:query', '#', 'admin', '2025-06-10 00:38:28', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2313, '用户图片推荐模型新增', 2311, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureRecommendInfo:add', '#', 'admin', '2025-06-10 00:38:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2314, '用户图片推荐模型修改', 2311, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureRecommendInfo:edit', '#', 'admin', '2025-06-10 00:38:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2315, '用户图片推荐模型删除', 2311, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureRecommendInfo:remove', '#', 'admin', '2025-06-10 00:38:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2316, '用户图片推荐模型导出', 2311, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureRecommendInfo:export', '#', 'admin', '2025-06-10 00:38:29', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2317, '用户举报信息审核', 2177, 6, '', NULL, NULL, '', 1, 0, 'F', '0', '0', 'picture:userReportInfo:audit', '#', 'admin', '2025-06-17 22:14:35', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2318, '图片申请信息', 2080, 15, 'pictureApplyInfo', 'picture/pictureApplyInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'picture:pictureApplyInfo:list', '#', 'admin', '2025-06-17 23:59:08', 'admin', '2025-06-17 23:59:37', '图片申请信息菜单');
INSERT INTO `sys_menu` VALUES (2319, '图片申请信息查询', 2318, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureApplyInfo:query', '#', 'admin', '2025-06-17 23:59:09', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2320, '图片申请信息新增', 2318, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureApplyInfo:add', '#', 'admin', '2025-06-17 23:59:09', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2321, '图片申请信息修改', 2318, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureApplyInfo:edit', '#', 'admin', '2025-06-17 23:59:10', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2322, '图片申请信息删除', 2318, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureApplyInfo:remove', '#', 'admin', '2025-06-17 23:59:10', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2323, '图片申请信息导出', 2318, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:pictureApplyInfo:export', '#', 'admin', '2025-06-17 23:59:10', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2324, '空间扩容信息', 2080, 17, 'spaceDilatationInfo', 'picture/spaceDilatationInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'picture:spaceDilatationInfo:list', '#', 'admin', '2025-06-28 00:56:59', 'admin', '2025-06-28 00:57:51', '空间扩容信息菜单');
INSERT INTO `sys_menu` VALUES (2325, '空间扩容信息查询', 2324, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:spaceDilatationInfo:query', '#', 'admin', '2025-06-28 00:57:00', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2326, '空间扩容信息新增', 2324, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:spaceDilatationInfo:add', '#', 'admin', '2025-06-28 00:57:00', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2327, '空间扩容信息修改', 2324, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:spaceDilatationInfo:edit', '#', 'admin', '2025-06-28 00:57:00', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2328, '空间扩容信息删除', 2324, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:spaceDilatationInfo:remove', '#', 'admin', '2025-06-28 00:57:00', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2329, '空间扩容信息导出', 2324, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:spaceDilatationInfo:export', '#', 'admin', '2025-06-28 00:57:00', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2330, '统计信息查询', 2335, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:statisticsInfo:query', '#', 'admin', '2025-07-17 23:41:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2331, '统计信息新增', 2335, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:statisticsInfo:add', '#', 'admin', '2025-07-17 23:41:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2332, '统计信息修改', 2335, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:statisticsInfo:edit', '#', 'admin', '2025-07-17 23:41:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2333, '统计信息删除', 2335, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:statisticsInfo:remove', '#', 'admin', '2025-07-17 23:41:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2334, '统计信息导出', 2335, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'picture:statisticsInfo:export', '#', 'admin', '2025-07-17 23:41:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2335, '统计信息', 2080, 18, 'statisticsInfo', 'picture/statisticsInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'picture:statisticsInfo:list', '#', 'admin', '2025-07-17 23:41:29', 'admin', '2025-07-17 23:48:37', '统计信息菜单');
INSERT INTO `sys_menu` VALUES (2336, '操作工具', 0, 4, 'convenientTool', NULL, NULL, '', 1, 0, 'M', '0', '0', NULL, 'guide', 'admin', '2025-07-24 21:35:19', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2337, '常用操作', 2336, 0, 'commonOperations', 'convenient/common/index', NULL, '', 1, 0, 'C', '0', '0', 'convenient:common:index', 'size', 'admin', '2025-07-24 21:38:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2338, '用户公告', 2000, 7, 'noticeInfo', 'config/noticeInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'config:noticeInfo:list', '#', 'admin', '2025-07-26 17:20:01', 'admin', '2025-07-26 17:26:47', '用户公告菜单');
INSERT INTO `sys_menu` VALUES (2339, '用户公告查询', 2338, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:noticeInfo:query', '#', 'admin', '2025-07-26 17:20:02', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2340, '用户公告新增', 2338, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:noticeInfo:add', '#', 'admin', '2025-07-26 17:20:02', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2341, '用户公告修改', 2338, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:noticeInfo:edit', '#', 'admin', '2025-07-26 17:20:02', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2342, '用户公告删除', 2338, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:noticeInfo:remove', '#', 'admin', '2025-07-26 17:20:02', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2343, '用户公告导出', 2338, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'config:noticeInfo:export', '#', 'admin', '2025-07-26 17:20:02', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2344, '私有图片', 2080, 2, 'pictureInfoPrivate', 'picture/pictureInfo/index', '{\"pictureStatus\":\"1\"}', '', 1, 0, 'C', '0', '0', 'picture:pictureInfo:list', '#', 'admin', '2025-03-25 00:05:08', 'admin', '2025-07-28 23:52:56', '图片信息菜单');
INSERT INTO `sys_menu` VALUES (2345, '公共图片', 2080, 2, 'pictureInfoPublic', 'picture/pictureInfo/index', '{\"pictureStatus\":\"0\"}', '', 1, 0, 'C', '0', '0', 'picture:pictureInfo:list', '#', 'admin', '2025-03-25 00:05:08', 'admin', '2025-07-28 23:52:56', '图片信息菜单');
INSERT INTO `sys_menu` VALUES (2346, '用户生成记录', 2189, 2, 'generateLogInfo', 'ai/generateLogInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'ai:generateLogInfo:list', '#', 'admin', '2025-08-08 21:42:05', 'admin', '2025-08-08 21:45:07', '用户生成记录菜单');
INSERT INTO `sys_menu` VALUES (2347, '用户生成记录查询', 2346, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:generateLogInfo:query', '#', 'admin', '2025-08-08 21:42:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2348, '用户生成记录新增', 2346, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:generateLogInfo:add', '#', 'admin', '2025-08-08 21:42:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2349, '用户生成记录修改', 2346, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:generateLogInfo:edit', '#', 'admin', '2025-08-08 21:42:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2350, '用户生成记录删除', 2346, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:generateLogInfo:remove', '#', 'admin', '2025-08-08 21:42:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2351, '用户生成记录导出', 2346, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:generateLogInfo:export', '#', 'admin', '2025-08-08 21:42:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2352, '提示词信息', 2189, 1, 'promptInfo', 'ai/promptInfo/index', NULL, '', 1, 0, 'C', '0', '0', 'ai:promptInfo:list', '#', 'admin', '2025-08-08 21:42:06', '', NULL, '提示词信息菜单');
INSERT INTO `sys_menu` VALUES (2353, '提示词信息查询', 2352, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:promptInfo:query', '#', 'admin', '2025-08-08 21:42:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2354, '提示词信息新增', 2352, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:promptInfo:add', '#', 'admin', '2025-08-08 21:42:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2355, '提示词信息修改', 2352, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:promptInfo:edit', '#', 'admin', '2025-08-08 21:42:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2356, '提示词信息删除', 2352, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:promptInfo:remove', '#', 'admin', '2025-08-08 21:42:06', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2357, '提示词信息导出', 2352, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'ai:promptInfo:export', '#', 'admin', '2025-08-08 21:42:06', '', NULL, '');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` int NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob NULL COMMENT '公告内容',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '通知公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, '温馨提醒：2018-07-01 若依新版本发布啦', '2', 0xE696B0E78988E69CACE58685E5AEB9, '0', 'admin', '2025-02-28 14:53:20', '', NULL, '管理员');
INSERT INTO `sys_notice` VALUES (2, '维护通知：2018-07-01 若依系统凌晨维护', '1', 0xE7BBB4E68AA4E58685E5AEB9, '0', 'admin', '2025-02-28 14:53:20', '', NULL, '管理员');

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '模块标题',
  `business_type` int NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求方式',
  `operator_type` int NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '返回参数',
  `status` int NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint NULL DEFAULT 0 COMMENT '消耗时间',
  PRIMARY KEY (`oper_id`) USING BTREE,
  INDEX `idx_sys_oper_log_bt`(`business_type` ASC) USING BTREE,
  INDEX `idx_sys_oper_log_s`(`status` ASC) USING BTREE,
  INDEX `idx_sys_oper_log_ot`(`oper_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2107 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '操作日志记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int NOT NULL COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '岗位信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'ceo', '董事长', 1, '0', 'admin', '2025-02-28 14:53:16', '', NULL, '');
INSERT INTO `sys_post` VALUES (2, 'se', '项目经理', 2, '0', 'admin', '2025-02-28 14:53:16', '', NULL, '');
INSERT INTO `sys_post` VALUES (3, 'hr', '人力资源', 3, '0', 'admin', '2025-02-28 14:53:16', '', NULL, '');
INSERT INTO `sys_post` VALUES (4, 'user', '普通员工', 4, '0', 'admin', '2025-02-28 14:53:16', '', NULL, '');
INSERT INTO `sys_post` VALUES (5, 't', 't', 0, '0', 'admin', '2025-07-27 20:43:25', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 101 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2025-02-28 14:53:16', '', NULL, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '2', 1, 1, '0', '0', 'admin', '2025-02-28 14:53:16', '', NULL, '普通角色');
INSERT INTO `sys_role` VALUES (100, 't', 't', 0, '1', 1, 1, '0', '0', 'admin', '2025-07-27 20:21:14', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色和部门关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES (2, 100);
INSERT INTO `sys_role_dept` VALUES (2, 101);
INSERT INTO `sys_role_dept` VALUES (2, 105);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2, 1);
INSERT INTO `sys_role_menu` VALUES (2, 2);
INSERT INTO `sys_role_menu` VALUES (2, 3);
INSERT INTO `sys_role_menu` VALUES (2, 4);
INSERT INTO `sys_role_menu` VALUES (2, 100);
INSERT INTO `sys_role_menu` VALUES (2, 101);
INSERT INTO `sys_role_menu` VALUES (2, 102);
INSERT INTO `sys_role_menu` VALUES (2, 103);
INSERT INTO `sys_role_menu` VALUES (2, 104);
INSERT INTO `sys_role_menu` VALUES (2, 105);
INSERT INTO `sys_role_menu` VALUES (2, 106);
INSERT INTO `sys_role_menu` VALUES (2, 107);
INSERT INTO `sys_role_menu` VALUES (2, 108);
INSERT INTO `sys_role_menu` VALUES (2, 109);
INSERT INTO `sys_role_menu` VALUES (2, 110);
INSERT INTO `sys_role_menu` VALUES (2, 111);
INSERT INTO `sys_role_menu` VALUES (2, 112);
INSERT INTO `sys_role_menu` VALUES (2, 113);
INSERT INTO `sys_role_menu` VALUES (2, 114);
INSERT INTO `sys_role_menu` VALUES (2, 115);
INSERT INTO `sys_role_menu` VALUES (2, 116);
INSERT INTO `sys_role_menu` VALUES (2, 117);
INSERT INTO `sys_role_menu` VALUES (2, 500);
INSERT INTO `sys_role_menu` VALUES (2, 501);
INSERT INTO `sys_role_menu` VALUES (2, 1000);
INSERT INTO `sys_role_menu` VALUES (2, 1001);
INSERT INTO `sys_role_menu` VALUES (2, 1002);
INSERT INTO `sys_role_menu` VALUES (2, 1003);
INSERT INTO `sys_role_menu` VALUES (2, 1004);
INSERT INTO `sys_role_menu` VALUES (2, 1005);
INSERT INTO `sys_role_menu` VALUES (2, 1006);
INSERT INTO `sys_role_menu` VALUES (2, 1007);
INSERT INTO `sys_role_menu` VALUES (2, 1008);
INSERT INTO `sys_role_menu` VALUES (2, 1009);
INSERT INTO `sys_role_menu` VALUES (2, 1010);
INSERT INTO `sys_role_menu` VALUES (2, 1011);
INSERT INTO `sys_role_menu` VALUES (2, 1012);
INSERT INTO `sys_role_menu` VALUES (2, 1013);
INSERT INTO `sys_role_menu` VALUES (2, 1014);
INSERT INTO `sys_role_menu` VALUES (2, 1015);
INSERT INTO `sys_role_menu` VALUES (2, 1016);
INSERT INTO `sys_role_menu` VALUES (2, 1017);
INSERT INTO `sys_role_menu` VALUES (2, 1018);
INSERT INTO `sys_role_menu` VALUES (2, 1019);
INSERT INTO `sys_role_menu` VALUES (2, 1020);
INSERT INTO `sys_role_menu` VALUES (2, 1021);
INSERT INTO `sys_role_menu` VALUES (2, 1022);
INSERT INTO `sys_role_menu` VALUES (2, 1023);
INSERT INTO `sys_role_menu` VALUES (2, 1024);
INSERT INTO `sys_role_menu` VALUES (2, 1025);
INSERT INTO `sys_role_menu` VALUES (2, 1026);
INSERT INTO `sys_role_menu` VALUES (2, 1027);
INSERT INTO `sys_role_menu` VALUES (2, 1028);
INSERT INTO `sys_role_menu` VALUES (2, 1029);
INSERT INTO `sys_role_menu` VALUES (2, 1030);
INSERT INTO `sys_role_menu` VALUES (2, 1031);
INSERT INTO `sys_role_menu` VALUES (2, 1032);
INSERT INTO `sys_role_menu` VALUES (2, 1033);
INSERT INTO `sys_role_menu` VALUES (2, 1034);
INSERT INTO `sys_role_menu` VALUES (2, 1035);
INSERT INTO `sys_role_menu` VALUES (2, 1036);
INSERT INTO `sys_role_menu` VALUES (2, 1037);
INSERT INTO `sys_role_menu` VALUES (2, 1038);
INSERT INTO `sys_role_menu` VALUES (2, 1039);
INSERT INTO `sys_role_menu` VALUES (2, 1040);
INSERT INTO `sys_role_menu` VALUES (2, 1041);
INSERT INTO `sys_role_menu` VALUES (2, 1042);
INSERT INTO `sys_role_menu` VALUES (2, 1043);
INSERT INTO `sys_role_menu` VALUES (2, 1044);
INSERT INTO `sys_role_menu` VALUES (2, 1045);
INSERT INTO `sys_role_menu` VALUES (2, 1046);
INSERT INTO `sys_role_menu` VALUES (2, 1047);
INSERT INTO `sys_role_menu` VALUES (2, 1048);
INSERT INTO `sys_role_menu` VALUES (2, 1049);
INSERT INTO `sys_role_menu` VALUES (2, 1050);
INSERT INTO `sys_role_menu` VALUES (2, 1051);
INSERT INTO `sys_role_menu` VALUES (2, 1052);
INSERT INTO `sys_role_menu` VALUES (2, 1053);
INSERT INTO `sys_role_menu` VALUES (2, 1054);
INSERT INTO `sys_role_menu` VALUES (2, 1055);
INSERT INTO `sys_role_menu` VALUES (2, 1056);
INSERT INTO `sys_role_menu` VALUES (2, 1057);
INSERT INTO `sys_role_menu` VALUES (2, 1058);
INSERT INTO `sys_role_menu` VALUES (2, 1059);
INSERT INTO `sys_role_menu` VALUES (2, 1060);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 101 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 103, 'admin', '若依', '00', 'ry@163.com', '15888888888', '1', '/profile/avatar/2025/07/30/50b936c6136898cf57689ab4be404ea1_20250730202140A003.jpg', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2025-08-21 14:39:50', 'admin', '2025-02-28 14:53:16', '', '2025-08-21 14:39:49', '管理员');
INSERT INTO `sys_user` VALUES (2, 105, 'ry', '若依', '00', 'ry@qq.com', '15666666666', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2025-02-28 14:53:16', 'admin', '2025-02-28 14:53:16', '', NULL, '测试员');
INSERT INTO `sys_user` VALUES (100, NULL, 'test', 'test', '00', '', '', '0', '', '$2a$10$aUI076kkyG9hAScclNDiJeRw0h2wg4PFi.Jig36mhI9jRev8cd6wS', '0', '0', '', NULL, 'admin', '2025-07-27 19:38:22', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (1, 1);
INSERT INTO `sys_user_post` VALUES (2, 2);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户和角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2);

-- ----------------------------
-- Table structure for u_banned_permission_info
-- ----------------------------
DROP TABLE IF EXISTS `u_banned_permission_info`;
CREATE TABLE `u_banned_permission_info`  (
  `banned_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '封禁记录编号',
  `permission_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限名称',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '状态（0=封禁中 1=结束）',
  `cause` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封禁原因',
  PRIMARY KEY (`banned_id`) USING BTREE,
  INDEX `idx_banned_user`(`user_id` ASC) USING BTREE,
  INDEX `idx_banned_permission`(`permission_name` ASC) USING BTREE,
  CONSTRAINT `fk_banned_permission_name` FOREIGN KEY (`permission_name`) REFERENCES `c_permission_info` (`permission_name`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_banned_user_id` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_time_sequence` CHECK (`end_time` > `start_time`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户封禁权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of u_banned_permission_info
-- ----------------------------

-- ----------------------------
-- Table structure for u_inform_info
-- ----------------------------
DROP TABLE IF EXISTS `u_inform_info`;
CREATE TABLE `u_inform_info`  (
  `record_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知记录编号',
  `template_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模板KEY',
  `template_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模版类型（1短信 2邮件 3站内通知 4APP推送 5微信模板）',
  `locale` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '语言（默认zh-CN）',
  `inform_title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '通知标题',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '实际发送内容',
  `inform_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '发送状态（0=待发送 1=已发送 2=发送失败 3=已撤回）',
  `is_read` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '是否已读（0=未读 1=已读）',
  `read_time` datetime NULL DEFAULT NULL COMMENT '读取时间',
  `retry_count` int NOT NULL DEFAULT 0 COMMENT '重试次数',
  `send_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除（0=正常 1=删除）',
  PRIMARY KEY (`record_id`) USING BTREE,
  INDEX `idx_send_time`(`send_time` ASC) USING BTREE,
  INDEX `idx_user_status`(`user_id` ASC, `status` ASC) USING BTREE,
  INDEX `fk_inform_locale`(`locale` ASC) USING BTREE,
  CONSTRAINT `fk_inform_locale` FOREIGN KEY (`locale`) REFERENCES `c_i18n_locale_info` (`locale`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_inform_user` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户通知记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for u_login_log_info
-- ----------------------------
DROP TABLE IF EXISTS `u_login_log_info`;
CREATE TABLE `u_login_log_info`  (
  `info_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户ID',
  `user_name` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `login_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录方式',
  `identifier` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '匿名标识',
  `ipaddr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '登录平台',
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备唯一标识',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '状态（0成功 1失败）',
  `error_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '错误码',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '提示消息',
  `login_time` datetime NOT NULL COMMENT '登录时间',
  PRIMARY KEY (`info_id`) USING BTREE,
  INDEX `idx_login_time`(`login_time` ASC) USING BTREE,
  INDEX `idx_platform_device`(`platform` ASC, `device_id`(64) ASC) USING BTREE,
  INDEX `idx_user_status`(`user_id` ASC, `status` ASC) USING BTREE,
  CONSTRAINT `u_login_log_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户登录日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for u_user_binding_info
-- ----------------------------
DROP TABLE IF EXISTS `u_user_binding_info`;
CREATE TABLE `u_user_binding_info`  (
  `binding_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '绑定ID',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `binding_type` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '绑定类型',
  `identifier` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '第三方唯一标识',
  `extend_config` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '扩展配置',
  `binding_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '绑定时间',
  PRIMARY KEY (`binding_id`) USING BTREE,
  UNIQUE INDEX `uniq_user_binding`(`user_id` ASC, `binding_type` ASC) USING BTREE,
  UNIQUE INDEX `uniq_identifier`(`identifier` ASC, `binding_type` ASC) USING BTREE,
  INDEX `idx_binding_type`(`binding_type` ASC) USING BTREE,
  CONSTRAINT `fk_binding_user` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户第三方账号绑定表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of u_user_binding_info
-- ----------------------------

-- ----------------------------
-- Table structure for u_user_friend_info
-- ----------------------------
DROP TABLE IF EXISTS `u_user_friend_info`;
CREATE TABLE `u_user_friend_info`  (
  `relation_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关系ID',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `friend_user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '好友用户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`relation_id`) USING BTREE,
  UNIQUE INDEX `uniq_user_friend`(`user_id` ASC, `friend_user_id` ASC) USING BTREE,
  INDEX `idx_friend_user`(`friend_user_id` ASC) USING BTREE,
  CONSTRAINT `fk_friend_target_user` FOREIGN KEY (`friend_user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_friend_user` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户好友关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of u_user_friend_info
-- ----------------------------

-- ----------------------------
-- Table structure for u_user_info
-- ----------------------------
DROP TABLE IF EXISTS `u_user_info`;
CREATE TABLE `u_user_info`  (
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `user_name` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `country_code` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '+86' COMMENT '国家代码',
  `nick_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '昵称',
  `avatar_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像地址',
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '状态（0=正常 1=异常 2=禁用）',
  `salt` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '加密盐',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '性别（0=未知 1=男 2=女）',
  `birthday` datetime NULL DEFAULT NULL COMMENT '生日',
  `occupation` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'unknown' COMMENT '职业',
  `preferred_language_locale` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '偏好语言',
  `introductory` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '个人简介',
  `ip_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP属地',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最后登录IP',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除标记（0=未删除 1=已删除）',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `uk_user_name`(`user_name` ASC) USING BTREE,
  INDEX `idx_phone`(`phone` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for u_user_relation_info
-- ----------------------------
DROP TABLE IF EXISTS `u_user_relation_info`;
CREATE TABLE `u_user_relation_info`  (
  `relation_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关系ID',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `relation_user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联用户ID',
  `relation_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关系类型（0=关注 1=互关 2=拉黑）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`relation_id`) USING BTREE,
  INDEX `idx_relation_user`(`user_id` ASC) USING BTREE,
  INDEX `idx_relation_target`(`relation_user_id` ASC) USING BTREE,
  INDEX `idx_relation_type`(`relation_type` ASC) USING BTREE,
  CONSTRAINT `fk_relation_target_user` FOREIGN KEY (`relation_user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_relation_user` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of u_user_relation_info
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
