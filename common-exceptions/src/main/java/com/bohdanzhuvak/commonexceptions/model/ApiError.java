package com.bohdanzhuvak.commonexceptions.model;

import java.util.List;

public record ApiError(
    String timestamp,
    int status,
    String error,
    String message,
    String path,
    String code,
    List<ValidationDetail> details
) {}
