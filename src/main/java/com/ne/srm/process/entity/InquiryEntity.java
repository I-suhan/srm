package com.ne.srm.process.entity;

import com.ne.srm.common.module.BaseEntity;
import com.ne.srm.dict.entity.SysDictItem;
import com.ne.srm.product.entity.ProductEntity;
import com.ne.srm.company.enums.InvoiceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class InquiryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String inquiryNo;

    @OneToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    //实物图
    private String physicalPicture;

    //目标价
    private BigDecimal targetPrice;

    //出厂价
    private BigDecimal exwPrice;

    //logo价格
    private BigDecimal logoPrice;

    //运费
    private BigDecimal shippingPrice;

    private Double invoiceExtra;//票面加点

    private Double actualExtra;//实际加点

    private InvoiceType invoice_type;//专普票

    //装柜类型
    @ManyToOne
    @JoinColumn(name = "container_load_type_id")
    private SysDictItem containerLoadType;

    private Integer inquiryQuantity;

    private String shopName;
    private String shopContact;

    private Integer cartonPcs;
    private Integer cartonNumber;
    private BigDecimal cartonWeight;
    private Double cartonCbm;
    private String cartonSize;

    private int statusCode;

    private String owner;
    private Integer version;


}
