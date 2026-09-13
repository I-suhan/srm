package com.ne.srm.process.entity;

import com.ne.srm.company.enums.InvoiceType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class InquiryHistory {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "inquiry_id")
    private InquiryEntity inquiry;

    private String aliName;

    //出厂价
    private BigDecimal exwPrice;

    //logo价格
    private BigDecimal logoPrice;

    //运费
    private BigDecimal shippingPrice;

    private Double invoiceExtra;//票面加点

    private Double actualExtra;//实际加点

    private InvoiceType type;//专普票

    private Integer inquiryQuantity;//询价数量

    private int version;

    private String createdBy;

    private Long createdTime;
}
