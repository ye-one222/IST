package com.se.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
@Setter
@Table(name = "comment")
@Entity
@NoArgsConstructor


public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "created_date")
    @CreatedDate
    private LocalDateTime createdDate;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creater_id")
    private MemberEntity createrId;

    @Column(name = "description", nullable = false)
    private String description;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "issue_id")
    private IssueEntity issue;  // Issue 필드를 추가하여 매핑 설정

}