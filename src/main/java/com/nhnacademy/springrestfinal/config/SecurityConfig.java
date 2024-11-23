package com.nhnacademy.springrestfinal.config;

import com.nhnacademy.springrestfinal.filter.CheckBlockFilter;
import com.nhnacademy.springrestfinal.filter.UserAuthenticationFilter;
import com.nhnacademy.springrestfinal.handler.CustomAuthenticationFailureHandler;
import com.nhnacademy.springrestfinal.handler.CustomAuthenticationSuccessHandler;
import com.nhnacademy.springrestfinal.handler.CustomLogoutHandler;
import com.nhnacademy.springrestfinal.handler.LoginFailCounter;
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
    private final LoginFailCounter loginFailCounter;

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
                        .requestMatchers("/dooray/**").permitAll() // 메세지 보내기
                        .anyRequest().authenticated()
        );

        // 로그인 설정
        http.formLogin(
                formLogin ->
                        formLogin.loginPage("/login")
                                .usernameParameter("id")
                                .passwordParameter("pwd")
                                .loginProcessingUrl("/login/process")
                                .successHandler(new CustomAuthenticationSuccessHandler(redisTemplate, loginFailCounter))
                                .failureHandler(new CustomAuthenticationFailureHandler(redisTemplate, loginFailCounter, memberService))
        )
                .oauth2Login(oauth2 -> oauth2
                                .loginPage("/login") // OAuth2 로그인도 같은 로그인 페이지 사용
                                .defaultSuccessUrl("/")); // OAuth2 로그인 성공 후 리디렉션

        // 로그아웃 설정
        http.logout(
                logout->
                        logout
                                .deleteCookies("SESSION")
                                .invalidateHttpSession(true)
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/")
                                .addLogoutHandler(new CustomLogoutHandler(redisTemplate))
        );

        // 에러 페이지 설정
        http
                .exceptionHandling()
                .accessDeniedPage("/403");

        // 필터 추가
        http
                // 입력한 아이디가 차단된 아이디인지 검사하는 필터
                .addFilterBefore(new CheckBlockFilter(memberService),
                        UsernamePasswordAuthenticationFilter.class)
                // 쿠키에 세션 아이디가 저장되어 있는지 검사하는 필터
                .addFilterBefore(new UserAuthenticationFilter(redisTemplate, memberService),
                        CheckBlockFilter.class);



        return http.build();
    }



}
