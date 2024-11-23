package com.nhnacademy.springrestfinal.client;

import com.nhnacademy.springrestfinal.domain.MessagePayload;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="DooraySendClient", url = "https://hook.dooray.com/services")
public interface DooraySendClient {
    @PostMapping("/{serviceId}/{botId}/{botToken}")
    public String sendMessage(@RequestBody MessagePayload messagePayload,
                              @PathVariable Long serviceId,
                              @PathVariable Long botId,
                              @PathVariable String botToken);
}

//https://hook.dooray.com/services/3204376758577275363/3943038317970916921/yXGOie6dRg-oDZiZxzHR5Q