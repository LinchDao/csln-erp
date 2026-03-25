-- 为商品表添加零售价字段
ALTER TABLE product
    ADD COLUMN retail_price decimal(10, 2) DEFAULT 0.00 NULL COMMENT '零售价' AFTER wholesale_price;


CREATE TABLE sys_file
(
    id                 bigint auto_increment comment '主键ID'
        primary key,
    file_name          varchar(255)                       not null comment '文件原始名称',
    storage_file_name  varchar(255)                       not null comment '服务器存储文件名（防重复，如：202603111530_8888.jpg）',
    file_path          varchar(500)                       not null comment '文件存储路径（相对路径，如：/upload/2026/03/11/）',
    full_file_path     varchar(500)                       not null comment '文件完整存储路径（绝对路径，如：/data/upload/2026/03/11/202603111530_8888.jpg）',
    file_size          bigint                             not null comment '文件大小（字节）',
    file_type          int                                not null comment '文件大类（1-图片，2-其他，枚举FileCategoryEnum）',
    file_extension     varchar(20)                        not null comment '文件扩展名（小写，如jpg/png/pdf）',
    content_type       varchar(100)                       not null comment '文件MIME类型（如image/jpeg/application/pdf）',
    create_time        datetime default CURRENT_TIMESTAMP null comment '创建时间',
    create_user        bigint                             null comment '创建人ID',
    is_delete          tinyint  default 0                 null comment '是否删除（0-未删，1-已删）'
)
    comment '文件核心信息表（存储文件基础信息，与业务解耦）' charset = utf8mb4;

CREATE TABLE sys_file_business_rel
(
    id             bigint auto_increment comment '主键ID'
        primary key,
    file_id        bigint                             not null comment '关联文件ID（关联sys_file.id）',
    business_type  varchar(50)                        not null comment '业务类型（枚举BusinessTypeEnum，如product_main_image）',
    business_id    bigint                             not null comment '关联业务主键ID（如商品ID/订单ID）',
    sort           int         default 0              null comment '排序号（同业务下文件排序）',
    create_time    datetime default CURRENT_TIMESTAMP null comment '创建时间',
    is_delete      tinyint  default 0                 null comment '是否删除（0-未删，1-已删）',
    constraint idx_file_business
        unique (file_id, business_type, business_id)  -- 唯一索引，避免重复关联
)
    comment '文件-业务关联表（解耦文件与业务，支持一对多关联）' charset = utf8mb4;

CREATE TABLE sys_dict (
                          id          CHAR(32)    NOT NULL COMMENT '字典主键',
                          parent_id   CHAR(32)    NOT NULL DEFAULT '0' COMMENT '父级ID，顶级节点为0',
                          dict_name   VARCHAR(50) NOT NULL COMMENT '字典名称/标签',
                          dict_value  VARCHAR(255) DEFAULT '' COMMENT '字典值（存储实际使用的编码/值）',
                          sort        INT(11)     NOT NULL DEFAULT 0 COMMENT '排序号，数字越小越靠前',
                          status      TINYINT(1)  NOT NULL DEFAULT 1 COMMENT '状态：1-启用 0-禁用',
                          is_delete   TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '删除标识：0-未删除 1-已删除',
                          remark      VARCHAR(255) DEFAULT '' COMMENT '备注说明',
                          create_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          update_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          PRIMARY KEY (id) USING BTREE,
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '系统数据字典表';


-- 1. 删除旧的唯一索引
ALTER TABLE product_sku DROP INDEX uk_sku;

-- 2. 删除 color_id、size_id
ALTER TABLE product_sku
DROP COLUMN color_id,
    DROP COLUMN size_id;

-- 3. 增加颜色名称、尺码名称
ALTER TABLE product_sku
    ADD COLUMN color_name VARCHAR(50) NOT NULL COMMENT '颜色名称' AFTER product_id,
    ADD COLUMN size_name  VARCHAR(50) NOT NULL COMMENT '尺码名称' AFTER color_name;

-- 1. 删除旧唯一索引
ALTER TABLE product_color_image DROP INDEX uk_prod_color;

-- 2. 删除 color_id
ALTER TABLE product_color_image DROP COLUMN color_id;

-- 3. 增加 color_name
ALTER TABLE product_color_image
    ADD COLUMN color_name VARCHAR(50) NOT NULL COMMENT '颜色名称' AFTER product_id;

