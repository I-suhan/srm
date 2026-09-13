package com.ne.srm.process.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
/* 收货记录 */
public class GoodsReceipt {

    @Id
    private int id;

    private String receiptNo;//

    @OneToOne
    @JoinColumn(name = "contract_id")
    private ContractEntity contract;

    //应收数量
    private Integer receivableQuantity;

    //实收数量
    private Integer ReceivedQuantity;

    private Long ReceiptTime;

    private String warehouseRemark;

    private String statusCode;
}
