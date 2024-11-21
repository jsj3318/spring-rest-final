package com.nhnacademy.springrestfinal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // http 멤버 조작 테스트용 인증 끄기
//        http.csrf(AbstractHttpConfigurer::disable);

        // 페이지 권한 설정
        http.authorizeHttpRequests(
                authorizeRequests ->
                authorizeRequests
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/member/**").hasAuthority("ROLE_MEMBER")
                        .requestMatchers("/google/**").hasRole("GOOGLE")
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/members/**").permitAll() // 멤버 조작 http
                        .anyRequest().authenticated()
        );

        // 로그인 폼 설정
        http.formLogin(
                formLogin ->
                        formLogin.loginPage("/login")
                                .usernameParameter("id")
                                .passwordParameter("pwd")
                                .loginProcessingUrl("/login/process")
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
