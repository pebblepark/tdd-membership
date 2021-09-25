package com.example.membership.service;

import com.example.membership.dto.MembershipAddResponse;
import com.example.membership.dto.MembershipDetailResponse;
import com.example.membership.entity.Membership;
import com.example.membership.entity.MembershipRepository;
import com.example.membership.entity.MembershipType;
import com.example.membership.exception.MembershipErrorResult;
import com.example.membership.exception.MembershipException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final PointService ratePointService;

    @Transactional
    public MembershipAddResponse addMembership(final String userId, final MembershipType membershipType, final Integer point) {
        final Membership result = membershipRepository.findByUserIdAndMembershipType(userId, membershipType);

        if(result != null) {
            throw new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
        }

        Membership membership = Membership.builder()
                .userId(userId)
                .membershipType(membershipType)
                .point(point)
                .build();

        final Membership savedMembership = membershipRepository.save(membership);

        return MembershipAddResponse.builder()
                .id(savedMembership.getId())
                .membershipType(savedMembership.getMembershipType())
                .build();
    }

    public List<MembershipDetailResponse> getMembershipList(final String userId) {
        final List<Membership> membershipList =  membershipRepository.findAllByUserId(userId);

        return membershipList.stream()
                .map(membership -> MembershipDetailResponse.builder()
                        .id(membership.getId())
                        .membershipType(membership.getMembershipType())
                        .point(membership.getPoint())
                        .createdAt(membership.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public MembershipDetailResponse getMembership(final String userId, final MembershipType membershipType) {
        final Membership membership = membershipRepository.findByUserIdAndMembershipType(userId, membershipType);

        if(membership == null) {
            throw new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
        }

        return MembershipDetailResponse.builder()
                .id(membership.getId())
                .membershipType(membership.getMembershipType())
                .point(membership.getPoint())
                .createdAt(membership.getCreatedAt())
                .build();
    }

    @Transactional
    public void removeMembership(final Long membershipId, final String userId) {
        final Optional<Membership> optionalMembership = membershipRepository.findById(membershipId);
        final Membership membership = optionalMembership
                .orElseThrow(() -> new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND));
        if(!membership.getUserId().equals(userId)) {
            throw new MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
        }
        membershipRepository.deleteById(membershipId);
    }

    @Transactional
    public void accumulateMembershipPoint(final Long membershipId, final String userId, final int amount) {
        final Optional<Membership> optionalMembership = membershipRepository.findById(membershipId);
        final Membership membership = optionalMembership
                .orElseThrow(() -> new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND));
        if(!membership.getUserId().equals(userId)) {
            throw new MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
        }

        final int additionalAmount = ratePointService.calculateAmount(amount);
        membership.addPoint(additionalAmount);
    }
}
