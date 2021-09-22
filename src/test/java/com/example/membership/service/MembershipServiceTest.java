package com.example.membership.service;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

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
                () -> target.addMembership(userId,membershipType, point));

        //then
        assertEquals(result.getErrorResult(), MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
    }


}