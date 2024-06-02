package com.se.demo.controller;

import com.se.demo.dto.MemberDTO;
import com.se.demo.entity.MemberEntity;
import com.se.demo.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("MemberController test")
class MemberControllerTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberController memberController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    @DisplayName("회원가입 성공")
    @Test
    void testSignupSuccess() throws Exception {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setNickname("testuser");
        memberDTO.setPassword("password");

        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberEntity.setUser_id(1);

        when(memberService.checkId("testuser")).thenReturn(true);
        when(memberService.signup(any(MemberDTO.class))).thenReturn(memberEntity);

        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nickname\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(memberService, times(1)).checkId("testuser");
        verify(memberService, times(1)).signup(any(MemberDTO.class));
    }

    @DisplayName("회원가입 실패")
    @Test
    void testSignupFail() throws Exception {
        when(memberService.checkId("testuser")).thenReturn(false);

        mockMvc.perform(post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nickname\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("ID already in use"));

        verify(memberService, times(1)).checkId("testuser");
        verify(memberService, times(0)).signup(any(MemberDTO.class));
    }

    @DisplayName("로그인 성공")
    @Test
    void testLoginSuccess() throws Exception {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setNickname("testuser");
        memberDTO.setPassword("password");

        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberEntity.setUser_id(1);

        when(memberService.login(any(MemberDTO.class))).thenReturn(memberEntity);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);

        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nickname\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.session_id").isNotEmpty())
                .andExpect(jsonPath("$.user_id").value(1));

        verify(memberService, times(1)).login(any(MemberDTO.class));
    }

    @DisplayName("로그인 실패")
    @Test
    void testLoginFail() throws Exception {
        when(memberService.login(any(MemberDTO.class))).thenReturn(null);

        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nickname\":\"testuser\",\"password\":\"wrongpassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("로그인 아이디 또는 비밀번호가 틀렸습니다."));

        verify(memberService, times(1)).login(any(MemberDTO.class));
    }
}
