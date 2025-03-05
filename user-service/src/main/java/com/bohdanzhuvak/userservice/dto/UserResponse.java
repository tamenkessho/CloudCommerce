package com.bohdanzhuvak.userservice.dto;

import com.bohdanzhuvak.userservice.model.Role;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Builder;

@Builder
public record UserResponse(
        String id,
        String email,
        String firstName,
        String lastName,
        Set<Role> roles,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
