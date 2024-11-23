package com.nhnacademy.springrestfinal.filter;

import com.nhnacademy.springrestfinal.domain.AcademyUser;
import com.nhnacademy.springrestfinal.domain.Member;
import com.nhnacademy.springrestfinal.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class UserAuthenticationFilter extends OncePerRequestFilter {

    private RedisTemplate<String, Object> redisTemplate;
    private MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 쿠키에 sessionid 가 저장되어 있는지 검사
        String sessionId = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("SESSION")) {
                    sessionId = cookie.getValue();
                }
            }
        }

        // 세션쿠키가 존재한다 -> 로그인을 했었다
        if(sessionId != null) {
            Object o = redisTemplate.opsForValue().get(sessionId);
            String id = (String) o;
            if(id != null) {
                Member member = memberService.findById(id);
                AcademyUser academyUser = new AcademyUser(member);
                Authentication auth = new PreAuthenticatedAuthenticationToken(academyUser, null, academyUser.getAuthorities());
                auth.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }

}
