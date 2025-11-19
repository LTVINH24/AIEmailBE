package com.example.aimailbox.dto;

import lombok.Data;

@Data
public class GoogleRequest {
    private String idToken;

    public String getIdToken() {
        return this.idToken;
    }
}
