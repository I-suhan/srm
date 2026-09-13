package com.ne.srm.auth.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "sys_role")
@Data
public class Role {


    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;


    /**
     * 角色名称
     */
    private String name;


    /**
     * 角色编码
     * 例如：
     * ADMIN
     */
    @Column(unique = true)
    private String code;


    private Integer status = 1;


    private Instant createdTime;

    /**
     * 角色-权限
     * sys_role_permission
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sys_role_permission",

            joinColumns =
            @JoinColumn(
                    name="role_id"
            ),

            inverseJoinColumns =
            @JoinColumn(
                    name="permission_id"
            )
    )
    private Set<Permission> permissions;


}
