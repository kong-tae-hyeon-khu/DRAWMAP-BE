package com.umc.drawmap.security;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }


    public String getId() {
        return (String) attributes.get("sub");
    }

    public String getNickname() {
        return (String) attributes.get("name");
    }

    public String getImageUrl() {
        return (String) attributes.get("picture");
    }
}