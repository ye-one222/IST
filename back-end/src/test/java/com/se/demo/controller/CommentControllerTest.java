package com.se.demo.controller;

import com.se.demo.IssueTrackingApplication;
import com.se.demo.dto.CommentDTO;
import com.se.demo.dto.ResponseCommentDTO;
import com.se.demo.entity.CommentEntity;
import com.se.demo.entity.IssueEntity;
import com.se.demo.entity.MemberEntity;
import com.se.demo.repository.MemberRepository;
import com.se.demo.service.CommentService;
import com.se.demo.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = IssueTrackingApplication.class)
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private MemberRepository memberRepository;

    private MockHttpSession session;

    private CommentDTO commentDTO;
    private MemberEntity memberEntity;
    private IssueEntity issueEntity;
    private CommentEntity commentEntity;
    private ResponseCommentDTO responseCommentDTO;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        session.setAttribute("userNickname", "testUser");

        commentDTO = new CommentDTO();
        commentDTO.setId(1);
        commentDTO.setDescription("Test comment");
        commentDTO.setCreated_date(LocalDateTime.now());
        commentDTO.setIssue_id(1);
        commentDTO.setCreater_id(1);

        memberEntity = new MemberEntity();
        memberEntity.setUser_id(1);
        memberEntity.setNickname("testUser");

        issueEntity = new IssueEntity();
        issueEntity.setId(1);
        issueEntity.setTitle("Test issue");
        issueEntity.setAssigneeId(1); // assigneeId 설정

        commentEntity = new CommentEntity();
        commentEntity.setId(1);
        commentEntity.setDescription("Test comment");
        commentEntity.setCreatedDate(LocalDateTime.now());
        commentEntity.setCreaterId(memberEntity);
        commentEntity.setIssue(issueEntity);

        responseCommentDTO = new ResponseCommentDTO(commentDTO, memberEntity.getNickname());
    }

    @Test
    void getCommentsForIssue() throws Exception {
        List<ResponseCommentDTO> comments = Arrays.asList(responseCommentDTO);
        when(commentService.findAllByIssueId(anyInt())).thenReturn(comments);

        mockMvc.perform(get("/api/issue/1/comments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].commentDTO.description").value("Test comment"))
                .andExpect(jsonPath("$[0].creater_nickname").value("testUser"));
    }

    @Test
    void saveComment() throws Exception {
        when(commentService.save(Mockito.any(CommentDTO.class), anyInt())).thenReturn(commentEntity);
        when(commentService.toCommentDTO(Mockito.any(CommentEntity.class))).thenReturn(commentDTO);

        mockMvc.perform(post("/api/comments/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\": \"Test comment\", \"issue_id\": 1, \"creater_id\": 1}")
                        .session(session))  // 세션 추가
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description").value("Test comment"));
    }
}