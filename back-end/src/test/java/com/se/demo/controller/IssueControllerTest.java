package com.se.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.se.demo.IssueTrackingApplication;
import com.se.demo.dto.ChangeIssueStateRequest;
import com.se.demo.dto.IssueDTO;
import com.se.demo.dto.ResponseIssueDTO;
import com.se.demo.service.IssueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = IssueTrackingApplication.class)
@AutoConfigureMockMvc
public class IssueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IssueService issueService;

    private IssueDTO issueDTO;
    private ResponseIssueDTO responseIssueDTO;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        issueDTO = new IssueDTO();
        issueDTO.setId(1);
        issueDTO.setTitle("Test Issue");
        issueDTO.setDescription("This is a test issue");
        issueDTO.setReporter_id(2);
        issueDTO.setAssignee_id(3);
        issueDTO.setFixer_id(3);
        issueDTO.setPriority("High");
        issueDTO.setState("open");
        issueDTO.setPl_id(1);
        issueDTO.setDate(LocalDateTime.now());

        responseIssueDTO = new ResponseIssueDTO(issueDTO, "reporter_nickname", "assignee_nickname");
    }

    @Test
    void testFindById() throws Exception {
        Mockito.when(issueService.findById(1)).thenReturn(responseIssueDTO);

        mockMvc.perform(get("/issue/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseIssue.id").value(1))
                .andExpect(jsonPath("$.reporter_nickname").value("reporter_nickname"))
                .andExpect(jsonPath("$.assignee_nickname").value("assignee_nickname"));
    }

    @Test
    void testFindMyIssues() throws Exception {
        List<ResponseIssueDTO> issues = Collections.singletonList(responseIssueDTO);
        Mockito.when(issueService.findMyIssues(2)).thenReturn(issues);

        mockMvc.perform(get("/issue/my/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].responseIssue.id").value(1))
                .andExpect(jsonPath("$[0].reporter_nickname").value("reporter_nickname"))
                .andExpect(jsonPath("$[0].assignee_nickname").value("assignee_nickname"));
    }

    @Test
    void testUpdateIssueState() throws Exception {
        ChangeIssueStateRequest request = new ChangeIssueStateRequest();
        request.setOldState("open");
        request.setNewState("assigned");
        request.setAssignee_id(3);

        Mockito.when(issueService.findById(1)).thenReturn(responseIssueDTO);
        Mockito.when(issueService.updateIssue(any(IssueDTO.class))).thenReturn(issueDTO);
        Mockito.when(issueService.checkProjMember(eq(3), any(IssueDTO.class))).thenReturn(true);

        String requestBody = objectMapper.writeValueAsString(request);

        ResultActions result = mockMvc.perform(patch("/issue/1/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.state").value("assigned"));
    }

    @Test
    void testSearch() throws Exception {
        List<IssueDTO> issues = Collections.singletonList(issueDTO);
        Mockito.when(issueService.search("keyword")).thenReturn(issues);

        mockMvc.perform(get("/issue/search?keyword=keyword"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }
}