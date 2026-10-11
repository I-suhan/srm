package com.ne.srm.dict.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;

@Data
@Entity
public class SysDictItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dictCode;

    private String itemCode;

    private String itemName;

    //所属分类编码(附件类型专用)
    private String categoryCode;

    private long sortNo;

    private int enabled;

    private String remark;

    private Long createdTime;

}
