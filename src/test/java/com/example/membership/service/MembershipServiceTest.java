package com.example.membership.service;

import com.example.membership.dto.MembershipAddResponse;
import com.example.membership.dto.MembershipDetailResponse;
import com.example.membership.entity.Membership;
import com.example.membership.entity.MembershipRepository;
import com.example.membership.entity.MembershipType;
import com.example.membership.exception.MembershipErrorResult;
import com.example.membership.exception.MembershipException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MembershipServiceTest {

    @InjectMocks
    private MembershipService target;

    @Mock
    private MembershipRepository membershipRepository;

    private final String userId = "userId";
    private final MembershipType membershipType = MembershipType.NAVER;
    private final Integer point = 1000;

    @Test
    public void 멤버십등록실패_이미존재함() {
        //given
        doReturn(Membership.builder().build()).when(membershipRepository).findByUserIdAndMembershipType(userId, membershipType);

        //when
        final MembershipException result = assertThrows(MembershipException.class,
                () -> target.addMembership(userId, membershipType, point));

        //then
        assertEquals(result.getErrorResult(), MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
    }

    @Test
    public void 멤버십_등록_성공() {
        //given
        doReturn(null).when(membershipRepository).findByUserIdAndMembershipType(userId, membershipType);
        doReturn(membership()).when(membershipRepository).save(any(Membership.class));

        //when
        final MembershipAddResponse result = target.addMembership(userId, membershipType, point);

        //then
        assertNotNull(result.getId());
        assertEquals(MembershipType.NAVER, result.getMembershipType());

        //verify
        verify(membershipRepository, times(1)).findByUserIdAndMembershipType(userId, membershipType);
        verify(membershipRepository, times(1)).save(any(Membership.class));
    }

    private Membership membership() {
        return Membership.builder()
                .id(-1L)
                .userId(userId)
                .point(point)
                .membershipType(MembershipType.NAVER).build();
    }


    @Test
    public void 멤버십목록조회() {
        //given
        doReturn(Arrays.asList(
                Membership.builder().build(),
                Membership.builder().build(),
                Membership.builder().build()
        )).when(membershipRepository).findAllByUserId(userId);

        //when
        final List<MembershipDetailResponse> result = target.getMembershipList(userId);

        //then
        assertEquals(3, result.size());
    }


}