package com.se.demo.dto;

import com.se.demo.entity.IssueEntity;
import com.se.demo.entity.MemberEntity;
import com.se.demo.entity.ProjectEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private int id;

    private String title;

    private int leader_id;

    private List<IssueDTO> issues = new ArrayList<>();

    private List<MemberDTO> members = new ArrayList<>();

    /*public ProjectEntity toEntity() {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setTitle(this.title);
        projectEntity.setLeader_id(this.leader_id);
        if (this.members != null) {
            List<MemberEntity> memberEntities = this.members.stream()
                    .map(MemberDTO::toEntity)
                    .collect(Collectors.toList());
            projectEntity.setMembers(memberEntities);
            for (MemberEntity member : memberEntities) {
                member.getProjects().add(projectEntity);
            }
        }
        return projectEntity;
    }*/

    public static ProjectDTO toProjectDTO(ProjectEntity projectEntity) {
        List<IssueEntity> issueEntities = projectEntity.getIssues();
        List<IssueDTO> issueDTOs = new ArrayList<>();
        if(issueEntities!=null){
            for(IssueEntity issueEntity : issueEntities) {
                issueDTOs.add(IssueDTO.toIssueDTO(issueEntity));
            }
        }

        List<MemberEntity> memberEntities = projectEntity.getMembers();
        List<MemberDTO> memberDTOs = new ArrayList<>();
        if(memberEntities!=null){
            for(MemberEntity memberEntity : memberEntities) {
                memberDTOs.add(MemberDTO.toMemberDTO(memberEntity));
            }
        }

        return new ProjectDTO(
                projectEntity.getId(),
                projectEntity.getTitle(),
                projectEntity.getLeader_id(),
                issueDTOs,
                memberDTOs
        );
    }

    public static List<ProjectDTO> toProjectDTOList(List<ProjectEntity> projectEntity){
        List<ProjectDTO> projectDTOs = new ArrayList<>();

        for(ProjectEntity projectEntity1 : projectEntity) {
            toProjectDTO(projectEntity1);
            projectDTOs.add(toProjectDTO(projectEntity1));
        }
        return projectDTOs;
    }
}
