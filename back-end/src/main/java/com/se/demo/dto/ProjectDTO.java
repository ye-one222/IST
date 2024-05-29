package com.se.demo.dto;

import com.se.demo.entity.MemberEntity;
import com.se.demo.entity.ProjectEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public ProjectEntity toEntity() {
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
    }
}
