package com.example.membership.controller;

import com.example.membership.common.DefaultRestController;
import com.example.membership.common.ValidationGroups.MembershipAccumulateMaker;
import com.example.membership.common.ValidationGroups.MembershipAddMaker;
import com.example.membership.dto.MembershipAddResponse;
import com.example.membership.dto.MembershipDetailResponse;
import com.example.membership.dto.MembershipRequest;
import com.example.membership.entity.MembershipType;
import com.example.membership.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.membership.common.MembershipConstants.USER_ID_HEADER;

@RestController
@RequiredArgsConstructor
public class MembershipController extends DefaultRestController {

    private final MembershipService membershipService;

    @PostMapping("/api/v1/membership")
    public ResponseEntity<MembershipAddResponse> addMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @RequestBody @Validated(MembershipAddMaker.class) final MembershipRequest membershipRequest) {

         final MembershipAddResponse membershipAddResponse = membershipService.addMembership(userId, membershipRequest.getMembershipType(), membershipRequest.getPoint());

        return ResponseEntity.status(HttpStatus.CREATED).body(membershipAddResponse);
    }

    @GetMapping("/api/v1/membership/list")
    public ResponseEntity<List<MembershipDetailResponse>> getMembershipList(@RequestHeader(USER_ID_HEADER) final String userId) {
        return ResponseEntity.ok(membershipService.getMembershipList(userId));
    }

    @GetMapping("/api/v1/membership")
    public ResponseEntity<MembershipDetailResponse> getMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @RequestParam final MembershipType membershipType) {
        return ResponseEntity.ok(membershipService.getMembership(userId, membershipType));
    }

    @DeleteMapping("/api/v1/membership/{membershipId}")
    public ResponseEntity<Void> deleteMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @PathVariable final Long membershipId) {

        membershipService.removeMembership(membershipId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/v1/membership/{membershipId}/accumulate")
    public ResponseEntity<Void> accumulateMembershipPoint(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @PathVariable final Long membershipId,
            @RequestBody @Validated(MembershipAccumulateMaker.class) final MembershipRequest membershipRequest) {

        membershipService.accumulateMembershipPoint(membershipId, userId, membershipRequest.getPoint());
        return ResponseEntity.noContent().build();
    }


}
