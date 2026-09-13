package com.ne.srm.auth.dto;

import java.time.Instant;

public record DeviceResponse(Long id,
                             String deviceId,
                             String deviceName,
                             String os,
                             String ipv4,
                             String ipv6,
                             String userAgent,
                             Instant lastTime) {
}
