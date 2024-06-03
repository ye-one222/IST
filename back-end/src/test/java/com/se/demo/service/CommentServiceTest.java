package com.se.demo.service;

import com.se.demo.IssueTrackingApplication;
import com.se.demo.dto.CommentDTO;
import com.se.demo.dto.ResponseCommentDTO;
import com.se.demo.entity.CommentEntity;
import com.se.demo.entity.IssueEntity;
import com.se.demo.entity.MemberEntity;
import com.se.demo.repository.CommentRepository;
import com.se.demo.repository.IssueRepository;
import com.se.demo.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = IssueTrackingApplication.class)
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private IssueRepository issueRepository;

    private CommentDTO commentDTO;
    private MemberEntity memberEntity;
    private IssueEntity issueEntity;
    private CommentEntity commentEntity;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    void saveComment() {
        when(memberRepository.findById(anyInt())).thenReturn(Optional.of(memberEntity));
        when(issueRepository.findById(anyInt())).thenReturn(Optional.of(issueEntity));
        when(commentRepository.save(any(CommentEntity.class))).thenReturn(commentEntity);

        CommentEntity savedComment = commentService.save(commentDTO, 1);

        assertNotNull(savedComment);
        assertEquals("Test comment", savedComment.getDescription());
    }

    @Test
    void findAllByIssueId() {
        when(commentRepository.findByIssueId(anyInt())).thenReturn(Arrays.asList(commentEntity));
        when(memberRepository.findById(anyInt())).thenReturn(Optional.of(memberEntity)); // Ensure this line exists

        List<ResponseCommentDTO> comments = commentService.findAllByIssueId(1);

        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals("Test comment", comments.get(0).getCommentDTO().getDescription());
        assertEquals("testUser", comments.get(0).getCreater_nickname());
    }
}
