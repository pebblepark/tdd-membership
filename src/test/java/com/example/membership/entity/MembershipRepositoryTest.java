package com.example.membership.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

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
                .membershipType(MembershipType.NAVER)
                .point(1000)
                .build();

        //when
        final Membership result = membershipRepository.save(membership);

        //then
        assertNotNull(result.getId());
        assertEquals("userId", result.getUserId());
        assertEquals(MembershipType.NAVER, result.getMembershipType());
        assertEquals(1000, result.getPoint());
    }

    @Test
    public void 멤버십이_존재하는지_테스트() {
        //given
        멤버십등록();

        //when
        final Membership findResult = membershipRepository.findByUserIdAndMembershipType("userId", MembershipType.NAVER);

        //then
        assertNotNull(findResult);
        assertNotNull(findResult.getId());
        assertEquals("userId", findResult.getUserId());
        assertEquals(MembershipType.NAVER, findResult.getMembershipType());
        assertEquals(1000, findResult.getPoint());
    }

    @Test
    public void 멤버십조회_사이즈가0() {
        //given

        //when
        List<Membership> result = membershipRepository.findAllByUserId("userId");

        //then
        assertEquals(0, result.size());
    }

    @Test
    public void 멤버십조회_사이즈가2() {
        //given
        final Membership naverMembership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();

        final Membership kakaoMembership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.KAKAO)
                .point(10000)
                .build();

        membershipRepository.save(naverMembership);
        membershipRepository.save(kakaoMembership);

        //when
        List<Membership> result = membershipRepository.findAllByUserId("userId");

        //then
        assertEquals(2, result.size());
    }

    @Test
    public void 멤버십추가후삭제() {
        //given
        final Membership naverMembership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();

        final Membership savedMembership = membershipRepository.save(naverMembership);

        //when
        membershipRepository.deleteById(savedMembership.getId());

        //then
    }
}