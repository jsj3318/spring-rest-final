package com.nhnacademy.springrestfinal.domain;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

@Getter
public class AcademyOAuthUser implements OAuth2User {
    private String id;
    private String name;
    private String email;
    private Role role;
    private Map<String, Object> attributes;

    public AcademyOAuthUser(OAuth2User oAuth2User) {
        id = (String) oAuth2User.getName();
        name = (String) oAuth2User.getAttribute("name");
        email = (String) oAuth2User.getAttribute("email");
        role = Role.GOOGLE;
        attributes = oAuth2User.getAttributes();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = "ROLE_" + this.role;
        return Arrays.asList(new SimpleGrantedAuthority(role));
    }

}
