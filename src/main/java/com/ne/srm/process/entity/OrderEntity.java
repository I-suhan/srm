package com.ne.srm.process.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class OrderEntity {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * 询价记录
     */
    @ManyToOne
    @JoinColumn(name = "inquiry_id")
    private InquiryEntity inquiry;

    /**
     * 订单批次
     */
    private String orderNo;

    /***
     * 采购数量
     * */
    private Integer quantity;

    /***
     * 采购数量
     * */
    private String statusCode;

    private Long createTime;
}
