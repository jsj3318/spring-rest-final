package com.nhnacademy.springrestfinal.handler;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LoginFailCounter {
    private Map<String, Integer> loginFailCounter = new ConcurrentHashMap<>();

    int getCount(String id){
        return loginFailCounter.get(id);
    }
    void increment(String id){
        loginFailCounter.compute(id, (key, value) -> value == null ? 1 : value + 1);
    }
    void reset(String id){
        loginFailCounter.remove(id);
    }
}
