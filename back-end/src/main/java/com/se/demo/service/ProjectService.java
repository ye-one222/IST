package com.se.demo.service;

import com.se.demo.dto.IssueDTO;
import com.se.demo.dto.MemberDTO;
import com.se.demo.dto.ProjectDTO;
import com.se.demo.dto.ResponseProjectDTO;
import com.se.demo.entity.IssueEntity;
import com.se.demo.entity.MemberEntity;
import com.se.demo.entity.ProjectEntity;
import com.se.demo.repository.IssueRepository;
import com.se.demo.repository.MemberRepository;
import com.se.demo.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final IssueRepository issueRepository;

    private final IssueService issueService;

    //@Transactional
    public ProjectEntity save(ProjectDTO projectDTO) {
        //System.out.println("LEADER::"+projectDTO.getLeader_id());
        MemberEntity leaderEntity = memberRepository.findById(projectDTO.getLeader_id())
            .orElseThrow(() -> new IllegalArgumentException("Invalid leader ID"));
        MemberDTO leaderDTO = MemberDTO.toMemberDTO(leaderEntity);
        projectDTO.getMembers().add(leaderDTO);

        ProjectEntity projectEntity = ProjectEntity.toProjectEntity(projectDTO);
        return projectRepository.save(projectEntity);
    }

    @Transactional
    public ResponseProjectDTO findById(int project_id) {
        ProjectEntity projectEntity = projectRepository.findById(project_id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project ID"));
        //return toDTO(projectEntity);
        ProjectDTO projectDTO = ProjectDTO.toProjectDTO(projectEntity);

        MemberEntity leaderEntity = memberRepository.findById(projectDTO.getLeader_id())
                .orElseThrow(() -> new IllegalArgumentException("Invalid leader nickname"));

        return new ResponseProjectDTO(projectDTO, leaderEntity.getNickname());
    }

    @Transactional
    public List<ResponseProjectDTO> findByUserId(int userId) {
        MemberEntity memberEntity = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        List <ProjectDTO> projectDTOList = ProjectDTO.toProjectDTOList(memberEntity.getProjects());
        List <ResponseProjectDTO> responseProjectDTOList = new ArrayList<>();

        for(ProjectDTO projectDTO : projectDTOList){
            MemberEntity leaderEntity = memberRepository.findById(projectDTO.getLeader_id())
                .orElseThrow(() -> new IllegalArgumentException("Invalid leader nickname"));
            ResponseProjectDTO responseProjectDTO = new ResponseProjectDTO(projectDTO, leaderEntity.getNickname());
            responseProjectDTOList.add(responseProjectDTO);
        }

        return responseProjectDTOList;
    }

    public List<IssueDTO> findByProjectId(int projectId) {
        List<IssueEntity> issueEntityList = issueRepository.findByProjectId(projectId);
        List<IssueDTO> issueDTOList = issueEntityList.stream()
                .map(IssueDTO::toIssueDTO)
                .collect(Collectors.toList());
        return issueDTOList;
    }

    @Transactional
    public IssueEntity createIssue(IssueDTO issueDTO) {
        //해당 프로젝트DTO의 issueList에 넣어주고

        ProjectEntity projectEntity = projectRepository.findById(issueDTO.getProject_id())
                .orElseThrow(() -> new IllegalArgumentException("Invalid project ID"));
        ProjectDTO projectDTO = ProjectDTO.toProjectDTO(projectEntity);
        projectDTO.getIssues().add(issueDTO);
        //이슈 진짜 생성
        return issueService.createIssue(issueDTO);
    }

    @Transactional
    public ProjectDTO inviteMember(int projectId, int userId) {
        //projectDTO 가져와서 members에 add
        //실제 DB에도 project_members table에 추가
        ProjectEntity projectEntity = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project ID"));
        MemberEntity memberEntity = memberRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        projectEntity.getMembers().add(memberEntity);
        return ProjectDTO.toProjectDTO(projectEntity);
    }
}
