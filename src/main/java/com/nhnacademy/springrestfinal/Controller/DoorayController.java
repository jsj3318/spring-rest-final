package com.nhnacademy.springrestfinal.Controller;

import com.nhnacademy.springrestfinal.client.DooraySendClient;
import com.nhnacademy.springrestfinal.domain.MessagePayload;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DoorayController {
    private final DooraySendClient dooraySendClient;

    // 단순 테스트용 두레이 메세지 보내기용 post
    @PostMapping("/dooray/{serviceId}/{botId}/{botToken}")
    public String sendMessage(
            @RequestBody MessagePayload messagePayload,
            @PathVariable Long serviceId,
            @PathVariable Long botId,
            @PathVariable String botToken
    ) {
        return dooraySendClient.sendMessage(messagePayload, serviceId, botId, botToken);
    }

}
