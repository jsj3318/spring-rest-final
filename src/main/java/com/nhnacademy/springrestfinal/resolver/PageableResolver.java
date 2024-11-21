package com.nhnacademy.springrestfinal.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PageableResolver implements HandlerMethodArgumentResolver {


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Pageable.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        String pageParam = httpServletRequest.getParameter("page");
        String sizeParam = httpServletRequest.getParameter("size");

        if(pageParam == null) {
            pageParam = "0";
        }

        if(sizeParam == null) {
            sizeParam = "5";
        }

        int page = Integer.parseInt(pageParam);
        int size = Integer.parseInt(sizeParam);

        if(size > 10){
            size = 10;
        }

        return PageRequest.of(page, size);
    }
}