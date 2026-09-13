package com.ne.srm.process.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class StateTransition {

    @Id
    private Long id;

    private String businessTypeCode;

    private String fromStatus;

    private String toStatus;
}
