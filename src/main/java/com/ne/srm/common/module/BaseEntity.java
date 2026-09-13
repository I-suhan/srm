package com.ne.srm.common.module;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;


@Setter
@Getter
public class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String createBy;

    private Long createTime;

    private String updateBy;

    private Long updateTime;

}
