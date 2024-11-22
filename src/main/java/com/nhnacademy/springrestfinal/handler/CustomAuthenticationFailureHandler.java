package com.nhnacademy.springrestfinal.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

@AllArgsConstructor
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private RedisTemplate<String, Object> redisTemplate;
    private LoginFailCounter loginFailCounter;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 로그인 실패 시 발생하는 이벤트

        // 카운터 1 증가
        String id = request.getParameter("id");
        loginFailCounter.increment(id);

        // 카운터 5회가 되면 차단하고 메신저 알림
        if(loginFailCounter.getCount(id) >= 5){

            // 아이디 접속 차단


            // 메신저 알림


        }

        response.sendRedirect("/auth/login?error=true");
    }
}
