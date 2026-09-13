package com.ne.srm.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@ConfigurationProperties(prefix = "security")
@Data
public class SecurityProperties {

    private String tokenMode = "cookie";

    private Cookie cookie = new Cookie();

    private Jwt jwt = new Jwt();



    @Data
    public static class Cookie {
        private boolean secure=false;
        private String sameSite="Strict";

    }


    @Data
    public static class Jwt {

        private String secretKey;

        /***
         * 毫秒
         * */
        private long accessExpire;

        private long refreshExpire;

        private long sessionExpire;
        private long sessionTimeout;

        /* 刷新界限 */
        private long accessRemainTime;

        private long refreshRemainTime;

        private Duration lockTimeout = Duration.ofSeconds(5);
        private Duration resultCacheTtl = Duration.ofMinutes(20);
        private int spinWaitMax = 10;
        private Duration spinWaitInterval = Duration.ofMillis(100);

    }

}
