package com.ne.srm.auth.entity;

import com.ne.srm.auth.vo.UserInfoVO;
import jakarta.persistence.Transient;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class SecurityUser implements UserDetails {

    /**
     * 用户ID
     */
    private final Long id;


    /**
     * 登录账号
     */
    private final String username;

    /**
     * BCrypt密码
     */
    private final String password;

    private final String nickname;

    private final Long tokenVersion;

    private final Collection<Role> roles;

    /**
     * 权限集合
     * 当前为空
     * 下一步加载RBAC权限
     */
    private final Collection<? extends GrantedAuthority> authorities;

    private final int status;

    private final boolean enabled;


    public SecurityUser(User user, Collection<? extends GrantedAuthority> authorities) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
        this.tokenVersion = user.getTokenVersion();
        this.roles = user.getRoles();
        this.authorities = authorities;
        this.status = user.getStatus();
        this.enabled = !user.getDisabled();
    }

    /**
     * 用户是否未过期
     */
    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    /**
     * 用户是否未锁定
     */
    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    /**
     * 密码是否未过期
     */
    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    /**
     * 用户是否启用
     */
    @Override
    public boolean isEnabled(){
        return this.enabled && this.status != 0;
    }

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
