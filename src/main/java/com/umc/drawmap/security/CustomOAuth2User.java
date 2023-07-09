package com.umc.drawmap.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey,
                            String email, Role role) {
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
        this.role = role;
    }
}
