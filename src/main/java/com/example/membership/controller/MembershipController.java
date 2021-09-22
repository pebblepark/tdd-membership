package com.example.membership.controller;

import com.example.membership.dto.MembershipRequest;
import com.example.membership.dto.MembershipResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.membership.common.MembershipConstants.USER_ID_HEADER;

@RestController
@RequestMapping("/api/v1")
public class MembershipController {

    @PostMapping("/membership")
    public ResponseEntity<MembershipResponse> addMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @RequestBody @Valid final MembershipRequest membershipRequest) {

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
