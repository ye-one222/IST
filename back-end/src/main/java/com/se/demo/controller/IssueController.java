package com.se.demo.controller;

import com.se.demo.dto.IssueAnalysisDTO;
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
    //이슈 통계 분석
    @GetMapping("/analysis/{user_id}")
    public IssueAnalysisDTO searchIssuesAnalysis(@PathVariable Integer user_id){
        return issueService.countAnalysis(user_id);
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
}