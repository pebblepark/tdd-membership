package com.example.membership.controller;

import com.example.membership.dto.MembershipRequest;
import com.example.membership.dto.MembershipResponse;
import com.example.membership.entity.MembershipType;
import com.example.membership.exception.MembershipErrorResult;
import com.example.membership.exception.MembershipException;
import com.example.membership.service.MembershipService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static com.example.membership.common.MembershipConstants.USER_ID_HEADER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MembershipControllerTest {

    @InjectMocks
    private MembershipController target;

    @Mock
    private MembershipService membershipService;

    private MockMvc mockMvc;
    private Gson gson;

    @BeforeEach
    public void init(){
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(target).build();
    }

    @Test
    public void 멤버십등록실패_사용자식별값이헤더에없음() throws Exception {
        //given
        final String url = "/api/v1/membership";

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버십등록실패_포인트가null() throws Exception {
        //given
        final String url = "/api/v1/membership";

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                .header(USER_ID_HEADER, "12345")
                .content(gson.toJson(membershipRequest(null, MembershipType.NAVER)))
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버십등록실패_포인트가음수() throws Exception {
        //given
        final String url = "/api/v1/membership";

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(-1, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버십등록실패_멤버십종류가null() throws Exception {
        //given
        final String url = "/api/v1/membership";

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(10000, null)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버십등록실패_MembershipService에서ExceptionThrow() throws Exception {
        //given
        final String url = "/api/v1/membership";
        doThrow(new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER))
                .when(membershipService)
                .addMembership("12345", MembershipType.NAVER, 10000);

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버십등록성공() throws Exception {
        //given
        final String url = "/api/v1/membership";
        final String userId = "12345";
        final MembershipType membershipType = MembershipType.NAVER;
        final Integer point = 10000;

        final MembershipResponse membershipResponse = MembershipResponse.builder()
                .id(-1L)
                .membershipType(membershipType)
                .build();
        doReturn(membershipResponse).when(membershipService).addMembership(userId, membershipType, point);

        //when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, userId)
                        .content(gson.toJson(membershipRequest(point, membershipType)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isCreated());

        final MembershipResponse response = gson.fromJson(resultActions.andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8), MembershipResponse.class);

        assertNotNull(response.getId());
        assertEquals(membershipType, response.getMembershipType());
    }

    private MembershipRequest membershipRequest(final Integer point, final MembershipType membershipType) {
        return MembershipRequest.builder()
                .point(point)
                .membershipType(membershipType)
                .build();
    }

}