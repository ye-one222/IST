package com.se.demo.entity;

import com.se.demo.dto.IssueDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter//모든필드에 대한 접근자 메서드 생성
@NoArgsConstructor
@Table(name = "issue")
@AllArgsConstructor
@Builder
@DynamicInsert

public class IssueEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Column(nullable = false)
    @ColumnDefault("new")
    public String state;
    @Column(name = "pl_id")
    public int plId;

    @Column(name = "title", nullable = false)
    public String title;

    @Column(name = "description")
    public String description;

    @Column(name = "reporter_id")
    public int reporterId;

    @Column(name = "fixer_id")
    public int fixerId;

    @Column(name = "assignee")
    public int assigneeId;

    @Column(name = "priority")
    public String priority;


    @OneToMany(mappedBy = "issue", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<CommentEntity> comments;  // mappedBy를 issue로 설정

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

}
