package com.ne.srm.auth.dto;

import java.util.List;

public record UserInfoResponse(Long id, String username, List<String> roles, List<String> permissions) {
}
