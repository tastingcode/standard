package com.shop.entity.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("Auditing test")
    @WithMockUser(username = "gildong", roles = "USER")
    public void auditingTest(){
        Member newMember = Member.builder()
                .build();
        memberRepository.save(newMember);

        em.flush();
        em.clear();

        Member findMember = memberRepository.findById(newMember.getId())
                .orElseThrow(EntityNotFoundException::new);

        System.out.println("findMember.getRegTime() = " + findMember.getRegTime());
        System.out.println("findMember.getUpdateTime() = " + findMember.getUpdateTime());
        System.out.println("findMember.getCreatedBy() = " + findMember.getCreatedBy());
        System.out.println("findMember.getModifiedBy() = " + findMember.getModifiedBy());

    }

}