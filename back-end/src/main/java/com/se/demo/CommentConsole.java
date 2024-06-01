package com.se.demo;

import com.se.demo.dto.CommentDTO;
import com.se.demo.dto.MemberDTO;
import com.se.demo.entity.CommentEntity;
import com.se.demo.entity.IssueEntity;
import com.se.demo.entity.MemberEntity;
import com.se.demo.service.CommentService;
import com.se.demo.service.MemberService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Getter
@Setter
@NoArgsConstructor
@SpringBootApplication
public class CommentConsole {
    public static void main(String[] args) {
        // API 호출할 URL
        String baseUrl = "http://localhost:8081/api";

        // 스프링 부트 애플리케이션 컨텍스트를 로드합니다.
        ConfigurableApplicationContext context = SpringApplication.run(IssueConsole.class, args);

        // CommentService 빈 가져오기
        CommentService commentService = context.getBean(CommentService.class);
        MemberService memberService = context.getBean(MemberService.class);

        CommentEntity commentEntity = new CommentEntity();

        // RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your nickname: ");
        String nickName = scanner.nextLine();


        // 이슈 선택
        System.out.print("Enter the ID of the issue to comment on: ");
        int issueId = scanner.nextInt();
        scanner.nextLine();  // consume the newline


        // 코멘트 내용 입력
        System.out.print("Enter your comment: ");
        String commentText = scanner.nextLine();

        // 현재 시간 설정
        LocalDateTime createdDate = LocalDateTime.now();

        // 새로운 코멘트 생성
        CommentDTO newComment = new CommentDTO();
        newComment.setDescription(commentText);
        newComment.setCreated_date(createdDate);
        newComment.setIssue_id(issueId);

        // HttpHeaders 설정
        /*HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // HttpEntity 생성
        HttpEntity<CommentDTO> requestEntity = new HttpEntity<>(newComment, headers);

        try {
            // 코멘트 저장 API 호출
            ResponseEntity<CommentDTO> response = restTemplate.exchange(
                    baseUrl + "/comments/create",
                    HttpMethod.POST,
                    requestEntity,
                    CommentDTO.class
            );

            // 저장된 코멘트 출력
            CommentDTO savedComment = response.getBody();
            System.out.println("Saved Comment: " + savedComment);

            // 특정 이슈의 댓글 목록 조회 API 호출
            ResponseEntity<List<CommentDTO>> commentResponse = restTemplate.exchange(
                    baseUrl + "/issue/" + issueId + "/comments",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CommentDTO>>() {}
            );*/
        try {
            // 코멘트 저장 API 호출
            ResponseEntity<CommentDTO> response = restTemplate.postForEntity(
                    baseUrl + "/comments/create",
                    newComment,
                    CommentDTO.class
            );

            // 저장된 코멘트 출력
            CommentDTO savedComment = response.getBody();
            System.out.println("Saved Comment: " + savedComment);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                System.out.println("Invalid member nickname or issue ID.");
            } else {
                System.err.println("Failed to save comment: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Failed to save comment: " + e.getMessage());
        }

    }
}
