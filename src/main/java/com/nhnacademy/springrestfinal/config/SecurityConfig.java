package com.nhnacademy.springrestfinal.config;

import com.nhnacademy.springrestfinal.filter.UserAuthenticationFilter;
import com.nhnacademy.springrestfinal.handler.CustomAuthenticationFailureHandler;
import com.nhnacademy.springrestfinal.handler.CustomAuthenticationSuccessHandler;
import com.nhnacademy.springrestfinal.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final RedisTemplate<String, Object> redisTemplate;
    private final MemberService memberService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CSRF 토큰 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        // 페이지 권한 설정
        http.authorizeHttpRequests(
                authorizeRequests ->
                authorizeRequests
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/member/**").hasAuthority("ROLE_MEMBER")
                        .requestMatchers("/google/**").hasRole("GOOGLE")
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/members/**").permitAll() // 멤버 조작 http를 실행하기 위함
                        .anyRequest().authenticated()
        );

        // 로그인 설정
        http.formLogin(
                formLogin ->
                        formLogin.loginPage("/login")
                                .usernameParameter("id")
                                .passwordParameter("pwd")
                                .loginProcessingUrl("/login/process")
                                .successHandler(new CustomAuthenticationSuccessHandler(redisTemplate))
                                .failureHandler(new CustomAuthenticationFailureHandler(redisTemplate))
        );

        // 로그아웃 설정
        http.logout(
                logout->
                        logout
                                .deleteCookies("SESSION")
                .invalidateHttpSession(true)
                .logoutUrl("/logout")
                .logoutSuccessUrl("/"));

        // 에러 페이지 설정
        http
                .exceptionHandling()
                .accessDeniedPage("/403");

        // 세션 쿠키를 읽어서 이미 로그인 한 걸 검사하는 필터 추가
        http
                .addFilterBefore(new UserAuthenticationFilter(redisTemplate, memberService),
                        UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }



}
