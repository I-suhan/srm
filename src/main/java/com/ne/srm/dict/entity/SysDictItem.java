package com.ne.srm.dict.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class SysDictItem {

    @Id
    private Long id;

    private String dictCode;

    private String itemCode;

    private String itemName;

    private long sortNo;

    private int enable;

    private String remark;

    private Long createdTime;

}
