package com.ne.srm.auth.vo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
public class UserInfoVO {

    private Long id;
    private String username;
    private String nickname;
    private int status;
    private List<String> roles;
}
