package com.example.membership.controller;

import com.example.membership.common.DefaultRestController;
import com.example.membership.dto.MembershipRequest;
import com.example.membership.dto.MembershipResponse;
import com.example.membership.entity.Membership;
import com.example.membership.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.membership.common.MembershipConstants.USER_ID_HEADER;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MembershipController extends DefaultRestController {

    private final MembershipService membershipService;

    @PostMapping("/membership")
    public ResponseEntity<MembershipResponse> addMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @RequestBody @Valid final MembershipRequest membershipRequest) {

         final MembershipResponse membershipResponse = membershipService.addMembership(userId, membershipRequest.getMembershipType(), membershipRequest.getPoint());

        return ResponseEntity.status(HttpStatus.CREATED).body(membershipResponse);
    }
}
