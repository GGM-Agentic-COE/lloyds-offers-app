package com.lloyds.offers.auth.api.dto;

public record ApiResponse<T>(T data, Object meta, Object errors) {
    public static <T> ApiResponse<T> of(T data) {
        return new ApiResponse<>(data, null, null);
    }
}
