package com.se.demo.controller;

import com.se.demo.dto.IssueDTO;
import com.se.demo.entity.IssueEntity;
import com.se.demo.service.IssueService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.se.demo.dto.ChangeIssueStateRequest;


import java.util.List;

import java.util.Objects;

@Setter
@Getter
@RestController
@RequiredArgsConstructor
@RequestMapping("/issue")
public class IssueController {
    private final IssueService issueService;

    //create
    @PostMapping("/create")
    public IssueDTO createIssue(@ModelAttribute IssueDTO issueDTO) {
        //받아온 issue 정보를 디비에 저장해줘야지
        //서비스의 매소드로 넘겨주기
        IssueEntity issueEntity = issueService.createIssue(issueDTO);


        return IssueService.toIssueDTO(issueEntity);
    }

    @GetMapping("/{id}")
    public IssueDTO findById(@PathVariable Integer id) {
        return issueService.findById(id);
    }

    @GetMapping("/my/{user_id}")
    public List<IssueDTO> findMyIssues(@PathVariable Integer user_id){
        return issueService.findMyIssues(user_id);
    }

    @PatchMapping("/{issue_id}/toAssigned/{user_id}")
    public ResponseEntity<?> changeIssueToAssigned(@PathVariable Integer issue_id, @PathVariable Integer user_id,  ChangeIssueStateRequest request) {
        IssueDTO issueDTO = issueService.findById(issue_id);
        if (issueDTO == null) {
            return ResponseEntity.notFound().build(); // 요청된 issue_id에 해당하는 이슈가 없음
        }

        if (!issueDTO.getState().equals("new") || issueDTO.getPl_id() != user_id) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이슈상태가 new가 아니거나 너 피엘 아님"); // 상태가 new가 아니거나, user_id와 pl_id가 일치하지 않음
        }

        issueDTO.setState("assigned");
        issueDTO.setAssignee_id(request.getAssignee_id());

        IssueDTO updatedIssue = issueService.updateIssue(issueDTO);
        if (updatedIssue != null) {
            return ResponseEntity.ok(updatedIssue); // 성공적으로 업데이트된 이슈를 반환
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update issue."); // 이슈 업데이트 실패
        }
    }

    @PatchMapping("/{issue_id}/toFixed/{user_id}")
    public ResponseEntity<?> changeIssueToFixed(@PathVariable Integer issue_id, @PathVariable Integer user_id) {
        IssueDTO issueDTO = issueService.findById(issue_id);
        if (issueDTO == null) {
            return ResponseEntity.notFound().build(); // 요청된 issue_id에 해당하는 이슈가 없음
        }

        if (!issueDTO.getState().equals("assigned") || issueDTO.getAssignee_id() != user_id) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이슈가 assigned가 아니거나 너 assignee 아니잖아 fix 못해");
        }

        issueDTO.setState("fixed");
        issueDTO.setFixer_id(issueDTO.getAssignee_id());

        IssueDTO updatedIssue = issueService.updateIssue(issueDTO);
        if (updatedIssue != null) {
            return ResponseEntity.ok(updatedIssue); // 성공적으로 업데이트된 이슈를 반환
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update issue."); // 이슈 업데이트 실패
        }
    }
}