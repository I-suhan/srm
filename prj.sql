CREATE TABLE sys_dict_type
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    dict_code    VARCHAR(100) NOT NULL UNIQUE COMMENT '字典类型编码，例如 CONTRACT_STATUS',
    dict_name    VARCHAR(100) NOT NULL COMMENT '字典类型名称',
    remark       VARCHAR(500) COMMENT '备注',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT ='字典类型表';
CREATE TABLE sys_dict_item
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    dict_code     VARCHAR(100) NOT NULL COMMENT '字典编码',
    item_code     VARCHAR(100) NOT NULL COMMENT '字典项编码',
    item_name     VARCHAR(100) NOT NULL COMMENT '字典项名称',
    category_code VARCHAR(100) COMMENT '所属分类编码(附件类型专用)',
    sort_no       INT     DEFAULT 0 COMMENT '排序号',
    enabled       TINYINT DEFAULT 1 COMMENT '是否启用',
    remark        VARCHAR(500) COMMENT '备注',
    created_time  BIGINT  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_dict_item (dict_code, item_code)
) COMMENT ='字典项表';
CREATE TABLE company
(
    id                BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    company_code      VARCHAR(50)  NOT NULL UNIQUE COMMENT '公司编码',
    company_name      VARCHAR(255) NOT NULL COMMENT '公司名称',
    company_type_code VARCHAR(50) COMMENT '公司类型：BUYER/SUPPLIER/FORWARDER/CUSTOMER',
    address           VARCHAR(500) COMMENT '公司地址',
    phone             VARCHAR(50) COMMENT '联系电话',
    tax_no            VARCHAR(100) COMMENT '税号',
    bank_name         VARCHAR(255) COMMENT '开户银行',
    bank_account      VARCHAR(255) COMMENT '银行账户',
    bank_id_code      VARCHAR(100) COMMENT '银行代码',
    enabled           TINYINT DEFAULT 1 COMMENT '是否启用',
    shop_name         VARCHAR(255) COMMENT '平台公司名称',
    shop_contact      VARCHAR(255) COMMENT '平台公司联系方式',
    created_time      BIGINT  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT ='公司主体表（买方/卖方/货代统一管理）';
CREATE TABLE contacter
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    company_id BIGINT COMMENT '所属公司ID',
    name       VARCHAR(100) COMMENT '联系人姓名',
    phone      VARCHAR(50) COMMENT '联系电话',
    address    VARCHAR(500) COMMENT '收货地址',
    email      VARCHAR(100) COMMENT '邮箱',
    remark     VARCHAR(500) COMMENT '备注',
    FOREIGN KEY (company_id) REFERENCES company (id)
) COMMENT ='公司标签说明书联系人表';
CREATE TABLE product
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    sku            VARCHAR(100) UNIQUE COMMENT '产品SKU',
    name_zh        VARCHAR(255) COMMENT '翻译名称',
    name_pl        VARCHAR(255) COMMENT '原文名称',
    description_zh TEXT COMMENT '翻译描述',
    description_pl TEXT COMMENT '原文描述',
    color          VARCHAR(100) COMMENT '颜色',
    need_ce        TINYINT DEFAULT 0 COMMENT '是否需要CE认证',
    manuals        TINYINT DEFAULT 0 COMMENT '是否有说明书',
    stickers       TINYINT DEFAULT 0 COMMENT '是否有贴纸'
) COMMENT ='产品表';
CREATE TABLE inquiry_entity
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    inquiry_no       VARCHAR(50) UNIQUE COMMENT '询盘编号',
    product_id       BIGINT COMMENT '产品ID',
    shop_name        VARCHAR(255) COMMENT '1688/阿里供应商名称（临时）',
    shop_contact     VARCHAR(255) COMMENT '供应商联系人（临时）',
    target_price     DECIMAL(18, 4) COMMENT '目标价格',
    exw_price        DECIMAL(18, 4) COMMENT 'EXW报价',
    logo_price       DECIMAL(18, 4) COMMENT 'LOGO报价',
    shipping_price   DECIMAL(18, 4) COMMENT '运输报价',
    actual_extra     DECIMAL(18, 4) COMMENT '实际加点',
    invoice_extra    DECIMAL(18, 4) COMMENT '票面加点',
    invoice_type     DECIMAL(18, 4) COMMENT '发票类型',
    owner            VARCHAR(100) COMMENT '采购负责人',
    inquiry_quantity INT COMMENT '询价数量',
    carton_pcs       INT COMMENT '每箱数量',
    carton_num       INT COMMENT '箱数',
    carton_size      VARCHAR(100) COMMENT '箱规',
    carton_cbm       DECIMAL(10, 4) COMMENT '外箱体积',
    carton_weight    DECIMAL(18, 4) COMMENT '外箱毛重',
    status_code      VARCHAR(50) COMMENT '询盘状态',
    created_time     BIGINT DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time     BIGINT DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    FOREIGN KEY (product_id) REFERENCES product (id)
) COMMENT ='询盘表（供应商报价阶段数据）';
CREATE TABLE `inquiry_history` (
                                   `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',

                                   `shop_name` varchar(255) DEFAULT NULL,
                                   `shop_contact` varchar(255) DEFAULT NULL,
                                   `created_by` varchar(255) DEFAULT NULL,
                                   `created_time` bigint DEFAULT NULL,
                                   `exw_price` decimal(38,2) DEFAULT NULL,
                                   `logo_price` decimal(38,2) DEFAULT NULL,
                                   `shipping_price` decimal(38,2) DEFAULT NULL,
                                   `actual_extra` double DEFAULT NULL,
                                   `invoice_extra` double DEFAULT NULL,
                                   `type` tinyint DEFAULT NULL,
                                   `inquiry_quantity` int DEFAULT NULL,
                                   `version` int NOT NULL,
                                   `inquiry_id` bigint DEFAULT NULL,
                                   FOREIGN KEY (`inquiry_id`) REFERENCES `inquiry_entity` (`id`)
)COMMENT ='询盘历史存档表';
CREATE TABLE order_entity
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    order_no     VARCHAR(50) UNIQUE COMMENT '订单编号',
    inquiry_id   BIGINT NOT NULL COMMENT '来源询盘ID',
    quantity     INT COMMENT '采购数量',
    status_code  VARCHAR(50) COMMENT '订单状态',
    created_time BIGINT DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (inquiry_id) REFERENCES inquiry (id)
) COMMENT ='总部采购订单（可一个询盘生成多个订单）';
CREATE TABLE contract_term
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    company_id BIGINT,
    term       LONGTEXT,
    version    INT DEFAULT 1,
    FOREIGN KEY (company_id)
        REFERENCES company (id)
);
CREATE TABLE contract_entity
(
    id                   BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    contract_no          VARCHAR(50) UNIQUE COMMENT '合同编号',
    order_id             BIGINT NOT NULL COMMENT '所属订单ID',
    buyer_company_id     BIGINT NOT NULL COMMENT '买方公司',
    supplier_company_id  BIGINT NOT NULL COMMENT '卖方公司',
    contract_type_code   VARCHAR(50) COMMENT '合同类型：整柜/拼柜/补货',
    contract_status_code VARCHAR(50) COMMENT '合同状态',
    version              INT    DEFAULT 1 COMMENT '版本号',
    parent_contract_id   BIGINT COMMENT '父合同ID（修订来源）',
    revision_reason      VARCHAR(500) COMMENT '修订原因（重量/CBM变化等）',
    total_amount         DECIMAL(18, 4) COMMENT '合同总金额',
    currency_code        VARCHAR(20) COMMENT '币种',
    sign_date            BIGINT COMMENT '签署日期',
    payment_status_code  VARCHAR(50) COMMENT '付款状态',
    contract_term_id     BIGINT NOT NULL COMMENT '合同条款ID',
    created_time         BIGINT DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (order_id) REFERENCES order_entity (id),
    FOREIGN KEY (buyer_company_id) REFERENCES company (id),
    FOREIGN KEY (supplier_company_id) REFERENCES company (id),
    FOREIGN KEY (contract_term_id) REFERENCES contract_term (id)
) COMMENT ='采购合同（支持版本控制与拆柜）';
CREATE TABLE contract_item
(
    id                  BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    contract_id         BIGINT NOT NULL COMMENT '合同ID',
    product_id          BIGINT COMMENT '产品ID',
    product_name        VARCHAR(255) COMMENT '产品名称',
    product_unit        VARCHAR(255) COMMENT '产品单位',
    product_description TEXT COMMENT '产品描述',
    color               VARCHAR(100) COMMENT '颜色',
    delivery_date       VARCHAR(255) COMMENT '交期',
    invoice_type        VARCHAR(255) COMMENT '发票类型',
    unit_price          DECIMAL(18, 4) COMMENT '单价',
    carton_pcs          INT COMMENT '每箱数量',
    carton_weight       DECIMAL(18, 4) COMMENT '外箱毛重',
    carton_size         VARCHAR(100) COMMENT '箱规',
    carton_cbm          DECIMAL(10, 4) COMMENT '外箱体积',
    carton_num          INT COMMENT '箱数',
    quantity            INT COMMENT '数量',
    amount              DECIMAL(18, 4) COMMENT '金额',
    remark              VARCHAR(500) COMMENT '备注',
    FOREIGN KEY (contract_id) REFERENCES contract_entity (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
) COMMENT ='合同明细（支持拆柜/拼柜）';
CREATE TABLE payment_record
(
    id                BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    payment_no        VARCHAR(50) UNIQUE COMMENT '付款单号',
    contract_id       BIGINT NOT NULL COMMENT '合同ID',
    payment_type_code VARCHAR(50) COMMENT '付款类型：定金/尾款/全款',
    amount            DECIMAL(18, 4) COMMENT '付款金额',
    currency_code     VARCHAR(20) COMMENT '币种',
    exchange_rate     DECIMAL(18, 6) COMMENT '汇率',
    local_amount      DECIMAL(18, 4) COMMENT '本币金额',
    payment_date      BIGINT COMMENT '付款日期',
    remark            VARCHAR(500) COMMENT '备注',
    FOREIGN KEY (contract_id) REFERENCES contract_entity (id)
) COMMENT ='付款记录（财务对账核心表）';
CREATE TABLE goods_receipt
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    receipt_no   VARCHAR(50) COMMENT '收货单号',
    contract_id  BIGINT COMMENT '合同ID',
    receive_date BIGINT COMMENT '收货日期',
    status_code  VARCHAR(50) COMMENT '收货状态',
    remark       VARCHAR(500) COMMENT '备注',
    FOREIGN KEY (contract_id) REFERENCES contract_entity (id)
) COMMENT ='收货记录（仓库模块）';
CREATE TABLE sys_file
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    file_name    VARCHAR(255) COMMENT '文件名',
    file_path    VARCHAR(1000) COMMENT '文件路径',
    file_size    BIGINT COMMENT '文件大小',
    content_type VARCHAR(100) COMMENT '文件类型',
    file_md5     VARCHAR(64) COMMENT '文件MD5（去重）',
    created_time BIGINT DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间'
) COMMENT ='文件存储表';
CREATE TABLE business_attachment
(
    id                       BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    file_id                  BIGINT      NOT NULL COMMENT '文件ID',
    business_type_code       VARCHAR(50) NOT NULL COMMENT '业务类型',
    business_id              BIGINT      NOT NULL COMMENT '业务ID',
    attachment_category_code VARCHAR(50) NOT NULL COMMENT '附件分类（CONTRACT/PAYMENT/INVOICE等）',
    attachment_type_code     VARCHAR(50) NOT NULL COMMENT '附件类型（SIGNED_CONTRACT/BANK_SLIP等）',
    source_code              VARCHAR(50) NOT NULL COMMENT '文件来源（SUPPLIER/WAREHOUSE/FACTORY/FORWARDER）',
    remark                   VARCHAR(500) COMMENT '备注',
    created_time             BIGINT DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    update_time              BIGINT DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    FOREIGN KEY (file_id)
        REFERENCES sys_file (id)
) COMMENT ='业务附件关联表';
CREATE TABLE contract_status_log
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    contract_id   BIGINT NOT NULL COMMENT '合同ID',
    from_status   VARCHAR(50) COMMENT '原状态',
    to_status     VARCHAR(50) COMMENT '目标状态',
    operator_name VARCHAR(100) COMMENT '操作人',
    remark        VARCHAR(500) COMMENT '备注',
    created_time  BIGINT COMMENT '创建时间',
    FOREIGN KEY (contract_id) REFERENCES contract_entity (id)
) COMMENT ='合同状态流转日志';
CREATE TABLE state_transition
(
    id                 BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    business_type_code VARCHAR(50) COMMENT '业务类型（CONTRACT/ORDER等）',
    from_status        VARCHAR(50) COMMENT '起始状态',
    to_status          VARCHAR(50) COMMENT '目标状态'
) COMMENT ='状态机流转规则表';
-- COMPANY_TYPE
INSERT INTO sys_dict_item(dict_code, item_code, item_name)
VALUES ('COMPANY_TYPE', 'BUYER', '采购方'),
       ('COMPANY_TYPE', 'SUPPLIER', '供应商'),
       ('COMPANY_TYPE', 'FORWARDER', '货代'),
       ('COMPANY_TYPE', 'CUSTOMER', '客户');
-- INQUIRY_STATUS 询价状态
INSERT INTO sys_dict_item(dict_code, item_code, item_name)
VALUES ('INQUIRY_STATUS', 'QUOTING', '询价中'),
       ('INQUIRY_STATUS', 'QUOTED', '已报价'),
       ('INQUIRY_STATUS', 'RE_QUOTING', '重新询价中'),
       ('INQUIRY_STATUS', 'ORDERED', '已下单');
-- ORDER_STATUS 订单状态
INSERT INTO sys_dict_item(dict_code, item_code, item_name)
VALUES ('ORDER_STATUS', 'WAIT_CONTRACT', '待生成合同'),
       ('ORDER_STATUS', 'CONTRACTING', '合同处理中'),
       ('ORDER_STATUS', 'IN_PRODUCTION', '生产中'),
       ('ORDER_STATUS', 'COMPLETED', '已完成'),
       ('ORDER_STATUS', 'CANCELLED', '已取消');
-- INVOICE_TYPE 发票类型
INSERT INTO sys_dict_item(dict_code, item_code, item_name)
VALUES ('INVOICE_TYPE', 'REGULAR', '普票'),
       ('INVOICE_TYPE', 'SPECIAL', '专票');
-- CONTRACT_TYPE 装柜类型
INSERT INTO sys_dict_item(dict_code, item_code, item_name)
VALUES ('CONTRACT_TYPE', 'FULL_CONTAINER', '整柜'),
       ('CONTRACT_TYPE', 'LCL', '拼柜'),
       ('CONTRACT_TYPE', 'REPLENISHMENT', '补货');
-- PAYMENT_TYPE
INSERT INTO sys_dict_item(dict_code, item_code, item_name)
VALUES ('PAYMENT_TYPE', 'DEPOSIT', '定金'),
       ('PAYMENT_TYPE', 'BALANCE', '尾款'),
       ('PAYMENT_TYPE', 'FULL_PAYMENT', '全款');
-- PAYMENT_STATUS
INSERT INTO sys_dict_item(dict_code, item_code, item_name)
VALUES ('PAYMENT_STATUS', 'APPLY_DEPOSIT', '申请定金'),
       ('PAYMENT_STATUS', 'DEPOSIT_PAID', '定金已付'),
       ('PAYMENT_STATUS', 'APPLY_BALANCE', '申请尾款'),
       ('PAYMENT_STATUS', 'APPLY_SUPPLEMENT', '申请补款'),
       ('PAYMENT_STATUS', 'APPLY_FULL_PAYMENT', '申请全款'),
       ('PAYMENT_STATUS', 'SETTLED', '款项已结清');
-- CONTRACT_STATUS
INSERT INTO sys_dict_item(dict_code, item_code, item_name)
VALUES ('CONTRACT_STATUS', 'DRAFT', '草稿'),
       ('CONTRACT_STATUS', 'WAIT_SIGN', '待签章'),
       ('CONTRACT_STATUS', 'SIGNED', '已签章'),
       ('CONTRACT_STATUS', 'IN_PRODUCTION', '生产中'),
       ('CONTRACT_STATUS', 'WAIT_ACTUAL_DATA', '等待实际数据'),
       ('CONTRACT_STATUS', 'WAIT_CONTRACT_UPDATE', '等待修订合同'),
       ('CONTRACT_STATUS', 'WAIT_RESIGN', '等待重新签章'),
       ('CONTRACT_STATUS', 'READY_SHIPMENT', '待出货'),
       ('CONTRACT_STATUS', 'RECEIVED', '已收货'),
       ('CONTRACT_STATUS', 'FORWARDER_SUBMITTED', '资料已提交货代'),
       ('CONTRACT_STATUS', 'COMPLETED', '已完成'),
       ('CONTRACT_STATUS', 'CANCELLED', '已取消');
-- ATTACHMENT_SOURCE 文件来源
INSERT INTO sys_dict_item(dict_code, item_code, item_name)
VALUES ('ATTACHMENT_SOURCE', 'SUPPLIER', '供应商提供'),
       ('ATTACHMENT_SOURCE', 'WAREHOUSE', '仓库生成'),
       ('ATTACHMENT_SOURCE', 'FACTORY', '工厂提供'),
       ('ATTACHMENT_SOURCE', 'FORWARDER', '货代提供');
-- ATTACHMENT_CATEGORY
INSERT INTO sys_dict_item(dict_code, item_code, item_name)
VALUES ('ATTACHMENT_CATEGORY', 'CONTRACT', '合同文件'),
       ('ATTACHMENT_CATEGORY', 'PAYMENT', '付款文件'),
       ('ATTACHMENT_CATEGORY', 'INVOICE', '发票文件'),
       ('ATTACHMENT_CATEGORY', 'CERTIFICATE', '认证文件'),
       ('ATTACHMENT_CATEGORY', 'WAREHOUSE', '仓库文件'),
       ('ATTACHMENT_CATEGORY', 'SHIPPING', '物流文件'),
       ('ATTACHMENT_CATEGORY', 'PRODUCT', '产品资料'),
       ('ATTACHMENT_CATEGORY', 'OTHER', '其他文件');
-- ATTACHMENT_TYPE
INSERT INTO sys_dict_item(dict_code, item_code, item_name, category_code)
VALUES ('ATTACHMENT_TYPE', 'SIGNED_CONTRACT', '正式合同', 'CONTRACT'),
       ('ATTACHMENT_TYPE', 'REVISED_CONTRACT', '修订合同', 'CONTRACT'),
       ('ATTACHMENT_TYPE', 'CONTRACT_APPENDIX', '合同附件', 'CONTRACT'),
       ('ATTACHMENT_TYPE', 'BANK_SLIP', '银行水单', 'PAYMENT'),
       ('ATTACHMENT_TYPE', 'PROFORMA_INVOICE', '形式发票', 'INVOICE'),
       ('ATTACHMENT_TYPE', 'COMMERCIAL_INVOICE', '商业发票', 'INVOICE'),
       ('ATTACHMENT_TYPE', 'VAT_INVOICE', '增值税发票', 'INVOICE'),
       ('ATTACHMENT_TYPE', 'PACKING_LIST', '装箱单', 'INVOICE'),
       ('ATTACHMENT_TYPE', 'CE_CERT', 'CE认证', 'CERTIFICATE'),
       ('ATTACHMENT_TYPE', 'ROHS_CERT', 'ROHS认证', 'CERTIFICATE'),
       ('ATTACHMENT_TYPE', 'REACH_CERT', 'REACH认证', 'CERTIFICATE'),
       ('ATTACHMENT_TYPE', 'MSDS', 'MSDS文件', 'CERTIFICATE'),
       ('ATTACHMENT_TYPE', 'TEST_REPORT', '其他检测报告', 'CERTIFICATE'),
       ('ATTACHMENT_TYPE', 'SHIPPING_PHOTO', '发货照片', 'WAREHOUSE'),
       ('ATTACHMENT_TYPE', 'PACKING_PHOTO', '装箱照片', 'WAREHOUSE'),
       ('ATTACHMENT_TYPE', 'BULK_GOODS_PHOTO', '大货照片', 'WAREHOUSE'),
       ('ATTACHMENT_TYPE', 'STACKING_PHOTO', '堆货照片', 'WAREHOUSE'),
       ('ATTACHMENT_TYPE', 'WAREHOUSE_RECEIPT', '仓库签收单', 'WAREHOUSE'),
       ('ATTACHMENT_TYPE', 'DELIVERY_ORDER', '出库单', 'WAREHOUSE'),
       ('ATTACHMENT_TYPE', 'RECEIPT_PHOTO', '收货照片', 'WAREHOUSE'),
       ('ATTACHMENT_TYPE', 'BOOKING_NOTE', '订舱单', 'SHIPPING'),
       ('ATTACHMENT_TYPE', 'BILL_OF_LADING', '提单', 'SHIPPING'),
       ('ATTACHMENT_TYPE', 'LOGISTICS_ORDER', '物流单/约车记录', 'SHIPPING'),
       ('ATTACHMENT_TYPE', 'PRODUCT_PHOTO', '产品图片', 'PRODUCT');
-- CURRENCY
INSERT INTO sys_dict_item(dict_code, item_code, item_name)
VALUES ('CURRENCY', 'USD', '美元'),
       ('CURRENCY', 'CNY', '人民币'),
       ('CURRENCY', 'EUR', '欧元'),
       ('CURRENCY', 'PLN', '波兰兹罗提');
-- CONTAINER_LOAD_TYPE
INSERT INTO sys_dict_item(dict_code, item_code, item_name)
VALUES ('CONTAINER_LOAD_TYPE', 'FULL_CONTAINER', '整柜'),
       ('CONTAINER_LOAD_TYPE', 'LCL', '拼柜');