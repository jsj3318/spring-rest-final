package com.nhnacademy.springrestfinal.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemberCreateCommand {
    private String id;
    private String name;
    private String password;
    private Integer age;
    private Role role;
}
