-- 为商品表添加零售价字段
ALTER TABLE csln_erp.product
    ADD COLUMN retail_price decimal(10, 2) DEFAULT 0.00 NULL COMMENT '零售价' AFTER wholesale_price;


CREATE TABLE csln_erp.file
(
    id              bigint AUTO_INCREMENT COMMENT '主键ID'
        PRIMARY KEY,
    file_name       varchar(255) NOT NULL COMMENT '文件原始名称',
    file_path       varchar(500) NOT NULL COMMENT '文件存储路径（相对/绝对路径）',
    file_size       bigint       NOT NULL COMMENT '文件大小（字节）',
    file_type       varchar(50)  NOT NULL COMMENT '文件类型（如jpg/png/pdf）',
    file_extension  varchar(20)  NOT NULL COMMENT '文件扩展名（如jpg/png）',
    business_type   varchar(50)  NULL COMMENT '业务类型（如product_main_image/product_color_image）',
    business_id     bigint       NULL COMMENT '关联业务主键ID',
    create_time     datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_user     bigint       NULL COMMENT '创建人ID',
    is_delete       tinyint DEFAULT 0 COMMENT '是否删除（0-未删，1-已删）'
)
    COMMENT '通用文件表（存储图片/文档等文件信息）'
    CHARSET = utf8mb4;


-- 1. 修改商品表：image字段改为main_file_id（关联文件表ID）
ALTER TABLE csln_erp.product
DROP COLUMN image,
    ADD COLUMN main_file_id bigint NULL COMMENT '商品主图文件ID（关联file表）' AFTER retail_price;

-- 2. 修改商品颜色图片表：image字段改为color_file_id（关联文件表ID）
ALTER TABLE csln_erp.product_color_image
DROP COLUMN image,
    ADD COLUMN color_file_id bigint NULL COMMENT '颜色图片文件ID（关联file表）' AFTER color_id;