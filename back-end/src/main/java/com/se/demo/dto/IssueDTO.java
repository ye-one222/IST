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
    private Integer reporter_id;
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

    public static IssueDTO toIssueDTO(IssueEntity issueEntity){
        if (issueEntity.getAssigneeId() == null) {
            throw new IllegalArgumentException("Assignee ID cannot be null");
        }

        IssueDTO issueDTO = new IssueDTO();
        issueDTO.setId(issueEntity.getId());
        issueDTO.setTitle((issueEntity.getTitle()));
        issueDTO.setDate(issueEntity.getDate());
        issueDTO.setDescription(issueEntity.getDescription());
        issueDTO.setPriority(issueEntity.getPriority());
        issueDTO.setAssignee_id(issueEntity.getAssigneeId());
        issueDTO.setFixer_id(issueEntity.getFixerId());
        issueDTO.setPl_id(issueEntity.getPlId());
        issueDTO.setReporter_id(issueEntity.getReporterId());
        issueDTO.setState(issueEntity.getState());

        //issueDTO.setProject_id(issueEntity.getProject().getId());
        if (issueEntity.getProject() != null) {
            issueDTO.setProject_id(issueEntity.getProject().getId());
        }

        return issueDTO;
    }

}