package com.nhnacademy.springrestfinal.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@AllArgsConstructor
public class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 로그인 성공시 기존 세션 무효화
        // id 비번 입력으로 로그인을 하고나면 세션과 쿠키가 유지된 채로 뒤로가기를 해서 로그인으로 간 뒤
        // 구글 로그인을 하면 구글 로그인이 무시되고 기존 로그인이 유지된다
        // 로그아웃을 한 것처럼 세션과 쿠키를 지운다

        // 로그아웃 핸들러에서 하는 일과 똑같으니 가져다 써야겠다
        CustomLogoutHandler logoutHandler = new CustomLogoutHandler(redisTemplate);
        logoutHandler.logout(request, response, authentication);

        // 세션의 아이디를 새로 바꾼다
        request.changeSessionId();

        //홈 이동
        response.sendRedirect("/");
    }

}
