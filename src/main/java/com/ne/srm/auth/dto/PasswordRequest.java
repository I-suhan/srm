package com.ne.srm.auth.dto;

public record PasswordRequest(String oldPassword, String newPassword) {
}
