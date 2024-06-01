package com.se.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.se.demo.IssueTrackingApplication;
import com.se.demo.dto.*;
import com.se.demo.entity.IssueEntity;
import com.se.demo.entity.ProjectEntity;
import com.se.demo.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = IssueTrackingApplication.class)
@AutoConfigureMockMvc
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    private ProjectEntity projectEntity;
    private IssueEntity issueEntity;
    private IssueDTO issueDTO;
    private ProjectDTO projectDTO;
    private ResponseProjectDTO responseProjectDTO;
    private ResponseIssueDTO responseIssueDTO;
    private MonthlyAnalysisDTO monthlyAnalysisDTO;

    @BeforeEach
    void setUp() {
        issueDTO = new IssueDTO();
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
        issueDTO.setDate(LocalDateTime.now());

        projectDTO = new ProjectDTO();
        projectDTO.setId(1);
        projectDTO.setTitle("Project");
        projectDTO.setLeader_id(1);
        projectDTO.setMembers(Arrays.asList());

        issueEntity = IssueEntity.builder()
                .id(issueDTO.getId())
                .title(issueDTO.getTitle())
                .description(issueDTO.getDescription())
                .reporterId(issueDTO.getReporter_id())
                .fixerId(issueDTO.getFixer_id())
                .assigneeId(issueDTO.getAssignee_id())
                .priority(issueDTO.getPriority())
                .state(issueDTO.getState())
                .plId(issueDTO.getPl_id())
                .project(new ProjectEntity())
                .build();

        projectEntity = new ProjectEntity();
        projectEntity.setId(1);
        projectEntity.setTitle("Project");
        projectEntity.setLeader_id(1);
        projectEntity.setMembers(Arrays.asList());

        responseProjectDTO = new ResponseProjectDTO(projectDTO, "Leader");
        responseIssueDTO = new ResponseIssueDTO(issueDTO);

        monthlyAnalysisDTO = new MonthlyAnalysisDTO();
        monthlyAnalysisDTO.setMonth(6);
        monthlyAnalysisDTO.setNewCnt(1);
        monthlyAnalysisDTO.setAssignedCnt(0);
        monthlyAnalysisDTO.setFixedCnt(0);
        monthlyAnalysisDTO.setResolvedCnt(0);
        monthlyAnalysisDTO.setClosedCnt(0);
        monthlyAnalysisDTO.setReopened(0);
    }

    @Test
    void testCreateProject() throws Exception {
        Mockito.when(projectService.save(ArgumentMatchers.any(ProjectDTO.class))).thenReturn(projectEntity);

        mockMvc.perform(MockMvcRequestBuilders.post("/project/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(projectDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Project")));
    }

    @Test
    void testFindByProjectId() throws Exception {
        Mockito.when(projectService.findById(1)).thenReturn(responseProjectDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/project/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectDTO.id", is(1)))
                .andExpect(jsonPath("$.leader_nickname", is("Leader")));
    }

    @Test
    void testFindByUserId() throws Exception {
        Mockito.when(projectService.findByUserId(1)).thenReturn(Arrays.asList(responseProjectDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/project/my/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].projectDTO.id", is(1)))
                .andExpect(jsonPath("$[0].leader_nickname", is("Leader")));
    }

    @Test
    void testFindIssuesByProjectId() throws Exception {
        Mockito.when(projectService.findByProjectId(1)).thenReturn(Arrays.asList(responseIssueDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/project/1/issues")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].responseIssue.id", is(1)))
                .andExpect(jsonPath("$[0].responseIssue.title", is("Issue")));
    }

    @Test
    void testCreateIssue() throws Exception {
        Mockito.when(projectService.createIssue(ArgumentMatchers.any(IssueDTO.class))).thenReturn(issueEntity);

        mockMvc.perform(MockMvcRequestBuilders.post("/project/issue/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(issueDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Issue")));
    }

    @Test
    void testInviteMember() throws Exception {
        Mockito.when(projectService.inviteMember(1, 2)).thenReturn(projectDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/project/1/invite/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Project")));
    }

    @Test
    void testSearchIssuesAnalysis() throws Exception {
        Mockito.when(projectService.countAnalysis(1)).thenReturn(Arrays.asList(monthlyAnalysisDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/project/analysis/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].month", is(6)))
                .andExpect(jsonPath("$[0].newCnt", is(1)));
    }

    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
