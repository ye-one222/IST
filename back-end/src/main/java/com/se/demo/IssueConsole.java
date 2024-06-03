package com.se.demo;

import com.se.demo.dto.ChangeIssueStateRequest;
import com.se.demo.dto.IssueDTO;
import com.se.demo.dto.ResponseIssueDTO;
import com.se.demo.entity.IssueEntity;
import com.se.demo.service.IssueService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import java.net.http.HttpClient;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
@Getter
@Setter
@NoArgsConstructor
@SpringBootApplication
public class IssueConsole {
    public static void main(String[] args) {
        String baseUrl = "http://localhost:8081/issue";




        Scanner scanner = new Scanner(System.in);

        RestTemplate restTemplate = new RestTemplate();


        // 스프링 부트 애플리케이션 컨텍스트를 로드합니다.
        ConfigurableApplicationContext context = SpringApplication.run(IssueConsole.class, args);
        // 이슈 생성
        IssueService issueService = context.getBean(IssueService.class);
        IssueDTO issueDTO = new IssueDTO();



        // 특정이슈 상세 조회
        System.out.println("특정 이슈 상세 조회");
        System.out.print("Enter Issue ID: ");
        int issueId = scanner.nextInt();
        ResponseEntity<ResponseIssueDTO> findByIdResponse = restTemplate.getForEntity(baseUrl + "/" + issueId, ResponseIssueDTO.class);
        System.out.println("Find Issue by ID Response: " + findByIdResponse.getBody());

        // 나의 모든 이슈 조회
        System.out.println("나의 모든 이슈 조회");
        System.out.print("Enter UserId: ");
        int userId = scanner.nextInt();
        ResponseEntity<ResponseIssueDTO[]> findMyIssuesResponse = restTemplate.getForEntity(baseUrl + "/my/" + userId, ResponseIssueDTO[].class);
        System.out.println("Find My Issues Response: " + Arrays.toString(findMyIssuesResponse.getBody()));

        // 이슈 상태 변경
        System.out.println("이슈상태 변경");
        System.out.println("Enter issue title: ");
        String title = scanner.next();
        issueDTO.setTitle(title);

        System.out.println("Enter issue description: ");
        String description = scanner.next();
        issueDTO.setDescription(description);

        System.out.println("Enter project ID: ");
        int projectId = scanner.nextInt();
        issueDTO.setProject_id(projectId);

        System.out.println("Enter reporter ID: ");
        int reporterId = scanner.nextInt();
        issueDTO.setReporter_id(reporterId);

        IssueEntity createdIssueEntity = issueService.createIssue(issueDTO);
        System.out.println("Created Issue: " + createdIssueEntity);

        // 이슈 상태 변경
        System.out.println("Enter issue ID to update state: ");
        //int issueId = scanner.nextInt();

        ChangeIssueStateRequest changeIssueStateRequest = new ChangeIssueStateRequest();
        changeIssueStateRequest.setOldState("open");
        changeIssueStateRequest.setNewState("assigned");
        changeIssueStateRequest.setAssignee_id(reporterId); // Example assignee ID

        IssueDTO updateIssueStateResponse = issueService.updateIssue(issueDTO);
        System.out.println("Update Issue State Response: " + updateIssueStateResponse);


        // 이슈 검색
        // 키워드 입력 받기
        System.out.print("Enter keyword to search for issues: ");
        String keyword = scanner.nextLine();

        // 이슈 검색
        List<IssueDTO> searchResults = null;
        try {
            String searchUrl = baseUrl + "/issues/search?keyword=" + keyword;
            ResponseEntity<IssueDTO[]> response = restTemplate.getForEntity(searchUrl, IssueDTO[].class);
            searchResults = Arrays.asList(response.getBody());
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                System.out.println("No issues found with the keyword: " + keyword);
            } else {
                System.err.println("Error searching for issues: " + e.getMessage());
            }
            return;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            return;
        }

        // 검색 결과 출력
        if (searchResults != null && !searchResults.isEmpty()) {
            System.out.println("Search Results:");
            for (issueDTO = (IssueDTO) searchResults;;) {
                System.out.println(issueDTO);
            }
        } else {
            System.out.println("No issues found with the keyword: " + keyword);
        }
    }
}
