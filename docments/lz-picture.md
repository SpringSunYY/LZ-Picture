# 数据库表结构文档（直接从数据库读取）

**数据库**: `lz-picture`
**生成时间**: 自动生成

### AI对话明细记录表：`ai_conversation_log_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| conversation_id | varchar | 128 | 主键 | 否 |  | 对话记录编号 |
| session_id | varchar | 128 | 索引 | 否 |  | 会话编号 |
| user_id | varchar | 128 | 索引 | 否 |  | 用户编号 |
| input_text | text |  |  | 否 |  | 用户输入文本 |
| output_text | text |  |  | 否 |  | AI返回文本 |
| request_time | datetime |  | 索引 | 否 | CURRENT_TIMESTAMP | 请求时间 |
| response_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 响应时间 |
| tokens_used | int |  |  | 否 | 0 | 消耗Tokens数量 |
| points_used | int |  |  | 否 | 0 | 消耗积分 |
| conversation_status | char | 1 |  | 否 |  | 状态（0=成功 1=失败） |
| ai_status_code | varchar | 16 |  | 是 | NULL | 模型返回码 |
| fail_reason | varchar | 128 |  | 是 | NULL | 失败原因 |
| conversation_type | varchar | 50 | 索引 | 否 |  | 对话类型（0文本 1图片） |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 创建时间 |

```sql
CREATE TABLE `ai_conversation_log_info` (
  `conversation_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '对话记录编号',
  `session_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会话编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `input_text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户输入文本',
  `output_text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'AI返回文本',
  `request_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '请求时间',
  `response_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '响应时间',
  `tokens_used` int NOT NULL DEFAULT '0' COMMENT '消耗Tokens数量',
  `points_used` int NOT NULL DEFAULT '0' COMMENT '消耗积分',
  `conversation_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态（0=成功 1=失败）',
  `ai_status_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '模型返回码',
  `fail_reason` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '失败原因',
  `conversation_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '对话类型（0文本 1图片）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`conversation_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `idx_session` (`session_id`) USING BTREE,
  KEY `idx_conversation_type` (`conversation_type`) USING BTREE,
  KEY `idx_request_time` (`request_time`) USING BTREE,
  CONSTRAINT `ai_conversation_log_info_ibfk_1` FOREIGN KEY (`session_id`) REFERENCES `ai_conversation_session_info` (`session_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `ai_conversation_log_info_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='AI对话明细记录表'
```

### AI会话管理表：`ai_conversation_session_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| session_id | varchar | 128 | 主键 | 否 |  | 会话编号 |
| user_id | varchar | 128 | 索引 | 否 |  | 用户编号 |
| conversation_id | varchar | 128 |  | 是 | NULL | AI会话编号 |
| session_name | varchar | 32 |  | 是 | NULL | 对话名称 |
| tokens_total_used | int |  |  | 否 | 0 | 累计消耗Tokens |
| points_total_used | int |  |  | 否 | 0 | 累计消耗积分 |
| remark | varchar | 512 |  | 是 | NULL | 备注 |
| ip_addr | varchar | 50 |  | 否 |  | 用户IP地址 |
| device_id | varchar | 255 |  | 是 | NULL | 设备唯一标识 |
| browser | varchar | 50 |  | 是 | NULL | 浏览器类型 |
| os | varchar | 50 |  | 是 | NULL | 操作系统 |
| platform | varchar | 20 |  | 是 | NULL | 平台 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 创建时间 |
| update_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 更新时间 |
| is_delete | char | 1 |  | 否 | 0 | 删除（0正常 1删除） |

```sql
CREATE TABLE `ai_conversation_session_info` (
  `session_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '会话编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `conversation_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'AI会话编号',
  `session_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '对话名称',
  `tokens_total_used` int NOT NULL DEFAULT '0' COMMENT '累计消耗Tokens',
  `points_total_used` int NOT NULL DEFAULT '0' COMMENT '累计消耗积分',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户IP地址',
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '平台',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除（0正常 1删除）',
  PRIMARY KEY (`session_id`) USING BTREE,
  KEY `idx_user_session` (`user_id`,`session_id`) USING BTREE,
  CONSTRAINT `ai_conversation_session_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='AI会话管理表'
```

### 用户生成记录表：`ai_generate_log_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| log_id | varchar | 128 | 主键 | 否 |  | 记录编号 |
| user_id | varchar | 128 | 索引 | 否 |  | 用户编号 |
| model_key | varchar | 128 | 索引 | 否 |  | 模型KEY |
| model_type | varchar | 50 |  | 否 |  | 模型类型 |
| input_file | text |  |  | 是 | NULL | 输入文件 |
| prompt | text |  |  | 否 |  | 提示词 |
| negative_prompt | text |  |  | 是 | NULL | 负向提示词 |
| seed | float |  |  | 是 | NULL | 随机种子 |
| numbers | int |  |  | 是 | NULL | 数量 |
| input_params | text |  |  | 是 | NULL | 输入参数 |
| task_id | varchar | 128 |  | 否 |  | 任务编号 |
| output_result | text |  |  | 是 | NULL | 返回结果 |
| file_urls | text |  |  | 是 | NULL | 文件地址 |
| width | int |  |  | 是 | NULL | 宽度 |
| height | int |  |  | 是 | NULL | 高度 |
| file_size | bigint |  |  | 否 | 0 | 文件大小 |
| request_time | datetime |  |  | 否 |  | 请求时间 |
| request_duration | bigint |  |  | 是 | NULL | 请求时长 |
| price_used | decimal | 5,2 |  | 否 | 0.00 | 价格 |
| points_used | int |  |  | 否 | 0 | 消耗的积分 |
| target_id | varchar | 128 |  | 是 | NULL | 参考对象 |
| log_status | char | 1 |  | 否 |  | 状态 |
| ai_status_code | varchar | 16 |  | 是 | NULL | 模型返回码 |
| fail_reason | varchar | 128 |  | 是 | NULL | 失败原因 |
| has_public | char | 1 |  | 否 | 1 | 是否发布 |
| has_statistics | char | 1 |  | 否 |  | 是否统计 |
| ip_addr | varchar | 50 |  | 否 |  | 用户IP地址 |
| device_id | varchar | 255 |  | 是 | NULL | 用户设备唯一标识 |
| browser | varchar | 50 |  | 是 | NULL | 浏览器类型 |
| os | varchar | 50 |  | 是 | NULL | 操作系统 |
| platform | varchar | 20 |  | 是 | NULL | 平台 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 创建时间 |
| update_time | datetime |  |  | 是 | CURRENT_TIMESTAMP | 更新时间 |
| is_delete | char | 1 |  | 否 | 0 | 删除 |

```sql
CREATE TABLE `ai_generate_log_info` (
  `log_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '记录编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `model_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型KEY',
  `model_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型类型',
  `input_file` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '输入文件',
  `prompt` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '提示词',
  `negative_prompt` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '负向提示词',
  `seed` float DEFAULT NULL COMMENT '随机种子',
  `numbers` int DEFAULT NULL COMMENT '数量',
  `input_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '输入参数',
  `task_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务编号',
  `output_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '返回结果',
  `file_urls` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '文件地址',
  `width` int DEFAULT NULL COMMENT '宽度',
  `height` int DEFAULT NULL COMMENT '高度',
  `file_size` bigint NOT NULL DEFAULT '0' COMMENT '文件大小',
  `request_time` datetime NOT NULL COMMENT '请求时间',
  `request_duration` bigint DEFAULT NULL COMMENT '请求时长',
  `price_used` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '价格',
  `points_used` int NOT NULL DEFAULT '0' COMMENT '消耗的积分',
  `target_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '参考对象',
  `log_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态',
  `ai_status_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '模型返回码',
  `fail_reason` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '失败原因',
  `has_public` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '是否发布',
  `has_statistics` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '是否统计',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户IP地址',
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '平台',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除',
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `fk_generate_user_id_log` (`user_id`) USING BTREE,
  KEY `fk_generate_model_key_log` (`model_key`) USING BTREE,
  CONSTRAINT `fk_generate_model_key_log` FOREIGN KEY (`model_key`) REFERENCES `ai_model_params_info` (`model_key`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_generate_user_id_log` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户生成记录表'
```

### 模型参数表：`ai_model_params_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| model_id | varchar | 128 | 主键 | 否 |  | 模型编号 |
| model_key | varchar | 128 | 唯一索引 | 否 |  | 模型KEY |
| model_name | varchar | 128 |  | 否 |  | 模型名称 |
| model_type | varchar | 50 |  | 否 |  | 模型类型 |
| model | varchar | 64 |  | 否 |  | 模型 |
| model_label | varchar | 128 |  | 否 |  | 名称 |
| api_url | varchar | 512 |  | 否 |  |  |
| api_key | varchar | 256 |  | 是 | NULL | 安全密钥 |
| secret_key | varchar | 256 |  | 是 | NULL | 安全KEY |
| price_use | decimal | 5,2 |  | 否 |  | 价格 |
| model_params | text |  |  | 否 |  | 模型参数 |
| model_description | varchar | 1024 |  | 是 | NULL | 模型介绍 |
| usage_count | int |  |  | 否 | 0 | 使用次数 |
| points_earned | bigint |  |  | 否 | 0 | 赚取积分 |
| points_need | int |  |  | 是 | 0 | 积分 |
| extend_config | varchar | 1024 |  | 是 | NULL | 扩展配置 |
| params_status | char | 1 |  | 否 | 1 | 状态 |
| order_num | int |  |  | 否 | 10 | 排序 |
| create_by | varchar | 32 |  | 否 |  | 创建人 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 创建时间 |
| update_by | varchar | 32 |  | 是 | NULL | 更新人 |
| update_time | datetime |  |  | 是 | CURRENT_TIMESTAMP | 更新时间 |
| remark | varchar | 512 |  | 是 | NULL | 备注 |

```sql
CREATE TABLE `ai_model_params_info` (
  `model_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型编号',
  `model_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型KEY',
  `model_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型名称',
  `model_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型类型',
  `model` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型',
  `model_label` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `api_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `api_key` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '安全密钥',
  `secret_key` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '安全KEY',
  `price_use` decimal(5,2) NOT NULL COMMENT '价格',
  `model_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型参数',
  `model_description` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '模型介绍',
  `usage_count` int NOT NULL DEFAULT '0' COMMENT '使用次数',
  `points_earned` bigint NOT NULL DEFAULT '0' COMMENT '赚取积分',
  `points_need` int DEFAULT '0' COMMENT '积分',
  `extend_config` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '扩展配置',
  `params_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '状态',
  `order_num` int NOT NULL DEFAULT '10' COMMENT '排序',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`model_id`) USING BTREE,
  UNIQUE KEY `ai_model_params_info_pk` (`model_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='模型参数表'
```

### 官方AI操作日志表：`ai_official_usage_log_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| log_id | varchar | 128 | 主键 | 否 |  | 记录编号 |
| user_id | bigint |  | 索引 | 是 | NULL | 管理员编号 |
| model_id | varchar | 128 |  | 否 |  | 模型编号 |
| operation_type | varchar | 50 | 索引 | 否 |  | 操作类型（如：data_analysis） |
| input_params | varchar | 1024 |  | 是 | NULL | 输入参数（JSON格式） |
| output_result | text |  |  | 是 | NULL | 模型返回结果（JSON/Text格式） |
| request_time | datetime |  | 索引 | 否 |  | 请求时间 |
| request_duration | bigint |  |  | 是 | NULL | 请求时长（毫秒） |
| tokens_used | int |  |  | 否 | 0 | 消耗Tokens数量 |
| log_status | char | 1 | 索引 | 否 | 0 | 状态（0成功 1失败） |
| ai_status_code | varchar | 16 |  | 是 | NULL | 模型返回状态码 |
| fail_reason | varchar | 128 |  | 是 | NULL | 失败原因 |
| remark | varchar | 512 |  | 是 | NULL | 备注 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 创建时间 |
| update_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 更新时间 |
| is_delete | char | 1 |  | 否 | 0 | 删除（0正常 1删除） |

```sql
CREATE TABLE `ai_official_usage_log_info` (
  `log_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '记录编号',
  `user_id` bigint DEFAULT NULL COMMENT '管理员编号',
  `model_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型编号',
  `operation_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作类型（如：data_analysis）',
  `input_params` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '输入参数（JSON格式）',
  `output_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '模型返回结果（JSON/Text格式）',
  `request_time` datetime NOT NULL COMMENT '请求时间',
  `request_duration` bigint DEFAULT NULL COMMENT '请求时长（毫秒）',
  `tokens_used` int NOT NULL DEFAULT '0' COMMENT '消耗Tokens数量',
  `log_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '状态（0成功 1失败）',
  `ai_status_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '模型返回状态码',
  `fail_reason` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '失败原因',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除（0正常 1删除）',
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `idx_operation_type` (`operation_type`) USING BTREE,
  KEY `idx_request_time` (`request_time`) USING BTREE,
  KEY `idx_status` (`log_status`) USING BTREE,
  CONSTRAINT `ai_official_usage_log_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='官方AI操作日志表'
```

### 提示词信息表：`ai_prompt_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| info_id | varchar | 128 | 主键 | 否 |  | 编号 |
| name | varchar | 128 |  | 否 |  | 名称 |
| content | text |  |  | 否 |  | 提示内容 |
| prompt_status | char | 1 |  | 否 |  | 状态 |
| order_num | int |  |  | 否 | 10 | 排序 |
| create_by | varchar | 64 |  | 否 |  | 创建人 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 创建时间 |
| update_by | varchar | 64 |  | 是 | NULL | 更新人 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| remark | varchar | 512 |  | 是 | NULL | 备注 |

```sql
CREATE TABLE `ai_prompt_info` (
  `info_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '编号',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '提示内容',
  `prompt_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态',
  `order_num` int NOT NULL DEFAULT '10' COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`info_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='提示词信息表'
```

### 用户AI使用记录表：`ai_user_usage_log_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| log_id | varchar | 128 | 主键 | 否 |  | 记录编号 |
| user_id | varchar | 128 | 索引 | 否 |  | 用户编号 |
| model_id | varchar | 128 |  | 否 |  | 模型编号 |
| input_params | varchar | 1024 |  | 是 | NULL | 输入参数 |
| output_result | text |  |  | 是 | NULL | 返回结果 |
| request_time | datetime |  |  | 否 |  | 请求时间 |
| request_duration | bigint |  |  | 是 | NULL | 请求时长（毫秒） |
| tokens_used | int |  |  | 否 | 0 | 消耗Tokens数量 |
| points_used | int |  |  | 否 | 0 | 消耗积分 |
| usage_type | varchar | 50 | 索引 | 否 |  | 使用类型（0AI扩图 1AI编辑 2AI搜索） |
| target_id | varchar | 128 |  | 是 | NULL | 目标编号 |
| log_status | char | 1 | 索引 | 否 |  | 状态（0成功 1失败 2超时） |
| ai_status_code | varchar | 16 |  | 是 | NULL | 模型返回码 |
| fail_reason | varchar | 128 |  | 是 | NULL | 失败原因 |
| ip_addr | varchar | 50 |  | 否 |  | 用户IP地址 |
| device_id | varchar | 255 |  | 是 | NULL | 设备唯一标识 |
| browser | varchar | 50 |  | 是 | NULL | 浏览器类型 |
| os | varchar | 50 |  | 是 | NULL | 操作系统 |
| platform | varchar | 20 |  | 是 | NULL | 平台 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 创建时间 |
| update_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 更新时间 |
| is_delete | char | 1 |  | 否 | 0 | 删除（0正常 1删除） |

```sql
CREATE TABLE `ai_user_usage_log_info` (
  `log_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '记录编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `model_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模型编号',
  `input_params` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '输入参数',
  `output_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '返回结果',
  `request_time` datetime NOT NULL COMMENT '请求时间',
  `request_duration` bigint DEFAULT NULL COMMENT '请求时长（毫秒）',
  `tokens_used` int NOT NULL DEFAULT '0' COMMENT '消耗Tokens数量',
  `points_used` int NOT NULL DEFAULT '0' COMMENT '消耗积分',
  `usage_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '使用类型（0AI扩图 1AI编辑 2AI搜索）',
  `target_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '目标编号',
  `log_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态（0成功 1失败 2超时）',
  `ai_status_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '模型返回码',
  `fail_reason` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '失败原因',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户IP地址',
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '平台',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除（0正常 1删除）',
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `idx_usage_type` (`usage_type`) USING BTREE,
  KEY `idx_status` (`log_status`) USING BTREE,
  KEY `idx_user_model` (`user_id`,`model_id`) USING BTREE,
  CONSTRAINT `ai_user_usage_log_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户AI使用记录表'
```

### 配置信息表：`c_config_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| config_id | bigint |  | 主键 | 否 | 自增 | 编号 |
| config_name | varchar | 128 | 唯一索引 | 否 |  | 配置名称 |
| config_key | varchar | 128 | 唯一索引 | 否 |  | 配置键名 |
| config_value | varchar | 1024 |  | 否 |  | 配置键值 |
| config_type | varchar | 1 |  | 否 | 1 | 配置类型（1值 2文件） |
| config_is_in | char | 1 |  | 否 | 0 | 是否内置 |
| order_num | int |  |  | 否 | 10 | 配置排序 |
| create_by | varchar | 64 |  | 否 |  | 创建人 |
| create_time | datetime |  |  | 否 |  | 创建时间 |
| update_by | varchar | 64 |  | 是 | NULL | 更新人 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| remark | varchar | 512 |  | 是 | NULL | 备注 |

```sql
CREATE TABLE `c_config_info` (
  `config_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `config_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置名称',
  `config_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置键名',
  `config_value` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置键值',
  `config_type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '配置类型（1值 2文件）',
  `config_is_in` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '是否内置',
  `order_num` int NOT NULL DEFAULT '10' COMMENT '配置排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE,
  UNIQUE KEY `uk_c_config_info_config_name` (`config_name`) USING BTREE,
  UNIQUE KEY `uk_c_config_info_config_key` (`config_key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='配置信息表'
```

### 文件日志表：`c_file_log_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| log_id | varchar | 128 | 主键 | 否 |  | 日志编号 |
| user_id | varchar | 128 | 索引 | 否 |  | 用户编号 |
| target_id | varchar | 128 |  | 是 | NULL | 目标对象 |
| target_content | varchar | 256 |  | 是 | NULL | 目标内容 |
| file_url | varchar | 512 |  | 否 |  | 文件路径 |
| file_type | varchar | 16 |  | 否 |  | 文件类型 |
| log_status | char | 1 |  | 否 |  | 状态 |
| log_type | char | 1 |  | 否 | 1 | 日志类型（0图片 1空间封面 2头像） |
| is_compress | char | 1 |  | 否 | 1 | 是否压缩（0是 1否） |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 创建时间 |
| delete_time | datetime |  |  | 是 | NULL | 删除时间 |
| ip_addr | varchar | 50 |  | 否 |  | IP地址 |
| device_id | varchar | 256 |  | 是 | NULL | 设备唯一标识 |
| browser | varchar | 50 |  | 是 | NULL | 浏览器类型 |
| os | varchar | 50 |  | 是 | NULL | 操作系统 |
| platform | varchar | 20 |  | 是 | NULL | 平台 |
| ip_address | varchar | 64 |  | 是 | NULL | IP属地 |

```sql
CREATE TABLE `c_file_log_info` (
  `log_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '日志编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `target_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '目标对象',
  `target_content` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '目标内容',
  `file_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件路径',
  `file_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件类型',
  `log_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态',
  `log_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '日志类型（0图片 1空间封面 2头像）',
  `is_compress` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '是否压缩（0是 1否）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'IP地址',
  `device_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '平台',
  `ip_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'IP属地',
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  CONSTRAINT `c_file_log_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='文件日志表'
```

### 国际化键名表：`c_i18n_key_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| key_id | bigint |  | 主键 | 否 | 自增 | 编号 |
| key_name | varchar | 128 | 唯一索引 | 否 |  | 键 |
| order_num | int |  |  | 是 | NULL | 显示顺序 |
| create_by | varchar | 64 |  | 否 |  | 创建人 |
| create_time | datetime |  |  | 否 |  | 创建时间 |
| update_by | varchar | 64 |  | 是 | NULL | 更新人 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| remark | varchar | 500 |  | 是 | NULL | 备注 |

```sql
CREATE TABLE `c_i18n_key_info` (
  `key_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `key_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '键',
  `order_num` int DEFAULT NULL COMMENT '显示顺序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`key_id`) USING BTREE,
  UNIQUE KEY `uk_c_i18n_key_info_key_name` (`key_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='国际化键名表'
```

### 国际化国家表：`c_i18n_locale_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| locale_id | bigint |  | 主键 | 否 | 自增 | 编号 |
| locale_name | varchar | 128 |  | 否 |  | 国家地区 |
| locale | varchar | 8 | 唯一索引 | 否 |  | 简称 |
| locale_status | char | 1 |  | 否 | 1 | 状态（0正常 1隐藏） |
| create_by | varchar | 64 |  | 否 |  | 创建人 |
| create_time | datetime |  |  | 否 |  | 创建时间 |
| update_by | varchar | 64 |  | 是 | NULL | 更新人 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| remark | varchar | 500 |  | 是 | NULL | 备注 |

```sql
CREATE TABLE `c_i18n_locale_info` (
  `locale_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `locale_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '国家地区',
  `locale` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '简称',
  `locale_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '状态（0正常 1隐藏）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`locale_id`) USING BTREE,
  UNIQUE KEY `uk_c_i18n_locale_info_locale` (`locale`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='国际化国家表'
```

### 国际化信息表：`c_i18n_message_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| message_id | bigint |  | 主键 | 否 | 自增 | 主键 |
| message_key | varchar | 128 | 索引 | 否 |  | 键 |
| locale | varchar | 8 | 索引 | 否 |  | 简称 |
| message | varchar | 1024 |  | 否 |  | 消息 |
| create_by | varchar | 64 |  | 否 |  | 创建人 |
| create_time | datetime |  |  | 否 |  | 创建时间 |
| update_by | varchar | 64 |  | 是 | NULL | 更新人 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| remark | varchar | 500 |  | 是 | NULL | 备注 |

```sql
CREATE TABLE `c_i18n_message_info` (
  `message_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `message_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '键',
  `locale` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '简称',
  `message` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '消息',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`message_id`) USING BTREE,
  UNIQUE KEY `uk_c_i18n_message_info_key_locale` (`message_key`,`locale`) USING BTREE,
  KEY `fk_c_i18n_message_info_locale` (`locale`) USING BTREE,
  CONSTRAINT `fk_c_i18n_message_info_locale` FOREIGN KEY (`locale`) REFERENCES `c_i18n_locale_info` (`locale`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='国际化信息表'
```

### 通知模版表：`c_inform_template_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| template_id | bigint |  | 主键 | 否 | 自增 | 主键 |
| template_name | varchar | 128 | 索引 | 否 |  | 模版名称 |
| template_key | varchar | 255 |  | 否 |  | 模版KEY |
| locale | varchar | 8 | 索引 | 否 |  | 语言（默认zh-CN） |
| channel | varchar | 32 |  | 是 | NULL | 渠道 |
| template_type | char | 1 | 索引 | 否 |  | 模版类型（1=短信 2=邮件 3=站内通知 4=APP推送 5=微信模板） |
| service_template_id | varchar | 64 |  | 是 | NULL | 服务商模版ID |
| service_sign_name | varchar | 64 |  | 是 | NULL | 服务商签名 |
| inform_title | varchar | 128 |  | 是 | NULL | 通知标题 |
| extend_config | varchar | 1024 |  | 是 | NULL | 扩展配置 |
| template_version | int |  |  | 否 |  | 版本 |
| template_version_history | text |  |  | 否 |  | 历史版本 |
| content | text |  |  | 否 |  | 内容 |
| example | text |  |  | 是 | NULL | 事例 |
| variables | text |  |  | 是 | NULL | 变量列表 |
| template_image | varchar | 1024 |  | 是 | NULL | 模版样式图 |
| status | char | 1 | 索引 | 否 |  | 状态（0=待审核 1=已启用 2=已禁用 3=审核失败） |
| create_by | varchar | 64 |  | 否 |  | 创建人 |
| create_time | datetime |  |  | 否 |  | 创建时间 |
| update_by | varchar | 64 |  | 是 | NULL | 更新人 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| remark | varchar | 500 |  | 是 | NULL | 备注 |

```sql
CREATE TABLE `c_inform_template_info` (
  `template_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `template_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模版名称',
  `template_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模版KEY',
  `locale` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '语言（默认zh-CN）',
  `channel` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '渠道',
  `template_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模版类型（1=短信 2=邮件 3=站内通知 4=APP推送 5=微信模板）',
  `service_template_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '服务商模版ID',
  `service_sign_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '服务商签名',
  `inform_title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '通知标题',
  `extend_config` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '扩展配置',
  `template_version` int NOT NULL COMMENT '版本',
  `template_version_history` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '历史版本',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
  `example` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '事例',
  `variables` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '变量列表',
  `template_image` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '模版样式图',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态（0=待审核 1=已启用 2=已禁用 3=审核失败）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`template_id`) USING BTREE,
  UNIQUE KEY `uk_template_name_locale` (`template_name`,`locale`,`template_type`) USING BTREE,
  KEY `fk_inform_template_locale` (`locale`) USING BTREE,
  KEY `idx_template_status` (`status`) USING BTREE,
  KEY `idx_template_type` (`template_type`) USING BTREE,
  CONSTRAINT `fk_inform_template_locale` FOREIGN KEY (`locale`) REFERENCES `c_i18n_locale_info` (`locale`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='通知模版表'
```

### 菜单信息表：`c_menu_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| menu_id | bigint |  | 主键 | 否 | 自增 | 编号 |
| menu_name | varchar | 50 | 唯一索引 | 否 |  | 菜单名称 |
| parent_id | varchar | 128 |  | 是 | NULL | 父菜单 |
| order_num | int |  |  | 是 | NULL | 显示顺序 |
| path | varchar | 256 |  | 是 | NULL | 路由地址 |
| component | varchar | 256 |  | 是 | NULL | 组件路径 |
| query | varchar | 256 |  | 是 | NULL | 路由参数 |
| route_name | varchar | 256 |  | 是 | NULL | 路由名称 |
| menu_address | char | 1 |  | 否 | 1 | 显示位置 |
| is_frame | char | 1 |  | 是 | 1 | 是否外链 |
| is_cache | char | 1 |  | 是 | 1 | 是否缓存 |
| menu_type | char | 1 |  | 否 |  | 菜单类型 |
| visible | char | 1 |  | 是 | 1 | 是否显示 |
| status | char | 1 |  | 是 | 1 | 菜单状态 |
| perms | varchar | 128 |  | 是 | NULL | 权限标识 |
| icon | varchar | 128 |  | 是 | NULL | 菜单图标 |
| create_by | varchar | 64 |  | 否 |  | 创建人 |
| create_time | datetime |  |  | 否 |  | 创建时间 |
| update_by | varchar | 64 |  | 是 | NULL | 更新人 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| remark | varchar | 512 |  | 是 | NULL | 备注 |

```sql
CREATE TABLE `c_menu_info` (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `parent_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '父菜单',
  `order_num` int DEFAULT NULL COMMENT '显示顺序',
  `path` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '路由地址',
  `component` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '组件路径',
  `query` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '路由名称',
  `menu_address` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '显示位置',
  `is_frame` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '1' COMMENT '是否外链',
  `is_cache` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '1' COMMENT '是否缓存',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单类型',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '1' COMMENT '是否显示',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '1' COMMENT '菜单状态',
  `perms` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE,
  UNIQUE KEY `menu_name` (`menu_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='菜单信息表'
```

### 用户公告表：`c_notice_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| notice_id | varchar | 128 | 主键 | 否 |  | 公告编号 |
| locale | varchar | 8 | 索引 | 否 |  | 语言 默认zh-CN |
| platform | char | 1 |  | 否 |  | 通知平台 |
| notice_type | char | 1 |  | 否 |  | 公告类型 |
| is_exhibit | char | 1 |  | 否 |  | 是否展示 |
| notice_title | varchar | 128 |  | 否 |  | 公告标题 |
| content | text |  |  | 否 |  | 公告内容 |
| order_num | int |  |  | 否 |  | 排序 |
| notice_status | char | 1 |  | 否 |  | 公告状态 |
| user_id | bigint |  |  | 否 |  | 创建人 |
| create_time | datetime |  | 索引 | 否 | CURRENT_TIMESTAMP | 创建时间 |
| update_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 更新时间 |
| remark | varchar | 500 |  | 是 | NULL | 备注 |

```sql
CREATE TABLE `c_notice_info` (
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
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE,
  KEY `fk_notice_info_locale` (`locale`) USING BTREE,
  CONSTRAINT `fk_notice_info_locale` FOREIGN KEY (`locale`) REFERENCES `c_i18n_locale_info` (`locale`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户公告表'
```

### 权限信息表：`c_permission_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| permission_id | bigint |  | 主键 | 否 | 自增 | 编号 |
| permission_name | varchar | 50 | 唯一索引 | 否 |  | 权限名称 |
| parent_id | varchar | 128 |  | 是 | NULL | 父菜单 |
| order_num | int |  |  | 是 | NULL | 显示顺序 |
| permission | varchar | 128 | 唯一索引 | 否 |  | 权限标识 |
| status | char | 1 |  | 否 |  | 是否使用（0正常 1关闭） |
| create_by | varchar | 64 |  | 否 |  | 创建人 |
| create_time | datetime |  |  | 否 |  | 创建时间 |
| update_by | varchar | 64 |  | 是 | NULL | 更新人 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| remark | varchar | 512 |  | 是 | NULL | 备注 |

```sql
CREATE TABLE `c_permission_info` (
  `permission_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `permission_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限名称',
  `parent_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '父菜单',
  `order_num` int DEFAULT NULL COMMENT '显示顺序',
  `permission` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限标识',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '是否使用（0正常 1关闭）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`permission_id`) USING BTREE,
  UNIQUE KEY `uk_c_permission_info_permission_name` (`permission_name`) USING BTREE,
  UNIQUE KEY `uk_c_permission_info_permission` (`permission`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1895390476631150603 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='权限信息表'
```

### 代码生成业务表：`gen_table`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| table_id | bigint |  | 主键 | 否 | 自增 | 编号 |
| table_name | varchar | 200 |  | 是 |  | 表名称 |
| table_comment | varchar | 500 |  | 是 |  | 表描述 |
| sub_table_name | varchar | 64 |  | 是 | NULL | 关联子表的表名 |
| sub_table_fk_name | varchar | 64 |  | 是 | NULL | 子表关联的外键名 |
| class_name | varchar | 100 |  | 是 |  | 实体类名称 |
| tpl_category | varchar | 200 |  | 是 | crud | 使用的模板（crud单表操作 tree树表操作） |
| tpl_web_type | varchar | 30 |  | 是 |  | 前端模板类型（element-ui模版 element-plus模版） |
| package_name | varchar | 100 |  | 是 | NULL | 生成包路径 |
| module_name | varchar | 30 |  | 是 | NULL | 生成模块名 |
| business_name | varchar | 30 |  | 是 | NULL | 生成业务名 |
| function_name | varchar | 50 |  | 是 | NULL | 生成功能名 |
| function_author | varchar | 50 |  | 是 | NULL | 生成功能作者 |
| gen_type | char | 1 |  | 是 | 0 | 生成代码方式（0zip压缩包 1自定义路径） |
| gen_path | varchar | 200 |  | 是 | / | 生成路径（不填默认项目路径） |
| options | varchar | 1000 |  | 是 | NULL | 其它生成选项 |
| create_by | varchar | 64 |  | 是 |  | 创建者 |
| create_time | datetime |  |  | 是 | NULL | 创建时间 |
| update_by | varchar | 64 |  | 是 |  | 更新者 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| remark | varchar | 500 |  | 是 | NULL | 备注 |

```sql
CREATE TABLE `gen_table` (
  `table_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `tpl_web_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '前端模板类型（element-ui模版 element-plus模版）',
  `package_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='代码生成业务表'
```

### 代码生成业务表字段：`gen_table_column`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| column_id | bigint |  | 主键 | 否 | 自增 | 编号 |
| table_id | bigint |  |  | 是 | NULL | 归属表编号 |
| column_name | varchar | 200 |  | 是 | NULL | 列名称 |
| column_comment | varchar | 500 |  | 是 | NULL | 列描述 |
| column_type | varchar | 100 |  | 是 | NULL | 列类型 |
| java_type | varchar | 500 |  | 是 | NULL | JAVA类型 |
| java_field | varchar | 200 |  | 是 | NULL | JAVA字段名 |
| is_pk | char | 1 |  | 是 | NULL | 是否主键（1是） |
| is_increment | char | 1 |  | 是 | NULL | 是否自增（1是） |
| is_required | char | 1 |  | 是 | NULL | 是否必填（1是） |
| is_insert | char | 1 |  | 是 | NULL | 是否为插入字段（1是） |
| is_edit | char | 1 |  | 是 | NULL | 是否编辑字段（1是） |
| is_list | char | 1 |  | 是 | NULL | 是否列表字段（1是） |
| is_query | char | 1 |  | 是 | NULL | 是否查询字段（1是） |
| query_type | varchar | 200 |  | 是 | EQ | 查询方式（等于、不等于、大于、小于、范围） |
| html_type | varchar | 200 |  | 是 | NULL | 显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件） |
| dict_type | varchar | 200 |  | 是 |  | 字典类型 |
| sort | int |  |  | 是 | NULL | 排序 |
| create_by | varchar | 64 |  | 是 |  | 创建者 |
| create_time | datetime |  |  | 是 | NULL | 创建时间 |
| update_by | varchar | 64 |  | 是 |  | 更新者 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |

```sql
CREATE TABLE `gen_table_column` (
  `column_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '字典类型',
  `sort` int DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1015 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='代码生成业务表字段'
```

### 图片申请信息表：`p_picture_apply_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| apply_id | varchar | 128 | 主键 | 否 |  | 申请编号 |
| picture_id | varchar | 128 | 索引 | 否 |  | 图片编号 |
| picture_name | varchar | 32 |  | 否 |  | 图片名称 |
| thumbnail_url | varchar | 512 |  | 否 |  | 缩略图 URL |
| apply_type | char | 1 |  | 否 |  | 申请类型 |
| apply_reason | text |  |  | 否 |  | 申请理由 |
| apply_image | text |  |  | 是 | NULL | 证明图片 |
| apply_file | text |  |  | 是 | NULL | 证明文件 |
| contact | varchar | 512 |  | 否 |  | 联系方式 |
| points_need | int |  |  | 否 | 10 | 所需积分 |
| price_need | decimal | 10,2 |  | 是 | 0.00 | 所需金额 |
| user_id | varchar | 128 | 索引 | 否 |  | 用户 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 创建时间 |
| update_time | datetime |  |  | 是 | CURRENT_TIMESTAMP | 更新时间 |
| review_status | char | 1 |  | 否 | 0 | 审核状态 |
| review_message | varchar | 512 |  | 是 | NULL | 审核信息 |
| review_user_id | bigint |  | 索引 | 是 | NULL | 审核人编号 |
| review_time | datetime |  |  | 是 | NULL | 审核时间 |

```sql
CREATE TABLE `p_picture_apply_info` (
  `apply_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '申请编号',
  `picture_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片编号',
  `picture_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片名称',
  `thumbnail_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '缩略图 URL',
  `apply_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '申请类型',
  `apply_reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '申请理由',
  `apply_image` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '证明图片',
  `apply_file` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '证明文件',
  `contact` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系方式',
  `points_need` int NOT NULL DEFAULT '10' COMMENT '所需积分',
  `price_need` decimal(10,2) DEFAULT '0.00' COMMENT '所需金额',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `review_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '审核状态',
  `review_message` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '审核信息',
  `review_user_id` bigint DEFAULT NULL COMMENT '审核人编号',
  `review_time` datetime DEFAULT NULL COMMENT '审核时间',
  PRIMARY KEY (`apply_id`) USING BTREE,
  KEY `fk_picture_apply_picture_id` (`picture_id`) USING BTREE,
  KEY `fk_picture_apply_user_id` (`user_id`) USING BTREE,
  KEY `fk_picture_apply_review_user_id` (`review_user_id`) USING BTREE,
  CONSTRAINT `fk_picture_apply_picture_id` FOREIGN KEY (`picture_id`) REFERENCES `p_picture_info` (`picture_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_picture_apply_review_user_id` FOREIGN KEY (`review_user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_picture_apply_user_id` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='图片申请信息表'
```

### 图片分类信息表：`p_picture_category_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| category_id | varchar | 128 | 主键 | 否 |  | 分类编号 |
| parent_id | varchar | 128 | 索引 | 否 | 0 | 父级分类编号 |
| ancestors | varchar | 1280 |  | 否 |  | 祖级列表 |
| cover_url | varchar | 512 |  | 是 | NULL | 封面图URL |
| category_icon | varchar | 64 |  | 是 | NULL | 封面图标 |
| name | varchar | 32 |  | 否 |  | 分类名称 |
| order_num | int |  |  | 否 | 1 | 显示顺序 |
| category_desc | varchar | 512 |  | 是 | NULL | 分类描述 |
| category_status | char | 1 |  | 否 | 0 | 分类状态（0正常 1关闭） |
| category_type | char | 1 |  | 否 | 0 | 分类类型 |
| query_status | char | 1 |  | 否 | 0 | 查询状态（0是 1否） |
| usage_count | bigint |  | 索引 | 否 | 0 | 使用次数 |
| look_count | bigint |  | 索引 | 否 | 0 | 查看次数 |
| download_count | bigint |  | 索引 | 否 | 0 | 下载次数 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 创建时间 |
| update_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 更新时间 |
| is_delete | char | 1 |  | 否 | 0 | 删除标记（0否 1是） |

```sql
CREATE TABLE `p_picture_category_info` (
  `category_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类编号',
  `parent_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '父级分类编号',
  `ancestors` varchar(1280) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '祖级列表',
  `cover_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '封面图URL',
  `category_icon` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '封面图标',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名称',
  `order_num` int NOT NULL DEFAULT '1' COMMENT '显示顺序',
  `category_desc` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '分类描述',
  `category_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '分类状态（0正常 1关闭）',
  `category_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '分类类型',
  `query_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '查询状态（0是 1否）',
  `usage_count` bigint NOT NULL DEFAULT '0' COMMENT '使用次数',
  `look_count` bigint NOT NULL DEFAULT '0' COMMENT '查看次数',
  `download_count` bigint NOT NULL DEFAULT '0' COMMENT '下载次数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除标记（0否 1是）',
  PRIMARY KEY (`category_id`) USING BTREE,
  KEY `parent_id` (`parent_id`) USING BTREE,
  KEY `idx_usage_count` (`usage_count`) USING BTREE,
  KEY `idx_look_count` (`look_count`) USING BTREE,
  KEY `idx_download_count` (`download_count`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='图片分类信息表'
```

### 图片评论表：`p_picture_comment_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| comment_id | varchar | 128 | 主键 | 否 |  | 评论编号 |
| user_id | varchar | 128 | 索引 | 否 |  | 用户编号 |
| parent_id | varchar | 128 | 索引 | 是 | NULL | 父级评论编号 |
| picture_id | varchar | 128 | 索引 | 否 |  | 图片编号 |
| category_id | varchar | 128 | 索引 | 否 |  | 图片分类 |
| tags | varchar | 256 |  | 是 | NULL | 图片标签（格式："标签1","标签2"） |
| content | varchar | 256 |  | 是 | NULL | 评论内容 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 评论时间 |
| like_count | int |  |  | 否 | 0 | 点赞数 |
| ip_address | varchar | 64 |  | 是 | NULL | 评论者IP属地 |
| picture_url | varchar | 500 |  | 是 | NULL | 评论图片URL |
| comment_status | char | 1 |  | 否 | 0 | 评论状态（0正常 1异常） |

```sql
CREATE TABLE `p_picture_comment_info` (
  `comment_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `parent_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '父级评论编号',
  `picture_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片编号',
  `category_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片分类',
  `tags` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图片标签（格式："标签1","标签2"）',
  `content` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '评论内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  `like_count` int NOT NULL DEFAULT '0' COMMENT '点赞数',
  `ip_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '评论者IP属地',
  `picture_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '评论图片URL',
  `comment_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '评论状态（0正常 1异常）',
  PRIMARY KEY (`comment_id`) USING BTREE,
  KEY `idx_comment_user` (`user_id`) USING BTREE,
  KEY `idx_comment_picture` (`picture_id`) USING BTREE,
  KEY `idx_comment_category` (`category_id`) USING BTREE,
  KEY `idx_comment_parent` (`parent_id`) USING BTREE,
  CONSTRAINT `fk_comment_category` FOREIGN KEY (`category_id`) REFERENCES `p_picture_category_info` (`category_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_comment_parent` FOREIGN KEY (`parent_id`) REFERENCES `p_picture_comment_info` (`comment_id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `fk_comment_picture` FOREIGN KEY (`picture_id`) REFERENCES `p_picture_info` (`picture_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `chk_like_count` CHECK ((`like_count` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='图片评论表'
```

### 图片点赞记录表：`p_picture_comment_like_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| like_id | varchar | 128 | 主键 | 否 |  | 点赞编号 |
| user_id | varchar | 128 | 索引 | 否 |  | 用户编号 |
| picture_id | varchar | 128 | 索引 | 否 |  | 图片编号 |
| target_cover | varchar | 512 |  | 是 | NULL | 封面URL |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 点赞时间 |

```sql
CREATE TABLE `p_picture_comment_like_info` (
  `like_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '点赞编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `picture_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片编号',
  `target_cover` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '封面URL',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
  PRIMARY KEY (`like_id`) USING BTREE,
  UNIQUE KEY `uk_comment_user_picture` (`user_id`,`picture_id`) USING BTREE,
  KEY `idx_comment_like_user` (`user_id`) USING BTREE,
  KEY `idx_comment_like_picture` (`picture_id`) USING BTREE,
  CONSTRAINT `fk_comment_like_picture` FOREIGN KEY (`picture_id`) REFERENCES `p_picture_info` (`picture_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_comment_like_user` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='图片点赞记录表'
```

### 图片下载记录表：`p_picture_download_log_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| download_id | varchar | 128 | 主键 | 否 |  | 下载编号 |
| user_id | varchar | 128 | 索引 | 否 |  | 用户编号 |
| picture_id | varchar | 128 | 索引 | 否 |  | 图片编号 |
| category_id | varchar | 128 |  | 是 | NULL | 图片分类 |
| picture_name | varchar | 32 |  | 否 |  | 图片名称 |
| thumbnail_url | varchar | 512 |  | 是 | NULL | 缩略图URL |
| tags | varchar | 256 |  | 是 | NULL | 图片标签（格式："标签1","标签2"） |
| space_id | varchar | 128 | 索引 | 是 | NULL | 空间编号 |
| points_cost | int |  |  | 否 | 0 | 消耗积分 |
| points_author_gain | int |  |  | 否 | 0 | 作者分成积分 |
| points_official_gain | int |  |  | 否 | 0 | 平台分成积分 |
| points_space_gain | int |  |  | 是 | 0 | 空间分成积分 |
| author_proportion | double | 10,2 |  | 是 | NULL | 作者分成比例 |
| official_proportion | double | 10,2 |  | 是 | NULL | 官方分成比例 |
| space_proportion | double | 10,2 |  | 是 | NULL | 空间分成比例 |
| create_time | datetime |  | 索引 | 否 | CURRENT_TIMESTAMP | 下载时间 |
| download_status | char | 1 | 索引 | 否 | 1 | 下载状态（1失败 0成功） |
| fail_reason | varchar | 255 |  | 是 | NULL | 失败原因 |
| download_type | char | 1 |  | 否 |  | 下载类型（0查看 1下载 2批量下载 |
| refer_source | char | 1 |  | 是 | NULL | 来源（0其他 1详情 2分享） |
| has_statistics | char | 1 |  | 否 | 0 | 是否统计（0否 1是） |
| score | decimal | 5,2 |  | 否 |  | 分数 |
| ip_addr | varchar | 64 |  | 否 |  | IP地址 |
| ip_address | varchar | 64 |  | 是 | NULL | IP属地 |
| device_id | varchar | 255 |  | 是 | NULL | 设备唯一标识 |
| browser | varchar | 50 |  | 是 | NULL | 浏览器类型 |
| os | varchar | 50 |  | 是 | NULL | 操作系统 |
| platform | varchar | 20 |  | 是 | NULL | 平台 |

```sql
CREATE TABLE `p_picture_download_log_info` (
  `download_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '下载编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `picture_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片编号',
  `category_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图片分类',
  `picture_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片名称',
  `thumbnail_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '缩略图URL',
  `tags` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图片标签（格式："标签1","标签2"）',
  `space_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '空间编号',
  `points_cost` int NOT NULL DEFAULT '0' COMMENT '消耗积分',
  `points_author_gain` int NOT NULL DEFAULT '0' COMMENT '作者分成积分',
  `points_official_gain` int NOT NULL DEFAULT '0' COMMENT '平台分成积分',
  `points_space_gain` int DEFAULT '0' COMMENT '空间分成积分',
  `author_proportion` double(10,2) DEFAULT NULL COMMENT '作者分成比例',
  `official_proportion` double(10,2) DEFAULT NULL COMMENT '官方分成比例',
  `space_proportion` double(10,2) DEFAULT NULL COMMENT '空间分成比例',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下载时间',
  `download_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '下载状态（1失败 0成功）',
  `fail_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '失败原因',
  `download_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '下载类型（0查看 1下载 2批量下载',
  `refer_source` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '来源（0其他 1详情 2分享）',
  `has_statistics` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '是否统计（0否 1是）',
  `score` decimal(5,2) NOT NULL COMMENT '分数',
  `ip_addr` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'IP地址',
  `ip_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'IP属地',
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '平台',
  PRIMARY KEY (`download_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `space_id` (`space_id`) USING BTREE,
  KEY `idx_download_time` (`create_time`) USING BTREE,
  KEY `idx_download_status` (`download_status`) USING BTREE,
  KEY `idx_picture` (`picture_id`) USING BTREE,
  CONSTRAINT `p_picture_download_log_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `p_picture_download_log_info_ibfk_2` FOREIGN KEY (`picture_id`) REFERENCES `p_picture_info` (`picture_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `p_picture_download_log_info_ibfk_3` FOREIGN KEY (`space_id`) REFERENCES `p_space_info` (`space_id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='图片下载记录表'
```

### 图片详细信息表：`p_picture_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| picture_id | varchar | 128 | 主键 | 否 |  | 图片编号 |
| picture_url | varchar | 512 |  | 否 |  | 图片URL |
| name | varchar | 32 |  | 否 |  | 图片名称 |
| introduction | text |  |  | 是 | NULL | 简介 |
| category_id | varchar | 128 | 索引 | 是 | NULL | 分类编号 |
| pic_size | bigint |  |  | 是 | NULL | 图片体积（字节） |
| pic_width | int |  |  | 是 | 0 | 图片宽度 |
| pic_height | int |  |  | 是 | 0 | 图片高度 |
| pic_scale | double |  |  | 是 | 0 | 宽高比例 |
| pic_format | varchar | 32 |  | 是 | NULL | 图片格式 |
| user_id | varchar | 128 | 索引 | 否 |  | 上传用户编号 |
| create_time | datetime |  | 索引 | 否 | CURRENT_TIMESTAMP | 创建时间 |
| publish_time | datetime |  |  | 是 | NULL | 发布时间 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| picture_status | char | 1 | 索引 | 否 |  | 图片状态（0公共 1私有） |
| thumbnail_url | varchar | 512 |  | 是 | NULL | 缩略图URL |
| look_count | bigint |  | 索引 | 否 | 0 | 查看次数 |
| collect_count | bigint |  | 索引 | 否 | 0 | 收藏次数 |
| like_count | bigint |  | 索引 | 否 | 0 | 点赞次数 |
| share_count | bigint |  | 索引 | 否 | 0 | 分享次数 |
| download_count | bigint |  | 索引 | 否 | 0 | 下载次数 |
| space_id | varchar | 128 | 索引 | 是 | NULL | 所属空间编号 |
| folder_id | varchar | 128 |  | 是 | NULL | 所属文件夹编号 |
| upload_type | char | 1 |  | 否 | 1 | 上传类型 |
| more_info | text |  |  | 是 | NULL | 更多信息 |
| is_delete | char | 1 |  | 否 | 0 | 删除（0否 1是） |
| deleted_time | datetime |  |  | 是 | NULL | 删除时间 |

```sql
CREATE TABLE `p_picture_info` (
  `picture_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片编号',
  `picture_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片URL',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片名称',
  `introduction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '简介',
  `category_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '分类编号',
  `pic_size` bigint DEFAULT NULL COMMENT '图片体积（字节）',
  `pic_width` int DEFAULT '0' COMMENT '图片宽度',
  `pic_height` int DEFAULT '0' COMMENT '图片高度',
  `pic_scale` double DEFAULT '0' COMMENT '宽高比例',
  `pic_format` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图片格式',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '上传用户编号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `picture_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片状态（0公共 1私有）',
  `thumbnail_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '缩略图URL',
  `look_count` bigint NOT NULL DEFAULT '0' COMMENT '查看次数',
  `collect_count` bigint NOT NULL DEFAULT '0' COMMENT '收藏次数',
  `like_count` bigint NOT NULL DEFAULT '0' COMMENT '点赞次数',
  `share_count` bigint NOT NULL DEFAULT '0' COMMENT '分享次数',
  `download_count` bigint NOT NULL DEFAULT '0' COMMENT '下载次数',
  `space_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属空间编号',
  `folder_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '所属文件夹编号',
  `upload_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '上传类型',
  `more_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '更多信息',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除（0否 1是）',
  `deleted_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`picture_id`) USING BTREE,
  KEY `category_id` (`category_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `idx_space_folder` (`space_id`,`folder_id`) USING BTREE,
  KEY `idx_picture_status` (`picture_status`) USING BTREE,
  KEY `p_picture_info_download_count_index` (`download_count` DESC) USING BTREE,
  KEY `p_picture_info_look_count_index` (`look_count`) USING BTREE,
  KEY `p_picture_info_collect_count_index` (`collect_count`) USING BTREE,
  KEY `p_picture_info_like_count_index` (`like_count`) USING BTREE,
  KEY `p_picture_info_share_count_index` (`share_count`) USING BTREE,
  KEY `idx_p_picture_info_create_time` (`create_time`) USING BTREE,
  CONSTRAINT `p_picture_info_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `p_picture_category_info` (`category_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `p_picture_info_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `p_picture_info_ibfk_3` FOREIGN KEY (`space_id`) REFERENCES `p_space_info` (`space_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='图片详细信息表'
```

### 用户图片推荐模型表：`p_picture_recommend_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| recommend_id | varchar | 128 | 主键 | 否 |  | 推荐编号 |
| category_scores | text |  |  | 是 | NULL | 分类分数 |
| top_categories | text |  |  | 是 | NULL | 高兴趣分类 |
| normalized_category_scores | text |  |  | 是 | NULL | 归一化分类分数 |
| tag_scores | text |  |  | 是 | NULL | 标签分数 |
| top_tags | text |  |  | 是 | NULL | 高兴趣标签 |
| normalized_tag_scores | text |  |  | 是 | NULL | 归一化标签分数 |
| more_info | text |  |  | 是 | NULL | 更多信息 |
| user_id | varchar | 128 | 索引 | 否 |  | 目标对象 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 创建时间 |

```sql
CREATE TABLE `p_picture_recommend_info` (
  `recommend_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '推荐编号',
  `category_scores` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '分类分数',
  `top_categories` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '高兴趣分类',
  `normalized_category_scores` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '归一化分类分数',
  `tag_scores` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '标签分数',
  `top_tags` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '高兴趣标签',
  `normalized_tag_scores` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '归一化标签分数',
  `more_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '更多信息',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目标对象',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`recommend_id`) USING BTREE,
  KEY `idx_picture_recommend_user` (`user_id`) USING BTREE,
  CONSTRAINT `fk_picture_recommend_user` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户图片推荐模型表'
```

### 图片标签信息表：`p_picture_tag_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| tag_id | varchar | 128 | 主键 | 否 |  | 标签编号 |
| name | varchar | 32 | 唯一索引 | 否 |  | 标签名称 |
| tags_status | char | 1 |  | 否 | 0 | 标签状态 |
| tag_desc | varchar | 512 |  | 是 | NULL | 标签描述 |
| usage_count | bigint |  | 索引 | 否 | 0 | 使用次数 |
| look_count | bigint |  | 索引 | 否 | 0 | 查看次数 |
| download_count | bigint |  | 索引 | 否 | 0 | 下载次数 |
| user_id | varchar | 128 | 索引 | 否 |  | 所属用户 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 创建时间 |
| update_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 更新时间 |

```sql
CREATE TABLE `p_picture_tag_info` (
  `tag_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签编号',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名称',
  `tags_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '标签状态',
  `tag_desc` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '标签描述',
  `usage_count` bigint NOT NULL DEFAULT '0' COMMENT '使用次数',
  `look_count` bigint NOT NULL DEFAULT '0' COMMENT '查看次数',
  `download_count` bigint NOT NULL DEFAULT '0' COMMENT '下载次数',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`tag_id`) USING BTREE,
  UNIQUE KEY `uk_tag_name` (`name`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `idx_usage_count` (`usage_count`) USING BTREE,
  KEY `idx_look_count` (`look_count`) USING BTREE,
  KEY `idx_download_count` (`download_count`) USING BTREE,
  CONSTRAINT `p_picture_tag_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='图片标签信息表'
```

### 图片标签关联表：`p_picture_tag_rel_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| rel_id | varchar | 128 | 主键 | 否 |  | 关联编号 |
| picture_id | varchar | 128 | 索引 | 否 |  | 图片编号 |
| picture_name | varchar | 32 |  | 否 |  | 图片名称 |
| tag_id | varchar | 128 | 索引 | 否 |  | 标签编号 |
| tag_name | varchar | 32 | 索引 | 否 |  | 标签名称 |
| look_count | bigint |  |  | 否 | 0 | 查看次数 |
| collect_count | bigint |  |  | 否 | 0 | 收藏次数 |
| like_count | bigint |  |  | 否 | 0 | 点赞次数 |
| share_count | bigint |  |  | 否 | 0 | 分享次数 |
| download_count | bigint |  |  | 否 | 0 | 下载次数 |
| user_id | varchar | 128 |  | 是 | NULL | 用户编号 |
| create_time | datetime |  | 索引 | 否 | CURRENT_TIMESTAMP |  |

```sql
CREATE TABLE `p_picture_tag_rel_info` (
  `rel_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联编号',
  `picture_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片编号',
  `picture_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '图片名称',
  `tag_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签编号',
  `tag_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '标签名称',
  `look_count` bigint NOT NULL DEFAULT '0' COMMENT '查看次数',
  `collect_count` bigint NOT NULL DEFAULT '0' COMMENT '收藏次数',
  `like_count` bigint NOT NULL DEFAULT '0' COMMENT '点赞次数',
  `share_count` bigint NOT NULL DEFAULT '0' COMMENT '分享次数',
  `download_count` bigint NOT NULL DEFAULT '0' COMMENT '下载次数',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户编号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`rel_id`) USING BTREE,
  KEY `idx_picture_id` (`picture_id`) USING BTREE,
  KEY `idx_tag_id` (`tag_id`) USING BTREE,
  KEY `p_picture_tag_rel_info_tag_name_index` (`tag_name`) USING BTREE,
  KEY `idx_p_picture_tag_rel_info_tag_name` (`tag_name`) USING BTREE,
  KEY `idx_p_picture_tag_rel_info_create_time` (`create_time`) USING BTREE,
  CONSTRAINT `fk_rel_picture` FOREIGN KEY (`picture_id`) REFERENCES `p_picture_info` (`picture_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_rel_tag` FOREIGN KEY (`tag_id`) REFERENCES `p_picture_tag_info` (`tag_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='图片标签关联表'
```

### 用户搜索记录表：`p_search_log_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| search_id | varchar | 128 | 主键 | 否 |  | 搜索记录编号 |
| user_id | varchar | 128 | 索引 | 是 | NULL | 用户编号 |
| keyword | varchar | 32 | 索引 | 否 |  | 搜索关键词 |
| search_type | char | 1 | 索引 | 否 | 0 | 搜索类型（0图片 1空间 2用户） |
| refer_source | char | 1 |  | 是 | 0 | 搜索来源（0首页 1推荐 2搜索页 3AI推荐 4历史搜索） |
| search_status | char | 1 |  | 否 | 0 | 搜索状态（0成功 1失败） |
| fail_reason | varchar | 256 |  | 是 | NULL | 失败原因 |
| result_count | int |  |  | 否 | 0 | 返回数量 |
| create_time | datetime |  | 索引 | 否 | CURRENT_TIMESTAMP | 搜索时间 |
| search_duration | int |  |  | 是 | 0 | 搜索时长（毫秒） |
| device_id | varchar | 256 |  | 是 | NULL | 设备唯一标识 |
| browser | varchar | 50 |  | 是 | NULL | 浏览器类型 |
| os | varchar | 50 |  | 是 | NULL | 操作系统 |
| platform | varchar | 20 |  | 是 | NULL | 平台 |
| ip_address | varchar | 64 |  | 是 | NULL | IP属地 |
| ip_addr | varchar | 64 |  | 是 | NULL | IP地址 |

```sql
CREATE TABLE `p_search_log_info` (
  `search_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '搜索记录编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户编号',
  `keyword` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '搜索关键词',
  `search_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '搜索类型（0图片 1空间 2用户）',
  `refer_source` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '搜索来源（0首页 1推荐 2搜索页 3AI推荐 4历史搜索）',
  `search_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '搜索状态（0成功 1失败）',
  `fail_reason` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '失败原因',
  `result_count` int NOT NULL DEFAULT '0' COMMENT '返回数量',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '搜索时间',
  `search_duration` int DEFAULT '0' COMMENT '搜索时长（毫秒）',
  `device_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '平台',
  `ip_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'IP属地',
  `ip_addr` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'IP地址',
  PRIMARY KEY (`search_id`) USING BTREE,
  KEY `idx_search_user` (`user_id`) USING BTREE,
  KEY `idx_search_type` (`search_type`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE,
  KEY `idx_p_search_log_info_create_time` (`create_time`) USING BTREE,
  KEY `idx_p_search_log_info_keyword` (`keyword`) USING BTREE,
  CONSTRAINT `fk_search_log_user` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户搜索记录表'
```

### 空间扩容信息表：`p_space_dilatation_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| dilatation_id | varchar | 128 | 主键 | 否 |  | 申请编号 |
| dilatation_key | varchar | 128 |  | 否 |  | 扩容KEY |
| space_id | varchar | 128 | 索引 | 否 |  | 空间编号 |
| space_name | varchar | 32 |  | 否 |  | 空间名称 |
| thumbnail_url | varchar | 512 |  | 否 |  | 缩略图 URL |
| dilatation_type | char | 1 |  | 否 |  | 扩容类型 |
| dilatation_unit | int |  |  | 否 |  | 扩容单价 |
| dilatation_total | int |  |  | 否 |  | 扩容总数 |
| points_total | int |  |  | 是 | 0 | 消耗积分 |
| user_id | varchar | 128 | 索引 | 否 |  | 用户 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 创建时间 |

```sql
CREATE TABLE `p_space_dilatation_info` (
  `dilatation_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '申请编号',
  `dilatation_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '扩容KEY',
  `space_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '空间编号',
  `space_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '空间名称',
  `thumbnail_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '缩略图 URL',
  `dilatation_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '扩容类型',
  `dilatation_unit` int NOT NULL COMMENT '扩容单价',
  `dilatation_total` int NOT NULL COMMENT '扩容总数',
  `points_total` int DEFAULT '0' COMMENT '消耗积分',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`dilatation_id`) USING BTREE,
  KEY `fk_space_dilatation_space_id` (`space_id`) USING BTREE,
  KEY `fk_space_dilatation_user_id_dilatation` (`user_id`) USING BTREE,
  CONSTRAINT `fk_space_dilatation_space_id` FOREIGN KEY (`space_id`) REFERENCES `p_space_info` (`space_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_space_dilatation_user_id_dilatation` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='空间扩容信息表'
```

### 空间文件夹表：`p_space_folder_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| folder_id | varchar | 128 | 主键 | 否 |  | 文件夹编号 |
| space_id | varchar | 128 | 索引 | 否 |  | 空间编号 |
| parent_id | varchar | 128 | 索引 | 否 | 0 | 父文件夹编号 |
| ancestors | varchar | 1280 | 索引 | 否 |  | 祖级列表 |
| folder_name | varchar | 32 |  | 否 |  | 文件夹名称 |
| full_path | varchar | 1024 | 索引 | 否 |  | 完整路径（格式：/文件夹名1/文件夹名2/） |
| folder_level | tinyint |  |  | 否 | 1 | 层级 |
| user_id | varchar | 128 | 索引 | 否 |  | 创建人 |
| sort_order | tinyint |  |  | 否 | 0 | 排序权重 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 创建时间 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| remark | varchar | 128 |  | 是 | NULL | 备注 |

```sql
CREATE TABLE `p_space_folder_info` (
  `folder_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件夹编号',
  `space_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '空间编号',
  `parent_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '父文件夹编号',
  `ancestors` varchar(1280) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '祖级列表',
  `folder_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件夹名称',
  `full_path` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '完整路径（格式：/文件夹名1/文件夹名2/）',
  `folder_level` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '层级',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `sort_order` tinyint NOT NULL DEFAULT '0' COMMENT '排序权重',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`folder_id`) USING BTREE,
  UNIQUE KEY `uk_folder_unique` (`space_id`,`parent_id`,`folder_name`) USING BTREE,
  KEY `parent_id` (`parent_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `idx_full_path` (`full_path`(200)) USING BTREE,
  KEY `idx_ancestors` (`ancestors`(200)) USING BTREE,
  CONSTRAINT `p_space_folder_info_ibfk_1` FOREIGN KEY (`space_id`) REFERENCES `p_space_info` (`space_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `p_space_folder_info_ibfk_3` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='空间文件夹表'
```

### 空间信息表：`p_space_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| space_id | varchar | 128 | 主键 | 否 |  | 空间编号 |
| space_name | varchar | 32 | 索引 | 否 |  | 空间名称 |
| space_avatar | varchar | 512 |  | 是 | NULL | 空间封面URL |
| max_size | bigint |  |  | 是 | 1073741824 | 最大容量（字节） |
| max_count | bigint |  |  | 是 | 1000 | 最大文件数 |
| total_size | bigint |  |  | 是 | 0 | 已用容量（字节） |
| total_count | bigint |  |  | 是 | 0 | 文件总数 |
| look_count | bigint |  |  | 否 | 0 | 查看次数 |
| collect_count | bigint |  |  | 否 | 0 | 收藏次数 |
| download_count | bigint |  |  | 否 | 0 | 下载次数 |
| user_id | varchar | 128 | 索引 | 否 |  | 所属用户 |
| space_desc | varchar | 512 |  | 是 | NULL | 空间描述 |
| space_status | char | 1 | 索引 | 否 |  | 空间状态 |
| space_type | char | 1 | 索引 | 否 | 0 | 空间类型（0个人 1团队 2官方） |
| member_limit | int |  |  | 是 | 10 | 成员上限 |
| current_members | int |  |  | 是 | 0 | 当前成员数 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 创建时间 |
| last_update_time | datetime |  |  | 是 | NULL | 最后上传时间 |
| update_time | datetime |  |  | 是 | NULL | 最后更新时间 |
| is_delete | char | 1 | 索引 | 否 | 0 | 删除（0否 1是） |
| deleted_time | datetime |  |  | 是 | NULL | 删除时间 |

```sql
CREATE TABLE `p_space_info` (
  `space_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '空间编号',
  `space_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '空间名称',
  `space_avatar` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '空间封面URL',
  `max_size` bigint DEFAULT '1073741824' COMMENT '最大容量（字节）',
  `max_count` bigint DEFAULT '1000' COMMENT '最大文件数',
  `total_size` bigint DEFAULT '0' COMMENT '已用容量（字节）',
  `total_count` bigint DEFAULT '0' COMMENT '文件总数',
  `look_count` bigint NOT NULL DEFAULT '0' COMMENT '查看次数',
  `collect_count` bigint NOT NULL DEFAULT '0' COMMENT '收藏次数',
  `download_count` bigint NOT NULL DEFAULT '0' COMMENT '下载次数',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属用户',
  `space_desc` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '空间描述',
  `space_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '空间状态',
  `space_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '空间类型（0个人 1团队 2官方）',
  `member_limit` int DEFAULT '10' COMMENT '成员上限',
  `current_members` int DEFAULT '0' COMMENT '当前成员数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后上传时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除（0否 1是）',
  `deleted_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`space_id`) USING BTREE,
  UNIQUE KEY `uk_space_name` (`space_name`,`user_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `idx_space_type` (`space_type`) USING BTREE,
  KEY `idx_is_deleted` (`is_delete`) USING BTREE,
  KEY `idx_space_status` (`space_status`) USING BTREE,
  CONSTRAINT `p_space_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='空间信息表'
```

### 空间成员邀请记录表：`p_space_invitation_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| invitation_id | varchar | 128 | 主键 | 否 |  | 邀请编号 |
| space_id | varchar | 128 | 索引 | 否 |  | 空间编号 |
| space_name | varchar | 32 |  | 否 |  | 空间名称 |
| space_avatar | varchar | 256 |  | 是 | NULL | 空间封面URL |
| role_type | char | 1 |  | 否 |  | 邀请角色（0创建者 1管理员 2编辑者 3浏览者） |
| invitation_status | char | 1 | 索引 | 否 | 0 | 邀请状态（0待同意 1同意 2拒绝 3过期） |
| invitation_url | varchar | 256 | 唯一索引 | 是 | NULL | 邀请链接（短链或唯一标识） |
| invitation | text |  |  | 是 | NULL | 邀请理由 |
| invitation_user_id | varchar | 128 | 索引 | 否 |  | 邀请人编号 |
| expire_time | datetime |  | 索引 | 否 |  | 过期时间 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 创建时间 |
| user_id | varchar | 128 | 索引 | 否 |  | 被邀请用户编号 |

```sql
CREATE TABLE `p_space_invitation_info` (
  `invitation_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邀请编号',
  `space_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '空间编号',
  `space_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '空间名称',
  `space_avatar` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '空间封面URL',
  `role_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邀请角色（0创建者 1管理员 2编辑者 3浏览者）',
  `invitation_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '邀请状态（0待同意 1同意 2拒绝 3过期）',
  `invitation_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邀请链接（短链或唯一标识）',
  `invitation` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '邀请理由',
  `invitation_user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邀请人编号',
  `expire_time` datetime NOT NULL COMMENT '过期时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '被邀请用户编号',
  PRIMARY KEY (`invitation_id`) USING BTREE,
  UNIQUE KEY `uk_invitation_url` (`invitation_url`) USING BTREE,
  KEY `space_id` (`space_id`) USING BTREE,
  KEY `invitation_user_id` (`invitation_user_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `idx_invitation_status` (`invitation_status`) USING BTREE,
  KEY `idx_expire_time` (`expire_time`) USING BTREE,
  CONSTRAINT `p_space_invitation_info_ibfk_1` FOREIGN KEY (`space_id`) REFERENCES `p_space_info` (`space_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `p_space_invitation_info_ibfk_2` FOREIGN KEY (`invitation_user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `p_space_invitation_info_ibfk_3` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='空间成员邀请记录表'
```

### 空间成员信息表：`p_space_member_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| member_id | varchar | 128 | 主键 | 否 |  | 成员编号 |
| space_id | varchar | 128 | 索引 | 否 |  | 空间编号 |
| user_id | varchar | 128 | 索引 | 否 |  | 用户编号 |
| role_type | char | 1 | 索引 | 否 |  | 角色（0创建者 1管理员 2编辑者 3浏览者） |
| last_active_time | datetime |  |  | 是 | CURRENT_TIMESTAMP | 最后操作时间 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 加入时间 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| inviter_user_id | varchar | 128 | 索引 | 是 | NULL | 邀请人编号 |
| join_type | char | 1 | 索引 | 否 | 0 | 加入方式（0邀请） |
| remark | varchar | 128 |  | 是 | NULL | 备注 |

```sql
CREATE TABLE `p_space_member_info` (
  `member_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '成员编号',
  `space_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '空间编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `role_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色（0创建者 1管理员 2编辑者 3浏览者）',
  `last_active_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后操作时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `inviter_user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邀请人编号',
  `join_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '加入方式（0邀请）',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`member_id`) USING BTREE,
  KEY `space_id` (`space_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `inviter_user_id` (`inviter_user_id`) USING BTREE,
  KEY `idx_role_type` (`role_type`) USING BTREE,
  KEY `idx_join_type` (`join_type`) USING BTREE,
  CONSTRAINT `p_space_member_info_ibfk_1` FOREIGN KEY (`space_id`) REFERENCES `p_space_info` (`space_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `p_space_member_info_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `p_space_member_info_ibfk_3` FOREIGN KEY (`inviter_user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='空间成员信息表'
```

### 统计信息表：`p_statistics_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| statistics_id | varchar | 128 | 主键 | 否 |  | 统计编号 |
| type | varchar | 2 |  | 否 |  | 统计类型 |
| statistics_name | varchar | 32 |  | 否 |  | 统计名称 |
| common_key | varchar | 64 |  | 是 | NULL | 公共KEY |
| statistics_key | varchar | 64 |  | 否 |  | KEY |
| stages | int |  |  | 是 | NULL | 期数 |
| content | text |  |  | 是 | NULL | 统计内容 |
| extend_content | text |  |  | 是 | NULL | 统计内容 |
| remark | text |  |  | 是 | NULL | 描述 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 创建时间 |

```sql
CREATE TABLE `p_statistics_info` (
  `statistics_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '统计编号',
  `type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '统计类型',
  `statistics_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '统计名称',
  `common_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '公共KEY',
  `statistics_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'KEY',
  `stages` int DEFAULT NULL COMMENT '期数',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '统计内容',
  `extend_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '统计内容',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`statistics_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='统计信息表'
```

### 用户行为表：`p_user_behavior_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| behavior_id | varchar | 128 | 主键 | 否 |  | 转发编号 |
| behavior_type | char | 1 |  | 否 |  | 行为类型 |
| user_id | varchar | 128 | 索引 | 否 |  | 用户编号 |
| target_type | char | 1 |  | 否 |  | 目标类型 |
| target_id | varchar | 128 |  | 否 |  | 目标对象 |
| target_content | varchar | 256 |  | 是 | NULL | 目标内容 |
| score | decimal | 5,2 |  | 否 |  | 分数 |
| share_link | varchar | 512 |  | 是 | NULL | 分享链接 |
| category_id | varchar | 128 |  | 是 | NULL | 图片分类 |
| space_id | varchar | 128 |  | 是 | NULL | 空间 |
| tags | varchar | 256 |  | 是 | NULL | 图片标签 |
| target_cover | varchar | 512 |  | 是 | NULL | 封面 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 转发时间 |
| has_statistics | char | 1 |  | 否 | 0 | 是否统计（0否 1是） |
| device_id | varchar | 256 |  | 是 | NULL | 设备唯一标识 |
| browser | varchar | 50 |  | 是 | NULL | 浏览器类型 |
| os | varchar | 50 |  | 是 | NULL | 操作系统 |
| platform | varchar | 20 |  | 是 | NULL | 平台 |
| ip_addr | varchar | 50 |  | 是 | NULL | IP地址 |
| ip_address | varchar | 64 |  | 是 | NULL | IP属地 |

```sql
CREATE TABLE `p_user_behavior_info` (
  `behavior_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '转发编号',
  `behavior_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '行为类型',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `target_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目标类型',
  `target_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目标对象',
  `target_content` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '目标内容',
  `score` decimal(5,2) NOT NULL COMMENT '分数',
  `share_link` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '分享链接',
  `category_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图片分类',
  `space_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '空间',
  `tags` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图片标签',
  `target_cover` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '封面',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '转发时间',
  `has_statistics` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '是否统计（0否 1是）',
  `device_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '平台',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'IP地址',
  `ip_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'IP属地',
  PRIMARY KEY (`behavior_id`) USING BTREE,
  KEY `p_user_behavior_info_user_id_target_type_create_time_index` (`user_id`,`target_type`,`create_time` DESC) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户行为表'
```

### 用户举报信息表：`p_user_report_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| report_id | varchar | 128 | 主键 | 否 |  | 举报编号 |
| user_id | varchar | 128 | 索引 | 否 |  | 用户编号 |
| report_type | char | 1 |  | 否 |  | 举报类型 |
| target_type | char | 1 | 索引 | 否 |  | 目标类型（0图片 1用户 2空间） |
| target_id | varchar | 128 |  | 否 |  | 目标对象编号 |
| target_cover | varchar | 512 |  | 是 | NULL | 封面快照（图片URL/用户头像URL/空间封面URL） |
| target_content | varchar | 256 |  | 是 | NULL | 目标内容 |
| reason | text |  |  | 否 |  | 举报原因 |
| contact | varchar | 512 |  | 是 | NULL | 联系方式 |
| create_time | datetime |  | 索引 | 否 | CURRENT_TIMESTAMP | 举报时间 |
| review_status | char | 1 |  | 否 | 0 | 审核状态（0待审核 1通过 2拒绝） |
| review_message | varchar | 512 |  | 是 | NULL | 审核信息 |
| review_user_id | bigint |  |  | 是 | NULL | 审核人编号 |
| review_time | datetime |  |  | 是 | NULL | 审核时间 |
| device_id | varchar | 256 |  | 是 | NULL | 设备唯一标识 |
| browser | varchar | 50 |  | 是 | NULL | 浏览器类型 |
| os | varchar | 50 |  | 是 | NULL | 操作系统 |
| platform | varchar | 20 |  | 是 | NULL | 平台 |
| ip_addr | varchar | 50 |  | 是 | NULL | IP地址 |
| ip_address | varchar | 64 |  | 是 | NULL | IP属地 |

```sql
CREATE TABLE `p_user_report_info` (
  `report_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '举报编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `report_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '举报类型',
  `target_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目标类型（0图片 1用户 2空间）',
  `target_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '目标对象编号',
  `target_cover` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '封面快照（图片URL/用户头像URL/空间封面URL）',
  `target_content` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '目标内容',
  `reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '举报原因',
  `contact` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '联系方式',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '举报时间',
  `review_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '审核状态（0待审核 1通过 2拒绝）',
  `review_message` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '审核信息',
  `review_user_id` bigint DEFAULT NULL COMMENT '审核人编号',
  `review_time` datetime DEFAULT NULL COMMENT '审核时间',
  `device_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '平台',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'IP地址',
  `ip_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'IP属地',
  PRIMARY KEY (`report_id`) USING BTREE,
  KEY `idx_report_user` (`user_id`) USING BTREE,
  KEY `idx_target` (`target_type`,`target_id`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE,
  CONSTRAINT `fk_report_user` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户举报信息表'
```

### 用户浏览记录表：`p_user_view_log_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| view_id | varchar | 128 | 主键 | 否 |  | 浏览记录编号 |
| user_id | varchar | 128 |  | 是 | NULL | 用户编号 |
| target_type | char | 1 |  | 否 |  | 目标类型 |
| target_id | varchar | 128 |  | 否 |  | 目标对象 |
| target_content | varchar | 256 |  | 是 | NULL | 目标内容 |
| score | decimal | 5,2 |  | 否 |  | 分数 |
| category_id | varchar | 128 |  | 是 | NULL | 图片分类 |
| space_id | varchar | 128 |  | 是 | NULL | 空间 |
| tags | varchar | 256 |  | 是 | NULL | 图片标签 |
| target_cover | varchar | 512 |  | 是 | NULL | 封面 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 查看时间 |
| has_statistics | char | 1 |  | 否 | 0 | 是否统计（0否 1是） |
| device_id | varchar | 256 |  | 是 | NULL | 设备唯一标识 |
| browser | varchar | 50 |  | 是 | NULL | 浏览器类型 |
| os | varchar | 50 |  | 是 | NULL | 操作系统 |
| platform | varchar | 20 |  | 是 | NULL | 平台 |
| ip_addr | varchar | 50 |  | 是 | NULL | IP地址 |
| ip_address | varchar | 64 |  | 是 | NULL | IP属地 |

```sql
CREATE TABLE `p_user_view_log_info` (
  `view_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '浏览记录编号',
  `user_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '用户编号',
  `target_type` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '目标类型',
  `target_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '目标对象',
  `target_content` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '目标内容',
  `score` decimal(5,2) NOT NULL COMMENT '分数',
  `category_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '图片分类',
  `space_id` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '空间',
  `tags` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '图片标签',
  `target_cover` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '封面',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '查看时间',
  `has_statistics` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0' COMMENT '是否统计（0否 1是）',
  `device_id` varchar(256) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '平台',
  `ip_addr` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'IP地址',
  `ip_address` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT 'IP属地',
  PRIMARY KEY (`view_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='用户浏览记录表'
```

### 积分账户表：`po_account_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| account_id | varchar | 128 | 主键 | 否 |  | 账户编号 |
| user_id | varchar | 128 | 唯一索引 | 否 |  | 用户编号 |
| password | varchar | 512 |  | 否 |  | 支付密码 |
| salt | varchar | 64 |  | 否 |  | 加密方式 |
| points_earned | bigint |  |  | 否 | 0 | 赚取总积分 |
| points_used | bigint |  |  | 否 | 0 | 使用总积分 |
| recharge_amount | decimal | 18,2 |  | 否 | 0.00 | 充值总金额（元） |
| account_status | char | 1 | 索引 | 否 | 0 | 账户状态（0正常 1异常 2禁用） |
| points_balance | bigint |  |  | 否 | 0 | 积分余额 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 创建时间 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| remark | varchar | 512 |  | 是 | NULL | 备注 |
| is_delete | char | 1 |  | 否 | 0 | 删除（0否 1是） |

```sql
CREATE TABLE `po_account_info` (
  `account_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账户编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `password` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付密码',
  `salt` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '加密方式',
  `points_earned` bigint NOT NULL DEFAULT '0' COMMENT '赚取总积分',
  `points_used` bigint NOT NULL DEFAULT '0' COMMENT '使用总积分',
  `recharge_amount` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '充值总金额（元）',
  `account_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '账户状态（0正常 1异常 2禁用）',
  `points_balance` bigint NOT NULL DEFAULT '0' COMMENT '积分余额',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除（0否 1是）',
  PRIMARY KEY (`account_id`) USING BTREE,
  UNIQUE KEY `uk_po_account_user` (`user_id`) USING BTREE,
  KEY `idx_po_account_status` (`account_status`) USING BTREE,
  CONSTRAINT `fk_po_account_user` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='积分账户表'
```

### 异常捕获日志表：`po_error_log_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| error_id | varchar | 128 | 主键 | 否 |  | 异常编号 |
| user_id | varchar | 128 | 索引 | 是 | NULL | 用户编号 |
| order_type | char | 1 |  | 否 |  | 订单类型 |
| method_type | varchar | 128 |  | 否 |  | 支付方式 |
| third_party | varchar | 128 |  | 否 |  | 第三方支付平台 |
| third_party_order | varchar | 128 |  | 是 | NULL | 第三方支付平台订单号 |
| error_type | varchar | 32 |  | 否 |  | 异常类型 |
| error_code | text |  |  | 是 | NULL | 返回Code |
| error_msg | text |  |  | 是 | NULL | 返回Msg |
| payment_extend | text |  |  | 是 | NULL | 额外信息 |
| related_order_id | varchar | 128 |  | 是 | NULL | 相关订单编号 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 异常记录时间 |
| device_id | varchar | 255 |  | 是 | NULL | 设备唯一标识 |
| browser | varchar | 50 |  | 是 | NULL | 浏览器类型 |
| os | varchar | 50 |  | 是 | NULL | 操作系统 |
| platform | varchar | 20 |  | 是 | NULL | 平台 |
| ip_addr | varchar | 50 |  | 否 |  | IP地址 |
| ip_address | varchar | 64 |  | 是 | NULL | IP属地 |
| resolve_status | varchar | 32 |  | 否 | 0 | 解决状态 |
| resolve_time | datetime |  |  | 是 | NULL | 解决时间 |
| remark | varchar | 512 |  | 是 | NULL | 备注 |

```sql
CREATE TABLE `po_error_log_info` (
  `error_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '异常编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户编号',
  `order_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单类型',
  `method_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付方式',
  `third_party` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '第三方支付平台',
  `third_party_order` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '第三方支付平台订单号',
  `error_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '异常类型',
  `error_code` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '返回Code',
  `error_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '返回Msg',
  `payment_extend` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '额外信息',
  `related_order_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '相关订单编号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '异常记录时间',
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '平台',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'IP地址',
  `ip_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'IP属地',
  `resolve_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '解决状态',
  `resolve_time` datetime DEFAULT NULL COMMENT '解决时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`error_id`) USING BTREE,
  KEY `fk_po_error_user` (`user_id`) USING BTREE,
  CONSTRAINT `fk_po_error_user` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='异常捕获日志表'
```

### 支付方式表：`po_payment_method_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| method_id | varchar | 128 | 主键 | 否 |  | 支付方式编号 |
| method_name | varchar | 32 |  | 否 |  | 名称 |
| third_party | varchar | 128 |  | 否 |  | 第三方支付平台 |
| method_type | varchar | 32 | 索引 | 否 |  | 类型 |
| api_url | varchar | 256 |  | 是 | NULL | 支付接口URL |
| merchant_id | varchar | 128 |  | 是 | NULL | 商户号 |
| app_id | varchar | 128 |  | 是 | NULL | 应用编号 |
| secret_key | varchar | 512 |  | 是 | NULL | 秘钥 |
| contact_information | varchar | 1024 |  | 是 | NULL | 联系方式 |
| extend_config | varchar | 1024 |  | 是 | NULL | 扩展配置 |
| method_status | varchar | 32 |  | 否 | 1 | 状态（0使用 1未使用） |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 创建时间 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| remark | varchar | 512 |  | 是 | NULL | 备注 |

```sql
CREATE TABLE `po_payment_method_info` (
  `method_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付方式编号',
  `method_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `third_party` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '第三方支付平台',
  `method_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类型',
  `api_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '支付接口URL',
  `merchant_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商户号',
  `app_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '应用编号',
  `secret_key` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '秘钥',
  `contact_information` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '联系方式',
  `extend_config` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '扩展配置',
  `method_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '状态（0使用 1未使用）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`method_id`) USING BTREE,
  KEY `idx_method_type` (`method_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='支付方式表'
```

### 支付订单表：`po_payment_order_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| order_id | varchar | 128 | 主键 | 否 |  | 订单编号 |
| user_id | varchar | 128 | 索引 | 否 |  | 用户编号 |
| order_type | char | 1 |  | 否 |  | 订单类型 |
| order_status | varchar | 32 |  | 否 | 0 | 订单状态 |
| payment_type | varchar | 128 |  | 否 |  | 支付方式 |
| total_amount | decimal | 10,2 |  | 否 | 0.00 | 订单总金额 |
| buyer_pay_amount | decimal | 10,2 |  | 是 | 0.00 | 实付金额 |
| receipt_amount | decimal | 10,2 |  | 是 | 0.00 | 实收金额 |
| discount_amount | decimal | 10,2 |  | 是 | 0.00 | 平台优惠金额 |
| third_party | varchar | 128 |  | 否 |  | 第三方支付平台 |
| third_user_id | varchar | 128 |  | 是 | NULL | 第三方用户编号 |
| third_party_order | varchar | 128 |  | 是 | NULL | 第三方支付平台订单号 |
| payment_time | datetime |  | 索引 | 是 | NULL | 支付时间 |
| payment_status | varchar | 32 |  | 否 | 0 | 支付状态 |
| payment_code | varchar | 128 |  | 是 | NULL | 支付返回Code |
| payment_msg | varchar | 128 |  | 是 | NULL | 支付返回Msg |
| payment_extend | text |  |  | 是 | NULL | 支付返回额外信息 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 创建时间 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| device_id | varchar | 255 |  | 是 | NULL | 设备唯一标识 |
| browser | varchar | 50 |  | 是 | NULL | 浏览器类型 |
| os | varchar | 50 |  | 是 | NULL | 操作系统 |
| platform | varchar | 20 |  | 是 | NULL | 平台 |
| ip_addr | varchar | 50 |  | 否 |  | IP地址 |
| ip_address | varchar | 64 |  | 是 | NULL | IP属地 |
| is_delete | char | 1 |  | 否 | 0 | 删除 |
| remark | varchar | 512 |  | 是 | NULL | 备注 |

```sql
CREATE TABLE `po_payment_order_info` (
  `order_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `order_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单类型',
  `order_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '订单状态',
  `payment_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付方式',
  `total_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '订单总金额',
  `buyer_pay_amount` decimal(10,2) DEFAULT '0.00' COMMENT '实付金额',
  `receipt_amount` decimal(10,2) DEFAULT '0.00' COMMENT '实收金额',
  `discount_amount` decimal(10,2) DEFAULT '0.00' COMMENT '平台优惠金额',
  `third_party` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '第三方支付平台',
  `third_user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '第三方用户编号',
  `third_party_order` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '第三方支付平台订单号',
  `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
  `payment_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '支付状态',
  `payment_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '支付返回Code',
  `payment_msg` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '支付返回Msg',
  `payment_extend` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '支付返回额外信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '平台',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'IP地址',
  `ip_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'IP属地',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`order_id`) USING BTREE,
  KEY `fk_payment_order_user_id` (`user_id`) USING BTREE,
  KEY `idx_po_payment_order_info_payment_time` (`payment_time`) USING BTREE,
  CONSTRAINT `fk_payment_order_user_id` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='支付订单表'
```

### 统计信息表：`po_po_statistics_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| statistics_id | varchar | 128 | 主键 | 否 |  | 统计编号 |
| type | varchar | 2 |  | 否 |  | 统计类型 |
| statistics_name | varchar | 128 |  | 否 |  | 统计名称 |
| common_key | varchar | 128 |  | 否 |  | 公共KEY |
| statistics_key | varchar | 128 |  | 否 |  | KEY |
| stages | int |  |  | 是 | NULL | 期数 |
| content | text |  |  | 是 | NULL | 统计内容 |
| extend_content | text |  |  | 是 | NULL | 统计内容 |
| remark | text |  |  | 是 | NULL | 描述 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 创建时间 |

```sql
CREATE TABLE `po_po_statistics_info` (
  `statistics_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '统计编号',
  `type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '统计类型',
  `statistics_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '统计名称',
  `common_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公共KEY',
  `statistics_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'KEY',
  `stages` int DEFAULT NULL COMMENT '期数',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '统计内容',
  `extend_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '统计内容',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`statistics_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='统计信息表'
```

### 充值记录表：`po_points_recharge_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| recharge_id | varchar | 128 | 主键 | 否 |  | 充值记录编号 |
| package_id | varchar | 128 | 索引 | 否 |  | 套餐编号 |
| user_id | varchar | 128 | 索引 | 否 |  | 用户编号 |
| order_id | varchar | 128 | 索引 | 否 |  | 订单编号 |
| total_count | int |  |  | 否 |  | 总数 |
| points_count | int |  |  | 否 | 0 | 充值积分数量 |
| bonus_count | int |  |  | 是 | 0 | 赠送数量 |
| price_count | decimal | 10,2 |  | 否 | 0.00 | 充值金额 |
| buyer_pay_amount | decimal | 10,2 |  | 是 | 0.00 | 实付金额 |
| recharge_count | int |  |  | 否 | 0 | 数量 |
| payment_type | char | 1 |  | 否 |  | 支付方式 |
| third_party | varchar | 128 |  | 否 |  | 第三方支付平台 |
| third_party_order | varchar | 128 |  | 是 | NULL | 第三方支付平台订单号 |
| recharge_status | varchar | 32 |  | 否 |  | 充值状态 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 充值时间 |
| arrival_time | datetime |  |  | 是 | NULL | 到账时间 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| device_id | varchar | 255 |  | 是 | NULL | 设备唯一标识 |
| browser | varchar | 50 |  | 是 | NULL | 浏览器类型 |
| os | varchar | 50 |  | 是 | NULL | 操作系统 |
| platform | varchar | 20 |  | 是 | NULL | 平台 |
| ip_addr | varchar | 50 |  | 否 |  | IP地址 |
| ip_address | varchar | 64 |  | 是 | NULL | IP属地 |
| remark | varchar | 512 |  | 是 | NULL | 备注 |
| is_delete | char | 1 |  | 否 | 0 | 删除 |

```sql
CREATE TABLE `po_points_recharge_info` (
  `recharge_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '充值记录编号',
  `package_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '套餐编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `order_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `total_count` int NOT NULL COMMENT '总数',
  `points_count` int NOT NULL DEFAULT '0' COMMENT '充值积分数量',
  `bonus_count` int DEFAULT '0' COMMENT '赠送数量',
  `price_count` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '充值金额',
  `buyer_pay_amount` decimal(10,2) DEFAULT '0.00' COMMENT '实付金额',
  `recharge_count` int NOT NULL DEFAULT '0' COMMENT '数量',
  `payment_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付方式',
  `third_party` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '第三方支付平台',
  `third_party_order` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '第三方支付平台订单号',
  `recharge_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '充值状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '充值时间',
  `arrival_time` datetime DEFAULT NULL COMMENT '到账时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '平台',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'IP地址',
  `ip_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'IP属地',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除',
  PRIMARY KEY (`recharge_id`) USING BTREE,
  KEY `fk_points_recharge_user_id` (`user_id`) USING BTREE,
  KEY `fk_points_recharge_order_id` (`order_id`) USING BTREE,
  KEY `fk_points_recharge_package_id` (`package_id`) USING BTREE,
  CONSTRAINT `fk_points_recharge_order_id` FOREIGN KEY (`order_id`) REFERENCES `po_payment_order_info` (`order_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_points_recharge_package_id` FOREIGN KEY (`package_id`) REFERENCES `po_points_recharge_package_info` (`package_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_points_recharge_user_id` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='充值记录表'
```

### 充值积分套餐表：`po_points_recharge_package_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| package_id | varchar | 128 | 主键 | 否 |  | 套餐编号 |
| package_name | varchar | 128 | 唯一索引 | 否 |  | 套餐名称 |
| price | decimal | 10,2 |  | 否 | 0.00 | 套餐价格 |
| points | int |  |  | 否 | 0 | 套餐积分数量 |
| points_bonus | int |  |  | 是 | 0 | 套餐赠送积分 |
| description | varchar | 512 |  | 是 | NULL | 套餐描述 |
| is_long_term | char | 1 |  | 否 | 1 | 是否长期（0是 1否） |
| sort_order | tinyint |  |  | 否 |  | 排序权重 |
| start_time | datetime |  | 索引 | 是 | NULL | 套餐生效时间 |
| end_time | datetime |  |  | 是 | NULL | 套餐结束时间 |
| package_status | char | 1 | 索引 | 否 | 0 | 套餐状态（0正常 1失效） |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 创建时间 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| remark | varchar | 512 |  | 是 | NULL | 备注 |

```sql
CREATE TABLE `po_points_recharge_package_info` (
  `package_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '套餐编号',
  `package_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '套餐名称',
  `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '套餐价格',
  `points` int NOT NULL DEFAULT '0' COMMENT '套餐积分数量',
  `points_bonus` int DEFAULT '0' COMMENT '套餐赠送积分',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '套餐描述',
  `is_long_term` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '是否长期（0是 1否）',
  `sort_order` tinyint NOT NULL COMMENT '排序权重',
  `start_time` datetime DEFAULT NULL COMMENT '套餐生效时间',
  `end_time` datetime DEFAULT NULL COMMENT '套餐结束时间',
  `package_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '套餐状态（0正常 1失效）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`package_id`) USING BTREE,
  UNIQUE KEY `uk_package_name` (`package_name`) USING BTREE,
  KEY `idx_status` (`package_status`) USING BTREE,
  KEY `idx_time_range` (`start_time`,`end_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='充值积分套餐表'
```

### 积分使用记录表：`po_points_usage_log_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| log_id | varchar | 128 | 主键 | 否 |  | 记录编号 |
| user_id | varchar | 128 | 索引 | 否 |  | 用户编号 |
| give_user_id | varchar | 128 | 索引 | 是 | NULL | 给予用户编号 |
| log_type | char | 1 |  | 是 | NULL | 日志类型（0充值 1消费 2提成 3提现） |
| usage_type | varchar | 1 | 索引 | 是 | NULL | 使用类型（0下载图片 1AI服务） |
| target_id | varchar | 128 | 索引 | 是 | NULL | 目标编号 |
| points_before | int |  |  | 否 | 0 | 使用前积分 |
| points_used | int |  |  | 否 | 0 | 消费积分 |
| points_after | int |  |  | 否 | 0 | 使用后积分 |
| device_id | varchar | 255 |  | 是 | NULL | 设备唯一标识 |
| browser | varchar | 50 |  | 是 | NULL | 浏览器类型 |
| os | varchar | 50 |  | 是 | NULL | 操作系统 |
| platform | varchar | 20 |  | 是 | NULL | 平台 |
| ip_addr | varchar | 50 |  | 否 |  | IP地址 |
| ip_address | varchar | 64 |  | 是 | NULL | IP属地 |
| remark | varchar | 512 |  | 是 | NULL | 备注 |
| create_time | datetime |  | 索引 | 否 | CURRENT_TIMESTAMP | 创建时间 |
| update_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 更新时间 |
| is_delete | char | 1 |  | 否 | 0 | 删除（0否 1是） |

```sql
CREATE TABLE `po_points_usage_log_info` (
  `log_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '记录编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `give_user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '给予用户编号',
  `log_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '日志类型（0充值 1消费 2提成 3提现）',
  `usage_type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '使用类型（0下载图片 1AI服务）',
  `target_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '目标编号',
  `points_before` int NOT NULL DEFAULT '0' COMMENT '使用前积分',
  `points_used` int NOT NULL DEFAULT '0' COMMENT '消费积分',
  `points_after` int NOT NULL DEFAULT '0' COMMENT '使用后积分',
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '平台',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'IP地址',
  `ip_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'IP属地',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除（0否 1是）',
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `idx_points_usage_usage_type` (`usage_type`) USING BTREE,
  KEY `idx_points_usage_user_id` (`user_id`) USING BTREE,
  KEY `idx_points_usage_give_user_id` (`give_user_id`) USING BTREE,
  KEY `idx_target` (`target_id`) USING BTREE,
  KEY `idx_po_points_usage_log_info_create_time` (`create_time`) USING BTREE,
  CONSTRAINT `fk_give_user_id` FOREIGN KEY (`give_user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `po_points_usage_log_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='积分使用记录表'
```

### 风控日志表：`po_risk_control_log_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| log_id | varchar | 128 | 主键 | 否 |  | 风控日志编号 |
| user_id | varchar | 128 | 索引 | 否 |  | 用户编号 |
| method_id | varchar | 128 |  | 否 |  | 支付方式 |
| risk_type | varchar | 32 | 索引 | 否 |  | 风险类型 |
| risk_level | varchar | 32 | 索引 | 否 |  | 风险等级（0低 1中 2高） |
| risk_description | text |  |  | 否 |  | 风险描述 |
| action_taken | varchar | 32 |  | 否 |  | 采取措施 |
| action_time | datetime |  | 索引 | 否 | CURRENT_TIMESTAMP | 措施时间 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 记录时间 |
| device_id | varchar | 255 |  | 是 | NULL | 设备唯一标识 |
| browser | varchar | 50 |  | 是 | NULL | 浏览器类型 |
| os | varchar | 50 |  | 是 | NULL | 操作系统 |
| platform | varchar | 20 |  | 是 | NULL | 平台（Web/APP） |
| ip_addr | varchar | 50 |  | 否 |  | IP地址 |
| remark | varchar | 512 |  | 是 | NULL | 备注 |

```sql
CREATE TABLE `po_risk_control_log_info` (
  `log_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '风控日志编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `method_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付方式',
  `risk_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '风险类型',
  `risk_level` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '风险等级（0低 1中 2高）',
  `risk_description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '风险描述',
  `action_taken` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '采取措施',
  `action_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '措施时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '平台（Web/APP）',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'IP地址',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`log_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `idx_risk_level` (`risk_level`) USING BTREE,
  KEY `idx_risk_type` (`risk_type`) USING BTREE,
  KEY `idx_action_time` (`action_time`) USING BTREE,
  CONSTRAINT `po_risk_control_log_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='风控日志表'
```

### 用户提现记录表：`po_withdrawal_order_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| withdrawal_id | varchar | 128 | 主键 | 否 |  | 提现订单编号 |
| user_id | varchar | 128 | 索引 | 是 | NULL | 用户编号 |
| points_withdrawal | int |  |  | 否 | 0 | 提现积分 |
| amount_withdrawal | decimal | 10,2 |  | 否 | 0.00 | 提现金额 |
| platform_fee | decimal | 10,2 |  | 否 | 0.00 | 平台抽成金额 |
| user_received_amount | decimal | 10,2 |  | 否 | 0.00 | 用户实际收到金额 |
| withdrawal_method | char | 1 |  | 否 | 0 | 提现方式（0支付宝 1微信） |
| withdrawal_account | varchar | 64 |  | 否 |  | 提现账户 |
| withdrawal_status | varchar | 32 | 索引 | 否 | 0 | 提现状态（0待处理 1完成 2失败 3超时） |
| withdrawal_platform_order | varchar | 64 |  | 是 | NULL | 提现平台订单号 |
| transaction_id | varchar | 128 |  | 是 | NULL | 交易编号 |
| review_status | char | 1 | 索引 | 否 | 0 | 审核状态（0待审核 1同意 2拒绝） |
| review_time | datetime |  |  | 是 | NULL | 审核时间 |
| review_user_id | bigint |  |  | 是 | NULL | 审核人编号 |
| review_remark | varchar | 512 |  | 是 | NULL | 审核建议 |
| accomplish_time | datetime |  |  | 是 | NULL | 完成时间 |
| device_id | varchar | 255 |  | 是 | NULL | 设备唯一标识 |
| browser | varchar | 50 |  | 是 | NULL | 浏览器类型 |
| os | varchar | 50 |  | 是 | NULL | 操作系统 |
| platform | varchar | 20 |  | 是 | NULL | 平台 |
| ip_addr | varchar | 50 |  | 否 |  | IP地址 |
| create_time | datetime |  | 索引 | 否 | CURRENT_TIMESTAMP | 创建时间 |
| update_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 更新时间 |
| fail_reason | varchar | 500 |  | 是 | NULL | 提现失败原因 |
| is_delete | char | 1 |  | 否 | 0 | 删除（0否 1是） |
| remark | varchar | 512 |  | 是 | NULL | 备注 |

```sql
CREATE TABLE `po_withdrawal_order_info` (
  `withdrawal_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '提现订单编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户编号',
  `points_withdrawal` int NOT NULL DEFAULT '0' COMMENT '提现积分',
  `amount_withdrawal` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '提现金额',
  `platform_fee` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '平台抽成金额',
  `user_received_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '用户实际收到金额',
  `withdrawal_method` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '提现方式（0支付宝 1微信）',
  `withdrawal_account` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '提现账户',
  `withdrawal_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '提现状态（0待处理 1完成 2失败 3超时）',
  `withdrawal_platform_order` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '提现平台订单号',
  `transaction_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '交易编号',
  `review_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '审核状态（0待审核 1同意 2拒绝）',
  `review_time` datetime DEFAULT NULL COMMENT '审核时间',
  `review_user_id` bigint DEFAULT NULL COMMENT '审核人编号',
  `review_remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '审核建议',
  `accomplish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '设备唯一标识',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '平台',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'IP地址',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `fail_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '提现失败原因',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除（0否 1是）',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`withdrawal_id`) USING BTREE,
  KEY `user_id` (`user_id`) USING BTREE,
  KEY `idx_withdrawal_status` (`withdrawal_status`) USING BTREE,
  KEY `idx_review_status` (`review_status`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE,
  CONSTRAINT `po_withdrawal_order_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户提现记录表'
```

### Blob类型的触发器表：`qrtz_blob_triggers`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| sched_name | varchar | 120 | 主键 | 否 |  | 调度名称 |
| trigger_name | varchar | 200 | 主键 | 否 |  | qrtz_triggers表trigger_name的外键 |
| trigger_group | varchar | 200 | 主键 | 否 |  | qrtz_triggers表trigger_group的外键 |
| blob_data | blob |  |  | 是 | NULL | 存放持久化Trigger对象 |

```sql
CREATE TABLE `qrtz_blob_triggers` (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `blob_data` blob COMMENT '存放持久化Trigger对象',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='Blob类型的触发器表'
```

### 日历信息表：`qrtz_calendars`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| sched_name | varchar | 120 | 主键 | 否 |  | 调度名称 |
| calendar_name | varchar | 200 | 主键 | 否 |  | 日历名称 |
| calendar | blob |  |  | 否 |  | 存放持久化calendar对象 |

```sql
CREATE TABLE `qrtz_calendars` (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `calendar_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '日历名称',
  `calendar` blob NOT NULL COMMENT '存放持久化calendar对象',
  PRIMARY KEY (`sched_name`,`calendar_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='日历信息表'
```

### Cron类型的触发器表：`qrtz_cron_triggers`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| sched_name | varchar | 120 | 主键 | 否 |  | 调度名称 |
| trigger_name | varchar | 200 | 主键 | 否 |  | qrtz_triggers表trigger_name的外键 |
| trigger_group | varchar | 200 | 主键 | 否 |  | qrtz_triggers表trigger_group的外键 |
| cron_expression | varchar | 200 |  | 否 |  | cron表达式 |
| time_zone_id | varchar | 80 |  | 是 | NULL | 时区 |

```sql
CREATE TABLE `qrtz_cron_triggers` (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `cron_expression` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'cron表达式',
  `time_zone_id` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '时区',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='Cron类型的触发器表'
```

### 已触发的触发器表：`qrtz_fired_triggers`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| sched_name | varchar | 120 | 主键 | 否 |  | 调度名称 |
| entry_id | varchar | 95 | 主键 | 否 |  | 调度器实例id |
| trigger_name | varchar | 200 |  | 否 |  | qrtz_triggers表trigger_name的外键 |
| trigger_group | varchar | 200 |  | 否 |  | qrtz_triggers表trigger_group的外键 |
| instance_name | varchar | 200 |  | 否 |  | 调度器实例名 |
| fired_time | bigint |  |  | 否 |  | 触发的时间 |
| sched_time | bigint |  |  | 否 |  | 定时器制定的时间 |
| priority | int |  |  | 否 |  | 优先级 |
| state | varchar | 16 |  | 否 |  | 状态 |
| job_name | varchar | 200 |  | 是 | NULL | 任务名称 |
| job_group | varchar | 200 |  | 是 | NULL | 任务组名 |
| is_nonconcurrent | varchar | 1 |  | 是 | NULL | 是否并发 |
| requests_recovery | varchar | 1 |  | 是 | NULL | 是否接受恢复执行 |

```sql
CREATE TABLE `qrtz_fired_triggers` (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `entry_id` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度器实例id',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `instance_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度器实例名',
  `fired_time` bigint NOT NULL COMMENT '触发的时间',
  `sched_time` bigint NOT NULL COMMENT '定时器制定的时间',
  `priority` int NOT NULL COMMENT '优先级',
  `state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态',
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '任务组名',
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '是否并发',
  `requests_recovery` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '是否接受恢复执行',
  PRIMARY KEY (`sched_name`,`entry_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='已触发的触发器表'
```

### 任务详细信息表：`qrtz_job_details`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| sched_name | varchar | 120 | 主键 | 否 |  | 调度名称 |
| job_name | varchar | 200 | 主键 | 否 |  | 任务名称 |
| job_group | varchar | 200 | 主键 | 否 |  | 任务组名 |
| description | varchar | 250 |  | 是 | NULL | 相关介绍 |
| job_class_name | varchar | 250 |  | 否 |  | 执行任务类名称 |
| is_durable | varchar | 1 |  | 否 |  | 是否持久化 |
| is_nonconcurrent | varchar | 1 |  | 否 |  | 是否并发 |
| is_update_data | varchar | 1 |  | 否 |  | 是否更新数据 |
| requests_recovery | varchar | 1 |  | 否 |  | 是否接受恢复执行 |
| job_data | blob |  |  | 是 | NULL | 存放持久化job对象 |

```sql
CREATE TABLE `qrtz_job_details` (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务组名',
  `description` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '相关介绍',
  `job_class_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '执行任务类名称',
  `is_durable` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '是否持久化',
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '是否并发',
  `is_update_data` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '是否更新数据',
  `requests_recovery` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '是否接受恢复执行',
  `job_data` blob COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`,`job_name`,`job_group`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='任务详细信息表'
```

### 存储的悲观锁信息表：`qrtz_locks`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| sched_name | varchar | 120 | 主键 | 否 |  | 调度名称 |
| lock_name | varchar | 40 | 主键 | 否 |  | 悲观锁名称 |

```sql
CREATE TABLE `qrtz_locks` (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `lock_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '悲观锁名称',
  PRIMARY KEY (`sched_name`,`lock_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='存储的悲观锁信息表'
```

### 暂停的触发器表：`qrtz_paused_trigger_grps`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| sched_name | varchar | 120 | 主键 | 否 |  | 调度名称 |
| trigger_group | varchar | 200 | 主键 | 否 |  | qrtz_triggers表trigger_group的外键 |

```sql
CREATE TABLE `qrtz_paused_trigger_grps` (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  PRIMARY KEY (`sched_name`,`trigger_group`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='暂停的触发器表'
```

### 调度器状态表：`qrtz_scheduler_state`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| sched_name | varchar | 120 | 主键 | 否 |  | 调度名称 |
| instance_name | varchar | 200 | 主键 | 否 |  | 实例名称 |
| last_checkin_time | bigint |  |  | 否 |  | 上次检查时间 |
| checkin_interval | bigint |  |  | 否 |  | 检查间隔时间 |

```sql
CREATE TABLE `qrtz_scheduler_state` (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `instance_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '实例名称',
  `last_checkin_time` bigint NOT NULL COMMENT '上次检查时间',
  `checkin_interval` bigint NOT NULL COMMENT '检查间隔时间',
  PRIMARY KEY (`sched_name`,`instance_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='调度器状态表'
```

### 简单触发器的信息表：`qrtz_simple_triggers`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| sched_name | varchar | 120 | 主键 | 否 |  | 调度名称 |
| trigger_name | varchar | 200 | 主键 | 否 |  | qrtz_triggers表trigger_name的外键 |
| trigger_group | varchar | 200 | 主键 | 否 |  | qrtz_triggers表trigger_group的外键 |
| repeat_count | bigint |  |  | 否 |  | 重复的次数统计 |
| repeat_interval | bigint |  |  | 否 |  | 重复的间隔时间 |
| times_triggered | bigint |  |  | 否 |  | 已经触发的次数 |

```sql
CREATE TABLE `qrtz_simple_triggers` (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `repeat_count` bigint NOT NULL COMMENT '重复的次数统计',
  `repeat_interval` bigint NOT NULL COMMENT '重复的间隔时间',
  `times_triggered` bigint NOT NULL COMMENT '已经触发的次数',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='简单触发器的信息表'
```

### 同步机制的行锁表：`qrtz_simprop_triggers`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| sched_name | varchar | 120 | 主键 | 否 |  | 调度名称 |
| trigger_name | varchar | 200 | 主键 | 否 |  | qrtz_triggers表trigger_name的外键 |
| trigger_group | varchar | 200 | 主键 | 否 |  | qrtz_triggers表trigger_group的外键 |
| str_prop_1 | varchar | 512 |  | 是 | NULL | String类型的trigger的第一个参数 |
| str_prop_2 | varchar | 512 |  | 是 | NULL | String类型的trigger的第二个参数 |
| str_prop_3 | varchar | 512 |  | 是 | NULL | String类型的trigger的第三个参数 |
| int_prop_1 | int |  |  | 是 | NULL | int类型的trigger的第一个参数 |
| int_prop_2 | int |  |  | 是 | NULL | int类型的trigger的第二个参数 |
| long_prop_1 | bigint |  |  | 是 | NULL | long类型的trigger的第一个参数 |
| long_prop_2 | bigint |  |  | 是 | NULL | long类型的trigger的第二个参数 |
| dec_prop_1 | decimal | 13,4 |  | 是 | NULL | decimal类型的trigger的第一个参数 |
| dec_prop_2 | decimal | 13,4 |  | 是 | NULL | decimal类型的trigger的第二个参数 |
| bool_prop_1 | varchar | 1 |  | 是 | NULL | Boolean类型的trigger的第一个参数 |
| bool_prop_2 | varchar | 1 |  | 是 | NULL | Boolean类型的trigger的第二个参数 |

```sql
CREATE TABLE `qrtz_simprop_triggers` (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `str_prop_1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'String类型的trigger的第一个参数',
  `str_prop_2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'String类型的trigger的第二个参数',
  `str_prop_3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'String类型的trigger的第三个参数',
  `int_prop_1` int DEFAULT NULL COMMENT 'int类型的trigger的第一个参数',
  `int_prop_2` int DEFAULT NULL COMMENT 'int类型的trigger的第二个参数',
  `long_prop_1` bigint DEFAULT NULL COMMENT 'long类型的trigger的第一个参数',
  `long_prop_2` bigint DEFAULT NULL COMMENT 'long类型的trigger的第二个参数',
  `dec_prop_1` decimal(13,4) DEFAULT NULL COMMENT 'decimal类型的trigger的第一个参数',
  `dec_prop_2` decimal(13,4) DEFAULT NULL COMMENT 'decimal类型的trigger的第二个参数',
  `bool_prop_1` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Boolean类型的trigger的第一个参数',
  `bool_prop_2` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'Boolean类型的trigger的第二个参数',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='同步机制的行锁表'
```

### 触发器详细信息表：`qrtz_triggers`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| sched_name | varchar | 120 | 主键 | 否 |  | 调度名称 |
| trigger_name | varchar | 200 | 主键 | 否 |  | 触发器的名字 |
| trigger_group | varchar | 200 | 主键 | 否 |  | 触发器所属组的名字 |
| job_name | varchar | 200 |  | 否 |  | qrtz_job_details表job_name的外键 |
| job_group | varchar | 200 |  | 否 |  | qrtz_job_details表job_group的外键 |
| description | varchar | 250 |  | 是 | NULL | 相关介绍 |
| next_fire_time | bigint |  |  | 是 | NULL | 上一次触发时间（毫秒） |
| prev_fire_time | bigint |  |  | 是 | NULL | 下一次触发时间（默认为-1表示不触发） |
| priority | int |  |  | 是 | NULL | 优先级 |
| trigger_state | varchar | 16 |  | 否 |  | 触发器状态 |
| trigger_type | varchar | 8 |  | 否 |  | 触发器的类型 |
| start_time | bigint |  |  | 否 |  | 开始时间 |
| end_time | bigint |  |  | 是 | NULL | 结束时间 |
| calendar_name | varchar | 200 |  | 是 | NULL | 日程表名称 |
| misfire_instr | smallint |  |  | 是 | NULL | 补偿执行的策略 |
| job_data | blob |  |  | 是 | NULL | 存放持久化job对象 |

```sql
CREATE TABLE `qrtz_triggers` (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '触发器的名字',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '触发器所属组的名字',
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_job_details表job_name的外键',
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'qrtz_job_details表job_group的外键',
  `description` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '相关介绍',
  `next_fire_time` bigint DEFAULT NULL COMMENT '上一次触发时间（毫秒）',
  `prev_fire_time` bigint DEFAULT NULL COMMENT '下一次触发时间（默认为-1表示不触发）',
  `priority` int DEFAULT NULL COMMENT '优先级',
  `trigger_state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '触发器状态',
  `trigger_type` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '触发器的类型',
  `start_time` bigint NOT NULL COMMENT '开始时间',
  `end_time` bigint DEFAULT NULL COMMENT '结束时间',
  `calendar_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '日程表名称',
  `misfire_instr` smallint DEFAULT NULL COMMENT '补偿执行的策略',
  `job_data` blob COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
  KEY `sched_name` (`sched_name`,`job_name`,`job_group`) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='触发器详细信息表'
```

### 参数配置表：`sys_config`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| config_id | int |  | 主键 | 否 | 自增 | 参数主键 |
| config_name | varchar | 100 |  | 是 |  | 参数名称 |
| config_key | varchar | 100 |  | 是 |  | 参数键名 |
| config_value | varchar | 500 |  | 是 |  | 参数键值 |
| config_type | char | 1 |  | 是 | N | 系统内置（Y是 N否） |
| create_by | varchar | 64 |  | 是 |  | 创建者 |
| create_time | datetime |  |  | 是 | NULL | 创建时间 |
| update_by | varchar | 64 |  | 是 |  | 更新者 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| remark | varchar | 500 |  | 是 | NULL | 备注 |

```sql
CREATE TABLE `sys_config` (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='参数配置表'
```

### 部门表：`sys_dept`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| dept_id | bigint |  | 主键 | 否 | 自增 | 部门id |
| parent_id | bigint |  |  | 是 | 0 | 父部门id |
| ancestors | varchar | 50 |  | 是 |  | 祖级列表 |
| dept_name | varchar | 30 |  | 是 |  | 部门名称 |
| order_num | int |  |  | 是 | 0 | 显示顺序 |
| leader | varchar | 20 |  | 是 | NULL | 负责人 |
| phone | varchar | 11 |  | 是 | NULL | 联系电话 |
| email | varchar | 50 |  | 是 | NULL | 邮箱 |
| status | char | 1 |  | 是 | 0 | 部门状态（0正常 1停用） |
| del_flag | char | 1 |  | 是 | 0 | 删除标志（0代表存在 2代表删除） |
| create_by | varchar | 64 |  | 是 |  | 创建者 |
| create_time | datetime |  |  | 是 | NULL | 创建时间 |
| update_by | varchar | 64 |  | 是 |  | 更新者 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |

```sql
CREATE TABLE `sys_dept` (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint DEFAULT '0' COMMENT '父部门id',
  `ancestors` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '部门名称',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='部门表'
```

### 字典数据表：`sys_dict_data`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| dict_code | bigint |  | 主键 | 否 | 自增 | 字典编码 |
| dict_sort | int |  |  | 是 | 0 | 字典排序 |
| dict_label | varchar | 100 |  | 是 |  | 字典标签 |
| dict_value | varchar | 100 |  | 是 |  | 字典键值 |
| dict_type | varchar | 100 |  | 是 |  | 字典类型 |
| css_class | varchar | 100 |  | 是 | NULL | 样式属性（其他样式扩展） |
| list_class | varchar | 100 |  | 是 | NULL | 表格回显样式 |
| is_default | char | 1 |  | 是 | N | 是否默认（Y是 N否） |
| status | char | 1 |  | 是 | 0 | 状态（0正常 1停用） |
| create_by | varchar | 64 |  | 是 |  | 创建者 |
| create_time | datetime |  |  | 是 | NULL | 创建时间 |
| update_by | varchar | 64 |  | 是 |  | 更新者 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| remark | varchar | 500 |  | 是 | NULL | 备注 |

```sql
CREATE TABLE `sys_dict_data` (
  `dict_code` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int DEFAULT '0' COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=406 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='字典数据表'
```

### 字典类型表：`sys_dict_type`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| dict_id | bigint |  | 主键 | 否 | 自增 | 字典主键 |
| dict_name | varchar | 100 |  | 是 |  | 字典名称 |
| dict_type | varchar | 100 | 唯一索引 | 是 |  | 字典类型 |
| status | char | 1 |  | 是 | 0 | 状态（0正常 1停用） |
| create_by | varchar | 64 |  | 是 |  | 创建者 |
| create_time | datetime |  |  | 是 | NULL | 创建时间 |
| update_by | varchar | 64 |  | 是 |  | 更新者 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| remark | varchar | 500 |  | 是 | NULL | 备注 |

```sql
CREATE TABLE `sys_dict_type` (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE KEY `dict_type` (`dict_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=214 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='字典类型表'
```

### 定时任务调度表：`sys_job`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| job_id | bigint |  | 主键 | 否 | 自增 | 任务ID |
| job_name | varchar | 64 | 主键 | 否 |  | 任务名称 |
| job_group | varchar | 64 | 主键 | 否 | DEFAULT | 任务组名 |
| invoke_target | varchar | 500 |  | 否 |  | 调用目标字符串 |
| cron_expression | varchar | 255 |  | 是 |  | cron执行表达式 |
| misfire_policy | varchar | 20 |  | 是 | 3 | 计划执行错误策略（1立即执行 2执行一次 3放弃执行） |
| concurrent | char | 1 |  | 是 | 1 | 是否并发执行（0允许 1禁止） |
| status | char | 1 |  | 是 | 0 | 状态（0正常 1暂停） |
| create_by | varchar | 64 |  | 是 |  | 创建者 |
| create_time | datetime |  |  | 是 | NULL | 创建时间 |
| update_by | varchar | 64 |  | 是 |  | 更新者 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| remark | varchar | 500 |  | 是 |  | 备注信息 |

```sql
CREATE TABLE `sys_job` (
  `job_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`,`job_name`,`job_group`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='定时任务调度表'
```

### 定时任务调度日志表：`sys_job_log`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| job_log_id | bigint |  | 主键 | 否 | 自增 | 任务日志ID |
| job_name | varchar | 64 |  | 否 |  | 任务名称 |
| job_group | varchar | 64 |  | 否 |  | 任务组名 |
| invoke_target | varchar | 500 |  | 否 |  | 调用目标字符串 |
| job_message | varchar | 500 |  | 是 | NULL | 日志信息 |
| status | char | 1 |  | 是 | 0 | 执行状态（0正常 1失败） |
| exception_info | varchar | 2000 |  | 是 |  | 异常信息 |
| create_time | datetime |  |  | 是 | NULL | 创建时间 |

```sql
CREATE TABLE `sys_job_log` (
  `job_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '日志信息',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '异常信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=132 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='定时任务调度日志表'
```

### 系统访问记录：`sys_logininfor`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| info_id | bigint |  | 主键 | 否 | 自增 | 访问ID |
| user_name | varchar | 50 |  | 是 |  | 用户账号 |
| ipaddr | varchar | 128 |  | 是 |  | 登录IP地址 |
| login_location | varchar | 255 |  | 是 |  | 登录地点 |
| browser | varchar | 50 |  | 是 |  | 浏览器类型 |
| os | varchar | 50 |  | 是 |  | 操作系统 |
| status | char | 1 | 索引 | 是 | 0 | 登录状态（0成功 1失败） |
| msg | varchar | 255 |  | 是 |  | 提示消息 |
| login_time | datetime |  | 索引 | 是 | NULL | 访问时间 |

```sql
CREATE TABLE `sys_logininfor` (
  `info_id` bigint NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '操作系统',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '提示消息',
  `login_time` datetime DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE,
  KEY `idx_sys_logininfor_s` (`status`) USING BTREE,
  KEY `idx_sys_logininfor_lt` (`login_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1182 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='系统访问记录'
```

### 菜单权限表：`sys_menu`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| menu_id | bigint |  | 主键 | 否 | 自增 | 菜单ID |
| menu_name | varchar | 50 |  | 否 |  | 菜单名称 |
| parent_id | bigint |  |  | 是 | 0 | 父菜单ID |
| order_num | int |  |  | 是 | 0 | 显示顺序 |
| path | varchar | 200 |  | 是 |  | 路由地址 |
| component | varchar | 255 |  | 是 | NULL | 组件路径 |
| query | varchar | 255 |  | 是 | NULL | 路由参数 |
| route_name | varchar | 50 |  | 是 |  | 路由名称 |
| is_frame | int |  |  | 是 | 1 | 是否为外链（0是 1否） |
| is_cache | int |  |  | 是 | 0 | 是否缓存（0缓存 1不缓存） |
| menu_type | char | 1 |  | 是 |  | 菜单类型（M目录 C菜单 F按钮） |
| visible | char | 1 |  | 是 | 0 | 菜单状态（0显示 1隐藏） |
| status | char | 1 |  | 是 | 0 | 菜单状态（0正常 1停用） |
| perms | varchar | 100 |  | 是 | NULL | 权限标识 |
| icon | varchar | 100 |  | 是 | # | 菜单图标 |
| create_by | varchar | 64 |  | 是 |  | 创建者 |
| create_time | datetime |  |  | 是 | NULL | 创建时间 |
| update_by | varchar | 64 |  | 是 |  | 更新者 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| remark | varchar | 500 |  | 是 |  | 备注 |

```sql
CREATE TABLE `sys_menu` (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '路由名称',
  `is_frame` int DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` int DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2379 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='菜单权限表'
```

### 通知公告表：`sys_notice`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| notice_id | int |  | 主键 | 否 | 自增 | 公告ID |
| notice_title | varchar | 50 |  | 否 |  | 公告标题 |
| notice_type | char | 1 |  | 否 |  | 公告类型（1通知 2公告） |
| notice_content | longblob |  |  | 是 | NULL | 公告内容 |
| status | char | 1 |  | 是 | 0 | 公告状态（0正常 1关闭） |
| create_by | varchar | 64 |  | 是 |  | 创建者 |
| create_time | datetime |  |  | 是 | NULL | 创建时间 |
| update_by | varchar | 64 |  | 是 |  | 更新者 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| remark | varchar | 255 |  | 是 | NULL | 备注 |

```sql
CREATE TABLE `sys_notice` (
  `notice_id` int NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob COMMENT '公告内容',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='通知公告表'
```

### 操作日志记录：`sys_oper_log`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| oper_id | bigint |  | 主键 | 否 | 自增 | 日志主键 |
| title | varchar | 50 |  | 是 |  | 模块标题 |
| business_type | int |  | 索引 | 是 | 0 | 业务类型（0其它 1新增 2修改 3删除） |
| method | varchar | 200 |  | 是 |  | 方法名称 |
| request_method | varchar | 10 |  | 是 |  | 请求方式 |
| operator_type | int |  |  | 是 | 0 | 操作类别（0其它 1后台用户 2手机端用户） |
| oper_name | varchar | 50 |  | 是 |  | 操作人员 |
| dept_name | varchar | 50 |  | 是 |  | 部门名称 |
| oper_url | varchar | 255 |  | 是 |  | 请求URL |
| oper_ip | varchar | 128 |  | 是 |  | 主机地址 |
| oper_location | varchar | 255 |  | 是 |  | 操作地点 |
| oper_param | varchar | 2000 |  | 是 |  | 请求参数 |
| json_result | varchar | 2000 |  | 是 |  | 返回参数 |
| status | int |  | 索引 | 是 | 0 | 操作状态（0正常 1异常） |
| error_msg | varchar | 2000 |  | 是 |  | 错误消息 |
| oper_time | datetime |  | 索引 | 是 | NULL | 操作时间 |
| cost_time | bigint |  |  | 是 | 0 | 消耗时间 |

```sql
CREATE TABLE `sys_oper_log` (
  `oper_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '模块标题',
  `business_type` int DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '请求方式',
  `operator_type` int DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '返回参数',
  `status` int DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint DEFAULT '0' COMMENT '消耗时间',
  PRIMARY KEY (`oper_id`) USING BTREE,
  KEY `idx_sys_oper_log_bt` (`business_type`) USING BTREE,
  KEY `idx_sys_oper_log_s` (`status`) USING BTREE,
  KEY `idx_sys_oper_log_ot` (`oper_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2303 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='操作日志记录'
```

### 岗位信息表：`sys_post`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| post_id | bigint |  | 主键 | 否 | 自增 | 岗位ID |
| post_code | varchar | 64 |  | 否 |  | 岗位编码 |
| post_name | varchar | 50 |  | 否 |  | 岗位名称 |
| post_sort | int |  |  | 否 |  | 显示顺序 |
| status | char | 1 |  | 否 |  | 状态（0正常 1停用） |
| create_by | varchar | 64 |  | 是 |  | 创建者 |
| create_time | datetime |  |  | 是 | NULL | 创建时间 |
| update_by | varchar | 64 |  | 是 |  | 更新者 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| remark | varchar | 500 |  | 是 | NULL | 备注 |

```sql
CREATE TABLE `sys_post` (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int NOT NULL COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='岗位信息表'
```

### 角色信息表：`sys_role`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| role_id | bigint |  | 主键 | 否 | 自增 | 角色ID |
| role_name | varchar | 30 |  | 否 |  | 角色名称 |
| role_key | varchar | 100 |  | 否 |  | 角色权限字符串 |
| role_sort | int |  |  | 否 |  | 显示顺序 |
| data_scope | char | 1 |  | 是 | 1 | 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限） |
| menu_check_strictly | tinyint | 1 |  | 是 | 1 | 菜单树选择项是否关联显示 |
| dept_check_strictly | tinyint | 1 |  | 是 | 1 | 部门树选择项是否关联显示 |
| status | char | 1 |  | 否 |  | 角色状态（0正常 1停用） |
| del_flag | char | 1 |  | 是 | 0 | 删除标志（0代表存在 2代表删除） |
| create_by | varchar | 64 |  | 是 |  | 创建者 |
| create_time | datetime |  |  | 是 | NULL | 创建时间 |
| update_by | varchar | 64 |  | 是 |  | 更新者 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| remark | varchar | 500 |  | 是 | NULL | 备注 |

```sql
CREATE TABLE `sys_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) DEFAULT '1' COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='角色信息表'
```

### 角色和部门关联表：`sys_role_dept`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| role_id | bigint |  | 主键 | 否 |  | 角色ID |
| dept_id | bigint |  | 主键 | 否 |  | 部门ID |

```sql
CREATE TABLE `sys_role_dept` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`,`dept_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='角色和部门关联表'
```

### 角色和菜单关联表：`sys_role_menu`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| role_id | bigint |  | 主键 | 否 |  | 角色ID |
| menu_id | bigint |  | 主键 | 否 |  | 菜单ID |

```sql
CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='角色和菜单关联表'
```

### 用户信息表：`sys_user`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| user_id | bigint |  | 主键 | 否 | 自增 | 用户ID |
| dept_id | bigint |  |  | 是 | NULL | 部门ID |
| user_name | varchar | 30 |  | 否 |  | 用户账号 |
| nick_name | varchar | 30 |  | 否 |  | 用户昵称 |
| user_type | varchar | 2 |  | 是 | 00 | 用户类型（00系统用户） |
| email | varchar | 50 |  | 是 |  | 用户邮箱 |
| phonenumber | varchar | 11 |  | 是 |  | 手机号码 |
| sex | char | 1 |  | 是 | 0 | 用户性别（0男 1女 2未知） |
| avatar | varchar | 100 |  | 是 |  | 头像地址 |
| password | varchar | 100 |  | 是 |  | 密码 |
| status | char | 1 |  | 是 | 0 | 帐号状态（0正常 1停用） |
| del_flag | char | 1 |  | 是 | 0 | 删除标志（0代表存在 2代表删除） |
| login_ip | varchar | 128 |  | 是 |  | 最后登录IP |
| login_date | datetime |  |  | 是 | NULL | 最后登录时间 |
| create_by | varchar | 64 |  | 是 |  | 创建者 |
| create_time | datetime |  |  | 是 | NULL | 创建时间 |
| update_by | varchar | 64 |  | 是 |  | 更新者 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| remark | varchar | 500 |  | 是 | NULL | 备注 |

```sql
CREATE TABLE `sys_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户信息表'
```

### 用户与岗位关联表：`sys_user_post`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| user_id | bigint |  | 主键 | 否 |  | 用户ID |
| post_id | bigint |  | 主键 | 否 |  | 岗位ID |

```sql
CREATE TABLE `sys_user_post` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`post_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户与岗位关联表'
```

### 用户和角色关联表：`sys_user_role`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| user_id | bigint |  | 主键 | 否 |  | 用户ID |
| role_id | bigint |  | 主键 | 否 |  | 角色ID |

```sql
CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户和角色关联表'
```

### 用户封禁权限表：`u_banned_permission_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| banned_id | varchar | 128 | 主键 | 否 |  | 封禁记录编号 |
| permission_name | varchar | 50 | 索引 | 否 |  | 权限名称 |
| user_id | varchar | 128 | 索引 | 否 |  | 用户 |
| start_time | datetime |  |  | 否 |  | 开始时间 |
| end_time | datetime |  |  | 否 |  | 结束时间 |
| status | char | 1 |  | 否 | 0 | 状态（0=封禁中 1=结束） |
| cause | varchar | 512 |  | 是 | NULL | 封禁原因 |

```sql
CREATE TABLE `u_banned_permission_info` (
  `banned_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '封禁记录编号',
  `permission_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限名称',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '状态（0=封禁中 1=结束）',
  `cause` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '封禁原因',
  PRIMARY KEY (`banned_id`) USING BTREE,
  KEY `idx_banned_user` (`user_id`) USING BTREE,
  KEY `idx_banned_permission` (`permission_name`) USING BTREE,
  CONSTRAINT `fk_banned_permission_name` FOREIGN KEY (`permission_name`) REFERENCES `c_permission_info` (`permission_name`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_banned_user_id` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `chk_time_sequence` CHECK ((`end_time` > `start_time`))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户封禁权限表'
```

### 用户通知记录表：`u_inform_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| record_id | varchar | 128 | 主键 | 否 |  | 通知记录编号 |
| template_key | varchar | 128 |  | 否 |  | 模板KEY |
| template_type | char | 1 |  | 否 |  | 模版类型（1短信 2邮件 3站内通知 4APP推送 5微信模板） |
| locale | varchar | 8 | 索引 | 否 |  | 语言（默认zh-CN） |
| inform_title | varchar | 128 |  | 是 | NULL | 通知标题 |
| user_id | varchar | 128 | 索引 | 否 |  | 用户编号 |
| content | text |  |  | 否 |  | 实际发送内容 |
| inform_type | varchar | 32 |  | 否 |  | 通知类型 |
| status | char | 1 |  | 否 | 0 | 发送状态（0=待发送 1=已发送 2=发送失败 3=已撤回） |
| is_read | char | 1 |  | 否 | 0 | 是否已读（0=未读 1=已读） |
| read_time | datetime |  |  | 是 | NULL | 读取时间 |
| retry_count | int |  |  | 否 | 0 | 重试次数 |
| send_time | datetime |  | 索引 | 否 | CURRENT_TIMESTAMP | 发送时间 |
| remark | varchar | 500 |  | 是 | NULL | 备注 |
| is_delete | char | 1 |  | 否 | 0 | 删除（0=正常 1=删除） |

```sql
CREATE TABLE `u_inform_info` (
  `record_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知记录编号',
  `template_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模板KEY',
  `template_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模版类型（1短信 2邮件 3站内通知 4APP推送 5微信模板）',
  `locale` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '语言（默认zh-CN）',
  `inform_title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '通知标题',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户编号',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '实际发送内容',
  `inform_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '发送状态（0=待发送 1=已发送 2=发送失败 3=已撤回）',
  `is_read` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '是否已读（0=未读 1=已读）',
  `read_time` datetime DEFAULT NULL COMMENT '读取时间',
  `retry_count` int NOT NULL DEFAULT '0' COMMENT '重试次数',
  `send_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除（0=正常 1=删除）',
  PRIMARY KEY (`record_id`) USING BTREE,
  KEY `idx_send_time` (`send_time`) USING BTREE,
  KEY `idx_user_status` (`user_id`,`status`) USING BTREE,
  KEY `fk_inform_locale` (`locale`) USING BTREE,
  CONSTRAINT `fk_inform_locale` FOREIGN KEY (`locale`) REFERENCES `c_i18n_locale_info` (`locale`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_inform_user` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户通知记录表'
```

### 用户登录日志表：`u_login_log_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| info_id | varchar | 128 | 主键 | 否 |  | 编号 |
| user_id | varchar | 128 | 索引 | 是 | NULL | 用户ID |
| user_name | varchar | 36 |  | 是 | NULL | 用户名 |
| login_type | varchar | 20 |  | 否 |  | 登录方式 |
| identifier | varchar | 128 |  | 是 | NULL | 匿名标识 |
| ipaddr | varchar | 50 |  | 否 |  | 登录IP地址 |
| login_location | varchar | 255 | 索引 | 是 | NULL | 登录地点 |
| browser | varchar | 50 |  | 是 | NULL | 浏览器类型 |
| os | varchar | 50 |  | 是 | NULL | 操作系统 |
| platform | varchar | 20 | 索引 | 是 | NULL | 登录平台 |
| device_id | varchar | 255 |  | 是 | NULL | 设备唯一标识 |
| status | char | 1 |  | 否 | 0 | 状态（0成功 1失败） |
| error_code | varchar | 64 |  | 是 | NULL | 错误码 |
| msg | varchar | 255 |  | 是 | NULL | 提示消息 |
| login_time | datetime |  | 索引 | 否 |  | 登录时间 |

```sql
CREATE TABLE `u_login_log_info` (
  `info_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '编号',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户ID',
  `user_name` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户名',
  `login_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录方式',
  `identifier` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '匿名标识',
  `ipaddr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作系统',
  `platform` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '登录平台',
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '设备唯一标识',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '状态（0成功 1失败）',
  `error_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '错误码',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '提示消息',
  `login_time` datetime NOT NULL COMMENT '登录时间',
  PRIMARY KEY (`info_id`) USING BTREE,
  KEY `idx_login_time` (`login_time`) USING BTREE,
  KEY `idx_platform_device` (`platform`,`device_id`(64)) USING BTREE,
  KEY `idx_user_status` (`user_id`,`status`) USING BTREE,
  KEY `idx_login_log_info_login_location` (`login_location`) USING BTREE,
  CONSTRAINT `u_login_log_info_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户登录日志表'
```

### 统计信息表：`u_statistics_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| statistics_id | varchar | 128 | 主键 | 否 |  | 统计编号 |
| type | varchar | 2 |  | 否 |  | 统计类型 |
| statistics_name | varchar | 128 |  | 否 |  | 统计名称 |
| common_key | varchar | 128 |  | 否 |  | 公共KEY |
| statistics_key | varchar | 128 |  | 否 |  | KEY |
| stages | int |  |  | 是 | NULL | 期数 |
| content | text |  |  | 是 | NULL | 统计内容 |
| extend_content | text |  |  | 是 | NULL | 统计内容 |
| remark | text |  |  | 是 | NULL | 描述 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 创建时间 |

```sql
CREATE TABLE `u_statistics_info` (
  `statistics_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '统计编号',
  `type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '统计类型',
  `statistics_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '统计名称',
  `common_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公共KEY',
  `statistics_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'KEY',
  `stages` int DEFAULT NULL COMMENT '期数',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '统计内容',
  `extend_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '统计内容',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`statistics_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='统计信息表'
```

### 统计信息表：`u_u_statistics_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| statistics_id | varchar | 128 | 主键 | 否 |  | 统计编号 |
| type | varchar | 2 |  | 否 |  | 统计类型 |
| statistics_name | varchar | 128 |  | 否 |  | 统计名称 |
| common_key | varchar | 128 |  | 否 |  | 公共KEY |
| statistics_key | varchar | 128 |  | 否 |  | KEY |
| stages | int |  |  | 是 | NULL | 期数 |
| content | text |  |  | 是 | NULL | 统计内容 |
| extend_content | text |  |  | 是 | NULL | 统计内容 |
| remark | text |  |  | 是 | NULL | 描述 |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 创建时间 |

```sql
CREATE TABLE `u_u_statistics_info` (
  `statistics_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '统计编号',
  `type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '统计类型',
  `statistics_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '统计名称',
  `common_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '公共KEY',
  `statistics_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'KEY',
  `stages` int DEFAULT NULL COMMENT '期数',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '统计内容',
  `extend_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '统计内容',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`statistics_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='统计信息表'
```

### 用户第三方账号绑定表：`u_user_binding_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| binding_id | varchar | 128 | 主键 | 否 |  | 绑定ID |
| user_id | varchar | 128 | 索引 | 否 |  | 用户ID |
| binding_type | char | 2 | 索引 | 是 | NULL | 绑定类型 |
| identifier | varchar | 128 | 索引 | 否 |  | 第三方唯一标识 |
| extend_config | varchar | 1024 |  | 是 | NULL | 扩展配置 |
| binding_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 绑定时间 |

```sql
CREATE TABLE `u_user_binding_info` (
  `binding_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '绑定ID',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `binding_type` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '绑定类型',
  `identifier` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '第三方唯一标识',
  `extend_config` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '扩展配置',
  `binding_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '绑定时间',
  PRIMARY KEY (`binding_id`) USING BTREE,
  UNIQUE KEY `uniq_user_binding` (`user_id`,`binding_type`) USING BTREE,
  UNIQUE KEY `uniq_identifier` (`identifier`,`binding_type`) USING BTREE,
  KEY `idx_binding_type` (`binding_type`) USING BTREE,
  CONSTRAINT `fk_binding_user` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户第三方账号绑定表'
```

### 用户好友关系表：`u_user_friend_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| relation_id | varchar | 128 | 主键 | 否 |  | 关系ID |
| user_id | varchar | 128 | 索引 | 否 |  | 用户ID |
| friend_user_id | varchar | 128 | 索引 | 否 |  | 好友用户ID |
| create_time | datetime |  |  | 否 | CURRENT_TIMESTAMP | 创建时间 |

```sql
CREATE TABLE `u_user_friend_info` (
  `relation_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关系ID',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `friend_user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '好友用户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`relation_id`) USING BTREE,
  UNIQUE KEY `uniq_user_friend` (`user_id`,`friend_user_id`) USING BTREE,
  KEY `idx_friend_user` (`friend_user_id`) USING BTREE,
  CONSTRAINT `fk_friend_target_user` FOREIGN KEY (`friend_user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `fk_friend_user` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户好友关系表'
```

### 用户信息表：`u_user_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| user_id | varchar | 128 | 主键 | 否 |  | 用户ID |
| user_name | varchar | 36 | 唯一索引 | 否 |  | 用户名 |
| phone | varchar | 32 | 索引 | 否 |  | 手机号 |
| country_code | varchar | 5 |  | 否 | +86 | 国家代码 |
| nick_name | varchar | 32 |  | 否 |  | 昵称 |
| avatar_url | varchar | 256 |  | 是 | NULL | 头像地址 |
| password | varchar | 128 |  | 是 | NULL | 密码 |
| status | char | 1 | 索引 | 否 | 0 | 状态（0=正常 1=异常 2=禁用） |
| salt | varchar | 64 |  | 是 | NULL | 加密盐 |
| sex | char | 1 |  | 是 | 0 | 性别（0=未知 1=男 2=女） |
| birthday | datetime |  |  | 是 | NULL | 生日 |
| occupation | varchar | 64 |  | 是 | unknown | 职业 |
| preferred_language_locale | varchar | 8 |  | 是 | NULL | 偏好语言 |
| introductory | varchar | 512 |  | 是 | NULL | 个人简介 |
| ip_address | varchar | 64 | 索引 | 是 | NULL | IP属地 |
| last_login_time | datetime |  |  | 是 | NULL | 最后登录时间 |
| last_login_ip | varchar | 64 |  | 是 | NULL | 最后登录IP |
| create_time | datetime |  | 索引 | 否 | CURRENT_TIMESTAMP | 创建时间 |
| update_time | datetime |  |  | 是 | NULL | 更新时间 |
| is_delete | char | 1 |  | 否 | 0 | 删除标记（0=未删除 1=已删除） |

```sql
CREATE TABLE `u_user_info` (
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `user_name` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '手机号',
  `country_code` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '+86' COMMENT '国家代码',
  `nick_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '昵称',
  `avatar_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像地址',
  `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '状态（0=正常 1=异常 2=禁用）',
  `salt` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '加密盐',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '性别（0=未知 1=男 2=女）',
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `occupation` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'unknown' COMMENT '职业',
  `preferred_language_locale` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '偏好语言',
  `introductory` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '个人简介',
  `ip_address` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'IP属地',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '最后登录IP',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_delete` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '0' COMMENT '删除标记（0=未删除 1=已删除）',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE KEY `uk_user_name` (`user_name`) USING BTREE,
  KEY `idx_phone` (`phone`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE,
  KEY `idx_user_info_create_time` (`create_time`) USING BTREE,
  KEY `idx_user_info_ip_address` (`ip_address`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户信息表'
```

### 用户关系表：`u_user_relation_info`

| 字段名 | 数据类型 | 长度 | 键类型 | 允许为空 | 默认值 | 描述 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| relation_id | varchar | 128 | 主键 | 否 |  | 关系ID |
| user_id | varchar | 128 | 索引 | 否 |  | 用户ID |
| relation_user_id | varchar | 128 | 索引 | 否 |  | 关联用户ID |
| relation_type | char | 1 | 索引 | 否 |  | 关系类型（0=关注 1=互关 2=拉黑） |
| create_time | datetime |  |  | 否 |  | 创建时间 |

```sql
CREATE TABLE `u_user_relation_info` (
  `relation_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关系ID',
  `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户ID',
  `relation_user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关联用户ID',
  `relation_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '关系类型（0=关注 1=互关 2=拉黑）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`relation_id`) USING BTREE,
  KEY `idx_relation_user` (`user_id`) USING BTREE,
  KEY `idx_relation_target` (`relation_user_id`) USING BTREE,
  KEY `idx_relation_type` (`relation_type`) USING BTREE,
  CONSTRAINT `fk_relation_target_user` FOREIGN KEY (`relation_user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_relation_user` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户关系表'
```

