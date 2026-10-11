package com.ne.srm.process.entity;

import com.ne.srm.product.entity.ProductEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class ContractItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private ContractEntity contract;

    //合同批次
    private String contractBatchNum;

    @OneToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    private String productName;//品名
    private String productUnit;//单位

    private String productDescription;

    private String color;

    private String deliveryDate;//交期

    private String invoiceType;

    private BigDecimal unitPrice;//单个费用

    private Integer cartonPcs;

    private BigDecimal cartonWeight;

    private Double cartonCbm;

    private String cartonSize;

    private Integer cartonNum;

    private Integer quantity;//数量

    private BigDecimal amount;


    private String remark;
}
