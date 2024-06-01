package com.se.demo.repository;

import com.se.demo.entity.CommentEntity;
import com.se.demo.entity.IssueEntity;
import com.se.demo.entity.MemberEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
//@DataJpaTest
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private IssueRepository issueRepository;

    private MemberEntity member;
    private IssueEntity issue;

    @BeforeEach
    public void setUp() {
        member = new MemberEntity();
        member.setNickname("testUser");
        member.setPassword("1111");
        member.setUser_id(1);
        member = memberRepository.save(member);

        issue = new IssueEntity();
        issue.setId(1);
        issue.setTitle("Test Issue");
        issue.setDescription("Test description"); // Set description here
        issue.setState("new");
        issue = issueRepository.save(issue);

        CommentEntity comment = new CommentEntity();
        comment.setDescription("Test description");
        comment.setCreatedDate(LocalDateTime.now());
        comment.setCreaterId(member);
        comment.setIssue(issue);
        commentRepository.save(comment);
    }

    @Test
    public void testFindByIssueId() {
        List<CommentEntity> comments = commentRepository.findByIssueId(issue.getId());

        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals("Test description", comments.get(0).getDescription());
        assertEquals(issue.getId(), comments.get(0).getIssue().getId());
        assertEquals(member.getUser_id(), comments.get(0).getCreaterId().getUser_id());
    }

    @Test
    public void testSaveComment() {
        CommentEntity comment = new CommentEntity();
        comment.setDescription("test description");
        comment.setCreatedDate(LocalDateTime.now());
        comment.setCreaterId(member);
        comment.setIssue(issue);

        CommentEntity savedComment = commentRepository.save(comment);

        assertNotNull(savedComment);
        assertEquals("test description", savedComment.getDescription());
        assertEquals(issue.getId(), savedComment.getIssue().getId());
        assertEquals(member.getUser_id(), savedComment.getCreaterId().getUser_id());
    }
}