package com.ne.srm.auth.service;

import com.ne.srm.auth.entity.Permission;
import com.ne.srm.auth.entity.SecurityUser;
import com.ne.srm.auth.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
public class PermissionService {


    /**
     * 根据用户角色获取权限
     */
    public Set<SimpleGrantedAuthority> getAuthorities(User user){

        return user.getRoles().stream()
                // 获取角色下所有权限
                .flatMap(role -> role.getPermissions().stream())
                // Permission.code
                .map(Permission::getCode)
                // 转Security权限
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

    }

    public Set<String> getPermissions(User user){
        return user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                // Permission.code
                .map(Permission::getCode).collect(Collectors.toSet());
    }

    public Set<String> getPermissions(SecurityUser user){
        return user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                // Permission.code
                .map(Permission::getCode).collect(Collectors.toSet());
    }

}
