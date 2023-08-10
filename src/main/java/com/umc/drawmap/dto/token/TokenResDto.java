package com.umc.drawmap.dto.token;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long refreshTokenExpireDate;
}
