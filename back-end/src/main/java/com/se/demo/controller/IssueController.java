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
        return issueService.findbyId(id);
    }

    //@GetMapping("/my/{user_id}")
    /*public List<IssueDTO> findMyIssues(@PathVariable Integer user_id){
        return issueService.findMyIssues(user_id);
    }*/
    /*@PatchMapping("/{issue_id}/toResolved/{user_id}")
    public ResponseEntity<IssueDTO> changeIssueToResolved(@PathVariable("issue_id") int issueId, @PathVariable("user_id") int userId, @RequestBody ChangeIssueStateRequest request) {
        boolean success = issueService.changeIssueStateToResolved(issueId, userId, request.getState());

        if (success) {
            // 성공 응답 반환
            return ResponseEntity.ok(new IssueDTO("Success", "이슈의 상태를 resolved로 변경하였습니다."));
        } else {
            // 실패 응답 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new IssueDTO("Failure", "이슈의 상태 변경에 실패하였습니다."));
        }

    }
    @PatchMapping("/{issue_id}/toClosed/{user_id}")
    public ResponseEntity<IssueDTO> changeIssueToClosed(@PathVariable("issue_id") int issueId, @PathVariable("user_id") int userId, @RequestBody ChangeIssueStateRequest request) {
        boolean success = issueService.changeIssueStateToClosed(issueId, userId, request.getState());

        if (success) {
            // 성공 응답 반환
            return ResponseEntity.ok(new IssueDTO("Success", "이슈의 상태를 closed로 변경하였습니다."));
        } else {
            // 실패 응답 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new IssueDTO("Failure", "이슈의 상태 변경에 실패하였습니다."));
        }

    }
    @PatchMapping("/{issue_id}/toClosed/{user_id}")
    public ResponseEntity<IssueDTO> changeIssueToReopened(@PathVariable("issue_id") int issueId, @PathVariable("user_id") int userId, @RequestBody ChangeIssueStateRequest request) {
        boolean success = issueService.changeIssueStateToReopened(issueId, userId, request.getState());

        if (success) {
            // 성공 응답 반환
            return ResponseEntity.ok(new IssueDTO("Success", "이슈의 상태를 reopened로 변경하였습니다."));
        } else {
            // 실패 응답 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new IssueDTO("Failure", "이슈의 상태 변경에 실패하였습니다."));
        }

    }*/
}