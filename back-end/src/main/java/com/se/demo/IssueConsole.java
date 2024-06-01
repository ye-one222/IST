package com.se.demo;

import com.se.demo.dto.ChangeIssueStateRequest;
import com.se.demo.dto.IssueDTO;
import com.se.demo.dto.ResponseIssueDTO;
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



        // 특정이슈 상세 조회
        System.out.print("Enter Issue ID: ");
        int issueId = scanner.nextInt();
        ResponseEntity<ResponseIssueDTO> findByIdResponse = restTemplate.getForEntity(baseUrl + "/" + issueId, ResponseIssueDTO.class);
        System.out.println("Find Issue by ID Response: " + findByIdResponse.getBody());

        // 나의 모든 이슈 조회
        System.out.print("Enter UserId: ");
        int userId = scanner.nextInt();
        ResponseEntity<ResponseIssueDTO[]> findMyIssuesResponse = restTemplate.getForEntity(baseUrl + "/my/" + userId, ResponseIssueDTO[].class);
        System.out.println("Find My Issues Response: " + Arrays.toString(findMyIssuesResponse.getBody()));

        // 이슈 상태 변경
        System.out.print("Enter Issue ID to update state: ");
        int issueIdToUpdate = scanner.nextInt();
        ChangeIssueStateRequest changeIssueStateRequest = new ChangeIssueStateRequest();
        changeIssueStateRequest.setOldState("open");
        changeIssueStateRequest.setNewState("assigned");
        changeIssueStateRequest.setAssignee_id(userId); // Example assignee ID

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ChangeIssueStateRequest> updateRequestEntity = new HttpEntity<>(changeIssueStateRequest, headers);

        // PATCH 요청 본문 설정
        String requestBody = "{\"oldState\":\"open\", \"newState\":\"assigned\", \"assignee_id\":11}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        // PATCH 요청 보내기
        ResponseEntity<String> responseEntity = restTemplate.exchange(baseUrl, HttpMethod.PATCH, requestEntity, String.class);

        // 응답 확인
        HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
        if (statusCode == HttpStatus.OK) {
            System.out.println("PATCH request successful.");
        } else {
            System.out.println("PATCH request failed. Status code: " + statusCode);
        }


        // ResponseEntity<ResponseIssueDTO> updateIssueStateResponse = restTemplate.exchange(baseUrl + "/" + issueIdToUpdate + "/update/" + userId, HttpMethod.PATCH, updateRequestEntity, ResponseIssueDTO.class);
       // System.out.println("Update Issue State Response: " + updateIssueStateResponse.getBody());
       // int issueId1 = scanner.nextInt();

        /*ChangeIssueStateRequest changeIssueStateRequest = new ChangeIssueStateRequest();
        changeIssueStateRequest.setOldState("open");
        changeIssueStateRequest.setNewState("assigned");
        changeIssueStateRequest.setAssignee_id(userId); // Example assignee ID

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestBody = "{\"oldState\":\"open\", \"newState\":\"assigned\", \"assignee_id\":11}";

        // HTTP 요청 엔티티 생성
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // PATCH 요청 보내기
        ResponseEntity<ResponseIssueDTO> updateIssueStateResponse = restTemplate.exchange(baseUrl + "/" + issueId1 + "/update/" + userId, HttpMethod.PATCH, requestEntity, ResponseIssueDTO.class);

        // 응답 출력
        System.out.println("Update Issue State Response: " + updateIssueStateResponse.getBody());

        // 이슈 검색
        System.out.print("Enter keyword: ");
        String keyword = scanner.next();
        // 키워드가 비어있는지 확인
        if (!keyword.isEmpty()) {
            try {
                // 검색을 위한 URL 생성
                String searchUrl = UriComponentsBuilder.fromUriString(baseUrl)
                        .path("/search")
                        .queryParam("keyword", keyword)
                        .toUriString();

                // 이슈 검색 요청 및 응답 처리
                ResponseEntity<IssueDTO[]> searchResponse = restTemplate.getForEntity(searchUrl, IssueDTO[].class);
                IssueDTO[] issues = searchResponse.getBody();

                // 검색 결과 출력
                if (issues != null && issues.length > 0) {
                    System.out.println("Search Issues Response: ");
                    for (IssueDTO issue : issues) {
                        System.out.println(issue);
                    }
                } else {
                    System.out.println("No issues found with the keyword: " + keyword);
                }
            } catch (HttpClientErrorException | HttpServerErrorException ex) {
                System.err.println("Error: " + ex.getRawStatusCode() + " - " + ex.getStatusText());
            } catch (Exception ex) {
                System.err.println("Unexpected Error: " + ex.getMessage());
            }
        } else {
            System.out.println("Keyword is empty. Please enter a valid keyword.");
        }*/
    }
}
