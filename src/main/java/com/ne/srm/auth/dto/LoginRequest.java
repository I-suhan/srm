package com.ne.srm.auth.dto;

public record LoginRequest(
        String username,
        String password,
        String deviceId,
        String deviceName,
        String os
) {}
