package com.nhnacademy.springrestfinal.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;

@Getter
public class GoogleMember {
    private String id;
    private String name;
    private String email;
    private Map<String, Object> attributes;

    @JsonCreator
    public GoogleMember(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("attributes") Map<String, Object> attributes) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.attributes = attributes;
    }

}
