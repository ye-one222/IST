/*package com.se.demo.service;

import com.se.demo.dto.MemberDTO;
import com.se.demo.dto.ProjectDTO;
import com.se.demo.entity.MemberEntity;
import com.se.demo.entity.ProjectEntity;
import com.se.demo.repository.IssueRepository;
import com.se.demo.repository.MemberRepository;
import com.se.demo.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private IssueService issueService;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        // Given
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setLeader_id(1);
        projectDTO.setTitle("New Project");

        MemberEntity leaderEntity = new MemberEntity();
        leaderEntity.setUser_id(1);
        leaderEntity.setNickname("Leader");
        leaderEntity.setPassword("password");

        MemberDTO leaderDTO = new MemberDTO();
        leaderDTO.setUser_id(1);
        leaderDTO.setNickname("Leader");
        leaderDTO.setPassword("password");

        projectDTO.getMembers().add(leaderDTO);

        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(1);
        projectEntity.setTitle("New Project");

        when(memberRepository.findById(projectDTO.getLeader_id())).thenReturn(Optional.of(leaderEntity));
        when(projectRepository.save(any(ProjectEntity.class))).thenReturn(projectEntity);

        // When
        ProjectEntity savedProject = projectService.save(projectDTO);

        // Then
        assertNotNull(savedProject);
        assertEquals("New Project", savedProject.getTitle());
        verify(memberRepository, times(1)).findById(projectDTO.getLeader_id());
        verify(projectRepository, times(1)).save(any(ProjectEntity.class));
    }
}
*/