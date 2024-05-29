package com.se.demo.controller;

import com.se.demo.dto.AddCommentRequest;
import com.se.demo.dto.CommentResponse;
import com.se.demo.dto.IssueDTO;
import com.se.demo.entity.CommentEntity;
import com.se.demo.entity.IssueEntity;
import com.se.demo.entity.MemberEntity;
import com.se.demo.repository.MemberRepository;
import com.se.demo.service.CommentService;
import com.se.demo.service.IssueService;
import com.se.demo.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.events.Comment;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/api")
@Controller
@Setter
@Getter
public class CommentController {

    private final CommentService commentService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    // 특정 이슈의 댓글 목록 및 댓글 생성 페이지
    @GetMapping("/issue/{id}/comments")
    @ResponseBody
    public ResponseEntity<List<CommentResponse>> getCommentsForIssue(@PathVariable int id) {
        try {
            List<CommentResponse> comments = commentService.findAllByIssueId(id);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PostMapping("/comments/create")
    @ResponseBody
    public ResponseEntity<CommentResponse> saveComment(@RequestBody AddCommentRequest request, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            int issueId = request.getIssueId();
            String nickName = principal.getName();
            CommentResponse savedComment = commentService.save(request, nickName, issueId);
            return ResponseEntity.ok(savedComment);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }




    // 이슈 검색 페이지 , state로 검색
    @GetMapping("/search/state")
    public String showSearchPage() {
        return "issue_search";
    }

    // 이슈 검색 결과
    @GetMapping("/search/state/results")
    public String searchIssues(@RequestParam String Istate, Model model) {
        try {
            List<IssueEntity> searchList = commentService.search(Istate);
            model.addAttribute("searchList", searchList);
            return "issue_search";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }
}
