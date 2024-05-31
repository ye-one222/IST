package com.se.demo;

import com.se.demo.controller.MemberController;
import com.se.demo.dto.MemberDTO;
import com.se.demo.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.filters.ExpiresFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@SpringBootApplication
public class MemberConsole {
    public static void main(String[] args) {
        String baseUrl = "http://localhost:8081/user";

        // RestTemplate 생성
        RestTemplate restTemplate = new RestTemplate();

        // 스프링 부트 애플리케이션 컨텍스트를 로드합니다.
        ConfigurableApplicationContext context = SpringApplication.run(MemberConsole.class, args);

        // MemberController 컨트롤러의 빈을 가져옵니다.
        MemberController memberController = context.getBean(MemberController.class);

        //Member Entity 생성
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setUser_id(1);
        memberEntity.setNickname("john");
        memberEntity.setPassword("123");

        // 생성된 MemberEntity 확인
        System.out.println("Created MemberEntity: " + memberEntity);

        // MemberDTO 객체 생성
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setUser_id(1);
        memberDTO.setNickname("john_doe");
        memberDTO.setPassword("password123");

        //회원가입 요청 보내기
        ResponseEntity<Map<String, Integer>> signupResponse = memberController.signup(memberDTO);
        System.out.println("Signup Response: " + signupResponse.getBody());



        // 로그인 요청을 보냅니다.
        ResponseEntity<MemberDTO> loginResponse = null;
        try {
            loginResponse = restTemplate.exchange(baseUrl + "/login", HttpMethod.POST, new HttpEntity<>(memberDTO), MemberDTO.class);

            // 응답 확인
            if (loginResponse.getStatusCode().is2xxSuccessful()) {
                System.out.println("Login successful!");
                MemberDTO responseBody = loginResponse.getBody();
                System.out.println("Login Response: " + responseBody);
            } else {
                System.out.println("Login failed");
            }
        } catch (HttpServerErrorException.InternalServerError ex) {
            System.err.println("Internal Server Error: " + ex.getMessage());
        } catch (HttpClientErrorException ex) {
            System.err.println("HTTP Client Error: " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("Unexpected Error: " + ex.getMessage());
        }
    }




}
