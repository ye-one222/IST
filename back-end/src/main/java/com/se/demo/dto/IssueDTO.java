package com.se.demo.dto;

import com.se.demo.entity.IssueEntity;
import com.se.demo.entity.ProjectEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter

public class IssueDTO {

    private int id;
    private String title;
    private String description;
    private int reporter_id;
    private LocalDateTime date;
    private int fixer_id;
    private int assignee_id;
    private String priority;
    private String state;
    private int pl_id;

    private List<CommentResponse> comments;
    private int project_id;

   public IssueDTO(){
        state = "new";
    }


/*
    public IssueDTO(IssueEntity issue) {
        this.id = issue.getId();
        this.title = issue.getTitle();
        this.comments = issue.getComments().stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
    }*/

    /*public IssueDTO(IssueEntity issue) {
        this.id = issue.getId();
        this.title = issue.getTitle();
        this.description = issue.getDescription();
        this.reporter_id = issue.getReporterId();
        this.date = issue.getDate();

        this.fixer_id = issue.getFixerId();
        this.assignee_id = issue.getAssigneeId();
        this.priority = issue.getPriority();
        this.state = "new";
        this.pl_id = issue.getProjectId();
        this.comments = issue.getComments().stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
    }*/


}