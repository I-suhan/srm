package com.ne.srm.auth.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name="sys_permission")
@Data
public class Permission {


    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;


    /**
     * 权限名称
     * 例如：
     * 用户新增
     */
    private String name;


    /**
     * 权限编码
     * 例如：
     * user:add
     */
    @Column(unique = true)
    private String code;

    private Integer status = 1;


    private Instant createdTime;

}
