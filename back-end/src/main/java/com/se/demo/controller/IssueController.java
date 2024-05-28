package com.se.demo.controller;

import com.se.demo.dto.IssueDTO;
import com.se.demo.entity.IssueEntity;
import com.se.demo.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/issue")
public class IssueController {
    private final IssueService issueService;
    //create
    @PostMapping("/create")
    public IssueDTO createIssue(@ModelAttribute IssueDTO issueDTO){
        //받아온 issue 정보를 디비에 저장해줘야지
        //서비스의 매소드로 넘겨주기
        IssueEntity issueEntity =  issueService.createIssue(issueDTO);


        return IssueService.toIssueDTO(issueEntity);
    }

    @GetMapping("/{id}")
    public IssueDTO findById(@PathVariable Integer id){
        return issueService.findbyId(id);
    }

    //@GetMapping("/my/{user_id}")
    /*public List<IssueDTO> findMyIssues(@PathVariable Integer user_id){
        return issueService.findMyIssues(user_id);
    }*/
}