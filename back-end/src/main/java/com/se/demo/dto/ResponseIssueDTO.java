package com.se.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class ResponseIssueDTO {
    private IssueDTO responseIssue;
    private String reporter_nickname;
    private String assignee_nickname;

    public ResponseIssueDTO(IssueDTO issueDTO){
        this.responseIssue = issueDTO;
        this.reporter_nickname = "";
        this.assignee_nickname = "";
    }

}
