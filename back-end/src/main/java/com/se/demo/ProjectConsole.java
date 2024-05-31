package com.se.demo;

import com.se.demo.controller.MemberController;
import com.se.demo.controller.ProjectController;
import com.se.demo.dto.IssueDTO;
import com.se.demo.dto.MemberDTO;
import com.se.demo.dto.ProjectDTO;
import com.se.demo.entity.ProjectEntity;
import com.se.demo.service.ProjectService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@SpringBootApplication
public class ProjectConsole {

    public static void main(String[] args) {
        String baseUrl = "http://localhost:8081/project";
        // RestTemplate 생성
        RestTemplate restTemplate = new RestTemplate();

        // 스프링 부트 애플리케이션 컨텍스트를 로드합니다.
        ConfigurableApplicationContext context = SpringApplication.run(ProjectConsole.class, args);

        // MemberController 컨트롤러의 빈을 가져옵니다.
        MemberController memberController = context.getBean(MemberController.class);

        ProjectController projectController = context.getBean(ProjectController.class);

        // ProjectDTO 객체 생성
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(1);
        projectDTO.setTitle("Project");
        projectDTO.setLeader_id(1);
        //projectDTO.setIssues(1);  // issues 필드 값 설정

        //프로젝트 엔티티 생성
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(1);
        projectEntity.setTitle("Project");
        projectEntity.setLeader_id(1);

        //엔티티 확인
        System.out.println("Created ProjectEntity: " +projectEntity);


        // Issues 추가
        List<IssueDTO> issues = new ArrayList<>();
        IssueDTO issue1 = new IssueDTO();
        issue1.setId(1);
        issue1.setDescription("Issue 1");
        issues.add(issue1);

        IssueDTO issue2 = new IssueDTO();
        issue2.setId(2);
        issue2.setDescription("Issue 2");
        issues.add(issue2);

        projectDTO.setIssues(issues);

        // Members 추가
        MemberDTO member1 = new MemberDTO();
        member1.setUser_id(1);
        member1.setNickname("john");
        member1.setPassword("123");

        MemberDTO member2 = new MemberDTO();
        member2.setUser_id(2);
        member2.setNickname("jane");
        member2.setPassword("456");

        // 멤버 리스트에 추가
        List<MemberDTO> members = new ArrayList<>();
        members.add(member1);
        members.add(member2);

        // ProjectDTO에 멤버 리스트 추가
        projectDTO.setMembers(members);

        // HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // HttpEntity 생성
        HttpEntity<ProjectDTO> requestEntity = new HttpEntity<>(projectDTO, headers);

        // 생성된 ProjectDTO 확인
        System.out.println("Created ProjectDTO: " + projectDTO);

        // 프로젝트 생성 요청을 보냅니다.
        ResponseEntity<ProjectDTO> createProjectResponse = restTemplate.exchange(baseUrl + "/create", HttpMethod.POST, requestEntity, ProjectDTO.class);
        System.out.println("Create Project Response: " + createProjectResponse.getBody());
        System.out.println("ID: " + projectDTO.getId());
        System.out.println("Title: " + projectDTO.getTitle());
        System.out.println("Leader: " + projectDTO.getLeader_id());


        // 프로젝트 조회 요청을 보냅니다.
        ResponseEntity<ProjectDTO> getProjectResponse = restTemplate.getForEntity(baseUrl + "/" + projectDTO.getId(), ProjectDTO.class);
        System.out.println("Search Project Response: " + getProjectResponse.getBody());
        System.out.println("ID: " + projectDTO.getId());
        System.out.println("Title: " + projectDTO.getTitle());
        System.out.println("Leader: " + projectDTO.getLeader_id());

        //나의 전체 프로젝트 조회
        ResponseEntity<ProjectDTO[]> searchProjectResponse1 = restTemplate.getForEntity(baseUrl + "/my/"+ member1.getUser_id(), ProjectDTO[].class);
        System.out.println("Get My Project Response: " + Arrays.toString(searchProjectResponse1.getBody()));
        // 첫 번째 사용자의 프로젝트 조회 결과 출력


        ResponseEntity<ProjectDTO[]> searchProjectResponse2 = restTemplate.getForEntity(baseUrl + "/my/"+ member2.getUser_id(), ProjectDTO[].class);
        System.out.println("Get My Project Response: " + Arrays.toString(searchProjectResponse2.getBody()));
        // 두 번째 사용자의 프로젝트 조회 결과 출력

    }
}
