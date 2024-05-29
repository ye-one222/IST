package com.se.demo.dto;

import com.se.demo.entity.CommentEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentDTO {
    private int id;
    private int creater_id;
    private String description;
    private LocalDateTime created_date;
    private int issue_id;

    public CommentDTO(CommentEntity entity) {
        this.id = entity.getId();
        this.creater_id = entity.getCreaterId().getUser_id();
        this.description = entity.getDescription();
        //this.created_date = entity.getCreatedDate();
        this.created_date = LocalDateTime.now();
        this.issue_id = entity.getIssue().getId();
    }
}
