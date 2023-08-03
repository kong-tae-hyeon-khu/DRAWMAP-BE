package com.umc.drawmap.dto.token;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenReqDto {
    private String access_token;
}
