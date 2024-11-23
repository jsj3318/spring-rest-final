package com.nhnacademy.springrestfinal.handler;

import com.nhnacademy.springrestfinal.domain.AcademyOAuthUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
public class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 로그인 성공시 기존 세션 무효화
        // id 비번 입력으로 로그인을 하고나면 세션과 쿠키가 유지된 채로 뒤로가기를 해서 로그인으로 간 뒤
        // 구글 로그인을 하면 구글 로그인이 무시되고 기존 로그인이 유지된다
        // 로그아웃을 한 것처럼 세션과 쿠키를 지운다

        // 세션 쿠키가 존재하면 레디스에서 제거
        Cookie[] cookies = request.getCookies();
        String sessionId = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("SESSION")) {
                sessionId = cookie.getValue();
                redisTemplate.delete(sessionId);
            }
        }

        // 세션의 아이디를 새로 바꾼다
        request.changeSessionId();

        // 쿠키 저장하기
        sessionId = UUID.randomUUID().toString();
        Cookie sessionCookie = new Cookie("SESSION", sessionId);
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true);
        sessionCookie.setMaxAge(60 * 60);
        response.addCookie(sessionCookie);

        // redis에 저장하기
        AcademyOAuthUser principal = (AcademyOAuthUser) authentication.getPrincipal();
        String userId = principal.getId();
        redisTemplate.opsForValue().set(sessionId, "GOOGLE:" + userId, 60 * 60, TimeUnit.SECONDS);
        // GOOGLE_USER:id 로 GoogleMember 객체 저장
        redisTemplate.opsForValue().set("GOOGLE_USER:" + userId, principal.getGoogleMember(), 60 * 60, TimeUnit.SECONDS);

        //홈 이동
        response.sendRedirect("/");
    }

}
