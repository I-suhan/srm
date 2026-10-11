package com.ne.srm.process.entity;

import com.ne.srm.common.module.BaseEntity;
import com.ne.srm.company.entity.CompanyEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class ContractEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contractNo;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @OneToOne
    @JoinColumn(name = "buyer_company_id")
    private CompanyEntity buyerCompany;

    @OneToOne
    @JoinColumn(name = "supplier_company_id")
    private CompanyEntity supplierCompany;

    private String contractTypeCode;//合同类型

    private String contractStatusCode;//合同状态

    private int version;

    private Long parentContractId;// 原合同

    private String revisionReason;

    private BigDecimal totalAmount;

    private String totalAmountUp;

    private String currencyCode;

    private Long signDate;

    private String paymentStatusCode;
    /***
    *   条款
    */
    @ManyToOne
    @JoinColumn(name = "term_id")
    private ContractTerm contractTerm;

    private String owner;

}
