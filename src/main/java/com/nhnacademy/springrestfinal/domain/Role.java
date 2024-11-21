package com.nhnacademy.springrestfinal.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    ADMIN, MEMBER, GOOGLE;

    @JsonCreator
    public static Role fromString(String str) {
        for(Role role : Role.values()) {
            if(role.name().equalsIgnoreCase(str)) {
                return role;
            }
        }
        return MEMBER;
    }

    @JsonValue
    public String toJson(){
        return this.name().toLowerCase();
    }

}
