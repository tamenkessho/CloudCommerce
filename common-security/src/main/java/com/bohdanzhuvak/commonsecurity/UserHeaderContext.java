package com.bohdanzhuvak.commonsecurity;

public record UserHeaderContext(
    String userId,
    String rolesHeader
) {}
