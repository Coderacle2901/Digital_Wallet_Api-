package com.example.Digital_Wallet_Api.auth.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRefreshRequestDto {
    private String refreshToken;
}
