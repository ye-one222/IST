package com.se.demo.dto;

import com.se.demo.entity.IssueEntity;
import com.se.demo.entity.ProjectEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private List<CommentDTO> comments = new ArrayList<>();
    private int project_id;

   public IssueDTO(){
        state = "new";
   }

}