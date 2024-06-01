package com.se.demo.service;

import com.se.demo.dto.IssueDTO;
import com.se.demo.dto.ResponseIssueDTO;
import com.se.demo.entity.IssueEntity;
import com.se.demo.entity.MemberEntity;
import com.se.demo.entity.ProjectEntity;
import com.se.demo.repository.IssueRepository;
import com.se.demo.repository.MemberRepository;
import com.se.demo.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IssueServiceTest {

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private IssueService issueService;

    private IssueDTO issueDTO;
    private ProjectEntity projectEntity;
    private MemberEntity memberEntity;
    private MemberEntity assignee;
    private MemberEntity reporter;
    private IssueEntity issueEntity;

    @BeforeEach
    void setUp() {
        issueDTO = new IssueDTO();
        issueDTO.setId(1);
        issueDTO.setProject_id(1);
        issueDTO.setReporter_id(1);
        issueDTO.setAssignee_id(2);
        issueDTO.setTitle("Test Issue");

        projectEntity = new ProjectEntity();
        projectEntity.setId(1);
        projectEntity.setLeader_id(1);
        projectEntity.setMembers(new ArrayList<>());

        memberEntity = new MemberEntity();
        memberEntity.setUser_id(1);
        memberEntity.setNickname("reporter");

        issueEntity = new IssueEntity();
        issueEntity.setId(1);
        issueEntity.setTitle("Test Issue");
        issueEntity.setReporterId(1);
        issueEntity.setAssigneeId(2);
        issueEntity.setPlId(1);

        assignee = new MemberEntity();
        assignee.setUser_id(2);
        assignee.setNickname("Assignee");

        reporter = new MemberEntity();
        reporter.setUser_id(1);
        reporter.setNickname("Reporter");
    }

    @Test
    void createIssue() {
        when(projectRepository.findById(1)).thenReturn(Optional.of(projectEntity));
        when(issueRepository.save(any(IssueEntity.class))).thenReturn(issueEntity);

        IssueEntity createdIssue = issueService.createIssue(issueDTO);

        assertNotNull(createdIssue);
        assertEquals(issueDTO.getTitle(), createdIssue.getTitle());
        verify(projectRepository, times(2)).findById(1);
        verify(issueRepository, times(1)).save(any(IssueEntity.class));
    }

    @Test
    void findById() {
        when(issueRepository.findById(1)).thenReturn(Optional.of(issueEntity));
        when(memberRepository.findById(1)).thenReturn(Optional.of(memberEntity));
        when(memberRepository.findById(2)).thenReturn(Optional.of(memberEntity));

        ResponseIssueDTO responseIssueDTO = issueService.findById(1);

        assertNotNull(responseIssueDTO);
        assertEquals("reporter", responseIssueDTO.getReporter_nickname());
        assertEquals("reporter", responseIssueDTO.getAssignee_nickname());
        verify(issueRepository, times(1)).findById(1);
        verify(memberRepository, times(1)).findById(1);
        verify(memberRepository, times(1)).findById(2);
    }

    @Test
    void findMyIssues() {
        List<IssueEntity> issueEntities = new ArrayList<>();
        issueEntities.add(issueEntity);
        when(issueRepository.findByReporterIdOrAssigneeIdOrPlId(1, 1, 1)).thenReturn(issueEntities);
        when(memberRepository.findById(1)).thenReturn(Optional.of(memberEntity));
        when(memberRepository.findById(2)).thenReturn(Optional.of(memberEntity));

        List<ResponseIssueDTO> responseIssueDTOList = issueService.findMyIssues(1);

        assertNotNull(responseIssueDTOList);
        assertFalse(responseIssueDTOList.isEmpty());
        assertEquals("reporter", responseIssueDTOList.get(0).getReporter_nickname());
        verify(issueRepository, times(1)).findByReporterIdOrAssigneeIdOrPlId(1, 1, 1);
        verify(memberRepository, times(1)).findById(1);
        verify(memberRepository, times(1)).findById(2);
    }

    @Test
    void updateIssue() {
        when(projectRepository.findById(1)).thenReturn(Optional.of(projectEntity));
        when(issueRepository.save(any(IssueEntity.class))).thenReturn(issueEntity);
        when(memberRepository.findById(2)).thenReturn(Optional.of(assignee));

        IssueDTO updatedIssueDTO = issueService.updateIssue(issueDTO);

        assertNotNull(updatedIssueDTO);
        assertEquals(issueDTO.getTitle(), updatedIssueDTO.getTitle());
        verify(projectRepository, times(1)).findById(1);
        verify(issueRepository, times(1)).save(any(IssueEntity.class));
        verify(memberRepository, times(1)).findById(2);
    }

    @Test
    void search() {
        List<IssueEntity> issueEntities = new ArrayList<>();
        issueEntities.add(issueEntity);

        MemberEntity assigneeEntity = new MemberEntity();
        assigneeEntity.setUser_id(2);
        assigneeEntity.setNickname("reporter");

        MemberEntity reporterEntity = new MemberEntity();
        reporterEntity.setUser_id(1);
        reporterEntity.setNickname("reporter");

        when(issueRepository.findByTitleContaining("reporter")).thenReturn(issueEntities);
        when(memberRepository.findByNickname("reporter"))
                .thenReturn(Optional.of(reporterEntity))
                .thenReturn(Optional.of(assigneeEntity));
        when(issueRepository.findByReporterId(1)).thenReturn(issueEntities);
        when(issueRepository.findByAssigneeId(2)).thenReturn(issueEntities);

        List<IssueDTO> result = issueService.search("reporter");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(issueRepository, times(1)).findByTitleContaining("reporter");
        verify(memberRepository, times(2)).findByNickname("reporter");
        verify(issueRepository, times(1)).findByReporterId(1);
        verify(issueRepository, times(1)).findByAssigneeId(2);
    }


    @Test
    void checkProjMember() {
        List<MemberEntity> members = new ArrayList<>();
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setUser_id(2);
        members.add(memberEntity);
        projectEntity.setMembers(members);

        when(projectRepository.findById(1)).thenReturn(Optional.of(projectEntity));

        boolean result = issueService.checkProjMember(2, issueDTO);

        assertTrue(result);
        verify(projectRepository, times(1)).findById(1);
    }
}
