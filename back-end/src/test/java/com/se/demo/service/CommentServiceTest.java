package com.se.demo.service;

import com.se.demo.dto.CommentDTO;
import com.se.demo.entity.CommentEntity;
import com.se.demo.entity.IssueEntity;
import com.se.demo.entity.MemberEntity;
import com.se.demo.repository.CommentRepository;
import com.se.demo.repository.IssueRepository;
import com.se.demo.repository.MemberRepository;
import com.se.demo.service.CommentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest{

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private IssueRepository issueRepository;

    @InjectMocks
    private CommentService commentService;
    private MemberEntity member;
    private IssueEntity issue;
    private CommentEntity commentEntity;
    private CommentDTO commentDTO;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        member = new MemberEntity();
        member.setNickname("testUser");
        member.setPassword("1111");
        member.setUser_id(1);

        issue = new IssueEntity();
        issue.setId(4);
        issue.setTitle("Test Issue");
        issue.setDescription("Test description");
        issue.setState("new");

        commentEntity = new CommentEntity();
        commentEntity.setId(3);
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
    public void testSaveComment() {
        // Arrange
        when(memberRepository.findById(1)).thenReturn(Optional.of(member));
        when(issueRepository.findById(4)).thenReturn(Optional.of(issue));
        //when(commentRepository.save(any(CommentEntity.class))).thenReturn(commentEntity);




        //Use ArgumentCaptor to capture the CommentEntity being saved
        ArgumentCaptor<CommentEntity> captor = ArgumentCaptor.forClass(CommentEntity.class);
        //doAnswer(invocation -> invocation.getArgument(0)).when(commentRepository).save(captor.capture());


        // Act
        CommentEntity savedComment = commentService.save(commentDTO, "1", 4);

        // Assert
        assertNotNull(savedComment);
        assertEquals(commentDTO.getDescription(), savedComment.getDescription());
        assertEquals(member.getUser_id(), savedComment.getCreaterId().getUser_id());
        assertEquals(issue.getId(), savedComment.getIssue().getId());

        // Verify that commentRepository.save was called once with the captured argument
        //verify(commentRepository, times(1)).save(any(CommentEntity.class));
        verify(commentRepository, times(1)).save(captor.capture());

        CommentEntity capturedComment = captor.getValue();
        assertEquals(commentDTO.getDescription(), capturedComment.getDescription());
        assertEquals(member.getUser_id(), capturedComment.getCreaterId().getUser_id());
        assertEquals(issue.getId(), capturedComment.getIssue().getId());
    }


    @Test
    public void testFindAllByIssueId() {
        when(commentRepository.findByIssueId(4)).thenReturn(Arrays.asList(commentEntity));

        List<CommentDTO> comments = commentService.findAllByIssueId(4);

        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals(commentDTO.getDescription(), comments.get(1).getDescription());
        //verify(commentRepository, times(1)).findByIssueId(anyInt());
    }




    @Test
    public void testToCommentEntity() {
        CommentEntity convertedEntity = commentService.toCommentEntity(commentDTO, issue, member);

        assertNotNull(convertedEntity);
        assertEquals(commentDTO.getId(), convertedEntity.getId());
        assertEquals(commentDTO.getDescription(), convertedEntity.getDescription());
        assertEquals(commentDTO.getCreated_date(), convertedEntity.getCreatedDate());
        assertEquals(commentDTO.getCreater_id(), convertedEntity.getCreaterId().getUser_id());
        assertEquals(commentDTO.getIssue_id(), convertedEntity.getIssue().getId());
    }

    @Test
    public void testToCommentDTO() {
        CommentDTO convertedDTO = commentService.toCommentDTO(commentEntity);

        assertNotNull(convertedDTO);
        assertEquals(commentEntity.getId(), convertedDTO.getId());
        assertEquals(commentEntity.getDescription(), convertedDTO.getDescription());
        assertEquals(commentEntity.getCreatedDate(), convertedDTO.getCreated_date());
        assertEquals(commentEntity.getCreaterId().getUser_id(), convertedDTO.getCreater_id());
        assertEquals(commentEntity.getIssue().getId(), convertedDTO.getIssue_id());
    }
}
