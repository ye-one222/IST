package com.se.demo.controller;

import com.se.demo.dto.IssueDTO;
import com.se.demo.dto.ResponseIssueDTO;
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

    @GetMapping("/{id}")
    public ResponseIssueDTO findById(@PathVariable Integer id) {
        return issueService.findById(id);
    }

    //내 이슈 찾기
    @GetMapping("/my/{user_id}")
    public List<ResponseIssueDTO> findMyIssues(@PathVariable Integer user_id){
        return issueService.findMyIssues(user_id);
    }

    //이슈 상태 변경 통합
    @PatchMapping("/{issue_id}/update/{user_id}")
    public ResponseEntity<?> updateIssueState(@PathVariable Integer issue_id, @PathVariable Integer user_id,@RequestBody ChangeIssueStateRequest request) {
        ResponseIssueDTO responseIssueDTO = issueService.findById(issue_id);
        IssueDTO issueDTO = responseIssueDTO.getResponseIssue();
        if (issueDTO == null) {
            return ResponseEntity.notFound().build(); // 요청된 issue_id에 해당하는 이슈가 없음
        }

        if (!issueDTO.getState().equals(request.getOldState())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("그 전 이슈상태가 요청과 다름");
        }

        issueDTO.setState(request.getNewState());
        if(request.getNewState().equals("assigned")){   //assigned이면
            if(issueDTO.getPl_id() == user_id){
                if(request.getAssignee_id() == null){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("assigned할거면 담당자를 입력하세여;;");
                }
                //여기서 멤버인지 검사하는 매소드 불러야함
                if(issueService.checkProjMember(request.getAssignee_id(), issueDTO)){
                    issueDTO.setAssignee_id(request.getAssignee_id());
                }else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 assignee가 플젝 멤버에 없습니다");
                }
            }else{
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("assign을 하려면 pl이어야하는데 너 pl아님");
            }
        }

         if(request.getNewState().equals("fixed")){  //fixed이면
             if(issueDTO.getAssignee_id() == user_id){
                 issueDTO.setFixer_id(issueDTO.getAssignee_id());
             }
             else{
                 return ResponseEntity.status(HttpStatus.FORBIDDEN).body("fixed하려면 너가 assignee여야하는데 너 아님");
             }
        }

        if(request.getNewState().equals("resolved") && !Objects.equals(issueDTO.getReporter_id(), user_id)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("resolved하려면 리포터여야하는 너 리포터 아님");
        }

        if(request.getNewState().equals("closed")||request.getNewState().equals("reopened")){
            if(issueDTO.getPl_id() != user_id) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("closed, reopened하려면 pl이어야하는데 너 아님");
        }

        IssueDTO updatedIssue = issueService.updateIssue(issueDTO);
        if (updatedIssue != null) {
            return ResponseEntity.ok(updatedIssue); // 성공적으로 업데이트된 이슈를 반환
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update issue."); // 이슈 업데이트 실패
        }
    }

    @GetMapping("/search")
    public List<IssueDTO> search(@RequestParam String keyword) {
        return issueService.search(keyword);
    }
    /* 이건 JSON으로 요청받는 버전
    @PostMapping("/search")
    public List<IssueDTO> search(@RequestBody Map<String, String> body) {
        String keyword = body.get("keyword");
        // keyword를 사용하여 검색 작업 수행
        return yourSearchService.search(keyword);
    }*/
}