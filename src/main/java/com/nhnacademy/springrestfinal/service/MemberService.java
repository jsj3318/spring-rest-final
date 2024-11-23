package com.nhnacademy.springrestfinal.service;

import com.nhnacademy.springrestfinal.client.DooraySendClient;
import com.nhnacademy.springrestfinal.domain.Member;
import com.nhnacademy.springrestfinal.domain.MessagePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.member;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {
    private final RedisTemplate<String ,Object> redisTemplate;
    private final PasswordEncoder passwordEncoder;
    private final DooraySendClient dooraySendClient;

    private String HASH_NAME = "Member:";
    private String BLOCK_NAME = "BlockedMember:";

    public Member save(Member member) {
        // 이미 존재하는 id 확인
        if(redisTemplate.opsForHash().hasKey(HASH_NAME, member.getId())) {
            throw new KeyAlreadyExistsException(HASH_NAME + " " + member.getId() + " already exists");
        }

        // 패스워드 암호화
        member.passwordEncode(passwordEncoder);

        redisTemplate.opsForHash().put(HASH_NAME, member.getId(), member);
        return member;
    }

    public Member findById(String id) {
        Member member = (Member) redisTemplate.opsForHash().get(HASH_NAME, id);

        // 존재하지 않는 멤버인지 확인
        if(member == null) {
            throw new IllegalArgumentException(HASH_NAME + " " + id + " not found");
        }

        return member;
    }

    public Page<Member> findAll(Pageable pageable) {
        // 전달 받은 페이저블 이용해서 페이징 처리

        List<Object> objectList = redisTemplate.opsForHash().values(HASH_NAME);

        List<Member> memberList = objectList.stream()
                .filter(Member.class::isInstance)
                .map(Member.class::cast)
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), memberList.size());

        List<Member> pagedList = (start <= end) ? memberList.subList(start, end) : new ArrayList<>();

        return new PageImpl<>(pagedList, pageable, memberList.size());
    }


    // 차단 여부
    public boolean isBlocked(String id) {
        return redisTemplate.opsForList().range(BLOCK_NAME, 0, -1).contains(id);
    }

    // 유저 차단 하고 두레이 알림
    public void block(String id) {
        redisTemplate.opsForList().rightPush(BLOCK_NAME, id);
        log.info("{} : 유저 차단 됨", id);

        // 메신저 알림
        // https://hook.dooray.com/services/3204376758577275363/3943038317970916921/yXGOie6dRg-oDZiZxzHR5Q
        Member member = findById(id);
        MessagePayload messagePayload = new MessagePayload("유저 차단 알림",
                member.getName() + "(" + member.getId() + ") 유저 차단 됨");
        dooraySendClient.sendMessage(messagePayload, 3204376758577275363L, 3943038317970916921L, "yXGOie6dRg-oDZiZxzHR5Q");

    }

    // 유저 차단 해제
    public void unblock(String id) {
        redisTemplate.opsForList().remove(BLOCK_NAME,1, id);
        log.info("{} : 유저 차단 해제 됨", id);
    }

}
