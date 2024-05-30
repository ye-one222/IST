package com.se.demo.entity;

import com.se.demo.repository.CommentRepository;
import com.se.demo.repository.IssueRepository;
import com.se.demo.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
//@DataJpaTest
class CommentEntityTest {



    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private IssueRepository issueRepository;

    private MemberEntity member;
    private IssueEntity issue;

    @BeforeEach
    void setUp() {
        member = new MemberEntity();
        member.setNickname("testUser");
        member.setPassword("1111");
        member.setUser_id(1);
        member.setProjects(null);
        member = memberRepository.save(member);

        issue = new IssueEntity();
        issue.setId(1);
        issue.setTitle("Test Issue");
        issue.setDescription("Test description"); // Set description here
        issue.setState("new");
        issue = issueRepository.save(issue);
    }
    @Test
    void testCreateCommentEntity() {
        // 댓글 엔터티 생성
        CommentEntity comment = CommentEntity.builder()
                .description("Test description")
                .createdDate(LocalDateTime.now())
                .createrId(member)
                .issue(issue)
                .build();

        // 저장한 댓글 엔터티를 데이터베이스에 저장
        CommentEntity savedComment = commentRepository.save(comment);




        // 저장한 댓글 엔터티 조회
        CommentEntity foundComment = commentRepository.findById(comment.getId()).orElse(null);

        // 조회한 댓글 엔터티 확인
        Assertions.assertNotNull(foundComment);
        Assertions.assertEquals(savedComment.getId(), foundComment.getId());
        Assertions.assertEquals("Test description", foundComment.getDescription());
    }
    @Test
    void testFindById() {
        // 댓글 엔터티 생성
        // 댓글 엔터티 생성
        CommentEntity comment = CommentEntity.builder()
                .description("Test description")
                .createdDate(LocalDateTime.now())
                .createrId(member)
                .issue(issue)
                .build();

        CommentEntity savedComment = commentRepository.save(comment);


        // 저장한 댓글 엔터티 조회
        CommentEntity foundComment = commentRepository.findById(comment.getId()).orElse(null);

        // 조회한 댓글 엔터티 확인
        Assertions.assertNotNull(foundComment);
        Assertions.assertEquals(savedComment.getId(), foundComment.getId());
        Assertions.assertEquals("Test description", foundComment.getDescription());
    }



}