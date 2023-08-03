package com.umc.drawmap.config.security;

import com.umc.drawmap.security.JwtAuthenticationFilter;
import com.umc.drawmap.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    private static final String[] PERMIT_URL_ARRAY = {
            /* swagger v2 */
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            /* swagger v3 */
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity)throws Exception {
//        httpSecurity
//                .authorizeRequests()
//                .anyRequest().permitAll()
//                .and()
//                .formLogin().disable();

        httpSecurity
                .httpBasic().disable()
                .csrf().disable()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .cors().and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/user/login").permitAll()
                .antMatchers(HttpMethod.POST, "/user/signup").permitAll()
                .antMatchers(HttpMethod.GET, "/callback/**").permitAll()
                .antMatchers(HttpMethod.GET, "/oauth2/kakao").permitAll()
                .antMatchers(PERMIT_URL_ARRAY).permitAll() // Swagger 를 제외시키기 위해서.
                .anyRequest().hasRole("User")

                // 로그인 안한 사용자도 접근할 수 있는 정보가 있지 않을까??

                .and()
                .exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler())

                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);




        return httpSecurity.build();
    }
}