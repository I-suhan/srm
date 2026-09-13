package com.ne.srm.auth.service;


import com.ne.srm.auth.entity.SecurityUser;
import com.ne.srm.auth.entity.User;
import com.ne.srm.auth.repositoty.UserRepository;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {


    private final UserRepository userRepository;
    private final PermissionService permissionService;


    public UserDetailsServiceImpl(UserRepository userRepository, PermissionService permissionService) {
        this.userRepository = userRepository;
        this.permissionService = permissionService;
    }


    /**
     * Spring Security核心入口
     * 用户认证时调用
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("用户不存在"));

        if(user.getStatus()==0){
            throw new UsernameNotFoundException("用户失效");
        }

        /*
         * RBAC权限加载
         */
        Collection<? extends GrantedAuthority> authorities = permissionService.getAuthorities(user);

        return new SecurityUser(user, authorities);
    }

    public UserDetails loadUserByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("用户不存在"));

        if(user.getStatus()==0){
            throw new UsernameNotFoundException("用户失效");
        }

        /*
         * RBAC权限加载
         */
        Collection<? extends GrantedAuthority> authorities = permissionService.getAuthorities(user);

        return new SecurityUser(user, authorities);
    }
}