# LitchiPicture

[TOC]



## 项目计划

### 250328

需在200404完成图片基本上传、图库、文件夹等功能

## 开发规范

1. 缓存

   > 缓存每个单词统一使用：分割
   >
   > 首个单词必须为每个模块的模块名，比如ai，命名为：ai:ai:....
   >
   > 注意：如果是若依自带的，缓存默认会加上：sys_config

2. 后端

> 类字段统一采用驼峰命名
>
> Constants字段统一全部大写加_分割单词
>
> 枚举命名：类为每个模块单词的首字母加上该字典类型，字段为该字典类型+值，例：类名PPictureReviewStatus，字段：PICTURE_REVIEW_STATUS_0

3. 用户端前端

   > 文件命名规范：每个模块会有模块的文件夹，比如图片模块picture文件夹，下面加上他的文件，picture.vue这样，如果是详情，pictureDetail.vue，采用驼峰命名，同时如果多个相似要放在同一个文件夹，比如图片模块的图库，就可以放在图库picture/space/space.vue
   >
   > vue模块：js必须要有name，模块+文件名，template最外层div必须加class，并且class命名为横杠来分割单词，模块+文件名例：forgetPassword，user-forgot-password
   >
   > css使用scss

4. 管理端前端

   > 采用若依当前规则即可
   >
   > css使用scss

5. 配置模块命名规则

   > 权限标识：路由地址+对应功能
   >
   > 配置键名：模块:功能:功能

6. 其他

   > 方法有两个相似的必须提出来当方法，减少代码耦合
   >
   > 公共使用的方法必须提到对应的公共方法类或者ts、js文件
   >
   > 参数不能使用基类，必须使用对应的vo、dto，前端同理使用type，防止参数耦合
   >
   > 一个方法行数实际代码数量不得超过一百行
   >
   > 前端使用多的，相似度高的地方要提取为组件

## 项目介绍



## 配置模块

### 配置信息

> 配置信息存入缓存信息，name是键名，value是值，键值对
>
> 值可以是文件和值
>
> 初始化项目加载所有配置信息
>
> 根据order_num升序
>
> 配置信息添加是否内置，如果内置则用户端不可以通过接口获取到数据

### 菜单信息

> 动态路由，为用户端实现动态路由
>
> 权限管理，菜单作为权限管理
>
> 缓存，刷新缓存
>
> 状态和是否显示都可以作为改菜单是否有权限，是否开启权限，是否显示对应接口，如果不显示表示这个接口不开启

### 国际化

#### 国际化国家

1. 简称是唯一的

#### 国际化键名

1. 根据order_num升序

2. 键是唯一的

#### 国际化信息

1. 后续国际化信息根据国家（简称）+键名，同时组成唯一键 （待完成）

2. 信息需要存入缓存，根据国家作为redis key存入  map key（键名）——value（信息）

3. 新增国际化信息并存入缓存，项目启动初始化至缓存

#### 消息模块

1. 可以新增，生成事例，根据变量生成事例

2. 回退对应版本

### 菜单信息

1. 路由

   自定义路由信息，根据用户权限

   用户权限

### 文件日志

> 记录文件上传的日志，防止冗余图片，比如头像、图片、封面

## 用户模块

#### 登录

> 账号密码登录，自定义密码加密方式，根据密码加密方式判断密码
>
> 手机短信登录
>
> 注册
>
> 忘记密码

## 图片模块

### 图片信息

1. 图片上传：

   图片上传会压缩图片，同时也会保留原图，添加水印，返回图片基本信息

   用户上传图片，选择图片文件夹、图片空间、图片标签，如果标签不存在则新增标签

   用户上传图片后会异步更新对应空间的使用量

2. 图片详情

   图片信息存入缓存，用户的点赞收藏也会存入缓存，获取图片信息同时获取用户是否点赞的缓存

   用户点赞收藏转发会异步更新图片信息，这里使用的是策略模式更新对应信息

3. 图片下载

   当前只完成图片从oss服务下载图片，未完成积分扣减，待积分模块完成则实现对应的功能

4. 图片列表

   首页图片列表

### 空间信息

1. 空间信息

   空间信息新增修改以及列表，空间信息的个数的限制

   空间文件夹的新增修改

## 数据库设计

### 配置模块

表名前缀统一c_

#### 配置信息表：c_config_info

| 字段         | 类型     | 长度 | 键类型 | null | 默认值   | 描述                    |
| :----------- | -------- | ---- | ------ | ---- | -------- | ----------------------- |
| config_id    | bigint   |      | 主键   | 否   | 自增     | 编号；                  |
| config_name  | varchar  | 128  |        | 否   | 唯一     | 配置名称；              |
| config_key   | varchar  | 128  |        | 否   | 唯一     | 配置键名；              |
| config_value | varchar  | 1024 |        | 否   |          | 配置键值                |
| config_type  | varchar  | 1    |        | 否   | 默认1    | 配置类型（1值 2文件）； |
| config_is_in | char     | 1    |        | 否   |          | 是否内置                |
| order_num    | int      |      |        | 否   | 默认10   | 配置排序；              |
| create_by    | varchar  | 64   |        | 否   |          | 创建人                  |
| create_time  | datetime |      |        | 否   | 当前时间 | 创建时间                |
| update_by    | varchar  | 64   |        | 是   |          | 更新人                  |
| update_time  | datetime |      |        | 是   |          | 更新时间                |
| remark       | varchar  | 512  |        | 是   |          | 备注                    |

配置类型：1：值，2：文件

是否内置：0是 1否  用于判断是否是系统的内置信息，如果用户端是不可以通过接口获取

```sql
CREATE TABLE c_config_info (
    config_id BIGINT(128) NOT NULL AUTO_INCREMENT COMMENT '编号',
    config_name VARCHAR(128) NOT NULL COMMENT '配置名称',
    config_key VARCHAR(128) NOT NULL COMMENT '配置键名',
    config_value VARCHAR(1024) NOT NULL COMMENT '配置键值',
    config_type VARCHAR(1) NOT NULL DEFAULT '1' COMMENT '配置类型（1值 2文件）',
    config_is_in CHAR(1) NOT NULL DEFAULT '0' COMMENT '是否内置（0是 1否）'
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



#### 菜单信息表：c_menu_info

| 字段         | 类型     | 长度 | 键类型 | null | 默认值 | 描述            |
| ------------ | -------- | ---- | ------ | ---- | ------ | --------------- |
| menu_id      | bigint   |      | 主键   | 否   | 自增   | 编号；自增      |
| menu_name    | varchar  | 50   |        | 否   |        | 菜单名称        |
| parent_id    | varchar  | 128  |        | 是   |        | 父菜单；ID      |
| order_num    | int      | 4    |        | 是   |        | 显示顺序        |
| path         | varchar  | 256  |        | 是   |        | 路由地址        |
| component    | varchar  | 256  |        | 是   |        | 组件路径        |
| query        | varchar  | 256  |        | 是   |        | 路由参数        |
| route_name   | varchar  | 256  |        | 是   |        | 路由名称        |
| menu_address | char     | 1    |        | 否   |        | 显示位置；默认1 |
| is_frame     | char     | 1    |        | 否   |        | 是否外链；默认1 |
| is_cache     | char     | 1    |        | 否   |        | 是否缓存；默认1 |
| menu_type    | char     | 1    |        | 否   |        | 菜单类型        |
| visible      | char     | 1    |        | 是   |        | 是否显示；默认1 |
| status       | char     | char |        | 是   |        | 菜单状态；默认1 |
| perms        | varchar  | 128  |        | 是   |        | 权限标识        |
| icon         | varchar  | 128  |        | 是   |        | 菜单图标        |
| create_by    | varchar  | 64   |        | 否   |        | 创建人          |
| create_time  | datetime |      |        | 否   |        | 创建时间        |
| update_by    | varchar  | 64   |        | 是   |        | 更新人          |
| update_time  | datetime |      |        | 是   |        | 更新时间        |
| remark       | varchar  | 512  |        | 是   |        | 备注            |

本权限信息表参考若依权限表，但他属于配置模块，后续权限需要使用feign调用，如果

菜单类型：M：目录，C：菜单，B：按钮，F：功能，T：tabs

是否为外链 1：否，0：是

是否为缓存1：不缓存，0：缓存

菜单状态：0：正常，1：关闭（关闭此接口） 关闭此接口，外部无法访问

是否显示：0显示 1不显示

显示位置：1：不显示 2：导航，3侧边，4：页内tabs

```sql
CREATE TABLE c_menu_info (
    menu_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '编号',
    menu_name VARCHAR(50) NOT NULL COMMENT '菜单名称',
    parent_id VARCHAR(128) DEFAULT NULL COMMENT '父菜单',
    order_num INT(4) DEFAULT NULL COMMENT '显示顺序',
    path VARCHAR(256) DEFAULT NULL COMMENT '路由地址',
    component VARCHAR(256) DEFAULT NULL COMMENT '组件路径',
    query VARCHAR(256) DEFAULT NULL COMMENT '路由参数',
    route_name VARCHAR(256) DEFAULT NULL COMMENT '路由名称',
    menu_address CHAR(1) NOT NULL DEFAULT '1' COMMENT '显示位置',
    is_frame CHAR(1) NOT NULL DEFAULT '1' COMMENT '是否外链',
    is_cache CHAR(1) NOT NULL DEFAULT '1' COMMENT '是否缓存',
    menu_type CHAR(1) NOT NULL COMMENT '菜单类型',
    visible CHAR(1) DEFAULT '1' COMMENT '是否显示',
    status CHAR(1) DEFAULT '1' COMMENT '菜单状态',
    perms VARCHAR(128) DEFAULT NULL COMMENT '权限标识',
    icon VARCHAR(128) DEFAULT NULL COMMENT '菜单图标',
    create_by VARCHAR(64) NOT NULL COMMENT '创建人',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT NULL COMMENT '更新人',
    update_time DATETIME DEFAULT NULL COMMENT '更新时间',
    remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (menu_id),
    UNIQUE KEY (menu_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单信息表';
```



#### 国际化国家表：c_i18n_locale_info

| 字段          | 类型     | 长度 | 键类型 | null | 默认值   | 描述                  |
| ------------- | -------- | ---- | ------ | ---- | -------- | --------------------- |
| locale_id     | bigint   |      | 主键   | 否   | 自增     | 编号                  |
| locale_name   | varchar  | 128  |        | 否   |          | 国家地区；唯一        |
| locale        | varchar  | 8    |        | 否   |          | 简称                  |
| locale_status | char     | 1    |        | 否   | 默认1    | 状态（0正常 1隐藏）； |
| create_by     | varchar  | 64   |        | 否   |          | 创建人                |
| create_time   | datetime |      |        | 否   | 当前时间 | 创建时间              |
| update_by     | varchar  | 64   |        | 是   |          | 更新人                |
| update_time   | datetime |      |        | 是   |          | 更新时间              |
| remark        | varchar  | 500  |        | 是   |          | 备注                  |

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

| 字段        | 类型     | 长度 | 键类型 | null | 默认值   | 描述     |
| ----------- | -------- | ---- | ------ | ---- | -------- | -------- |
| key_id      | bigint   |      | 主键   | 否   | 自增     | 编号     |
| key_name    | varchar  | 128  |        | 否   |          | 键；唯一 |
| order_num   | int      |      |        | 是   |          | 显示顺序 |
| create_by   | varchar  | 64   |        | 否   |          | 创建人   |
| create_time | datetime |      |        | 否   | 当前时间 | 创建时间 |
| update_by   | varchar  | 64   |        | 是   |          | 更新人   |
| update_time | datetime |      |        | 是   |          | 更新时间 |
| remark      | varchar  | 500  |        | 是   |          | 备注     |

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

| 字段        | 类型     | 长度 | 键类型                            | null | 默认值   | 描述     |
| ----------- | -------- | ---- | --------------------------------- | ---- | -------- | -------- |
| message_id  | bigint   |      | 主键                              | 否   | 自增     | 主键     |
| message_key | varchar  | 128  |                                   | 否   |          | 键       |
| locale      | varchar  | 8    | 外键（c_i18n_locale_info:locale） | 否   |          | 简称     |
| message     | varchar  | 1024 |                                   | 否   |          | 消息     |
| create_by   | varchar  | 64   |                                   | 否   |          | 创建人   |
| create_time | datetime |      |                                   | 否   | 当前时间 | 创建时间 |
| update_by   | varchar  | 64   |                                   | 是   |          | 更新人   |
| update_time | datetime |      |                                   | 是   |          | 更新时间 |
| remark      | varchar  | 500  |                                   | 是   |          | 备注     |

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
    UNIQUE KEY uk_c_i18n_message_info_key_locale (message_key, locale), 
    CONSTRAINT fk_c_i18n_message_info_locale 
        FOREIGN KEY (locale) 
        REFERENCES c_i18n_locale_info (locale) 
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='国际化信息表';
```



#### 通知模版表:c_inform_template_info

| 字段                    | 类型     | 长度 | 键类型                            | null | 默认值   | 描述                                   |
| :---------------------- | -------- | ---- | --------------------------------- | ---- | -------- | -------------------------------------- |
| template_id             | bigint   |      | 主键                              | 否   | 自增     | 主键                                   |
| template_name           | varchar  | 128  |                                   | 否   |          | 模版名称                               |
| template_key            | varchar  | 128  |                                   | 否   |          | 模版key                                |
| locale                  | varchar  | 8    | 外键（c_i18n_locale_info:locale） | 否   |          | 语言 默认zh-CN                         |
| template_type           | char     | 2    |                                   | 否   |          | 模版类型                               |
| service_template_id     | varchar  | 64   |                                   | 是   |          | 服务商模版编号                         |
| service_sign_name       | varchar  | 64   |                                   | 是   |          | 服务商签名                             |
| extend_config           | varchar  | 1024 |                                   | 是   |          | 扩展配置                               |
| templat_version         | int      |      |                                   | 否   |          | 版本                                   |
| templat_version_history | text     |      |                                   | 否   |          | 历史版本                               |
| channel                 | varchar  | 32   |                                   | 是   |          | 渠道                                   |
| content                 | text     |      |                                   | 否   |          | 内容                                   |
| variables               | text     |      |                                   | 是   |          | 变量列表  如 ["user_name", "order_id"] |
| template_image          | varchar  | 1024 |                                   | 是   |          | 模版样式图                             |
| status                  | char     | 1    |                                   | 否   |          | 状态 0已启用，1=已禁用                 |
| create_by               | varchar  | 64   |                                   | 否   |          | 创建人                                 |
| create_time             | datetime |      |                                   | 否   | 当前时间 | 创建时间                               |
| update_by               | varchar  | 64   |                                   | 是   |          | 更新人                                 |
| update_time             | datetime |      |                                   | 是   |          | 更新时间                               |
| remark                  | varchar  | 500  |                                   | 是   |          | 备注                                   |

根据用户偏好语言发送模版，如果用户没有偏好语言发送默认语言模版zh-CN，如果用户的偏好语言里模版没有此语言，则也发送默认的zh-CN模版

历史版本可用于回退版本  用于记录版本，存入一个map，每个版本的详细信息 key版本-value版本信息

扩展配置 灵活兼容不同平台的配置

渠道：比如阿里云、腾讯云

模版类型： 1=短信，2=邮件，3=站内通知，4=APP推送，5=微信模板消息

```sql
create table c_inform_template_info
(
    template_id              bigint auto_increment comment '主键'
        primary key,
    template_name            varchar(128)  not null comment '模版名称',
    template_key            varchar(128)  not null comment '模版KEY',    
    locale                   varchar(8)    not null comment '语言（默认zh-CN）',
    channel                  varchar(32)   null comment '渠道',
    template_type            char          not null comment '模版类型（1短信 2邮件 3站内通知 4APP推送 5微信模板）',
    service_template_id      varchar(64)   null comment '服务商模版编号',
    service_sign_name        varchar(64)   null comment '服务商签名',
    extend_config            varchar(1024) null comment '扩展配置',
    template_version         int           not null comment '版本',
    template_version_history text          not null comment '历史版本',
    content                  text          not null comment '内容',
    example                  text          null comment '事例',
    variables                text          null comment '变量列表',
    template_image           varchar(1024) null comment '模版样式图',
    status                   char          not null comment '状态（0待审核 1已启用 2已禁用 3审核失败）',
    create_by                varchar(64)   not null comment '创建人',
    create_time              datetime      not null comment '创建时间',
    update_by                varchar(64)   null comment '更新人',
    update_time              datetime      null comment '更新时间',
    remark                   varchar(500)  null comment '备注',
    constraint uk_template_name_locale
        unique (template_name, locale, template_type),
    constraint fk_inform_template_locale
        foreign key (locale) references c_i18n_locale_info (locale)
            on update cascade
)
    comment '通知模版表';
create index idx_template_status
    on c_inform_template_info (status);
create index idx_template_type
    on c_inform_template_info (template_type);
```





#### 文件日志表：c_file_log_info

用于存储文件每次上传的日志，防止冗余数据，定时删除这些冗余数据

| 字段名         | 类型     | 长度 | 键类型                     | Null | 默认值   | 描述                       |
| -------------- | -------- | ---- | -------------------------- | ---- | -------- | -------------------------- |
| log_id         | varchar  | 128  | 主键                       | 否   |          | 日志编号                   |
| user_id        | varchar  | 128  | 外键 (u_user_info:user_id) | 否   |          | 用户编号                   |
| target_id      | varchar  | 128  |                            | 是   |          | 目标对象                   |
| target_content | varchar  | 256  |                            | 是   |          | 目标内容                   |
| dns_url        | varchar  | 512  |                            | 是   |          | 域名URL                    |
| file_url       | varchar  | 512  |                            | 否   |          | 文件路径                   |
| file_type      | varchar  | 16   |                            | 否   |          | 文件类型                   |
| log_status     | char     | 1    |                            | 否   |          | 状态;(0冗余,1正常,1已删除) |
| oss_type       | char     | 1    |                            | 否   | 1        | 存储类型                   |
| log_type       | char     | 1    |                            | 否   | 1        | 日志类型                   |
| is_compress    | char     | 1    |                            | 否   | 1        | 是否压缩                   |
| create_time    | datetime |      |                            | 否   | 当前时间 | 创建时间                   |
| delete_time    | datetime |      |                            | 是   |          | 删除时间                   |
| ip_addr        | varchar  | 50   |                            | 否   |          | IP地址                     |
| device_id      | varchar  | 256  |                            | 是   |          | 设备唯一标识               |
| browser        | varchar  | 50   |                            | 是   |          | 浏览器类型                 |
| os             | varchar  | 50   |                            | 是   |          | 操作系统                   |
| platform       | varchar  | 20   |                            | 是   |          | 平台                       |
| ip_address     | varchar  | 64   |                            | 是   |          | IP属地                     |

域名URL：如果没有就是官方

日志类型：0图片 1空间封面 2头像

是否压缩：记录图片是否压缩 0是 1否

存储类型：0官方 1阿里云 。。。。其他的用户自定义

```sql
DROP TABLE IF EXISTS c_file_log_info;
CREATE TABLE `c_file_log_info`
(
    `log_id`      VARCHAR(128) NOT NULL COMMENT '日志编号',
    `user_id`     VARCHAR(128) NOT NULL COMMENT '用户编号',
  	`target_id` VARCHAR(128)  NULL COMMENT '目标对象',
  	`target_content` VARCHAR(256) DEFAULT NULL COMMENT '目标内容',
    `dns_url`     VARCHAR(512) NOT NULL COMMENT '域名URL',
    `file_url`    VARCHAR(512) NOT NULL COMMENT '文件路径',
    `file_type`   VARCHAR(16)  NOT NULL COMMENT '文件类型',
    `log_status`  CHAR(1)      NOT NULL COMMENT '状态',
    `oss_type`    CHAR(1)      NOT NULL DEFAULT '0' COMMENT '存储类型（0官方 1阿里云）',
    `log_type`    CHAR(1)      NOT NULL DEFAULT '1' COMMENT '日志类型（0图片 1空间封面 2头像）',
    `is_compress`    CHAR(1)      NOT NULL DEFAULT '1' COMMENT '是否压缩（0是 1否）',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `delete_time` DATETIME              DEFAULT NULL COMMENT '删除时间',
    `ip_addr` VARCHAR(50) NOT NULL COMMENT 'IP地址',
    `device_id`   VARCHAR(256)          DEFAULT NULL COMMENT '设备唯一标识',
    `browser`     VARCHAR(50)           DEFAULT NULL COMMENT '浏览器类型',
    `os`          VARCHAR(50)           DEFAULT NULL COMMENT '操作系统',
    `platform`    VARCHAR(20)           DEFAULT NULL COMMENT '平台',
    `ip_address`  VARCHAR(64)           DEFAULT NULL COMMENT 'IP属地',
    PRIMARY KEY (`log_id`),
    FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='文件日志表';
```



### 用户模块

表名前缀统一u_

#### 用户信息表：u_user_info

| 字段                      | 类型     | 长度 | 键类型                            | null | 默认值   | 描述                    |
| :------------------------ | -------- | ---- | --------------------------------- | ---- | -------- | ----------------------- |
| user_id                   | varchar  | 128  | 主键                              | 否   |          | 主键                    |
| user_name                 | varchar  | 36   |                                   | 否   |          | 用户名 唯一             |
| phone                     | varchar  | 32   |                                   | 否   |          | 手机号码                |
| country_code              | varchar  | 5    |                                   | 否   | +86      | 国家代码 默认+86        |
| nick_name                 | varchar  | 32   |                                   | 是   |          | 昵称                    |
| avatar_url                | varchar  | 256  |                                   | 是   |          | 头像                    |
| password                  | varchar  | 128  |                                   | 是   |          | 密码                    |
| status                    | char     | 1    |                                   | 否   |          | 状态(0正常 1异常 2禁用) |
| salt                      | varchar  | 64   |                                   | 是   |          | 加密方式                |
| sex                       | char     | 1    |                                   | 是   | 0        | 性别(1=男 2=女) 默认0   |
| birthday                  | datetime |      |                                   | 是   |          | 生日 默认当前时间       |
| occupation                | varchar  | 64   |                                   | 是   |          | 职业 默认unknown        |
| preferred_language_locale | varchar  | 8    | 外键（c_i18n_locale_info:locale） | 是   | cn       | 偏好语言 默认0          |
| introductory              | varchar  | 512  |                                   | 是   |          | 简介                    |
| ip_address                | varchar  | 64   |                                   | 是   |          | IP属地                  |
| last_login_time           | datetime |      |                                   | 是   |          | 最后登录时间            |
| last_login_ip             | varchar  | 64   |                                   | 是   |          | 最后登录IP              |
| create_time               | datetime |      |                                   | 否   | 当前时间 | 创建时间                |
| update_time               | datetime |      |                                   | 是   |          | 修改时间                |
| is_delete                 | char     | 1    |                                   | 否   | 0        | 删除(0否 1是) 默认0     |

性别：0：未知，1：男，2：女

生日 使用生日来计算用户年龄

```sql
DROP TABLE IF EXISTS u_user_info;
-- 创建用户信息表
CREATE TABLE u_user_info (
    user_id VARCHAR(128) NOT NULL COMMENT '用户编号',
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
    is_delete CHAR(1) NOT NULL DEFAULT '0' COMMENT '删除（0=未删除 1=已删除）',
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

| 字段            | 类型     | 长度 | 键类型                                  | null | 默认值   | 描述                |
| --------------- | -------- | ---- | --------------------------------------- | ---- | -------- | ------------------- |
| banned_id       | varchar  | 128  | 主键                                    | 否   |          | 编号                |
| permission_name | varchar  | 50   | 外键(c_permission_info:permission_name) | 否   |          | 权限名称            |
| user_id         | varchar  | 128  | 外键(u_user_info：user_id)              | 否   |          | 用户                |
| start_time      | datetime |      |                                         | 否   | 当前时间 | 开始时间            |
| end_time        | datetime |      |                                         | 否   |          | 结束时间            |
| status          | char     | 1    |                                         | 否   |          | 状态(0封禁中 1结束) |
| cause           | varchar  |      |                                         | 是   |          | 原因                |

权限名称：权限名称基本不会修改所以直接使用权限名称

用户：用户账号可能会修改，所以使用id

```sql
DROP TABLE IF EXISTS u_banned_permission_info;
-- 创建用户封禁权限表
CREATE TABLE u_banned_permission_info (
    banned_id VARCHAR(128) NOT NULL COMMENT '封禁记录编号',
    permission_name VARCHAR(50) NOT NULL COMMENT '权限名称',
    user_id VARCHAR(128) NOT NULL COMMENT '用户',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NOT NULL COMMENT '结束时间',
    status CHAR(1) NOT NULL DEFAULT '0' COMMENT '状态（0封禁中 1结束）',
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

| 字段             | 类型     | 长度 | 键类型                     | null | 默认值   | 描述                        |
| ---------------- | -------- | ---- | -------------------------- | ---- | -------- | --------------------------- |
| relation_id      | varchar  | 128  | 主键                       | 否   |          | 编号                        |
| user_id          | varchar  | 128  | 外键(u_user_info：user_id) | 否   |          | 用户                        |
| relation_user_id | varchar  | 128  | 外键(u_user_info：user_id) | 否   |          | 目标用户                    |
| relation_type    | char     | 1    |                            | 否   |          | 关系类型(0关注 1互关 2拉黑) |
| create_time      | datetime |      |                            | 否   | 当前时间 | 创建时间                    |

取消关注就给他的删除为删除 要是下次关注给他更新回来

```sql
CREATE TABLE u_user_relation_info (
    relation_id VARCHAR(128) NOT NULL COMMENT '关系编号', 
    user_id VARCHAR(128) NOT NULL COMMENT '用户编号',
    relation_user_id VARCHAR(128) NOT NULL COMMENT '关联用户编号',
    relation_type CHAR(1) NOT NULL COMMENT '关系类型（0=关注 1=互关 2=拉黑）',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    PRIMARY KEY (relation_id),
    INDEX idx_relation_user (user_id),
    INDEX idx_relation_target (relation_user_id),
    CONSTRAINT fk_relation_user 
        FOREIGN KEY (user_id) 
        REFERENCES u_user_info(user_id),
    CONSTRAINT fk_relation_target_user 
        FOREIGN KEY (relation_user_id) 
        REFERENCES u_user_info(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户关系表';

```



#### 用户好友表：u_user_friend_info

| 字段           | 类型     | 长度 | 键类型                     | null | 默认值   | 描述     |
| -------------- | -------- | ---- | -------------------------- | ---- | -------- | -------- |
| relation_id    | varchar  | 128  | 主键                       | 否   |          | 编号     |
| user_id        | varchar  | 128  | 外键(u_user_info：user_id) | 否   |          | 用户     |
| friend_user_id | varchar  | 128  | 外键(u_user_info：user_id) | 否   |          | 好友     |
| create_time    | datetime |      |                            | 否   | 当前时间 | 创建时间 |

可以冗余 比如用户a->b  创建两条数据，一条是a的好友 一条是b的好友，这样方便后续的查询

当a取消关注b，删除两者的好友，删除a关注b的关系，如果拉黑直接删除两者的好友信息

使用缓存存储a或者b的好友列表，例：a:[b,c,d]  b:[a,b,c]

```sql
DROP TABLE IF EXISTS u_user_friend_info;
CREATE TABLE u_user_friend_info
(
    relation_id    VARCHAR(128) NOT NULL COMMENT '关系编号',
    user_id        VARCHAR(128) NOT NULL COMMENT '用户编号',
    friend_user_id VARCHAR(128) NOT NULL COMMENT '好友用户编号',
    create_time    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (relation_id),
    UNIQUE KEY uniq_user_friend (user_id, friend_user_id),        
    INDEX idx_friend_user (friend_user_id),                        
    CONSTRAINT fk_friend_user
        FOREIGN KEY (user_id)
            REFERENCES u_user_info (user_id)
            ON UPDATE CASCADE
            ON DELETE CASCADE,                                    
    CONSTRAINT fk_friend_target_user
        FOREIGN KEY (friend_user_id)
            REFERENCES u_user_info (user_id)
            ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户好友关系表';
```



#### 用户绑定表：u_user_binding_info

| 字段          | 类型     | 长度 | 键类型                     | null | 默认值   | 描述       |
| ------------- | -------- | ---- | -------------------------- | ---- | -------- | ---------- |
| binding_id    | varchar  | 128  | 主键                       | 否   |          | 编号       |
| user_id       | varchar  | 128  | 外键(u_user_info：user_id) | 否   |          | 用户       |
| binding_type  | char     | 2    |                            | 否   |          | 绑定类型   |
| identifier    | varchar  | 128  |                            | 否   |          | 第三方标识 |
| extend_config | varchar  | 1024 |                            | 是   |          | 扩展配置   |
| binding_time  | datetime |      |                            | 否   | 当前时间 | 绑定时间   |

```sql
DROP TABLE IF EXISTS u_user_binding_info;
CREATE TABLE u_user_binding_info (
    binding_id VARCHAR(128) NOT NULL COMMENT '绑定编号',
    user_id VARCHAR(128) NOT NULL COMMENT '用户编号',
    binding_type CHAR(2) NOT NULL COMMENT '绑定类型（01=微信 02=支付宝 03=Apple）',
    identifier VARCHAR(128) NOT NULL COMMENT '第三方唯一标识',
    extend_config            varchar(1024) null comment '扩展配置',
    binding_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '绑定时间',
    PRIMARY KEY (binding_id),
    UNIQUE KEY uniq_user_binding (user_id, binding_type),  
    UNIQUE KEY uniq_identifier (identifier,binding_type),  
    INDEX idx_binding_type (binding_type),  
    CONSTRAINT fk_binding_user 
        FOREIGN KEY (user_id) 
        REFERENCES u_user_info(user_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户第三方账号绑定表';
```



#### 用户通知表：u_inform_info

| 字段名        | 类型         | 长度 | 键类型/索引                                | Null | 默认值   | 描述                |
| ------------- | ------------ | ---- | ------------------------------------------ | ---- | -------- | ------------------- |
| record_id     | varchar      | 128  | 主键                                       | 否   |          | 编号                |
| template_id   | bigint       |      | 外键（c_inform_template_info:template_id） | 否   |          | 关联的模板编号      |
| user_id       | varchar(128) | 128  | 外键(u_user_info：user_id)                 | 否   |          | 用户                |
| content       | text         |      |                                            | 否   |          | 实际发送内容        |
| inform_type   | varchar(32)  | 32   |                                            | 否   |          | 通知类型            |
| status        | tinyint      | 1    |                                            | 否   |          | 发送状态            |
| is_read       | tinyint      | 1    |                                            | 否   | 0        | 是否已读            |
| read_time     | datetime     |      |                                            | 是   |          | 读取时间            |
| retry_count   | int          |      |                                            | 否   |          | 重试次数            |
| response_info | text         |      |                                            | 是   |          | 返回信息            |
| send_time     | datetime     |      | 索引                                       | 否   | 当前时间 | 发送时间            |
| remark        | varchar(500) | 500  |                                            | 是   |          | 备注                |
| is_delete     | char         | 1    |                                            | 否   |          | 删除(0否 1是) 默认0 |

 发送状态： 0=待发送，1=已发送，2=发送失败，3=已撤回

是否已读： 0=未读，1=已读（默认0）

```sql
DROP TABLE IF EXISTS u_inform_info;
CREATE TABLE u_inform_info (
    record_id VARCHAR(128) NOT NULL COMMENT '通知记录编号',
    template_id BIGINT NOT NULL COMMENT '模板编号',
    user_id VARCHAR(128) NOT NULL COMMENT '用户编号',
    content TEXT NOT NULL COMMENT '实际发送内容',
    inform_type VARCHAR(32) NOT NULL COMMENT '通知类型',
    status TINYINT(1) NOT NULL DEFAULT 0 COMMENT '发送状态（0=待发送 1=已发送 2=发送失败 3=已撤回）',
    is_read TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已读（0=未读 1=已读）',
    read_time DATETIME COMMENT '读取时间',
    retry_count INT NOT NULL DEFAULT 0 COMMENT '重试次数',
    send_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
    remark VARCHAR(500) COMMENT '备注',
    is_delete TINYINT(1) NOT NULL DEFAULT 0 COMMENT '删除（0=正常 1=删除）',
    PRIMARY KEY (record_id),
    INDEX idx_send_time (send_time),  
    INDEX idx_user_status (user_id, status),  
    INDEX idx_channel_status (channel, status), 
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



#### 用户登录日志表：u_login_log_info

| 字段名         | 类型     | 长度 | 键类型                    | null | 默认值   | 描述         |
| -------------- | -------- | ---- | ------------------------- | ---- | -------- | ------------ |
| info_id        | bigint   | 20   | 主键                      | 否   | 自增     | 日志编号     |
| user_id        | varchar  | 128  | 外键(u_user_info:user_id) | 是   |          | 用户编号     |
| user_name      | varchar  | 36   |                           | 是   |          | 用户名       |
| login_type     | varchar  | 20   |                           | 否   |          | 登录方式     |
| identifier     | varchar  | 128  |                           | 是   |          | 匿名标识     |
| ip_addr        | varchar  | 50   |                           | 否   |          | 登录IP地址   |
| login_location | varchar  | 255  |                           | 是   |          | 登录地点     |
| browser        | varchar  | 50   |                           | 是   |          | 浏览器类型   |
| os             | varchar  | 50   |                           | 是   |          | 操作系统     |
| platform       | varchar  | 20   |                           | 是   |          | 平台         |
| device_id      | varchar  | 255  |                           | 是   |          | 设备唯一标识 |
| status         | char     | 1    |                           | 否   |          | 状态         |
| error_code     | varchar  | 64   |                           | 是   |          | 错误码       |
| msg            | varchar  | 255  |                           | 是   |          | 提示消息     |
| login_time     | datetime |      | 索引                      | 否   | 当前时间 | 登录时间     |

匿名标识：比如手机号等等

```sql
DROP TABLE IF EXISTS u_login_log_info;
CREATE TABLE u_login_log_info (
    info_id VARCHAR(128) NOT NULL  COMMENT '编号',
    user_id VARCHAR(128) COMMENT '用户编号',
    user_name VARCHAR(36) COMMENT '用户名',
    login_type VARCHAR(20) NOT NULL COMMENT '登录方式',
    identifier VARCHAR(128) COMMENT '匿名标识',
    ip_addr VARCHAR(50) NOT NULL COMMENT '登录IP地址',
    login_location VARCHAR(255) COMMENT '登录地点',
    browser VARCHAR(50) COMMENT '浏览器类型',
    os VARCHAR(50) COMMENT '操作系统',
    platform VARCHAR(20) COMMENT '平台',
    device_id VARCHAR(255) COMMENT '设备唯一标识',
    status CHAR(1) NOT NULL DEFAULT '0' COMMENT '状态（0成功 1失败）',
    error_code VARCHAR(64) COMMENT '错误码',
    msg VARCHAR(255)  NULL COMMENT '提示消息',
    login_time DATETIME NOT NULL COMMENT '登录时间',
    PRIMARY KEY (info_id),
    INDEX idx_login_time (login_time),
    INDEX idx_platform_device (platform, device_id(64)),
    INDEX idx_user_status (user_id, status),
    FOREIGN KEY (user_id) REFERENCES u_user_info(user_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户登录日志表';
```



### 积分模块

#### 账户信息表：po_account_info

记录账户的信息，积分，密码，一些基本信息

| 字段名          | 类型     | 长度 | 键类型                     | Null | 默认值   | 描述     |
| --------------- | -------- | ---- | -------------------------- | ---- | -------- | -------- |
| account_id      | varchar  | 128  | 主键                       | 否   |          | 账户编号 |
| user_id         | varchar  | 128  | 外键 (u_user_info:user_id) | 否   |          | 用户编号 |
| password        | varchar  | 512  |                            | 否   |          | 支付密码 |
| salt            | varchar  | 64   |                            | 否   |          | 加密方式 |
| points_earned   | bigint   |      |                            | 否   | 0        | 赚取积分 |
| points_used     | bigint   |      |                            | 否   | 0        | 使用积分 |
| recharge_amount | decimal  | 10,2 |                            | 否   | 0        | 充值金额 |
| account_status  | char     | 1    |                            | 否   |          | 状态     |
| points_balance  | int      |      |                            | 否   | 0        | 积分余额 |
| create_time     | datetime |      |                            | 否   | 当前时间 | 创建时间 |
| update_time     | datetime |      |                            | 否   | 当前时间 | 更新时间 |
| remark          | varchar  | 512  |                            | 是   |          | 备注     |
| is_delete       | char     | 1    |                            | 否   | 0        | 删除     |

加密方式：md5、bcrypt等等

支付密码错误次数：用于风控，比如进一分钟内密码输入错误五次则直接禁止五分钟不可以输入密码，也就不可以使用关于积分的内容

状态：0正常 1异常 2禁用

删除：0否 1是

```sql
DROP TABLE IF EXISTS po_account_info;
CREATE TABLE po_account_info
(
    account_id      VARCHAR(128)   NOT NULL COMMENT '账户编号',
    user_id         VARCHAR(128)   NOT NULL COMMENT '用户编号',
    password        VARCHAR(512)   NOT NULL COMMENT '支付密码',
    salt            VARCHAR(64)    NOT NULL COMMENT '加密方式',
    points_earned   BIGINT         NOT NULL DEFAULT 0 COMMENT '赚取总积分',
    points_used     BIGINT         NOT NULL DEFAULT 0 COMMENT '使用总积分',
    recharge_amount DECIMAL(18, 2) NOT NULL DEFAULT 0 COMMENT '充值总金额（元）',
    account_status  CHAR(1)        NOT NULL DEFAULT '0' COMMENT '账户状态（0正常 1异常 2禁用）',
    points_balance  BIGINT         NOT NULL DEFAULT 0 COMMENT '积分余额',
    create_time     DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark          VARCHAR(512) COMMENT '备注',
    is_delete       CHAR(1)        NOT NULL DEFAULT '0' COMMENT '删除（0否 1是）',
    PRIMARY KEY (account_id),
    UNIQUE KEY uk_po_account_user (user_id),
    CONSTRAINT fk_po_account_user
        FOREIGN KEY (user_id)
            REFERENCES u_user_info (user_id),
    INDEX idx_po_account_status (account_status)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='积分账户表';
```



#### 支付方式表：po_payment_method_info

方便配置支付方式的一些信息

| 字段名              | 类型     | 长度 | 键类型 | Null | 默认值   | 描述           |
| ------------------- | -------- | ---- | ------ | ---- | -------- | -------------- |
| method_id           | varchar  | 128  | 主键   | 否   |          | 支付方式编号   |
| method_name         | varchar  | 32   |        | 否   |          | 名称           |
| third_party         | varchar  | 128  |        | 否   |          | 第三方支付平台 |
| method_type         | varchar  | 32   |        | 否   |          | 类型           |
| api_url             | varchar  | 256  |        | 是   |          | 支付接口URL    |
| merchant_id         | varchar  | 128  |        | 是   |          | 商户号         |
| app_id              | varchar  | 128  |        | 是   |          | 应用编号       |
| secret_key          | varchar  | 512  |        | 是   |          | 秘钥           |
| contact_information | varchar  | 1024 |        | 是   |          | 联系方式       |
| extend_config       | varchar  | 1024 |        | 是   |          | 扩展配置       |
| method_status       | varchar  | 32   |        | 否   | 1        | 状态           |
| create_time         | datetime |      |        | 否   | 当前时间 | 创建时间       |
| update_time         | datetime |      |        | 否   | 当前时间 | 更新时间       |
| remark              | varchar  | 512  |        | 是   |          | 备注           |

支付方式状态：0使用 1未使用

类型：0线上 1线下

联系方式：比如微信号，自己填入

扩展配置：不同平台可能要不同的配置，便于扩展

```sql
DROP TABLE IF EXISTS po_payment_method_info;
CREATE TABLE po_payment_method_info (
    method_id VARCHAR(128) NOT NULL COMMENT '支付方式编号',
    method_name VARCHAR(32) NOT NULL COMMENT '名称',
    third_party VARCHAR(128)   NOT NULL COMMENT '第三方支付平台',
    method_type VARCHAR(32) NOT NULL COMMENT '类型',
    api_url VARCHAR(256) COMMENT '支付接口URL',
    merchant_id VARCHAR(128) COMMENT '商户号',
    app_id VARCHAR(128) COMMENT '应用编号',
    secret_key VARCHAR(512) COMMENT '秘钥',
    contact_information VARCHAR(1024) COMMENT '联系方式',
    extend_config VARCHAR(1024) COMMENT '扩展配置',
    method_status VARCHAR(32) NOT NULL DEFAULT '1' COMMENT '状态（0使用 1未使用）',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(512) COMMENT '备注',
    PRIMARY KEY (method_id),
    INDEX idx_method_type (method_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付方式表';
```

```sql
DROP TABLE IF EXISTS po_payment_method_info;
CREATE TABLE po_payment_method_info (
    method_id VARCHAR(128) NOT NULL COMMENT '支付方式编号',
    method_name VARCHAR(32) NOT NULL COMMENT '名称',
    third_party VARCHAR(128)   NOT NULL COMMENT '第三方支付平台',
    method_type VARCHAR(32) NOT NULL COMMENT '类型',
    api_url VARCHAR(256) COMMENT '支付接口URL',
    merchant_id VARCHAR(128) COMMENT '商户号',
    app_id VARCHAR(128) COMMENT '应用编号',
    secret_key VARCHAR(512) COMMENT '秘钥',
    contact_information VARCHAR(1024) COMMENT '联系方式',
    extend_config VARCHAR(1024) COMMENT '扩展配置',
    method_status VARCHAR(32) NOT NULL DEFAULT '1' COMMENT '状态（0使用 1未使用）',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(512) COMMENT '备注',
    PRIMARY KEY (method_id),
    INDEX idx_method_type (method_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付方式表';
```



#### 支付订单表：po_payment_order_info

| 字段名            | 类型     | 长度 | 键类型                           | Null | 默认值   | 描述                 |
| ----------------- | -------- | ---- | -------------------------------- | ---- | -------- | -------------------- |
| order_id          | varchar  | 128  | 主键                             | 否   |          | 订单编号             |
| user_id           | varchar  | 128  | 外键 (u_user_info:user_id)、唯一 | 否   |          | 用户编号             |
| order_status      | varchar  | 32   |                                  | 否   | 0        | 订单状态             |
| payment_type      | varchar  | 128  |                                  | 否   |          | 支付方式             |
| total_amount      | decimal  | 10,2 |                                  | 否   | 0.00     | 订单总金额           |
| buyer_pay_amount  | decimal  | 10,2 |                                  | 是   | 0.00     | 实付金额             |
| receipt_amount    | decimal  | 10,2 |                                  | 是   | 0.00     | 实收金额             |
| discount_amount   | decimal  | 10,2 |                                  | 是   | 0.00     | 平台优惠金额         |
| third_party       | varchar  | 128  |                                  | 否   |          | 第三方支付平台       |
| third_user_id     | varchar  | 128  |                                  | 是   |          | 第三方用户编号       |
| third_party_order | varchar  | 128  |                                  | 是   |          | 第三方支付平台订单号 |
| payment_time      | datetime |      |                                  | 是   |          | 支付时间             |
| payment_status    | varchar  | 32   |                                  | 否   | 0        | 支付状态             |
| payment_code      | varchar  | 128  |                                  | 是   |          | 支付返回Code         |
| payment_msg       | varchar  | 128  |                                  | 是   |          | 支付返回Msg          |
| payment_extend    | text     |      |                                  | 是   |          | 支付返回额外信息     |
| create_time       | datetime |      |                                  | 否   | 当前时间 | 创建时间             |
| update_time       | datetime |      |                                  | 否   | 当前时间 | 更新时间             |
| device_id         | varchar  | 255  |                                  | 是   |          | 设备唯一标识         |
| browser           | varchar  | 50   |                                  | 是   |          | 浏览器类型           |
| os                | varchar  | 50   |                                  | 是   |          | 操作系统             |
| platform          | varchar  | 20   |                                  | 是   |          | 平台                 |
| ip_addr           | varchar  | 50   |                                  | 否   |          | IP地址               |
| is_delete         | char     | 1    |                                  | 否   | 0        | 删除                 |
| remark            | varchar  | 512  |                                  | 是   |          | 备注                 |

订单状态：0待支付 1支付成功 2支付失败 3超时 4已取消等

订单类型：0充值 1消费

支付状态：根据第三方返回信息获取，也就是交易状态

```sql
DROP TABLE IF EXISTS po_payment_order_info;
CREATE TABLE po_payment_order_info (
    order_id VARCHAR(128) NOT NULL PRIMARY KEY COMMENT '订单编号',
    user_id VARCHAR(128) NOT NULL UNIQUE COMMENT '用户编号',
    order_status VARCHAR(32) NOT NULL DEFAULT '0' COMMENT '订单状态',
    payment_type VARCHAR(128) NOT NULL COMMENT '支付方式',
    total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '订单总金额',
    buyer_pay_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '实付金额',
    receipt_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '实收金额',
    discount_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '平台优惠金额',
    third_party VARCHAR(128) NOT NULL COMMENT '第三方支付平台',
    third_user_id VARCHAR(128) DEFAULT NULL COMMENT '第三方用户编号',
    third_party_order VARCHAR(128) DEFAULT NULL COMMENT '第三方支付平台订单号',
    payment_time DATETIME DEFAULT NULL COMMENT '支付时间',
    payment_status VARCHAR(32) NOT NULL DEFAULT '0' COMMENT '支付状态',
    payment_code VARCHAR(128) DEFAULT NULL COMMENT '支付返回Code',
    payment_msg VARCHAR(128) DEFAULT NULL COMMENT '支付返回Msg',
    payment_extend TEXT COMMENT '支付返回额外信息',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    device_id VARCHAR(255) DEFAULT NULL COMMENT '设备唯一标识',
    browser VARCHAR(50) DEFAULT NULL COMMENT '浏览器类型',
    os VARCHAR(50) DEFAULT NULL COMMENT '操作系统',
    platform VARCHAR(20) DEFAULT NULL COMMENT '平台',
    ip_addr VARCHAR(50) NOT NULL COMMENT 'IP地址',
    is_delete CHAR(1) NOT NULL DEFAULT '0' COMMENT '删除',
    remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
    CONSTRAINT fk_payment_order_user_id FOREIGN KEY (user_id) REFERENCES u_user_info(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付订单表';
```



#### 充值积分套餐表：po_points_recharge_package_info

积分套餐，如果不是长期有效，后面会有定时任务在他结束时间之后给他更新未失效，也会更新开始时间

| 字段名         | 类型     | 长度 | 键类型 | Null | 默认值   | 描述         |
| -------------- | -------- | ---- | ------ | ---- | -------- | ------------ |
| package_id     | varchar  | 128  | 主键   | 否   |          | 套餐编号     |
| package_name   | varchar  | 128  | 唯一   | 否   |          | 套餐名称     |
| price          | decimal  | 10,2 |        | 否   | 0.00     | 套餐价格     |
| points         | int      |      |        | 否   | 0        | 套餐积分数量 |
| points_bonus   | int      |      |        | 是   | 0        | 套餐赠送积分 |
| description    | varchar  | 512  |        | 是   |          | 套餐描述     |
| is_long_term   | char     | 1    |        | 否   | 1        | 是否长期     |
| sort_order     | tinyint  |      |        | 否   | 0        | 排序权重     |
| start_time     | datetime |      |        | 是   |          | 套餐生效时间 |
| end_time       | datetime |      |        | 是   |          | 套餐结束时间 |
| package_status | char     | 1    |        | 否   | 0        | 套餐状态     |
| create_time    | datetime |      |        | 否   | 当前时间 | 创建时间     |
| update_time    | datetime |      |        | 否   |          | 更新时间     |
| remark         | varchar  | 512  |        | 是   |          | 备注         |

套餐状态：0未开始 1正常 2失效

是否长期：0是 1否，表示固定的充值活动，不会应为套餐结束时间而结束

```sql
DROP TABLE IF EXISTS po_points_recharge_package_info;
CREATE TABLE po_points_recharge_package_info (
    package_id VARCHAR(128) NOT NULL COMMENT '套餐编号',
    package_name VARCHAR(128) NOT NULL COMMENT '套餐名称',
    price DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '套餐价格',
    points INT NOT NULL DEFAULT 0 COMMENT '套餐积分数量',
    points_bonus INT DEFAULT 0 COMMENT '套餐赠送积分',
    description VARCHAR(512) COMMENT '套餐描述',
    is_long_term CHAR(1) NOT NULL DEFAULT '1' COMMENT '是否长期（0是 1否）',
    sort_order   TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序权重',
    start_time DATETIME COMMENT '套餐生效时间',
    end_time DATETIME COMMENT '套餐结束时间',
    package_status CHAR(1) NOT NULL DEFAULT '0' COMMENT '套餐状态（0正常 1失效）',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark VARCHAR(512) COMMENT '备注',
    PRIMARY KEY (package_id),
    UNIQUE KEY uk_package_name (package_name),
    INDEX idx_status (status),
    INDEX idx_time_range (start_time, end_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='充值积分套餐表';
```



#### 充值记录表：po_points_recharge_info

记录用户充值积分记录

| 字段名            | 类型     | 长度 | 键类型                                           | Null | 默认值   | 描述                 |
| ----------------- | -------- | ---- | ------------------------------------------------ | ---- | -------- | -------------------- |
| recharge_id       | varchar  | 128  | 主键                                             | 否   |          | 充值记录编号         |
| package_id        | varchar  | 128  | 外键(po_points_recharge_package_info:package_id) | 否   |          | 套餐编号             |
| user_id           | varchar  | 128  | 外键 (u_user_info:user_id)                       | 否   |          | 用户编号             |
| order_id          | varchar  | 128  | 外键（po_payment_order_info:order_id）           | 否   |          | 订单编号             |
| total_count       | int      |      |                                                  | 否   |          | 总数                 |
| points_count      | int      |      |                                                  | 否   | 0        | 充值积分数量         |
| bonus_count       | int      |      |                                                  | 是   | 0        | 赠送数量             |
| price_count       | decimal  | 10,2 |                                                  | 否   | 0.00     | 充值金额             |
| recharge_count    | int      |      |                                                  | 否   | 0        | 数量                 |
| third_party       | varchar  | 128  |                                                  | 否   |          | 第三方支付平台       |
| third_party_order | varchar  | 128  |                                                  | 是   |          | 第三方支付平台订单号 |
| recharge_status   | varchar  | 32   |                                                  | 否   |          | 充值状态             |
| create_time       | datetime |      |                                                  | 否   | 当前时间 | 充值时间             |
| arrival_time      | datetime |      |                                                  | 是   |          | 到账时间             |
| update_time       | datetime |      |                                                  | 是   |          | 更新时间             |
| device_id         | varchar  | 255  |                                                  | 是   |          | 设备唯一标识         |
| browser           | varchar  | 50   |                                                  | 是   |          | 浏览器类型           |
| os                | varchar  | 50   |                                                  | 是   |          | 操作系统             |
| platform          | varchar  | 20   |                                                  | 是   |          | 平台                 |
| ip_addr           | varchar  | 50   |                                                  | 否   |          | IP地址               |
| remark            | varchar  | 512  |                                                  | 是   |          | 备注                 |
| is_delete         | char     | 1    |                                                  | 否   | 0        | 删除                 |

充值状态：0待支付 1支付成功 2支付失败 3超时 4已取消等

```sql
DROP TABLE IF EXISTS po_points_recharge_info;

CREATE TABLE po_points_recharge_info (
    recharge_id VARCHAR(128) NOT NULL PRIMARY KEY COMMENT '充值记录编号',
    package_id VARCHAR(128) NOT NULL COMMENT '套餐编号',
    user_id VARCHAR(128) NOT NULL COMMENT '用户编号',
    order_id VARCHAR(128) NOT NULL COMMENT '订单编号',
    total_count INT NOT NULL COMMENT '总数',
    points_count INT NOT NULL DEFAULT 0 COMMENT '充值积分数量',
    bonus_count INT DEFAULT 0 COMMENT '赠送数量',
    price_count DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '充值金额',
    recharge_count INT NOT NULL DEFAULT 0 COMMENT '数量',
    third_party VARCHAR(128) NOT NULL COMMENT '第三方支付平台',
    third_party_order VARCHAR(128) DEFAULT NULL COMMENT '第三方支付平台订单号',
    recharge_status VARCHAR(32) NOT NULL COMMENT '充值状态',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '充值时间',
    arrival_time DATETIME DEFAULT NULL COMMENT '到账时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    device_id VARCHAR(255) DEFAULT NULL COMMENT '设备唯一标识',
    browser VARCHAR(50) DEFAULT NULL COMMENT '浏览器类型',
    os VARCHAR(50) DEFAULT NULL COMMENT '操作系统',
    platform VARCHAR(20) DEFAULT NULL COMMENT '平台',
    ip_addr VARCHAR(50) NOT NULL COMMENT 'IP地址',
    remark VARCHAR(512) DEFAULT NULL COMMENT '备注',
    is_delete CHAR(1) NOT NULL DEFAULT '0' COMMENT '删除',

    -- 外键约束
    CONSTRAINT fk_points_recharge_user_id FOREIGN KEY (user_id) REFERENCES u_user_info(user_id),
    CONSTRAINT fk_points_recharge_order_id FOREIGN KEY (order_id) REFERENCES po_payment_order_info(order_id),
    CONSTRAINT fk_points_recharge_package_id FOREIGN KEY (package_id) REFERENCES po_points_recharge_package_info(package_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='充值记录表：记录用户充值积分记录';
```



#### 异常捕获表：po_error_log_info

捕获此模块充值提现使用积分等异常

| 字段名           | 类型     | 长度 | 键类型                     | Null | 默认值   | 描述         |
| ---------------- | -------- | ---- | -------------------------- | ---- | -------- | ------------ |
| error_id         | varchar  | 128  | 主键                       | 否   |          | 异常编号     |
| user_id          | varchar  | 128  | 外键 (u_user_info:user_id) | 否   |          | 用户编号     |
| method_id        | varchar  | 128  |                            | 否   |          | 支付方式     |
| error_type       | varchar  | 32   |                            | 否   |          | 异常类型     |
| error_message    | text     |      |                            | 否   |          | 异常信息     |
| error_details    | text     |      |                            | 是   |          | 异常详细信息 |
| related_order_id | varchar  | 128  | 外键                       | 是   |          | 相关订单编号 |
| create_time      | datetime |      |                            | 否   | 当前时间 | 异常记录时间 |
| device_id        | varchar  | 255  |                            | 是   |          | 设备唯一标识 |
| browser          | varchar  | 50   |                            | 是   |          | 浏览器类型   |
| os               | varchar  | 50   |                            | 是   |          | 操作系统     |
| platform         | varchar  | 20   |                            | 是   |          | 平台         |
| ip_addr          | varchar  | 50   |                            | 否   |          | IP地址       |
| resolve_status   | varchar  | 32   |                            | 否   | 0        | 解决状态     |
| resolve_time     | datetime |      |                            | 是   |          | 解决时间     |
| remark           | varchar  | 512  |                            | 是   |          | 备注         |

解决状态：0未处理 1处理中 2已解决

异常类型：报错原因，简短的

```sql
DROP TABLE IF EXISTS po_error_log_info;
CREATE TABLE po_error_log_info (
    error_id VARCHAR(128) NOT NULL COMMENT '异常编号',
    user_id VARCHAR(128) COMMENT '用户编号',  
    method_id VARCHAR(128) NOT NULL COMMENT '支付方式',
    error_type VARCHAR(32) NOT NULL COMMENT '异常类型',
    error_message TEXT NOT NULL COMMENT '异常信息',
    error_details TEXT COMMENT '异常详细信息',
    related_order_id VARCHAR(128) COMMENT '关联订单编号',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '异常记录时间',
    device_id VARCHAR(255) COMMENT '设备唯一标识',
    browser VARCHAR(50) COMMENT '浏览器类型',
    os VARCHAR(50) COMMENT '操作系统',
    platform VARCHAR(20) COMMENT '平台（如Web、APP）',
    ip_addr VARCHAR(50) NOT NULL COMMENT 'IP地址',
    resolve_status VARCHAR(32) NOT NULL DEFAULT '0' COMMENT '解决状态（0未处理 1处理中 2已解决）',
    resolve_time DATETIME COMMENT '解决时间',
    remark VARCHAR(512) COMMENT '备注',
    PRIMARY KEY (error_id),
    FOREIGN KEY (user_id) REFERENCES u_user_info(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_error_type (error_type),
    INDEX idx_resolve_status (resolve_status),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='异常捕获表';
```



#### 风控日志表：po_risk_control_log_info

捕获此模块风控

| 字段名           | 类型     | 长度 | 键类型                     | Null | 默认值   | 描述         |
| ---------------- | -------- | ---- | -------------------------- | ---- | -------- | ------------ |
| log_id           | varchar  | 128  | 主键                       | 否   |          | 风控日志编号 |
| user_id          | varchar  | 128  | 外键 (u_user_info:user_id) | 否   |          | 用户编号     |
| method_id        | varchar  | 32   |                            | 否   |          | 支付方式     |
| risk_type        | varchar  | 32   |                            | 否   |          | 风险类型     |
| risk_level       | varchar  | 32   |                            | 否   |          | 风险等级     |
| risk_description | text     |      |                            | 否   |          | 风险描述     |
| action_taken     | varchar  | 32   |                            | 否   |          | 采取的措施   |
| action_time      | datetime |      |                            | 否   | 当前时间 | 措施时间     |
| create_time      | datetime |      |                            | 否   | 当前时间 | 风控记录时间 |
| device_id        | varchar  | 255  |                            | 是   |          | 设备唯一标识 |
| browser          | varchar  | 50   |                            | 是   |          | 浏览器类型   |
| os               | varchar  | 50   |                            | 是   |          | 操作系统     |
| platform         | varchar  | 20   |                            | 是   |          | 平台         |
| ip_addr          | varchar  | 50   |                            | 否   |          | IP地址       |
| remark           | varchar  | 512  |                            | 是   |          | 备注         |

风险等级：0低 1中 2高

风险原因，简短的

```sql
DROP TABLE IF EXISTS po_risk_control_log_info;
CREATE TABLE po_risk_control_log_info
(
    log_id           VARCHAR(128) NOT NULL COMMENT '风控日志编号',
    user_id          VARCHAR(128) NOT NULL COMMENT '用户编号',
    method_id   	 VARCHAR(128)  NOT NULL COMMENT '支付方式',
    risk_type        VARCHAR(32)  NOT NULL COMMENT '风险类型',
    risk_level       VARCHAR(32)  NOT NULL COMMENT '风险等级（0低 1中 2高）',
    risk_description TEXT         NOT NULL COMMENT '风险描述',
    action_taken     VARCHAR(32)  NOT NULL COMMENT '采取措施',
    action_time      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '措施时间',
    create_time      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
    device_id        VARCHAR(255) COMMENT '设备唯一标识',
    browser          VARCHAR(50) COMMENT '浏览器类型',
    os               VARCHAR(50) COMMENT '操作系统',
    platform         VARCHAR(20) COMMENT '平台（Web/APP）',
    ip_addr          VARCHAR(50)  NOT NULL COMMENT 'IP地址',
    remark           VARCHAR(512) COMMENT '备注',
    PRIMARY KEY (log_id),
    FOREIGN KEY (user_id) REFERENCES u_user_info (user_id),
    INDEX idx_risk_level (risk_level),
    INDEX idx_risk_type (risk_type),
    INDEX idx_action_time (action_time)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='风控日志表';
```



#### 提现记录表：po_withdrawal_order_info

| 字段名                    | 类型     | 长度 | 键类型                     | Null | 默认值   | 描述             |
| ------------------------- | -------- | ---- | -------------------------- | ---- | -------- | ---------------- |
| withdrawal_id             | varchar  | 128  | 主键                       | 否   |          | 提现订单编号     |
| user_id                   | varchar  | 128  | 外键 (u_user_info:user_id) | 否   |          | 用户编号         |
| points_withdrawal         | int      |      |                            | 否   | 0        | 提现积分         |
| amount_withdrawal         | decimal  | 10,2 |                            | 否   | 0.00     | 提现金额         |
| platform_fee              | decimal  | 10,2 |                            | 否   | 0.00     | 平台抽成金额     |
| user_received_amount      | decimal  | 10,2 |                            | 否   | 0.00     | 用户实际收到金额 |
| withdrawal_method         | char     | 1    |                            | 否   | 0        | 提现方式         |
| withdrawal_account        | varchar  | 64   |                            | 否   |          | 提现账户         |
| withdrawal_status         | varchar  | 32   |                            | 否   | 0        | 提现状态         |
| withdrawal_platform_order | varcahr  | 64   |                            | 是   |          | 提现平台订单号   |
| transaction_id            | varchar  | 128  |                            | 是   |          | 交易编号         |
| review_status             | char     | 1    |                            | 否   |          | 审核状态         |
| review_time               | datetime |      |                            | 是   |          | 审核时间         |
| review_user_id            | bigint   |      |                            | 是   |          | 审核人           |
| review_remark             | varchar  | 512  |                            | 是   |          | 审核建议         |
| accomplish_time           | datetime |      |                            | 是   |          | 完成时间         |
| device_id                 | varchar  | 255  |                            | 是   |          | 设备唯一标识     |
| browser                   | varchar  | 50   |                            | 是   |          | 浏览器类型       |
| os                        | varchar  | 50   |                            | 是   |          | 操作系统         |
| platform                  | varchar  | 20   |                            | 是   |          | 平台             |
| ip_addr                   | varchar  | 50   |                            | 否   |          | IP地址           |
| create_time               | datetime |      |                            | 否   | 当前时间 | 创建时间         |
| update_time               | datetime |      |                            | 否   | 当前时间 | 更新时间         |
| fail_reason               | varchar  | 500  |                            | 是   |          | 提现失败原因     |
| is_delete                 | char     | 1    |                            | 否   | 0        | 删除             |
| remark                    | varchar  | 512  |                            | 是   |          | 备注             |

提现状态：0待处理 1已完成 2失败 3超时

提现方式：0支付宝 1微信

审核状态：0待审核 1同意 2拒绝

```sql
DROP TABLE IF EXISTS po_withdrawal_order_info;
CREATE TABLE po_withdrawal_order_info
(
    withdrawal_id             VARCHAR(128)   NOT NULL COMMENT '提现订单编号',
    user_id                   VARCHAR(128)   COMMENT '用户编号',
    points_withdrawal         INT            NOT NULL DEFAULT 0 COMMENT '提现积分',
    amount_withdrawal         DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '提现金额',
    platform_fee              DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '平台抽成金额',
    user_received_amount      DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '用户实际收到金额',
    withdrawal_method         CHAR(1)        NOT NULL DEFAULT '0' COMMENT '提现方式（0支付宝 1微信）',
    withdrawal_account        VARCHAR(64)    NOT NULL COMMENT '提现账户',
    withdrawal_status         VARCHAR(32)    NOT NULL DEFAULT '0' COMMENT '提现状态（0待处理 1完成 2失败 3超时）',
    withdrawal_platform_order VARCHAR(64) COMMENT '提现平台订单号',
    transaction_id            VARCHAR(128) COMMENT '交易编号',
    review_status             CHAR(1)        NOT NULL DEFAULT '0' COMMENT '审核状态（0待审核 1同意 2拒绝）',
    review_time               DATETIME COMMENT '审核时间',
    review_user_id            BIGINT COMMENT '审核人编号',
    review_remark             VARCHAR(512) COMMENT '审核建议',
    accomplish_time           DATETIME COMMENT '完成时间',
    device_id                 VARCHAR(255) COMMENT '设备唯一标识',
    browser                   VARCHAR(50) COMMENT '浏览器类型',
    os                        VARCHAR(50) COMMENT '操作系统',
    platform                  VARCHAR(20) COMMENT '平台',
    ip_addr                   VARCHAR(50)    NOT NULL COMMENT 'IP地址',
    create_time               DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time               DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    fail_reason               VARCHAR(500) COMMENT '提现失败原因',
    is_delete                 CHAR(1)        NOT NULL DEFAULT '0' COMMENT '删除（0否 1是）',
    remark                    VARCHAR(512) COMMENT '备注',
    PRIMARY KEY (withdrawal_id),
    FOREIGN KEY (user_id) REFERENCES u_user_info (user_id),
    INDEX idx_withdrawal_status (withdrawal_status),
    INDEX idx_review_status (review_status),
    INDEX idx_create_time (create_time)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户提现记录表';
```



#### 积分使用记录表：po_points_usage_log

记录积分的使用流水，充值消费都会有

| 字段名        | 类型     | 长度 | 键类型                     | Null | 默认值   | 描述         |
| ------------- | -------- | ---- | -------------------------- | ---- | -------- | ------------ |
| log_id        | varchar  | 128  | 主键                       | 否   |          | 记录编号     |
| user_id       | varchar  | 128  | 外键 (u_user_info:user_id) | 否   |          | 用户编号     |
| usage_type    | varchar  | 4    |                            | 否   |          | 使用类型     |
| target_id     | varchar  | 128  |                            | 是   |          | 目标编号     |
| points_before | int      |      |                            | 否   | 0        | 使用前积分   |
| points_used   | int      |      |                            | 否   | 0        | 消费的积分   |
| points_after  | int      |      |                            | 否   | 0        | 使用后积分   |
| device_id     | varchar  | 255  |                            | 是   |          | 设备唯一标识 |
| browser       | varchar  | 50   |                            | 是   |          | 浏览器类型   |
| os            | varchar  | 50   |                            | 是   |          | 操作系统     |
| platform      | varchar  | 20   |                            | 是   |          | 平台         |
| ip_addr       | varchar  | 50   |                            | 否   |          | IP地址       |
| remark        | varchar  | 512  |                            | 是   |          | 备注         |
| create_time   | datetime |      |                            | 否   | 当前时间 | 创建时间     |
| update_time   | datetime |      |                            | 否   | 当前时间 | 更新时间     |
| is_delete     | char     | 1    |                            | 否   | 0        | 删除         |

使用类型：0下载图片 1AI

使用目标：比如下载了某一张图片，图片的编号

同时，如果用户下载某一张图片，那创作者也会获得积分，会给创作者按照分成添加积分

```sql
DROP TABLE IF EXISTS po_points_usage_log;
CREATE TABLE po_points_usage_log
(
    log_id        VARCHAR(128) NOT NULL COMMENT '记录编号',
    user_id       VARCHAR(128) NOT NULL COMMENT '用户编号',
    usage_type    VARCHAR(4)   NOT NULL COMMENT '使用类型（0下载图片 1AI服务）',
    target_id     VARCHAR(128) COMMENT '目标编号',
    points_before INT          NOT NULL DEFAULT 0 COMMENT '使用前积分',
    points_used   INT          NOT NULL DEFAULT 0 COMMENT '消费积分',
    points_after  INT          NOT NULL DEFAULT 0 COMMENT '使用后积分',
    device_id     VARCHAR(255) COMMENT '设备唯一标识',
    browser       VARCHAR(50) COMMENT '浏览器类型',
    os            VARCHAR(50) COMMENT '操作系统',
    platform      VARCHAR(20) COMMENT '平台',
    ip_addr       VARCHAR(50)  NOT NULL COMMENT 'IP地址',
    remark        VARCHAR(512) COMMENT '备注',
    create_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_delete     CHAR(1)      NOT NULL DEFAULT '0' COMMENT '删除（0否 1是）',
    PRIMARY KEY (log_id),
    FOREIGN KEY (user_id) REFERENCES u_user_info (user_id),
    INDEX idx_usage_type (usage_type),
    INDEX idx_user_usage (user_id, usage_type),
    INDEX idx_target (target_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='积分使用记录表';
```



### AI模块

#### 模型参数表：ai_model_params_info

| 字段名            | 类型     | 长度 | 键类型                    | Null | 默认值   | 描述           |
| ----------------- | -------- | ---- | ------------------------- | ---- | -------- | -------------- |
| model_id          | varchar  | 128  | 主键                      | 否   |          | 模型编号       |
| model_name        | varchar  | 128  |                           | 否   |          | 模型名称       |
| model_type        | varchar  | 50   |                           | 否   |          | 模型类型       |
| api_key           | varchar  | 256  |                           | 是   |          | 安全密钥       |
| secret_key        | varchar  | 256  |                           | 是   |          | 安全KEY        |
| model_params      | text     |      |                           | 否   |          | 模型参数       |
| model_description | varchar  | 1024 |                           | 是   |          | 模型介绍       |
| tokens_avg        | int      |      |                           | 否   | 0        | 平均使用tokens |
| tokens_total      | int      |      |                           | 否   | 0        | 总共消耗Tokens |
| usage_count       | int      |      |                           | 否   | 0        | 使用次数       |
| ponints_need      | int      |      |                           | 是   | 0        | 积分           |
| extend_config     | varchar  | 1024 |                           | 是   |          | 扩展配置       |
| params_status     | char     | 1    |                           | 否   | 1        | 状态           |
| user_id           | bigint   |      | 外键（sys_user：user_id） | 否   |          | 管理员         |
| create_by         | varchar  | 32   |                           | 否   |          | 创建人         |
| create_time       | datetime |      |                           | 否   | 当前时间 | 创建时间       |
| update_by         | varchar  | 32   |                           | 是   |          | 更新人         |
| update_time       | datetime |      |                           | 是   | 当前时间 | 更新时间       |
| remark            | varchar  | 512  |                           | 是   |          | 备注           |

状态：0开启 1关闭

模型类型：自定义，比如编辑图片，对话模型

api_key:必须加密

secret_key:必须加密

积分：单位1，比如一个积分替换多少tokens

```sql
DROP TABLE IF EXISTS ai_model_params_info;
CREATE TABLE ai_model_params_info
(
    model_id          VARCHAR(128) NOT NULL COMMENT '模型编号',
    model_name        VARCHAR(128) NOT NULL COMMENT '模型名称',
    model_type        VARCHAR(50)  NOT NULL COMMENT '模型类型',
    api_key           VARCHAR(256) COMMENT '安全密钥',
    secret_key        VARCHAR(256) COMMENT '安全KEY',
    model_params            TEXT         NOT NULL COMMENT '模型参数',
    model_description VARCHAR(1024) COMMENT '模型介绍',
    tokens_avg        INT          NOT NULL DEFAULT 0 COMMENT '平均使用tokens/次',
    tokens_total      INT          NOT NULL DEFAULT 0 COMMENT '累计消耗Tokens',
    usage_count       INT          NOT NULL DEFAULT 0 COMMENT '使用次数',
    points_need       INT                   DEFAULT 0 COMMENT '积分消耗比例',
    extend_config     VARCHAR(1024) COMMENT '扩展配置',
    params_status     CHAR(1)      NOT NULL DEFAULT '1' COMMENT '状态（0开启 1关闭）',
    user_id           BIGINT       NOT NULL COMMENT '管理员编号',
    create_by         VARCHAR(32)  NOT NULL COMMENT '创建人',
    create_time       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by         VARCHAR(32) COMMENT '更新人',
    update_time       DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark            VARCHAR(512) COMMENT '备注',
    PRIMARY KEY (model_id),
    UNIQUE KEY uk_model_name (model_name),
    FOREIGN KEY (user_id) REFERENCES sys_user (user_id) ON DELETE SET NULL ,
    INDEX idx_model_type (model_type),
    INDEX idx_status (params_status)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='AI模型参数配置表';
```



#### 用户使用AI记录：ai_user_usage_log_info

| 字段名           | 类型     | 长度 | 键类型                                | Null | 默认值   | 描述               |
| ---------------- | -------- | ---- | ------------------------------------- | ---- | -------- | ------------------ |
| log_id           | varchar  | 128  | 主键                                  | 否   |          | 记录编号           |
| user_id          | varchar  | 128  | 外键（u_user_info:user_id）           | 否   |          | 用户编号           |
| model_id         | varchar  | 128  | 外键（ai_model_params_info:model_id） | 否   |          | 使用的模型编号     |
| input_params     | varchar  | 1024 |                                       | 是   |          | 输入参数           |
| output_result    | text     |      |                                       | 是   |          | 返回结果           |
| request_time     | datetime |      |                                       | 否   |          | 请求时间           |
| request_duration | bigint   |      |                                       | 是   |          | 请求时长·          |
| tokens_used      | int      |      |                                       | 否   | 0        | 使用的 tokens 数量 |
| points_used      | int      |      |                                       | 否   | 0        | 消耗的积分         |
| usage_type       | varchar  | 50   |                                       | 否   |          | 使用类型           |
| target_id        | varchar  | 128  |                                       | 是   |          | 目标编号           |
| log_status       | char     | 1    |                                       | 否   |          | 状态               |
| ai_status_code   | varchar  | 16   |                                       | 是   |          | 模型返回码         |
| fail_reason      | varchar  | 128  |                                       | 是   |          | 失败原因           |
| ip_addr          | varchar  | 50   |                                       | 否   |          | 用户IP地址         |
| device_id        | varchar  | 255  |                                       | 是   |          | 用户设备唯一标识   |
| browser          | varchar  | 50   |                                       | 是   |          | 浏览器类型         |
| os               | varchar  | 50   |                                       | 是   |          | 操作系统           |
| platform         | varchar  | 20   |                                       | 是   |          | 平台               |
| create_time      | datetime |      |                                       | 否   | 当前时间 | 创建时间           |
| update_time      | datetime |      |                                       | 否   | 当前时间 | 更新时间           |
| is_delete        | char     | 1    |                                       | 否   | 0        | 删除               |

使用类型：0ai扩图 1AI编辑 2AI搜索。。。。

状态：0成功 1失败 2超时

```sql
DROP TABLE IF EXISTS ai_user_usage_log_info;
CREATE TABLE ai_user_usage_log_info
(
    log_id           VARCHAR(128) NOT NULL COMMENT '记录编号',
    user_id          VARCHAR(128) NOT NULL COMMENT '用户编号',
    model_id         VARCHAR(128) NOT NULL COMMENT '模型编号',
    input_params     VARCHAR(1024) COMMENT '输入参数',
    output_result    TEXT COMMENT '返回结果',
    request_time     DATETIME     NOT NULL COMMENT '请求时间',
    request_duration BIGINT COMMENT '请求时长（毫秒）',
    tokens_used      INT          NOT NULL DEFAULT 0 COMMENT '消耗Tokens数量',
    points_used      INT          NOT NULL DEFAULT 0 COMMENT '消耗积分',
    usage_type       VARCHAR(50)  NOT NULL COMMENT '使用类型（0AI扩图 1AI编辑 2AI搜索）',
    target_id        VARCHAR(128) COMMENT '目标编号',
    log_status       CHAR(1)      NOT NULL COMMENT '状态（0成功 1失败 2超时）',
    ai_status_code   VARCHAR(16) COMMENT '模型返回码',
    fail_reason      VARCHAR(128) COMMENT '失败原因',
    ip_addr          VARCHAR(50)  NOT NULL COMMENT '用户IP地址',
    device_id        VARCHAR(255) COMMENT '设备唯一标识',
    browser          VARCHAR(50) COMMENT '浏览器类型',
    os               VARCHAR(50) COMMENT '操作系统',
    platform         VARCHAR(20) COMMENT '平台',
    create_time      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_delete        CHAR(1)      NOT NULL DEFAULT '0' COMMENT '删除（0正常 1删除）',
    PRIMARY KEY (log_id),
    FOREIGN KEY (user_id) REFERENCES u_user_info (user_id),
    FOREIGN KEY (model_id) REFERENCES ai_model_params_info (model_id),
    INDEX idx_usage_type (usage_type),
    INDEX idx_status (log_status),
    INDEX idx_user_model (user_id, model_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户AI使用记录表';
```



#### 官方使用AI记录表：ai_official_usage_log_info

| 字段名           | 类型     | 长度 | 键类型                                | Null | 默认值   | 描述               |
| ---------------- | -------- | ---- | ------------------------------------- | ---- | -------- | ------------------ |
| log_id           | varchar  | 128  | 主键                                  | 否   |          | 记录编号           |
| admin_id         | varchar  | 128  | 外键（sys_user:user_id）              | 否   |          | 操作管理员的编号   |
| model_id         | varchar  | 128  | 外键（ai_model_params_info:model_id） | 否   |          | 使用的模型编号     |
| operation_type   | varchar  | 50   |                                       | 否   |          | 操作类型           |
| input_params     | varchar  | 1024 |                                       | 是   |          | 输入的参数         |
| output_result    | text     |      |                                       | 是   |          | 模型返回的结果     |
| request_time     | datetime |      |                                       | 否   |          | 请求时间           |
| request_duration | bigint   |      |                                       | 是   |          | 请求时长           |
| tokens_used      | int      |      |                                       | 否   | 0        | 使用的 tokens 数量 |
| log_status       | char     | 1    |                                       | 否   | 0        | 状态               |
| ai_status_code   | varchar  | 16   |                                       | 是   |          | 模型返回码         |
| fail_reason      | varchar  | 128  |                                       | 是   |          | 失败原因           |
| remark           | varchar  | 512  |                                       | 是   |          | 备注               |
| create_time      | datetime |      |                                       | 否   | 当前时间 | 创建时间           |
| update_time      | datetime |      |                                       | 否   | 当前时间 | 更新时间           |
| is_delete        | char     | 1    |                                       | 否   | 0        | 删除标志           |

使用类型：0数据分析 。。。

```sql
DROP TABLE IF EXISTS ai_official_usage_log_info;
CREATE TABLE ai_official_usage_log_info
(
    log_id           VARCHAR(128) NOT NULL COMMENT '记录编号',
    user_id          bigint COMMENT '管理员编号',
    model_id         VARCHAR(128) NOT NULL COMMENT '模型编号',
    operation_type   VARCHAR(50)  NOT NULL COMMENT '操作类型（如：data_analysis）',
    input_params     VARCHAR(1024) COMMENT '输入参数（JSON格式）',
    output_result    TEXT COMMENT '模型返回结果（JSON/Text格式）',
    request_time     DATETIME     NOT NULL COMMENT '请求时间',
    request_duration BIGINT COMMENT '请求时长（毫秒）',
    tokens_used      INT          NOT NULL DEFAULT 0 COMMENT '消耗Tokens数量',
    ai_status_code   VARCHAR(16) COMMENT '模型返回状态码',
    fail_reason      VARCHAR(128) COMMENT '失败原因',
    remark           VARCHAR(512) COMMENT '备注',
    create_time      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_delete        CHAR(1)      NOT NULL DEFAULT '0' COMMENT '删除（0正常 1删除）',
    PRIMARY KEY (log_id),
    FOREIGN KEY (user_id) REFERENCES sys_user (user_id),
    FOREIGN KEY (model_id) REFERENCES ai_model_params_info (model_id),
    INDEX idx_operation_type (operation_type),
    INDEX idx_request_time (request_time),
    INDEX idx_status (log_status)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='官方AI操作日志表';
```



#### 会话表设计：ai_conversation_session

| 字段名            | 类型     | 长度 | 键类型                      | Null | 默认值   | 描述                         |
| ----------------- | -------- | ---- | --------------------------- | ---- | -------- | ---------------------------- |
| session_id        | varchar  | 128  | 主键                        | 否   |          | 会话 编号                    |
| user_id           | varchar  | 128  | 外键（u_user_info:user_id） | 否   |          | 用户编号                     |
| conversation_id   | varchar  | 128  |                             | 是   |          | AI会话编号                   |
| session_name      | varchar  | 32   |                             | 是   |          | 对话名称                     |
| tokens_total_used | int      |      |                             | 否   | 0        | 会话中总共使用的 tokens 数量 |
| points_total_used | int      |      |                             | 否   | 0        | 会话中总共消耗的积分         |
| remark            | varchar  | 512  |                             | 是   |          | 备注                         |
| ip_addr           | varchar  | 50   |                             | 否   |          | 用户IP地址                   |
| device_id         | varchar  | 255  |                             | 是   |          | 用户设备唯一标识             |
| browser           | varchar  | 50   |                             | 是   |          | 浏览器类型                   |
| os                | varchar  | 50   |                             | 是   |          | 操作系统                     |
| platform          | varchar  | 20   |                             | 是   |          | 平台                         |
| create_time       | datetime |      |                             | 否   | 当前时间 | 创建时间                     |
| update_time       | datetime |      |                             | 是   | 当前时间 | 更新时间                     |
| is_delete         | char     | 1    |                             | 否   | 0        | 删除标志                     |

AI会话编号：AI保持长轮训的编号

```sql
DROP TABLE IF EXISTS ai_conversation_session_info;
CREATE TABLE ai_conversation_session_info
(
    session_id        VARCHAR(128) NOT NULL COMMENT '会话编号',
    user_id           VARCHAR(128) NOT NULL COMMENT '用户编号',
    conversation_id   VARCHAR(128) COMMENT 'AI会话编号',
    session_name      VARCHAR(32) COMMENT '对话名称',
    tokens_total_used INT          NOT NULL DEFAULT 0 COMMENT '累计消耗Tokens',
    points_total_used INT          NOT NULL DEFAULT 0 COMMENT '累计消耗积分',
    remark            VARCHAR(512) COMMENT '备注',
    ip_addr           VARCHAR(50)  NOT NULL COMMENT '用户IP地址',
    device_id         VARCHAR(255) COMMENT '设备唯一标识',
    browser           VARCHAR(50) COMMENT '浏览器类型',
    os                VARCHAR(50) COMMENT '操作系统',
    platform          VARCHAR(20) COMMENT '平台',
    create_time       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_delete         CHAR(1)      NOT NULL DEFAULT '0' COMMENT '删除（0正常 1删除）',
    PRIMARY KEY (session_id),
    FOREIGN KEY (user_id) REFERENCES u_user_info (user_id),
    INDEX idx_conversation_id (conversation_id),
    INDEX idx_user_session (user_id, session_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='AI会话管理表';
```



#### 对话记录表：ai_conversation_log

| 字段名              | 类型     | 长度 | 键类型                                     | Null | 默认值   | 描述               |
| ------------------- | -------- | ---- | ------------------------------------------ | ---- | -------- | ------------------ |
| conversation_id     | varchar  | 128  | 主键                                       | 否   |          | 对话记录编号       |
| session_id          | varchar  | 128  | 外键（ai_conversation_session:session_id） | 否   |          | 会话 编号          |
| user_id             | varchar  | 128  | 外键（u_user_info:user_id）                | 否   |          | 用户编号           |
| input_text          | text     |      |                                            | 否   |          | 用户输入文本       |
| output_text         | text     |      |                                            | 否   |          | AI 返回的文本      |
| request_time        | datetime |      |                                            | 否   | 当前时间 | 请求时间           |
| response_time       | datetime |      |                                            | 否   | 当前时间 | AI 返回时间        |
| tokens_used         | int      |      |                                            | 否   | 0        | 使用的 tokens 数量 |
| points_used         | int      |      |                                            | 否   | 0        | 消耗的积分         |
| conversation_status | char     | 1    |                                            | 否   |          | 状态               |
| ai_status_code      | varchar  | 16   |                                            | 是   |          | 模型返回码         |
| fail_reason         | varchar  | 128  |                                            | 是   |          | 失败原因           |
| conversation_type   | varchar  | 50   |                                            | 否   |          | 对话类型           |
| create_time         | datetime |      |                                            | 否   | 当前时间 | 创建时间           |

对话类型：0文本、1图片

状态：0成功 1失败 2超时

```sql
DROP TABLE IF EXISTS ai_conversation_log_info;
CREATE TABLE ai_conversation_log_info
(
    conversation_id     VARCHAR(128) NOT NULL COMMENT '对话记录编号',
    session_id          VARCHAR(128) NOT NULL COMMENT '会话编号',
    user_id             VARCHAR(128) NOT NULL COMMENT '用户编号',
    input_text          TEXT         NOT NULL COMMENT '用户输入文本',
    output_text         TEXT         NOT NULL COMMENT 'AI返回文本',
    request_time        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '请求时间',
    response_time       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '响应时间',
    tokens_used         INT          NOT NULL DEFAULT 0 COMMENT '消耗Tokens数量',
    points_used         INT          NOT NULL DEFAULT 0 COMMENT '消耗积分',
    conversation_status CHAR(1)      NOT NULL COMMENT '状态（0=成功 1=失败）',
    ai_status_code      VARCHAR(16) COMMENT '模型返回码',
    fail_reason         VARCHAR(128) COMMENT '失败原因',
    conversation_type   VARCHAR(50)  NOT NULL COMMENT '对话类型（0文本 1图片）',
    create_time         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (conversation_id),
    FOREIGN KEY (session_id) REFERENCES ai_conversation_session_info (session_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES u_user_info (user_id),
    INDEX idx_session (session_id),
    INDEX idx_conversation_type (conversation_type),
    INDEX idx_request_time (request_time)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='AI对话明细记录表';
```



### 图库模块

#### 空间信息表：p_space_info

| 字段名           | 类型     | 长度 | 键类型                    | null | 默认值     | 描述                          |
| ---------------- | -------- | ---- | ------------------------- | ---- | ---------- | ----------------------------- |
| space_id         | varchar  | 128  | 主键                      | 否   |            | 空间编号                      |
| space_name       | varchar  | 32   | 唯一                      | 否   |            | 空间名称                      |
| space_avatar     | varchar  | 512  |                           | 是   |            | 空间封面                      |
| oss_type         | char     | 1    |                           | 否   | 1          | 存储类型                      |
| oss_config       | varcahr  | 1024 |                           | 是   |            | 存储配置                      |
| max_size         | bigint   | 20   |                           | 是   | 1073741824 | 最大容量                      |
| max_count        | bigint   | 20   |                           | 是   | 1000       | 最大文件数                    |
| total_size       | bigint   | 20   |                           | 是   | 0          | 已用容量                      |
| total_count      | bigint   | 20   |                           | 是   | 0          | 文件总数                      |
| space_status     | char     | 1    |                           | 否   |            | 空间状态                      |
| user_id          | varchar  | 128  | 外键(u_user_info:user_id) | 否   |            | 用户                          |
| space_desc       | varchar  | 512  |                           | 是   |            | 空间描述                      |
| create_time      | datetime |      |                           | 否   | 当前时间   | 创建时间                      |
| last_update_time | datetime |      |                           | 是   | 当前时间   | 最后上传文件时间              |
| update_time      | datetime |      |                           | 是   | 当前时间   | 最后更新时间                  |
| is_delete        | char     | 1    |                           | 否   | 0          | 删除(0否 1是) 默认0           |
| deleted_time     | datetime |      |                           | 是   |            | 删除时间                      |
| space_type       | char     | 1    |                           | 否   | 0          | 空间类型（0个人 1团队 2官方） |
| member_limit     | int      |      |                           | 是   | 10         | 人数上限                      |
| current_members  | int      |      |                           | 是   | 0          | 当前人数                      |

存储类型：0官方 1阿里云 。。。。其他的用户自定义

存储配置：存储后转为json，比如说一些阿里的oss配置，腾讯云的配置

最大容量：用户自定义，字节

已用容量：自定义，计算这个图库的当前已用的容量，字节

最大文件数：用户自定义

文件总数：当前文件总数

人数上限：当空间为团队空间时，拥有人数上限

当前人数：当空间为团队空间时，记录当前人数

空间状态：0公共 1私有

删除时间：如果用户在删除时间三十天后永久删除此数据

空间类型：2个人 1团队 0官方

```sql
DROP TABLE IF EXISTS p_space_info;
CREATE TABLE p_space_info
(
    space_id         VARCHAR(128) NOT NULL COMMENT '空间编号',
    space_name       VARCHAR(32)  NOT NULL COMMENT '空间名称',
    space_avatar     VARCHAR(512) COMMENT '空间封面URL',
    oss_type         CHAR(1)      NOT NULL DEFAULT '1' COMMENT '存储类型（0官方 阿里云）',
    oss_config       VARCHAR(1024) COMMENT '存储配置',
    max_size         BIGINT                DEFAULT 1073741824 COMMENT '最大容量（字节）',
    max_count        BIGINT                DEFAULT 1000 COMMENT '最大文件数',
    total_size       BIGINT                DEFAULT 0 COMMENT '已用容量（字节）',
    total_count      BIGINT                DEFAULT 0 COMMENT '文件总数',
    user_id          VARCHAR(128) NOT NULL COMMENT '所属用户',
    space_desc       VARCHAR(512) COMMENT '空间描述',
    space_status     CHAR(1)      NOT NULL COMMENT '空间状态',
    space_type       CHAR(1)      NOT NULL DEFAULT '0' COMMENT '空间类型（0个人 1团队 2官方）',
    member_limit     INT                   DEFAULT 10 COMMENT '成员上限',
    current_members  INT                   DEFAULT 0 COMMENT '当前成员数',
    create_time      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    last_update_time DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '最后上传时间',
    update_time      DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    is_delete       CHAR(1)      NOT NULL DEFAULT '0' COMMENT '删除（0否 1是）',
    deleted_time     DATETIME COMMENT '删除时间',
    PRIMARY KEY (space_id),
    UNIQUE KEY uk_space_name (space_name),
    FOREIGN KEY (user_id) REFERENCES u_user_info (user_id),
    INDEX idx_space_type (space_type),
    INDEX idx_is_delete (is_delete),
    index idx_space_status (space_status)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='空间信息表';
```



#### 空间邀请记录表：p_space_invitation_info

记录邀请进入此空间的记录

| 字段名             | 类型     | 长度 | 键类型                      | nuu  | 默认值 | 描述     |
| ------------------ | -------- | ---- | --------------------------- | ---- | ------ | -------- |
| invitation_id      | varchar  | 128  | 主键                        | 否   |        | 邀请编号 |
| space_id           | varchar  | 128  | 外键(p_space_info:space_id) | 否   |        | 空间编号 |
| space_name         | varchar  | 32   |                             | 否   |        | 空间名称 |
| space_avatar       | varchar  | 256  |                             | 是   |        | 空间封面 |
| role_type          | char     | 1    |                             | 否   |        | 邀请角色 |
| invitation_status  | char     | 1    |                             | 否   |        | 邀请状态 |
| invitation_url     | varchar  | 256  |                             | 是   |        | 邀请链接 |
| invitation         | varchar  | text |                             | 是   |        | 邀请理由 |
| invitation_user_id | varchar  | 128  | 外键(u_user_info:user_id)   | 否   |        | 邀请用户 |
| *expire_*time      | datetime |      |                             | 否   |        | 过期时间 |
| create_time        | datetime |      |                             | 否   |        | 创建时间 |
| user_id            | varchar  | 128  | 外键(u_user_info:user_id)   | 否   |        | 用户     |

角色：0创建者 1管理员 2编辑者 3浏览者

邀请状态：0待同意 1同意 2拒绝 3过期

邀请链接：用户可以点击链接访问

```sql
DROP TABLE IF EXISTS p_space_invitation_info;
CREATE TABLE p_space_invitation_info
(
    invitation_id      VARCHAR(128) NOT NULL COMMENT '邀请编号',
    space_id           VARCHAR(128) NOT NULL COMMENT '空间编号',
    space_name         VARCHAR(32)  NOT NULL COMMENT '空间名称',
    space_avatar       VARCHAR(256) COMMENT '空间封面URL',
    role_type          CHAR(1)      NOT NULL COMMENT '邀请角色（0创建者 1管理员 2编辑者 3浏览者）',
    invitation_status  CHAR(1)      NOT NULL DEFAULT '0' COMMENT '邀请状态（0待同意 1同意 2拒绝 3过期）',
    invitation_url     VARCHAR(256) COMMENT '邀请链接（短链或唯一标识）',
    invitation         TEXT COMMENT '邀请理由',
    invitation_user_id VARCHAR(128) NOT NULL COMMENT '邀请人编号',
    expire_time        DATETIME     NOT NULL COMMENT '过期时间',
    create_time        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    user_id            VARCHAR(128) NOT NULL COMMENT '被邀请用户编号',
    PRIMARY KEY (invitation_id),
    FOREIGN KEY (space_id) REFERENCES p_space_info (space_id),
    FOREIGN KEY (invitation_user_id) REFERENCES u_user_info (user_id),
    FOREIGN KEY (user_id) REFERENCES u_user_info (user_id),
    UNIQUE KEY uk_invitation_url (invitation_url),
    INDEX idx_invitation_status (invitation_status),
    INDEX idx_expire_time (expire_time)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='空间成员邀请记录表';
```



#### 空间成员表 ：p_space_member_info

记录此空间有哪些成员

| 字段名           | 类型     | 长度 | 键类型                      | Null | 默认值   | 描述         |
| ---------------- | -------- | ---- | --------------------------- | ---- | -------- | ------------ |
| member_id        | varchar  | 128  | 主键                        | 否   |          | 成员编号     |
| space_id         | varchar  | 128  | 外键(p_space_info:space_id) | 否   |          | 空间编号     |
| user_id          | varchar  | 128  | 外键(u_user_info:user_id)   | 否   |          | 成员编号     |
| role_type        | char     | 1    |                             | 否   |          | 角色         |
| last_active_time | datetime |      |                             | 是   | 当前时间 | 最后操作时间 |
| create_time      | datetime |      |                             | 否   | 当前时间 | 加入时间     |
| update_time      | datetime |      |                             | 是   |          | 更新时间     |
| inviter_user_id  | varchar  | 128  |                             | 是   |          | 邀请者       |
| join_type        | char     | 1    |                             | 否   | 0        | 加入方式     |
| remark           | varchar  | 128  |                             | 是   |          | 备注         |

角色：0创建者 1管理员 2编辑者 3浏览者

加入方式：0邀请。。。便于后续扩展，当前仅邀请

```sql
DROP TABLE IF EXISTS p_space_member_info;
CREATE TABLE p_space_member_info
(
    member_id        VARCHAR(128) NOT NULL COMMENT '成员编号',
    space_id         VARCHAR(128) NOT NULL COMMENT '空间编号',
    user_id          VARCHAR(128) NOT NULL COMMENT '用户编号',
    role_type        CHAR(1)      NOT NULL COMMENT '角色（0创建者 1管理员 2编辑者 3浏览者）',
    last_active_time DATETIME              DEFAULT CURRENT_TIMESTAMP COMMENT '最后操作时间',
    create_time      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
    update_time      DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    inviter_user_id  VARCHAR(128) COMMENT '邀请人编号',
    join_type        CHAR(1)      NOT NULL DEFAULT '0' COMMENT '加入方式（0邀请）',
    remark           VARCHAR(128) COMMENT '备注',
    PRIMARY KEY (member_id),
    FOREIGN KEY (space_id) REFERENCES p_space_info (space_id),
    FOREIGN KEY (user_id) REFERENCES u_user_info (user_id),
    FOREIGN KEY (inviter_user_id) REFERENCES u_user_info (user_id) ON DELETE SET NULL,
    UNIQUE KEY uk_space_user (space_id, user_id),
    INDEX idx_role_type (role_type),
    INDEX idx_join_type (join_type)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='空间成员信息表';
```



#### 文件夹表 :p_space_folder_info

记录空间内的文件夹

| 字段名       | 类型     | 长度 | 键类型                      | Null | 默认值   | 描述       |
| ------------ | -------- | ---- | --------------------------- | ---- | -------- | ---------- |
| folder_id    | varchar  | 128  | 主键                        | 否   |          | 文件夹编号 |
| space_id     | varchar  | 128  | 外键(p_space_info:space_id) | 否   |          | 空间编号   |
| parent_id    | varchar  | 128  |                             | 否   | 0        | 父级       |
| ancestors    | varchar  | 1280 |                             | 否   |          | 祖级列表   |
| folder_name  | varchar  | 32   | 索引                        | 否   |          | 文件夹名称 |
| full_path    | varchar  | 1024 | 索引                        | 否   |          | 路径快照   |
| folder_level | tinyint  |      |                             | 否   | 1        | 等级       |
| user_id      | varchar  |      | 外键(u_user_info:user_id)   | 否   |          | 创建人     |
| sort_order   | tinyint  |      |                             | 否   | 0        | 排序权重   |
| create_time  | datetime |      |                             | 否   | 当前时间 | 创建时间   |
| update_time  | datetime |      |                             | 是   | 当前时间 | 更新时间   |
| remark       | varchar  | 128  |                             | 是   |          | 备注       |

等级：最大7，只支持7级，默认顶级为1  

父级：顶级父类为0

祖级列表：记录文件夹的父类，方便查询文件夹子集下面的图片

空间+父级+文件夹名称组成唯一键	

```sql
DROP TABLE IF EXISTS p_space_folder_info;
CREATE TABLE p_space_folder_info
(
    folder_id    VARCHAR(128)     NOT NULL COMMENT '文件夹编号',
    space_id     VARCHAR(128)     NOT NULL COMMENT '空间编号',
    parent_id    VARCHAR(128)     NOT NULL DEFAULT '0' COMMENT '父文件夹编号',
    ancestors    VARCHAR(1280)    NOT NULL COMMENT '祖级列表',
    folder_name  VARCHAR(32)      NOT NULL COMMENT '文件夹名称',
    full_path    VARCHAR(1024)    NOT NULL COMMENT '完整路径（格式：/文件夹名1/文件夹名2/）',
    folder_level TINYINT UNSIGNED NOT NULL DEFAULT 1 COMMENT '层级',
    user_id      VARCHAR(128)     NOT NULL COMMENT '创建人',
    sort_order   TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序权重',
    create_time  DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark       VARCHAR(128) COMMENT '备注',
    PRIMARY KEY (folder_id),
    FOREIGN KEY (space_id) REFERENCES p_space_info (space_id) ON DELETE CASCADE,
    FOREIGN KEY (parent_id) REFERENCES p_space_folder_info (folder_id),
    FOREIGN KEY (user_id) REFERENCES u_user_info (user_id),
    UNIQUE KEY uk_folder_unique (space_id, parent_id, folder_name),
    INDEX idx_full_path (full_path(200)),
    INDEX idx_ancestors (ancestors(200))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='空间文件夹表';
```



#### 图片分类信息表：p_picture_category_info

记录图片的分类，便于图片的分类统计，图片可以选择分类

| 字段名          | 类型     | 长度 | 键类型 | Null | 默认值   | 描述     |
| --------------- | -------- | ---- | ------ | ---- | -------- | -------- |
| category_id     | varchar  | 128  | 主键   | 否   |          | 分类编号 |
| parent_id       | varchar  | 128  |        | 否   | 0        | 父级     |
| ancestors       | varchar  | 1280 |        | 否   |          | 祖级列表 |
| cover_url       | varchar  | 512  |        | 是   |          | 封面图   |
| name            | varchar  | 32   | 唯一键 | 否   |          | 分类名称 |
| category_desc   | varchar  | 512  |        | 是   |          | 分类描述 |
| category_status | char     | 1    |        | 否   |          | 分类状态 |
| category_type   | char     | 1    |        | 否   |          | 分类类型 |
| query_status    | char     | 1    |        | 否   |          | 查询状态 |
| usage_count     | bigint   |      | 索引   | 否   | 0        | 使用次数 |
| look_count      | bigint   |      | 索引   | 否   | 0        | 查看次数 |
| download_count  | bigint   |      | 索引   | 否   | 0        | 下载次数 |
| create_time     | datetime |      |        | 否   | 当前时间 | 创建时间 |
| update_time     | datetime |      |        | 否   | 当前时间 | 更新时间 |
| is_delete       | char     | 1    |        | 否   | 0        | 删除     |

分类状态：0正常 1关闭，是否可以查询到且使用

分类类型：0图片 。。。

查询状态：0是 1否，记录后续图片列表根据图片类别分类的查询条件

```sql
create table p_picture_category_info
(
    category_id     varchar(128)                           not null comment '分类编号' primary key,
    parent_id       varchar(128) default '0'               not null comment '父级分类编号',
    ancestors       varchar(1280)                          not null comment '祖级列表',
    cover_url       varchar(512)                           null comment '封面图URL',
    name            varchar(32)                            not null comment '分类名称',
    category_desc   varchar(512)                           null comment '分类描述',
    category_status char         default '0'               not null comment '分类状态（0正常 1关闭）',
    category_type   char         default '0'               not null comment '分类类型',
    query_status    char         default '0'               not null comment '查询状态（0是 1否）',
    usage_count     bigint       default 0                 not null comment '使用次数',
    look_count      bigint       default 0                 not null comment '查看次数',
    download_count  bigint       default 0                 not null comment '下载次数',
    create_time     datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time     datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete       char         default '0'               not null comment '删除标记（0否 1是）'
)comment '图片分类信息表';
create index idx_download_count
    on p_picture_category_info (download_count);
create index idx_look_count
    on p_picture_category_info (look_count);
create index idx_usage_count
    on p_picture_category_info (usage_count);
create index parent_id
    on p_picture_category_info (parent_id)

```



#### 图片标签表：p_picture_tag_info

图片的标签，统一管理

| 字段名         | 类型     | 长度 | 键类型                    | Null | 默认值   | 描述     |
| -------------- | -------- | ---- | ------------------------- | ---- | -------- | -------- |
| tag_id         | varchar  | 128  | 主键                      | 否   |          | 标签编号 |
| name           | varchar  | 32   | 唯一键                    | 否   |          | 标签名称 |
| tag_desc       | varchar  | 512  |                           | 是   |          | 标签描述 |
| tag_status     | char     | 1    |                           | 否   |          | 标签状态 |
| usage_count    | bigint   |      | 索引                      | 否   | 0        | 使用次数 |
| look_count     | bigint   |      | 索引                      | 否   | 0        | 查看次数 |
| download_count | bigint   |      | 索引                      | 否   | 0        | 下载次数 |
| user_id        | varchar  | 128  | 外键(u_user_info:user_id) | 否   |          | 用户     |
| create_time    | datetime |      |                           | 否   | 当前时间 | 创建时间 |
| update_time    | datetime |      |                           | 否   | 当前时间 | 更新时间 |

标签状态：0正常 1禁止 记录此标签是否可以添加，如果关闭禁止，则不可以添加，防止有一些恶意标签

删除标签时，把所有的图片的标签删除

```sql
DROP TABLE IF EXISTS p_picture_tag_info;
CREATE TABLE p_picture_tag_info (
    tag_id VARCHAR(128) NOT NULL COMMENT '标签编号',
    name VARCHAR(32) NOT NULL COMMENT '标签名称',
    tag_desc VARCHAR(512) COMMENT '标签描述',
    usage_count BIGINT NOT NULL DEFAULT 0 COMMENT '使用次数',
    look_count BIGINT NOT NULL DEFAULT 0 COMMENT '查看次数',
    download_count BIGINT NOT NULL DEFAULT 0 COMMENT '下载次数',
    user_id          VARCHAR(128) NOT NULL COMMENT '所属用户',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (tag_id),
    FOREIGN KEY (user_id) REFERENCES u_user_info (user_id),
    UNIQUE KEY uk_tag_name (name),
    INDEX idx_usage_count (usage_count),
    INDEX idx_look_count (look_count),
    INDEX idx_download_count (download_count)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图片标签信息表';
```



#### 图片信息表：p_picture_info

记录图片的基本信息，公共图片一定要审核，私有图片可以不审核

| 字段英文名     | 类型     | 长度 | 键类型                            | Null | 默认值         | 描述                |
| -------------- | -------- | ---- | --------------------------------- | ---- | -------------- | ------------------- |
| picture_id     | varchar  | 128  | 主键                              | 否   | auto_increment | 图片编号            |
| picture_url    | varchar  | 512  |                                   | 否   |                | 图片 url            |
| dns_url        | varchar  | 512  |                                   | 是   |                | 域名URL             |
| name           | varchar  | 32   |                                   | 否   |                | 图片名称            |
| introduction   | varchar  | 512  |                                   | 是   |                | 简介                |
| category_id    | varchar  | 128  | 外键(p_category_info:category_id) | 否   |                | 分类                |
| pic_size       | bigint   |      |                                   | 是   |                | 图片体积            |
| pic_width      | int      |      |                                   | 是   | 0              | 图片宽度            |
| pic_height     | int      |      |                                   | 是   | 0              | 图片高度            |
| pic_scale      | double   |      |                                   | 是   | 0              | 宽高比例            |
| pic_format     | varchar  | 32   |                                   | 是   |                | 图片格式            |
| points_need    | int      |      |                                   | 否   | 10             | 所需积分            |
| user_id        | varchar  | 128  | 外键(u_user_info:user_id)         | 否   |                | 用户                |
| create_time    | datetime |      |                                   | 否   | 当前时间       | 创建时间            |
| edit_time      | datetime |      |                                   | 是   | 当前时间       | 编辑时间            |
| update_time    | datetime |      |                                   | 是   | 当前时间       | 更新时间            |
| picture_status | char     | 1    |                                   | 否   | 0              | 图片状态            |
| review_status  | int      |      |                                   | 否   | 0              | 审核状态            |
| review_message | varchar  | 512  |                                   | 是   |                | 审核信息            |
| review_user_id | bigint   |      |                                   | 是   |                | 审核人 编号         |
| review_time    | datetime |      |                                   | 是   |                | 审核时间            |
| thumbnail_url  | varchar  | 512  |                                   | 是   |                | 缩略图 url          |
| more_info      | text     |      |                                   | 是   |                | 更多信息            |
| space_id       | varchar  | 128  | 外键（p_spece_info:space_id）     | 是   |                | 空间 编号           |
| folder_id      | varchar  | 128  |                                   | 是   |                | 文件夹              |
| is_delete      | char     | 1    |                                   | 否   | 0              | 删除(0否 1是) 默认0 |
| deleted_time   | datetime |      |                                   | 是   |                | 删除时间            |

审核状态：0待审核; 1通过; 2拒绝

图片状态：0公共 1私有

空间编号：空为公共空间

上传的图片根据自己的空间来判断，如果是公共空间则图片状态为公共，如果为私有则状态为私有

```sql
DROP TABLE IF EXISTS p_picture_info;
CREATE TABLE p_picture_info
(
    picture_id     varchar(128) COMMENT '图片编号',
    picture_url    VARCHAR(512) NOT NULL COMMENT '图片URL',
    dns_url        VARCHAR(512) NULL COMMENT '域名URL',
    name           VARCHAR(32)  NOT NULL COMMENT '图片名称',
    introduction   VARCHAR(512) COMMENT '简介',
    category_id    VARCHAR(128) NOT NULL COMMENT '分类编号',
    pic_size       BIGINT COMMENT '图片体积（字节）',
    pic_width      INT                   DEFAULT 0 COMMENT '图片宽度',
    pic_height     INT                   DEFAULT 0 COMMENT '图片高度',
    pic_scale      DOUBLE                DEFAULT 0 COMMENT '宽高比例',
    pic_format     VARCHAR(32) COMMENT '图片格式',
    points_need     INT                   DEFAULT 10 COMMENT '所需积分',
    user_id        VARCHAR(128) NOT NULL COMMENT '上传用户编号',
    create_time    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    edit_time      DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '编辑时间',
    update_time    DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    picture_status CHAR(1)      NOT NULL COMMENT '图片状态（0公共 1私有）',
    review_status  INT          NOT NULL DEFAULT 0 COMMENT '审核状态（0待审核 1通过 2拒绝）',
    review_message VARCHAR(512) COMMENT '审核信息',
    review_user_id BIGINT COMMENT '审核人编号',
    review_time    DATETIME COMMENT '审核时间',
    thumbnail_url  VARCHAR(512) COMMENT '缩略图URL',
    more_info      TEXT         COMMENT '更多信息',
    space_id       VARCHAR(128) COMMENT '所属空间编号',
    folder_id      VARCHAR(128) COMMENT '所属文件夹编号',
    is_delete      CHAR(1)      NOT NULL DEFAULT '0' COMMENT '删除（0否 1是）',
    deleted_time   DATETIME COMMENT '删除时间',
    PRIMARY KEY (picture_id),
    FOREIGN KEY (category_id) REFERENCES p_picture_category_info (category_id) ON UPDATE CASCADE,
    FOREIGN KEY (user_id) REFERENCES u_user_info (user_id) ON UPDATE CASCADE,
    FOREIGN KEY (space_id) REFERENCES p_space_info (space_id) ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_review_status (review_status),
    INDEX idx_space_folder (space_id, folder_id),
    INDEX idx_pic_color (pic_color),
    INDEX idx_picture_status (picture_status)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='图片详细信息表';
```



#### 图片标签关联表：p_picture_tag_rel_info

| 字段名     | 类型    | 长度 | 键类型                           | Null | 默认值 | 描述      |
| ---------- | ------- | ---- | -------------------------------- | ---- | ------ | --------- |
| picture_id | varchar | 128  | 外键 (p_picture_info:picture_id) | 否   |        | 图片 编号 |
| tag_id     | varchar | 128  | 外键 (p_tag_info:tag_id)         | 否   |        | 标签 编号 |

一张图片最多保存5个标签

```sql
DROP TABLE IF EXISTS p_picture_tag_rel_info;
CREATE TABLE p_picture_tag_rel_info
(
    picture_id varchar(128) NOT NULL COMMENT '图片编号',
    tag_id     VARCHAR(128) NOT NULL COMMENT '标签编号',
    PRIMARY KEY (picture_id, tag_id),
    INDEX idx_picture_id (picture_id),
    INDEX idx_tag_id (tag_id),
    CONSTRAINT fk_rel_picture
        FOREIGN KEY (picture_id)
            REFERENCES p_picture_info (picture_id)
            ON DELETE CASCADE,
    CONSTRAINT fk_rel_tag
        FOREIGN KEY (tag_id)
            REFERENCES p_picture_tag_info (tag_id)
            ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='图片标签关联表';
```



#### 图片下载记录表：p_picture_download_log

记录用户下载的日志

| 字段名               | 类型     | 长度 | 键类型                            | Null | 默认值   | 描述         |
| -------------------- | -------- | ---- | --------------------------------- | ---- | -------- | ------------ |
| download_id          | varchar  | 128  | 主键                              | 否   |          | 下载编号     |
| user_id              | varchar  | 128  | 外键 (u_user_info:user_id)        | 否   |          | 用户编号     |
| picture_id           | varchar  | 128  | 外键 (p_picture:picture_id)       | 否   |          | 图片编号     |
| space_id             | varchar  | 128  | 外键 (p_space:space_id)           | 是   |          | 空间编号     |
| category_id          | varchar  | 128  | 外键(p_category_info:category_id) | 否   |          | 图片分类     |
| tags                 | varchar  | 256  |                                   | 是   |          | 图片标签     |
| download_ip          | varchar  | 64   |                                   | 是   |          | IP 地址      |
| device_id            | varchar  | 255  |                                   | 是   |          | 设备唯一标识 |
| browser              | varchar  | 50   |                                   | 是   |          | 浏览器类型   |
| os                   | varchar  | 50   |                                   | 是   |          | 操作系统     |
| platform             | varchar  | 20   |                                   | 是   |          | 平台         |
| points_cost          | int      |      |                                   | 否   | 0        | 消耗积分     |
| is_free              | char     | 1    |                                   | 否   | 0        | 是否免费     |
| points_author_gain   | int      |      |                                   | 否   | 0        | 作者获得积分 |
| points_official_gain | int      |      |                                   | 否   | 0        | 官方获得积分 |
| points_space_gain    | int      |      |                                   | 是   | 0        | 空间获得积分 |
| proportion           | double   |      |                                   | 是   | 0        | 分成         |
| create_time          | datetime |      |                                   | 否   | 当前时间 | 下载时间     |
| download_status      | char     | 1    |                                   | 否   |          | 下载状态     |
| fail_reason          | varchar  | 255  |                                   | 是   |          | 失败原因     |
| download_type        | char     | 1    |                                   | 否   |          | 下载方式     |
| refer_source         | char     | 1    |                                   | 是   |          | 来源         |

消耗积分（0 表示免费）

是否免费（0 是 1否）

下载方式：0手动 1API 2批量

来源：0其他 1详情 2分享

下载状态：1失败 0成功

```sql
DROP TABLE IF EXISTS p_picture_download_log;
CREATE TABLE p_picture_download_log
(
    download_id          VARCHAR(128) NOT NULL COMMENT '下载编号',
    user_id              VARCHAR(128) NOT NULL COMMENT '用户编号',
    picture_id           VARCHAR(128)       NOT NULL COMMENT '图片编号',
    category_id VARCHAR(128) NOT NULL COMMENT '图片分类',
    tags        VARCHAR(256) COMMENT '图片标签（格式："标签1","标签2"）',
    space_id             VARCHAR(128) COMMENT '空间编号',
    download_ip          VARCHAR(64)  NOT NULL COMMENT '下载IP地址',
    device_id            VARCHAR(255) COMMENT '设备唯一标识',
    browser              VARCHAR(50) COMMENT '浏览器类型',
    os                   VARCHAR(50) COMMENT '操作系统',
    platform             VARCHAR(20) COMMENT '平台（Web/APP）',
    points_cost          INT          NOT NULL DEFAULT 0 COMMENT '消耗积分',
    is_free              CHAR(1)      NOT NULL DEFAULT '0' COMMENT '是否免费（0是 1否）',
    points_author_gain   INT          NOT NULL DEFAULT 0 COMMENT '作者分成积分',
    points_official_gain INT          NOT NULL DEFAULT 0 COMMENT '平台分成积分',
    points_space_gain    INT                   DEFAULT 0 COMMENT '空间分成积分',
    proportion           DOUBLE COMMENT '分成比例（如0.3表示30%）',
    create_time          DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下载时间',
    download_status      CHAR(1)      NOT NULL DEFAULT '1' COMMENT '下载状态（1失败 0成功）',
    fail_reason          VARCHAR(255) COMMENT '失败原因',
    download_type        CHAR(1)      NOT NULL COMMENT '下载方式（0手动 1API 2批量）',
    refer_source         CHAR(1) COMMENT '来源（0其他 1详情 2分享）',
    PRIMARY KEY (download_id),
    FOREIGN KEY (user_id) REFERENCES u_user_info (user_id),
    FOREIGN KEY (picture_id) REFERENCES p_picture_info (picture_id),
    FOREIGN KEY (space_id) REFERENCES p_space_info (space_id) ON DELETE SET NULL,
    INDEX idx_download_time (create_time),
    INDEX idx_download_status (download_status),
    INDEX idx_picture (picture_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='图片下载记录表';
```



#### 搜索记录：p_search_log_info

记录用户的搜索记录

| 字段名          | 类型     | 长度 | 键类型                     | Null | 默认值   | 描述         |
| --------------- | -------- | ---- | -------------------------- | ---- | -------- | ------------ |
| search_id       | varchar  | 128  | 主键                       | 否   |          | 搜索记录编号 |
| user_id         | varchar  | 128  | 外键 (u_user_info:user_id) | 是   |          | 用户编号     |
| keyword         | varchar  | 32   |                            | 否   |          | 搜索关键词   |
| search_type     | char     | 1    |                            | 否   | 0        | 搜索类型     |
| refer_source    | char     | 1    |                            | 是   | 0        | 搜索来源     |
| search_status   | char     | 1    |                            | 否   | 0        | 搜索状态     |
| fail_reason     | varchar  | 256  |                            | 是   |          | 失败原因     |
| result_count    | int      |      |                            | 否   | 0        | 返回数量     |
| create_time     | datetime |      |                            | 否   | 当前时间 | 搜索时间     |
| search_duration | int      |      |                            | 是   | 0        | 搜索时长     |
| device_id       | varchar  | 256  |                            | 是   |          | 设备唯一标识 |
| browser         | varchar  | 50   |                            | 是   |          | 浏览器类型   |
| os              | varchar  | 50   |                            | 是   |          | 操作系统     |
| platform        | varchar  | 20   |                            | 是   |          | 平台         |
| ip_address      | varchar  | 64   |                            | 是   |          | IP属地       |
| click_count     | int      |      |                            | 否   | 0        | 点击次数     |
| favorite_count  | int      |      |                            | 否   | 0        | 收藏数       |
| download_count  | int      |      |                            | 否   | 0        | 下载数       |

用户编号:如果未登录可为空

返回数量：搜索返回的数量

搜索状态：0成功 1失败

搜索类型：0图片 1空间 2用户

搜索来源：0首页 1推荐 2搜索页 3AI推荐 4历史搜索

点击次数：用户查看详情的次数

收藏数：用户点击详情如果有收藏

下载数：用户下载数量

IP属地：记录哪个省或者省同级单位的搜索，便于比如贵州省的热搜

```sql
DROP TABLE IF EXISTS p_search_log_info;
CREATE TABLE p_search_log_info (
    search_id VARCHAR(128) NOT NULL COMMENT '搜索记录编号',
    user_id VARCHAR(128) COMMENT '用户编号',
    keyword VARCHAR(32) NOT NULL COMMENT '搜索关键词',
    search_type CHAR(1) NOT NULL DEFAULT '0' COMMENT '搜索类型（0图片 1空间 2用户）',
    refer_source CHAR(1) DEFAULT '0' COMMENT '搜索来源（0首页 1推荐 2搜索页 3AI推荐 4历史搜索）',
    search_status CHAR(1) NOT NULL DEFAULT '0' COMMENT '搜索状态（0成功 1失败）',
    fail_reason VARCHAR(256) COMMENT '失败原因',
    result_count INT NOT NULL DEFAULT 0 COMMENT '返回数量',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '搜索时间',
    search_duration INT DEFAULT 0 COMMENT '搜索时长（毫秒）',
    device_id VARCHAR(256) COMMENT '设备唯一标识',
    browser VARCHAR(50) COMMENT '浏览器类型',
    os VARCHAR(50) COMMENT '操作系统',
    platform VARCHAR(20) COMMENT '平台',
    ip_address VARCHAR(64) COMMENT 'IP属地',
    click_count INT NOT NULL DEFAULT 0 COMMENT '点击次数',
    favorite_count INT NOT NULL DEFAULT 0 COMMENT '收藏数',
    download_count INT NOT NULL DEFAULT 0 COMMENT '下载数',
    PRIMARY KEY (search_id),
    INDEX idx_search_user (user_id),
    INDEX idx_search_type (search_type),
    INDEX idx_create_time (create_time),
    CONSTRAINT fk_search_log_user 
        FOREIGN KEY (user_id) 
        REFERENCES u_user_info(user_id)
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户搜索记录表';
```



#### 用户行为日志表：p_user_action_log_info

为搜索记录做日志

| 字段名      | 类型     | 长度 | 键类型                             | Null | 默认值   | 描述         |
| ----------- | -------- | ---- | ---------------------------------- | ---- | -------- | ------------ |
| action_id   | varchar  | 128  | 主键                               | 否   | 自增     | 日志编号     |
| search_id   | varchar  | 128  | 外键 (p_search_log_info:search_id) | 否   |          | 搜索记录编号 |
| user_id     | varchar  | 128  | 外键 (u_user_info:user_id)         | 是   |          | 用户编号     |
| action_type | char     | 1    |                                    | 否   |          | 行为类型     |
| target_type | char     | 1    |                                    | 否   |          | 目标类型     |
| target_id   | varchar  | 128  |                                    | 否   |          | 目标对象     |
| create_time | datetime |      |                                    | 否   | 当前时间 | 行为时间     |
| device_id   | varchar  | 255  |                                    | 是   |          | 设备唯一标识 |
| browser     | varchar  | 50   |                                    | 是   |          | 浏览器类型   |
| os          | varchar  | 50   |                                    | 是   |          | 操作系统     |
| platform    | varchar  | 20   |                                    | 是   |          | 平台         |
| ip_address  | varchar  | 64   |                                    | 是   |          | IP属地       |

行为类型：0点击 1收藏 2下载 3关注用户 4查看空间。。。

目标对象：0图片 1空间 2用户

IP属地：记录哪个省或者省同级单位的搜索，便于比如贵州省的热搜

```sql
DROP TABLE IF EXISTS p_user_action_log;
CREATE TABLE p_user_action_log_info (
    action_id VARCHAR(128) NOT NULL COMMENT '日志编号',
    search_id VARCHAR(128) COMMENT '搜索记录编号',
    user_id VARCHAR(128) COMMENT '用户编号',
    action_type CHAR(1) NOT NULL COMMENT '行为类型（0点击 1收藏 2下载 3关注用户 4查看空间）',
    target_type CHAR(1) NOT NULL COMMENT '目标类型（0图片 1空间 2用户）',
    target_id VARCHAR(128) NOT NULL COMMENT '目标对象ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '行为时间',
    device_id VARCHAR(255) COMMENT '设备唯一标识',
    browser VARCHAR(50) COMMENT '浏览器类型',
    os VARCHAR(50) COMMENT '操作系统',
    platform VARCHAR(20) COMMENT '平台',
    ip_address VARCHAR(64) COMMENT 'IP属地（如贵州省）',
    PRIMARY KEY (action_id),
    INDEX idx_user_action (user_id, action_type),
    INDEX idx_target (target_type, target_id),
    INDEX idx_create_time (create_time),
    CONSTRAINT fk_action_log_user 
        FOREIGN KEY (user_id) 
        REFERENCES u_user_info(user_id)
        ON DELETE SET NULL,
    CONSTRAINT fk_action_log_search 
        FOREIGN KEY (search_id) 
        REFERENCES p_search_log_info(search_id)
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户行为日志表';
```



#### 用户评论表：p_user_comment_info

| 字段名         | 类型     | 长度 | 键类型                            | Null | 默认值   | 描述     |
| -------------- | -------- | ---- | --------------------------------- | ---- | -------- | -------- |
| comment_id     | varchar  | 128  | 主键                              | 否   |          | 评论编号 |
| user_id        | varchar  | 128  | 外键 (u_user_info:user_id)        | 否   |          | 用户编号 |
| parent_id      | varchar  | 128  | 外键 (p_picture_comment)          | 是   |          | 父级编号 |
| target_type    | char     | 1    |                                   | 否   |          | 目标类型 |
| target_id      | varchar  | 128  |                                   | 否   |          | 目标对象 |
| target_content | varchar  | 256  |                                   | 是   |          | 目标内容 |
| category_id    | varchar  | 128  | 外键(p_category_info:category_id) | 是   |          | 图片分类 |
| space_id       | varchar  | 128  |                                   | 是   |          | 空间     |
| tags           | varchar  | 256  |                                   | 是   |          | 图片标签 |
| content        | varchar  | 256  |                                   | 是   |          | 评论内容 |
| create_time    | datetime |      |                                   | 否   | 当前时间 | 评论时间 |
| like_count     | int      |      |                                   | 否   | 0        | 点赞数   |
| ip_address     | varchar  | 64   |                                   | 是   |          | IP属地   |
| picture_url    | varchar  | 500  |                                   | 是   |          | 图片     |
| comment_status | char     | 1    |                                   | 否   |          | 正常     |

图片：评论可以上传一张图片，但是会被压缩

目标内容：评论就是父级评论内容 图片就是图片名称 空间就是空间名称

分类：存这个图片的分类，后续可以做推荐算法

标签：直接使用字符串存储，一个图片最多五个标签，存储方式："花","玫瑰花"，每个用引号包裹，，分割

评论状态：0正常 1异常

```sql
DROP TABLE IF EXISTS p_user_comment_info;
CREATE TABLE p_user_comment_info
(
    comment_id  VARCHAR(128) NOT NULL COMMENT '评论编号',
    user_id     VARCHAR(128) NOT NULL COMMENT '用户编号',
    parent_id   VARCHAR(128) COMMENT '父级评论编号',
    picture_id  varchar(128) NOT NULL COMMENT '图片编号',
    category_id VARCHAR(128) NOT NULL COMMENT '图片分类',
    tags        VARCHAR(256) COMMENT '图片标签（格式："标签1","标签2"）',
    content     VARCHAR(256) COMMENT '评论内容',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
    like_count  INT          NOT NULL DEFAULT 0 COMMENT '点赞数',
    ip_address  VARCHAR(64) COMMENT '评论者IP属地',
    picture_url VARCHAR(500) COMMENT '评论图片URL',
    comment_status CHAR(1)   COMMENT '评论状态（0正常 1异常）'
    PRIMARY KEY (comment_id),
    INDEX idx_comment_user (user_id),
    INDEX idx_comment_picture (picture_id),
    INDEX idx_comment_category (category_id),
    INDEX idx_comment_parent (parent_id),
    CONSTRAINT fk_comment_user
        FOREIGN KEY (user_id)
            REFERENCES u_user_info (user_id)
            ON DELETE CASCADE,
    CONSTRAINT fk_comment_picture
        FOREIGN KEY (picture_id)
            REFERENCES p_picture_info (picture_id)
            ON DELETE CASCADE,
    CONSTRAINT fk_comment_category
        FOREIGN KEY (category_id)
            REFERENCES p_picture_category_info (category_id),
    CONSTRAINT fk_comment_parent
        FOREIGN KEY (parent_id)
            REFERENCES p_picture_comment_info (comment_id)
            ON DELETE SET NULL,
    CONSTRAINT chk_like_count CHECK (like_count >= 0)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='图片评论表';
```

#### 评论点赞表：p_picture_comment_like_info

| 字段名       | 类型     | 长度 | 键类型                      | Null | 默认值   | 描述         |
| ------------ | -------- | ---- | --------------------------- | ---- | -------- | ------------ |
| like_id      | varchar  | 128  | 主键                        | 否   |          | 点赞记录编号 |
| user_id      | varchar  | 128  | 外键 (u_user_info:user_id)  | 否   |          | 用户编号     |
| picture_id   | varchar  | 128  | 外键 (p_picture:picture_id) | 否   |          | 图片编号     |
| comment_id   | varchar  | 128  | 外键 (p_picture_comment)    | 否   |          | 评论编号     |
| target_cover | varchar  | 512  |                             | 是   |          | 封面         |
| create_time  | datetime |      |                             | 否   | 当前时间 | 点赞时间     |

```sql
DROP TABLE IF EXISTS p_picture_comment_like_info;
CREATE TABLE p_picture_comment_like_info (
    like_id VARCHAR(128) NOT NULL COMMENT '点赞编号',
    user_id VARCHAR(128) NOT NULL COMMENT '用户编号',
    picture_id varchar(128) NOT NULL COMMENT '图片编号',
    target_cover VARCHAR(512) COMMENT '封面URL', 
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
    PRIMARY KEY (like_id),
    UNIQUE KEY uk_user_picture (user_id, picture_id),
    INDEX idx_like_user (user_id),
    INDEX idx_like_picture (picture_id),
    CONSTRAINT fk_like_user 
        FOREIGN KEY (user_id) 
        REFERENCES u_user_info(user_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_like_picture 
        FOREIGN KEY (picture_id) 
        REFERENCES p_picture(picture_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_like_category 
        FOREIGN KEY (category_id) 
        REFERENCES p_category_info(category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论点赞记录表';
```

#### 用户行为表：p_user_behavior_info

| 字段名         | 类型     | 长度 | 键类型                                    | Null | 默认值   | 描述         |
| -------------- | -------- | ---- | ----------------------------------------- | ---- | -------- | ------------ |
| behavior_id    | varchar  | 128  | 主键                                      | 否   |          | 转发编号     |
| behavior_type  | char     | 1    |                                           | 否   |          | 行为类型     |
| user_id        | varchar  | 128  | 外键 (u_user_info:user_id)                | 否   |          | 用户编号     |
| target_type    | char     | 1    |                                           | 否   |          | 目标类型     |
| target_id      | varchar  | 128  |                                           | 否   |          | 目标对象     |
| target_content | varchar  | 256  |                                           | 是   |          | 目标内容     |
| score          | decimal  | 5,2  |                                           | 否   |          | 分数         |
| share_link     | varchar  | 512  |                                           | 是   |          | 分享链接     |
| category_id    | varchar  | 128  | 外键(p_picture_category_info:category_id) | 是   |          | 图片分类     |
| space_id       | varchar  | 128  |                                           | 是   |          | 空间         |
| tags           | varchar  | 256  |                                           | 是   |          | 图片标签     |
| target_cover   | varchar  | 512  |                                           | 是   |          | 封面         |
| create_time    | datetime |      |                                           | 否   | 当前时间 | 转发时间     |
| device_id      | varchar  | 256  |                                           | 是   |          | 设备唯一标识 |
| browser        | varchar  | 50   |                                           | 是   |          | 浏览器类型   |
| os             | varchar  | 50   |                                           | 是   |          | 操作系统     |
| platform       | varchar  | 20   |                                           | 是   |          | 平台         |
| ip_address     | varchar  | 64   |                                           | 是   |          | IP属地       |

行为类型：0点赞 1收藏 2转发

目标类型：0图片 2用户 1空间

目标内容：比如说如果是空间，可以存储空间名称，如果是图片可以存储图片名称

封面：每个目标类型都有封面，图片就是本图片、用户是头像、空间是空间的封面

行为得分：比如点赞3分 收藏6分 转发12分

```sql
DROP TABLE IF EXISTS p_user_behavior_info;
CREATE TABLE `p_user_behavior_info` (
  `behavior_id` VARCHAR(128) NOT NULL COMMENT '转发编号',
  `behavior_type` CHAR(1) NOT NULL COMMENT '行为类型',
  `user_id` VARCHAR(128) NOT NULL COMMENT '用户编号',
  `target_type` CHAR(1) NOT NULL COMMENT '目标类型',
  `target_id` VARCHAR(128) NOT NULL COMMENT '目标对象',
  `target_content` VARCHAR(256) DEFAULT NULL COMMENT '目标内容',
  `score` DECIMAL(5,2) NOT NULL COMMENT '分数',
  `share_link` VARCHAR(512) DEFAULT NULL COMMENT '分享链接',
  `category_id` VARCHAR(128) DEFAULT NULL COMMENT '图片分类',
  `space_id` VARCHAR(128) DEFAULT NULL COMMENT '空间',
  `tags` VARCHAR(256) DEFAULT NULL COMMENT '图片标签',
  `target_cover` VARCHAR(512) DEFAULT NULL COMMENT '封面',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '转发时间',
  `device_id` VARCHAR(256) DEFAULT NULL COMMENT '设备唯一标识',
  `browser` VARCHAR(50) DEFAULT NULL COMMENT '浏览器类型',
  `os` VARCHAR(50) DEFAULT NULL COMMENT '操作系统',
  `platform` VARCHAR(20) DEFAULT NULL COMMENT '平台',
  `ip_address` VARCHAR(64) DEFAULT NULL COMMENT 'IP属地',
  PRIMARY KEY (`behavior_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_target` (`target_type`, `target_id`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `u_user_info` (`user_id`),
  CONSTRAINT `fk_category_id` FOREIGN KEY (`category_id`) REFERENCES `p_picture_category_info` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户行为表';
```



#### 用户浏览记录表：p_user_view_log_info

| 字段名         | 类型     | 长度 | 键类型                                    | Null | 默认值   | 描述         |
| -------------- | -------- | ---- | ----------------------------------------- | ---- | -------- | ------------ |
| view_id        | varchar  | 128  | 主键                                      | 否   |          | 浏览记录编号 |
| user_id        | varchar  | 128  | 外键 (u_user_info:user_id)                | 是   |          | 用户编号     |
| target_type    | char     | 1    |                                           | 否   |          | 目标类型     |
| target_id      | varchar  | 128  |                                           | 否   |          | 目标对象     |
| target_content | varchar  | 256  |                                           | 是   |          | 目标内容     |
| score          | decimal  | 5,2  |                                           | 否   |          | 分数         |
| category_id    | varchar  | 128  | 外键(p_picture_category_info:category_id) | 是   |          | 图片分类     |
| space_id       | varchar  | 128  |                                           | 是   |          | 空间         |
| tags           | varchar  | 256  |                                           | 是   |          | 图片标签     |
| target_cover   | varchar  | 512  |                                           | 是   |          | 封面         |
| create_time    | datetime |      |                                           | 否   | 当前时间 | 查看时间     |
| device_id      | varchar  | 256  |                                           | 是   |          | 设备唯一标识 |
| browser        | varchar  | 50   |                                           | 是   |          | 浏览器类型   |
| os             | varchar  | 50   |                                           | 是   |          | 操作系统     |
| platform       | varchar  | 20   |                                           | 是   |          | 平台         |
| ip_address     | varchar  | 64   |                                           | 是   |          | IP属地       |

目标类型：0图片 2用户 1空间

目标内容：比如说如果是空间，可以存储空间名称，如果是图片可以存储图片名称

封面：每个目标类型都有封面，图片就是本图片、用户是头像、空间是空间的封面

```sql
DROP TABLE IF EXISTS p_user_view_log_info;
CREATE TABLE p_user_view_log_info (
    view_id VARCHAR(128) NOT NULL PRIMARY KEY COMMENT '浏览记录编号',
    user_id VARCHAR(128) DEFAULT NULL COMMENT '用户编号',
    target_type CHAR(1) NOT NULL COMMENT '目标类型',
    target_id VARCHAR(128) NOT NULL COMMENT '目标对象',
    target_content VARCHAR(256) DEFAULT NULL COMMENT '目标内容',
    score DECIMAL(5, 2) NOT NULL COMMENT '分数',
    category_id VARCHAR(128) DEFAULT NULL COMMENT '图片分类',
    space_id VARCHAR(128) DEFAULT NULL COMMENT '空间',
    tags VARCHAR(256) DEFAULT NULL COMMENT '图片标签',
    target_cover VARCHAR(512) DEFAULT NULL COMMENT '封面',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '查看时间',
    device_id VARCHAR(256) DEFAULT NULL COMMENT '设备唯一标识',
    browser VARCHAR(50) DEFAULT NULL COMMENT '浏览器类型',
    os VARCHAR(50) DEFAULT NULL COMMENT '操作系统',
    platform VARCHAR(20) DEFAULT NULL COMMENT '平台',
    ip_address VARCHAR(64) DEFAULT NULL COMMENT 'IP属地',
    FOREIGN KEY (user_id) REFERENCES u_user_info(user_id),
    FOREIGN KEY (category_id) REFERENCES p_picture_category_info(category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户浏览记录表';
```



#### 用户举报表：p_user_report_info

| 字段名         | 类型     | 长度 | 键类型                     | Null | 默认值   | 描述        |
| -------------- | -------- | ---- | -------------------------- | ---- | -------- | ----------- |
| report_id      | varchar  | 128  | 主键                       | 否   |          | 举报编号    |
| user_id        | varchar  | 128  | 外键 (u_user_info:user_id) | 否   |          | 用户编号    |
| target_type    | char     | 1    |                            | 否   |          | 目标类型    |
| target_id      | bigint   |      |                            | 否   |          | 目标对象    |
| target_cover   | varchar  | 512  |                            | 是   |          | 封面        |
| reason         | varchar  | 500  |                            | 否   |          | 举报原因    |
| create_time    | datetime |      |                            | 否   | 当前时间 | 举报时间    |
| review_status  | int      |      |                            | 否   | 0        | 审核状态    |
| review_message | varchar  | 512  |                            | 是   |          | 审核信息    |
| review_user_id | bigint   |      |                            | 是   |          | 审核人 编号 |
| review_time    | datetime |      |                            | 是   |          | 审核时间    |

目标类型：0图片 1用户 2空间

封面：每个目标类型都有封面，图片就是本图片、用户是头像、空间是空间的封面，记录举报这个内容时候的快照

审核状态：0待审核; 1通过; 2拒绝

```sql
DROP TABLE IF EXISTS p_user_report_info;
CREATE TABLE p_user_report_info
(
    report_id      VARCHAR(128) NOT NULL COMMENT '举报编号',
    user_id        VARCHAR(128) NOT NULL COMMENT '用户编号',
    target_type    CHAR(1)      NOT NULL COMMENT '目标类型（0图片 1用户 2空间）',
    target_id      BIGINT       NOT NULL COMMENT '目标对象编号',
    target_cover   VARCHAR(512) COMMENT '封面快照（图片URL/用户头像URL/空间封面URL）',
    reason         VARCHAR(500) NOT NULL COMMENT '举报原因',
    create_time    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '举报时间',
    review_status  INT          NOT NULL DEFAULT 0 COMMENT '审核状态（0待审核 1通过 2拒绝）',
    review_message VARCHAR(512) COMMENT '审核信息',
    review_user_id BIGINT COMMENT '审核人编号',
    review_time    DATETIME COMMENT '审核时间',
    PRIMARY KEY (report_id),
    INDEX idx_report_user (user_id),
    INDEX idx_target (target_type, target_id),
    INDEX idx_create_time (create_time),
    CONSTRAINT fk_report_user
        FOREIGN KEY (user_id)
            REFERENCES u_user_info (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户举报信息表';
```

