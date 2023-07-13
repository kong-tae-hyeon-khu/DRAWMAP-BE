package com.umc.drawmap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfigure {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .rememberMe().disable()

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // /oauth2/** 에 대한 요청은 허가, 이외에 요청은 인증 필요
        http.authorizeRequests()
                .antMatchers("/oauth2/**").permitAll()
                .anyRequest().authenticated();

        http.oauth2Login()
                // 프론트 -> 백 : 소셜 로그인 요청을 보내는 URL
                .authorizationEndpoint().baseUri("ouath2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository)
                .and()
                // 소셜 인증 후, redirect URL
                .redirectionEndpoint().baseUri("/oauth2/callback/*")
                // 인증 요청을 cookie 에 저장. 그럼 왜 JWT 를 사용하는거지..?
                .and()
                .userInfoEndpoint().userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);

        http.logout()
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID");

        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }
}
