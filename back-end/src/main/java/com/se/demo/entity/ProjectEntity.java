package com.se.demo.entity;

import com.se.demo.dto.MemberDTO;
import com.se.demo.dto.ProjectDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String title;

    @Column(nullable = true)
    private int leader_id;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<IssueEntity> issues;

    @ManyToMany
    @JoinTable(
            name = "project_member",
            joinColumns = @JoinColumn(name = "projects_id"),
            inverseJoinColumns = @JoinColumn(name = "members_user_id")
    )
    private List<MemberEntity> members = new ArrayList<>();


    /*public static ProjectEntity toSaveEntity(ProjectDTO projectDTO) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setTitle(projectDTO.getTitle());
        projectEntity.setLeader_id(projectDTO.getLeader_id());
        projectEntity.setIssues(new ArrayList<>());

        if (projectDTO.getMembers() != null) {
            List<MemberEntity> memberEntities = projectDTO.getMembers().stream()
                    .map(MemberDTO::toEntity)
                    .collect(Collectors.toList());
            projectEntity.setMembers(memberEntities);
            for (MemberEntity member : memberEntities) {
                member.getProjects().add(projectEntity);
            }
        }

        return projectEntity;
    }*/

    public static ProjectEntity toProjectEntity(ProjectDTO projectDTO) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setTitle(projectDTO.getTitle());
        projectEntity.setLeader_id(projectDTO.getLeader_id());
        if (projectDTO.getMembers() != null) {
            List<MemberEntity> memberEntities = projectDTO.getMembers().stream()
                    .map(MemberEntity::toMemberEntity)
                    .collect(Collectors.toList());
            projectEntity.setMembers(memberEntities);
            for (MemberEntity member : memberEntities) {
                member.getProjects().add(projectEntity);
            }
        }
        return projectEntity;
    }
}
