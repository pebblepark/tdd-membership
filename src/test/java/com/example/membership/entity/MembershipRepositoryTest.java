package com.example.membership.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MembershipRepositoryTest {

    @Autowired
    private MembershipRepository membershipRepository;

    @Test
    public void 멤버십등록() {
        //given
        final Membership membership = Membership.builder()
                .userId("userId")
                .membershipName("네이버")
                .point(1000)
                .build();

        //when
        final Membership result = membershipRepository.save(membership);

        //then
        assertNotNull(result.getId());
        assertEquals("userId", result.getUserId());
        assertEquals("네이버", result.getMembershipName());
        assertEquals(1000, result.getPoint());

    }
}