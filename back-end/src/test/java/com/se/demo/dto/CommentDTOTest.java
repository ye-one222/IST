package com.se.demo.dto;

import com.se.demo.entity.CommentEntity;
import com.se.demo.entity.IssueEntity;
import com.se.demo.entity.MemberEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentDTOTest {

    @Test
    public void testNoArgsConstructor() {
        CommentDTO commentDTO = new CommentDTO();
        Assertions.assertNotNull(commentDTO);
    }

    @Test
    public void testEntityConstructor() {
        // Set up MemberEntity
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setUser_id(1);

        // Set up IssueEntity
        IssueEntity issueEntity = new IssueEntity();
        issueEntity.setId(1);

        // Set up CommentEntity
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(1);
        commentEntity.setCreaterId(memberEntity);
        commentEntity.setDescription("Test description");
        commentEntity.setCreatedDate(LocalDateTime.now());
        commentEntity.setIssue(issueEntity);

        // Create CommentDTO from CommentEntity
        CommentDTO commentDTO = new CommentDTO(commentEntity);

        // Assert values
        Assertions.assertEquals(1, commentDTO.getId());
        Assertions.assertEquals(1, commentDTO.getCreater_id());
        Assertions.assertEquals("Test description", commentDTO.getDescription());
        Assertions.assertNotNull(commentDTO.getCreated_date()); // Since it's set to LocalDateTime.now()
        Assertions.assertEquals(1, commentDTO.getIssue_id());
    }

    @Test
    public void testGettersAndSetters() {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(1);
        commentDTO.setCreater_id(2);
        commentDTO.setDescription("Sample description");
        commentDTO.setCreated_date(LocalDateTime.of(2023, 5, 30, 10, 0));
        commentDTO.setIssue_id(3);

        Assertions.assertEquals(1, commentDTO.getId());
        Assertions.assertEquals(2, commentDTO.getCreater_id());
        Assertions.assertEquals("Sample description", commentDTO.getDescription());
        Assertions.assertEquals(LocalDateTime.of(2023, 5, 30, 10, 0), commentDTO.getCreated_date());
        Assertions.assertEquals(3, commentDTO.getIssue_id());
    }
}