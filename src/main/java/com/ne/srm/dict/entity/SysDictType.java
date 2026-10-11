package com.ne.srm.dict.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;

/***
 * 基础字典表
 * */
@Data
@Entity
public class SysDictType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String dictCode;

    private String dictName;

    private String remark;

    private Long createTime;
}
