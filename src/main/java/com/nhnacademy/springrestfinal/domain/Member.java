package com.nhnacademy.springrestfinal.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Member {
    private String id;
    private String name;
    private String password;
    private Integer age;
    private Role role;

    @JsonCreator
    public Member(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("password") String password,
            @JsonProperty("age") Integer age,
            @JsonProperty("role") Role role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.age = age;
        this.role = role;
    }

}
