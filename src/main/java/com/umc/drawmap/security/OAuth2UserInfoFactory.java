package com.umc.drawmap.security;

import com.umc.drawmap.domain.auth.AuthProvider;

import java.util.Map;


// 구글 , 카카오 , 네이버와 같은 여러 소셜에 해당하는 값을 불러오기 위해서.
// 현재는 구글만 있으니까 !
public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationid, Map<String, Object> attributes) {
//        if (registrationid.equalsIgnoreCase(AuthProvider.google.toString())) {
//            return new GoogleOAuth2UserInfo(attributes);
//        }
        return new GoogleOAuth2UserInfo(attributes);
    }
}
