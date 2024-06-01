package com.se.demo.service;

import com.se.demo.IssueTrackingApplication;
import com.se.demo.dto.*;
import com.se.demo.entity.BaseEntity;
import com.se.demo.entity.IssueEntity;
import com.se.demo.entity.MemberEntity;
import com.se.demo.entity.ProjectEntity;
import com.se.demo.repository.IssueRepository;
import com.se.demo.repository.MemberRepository;
import com.se.demo.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = IssueTrackingApplication.class)
public class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private IssueRepository issueRepository;

    @MockBean
    private IssueService issueService;

    private ProjectEntity projectEntity;
    private MemberEntity memberEntity;
    private IssueEntity issueEntity;

    @BeforeEach
    void setUp() {
        memberEntity = new MemberEntity();
        memberEntity.setUser_id(1);
        memberEntity.setNickname("Leader");
        memberEntity.setPassword("password");

        issueEntity = new IssueEntity();
        issueEntity.setId(1);
        issueEntity.setTitle("Issue");
        issueEntity.setDescription("Description");
        issueEntity.setReporterId(2);
        issueEntity.setFixerId(3);
        issueEntity.setAssigneeId(3);
        issueEntity.setState("new");
        issueEntity.setPriority("High");

        projectEntity = new ProjectEntity();
        projectEntity.setId(1);
        projectEntity.setTitle("Project");
        projectEntity.setLeader_id(1);
        projectEntity.setMembers(new ArrayList<>(Arrays.asList(memberEntity)));
        projectEntity.setIssues(new ArrayList<>(Arrays.asList(issueEntity)));

        // 프로젝트 리포지토리를 모킹하여 프로젝트를 반환하도록 설정
        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.of(projectEntity));
    }

    @Test
    void testSave() {
        ProjectDTO projectDTO = ProjectDTO.toProjectDTO(projectEntity);

        Mockito.when(memberRepository.findById(1)).thenReturn(Optional.of(memberEntity));
        Mockito.when(projectRepository.save(ArgumentMatchers.any(ProjectEntity.class))).thenReturn(projectEntity);

        ProjectEntity savedEntity = projectService.save(projectDTO);

        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getTitle()).isEqualTo("Project");
    }

    @Test
    void testFindById() {
        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.of(projectEntity));
        Mockito.when(memberRepository.findById(1)).thenReturn(Optional.of(memberEntity));

        ResponseProjectDTO responseProjectDTO = projectService.findById(1);

        assertThat(responseProjectDTO).isNotNull();
        assertThat(responseProjectDTO.getProjectDTO().getId()).isEqualTo(1);
        assertThat(responseProjectDTO.getLeader_nickname()).isEqualTo("Leader");
    }

    @Test
    void testFindByUserId() {
        Mockito.when(memberRepository.findById(1)).thenReturn(Optional.of(memberEntity));

        List<ProjectEntity> projects = new ArrayList<>(Arrays.asList(projectEntity));
        memberEntity.setProjects(projects);

        List<ResponseProjectDTO> responseProjectDTOList = projectService.findByUserId(1);

        assertThat(responseProjectDTOList).isNotEmpty();
        assertThat(responseProjectDTOList.get(0).getProjectDTO().getId()).isEqualTo(1);
        assertThat(responseProjectDTOList.get(0).getLeader_nickname()).isEqualTo("Leader");
    }

    @Test
    void testFindByProjectId() {
        Mockito.when(issueRepository.findByProjectId(1)).thenReturn(Arrays.asList(issueEntity));
        Mockito.when(memberRepository.findById(2)).thenReturn(Optional.of(memberEntity));
        Mockito.when(memberRepository.findById(3)).thenReturn(Optional.of(memberEntity));

        List<ResponseIssueDTO> responseIssueDTOList = projectService.findByProjectId(1);

        assertThat(responseIssueDTOList).isNotEmpty();
        assertThat(responseIssueDTOList.get(0).getResponseIssue().getId()).isEqualTo(1);
    }

    @Test
    @Transactional
    void testCreateIssue() {
        IssueDTO issueDTO = IssueDTO.toIssueDTO(issueEntity);
        issueDTO.setProject_id(1);  // 프로젝트 ID 설정

        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.of(projectEntity));  // 프로젝트 ID가 유효하도록 모킹
        Mockito.when(issueService.createIssue(ArgumentMatchers.any(IssueDTO.class))).thenReturn(issueEntity);

        IssueEntity createdIssue = projectService.createIssue(issueDTO);

        assertThat(createdIssue).isNotNull();
        assertThat(createdIssue.getTitle()).isEqualTo("Issue");
    }

    @Test
    @Transactional
    void testInviteMember() {
        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.of(projectEntity));
        Mockito.when(memberRepository.findById(2)).thenReturn(Optional.of(memberEntity));

        ProjectDTO updatedProjectDTO = projectService.inviteMember(1, 2);

        assertThat(updatedProjectDTO).isNotNull();
        assertThat(updatedProjectDTO.getMembers()).isNotEmpty();
    }

    @Test
    void testCountAnalysis() {
        // IssueDTO 생성 및 날짜 필드 설정
        IssueDTO issueDTO = new IssueDTO();
        issueDTO.setId(1);
        issueDTO.setTitle("Issue");
        issueDTO.setDescription("Description");
        issueDTO.setReporter_id(2);
        issueDTO.setFixer_id(3);
        issueDTO.setAssignee_id(3);
        issueDTO.setPriority("High");
        issueDTO.setState("new");
        issueDTO.setPl_id(1);
        issueDTO.setProject_id(1);
        issueDTO.setDate(LocalDateTime.now()); // 현재 날짜로 설정

        // ProjectEntity 설정
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(1);
        projectEntity.setTitle("Project");
        projectEntity.setLeader_id(1);
        projectEntity.setMembers(new ArrayList<>());

        // IssueEntity 설정
        IssueEntity issueEntity = IssueEntity.builder()
                .id(issueDTO.getId())
                .title(issueDTO.getTitle())
                .description(issueDTO.getDescription())
                .reporterId(issueDTO.getReporter_id())
                .fixerId(issueDTO.getFixer_id())
                .assigneeId(issueDTO.getAssignee_id())
                .priority(issueDTO.getPriority())
                .state(issueDTO.getState())
                .plId(issueDTO.getPl_id())
                .project(projectEntity)
                .build();

        try {
            Field dateField = BaseEntity.class.getDeclaredField("date");
            dateField.setAccessible(true);
            dateField.set(issueEntity, issueDTO.getDate());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        // ProjectRepository 모킹
        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.of(projectEntity));

        // IssueRepository 모킹 설정
        Mockito.when(issueRepository.findByProjectId(1)).thenReturn(Arrays.asList(issueEntity));

        // countAnalysis 메서드 호출 및 결과 검증
        List<MonthlyAnalysisDTO> analysisDTOList = projectService.countAnalysis(1);

        assertThat(analysisDTOList).isNotEmpty();
        assertThat(analysisDTOList.get(0).getNewCnt()).isEqualTo(1); // 상태 검증
    }
}
