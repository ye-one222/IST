package com.se.demo.service;

import com.se.demo.dto.MemberDTO;
import com.se.demo.entity.MemberEntity;
import com.se.demo.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignup() {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setNickname("testuser");
        memberDTO.setPassword("password");

        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        when(memberRepository.save(any(MemberEntity.class))).thenReturn(memberEntity);

        MemberEntity result = memberService.signup(memberDTO);

        assertNotNull(result);
        assertEquals("testuser", result.getNickname());
        verify(memberRepository, times(1)).save(any(MemberEntity.class));
    }

    @Test
    void testLoginSuccess() {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setNickname("testuser");
        memberDTO.setPassword("password");

        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        when(memberRepository.findByNickname("testuser")).thenReturn(Optional.of(memberEntity));

        MemberEntity result = memberService.login(memberDTO);

        assertNotNull(result);
        assertEquals("testuser", result.getNickname());
        verify(memberRepository, times(1)).findByNickname("testuser");
    }

    @Test
    void testLoginFail() {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setNickname("testuser");
        memberDTO.setPassword("wrongpassword");

        when(memberRepository.findByNickname("testuser")).thenReturn(Optional.empty());

        MemberEntity result = memberService.login(memberDTO);

        assertNull(result);
        verify(memberRepository, times(1)).findByNickname("testuser");
    }

    @Test
    void testCheckId() {
        when(memberRepository.existsByNickname("testuser")).thenReturn(true);
        boolean result = memberService.checkId("testuser");
        assertFalse(result);

        when(memberRepository.existsByNickname("newuser")).thenReturn(false);
        result = memberService.checkId("newuser");
        assertTrue(result);
    }
}
