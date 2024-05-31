package com.se.demo.controller;

import com.se.demo.dto.IssueDTO;
import com.se.demo.dto.ProjectDTO;
import com.se.demo.entity.IssueEntity;
import com.se.demo.entity.ProjectEntity;
import com.se.demo.service.IssueService;
import com.se.demo.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping("/create")
    public ProjectDTO createProject(@RequestBody ProjectDTO projectDTO){ //@ModelAttribute 사용했으므로 form으로만 가능, JSON으로 받으려면 @RequestBody로 바꿔야함
        //System.out.println("projectDTO = " + projectDTO);
        ProjectEntity savedEntity = projectService.save(projectDTO);
        return ProjectDTO.toProjectDTO(savedEntity);
    }

    @GetMapping("/{project_id}")
    public ProjectDTO findByProjectId(@PathVariable int project_id) {
        return projectService.findById(project_id);
    }

    @GetMapping("/my/{user_id}")
    public List<ProjectDTO> findByUserId(@PathVariable int user_id) {
        return projectService.findByUserId(user_id);
    }

    @GetMapping("/{project_id}/issues")
    public List<IssueDTO> findIssuesByProjectId(@PathVariable int project_id) {
        return projectService.findByProjectId(project_id);
    }

    @PostMapping("/issue/create")
    public IssueDTO createIssue(@RequestBody IssueDTO issueDTO) {
        //받아온 issue 정보를 디비에 저장해줘야지
        //서비스의 매소드로 넘겨주기
        //IssueEntity issueEntity = issueService.createIssue(issueDTO);
        //return IssueService.toIssueDTO(issueEntity);
        IssueEntity issueEntity = projectService.createIssue(issueDTO);
        return IssueDTO.toIssueDTO(issueEntity);
    }
}
