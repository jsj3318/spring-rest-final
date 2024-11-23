package com.nhnacademy.springrestfinal.filter;

import com.nhnacademy.springrestfinal.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class CheckBlockFilter extends OncePerRequestFilter {
    private MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 로그인 post 요청일 때만 검사
        if ("/login/process".equals(request.getServletPath()) && "POST".equalsIgnoreCase(request.getMethod())) {
            String id = request.getParameter("id");

            // 차단된 아이디 인지 확인
            if(memberService.isBlocked(id)){
                throw new RuntimeException(id + " : 차단된 사용자 입니다.");
            }
        }

        filterChain.doFilter(request, response);
    }
}


