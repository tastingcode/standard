package com.shop.service.member;

import com.shop.dto.member.MemberFormDto;
import com.shop.entity.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 동작구 대방동");
        memberFormDto.setPassword("1234");

        return Member.createMember(memberFormDto, passwordEncoder);

    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest() throws Exception {
        //given
        Member member = createMember();

        //when
        Member savedMember = memberService.saveMember(member);

        //then
        assertThat(member.getEmail()).isEqualTo(savedMember.getEmail());

    }

    @Test
    @DisplayName("중복 회원 가입 테스트")
    public void saveDuplicateMemberTest() {
        //given
        Member member1 = createMember();
        Member member2 = createMember();

        //when
        memberService.saveMember(member1);
        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(member2);
        });

        //then
        assertThat("이미 가입된 회원입니다.").isEqualTo(e.getMessage());
        System.out.println("e.getMessage() = " + e.getMessage());

    }

}