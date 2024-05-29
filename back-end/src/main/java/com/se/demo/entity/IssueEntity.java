package com.se.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.List;

@Entity
@Getter//모든필드에 대한 접근자 메서드 생성
@NoArgsConstructor
@Table(name = "issue")
@AllArgsConstructor
@Builder
@DynamicInsert
@Setter

public class IssueEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    @ColumnDefault("new")
    private String state;
    @Column(name = "pl_id")
    private int plId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "reporter_id")
    private int reporterId;

    @Column(name = "fixer_id")
    private int fixerId;

    @Column(name = "assignee")
    private int assigneeId;

    @Column(name = "priority")
    private String priority;


    @OneToMany(mappedBy = "issue", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<CommentEntity> comments;  // mappedBy를 issue로 설정

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity project;


}
