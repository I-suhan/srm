package com.ne.srm.pay.entity;

import com.ne.srm.process.entity.ContractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;

/****
*   付款记录
*/
@Entity
@Data
public class PaymentRecord {

    @Id
    private Long id;

    private String paymentNo;//付款业务号

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private ContractEntity contract;

    private String paymentTypeCode;//预付款/尾款/全款/补款

    private BigDecimal amount;//金额

    private String currencyCode;//币种

    private BigDecimal exchangeRate;//汇率

    private BigDecimal localAmount;//本币金额

    private Long paymentDate;

    private String remark;
}
