package com.ne.srm.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfo {
    /**
     * SKU (库存单位)
     * 用于唯一标识产品的编码
     */
    private String sku;

    /**
     * 产品描述
     * 产品的详细描述信息
     */
    private String description;

    /**
     * 外箱尺寸
     * 供应商包装完成时需再次确认的包装箱尺寸
     * 格式示例：长x宽x高 cm，如 30x30x30 cm
     */
    private String outerBoxSize;

    /**
     * 交期
     * 产品预计交付日期
     */
    private String deliveryDate;

    /**
     * 发票类型
     * 如：增值税专用发票、普通发票、电子发票等
     */
    private String invoiceType;

    /**
     * 含运含税价
     * 包含运费和税费的总价格
     */
    private BigDecimal totalPrice;

    /**
     * 装箱率
     * 每个外箱可装的产品数量
     */
    private Integer packingRate;

    /**
     * 外箱毛重
     * 包含产品和包装材料的总重量
     * 格式示例：15 kg
     */
    private String outerBoxGrossWeight;

    /**
     * 外箱材积
     * 外箱的体积（通常用于计算物流费用）
     * 格式示例：0.027 m³ 或 1.2 cbm
     */
    private String outerBoxVolume;

    /**
     * 外箱总数
     * 该产品的总箱数
     */
    private Integer totalOuterBoxes;
}
