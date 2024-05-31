package com.se.demo;

import com.se.demo.controller.IssueController;
import com.se.demo.controller.MemberController;
import com.se.demo.controller.ProjectController;
import com.se.demo.entity.IssueEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.RestTemplate;

public class IssueConsole {
    public static void main(String[] args) {
        String baseUrl = "http://localhost:8081/issue";

        // RestTemplate 생성
        RestTemplate restTemplate = new RestTemplate();

        // 스프링 부트 애플리케이션 컨텍스트를 로드합니다.
        ConfigurableApplicationContext context = SpringApplication.run(MemberConsole.class, args);

        // MemberController 컨트롤러의 빈을 가져옵니다.
        MemberController memberController = context.getBean(MemberController.class);

        //Project Controller
        ProjectController projectController = context.getBean(ProjectController.class);

        //issue controller
        IssueController issueController = context.getBean(IssueController.class);

        //Issue entity 생성
        IssueEntity issueEntity= new IssueEntity();


    }

}
