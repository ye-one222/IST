package com.se.demo.controller;

import com.se.demo.dto.CommentDTO;
import com.se.demo.dto.IssueDTO;
import com.se.demo.dto.ResponseCommentDTO;
import com.se.demo.entity.CommentEntity;
import com.se.demo.entity.IssueEntity;
import com.se.demo.entity.MemberEntity;
import com.se.demo.repository.MemberRepository;
import com.se.demo.service.CommentService;
import com.se.demo.service.IssueService;
import com.se.demo.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.events.Comment;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<List<ResponseCommentDTO>> getCommentsForIssue(@PathVariable int id) {
        try {
            List<ResponseCommentDTO> comments = commentService.findAllByIssueId(id);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    /*@PostMapping("/comments/create")
    @ResponseBody
    public ResponseEntity<?> saveComment(@RequestBody CommentDTO request, HttpServletRequest httpServletRequest) {
        try {
            int issueId = request.getIssue_id();
            HttpSession httpSession = httpServletRequest.getSession();
            String nickName = (String) httpSession.getAttribute("userNickname");
            CommentEntity savedCommentEntity = commentService.save(request, nickName, issueId);
            CommentDTO savedComment = commentService.toCommentDTO(savedCommentEntity);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(savedComment);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }*/

    @PostMapping("/comments/create")
    @ResponseBody
    public ResponseEntity<?> saveComment(@RequestBody CommentDTO request) {
        try {
            int issueId = request.getIssue_id();
            CommentEntity savedCommentEntity = commentService.save(request, issueId);
            CommentDTO savedComment = commentService.toCommentDTO(savedCommentEntity);

            Map<String, Object> response = new HashMap<>();
            response.put("creater_id", savedComment.getCreater_id());
            response.put("description", savedComment.getDescription());
            response.put("created_date", savedComment.getCreated_date());
            response.put("issue_id", savedComment.getIssue_id());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
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