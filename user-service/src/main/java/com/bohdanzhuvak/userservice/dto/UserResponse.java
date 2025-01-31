package com.bohdanzhuvak.userservice.dto;

import com.bohdanzhuvak.userservice.model.Role;
import lombok.Builder;

import java.time.Instant;
import java.util.Set;

@Builder
public record UserResponse(
        String id,
        String email,
        String firstName,
        String lastName,
        Set<Role> roles,
        Instant createdAt
) {}
