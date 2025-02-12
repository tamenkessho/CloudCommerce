package com.bohdanzhuvak.commonsecurity.dto;

public record UserHeaderContext(
    String userId,
    String rolesHeader
) {}
