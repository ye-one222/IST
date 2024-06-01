package com.se.demo.service;

import com.se.demo.dto.*;
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

import java.time.LocalDateTime;
import java.util.*;
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

    public List<ResponseIssueDTO> findByProjectId(int projectId) {
        List<IssueEntity> issueEntityList = issueRepository.findByProjectId(projectId);
        List<ResponseIssueDTO> issueDTOList = issueEntityList.stream()
                .map(issueEntity -> {
                    ResponseIssueDTO responseIssueDTO = new ResponseIssueDTO(IssueDTO.toIssueDTO(issueEntity));
                    // 여기서 reporter_nickname
                    String reporterNickname = memberRepository.findById(issueEntity.getReporterId())
                            .orElseThrow(() -> new RuntimeException("해당 리포터가 디비에 없음")).getNickname();
                    responseIssueDTO.setReporter_nickname(reporterNickname);
                    //여기서 assignee_nickname
                    //상태가 new인지 확인하는 로직 필요하네
                    if(!Objects.equals(issueEntity.getState(), "new")){
                        String assigneeNickname = memberRepository.findById(issueEntity.getAssigneeId())
                                .orElseThrow(() -> new RuntimeException("해당 담당자가 디비에 없음")).getNickname();
                        responseIssueDTO.setAssignee_nickname(assigneeNickname);
                    }
                    return responseIssueDTO;
                })
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

    //통계분석
    //해당 플젝 이슈 서치하면서 각 달마다 개수 세기
    public List<MonthlyAnalysisDTO> countAnalysis(Integer proj_id){

        //그 리스트 반복문 돌려서 각 issueEntity 마다 getState해서 new, assigned, fixed에서 같은 상태있는지 파악하고
        // issueAnalysisDTO에서 각각 setNewCnt, setAssignedCnt 등등 에서 증가시키기
        List<IssueEntity> issueEntityList = issueRepository.findByProjectId(proj_id);
        //IssueAnalysisDTO issueAnalysisDTO = new IssueAnalysisDTO();
        List<MonthlyAnalysisDTO> monthlyIssueAnalysisList = new ArrayList<>();

        //월별 그룹화를 위한 맵
        Map<Integer, MonthlyAnalysisDTO> monthlyMap = new HashMap<>();
        for(IssueEntity issueEntity : issueEntityList){
            LocalDateTime date = issueEntity.getDate();
            int month = date.getMonthValue();

            //해당 월의 그룹이 없으면 새로운 그룹 생성
            MonthlyAnalysisDTO monthlyIssueAnalysis = monthlyMap.getOrDefault(month, new MonthlyAnalysisDTO());
            //각 상태에 따라 카운트 증가
            switch (issueEntity.getState()) {
                case "new":
                    monthlyIssueAnalysis.setNewCnt(monthlyIssueAnalysis.getNewCnt() + 1);
                    break;
                case "assigned":
                    monthlyIssueAnalysis.setAssignedCnt(monthlyIssueAnalysis.getAssignedCnt() + 1);
                    break;
                case "fixed":
                    monthlyIssueAnalysis.setFixedCnt(monthlyIssueAnalysis.getFixedCnt() + 1);
                    break;
                case "resolved":
                    monthlyIssueAnalysis.setResolvedCnt(monthlyIssueAnalysis.getResolvedCnt() + 1);
                    break;
                case "closed":
                    monthlyIssueAnalysis.setClosedCnt(monthlyIssueAnalysis.getClosedCnt() + 1);
                    break;
                case "reopened":
                    monthlyIssueAnalysis.setReopened(monthlyIssueAnalysis.getReopened() + 1);
                    break;
                default:
                    // 다른 상태 처리
                    break;
            }
            // 맵에 업데이트된 그룹 저장
            monthlyMap.put(month, monthlyIssueAnalysis);
        }
        // 맵에서 값들을 리스트에 추가
        for (Map.Entry<Integer, MonthlyAnalysisDTO> entry : monthlyMap.entrySet()) {
            MonthlyAnalysisDTO monthlyIssueAnalysis = entry.getValue();
            monthlyIssueAnalysis.setMonth(entry.getKey());
            monthlyIssueAnalysisList.add(monthlyIssueAnalysis);
        }
        ///issueAnalysisDTO.setMonthlyIssueAnalysisList(monthlyIssueAnalysisList);
        return monthlyIssueAnalysisList;
    }
}
