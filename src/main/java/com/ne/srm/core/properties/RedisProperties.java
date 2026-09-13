package com.ne.srm.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "redis")
@Data
public class RedisProperties {


    private String prefix;

    private List<String> trustedPackages = new ArrayList<>();
}
