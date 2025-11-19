package com.example.aimailbox.dto;

import lombok.Data;

@Data
public class RefreshRequest {
    private String refreshToken;

    public String getRefreshToken() {
        return this.refreshToken;
    }
}
