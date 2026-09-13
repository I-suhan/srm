package com.ne.srm.dict.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

/***
 * 基础字典表
 * */
@Data
@Entity
public class SysDictType {

    @Id
    private int id;

    private String dictCode;

    private String dictName;

    private String remark;

    private Long createTime;
}
