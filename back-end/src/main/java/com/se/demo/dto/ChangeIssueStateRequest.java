package com.se.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class ChangeIssueStateRequest {
    private Integer assignee_id;
    private String oldState;
    private String newState;
}