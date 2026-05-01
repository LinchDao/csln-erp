create table after_sale
(
    id            char(32)                           not null comment '主键'
        primary key,
    as_no         varchar(50)                        not null comment '退换货单号',
    order_id      char(32)                           not null comment '原母单ID',
    order_no      varchar(50)                        not null comment '原订单号',
    customer_id   char(32)                           not null comment '客户ID',
    shop_id       char(32)                           not null comment '门店ID',
    sales_user_id char(32)                           not null comment '制单销售',
    as_type       tinyint                            not null comment '1仅退货 2仅换货 3退+换',
    status        tinyint  default 0                 not null comment '0草稿 1待审核 2已审核 3待收货 4已收货 5已完成 6已取消',
    reason        varchar(255)                       null comment '原因',
    remark        varchar(255)                       null comment '备注',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    constraint uk_as_no
        unique (as_no)
)
    comment '退换货主表' charset = utf8mb4;

create table after_sale_item
(
    id                  char(32)                           not null comment '主键'
        primary key,
    as_id               char(32)                           not null comment '退换货单ID',
    order_item_id       char(32)                           not null comment '原订单明细ID',
    product_id          char(32)                           not null comment '商品ID',
    color_id            char(32)                           not null comment '颜色ID',
    real_sku_id         char(32)                           not null comment '原真实SKU（参考）',
    ship_sku_id         char(32)                           not null comment '原发货SKU',
    qty                 int                                not null comment '退货/换货数量',
    op_type             tinyint                            not null comment '1退货 2换货',
    new_sku_id          char(32)                           null comment '换货目标SKU',
    new_qty             int      default 0                 null comment '换货发出数量',
    warehouse_id        char(32)                           not null comment '仓库ID',
    confirm_real_sku_id char(32)                           null comment '仓管确认的真实SKU（最终入库用）',
    create_time         datetime default CURRENT_TIMESTAMP null comment '创建时间'
)
    comment '退换货明细' charset = utf8mb4;

create table color
(
    id   char(32)      not null comment '主键'
        primary key,
    name varchar(50)   not null comment '颜色名称',
    code varchar(50)   null comment '颜色编码',
    sort int default 0 null comment '排序'
)
    comment '颜色表' charset = utf8mb4;

create table customer
(
    id          char(32)                           not null comment '主键'
        primary key,
    name        varchar(100)                       not null comment '客户名称',
    phone       varchar(20)                        null comment '电话',
    level_id    char(32)                           null comment '等级ID',
    address     varchar(255)                       null comment '地址',
    remark      varchar(255)                       null comment '备注',
    status      tinyint  default 1                 null comment '状态',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间'
)
    comment '客户表' charset = utf8mb4;

create table customer_level
(
    id         char(32)                    not null comment '主键'
        primary key,
    level_name varchar(50)                 not null comment '等级名称',
    discount   decimal(10, 2) default 1.00 null comment '折扣率',
    remark     varchar(255)                null comment '备注'
)
    comment '客户等级表' charset = utf8mb4;

create table delivery_note
(
    id               char(32)     not null comment '主键'
        primary key,
    delivery_no      varchar(50)  not null comment '发货单号',
    sub_order_id     char(32)     not null comment '子单ID',
    delivery_type    tinyint      null comment '配送方式',
    express_no       varchar(100) null comment '快递单号',
    driver_phone     varchar(20)  null comment '司机/自提手机号',
    remark           varchar(255) null comment '备注',
    actual_send_time datetime     null comment '发货时间',
    send_user_id     char(32)     null comment '发货人',
    constraint uk_delivery_no
        unique (delivery_no)
)
    comment '发货单' charset = utf8mb4;

create table order_change_log
(
    id              char(32)                           not null comment '主键'
        primary key,
    master_id       char(32)                           not null comment '母单ID',
    sub_id          char(32)                           null comment '子单ID',
    before_content  text                               null comment '修改前',
    after_content   text                               null comment '修改后',
    apply_user_id   char(32)                           null comment '申请人',
    approve_user_id char(32)                           null comment '审批人',
    status          tinyint  default 0                 null comment '0待审批 1通过 2拒绝',
    create_time     datetime default CURRENT_TIMESTAMP null comment '操作时间'
)
    comment '订单修改日志' charset = utf8mb4;

create table order_help_send
(
    id           char(32)          not null comment '主键'
        primary key,
    order_sub_id char(32)          not null comment '子单ID',
    from_ware_id char(32)          not null comment '代发仓库',
    status       tinyint default 0 null comment '状态'
)
    comment '协发单' charset = utf8mb4;

create table order_item
(
    id         char(32)       not null comment '主键'
        primary key,
    master_id  char(32)       not null comment '母单ID',
    sub_id     char(32)       not null comment '子单ID',
    sku_id     char(32)       not null comment 'SKU_ID',
    sku_spec_snapshot text    null comment 'SKU规格快照JSON',
    qty        int            not null comment '下单数量',
    price      decimal(10, 2) not null comment '单价',
    amount     decimal(12, 2) not null comment '金额',
    actual_qty int default 0  null comment '实际发货数量'
)
    comment '订单明细表' charset = utf8mb4;

create table order_master
(
    id             char(32)                                 not null comment '主键'
        primary key,
    order_no       varchar(50)                              not null comment '订单号',
    shop_id        char(32)                                 not null comment '门店ID',
    customer_id    char(32)                                 not null comment '客户ID',
    total_qty      int            default 0                 not null comment '总数量',
    total_amount   decimal(12, 2) default 0.00              not null comment '总金额',
    order_type     tinyint                                  not null comment '1现货 2预售 3分批发货',
    price_from     tinyint                                  null comment '1专属 2等级 3上次 4手动',
    audit_status   tinyint        default 0                 null comment '0无需 1待审核 2通过 3拒绝',
    audit_user_id  char(32)                                 null comment '审核人',
    audit_time     datetime                                 null comment '审核时间',
    sales_user_id  char(32)                                 not null comment '业绩归属销售ID',
    create_time    datetime       default CURRENT_TIMESTAMP null comment '开单时间',
    status         tinyint                                  not null comment '订单状态',
    create_user_id char(32)                                 not null comment '开单人',
    remark         varchar(255)                             null comment '备注',
    is_ar          tinyint        default 0                 null comment '是否生成应收 0否 1是',
    allow_replace  tinyint(1)     default 0                 not null comment '是否允许改码凑单 0不允许 1允许',
    is_draft       tinyint        default 0                 not null comment '是否草稿 0正式单 1草稿单',
    constraint uk_order_no
        unique (order_no)
)
    comment '订单主表' charset = utf8mb4;

create table order_replace_detail
(
    id            char(32)                           not null comment '主键'
        primary key,
    order_item_id char(32)                           not null comment '订单明细ID',
    product_id    char(32)                           not null comment '商品ID',
    color_id      char(32)                           not null comment '颜色ID',
    target_sku_id char(32)                           not null comment '客户要的SKU（目标码）',
    real_sku_id   char(32)                           not null comment '实际发货SKU（真实库存码）',
    qty           int      default 0                 not null comment '数量',
    warehouse_id  char(32)                           not null comment '仓库ID',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间'
)
    comment '订单改码配货明细表' charset = utf8mb4;

create index idx_order_item
    on order_replace_detail (order_item_id);

create table order_sub
(
    id                char(32)                    not null comment '主键'
        primary key,
    sub_order_no      varchar(50)                 not null comment '子订单号',
    master_id         char(32)                    not null comment '母单ID',
    warehouse_id      char(32)                    not null comment '发货仓库',
    amount            decimal(12, 2) default 0.00 not null comment '子单金额',
    send_type         tinyint        default 1    null comment '1本仓 2协发 3调货',
    from_warehouse_id char(32)                    null comment '来源仓',
    status            tinyint                     not null comment '子单状态',
    delivery_type     tinyint                     null comment '配送方式',
    express_no        varchar(100)                null comment '快递单号',
    driver_phone      varchar(20)                 null comment '司机/自提手机号',
    delivery_remark   varchar(255)                null comment '配送备注',
    expect_send_date  date                        null comment '期望发货日期',
    actual_send_date  date                        null comment '实际发货日期',
    picker_user_id    char(32)                    null comment '配货员',
    remark            varchar(255)                null comment '备注',
    is_delete         int            default 0    not null comment '是否删除 0-未删除 1-已删除',
    constraint uk_sub_order_no
        unique (sub_order_no)
)
    comment '子订单表' charset = utf8mb4;

create table payment
(
    id             char(32)                           not null comment '主键'
        primary key,
    payment_no     varchar(50)                        not null comment '收款单号',
    order_no       varchar(50)                        not null comment '订单号',
    customer_id    char(32)                           not null comment '客户ID',
    amount         decimal(12, 2)                     not null comment '收款金额',
    pay_type       varchar(50)                        null comment '支付方式',
    remark         varchar(255)                       null comment '备注',
    create_user_id char(32)                           null comment '操作人',
    create_time    datetime default CURRENT_TIMESTAMP null comment '收款时间',
    constraint uk_payment_no
        unique (payment_no)
)
    comment '收款单' charset = utf8mb4;

create table price_customer
(
    id          char(32)       not null comment '主键'
        primary key,
    product_id  char(32)       not null comment '商品ID',
    customer_id char(32)       not null comment '客户ID',
    price       decimal(10, 2) not null comment '价格',
    constraint uk_prod_cust
        unique (product_id, customer_id)
)
    comment '客户专属价格表' charset = utf8mb4;

create table price_last
(
    id          char(32)                           not null comment '主键'
        primary key,
    product_id  char(32)                           not null comment '商品ID',
    customer_id char(32)                           not null comment '客户ID',
    last_price  decimal(10, 2)                     not null comment '最近成交价',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_prod_cust_last
        unique (product_id, customer_id)
)
    comment '最近成交价表' charset = utf8mb4;

create table price_level
(
    id         char(32)       not null comment '主键'
        primary key,
    product_id char(32)       not null comment '商品ID',
    level_id   char(32)       not null comment '等级ID',
    price      decimal(10, 2) not null comment '价格',
    constraint uk_prod_level
        unique (product_id, level_id)
)
    comment '等级价格表' charset = utf8mb4;

create table product
(
    id              char(32)                                 not null comment '主键'
        primary key,
    product_no      varchar(50)                              not null comment '款号',
    name            varchar(255)                             not null comment '商品名称',
    brand           varchar(100)                             null comment '品牌',
    season          varchar(50)                              null comment '季节',
    year            varchar(20)                              null comment '年份',
    series          varchar(100)                             null comment '系列',
    cost_price      decimal(10, 2) default 0.00              null comment '成本价',
    wholesale_price decimal(10, 2) default 0.00              null comment '默认批发价',
    is_delete       int            default 0                 not null comment '1-已删除 0-存在',
    retail_price    decimal(10, 2) default 0.00              null comment '零售价',
    main_image_id   char(32)                                 null comment '商品主图文件ID（关联file表）',
    status          tinyint        default 1                 null comment '状态',
    create_time     datetime       default CURRENT_TIMESTAMP null comment '创建时间',
    constraint uk_product_no
        unique (product_no)
)
    comment '商品表' charset = utf8mb4;

create table product_color_image
(
    id            char(32)                           not null comment '主键'
        primary key,
    product_id    char(32)                           not null comment '商品ID',
    color_name    varchar(50)                        not null comment '颜色名称',
    color_file_id char(32)                           null comment '颜色图片文件ID（关联file表）',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    is_delete     int      default 0                 not null
)
    comment '商品颜色图片表' charset = utf8mb4;

create table product_dim_image
(
    id          char(32)                           not null comment '主键'
        primary key,
    product_id  char(32)                           not null comment '商品ID',
    dim_key     varchar(50)                        not null comment '维度键',
    dim_value   varchar(100)                       not null comment '维度值',
    file_id     char(32)                           not null comment '图片文件ID',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    is_delete   int      default 0                 not null comment '删除标识'
)
    comment '商品维度值图片表' charset = utf8mb4;

create table product_dim_image_config
(
    id          char(32)                           not null comment '主键'
        primary key,
    product_id  char(32)                           not null comment '商品ID',
    dim_key     varchar(50)                        not null comment '维度键',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    is_delete   int      default 0                 not null comment '删除标识'
)
    comment '商品可挂图维度配置表' charset = utf8mb4;

create table product_sku
(
    id                  char(32)      not null comment '主键'
        primary key,
    product_id          char(32)      null comment '商品ID',
    product_v2_id       char(32)      null comment 'V2商品ID',
    color_name          varchar(50)   null comment '颜色名称',
    size_name           varchar(50)   null comment '尺码名称',
    barcode             varchar(100)  null comment '条码',
    dimension_signature varchar(500)  null comment '维度组合签名',
    is_delete           int default 0 not null
)
    comment 'SKU表' charset = utf8mb4;

create table product_sku_dim
(
    id        char(32)      not null comment '主键'
        primary key,
    sku_id    char(32)      not null comment 'SKU ID',
    dim_key   varchar(50)   not null comment '维度键',
    dim_name  varchar(50)   not null comment '维度名',
    dim_value varchar(100)  not null comment '维度值',
    dim_order int           not null comment '维度顺序',
    is_delete int default 0 not null comment '删除标识'
)
    comment 'SKU维度明细表' charset = utf8mb4;

create table product_v2
(
    id                char(32)                                 not null comment '主键'
        primary key,
    product_no        varchar(50)                              not null comment '商品编码',
    name              varchar(255)                             not null comment '商品名称',
    category_id       char(32)                                 null comment '类目ID',
    brand             varchar(100)                             null comment '品牌',
    status            tinyint        default 1                 null comment '状态',
    cost_price        decimal(10, 2) default 0.00              null comment '成本价',
    wholesale_price   decimal(10, 2) default 0.00              null comment '批发价',
    retail_price      decimal(10, 2) default 0.00              null comment '零售价',
    main_image_id     char(32)                                 null comment '主图文件ID',
    sale_attrs_schema text                                     null comment '销售属性定义JSON',
    ext_json          text                                     null comment '扩展信息JSON',
    create_time       datetime       default CURRENT_TIMESTAMP null comment '创建时间',
    update_time       datetime       default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete         int            default 0                 not null comment '删除标识',
    constraint uk_product_v2_no
        unique (product_no)
)
    comment 'V2商品主表' charset = utf8mb4;

create table product_v2_attr
(
    id            char(32)                           not null comment '主键'
        primary key,
    product_v2_id char(32)                           not null comment 'V2商品ID',
    attr_key      varchar(100)                       not null comment '属性键',
    attr_name     varchar(100)                       not null comment '属性名',
    attr_value    varchar(500)                       null comment '属性值',
    value_type    varchar(30)                        null comment '值类型',
    sort          int      default 0                 not null comment '排序',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete     int      default 0                 not null comment '删除标识'
)
    comment 'V2商品属性表' charset = utf8mb4;

create table purchase_in
(
    id             char(32)                           not null comment '主键'
        primary key,
    in_no          varchar(50)                        not null comment '入库单号',
    purchase_id    char(32)                           null comment '来源采购单',
    warehouse_id   char(32)                           not null comment '入库仓库',
    total_qty      int      default 0                 not null comment '入库总数量',
    status         tinyint  default 0                 null comment '0待审核 1已审核',
    create_user_id char(32)                           null comment '操作人',
    audit_user_id  char(32)                           null comment '审核人ID',
    create_time    datetime default CURRENT_TIMESTAMP null comment '入库时间',
    remark         varchar(255)                       null comment '备注',
    constraint uk_in_no
        unique (in_no)
)
    comment '采购入库单' charset = utf8mb4;

create table purchase_in_item
(
    id     char(32) not null comment '主键'
        primary key,
    in_id  char(32) not null comment '入库单ID',
    sku_id char(32) not null comment 'SKU_ID',
    sku_spec_snapshot text null comment 'SKU规格快照JSON',
    qty    int      not null comment '入库数量'
)
    comment '入库明细表' charset = utf8mb4;

create table purchase_order
(
    id             char(32)                                 not null comment '主键'
        primary key,
    purchase_no    varchar(50)                              not null comment '采购单号',
    supplier_id    char(32)                                 not null comment '供应商ID',
    total_qty      int            default 0                 null comment '总数量',
    total_amount   decimal(12, 2) default 0.00              null comment '总金额',
    status         tinyint        default 0                 null comment '0待入库 1部分入库 2已完成 3取消',
    create_user_id char(32)                                 null comment '制单人',
    order_time     datetime                                 null comment '下单时间',
    arrival_time   datetime                                 null comment '到货时间',
    create_time    datetime       default CURRENT_TIMESTAMP null comment '下单时间',
    remark         varchar(255)                             null comment '备注',
    is_delete      int            default 0                 not null,
    constraint uk_purchase_no
        unique (purchase_no)
)
    comment '采购订单' charset = utf8mb4;

create table purchase_order_item
(
    id          char(32)       not null comment '主键'
        primary key,
    purchase_id char(32)       not null comment '采购单ID',
    sku_id      char(32)       not null comment 'SKU_ID',
    sku_spec_snapshot json     default null comment 'SKU规格快照JSON',
    qty         int            not null comment '采购数量',
    price       decimal(10, 2) not null comment '单价',
    amount      decimal(12, 2) not null comment '金额',
    is_delete   int default 0  not null
)
    comment '采购明细表' charset = utf8mb4;

create table purchase_return
(
    id           char(32)                           not null comment '主键'
        primary key,
    return_no    varchar(50)                        not null comment '退货单号',
    purchase_id  char(32)                           null comment '采购单ID',
    supplier_id  char(32)                           not null comment '供应商ID',
    warehouse_id char(32)                           not null comment '退货仓库',
    status       tinyint  default 0                 null comment '0待审核 1已审核',
    create_time  datetime default CURRENT_TIMESTAMP null comment '退货时间',
    constraint uk_return_no
        unique (return_no)
)
    comment '采购退货单' charset = utf8mb4;

create table purchase_return_item
(
    id        char(32)       not null comment '主键'
        primary key,
    return_id char(32)       not null comment '退货单ID',
    sku_id    char(32)       not null comment 'SKU_ID',
    qty       int            not null comment '退货数量',
    price     decimal(10, 2) not null comment '单价',
    amount    decimal(12, 2) not null comment '金额'
)
    comment '退货明细表' charset = utf8mb4;

create table sales_statistics
(
    id            char(32)                    not null comment '主键'
        primary key,
    stat_date     date                        not null comment '统计日期',
    stat_type     varchar(20)                 not null comment 'day/week/month',
    shop_id       char(32)                    not null comment '门店ID',
    sales_user_id char(32)                    not null comment '销售ID',
    order_count   int            default 0    null comment '订单数',
    total_qty     int            default 0    null comment '总数量',
    total_amount  decimal(12, 2) default 0.00 null comment '总金额',
    cost_amount   decimal(12, 2) default 0.00 null comment '总成本',
    profit_amount decimal(12, 2) default 0.00 null comment '毛利',
    constraint uk_stat
        unique (stat_date, stat_type, shop_id, sales_user_id)
)
    comment '销售业绩统计表' charset = utf8mb4;

create table shop
(
    id                   char(32)                           not null comment '主键'
        primary key,
    shop_name            varchar(100)                       not null comment '门店名称',
    manager_user_id      char(32)                           null comment '店长ID',
    default_warehouse_id char(32)                           null comment '默认送单仓',
    status               tinyint  default 1                 not null comment '状态',
    create_time          datetime default CURRENT_TIMESTAMP null comment '创建时间',
    constraint uk_manager_user
        unique (manager_user_id)
)
    comment '门店表' charset = utf8mb4;

create table size
(
    id   char(32)      not null comment '主键'
        primary key,
    name varchar(50)   not null comment '尺码名称',
    sort int default 0 null comment '排序'
)
    comment '尺码表' charset = utf8mb4;

create table stock
(
    id           char(32)                           not null comment '主键'
        primary key,
    warehouse_id char(32)                           not null comment '仓库ID',
    sku_id       char(32)                           not null comment 'SKU_ID',
    qty          int      default 0                 not null comment '可用库存',
    lock_qty     int      default 0                 not null comment '锁定库存',
    recovery_qty int      default 0                 not null comment '追回货预占',
    warn_qty     int      default 0                 null comment '预警数量',
    update_time  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_ware_sku
        unique (warehouse_id, sku_id)
)
    comment '库存表' charset = utf8mb4;

create table stock_lock_log
(
    id           char(32)                           not null comment '主键'
        primary key,
    warehouse_id char(32)                           not null comment '仓库ID',
    sku_id       char(32)                           not null comment 'SKU_ID',
    order_no     varchar(50)                        not null comment '订单号',
    qty          int                                not null comment '数量',
    type         tinyint                            not null comment '1锁定 2释放',
    create_time  datetime default CURRENT_TIMESTAMP null comment '操作时间'
)
    comment '库存锁定日志表' charset = utf8mb4;

create table stock_recovery
(
    id                char(32)          not null comment '主键'
        primary key,
    sku_id            char(32)          not null comment 'SKU_ID',
    warehouse_id      char(32)          not null comment '仓库ID',
    qty               int               not null comment '数量',
    type              tinyint           not null comment '1待换货 2退货未入仓 3作废未追回',
    source_order_no   varchar(50)       null comment '来源单号',
    status            tinyint default 0 null comment '0未占用 1已占用',
    used_sub_order_id char(32)          null comment '占用子单ID',
    used_time         datetime          null comment '占用时间'
)
    comment '追回货表' charset = utf8mb4;

create table stock_transfer
(
    id             char(32)                           not null comment '主键'
        primary key,
    transfer_no    varchar(50)                        not null comment '调货单号',
    from_ware_id   char(32)                           not null comment '调出仓',
    to_ware_id     char(32)                           not null comment '调入仓',
    status         tinyint  default 0                 null comment '0待发货 1已发货 2已收货 3取消',
    remark         varchar(255)                       null comment '备注',
    create_user_id char(32)                           null comment '创建人',
    create_time    datetime default CURRENT_TIMESTAMP null comment '创建时间',
    constraint uk_transfer_no
        unique (transfer_no)
)
    comment '调货单' charset = utf8mb4;

create table stock_transfer_item
(
    id          char(32)          not null comment '主键'
        primary key,
    transfer_id char(32)          not null comment '调货单ID',
    sku_id      char(32)          not null comment 'SKU_ID',
    qty         int     default 0 null comment '申请数量',
    send_qty    int     default 0 null comment '发出数量',
    receive_qty int     default 0 null comment '收货数量',
    diff_qty    int     default 0 null comment '差异数量',
    diff_type   tinyint default 0 null comment '0无 1少货 2破损 3多货',
    diff_remark varchar(255)      null comment '差异备注'
)
    comment '调货明细表' charset = utf8mb4;

create table supplier
(
    id          char(32)                           not null comment '主键'
        primary key,
    name        varchar(100)                       not null comment '供应商名称',
    phone       varchar(20)                        null comment '电话',
    address     varchar(255)                       null comment '地址',
    remark      varchar(255)                       null comment '备注',
    status      tinyint  default 1                 null comment '状态',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间'
)
    comment '供应商表' charset = utf8mb4;

create table sys_dict
(
    id          char(32)                               not null comment '字典主键',
    parent_id   char(32)     default '0'               not null comment '父级ID，顶级节点为0',
    dict_name   varchar(50)                            not null comment '字典名称/标签',
    dict_value  varchar(255) default ''                null comment '字典值（存储实际使用的编码/值）',
    sort        int          default 0                 not null comment '排序号，数字越小越靠前',
    status      tinyint(1)   default 1                 not null comment '状态：1-启用 0-禁用',
    is_delete   tinyint(1)   default 0                 not null comment '删除标识：0-未删除 1-已删除',
    remark      varchar(255) default ''                null comment '备注说明',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '系统数据字典表' charset = utf8mb4;

create index sys_dict_parent_id_index
    on sys_dict (parent_id);

create table sys_file
(
    id                char(32)                           not null comment '主键ID'
        primary key,
    file_name         varchar(255)                       not null comment '文件原始名称',
    storage_file_name varchar(255)                       not null comment '服务器存储文件名（防重复，如：202603111530_8888.jpg）',
    file_path         varchar(500)                       not null comment '文件存储路径（相对路径，如：/upload/2026/03/11/）',
    full_file_path    varchar(500)                       not null comment '文件完整存储路径（绝对路径，如：/data/upload/2026/03/11/202603111530_8888.jpg）',
    file_size         bigint                             not null comment '文件大小（字节）',
    file_extension    varchar(20)                        not null comment '文件扩展名（小写，如jpg/png/pdf）',
    content_type      varchar(100)                       not null comment '文件MIME类型（如image/jpeg/application/pdf）',
    create_time       datetime default CURRENT_TIMESTAMP null comment '创建时间',
    create_user       char(32)                           null comment '创建人ID',
    is_delete         tinyint  default 0                 null comment '是否删除（0-未删，1-已删）'
)
    comment '文件核心信息表（存储文件基础信息，与业务解耦）' charset = utf8mb4;

create table sys_file_business_rel
(
    id            char(32)                           not null comment '主键ID'
        primary key,
    file_id       char(32)                           not null comment '关联文件ID（关联sys_file.id）',
    business_type varchar(50)                        not null comment '业务类型（枚举BusinessTypeEnum，如product_main_image）',
    business_id   char(32)                           not null comment '关联业务主键ID（如商品ID/订单ID）',
    sort          int      default 0                 null comment '排序号（同业务下文件排序）',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    is_delete     tinyint  default 0                 null comment '是否删除（0-未删，1-已删）',
    constraint idx_file_business
        unique (file_id, business_type, business_id)
)
    comment '文件-业务关联表（解耦文件与业务，支持一对多关联）' charset = utf8mb4;

create table sys_log
(
    id             varchar(32)                          not null comment '主键(雪花算法ID)'
        primary key,
    biz_id         varchar(32)                          null comment '业务主键(如订单号、商品ID，极速查单据流水)',
    module         varchar(50)                          not null comment '所属模块(如: 订单中心, 系统设置)',
    action_type    varchar(50)                          null comment '动作类型(如: INSERT, UPDATE, DELETE, EXPORT)',
    diff_data      json                                 null comment '数据快照(只存差异)',
    user_id        varchar(64)                          null comment '操作人ID',
    request_url    varchar(255)                         null comment '请求接口URL',
    request_method varchar(10)                          null comment '请求方式(GET, POST, PUT, DELETE)',
    status         tinyint(1) default 1                 not null comment '执行状态: 0-失败, 1-成功',
    error_msg      varchar(1000)                        null comment '异常信息简述(执行失败时记录)',
    cost_time      int                                  null comment '接口执行耗时(毫秒，用于监控慢请求)',
    create_time    datetime   default CURRENT_TIMESTAMP not null comment '操作时间'
)
    comment '系统操作审计日志表' charset = utf8mb4;

create table sys_menu
(
    id          char(32)                           not null comment '主键'
        primary key,
    parent_id   char(32)                           null comment '父菜单ID，顶级菜单为null',
    menu_type   tinyint  default 1                 not null comment '菜单类型 1目录 2菜单 3按钮',
    name        varchar(100)                       not null comment '路由名称',
    path        varchar(200)                       null comment '路由路径',
    component   varchar(200)                       null comment '组件路径',
    redirect    varchar(200)                       null comment '重定向路径',
    permission  varchar(100)                       null comment '权限标识',
    title       varchar(100)                       not null comment '菜单标题',
    icon        varchar(100)                       null comment '菜单图标',
    no_cache    tinyint  default 0                 null comment '是否缓存 0缓存 1不缓存',
    breadcrumb  tinyint  default 1                 null comment '是否显示面包屑 0不显示 1显示',
    affix       tinyint  default 0                 null comment '是否固定 0不固定 1固定',
    hidden      tinyint  default 0                 not null comment '是否隐藏 0显示 1隐藏',
    always_show tinyint  default 0                 null comment '是否总是显示 0否 1是',
    sort        int      default 0                 not null comment '排序号',
    status      tinyint  default 1                 not null comment '状态 1正常 0禁用',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   int      default 0                 not null,
    constraint uk_menu_name
        unique (name)
)
    comment '菜单表' charset = utf8mb4;

create table sys_permission
(
    id        char(32)     not null comment '主键'
        primary key,
    perm_code varchar(100) not null comment '权限编码',
    perm_name varchar(100) not null comment '权限名称',
    constraint uk_perm_code
        unique (perm_code)
)
    comment '权限表' charset = utf8mb4;

create table sys_role
(
    id        char(32)     not null comment '主键'
        primary key,
    role_code varchar(50)  not null comment '角色编码',
    role_name varchar(50)  not null comment '角色名称',
    remark    varchar(255) null comment '备注',
    constraint uk_role_code
        unique (role_code)
)
    comment '角色表' charset = utf8mb4;

create table sys_role_menu
(
    id          char(32)                           not null comment '主键'
        primary key,
    role_id     char(32)                           not null comment '角色ID',
    menu_id     char(32)                           not null comment '菜单ID',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间'
)
    comment '角色菜单关联表' charset = utf8mb4;

create table sys_role_permission
(
    id      char(32) not null comment '主键'
        primary key,
    role_id char(32) not null comment '角色ID',
    perm_id char(32) not null comment '权限ID'
)
    comment '角色权限表' charset = utf8mb4;

create table sys_user
(
    id           char(32)                           not null comment '主键'
        primary key,
    username     varchar(50)                        not null comment '登录账号',
    password     varchar(100)                       not null comment '密码',
    real_name    varchar(50)                        null comment '姓名',
    phone        varchar(20)                        null comment '电话',
    shop_id      char(32)                           null comment '所属门店ID',
    warehouse_id char(32)                           null comment '所属仓库ID',
    status       tinyint  default 1                 not null comment '状态 1正常 0禁用',
    create_time  datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '用户表' charset = utf8mb4;

create table sys_user_role
(
    id      char(32) not null comment '主键'
        primary key,
    user_id char(32) not null comment '用户ID',
    role_id char(32) not null comment '角色ID'
)
    comment '用户角色关联表' charset = utf8mb4;

create table warehouse
(
    id              char(32)                           not null comment '主键'
        primary key,
    warehouse_name  varchar(100)                       not null comment '仓库名称',
    manager_user_id char(32)                           null comment '仓管ID',
    address         varchar(255)                       null comment '地址',
    status          tinyint  default 1                 not null comment '状态',
    create_time     datetime default CURRENT_TIMESTAMP null comment '创建时间',
    is_delete       int      default 0                 not null,
    constraint uk_wh_manager
        unique (manager_user_id)
)
    comment '仓库表' charset = utf8mb4;
