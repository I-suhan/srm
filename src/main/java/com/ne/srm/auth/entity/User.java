package com.ne.srm.auth.entity;

import com.ne.srm.auth.vo.UserInfoVO;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
@Entity
@Table(name="sys_user")
public class User {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    /**
     * 登录账号
     */
    @Column(
            unique = true,
            nullable = false
    )
    private String username;

    private String nickname;

    /**
     * BCrypt密码
     */
    @Column(nullable = false)
    private String password;


    /**
     * 1启用 0禁用
     */
    private int status = 1;

    private Boolean disabled = Boolean.FALSE;

    private Long tokenVersion = 1L;

    private Instant createdTime;

    private Instant updatedTime;


    /**
     * 用户-角色
     * 中间表：
     * sys_user_role
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "sys_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @Transient
    public UserInfoVO convertVO(){
        UserInfoVO vo = new UserInfoVO();
        vo.setId(this.id);
        vo.setUsername(this.username);
        vo.setNickname(this.nickname);
        vo.setStatus(this.status);
        vo.setRoles(this.getRoles().stream().map(Role::getName).toList());
        return vo;
    }
}
