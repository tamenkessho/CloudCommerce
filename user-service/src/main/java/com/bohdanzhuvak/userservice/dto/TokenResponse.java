package com.bohdanzhuvak.userservice.dto;

import java.time.Instant;

public record TokenResponse(
    String accessToken,
    Instant expiresAt
) {

}
