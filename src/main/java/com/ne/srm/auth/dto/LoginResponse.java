package com.ne.srm.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ne.srm.auth.vo.UserInfoVO;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Collection;

@Data
@Builder
public class LoginResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private Long expiresIn;  // 单位：秒

    @JsonProperty("refresh_expires_in")
    private Long refreshExpiresIn;  // 单位：秒

    @JsonProperty("user_info")
    private UserInfoVO userInfo;

    @JsonProperty("permissions")
    private Collection<String> permissions;
}
