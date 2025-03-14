# 荔枝云图库

[TOC]



## 项目介绍



## 配置模块

### 配置信息

配置信息存入缓存信息，name是键名，value是值，键值对

值可以是文件和值

初始化项目加载所有配置信息

根据order_num升序

### 国际化

#### 国际化国家

简称是唯一的

#### 国际化键名

根据order_num升序

键是唯一的

#### 国际化信息

后续国际化信息根据国家（简称）+键名，同时组成唯一键 （待完成）

信息需要存入缓存，根据国家作为redis key存入  map key（键名）——value（信息）

新增国际化信息并存入缓存，项目启动初始化至缓存



## 数据库设计

### 配置模块

表名前缀统一c_

#### 配置信息表：c_config_info

| 字段         | 类型     | 长度 | 键类型 | null | 描述                         |
| :----------- | -------- | ---- | ------ | ---- | ---------------------------- |
| config_id    | bigint   |      | 主键   | 否   | 编号；自增                   |
| config_name  | varchar  | 128  |        | 否   | 配置名称；唯一               |
| config_key   | varchar  | 128  |        | 否   | 配置键名；唯一               |
| config_value | varchar  | 1024 |        | 否   | 配置键值                     |
| config_type  | varchar  | 1    |        | 否   | 配置类型（1值 2文件）；默认1 |
| order_num    | int      |      |        | 否   | 配置排序；默认10             |
| create_by    | varchar  | 64   |        | 否   | 创建人                       |
| create_time  | datetime |      |        | 否   | 创建时间                     |
| update_by    | varchar  | 64   |        | 是   | 更新人                       |
| update_time  | datetime |      |        | 是   | 更新时间                     |
| remark       | varchar  | 512  |        | 是   | 备注                         |

配置类型：1：值，2：文件

```sql
CREATE TABLE c_config_info (
    config_id BIGINT(128) NOT NULL AUTO_INCREMENT COMMENT '编号',
    config_name VARCHAR(128) NOT NULL COMMENT '配置名称',
    config_key VARCHAR(128) NOT NULL COMMENT '配置键名',
    config_value VARCHAR(1024) NOT NULL COMMENT '配置键值',
    config_type VARCHAR(1) NOT NULL DEFAULT '1' COMMENT '配置类型（1值 2文件）',
    order_num INT NOT NULL DEFAULT 10 COMMENT '配置排序',
    create_by VARCHAR(64) NOT NULL COMMENT '创建人',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新人',
    update_time DATETIME COMMENT '更新时间',
    remark VARCHAR(512) COMMENT '备注',
    PRIMARY KEY (config_id),
    UNIQUE KEY uk_c_config_info_config_name (config_name),
    UNIQUE KEY uk_c_config_info_config_key (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='配置信息表';
```



#### 权限信息表：c_permission_info

| 字段            | 类型     | 长度 | 键类型 | null | 描述                    |
| --------------- | -------- | ---- | ------ | ---- | ----------------------- |
| permission_id   | bigint   |      | 主键   | 否   | 编号；自增              |
| permission_name | varchar  | 50   |        | 否   | 权限名称；唯一          |
| parent_id       | varchar  | 128  |        | 是   | 父权限；ID              |
| order_num       | int      |      |        | 是   | 显示顺序                |
| permission      | varchar  | 128  |        | 是   | 权限标识；唯一          |
| status          | char     | 1    |        | 否   | 是否使用（0正常 1关闭） |
| create_by       | varchar  | 64   |        | 否   | 创建人                  |
| create_time     | datetime |      |        | 否   | 创建时间                |
| update_by       | varchar  | 64   |        | 是   | 更新人                  |
| update_time     | datetime |      |        | 是   | 更新时间                |
| remark          | varchar  | 512  |        | 是   | 备注                    |

本权限信息表参考若依权限表，但他属于配置模块，后续权限需要使用feign调用，如果

权限状态：0：正常，1：关闭 用于关闭接口

```sql
CREATE TABLE c_permission_info (
    permission_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '编号',
    permission_name VARCHAR(50) NOT NULL COMMENT '权限名称',
    parent_id VARCHAR(128) COMMENT '父权限',
    order_num INT COMMENT '显示顺序',
    permission VARCHAR(128) COMMENT '权限标识',
    status CHAR(1) NOT NULL COMMENT '是否使用（0正常 1关闭）',
    create_by VARCHAR(64) NOT NULL COMMENT '创建人',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新人',
    update_time DATETIME COMMENT '更新时间',
    remark VARCHAR(512) COMMENT '备注',
    PRIMARY KEY (permission_id),
    UNIQUE KEY uk_c_permission_info_permission_name (permission_name),
    UNIQUE KEY uk_c_permission_info_permission (permission)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='权限信息表';
```



#### 国际化国家表：c_i18n_locale_info

| 字段          | 类型     | 长度 | 键类型 | null | 描述                       |
| ------------- | -------- | ---- | ------ | ---- | -------------------------- |
| locale_id     | bigint   |      | 主键   | 否   | 编号 自增                  |
| locale_name   | varchar  | 128  |        | 否   | 国家地区；唯一             |
| locale        | varchar  | 8    |        | 否   | 简称                       |
| locale_status | char     | 1    |        | 否   | 状态（0正常 1隐藏）；默认1 |
| create_by     | varchar  | 64   |        | 否   | 创建人                     |
| create_time   | datetime |      |        | 否   | 创建时间                   |
| update_by     | varchar  | 64   |        | 是   | 更新人                     |
| update_time   | datetime |      |        | 是   | 更新时间                   |
| remark        | varchar  | 500  |        | 是   | 备注                       |

状态：0：正常，1：隐藏

```sql
CREATE TABLE c_i18n_locale_info (
    locale_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '编号',
    locale_name VARCHAR(128) NOT NULL COMMENT '国家地区',
    locale VARCHAR(8) NOT NULL COMMENT '简称',
    locale_status CHAR(1) NOT NULL DEFAULT '1' COMMENT '状态（0正常 1隐藏）',
    create_by VARCHAR(64) NOT NULL COMMENT '创建人',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新人',
    update_time DATETIME COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    PRIMARY KEY (locale_id),
    UNIQUE KEY uk_c_i18n_locale_info_locale (locale)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='国际化国家表';
```

#### 国际化键名表：c_i18n_key_info

| 字段        | 类型     | 长度 | 键类型 | null | 描述      |
| ----------- | -------- | ---- | ------ | ---- | --------- |
| key_id      | bigint   |      | 主键   | 否   | 编号 自增 |
| key_name    | varchar  | 128  |        | 否   | 键；唯一  |
| order_num   | int      |      |        | 是   | 显示顺序  |
| create_by   | varchar  | 64   |        | 否   | 创建人    |
| create_time | datetime |      |        | 否   | 创建时间  |
| update_by   | varchar  | 64   |        | 是   | 更新人    |
| update_time | datetime |      |        | 是   | 更新时间  |
| remark      | varchar  | 500  |        | 是   | 备注      |

```sql
CREATE TABLE c_i18n_key_info (
    key_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '编号',
    key_name VARCHAR(128) NOT NULL COMMENT '键',
    order_num INT COMMENT '显示顺序',
    create_by VARCHAR(64) NOT NULL COMMENT '创建人',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新人',
    update_time DATETIME COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    PRIMARY KEY (key_id),
    UNIQUE KEY uk_c_i18n_key_info_key_name (key_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='国际化键名表';
```



#### 国际化信息表：c_i18n_message_info

| 字段        | 类型     | 长度 | 键类型                            | null | 描述      |
| ----------- | -------- | ---- | --------------------------------- | ---- | --------- |
| message_id  | bigint   |      | 主键                              | 否   | 主键 自增 |
| message_key | varchar  | 128  |                                   | 否   | 键        |
| locale      | varchar  | 8    | 外键（c_i18n_locale_info:locale） | 否   | 简称      |
| message     | varchar  | 1024 |                                   | 否   | 消息      |
| create_by   | varchar  | 64   |                                   | 否   | 创建人    |
| create_time | datetime |      |                                   | 否   | 创建时间  |
| update_by   | varchar  | 64   |                                   | 是   | 更新人    |
| update_time | datetime |      |                                   | 是   | 更新时间  |
| remark      | varchar  | 500  |                                   | 是   | 备注      |

使用redis存储，可以随时随地修改

配置国际化也可以把name设置为这个的键，前端要把国际化信息存储到本地，每次修改配置可以使用推送机制推送到前端，或者用户重新登录之后更新国际化信息

```sql
CREATE TABLE c_i18n_message_info (
    message_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    message_key VARCHAR(128) NOT NULL COMMENT '键',
    locale VARCHAR(8) NOT NULL COMMENT '简称',
    message VARCHAR(1024) NOT NULL COMMENT '消息',
    create_by VARCHAR(64) NOT NULL COMMENT '创建人',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新人',
    update_time DATETIME COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    PRIMARY KEY (message_id),
    UNIQUE KEY uk_c_i18n_message_info_key_locale (message_key, locale), -- 键+地区的组合唯一
    CONSTRAINT fk_c_i18n_message_info_locale 
        FOREIGN KEY (locale) 
        REFERENCES c_i18n_locale_info (locale) 
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='国际化信息表';
```



#### 通知模版表:c_inform_template_info

| 字段           | 类型     | 长度 | 键类型                            | null | 描述                                                         |
| :------------- | -------- | ---- | --------------------------------- | ---- | ------------------------------------------------------------ |
| template_id    | bigint   |      | 主键                              | 否   | 主键 自增                                                    |
| template_name  | varchar  | 128  |                                   | 否   | 模版名称                                                     |
| locale         | varchar  | 8    | 外键（c_i18n_locale_info:locale） | 否   | 语言 默认zh-CN                                               |
| template_type  | char     | 2    |                                   | 否   | 模版类型 1=短信，2=邮件，3=站内通知，4=APP推送，5=微信模板消息 |
| channel        | varchar  | 32   |                                   | 是   | 渠道                                                         |
| content        | text     |      |                                   | 否   | 内容                                                         |
| variables      | text     |      |                                   | 是   | 变量列表  如 ["user_name", "order_id"]                       |
| template_image | varchar  | 1024 |                                   | 是   | 模版样式图                                                   |
| status         | char     | 1    |                                   | 否   | 状态 0=待审核，1=已启用，2=已禁用，3=审核失败                |
| audit_comment  | varchar  | 256  |                                   | 是   | 审核意见                                                     |
| create_by      | varchar  | 64   |                                   | 否   | 创建人                                                       |
| create_time    | datetime |      |                                   | 否   | 创建时间                                                     |
| update_by      | varchar  | 64   |                                   | 是   | 更新人                                                       |
| update_time    | datetime |      |                                   | 是   | 更新时间                                                     |
| remark         | varchar  | 500  |                                   | 是   | 备注                                                         |

根据用户偏好语言发送模版，如果用户没有偏好语言发送默认语言模版zh-CN，如果用户的偏好语言里模版没有此语言，则也发送默认的zh-CN模版

```sql
CREATE TABLE c_inform_template_info (
    template_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    template_name VARCHAR(128) NOT NULL COMMENT '模版名称',
    locale VARCHAR(8) NOT NULL COMMENT '语言（默认zh-CN）',
    template_type CHAR(1) NOT NULL COMMENT '模版类型（1=短信 2=邮件 3=站内通知 4=APP推送 5=微信模板）',
    channel VARCHAR(32) COMMENT '渠道',
    content TEXT NOT NULL COMMENT '内容',
    variables TEXT COMMENT '变量列表',    template_image VARCHAR(1024) COMMENT '模版样式图',
    status CHAR(1) NOT NULL COMMENT '状态（0=待审核 1=已启用 2=已禁用 3=审核失败）',
    audit_comment VARCHAR(256)  DEFAULT '' COMMENT '审核意见', -- 默认值处理
    create_by VARCHAR(64) NOT NULL COMMENT '创建人',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新人',
    update_time DATETIME COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注',
    PRIMARY KEY (template_id),
    UNIQUE KEY uk_template_name_locale (template_name, locale), -- 名称+语言唯一约束
    CONSTRAINT fk_inform_template_locale 
        FOREIGN KEY (locale) 
        REFERENCES c_i18n_locale_info (locale) 
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='通知模版表';

-- 添加状态索引
ALTER TABLE c_inform_template_info 
ADD INDEX idx_template_status (status);

-- 添加类型索引
ALTER TABLE c_inform_template_info 
ADD INDEX idx_template_type (template_type);
```



### 用户模块

表名前缀统一u_

#### 用户信息表：u_user_info

| 字段                      | 类型     | 长度 | 键类型                            | null | 描述                    |
| :------------------------ | -------- | ---- | --------------------------------- | ---- | ----------------------- |
| user_id                   | varchar  | 128  | 主键                              | 否   | 主键                    |
| user_name                 | varchar  | 36   |                                   | 否   | 用户名 唯一             |
| phone                     | varchar  | 32   |                                   | 否   | 手机号码                |
| country_code              | varchar  | 5    |                                   | 否   | 国家代码 默认+86        |
| nick_name                 | varchar  | 32   |                                   | 是   | 昵称                    |
| avatar_url                | varchar  | 256  |                                   | 是   | 头像                    |
| password                  | varchar  | 128  |                                   | 是   | 密码                    |
| status                    | char     | 1    |                                   | 否   | 状态(0正常 1异常 2禁用) |
| salt                      | varchar  | 64   |                                   | 是   | 加密方式                |
| sex                       | char     | 1    |                                   | 是   | 性别(1=男 2=女) 默认0   |
| birthday                  | datetime |      |                                   | 是   | 生日 默认当前时间       |
| occupation                | varchar  | 64   |                                   | 是   | 职业 默认unknown        |
| preferred_language_locale | varchar  | 8    | 外键（c_i18n_locale_info:locale） | 是   | 偏好语言 默认zh-CN      |
| introductory              | varchar  | 512  |                                   | 是   | 简介                    |
| ip_address                | varchar  | 64   |                                   | 是   | IP属地                  |
| last_login_time           | datetime |      |                                   | 是   | 最后登录时间            |
| last_login_ip             | varchar  | 64   |                                   | 是   | 最后登录IP              |
| create_time               | datetime |      |                                   | 否   | 创建时间                |
| update_time               | datetime |      |                                   | 是   | 修改时间                |
| is_delete                 | char     | 1    |                                   | 否   | 删除(0否 1是) 默认0     |

性别：0：未知，1：男，2：女

生日 使用生日来计算用户年龄

```sql
-- 删除已存在的表（如果存在）
DROP TABLE IF EXISTS u_user_info;

-- 创建用户信息表
CREATE TABLE u_user_info (
    user_id VARCHAR(128) NOT NULL COMMENT '用户ID',
    user_name VARCHAR(36) NOT NULL COMMENT '用户名',
    phone VARCHAR(32) NOT NULL COMMENT '手机号',
    country_code VARCHAR(5) NOT NULL DEFAULT '+86' COMMENT '国家代码',
    nick_name VARCHAR(32) NOT NULL COMMENT '昵称',
    avatar_url VARCHAR(256) COMMENT '头像地址',
    password VARCHAR(128) COMMENT '密码',
    status CHAR(1) NOT NULL DEFAULT '0' COMMENT '状态（0=正常 1=异常 2=禁用）',
    salt VARCHAR(64) COMMENT '加密盐',
    sex CHAR(1) DEFAULT '0' COMMENT '性别（0=未知 1=男 2=女）',
    birthday DATETIME COMMENT '生日',
    occupation VARCHAR(64) DEFAULT 'unknown' COMMENT '职业',
    preferred_language_locale VARCHAR(8) DEFAULT 'zh-CN' COMMENT '偏好语言',
    introductory VARCHAR(512) COMMENT '个人简介',
    ip_address VARCHAR(64) COMMENT 'IP属地',
    last_login_time DATETIME COMMENT '最后登录时间',
    last_login_ip VARCHAR(64) COMMENT '最后登录IP',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_delete CHAR(1) NOT NULL DEFAULT '0' COMMENT '删除标记（0=未删除 1=已删除）',
    PRIMARY KEY (user_id),
    UNIQUE KEY uk_user_name (user_name),
    INDEX idx_phone (phone),
    INDEX idx_status (status),
    CONSTRAINT fk_preferred_language 
        FOREIGN KEY (preferred_language_locale) 
        REFERENCES c_i18n_locale_info(locale)
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';

```



#### 用户封禁权限表：u_banned_permission_info

| 字段            | 类型     | 长度 | 键类型                                  | null | 描述                |
| --------------- | -------- | ---- | --------------------------------------- | ---- | ------------------- |
| banned_id       | varchar  | 128  | 主键                                    | 否   | 编号                |
| permission_name | varchar  | 50   | 外键(c_permission_info:permission_name) | 否   | 权限名称            |
| user_id         | varchar  | 128  | 外键(u_user_info：user_id)              | 否   | 用户                |
| start_time      | datetime |      |                                         | 否   | 开始时间            |
| end_time        | datetime |      |                                         | 否   | 结束时间            |
| status          | char     | 1    |                                         | 否   | 状态(0封禁中 1结束) |
| cause           | varchar  |      |                                         | 是   | 原因                |

权限名称：权限名称基本不会修改所以直接使用权限名称

用户：用户账号可能会修改，所以使用id

```sql
-- 删除已存在的表（如果存在）
DROP TABLE IF EXISTS u_banned_permission_info;

-- 创建用户封禁权限表
CREATE TABLE u_banned_permission_info (
    banned_id VARCHAR(128) NOT NULL COMMENT '封禁记录编号',
    permission_name VARCHAR(50) NOT NULL COMMENT '权限名称',
    user_id VARCHAR(128) NOT NULL COMMENT '用户',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NOT NULL COMMENT '结束时间',
    status CHAR(1) NOT NULL DEFAULT '0' COMMENT '状态（0=封禁中 1=结束）',
    cause VARCHAR(512) COMMENT '封禁原因',
    PRIMARY KEY (banned_id),
    INDEX idx_banned_user (user_id), -- 用户维度查询优化
    INDEX idx_banned_permission (permission_name), -- 权限维度查询优化
    CONSTRAINT fk_banned_permission_name 
        FOREIGN KEY (permission_name) 
        REFERENCES c_permission_info(permission_name)
        ON UPDATE CASCADE,
    CONSTRAINT fk_banned_user_id 
        FOREIGN KEY (user_id) 
        REFERENCES u_user_info(user_id)
        ON UPDATE CASCADE,
    -- 添加数据完整性校验
    CONSTRAINT chk_time_sequence CHECK (end_time > start_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户封禁权限表';
```



#### 用户关系表：u_user_relation_info

| 字段             | 类型     | 长度 | 键类型                     | null | 描述                        |
| ---------------- | -------- | ---- | -------------------------- | ---- | --------------------------- |
| relation_id      | varchar  | 128  | 主键                       | 否   | 编号                        |
| user_id          | varchar  | 128  | 外键(u_user_info：user_id) | 否   | 用户                        |
| relation_user_id | varchar  | 128  | 外键(u_user_info：user_id) | 否   | 目标用户                    |
| relation_type    | char     | 1    |                            | 否   | 关系类型(0关注 1互关 2拉黑) |
| create_time      | datetime |      |                            | 否   | 创建时间                    |

取消关注就给他的逻辑删除为删除 要是下次关注给他更新回来

```sql
-- 严格保持字段类型与需求一致
CREATE TABLE u_user_relation_info (
    relation_id VARCHAR(128) NOT NULL COMMENT '关系ID', -- 保持原始VARCHAR(128)类型
    user_id VARCHAR(128) NOT NULL COMMENT '用户ID',
    relation_user_id VARCHAR(128) NOT NULL COMMENT '关联用户ID',
    relation_type CHAR(1) NOT NULL COMMENT '关系类型（0=关注 1=互关 2=拉黑）',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    PRIMARY KEY (relation_id),
    -- 保持原始索引结构
    INDEX idx_relation_user (user_id),
    INDEX idx_relation_target (relation_user_id),
    INDEX idx_relation_type (relation_type),
    -- 保留外键约束
    CONSTRAINT fk_relation_user 
        FOREIGN KEY (user_id) 
        REFERENCES u_user_info(user_id),
    CONSTRAINT fk_relation_target_user 
        FOREIGN KEY (relation_user_id) 
        REFERENCES u_user_info(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户关系表';

```



#### 用户好友表：u_user_friend_info

| 字段           | 类型     | 长度 | 键类型                     | null | 描述     |
| -------------- | -------- | ---- | -------------------------- | ---- | -------- |
| relation_id    | varchar  | 128  | 主键                       | 否   | 编号     |
| user_id        | varchar  | 128  | 外键(u_user_info：user_id) | 否   | 用户     |
| friend_user_id | varchar  | 128  | 外键(u_user_info：user_id) | 否   | 好友     |
| create_time    | datetime |      |                            | 否   | 创建时间 |

可以冗余 比如用户a->b  创建两条数据，一条是a的好友 一条是b的好友，这样方便后续的查询

当a取消关注b，删除两者的好友，删除a关注b的关系，如果拉黑直接删除两者的好友信息

使用缓存存储a或者b的好友列表，例：a:[b,c,d]  b:[a,b,c]

```sql
-- 删除已存在的表（如果存在）
DROP TABLE IF EXISTS u_user_friend_info;

-- 创建用户好友关系表（优化版）
CREATE TABLE u_user_friend_info (
    relation_id VARCHAR(128) NOT NULL COMMENT '关系ID',
    user_id VARCHAR(128) NOT NULL COMMENT '用户ID',
    friend_user_id VARCHAR(128) NOT NULL COMMENT '好友用户ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (relation_id),
    UNIQUE KEY uniq_user_friend (user_id, friend_user_id), -- 防止重复添加
    INDEX idx_friend_user (friend_user_id), -- 好友维度查询优化
    CONSTRAINT fk_friend_user 
        FOREIGN KEY (user_id) 
        REFERENCES u_user_info(user_id) 
        ON UPDATE CASCADE 
        ON DELETE CASCADE, -- 级联删除
    CONSTRAINT fk_friend_target_user 
        FOREIGN KEY (friend_user_id) 
        REFERENCES u_user_info(user_id) 
        ON UPDATE CASCADE,
    -- 数据完整性校验
    CONSTRAINT chk_self_relation CHECK (user_id != friend_user_id) -- 禁止自我关联
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户好友关系表';
```



#### 用户绑定表：u_user_binding_info

| 字段         | 类型     | 长度 | 键类型                     | null | 描述       |
| ------------ | -------- | ---- | -------------------------- | ---- | ---------- |
| binding_id   | varchar  | 128  | 主键                       | 否   | 编号       |
| user_id      | varchar  | 128  | 外键(u_user_info：user_id) | 否   | 用户       |
| binding_type | char     | 2    |                            | 否   | 绑定类型   |
| identifier   | varchar  | 128  |                            | 否   | 第三方标识 |
| binding_time | datetime |      |                            | 否   | 绑定时间   |

```sql
-- 删除已存在的表（如果存在）
DROP TABLE IF EXISTS u_user_binding_info;

-- 创建用户绑定表（优化版）
CREATE TABLE u_user_binding_info (
    binding_id VARCHAR(128) NOT NULL COMMENT '绑定ID',
    user_id VARCHAR(128) NOT NULL COMMENT '用户ID',
    binding_type CHAR(2) NOT NULL COMMENT '绑定类型（01=微信 02=支付宝 03=Apple）',
    identifier VARCHAR(128) NOT NULL COMMENT '第三方唯一标识',
    binding_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '绑定时间',
    PRIMARY KEY (binding_id),
    UNIQUE KEY uniq_user_binding (user_id, binding_type),  -- 同类型唯一绑定
    UNIQUE KEY uniq_identifier (identifier,binding_type),  -- 第三方标识全局唯一
    INDEX idx_binding_type (binding_type),  -- 类型维度查询
    CONSTRAINT fk_binding_user 
        FOREIGN KEY (user_id) 
        REFERENCES u_user_info(user_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE  -- 用户删除时级联解绑
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户第三方账号绑定表';

-- 扩展建议（按需执行）：
-- 1. 添加索引注释
ALTER TABLE u_user_binding_info 
    MODIFY COLUMN binding_type CHAR(2) COMMENT '绑定类型';
```



#### 用户通知表：u_inform_info

| 字段名      | 类型         | 长度 | 键类型/索引                                | Null | 描述                                              |
| ----------- | ------------ | ---- | ------------------------------------------ | ---- | ------------------------------------------------- |
| record_id   | bigint       |      | 主键                                       | 否   | 编号                                              |
| template_id | bigint       |      | 外键（c_inform_template_info:template_id） | 否   | 关联的模板ID                                      |
| user_id     | varchar(128) | 128  | 外键(u_user_info：user_id)                 | 否   | 用户                                              |
| content     | text         |      |                                            | 否   | 实际发送内容 （含动态变量替换后的完整文本）       |
| channel     | varchar(32)  | 32   |                                            | 否   | 发送渠道                                          |
| status      | tinyint      | 1    |                                            | 否   | 发送状态 0=待发送，1=已发送，2=发送失败，3=已撤回 |
| is_read     | tinyint      | 1    |                                            | 否   | 是否已读 0=未读，1=已读（默认0）                  |
| read_time   | datetime     |      |                                            | 是   | 读取时间                                          |
| retry_count | int          |      |                                            | 否   | 重试次数                                          |
| send_time   | datetime     |      | 索引                                       | 否   | 发送时间                                          |
| remark      | varchar(500) | 500  |                                            | 是   | 备注                                              |
| is_deleted  | tinyint      | 1    |                                            | 否   | 删除(0否 1是) 默认0                               |

```sql
-- 删除已存在的表（若存在）
DROP TABLE IF EXISTS u_inform_info;

-- 创建用户通知表（优化版）
CREATE TABLE u_inform_info (
    record_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '通知记录ID',
    template_id BIGINT NOT NULL COMMENT '模板ID',
    user_id VARCHAR(128) NOT NULL COMMENT '用户ID',
    content TEXT NOT NULL COMMENT '实际发送内容',
    channel VARCHAR(32) NOT NULL COMMENT '发送渠道',
    status TINYINT(1) NOT NULL DEFAULT 0 COMMENT '发送状态（0=待发送 1=已发送 2=发送失败 3=已撤回）',
    is_read TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已读（0=未读 1=已读）',
    read_time DATETIME COMMENT '读取时间',
    retry_count INT NOT NULL DEFAULT 0 COMMENT '重试次数',
    send_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
    remark VARCHAR(500) COMMENT '备注',
    is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记（0=正常 1=删除）',
    PRIMARY KEY (record_id),
    INDEX idx_send_time (send_time),  -- 时间范围查询优化
    INDEX idx_user_status (user_id, status),  -- 用户维度状态查询
    INDEX idx_channel_status (channel, status),  -- 渠道+状态统计优化
    CONSTRAINT fk_inform_template 
        FOREIGN KEY (template_id) 
        REFERENCES c_inform_template_info(template_id)
        ON UPDATE CASCADE,
    CONSTRAINT fk_inform_user 
        FOREIGN KEY (user_id) 
        REFERENCES u_user_info(user_id)
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户通知记录表';
```

