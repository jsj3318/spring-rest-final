package com.nhnacademy.springrestfinal.handler;

import com.nhnacademy.springrestfinal.domain.AcademyUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.UUID;

@AllArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private RedisTemplate<String, Object> redisTemplate;
    private LoginFailCounter loginFailCounter;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 로그인 성공 시 발생하는 이벤트

        // 카운터 초기화
        String id = request.getParameter("id");
        loginFailCounter.reset(id);

        // 랜덤 세션 아이디를 생성해서 쿠키에 저장
        String sessionId = UUID.randomUUID().toString();
        Cookie sessionCookie = new Cookie("SESSION", sessionId);
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true);
        sessionCookie.setMaxAge(60 * 60); // 1시간
        response.addCookie(sessionCookie);

        // 레디스에 세션 아이디 - 유저 아이디로 저장
        AcademyUser academyUser = (AcademyUser) authentication.getPrincipal();
        redisTemplate.opsForValue().set(sessionId, academyUser.getId());

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
