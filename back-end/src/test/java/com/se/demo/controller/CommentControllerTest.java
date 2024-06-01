package com.se.demo.controller;

import com.se.demo.dto.CommentDTO;
import com.se.demo.entity.CommentEntity;
import com.se.demo.entity.IssueEntity;
import com.se.demo.entity.MemberEntity;
import com.se.demo.service.CommentService;
import com.se.demo.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


//@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpSession httpSession;

    @InjectMocks

    private MemberEntity member;
    private IssueEntity issue;
    private CommentEntity commentEntity;
    private CommentDTO commentDTO;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(httpServletRequest.getSession()).thenReturn(httpSession); // getSession()이 호출될 때 HttpSession 객체 반환 설정
        when(httpSession.getAttribute("userNickname")).thenReturn("testUser"); // getSession().getAttribute()이 호출될 때 "testUser" 반환 설정
        member = new MemberEntity();
        member.setNickname("testUser");
        member.setPassword("1111");
        member.setUser_id(1);

        issue = new IssueEntity();
        issue.setId(1);
        issue.setTitle("Test Issue");
        issue.setDescription("Test description");
        issue.setState("new");

        commentEntity = new CommentEntity();
        commentEntity.setId(1);
        commentEntity.setDescription("Test description");
        commentEntity.setCreatedDate(LocalDateTime.now());
        commentEntity.setCreaterId(member);
        commentEntity.setIssue(issue);

        commentDTO = new CommentDTO();
        commentDTO.setId(1);
        commentDTO.setDescription("Test description");
        commentDTO.setCreated_date(LocalDateTime.now());
        commentDTO.setCreater_id(member.getUser_id());
        commentDTO.setIssue_id(issue.getId());
    }


    @Test
    void testGetCommentsForIssue() {
        // Arrange
        int issueId = 1;
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(1);
        commentDTO.setDescription("Test comment");

        when(commentService.findAllByIssueId(issueId)).thenReturn(Collections.singletonList(commentDTO));

        // Act
        ResponseEntity<List<CommentDTO>> responseEntity = commentController.getCommentsForIssue(issueId);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<CommentDTO> comments = responseEntity.getBody();
        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals(commentDTO.getDescription(), comments.get(0).getDescription());
    }


    @Test
    void testSaveComment() {
       /* // Arrange
        CommentDTO requestDTO = new CommentDTO();
        requestDTO.setIssue_id(1);
        requestDTO.setDescription("Test comment");
       // requestDTO.setCreated_date(now());
       // requestDTO.setId(1);
       // requestDTO.setCreater_id(1);

        CommentEntity savedCommentEntity = new CommentEntity();
        savedCommentEntity.setId(1);
        savedCommentEntity.setDescription("Test comment");

        when(commentService.save(any(CommentDTO.class), any(String.class), any(Integer.class))).thenReturn(savedCommentEntity);

        // Act
        ResponseEntity<?> responseEntity = commentController.saveComment(requestDTO, request);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        CommentDTO savedComment = (CommentDTO) responseEntity.getBody();
        assertNotNull(savedComment);
        assertEquals(savedCommentEntity.getId(), savedComment.getId());
        assertEquals(savedCommentEntity.getDescription(), savedComment.getDescription());*/
        // Arrange
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute("userNickname")).thenReturn("testUser");
        when(commentService.save(any(CommentDTO.class), anyString(), anyInt())).thenReturn(commentEntity);
        when(commentService.toCommentDTO(any(CommentEntity.class))).thenReturn(commentDTO);

        // Act
        ResponseEntity<?> response = commentController.saveComment(commentDTO, httpServletRequest);

        // Assert
        assertNotNull(response);
        assertEquals(ResponseEntity.class, response.getClass());
        assertEquals(commentDTO, response.getBody());

    }
}



