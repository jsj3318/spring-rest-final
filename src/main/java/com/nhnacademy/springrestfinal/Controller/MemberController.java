package com.nhnacademy.springrestfinal.Controller;

import com.nhnacademy.springrestfinal.domain.Member;
import com.nhnacademy.springrestfinal.domain.MemberCreateCommand;
import com.nhnacademy.springrestfinal.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity addMember(
            @RequestBody MemberCreateCommand memberCreateCommand
            ) {
        memberService.save(new Member(
                memberCreateCommand.getId(),
                memberCreateCommand.getName(),
                memberCreateCommand.getPassword(),
                memberCreateCommand.getAge(),
                memberCreateCommand.getRole()
        ));

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<Member> getAllMembers(
            Pageable pageable
    ) {
        return memberService.findAll(pageable).getContent();
    }

    @GetMapping("/{memberId}")
    public Member getMember(
            @PathVariable String memberId
    ){
        return memberService.findById(memberId);
    }

}
