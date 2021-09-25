package com.example.membership.dto;

import com.example.membership.common.ValidationGroups.MembershipAccumulateMaker;
import com.example.membership.common.ValidationGroups.MembershipAddMaker;
import com.example.membership.entity.MembershipType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class MembershipRequest {

    @NotNull(groups = {MembershipAddMaker.class, MembershipAccumulateMaker.class})
    @Min(value = 0, groups = {MembershipAddMaker.class, MembershipAccumulateMaker.class})
    private final Integer point;

    @NotNull(groups = {MembershipAddMaker.class})
    private final MembershipType membershipType;
}
