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
    /*@PostMapping("/create")
    public IssueDTO createIssue(@ModelAttribute IssueDTO issueDTO) {
        //받아온 issue 정보를 디비에 저장해줘야지
        //서비스의 매소드로 넘겨주기
        IssueEntity issueEntity = issueService.createIssue(issueDTO);


        return IssueService.toIssueDTO(issueEntity);
    }*/

    @GetMapping("/{id}")
    public IssueDTO findById(@PathVariable Integer id) {
        return issueService.findById(id);
    }

    @GetMapping("/my/{user_id}")
    public List<IssueDTO> findMyIssues(@PathVariable Integer user_id){
        return issueService.findMyIssues(user_id);
    }

    @PatchMapping("/{issue_id}/update/{user_id}")
    public ResponseEntity<?> updateIssueState(@PathVariable Integer issue_id, @PathVariable Integer user_id,@RequestBody ChangeIssueStateRequest request) {
        IssueDTO issueDTO = issueService.findById(issue_id);
        if (issueDTO == null) {
            return ResponseEntity.notFound().build(); // 요청된 issue_id에 해당하는 이슈가 없음
        }

        if (!issueDTO.getState().equals(request.getOldState()) || issueDTO.getPl_id() != user_id) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이슈상태가 올바른 단계가 아니거나 너 권한 없음");
        }

        issueDTO.setState(request.getNewState());
        if(request.getNewState().equals("assigned")){   //assigned이면
            if(request.getAssignee_id() == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("assigned할거면 담당자를 입력하세여;;");
            }
            issueDTO.setAssignee_id(request.getAssignee_id());
        }
        if(request.getNewState().equals("fixed")){  //fixed이면
            issueDTO.setFixer_id(issueDTO.getAssignee_id());
        }

        IssueDTO updatedIssue = issueService.updateIssue(issueDTO);
        if (updatedIssue != null) {
            return ResponseEntity.ok(updatedIssue); // 성공적으로 업데이트된 이슈를 반환
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update issue."); // 이슈 업데이트 실패
        }
    }
/*
    @PatchMapping("/{issue_id}/toAssigned/{user_id}")
    public ResponseEntity<?> changeIssueToAssigned(@PathVariable Integer issue_id, @PathVariable Integer user_id,@RequestBody ChangeIssueStateRequest request) {
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
    }*/

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
/*
    @PatchMapping("/{issue_id}/toResolved/{user_id}")
    public ResponseEntity<?> changeIssueToResolved(@PathVariable Integer issue_id, @PathVariable Integer user_id) {
        IssueDTO issueDTO = issueService.findById(issue_id);
        if (issueDTO == null) {
            return ResponseEntity.notFound().build(); // 요청된 issue_id에 해당하는 이슈가 없음
        }

        if (!issueDTO.getState().equals("fixed") || issueDTO.getReporter_id() != user_id) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이슈가 fixed가 아니거나 너 reporter 아니잖아 resolved 못해");
        }

        issueDTO.setState("resolved");
        //issueDTO.setR(issueDTO.getAssignee_id());

        IssueDTO updatedIssue = issueService.updateIssue(issueDTO);
        if (updatedIssue != null) {
            return ResponseEntity.ok(updatedIssue); // 성공적으로 업데이트된 이슈를 반환
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update issue."); // 이슈 업데이트 실패
        }
    }

    @PatchMapping("/{issue_id}/toClosed/{user_id}")
    public ResponseEntity<?> changeIssueToClosed(@PathVariable Integer issue_id, @PathVariable Integer user_id) {
        IssueDTO issueDTO = issueService.findById(issue_id);
        if (issueDTO == null) {
            return ResponseEntity.notFound().build(); // 요청된 issue_id에 해당하는 이슈가 없음
        }

        if (!issueDTO.getState().equals("resolved") || issueDTO.getPl_id() != user_id) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이슈가 resolved 아니거나 너 pl 아니잖아 closed 못해");
        }

        issueDTO.setState("closed");
        //issueDTO.setR(issueDTO.getAssignee_id());

        IssueDTO updatedIssue = issueService.updateIssue(issueDTO);
        if (updatedIssue != null) {
            return ResponseEntity.ok(updatedIssue); // 성공적으로 업데이트된 이슈를 반환
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update issue."); // 이슈 업데이트 실패
        }
    }

    @PatchMapping("/{issue_id}/toReopened/{user_id}")
    public ResponseEntity<?> changeIssueToReopened(@PathVariable Integer issue_id, @PathVariable Integer user_id) {
        IssueDTO issueDTO = issueService.findById(issue_id);
        if (issueDTO == null) {
            return ResponseEntity.notFound().build(); // 요청된 issue_id에 해당하는 이슈가 없음
        }

        if (!issueDTO.getState().equals("closed") || issueDTO.getPl_id() != user_id) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이슈가 closed 아니거나 너 pl 아니잖아 reopened 못해");
        }

        issueDTO.setState("reopened");
        //issueDTO.setR(issueDTO.getAssignee_id());

        IssueDTO updatedIssue = issueService.updateIssue(issueDTO);
        if (updatedIssue != null) {
            return ResponseEntity.ok(updatedIssue); // 성공적으로 업데이트된 이슈를 반환
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update issue."); // 이슈 업데이트 실패
        }
    }*/
}