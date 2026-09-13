package com.ne.srm.core.dto;

public record RedisLockToken(
        String key,
        String value
) {
}
