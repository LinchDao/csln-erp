-- 为商品表添加零售价字段
ALTER TABLE csln_erp.product
    ADD COLUMN retail_price decimal(10, 2) DEFAULT 0.00 NULL COMMENT '零售价' AFTER wholesale_price;


CREATE TABLE csln_erp.sys_file
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

CREATE TABLE csln_erp.sys_file_business_rel
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
