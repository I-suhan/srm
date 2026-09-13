package com.ne.srm.auth.service;

import com.ne.srm.auth.entity.User;
import com.ne.srm.auth.repositoty.RefreshTokenRepository;
import com.ne.srm.auth.repositoty.UserRepository;
import com.ne.srm.auth.vo.UserInfoVO;
import com.ne.srm.auth.exception.AuthException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;


    public UserService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository){

        this.userRepository = userRepository;

        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * 禁用
     */
    @Transactional
    public void disable(Long id){
        User user = userRepository.findById(id).orElseThrow(()->new AuthException(404, "用户不存在" ));
        user.setStatus(0);
        refreshTokenRepository.revokeByUserId(id);
        userRepository.save(user);
    }

    /**
     * 启用
     */
    @Transactional
    public void enable(Long id){
        User user = userRepository.findById(id).orElseThrow(()->new AuthException(404, "用户不存在"));
        user.setStatus(1);
        userRepository.save(user);
    }


    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()-> new AuthException(404, "用户不存在"));
    }

}
