package com.ne.srm.process.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class ContractStatusLog {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private ContractEntity contract;

    private String fromStatus;

    private String toStatus;

    private String operator;

    private String remark;

    private Long createdTime;
}
