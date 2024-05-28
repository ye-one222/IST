package com.se.demo.service;

import com.se.demo.dto.IssueDTO;
import com.se.demo.dto.MemberDTO;
import com.se.demo.dto.ProjectDTO;
import com.se.demo.entity.IssueEntity;
import com.se.demo.entity.MemberEntity;
import com.se.demo.entity.ProjectEntity;
import com.se.demo.repository.MemberRepository;
import com.se.demo.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    //@Transactional
    public ProjectEntity save(ProjectDTO projectDTO) {
        System.out.println("LEADER::"+projectDTO.getLeader_id());
        MemberEntity leaderEntity = memberRepository.findById(projectDTO.getLeader_id())
            .orElseThrow(() -> new IllegalArgumentException("Invalid leader ID"));
        MemberDTO leaderDTO = toMemberDTO(leaderEntity);
        projectDTO.getMembers().add(leaderDTO);

        ProjectEntity projectEntity = projectDTO.toEntity();
        return projectRepository.save(projectEntity);
    }

    @Transactional
    public ProjectDTO findById(int project_id) {
        ProjectEntity projectEntity = projectRepository.findById(project_id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project ID"));
        return toDTO(projectEntity);
    }

    @Transactional
    public List<ProjectDTO> findByUserId(int userId) {
        MemberEntity memberEntity = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
        return toProjectDTOList(memberEntity.getProjects());
    }

    private MemberDTO toMemberDTO(MemberEntity memberEntity) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setUser_id(memberEntity.getUser_id());
        memberDTO.setNickname(memberEntity.getNickname());
        memberDTO.setPassword(memberEntity.getPassword());
        //memberDTO.setProjects(toProjectDTOList(memberEntity.getProjects())); 이거 있어야 나의 project의 member의 project 정보가 올바르게 나오는데, 이거 실행하면 스택오버플로우 발생해서.. 그리고 내 동료의 project들을 내가 굳이 알아야 할 필요는 없잖앙
        return memberDTO;
    }

    //밑에 있는 toProjectDTO랑 거의 똑같은데 이건 issue set하는게 없음. 근데 아직 머가 문제인지 모르겠음
    public ProjectDTO toDTO(ProjectEntity projectEntity) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(projectEntity.getId());
        projectDTO.setTitle(projectEntity.getTitle());
        projectDTO.setLeader_id(projectEntity.getLeader_id());

        projectDTO.setMembers(
                projectEntity.getMembers().stream()
                        .map(member -> new MemberDTO(member.getUser_id(), member.getNickname(), member.getPassword()))
                        .collect(Collectors.toList())
        );
        return projectDTO;
    }

    private IssueDTO toIssueDTO(IssueEntity issueEntity) {
        return new IssueDTO(
                //issueEntity.getId()
                //나중에 issueEntity 필드 생기면 다 추가
                //issueEntity.getIssue_title(),
                //issueEntity.getIssue_description()
        );
    }

    private ProjectDTO toProjectDTO(ProjectEntity projectEntity) {
        List<IssueEntity> issueEntities = projectEntity.getIssues();
        List<IssueDTO> issueDTOs = new ArrayList<>();
        if(issueEntities!=null){
            for(IssueEntity issueEntity : issueEntities) {
                issueDTOs.add(toIssueDTO(issueEntity));
            }
        }

        List<MemberEntity> memberEntities = projectEntity.getMembers();
        List<MemberDTO> memberDTOs = new ArrayList<>();
        if(memberEntities!=null){
            for(MemberEntity memberEntity : memberEntities) {
                memberDTOs.add(toMemberDTO(memberEntity));
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

    private List<ProjectDTO> toProjectDTOList(List<ProjectEntity> projectEntity){
        List<ProjectDTO> projectDTOs = new ArrayList<>();

        for(ProjectEntity projectEntity1 : projectEntity) {
            toProjectDTO(projectEntity1);
            projectDTOs.add(toProjectDTO(projectEntity1));
        }
        return projectDTOs;
    }
}
