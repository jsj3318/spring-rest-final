package com.nhnacademy.springrestfinal.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@AllArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // SESSION 아이디 가져와서 레디스에서 제거
        Cookie[] cookies = request.getCookies();
        String sessionId = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("SESSION")) {
                sessionId = cookie.getValue();
            }
        }

        redisTemplate.delete(sessionId);

        // SESSION 쿠키 제거
        Cookie cookie = new Cookie("SESSION", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

    }
}
