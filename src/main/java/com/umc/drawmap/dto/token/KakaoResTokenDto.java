package com.umc.drawmap.dto.token;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KakaoResTokenDto {
    String accessToekn;
    String refreshToken;
}
