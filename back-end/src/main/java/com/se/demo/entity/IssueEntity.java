package com.se.demo.entity;

import com.se.demo.dto.IssueDTO;
import com.se.demo.repository.ProjectRepository;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter//모든필드에 대한 접근자 메서드 생성
@Setter
@NoArgsConstructor
@Table(name = "issue")
@AllArgsConstructor
@Builder
@DynamicInsert

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

    @Column(name = "assignee_id")
    private Integer assigneeId;

    @Column(name = "priority")
    private String priority;


    @OneToMany(mappedBy = "issue", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<CommentEntity> comments;  // mappedBy를 issue로 설정

    @ManyToOne
    @JoinColumn(name = "project_id",nullable = false)
    private ProjectEntity project;

    public static IssueEntity toIssueEntity(IssueDTO issueDTO, ProjectRepository projectRepository) { //static 빼도 되나여
        IssueEntity issueEntity = new IssueEntity();
        issueEntity.setId(issueDTO.getId());
        issueEntity.setTitle(issueDTO.getTitle());
        issueEntity.setDescription(issueDTO.getDescription());

        issueEntity.setReporterId(issueDTO.getReporter_id());
        issueEntity.setFixerId(issueDTO.getFixer_id());
        issueEntity.setAssigneeId(issueDTO.getAssignee_id());
        issueEntity.setPriority(issueDTO.getPriority());
        issueEntity.setState(issueDTO.getState());
        issueEntity.setPlId(issueDTO.getPl_id());

        issueEntity.setProject(projectRepository.findById(issueDTO.getProject_id())
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID")));;

        return issueEntity;
    }

}