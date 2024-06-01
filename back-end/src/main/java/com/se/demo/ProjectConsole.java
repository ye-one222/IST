package com.se.demo;

import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.se.demo.controller.MemberController;
import com.se.demo.controller.ProjectController;
import com.se.demo.dto.IssueDTO;
import com.se.demo.dto.MemberDTO;
import com.se.demo.dto.ProjectDTO;
import com.se.demo.dto.ResponseProjectDTO;
import com.se.demo.entity.IssueEntity;
import com.se.demo.entity.MemberEntity;
import com.se.demo.entity.ProjectEntity;
import com.se.demo.repository.MemberRepository;
import com.se.demo.service.MemberService;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Getter
@Setter
@NoArgsConstructor
@SpringBootApplication
public class ProjectConsole {

    public static void main(String[] args) {

        String baseUrl = "http://localhost:8081/project";
        String memberBaseUrl = "http://localhost:8081/user";
        // RestTemplate 생성
        RestTemplate restTemplate = new RestTemplate();

        // 스프링 부트 애플리케이션 컨텍스트를 로드합니다.
        ConfigurableApplicationContext context = SpringApplication.run(com.se.demo.ProjectConsole.class, args);

        // 프로젝트 서비스와 멤버 서비스의 빈을 가져옵니다.
        ProjectService projectService = context.getBean(ProjectService.class);
        MemberService memberService = context.getBean(MemberService.class);


        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(1);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter project Title: ");
        String title = scanner.next();
        projectEntity.setTitle(title);

        System.out.println("Enter project Leader id: ");
        int Leader_id = scanner.nextInt();
        projectEntity.setLeader_id(Leader_id);
// 멤버 리스트 생성
        List<MemberEntity> memberEntities = new ArrayList<>();

        // 사용자로부터 멤버 정보 입력 받기
        System.out.println("Enter the number of members: ");
        int numberOfMembers = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (int i = 0; i < numberOfMembers; i++) {
            MemberEntity memberEntity = new MemberEntity();


            System.out.println("Enter nickname for member " + (i + 1) + ": ");
            String nickname = scanner.next();
            memberEntity.setNickname(nickname);


            // 리스트에 멤버 추가
            memberEntities.add(memberEntity);

        }

        // 멤버 엔티티 목록을 프로젝트 엔티티에 설정
        projectEntity.setMembers(memberEntities);

// 멤버 엔티티 목록을 프로젝트 엔티티에 설정
      //  projectEntity.setMembers(members);

        // Members 추가
        /*MemberDTO member1 = new MemberDTO();
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
        members.add(member2);*/


        //dto 생성
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(projectEntity.getId());
        projectDTO.setTitle(title);
        projectDTO.setLeader_id(Leader_id);
        //projectDTO.setMembers(memberEntities);




        //DTO 확인
        System.out.println("Created Project DTO : " + projectDTO);


        // HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // HttpEntity 생성
        HttpEntity<ProjectDTO> requestEntity = new HttpEntity<>(projectDTO, headers);

        // 생성된 ProjectDTO 확인
        System.out.println("Created ProjectDTO: " + projectDTO);

        // 프로젝트 생성 요청을 보냅니다.
        ResponseEntity<ProjectDTO> createProjectResponse = restTemplate.exchange(baseUrl + "/create", HttpMethod.POST, requestEntity, ProjectDTO.class);
        ProjectDTO createdProject = createProjectResponse.getBody();
        assert createdProject != null;

        // 프로젝트 생성 확인
        //System.out.println("Created Project: " + createdProject);

        ProjectEntity createdProjectEntity = projectService.save(projectDTO);
        System.out.println("Create Project Response: " + createdProjectEntity);


        // 프로젝트 조회 요청을 보냅니다.
        ResponseEntity<ProjectDTO> getProjectResponse = restTemplate.getForEntity(baseUrl + "/" + projectDTO.getId(), ProjectDTO.class);
        //ProjectDTO retrievedProject = getProjectResponse.getBody();
        //System.out.println("Search Project Response: " + retrievedProject);
        ResponseProjectDTO retrievedProject = projectService.findById(createdProjectEntity.getId());
        System.out.println("Retrieved Project: " + retrievedProject);

        //나의 전체 프로젝트 조회
        System.out.println("Enter your user ID: ");
        int userId = scanner.nextInt();
        //ResponseEntity<ProjectDTO[]> searchProjectResponse1 = restTemplate.getForEntity(baseUrl + "/my/" +userId, ProjectDTO[].class);
        List<ResponseProjectDTO> myProjects = projectService.findByUserId(userId);
        System.out.println("My Projects: " + myProjects);

        // 특정 프로젝트의 모든 이슈 조회
        //assert createdProject != null;
        ResponseEntity<IssueDTO[]> getProjectIssuesResponse = restTemplate.getForEntity(baseUrl + "/" + createdProjectEntity.getId() + "/issues", IssueDTO[].class);
        IssueDTO[] projectIssues = getProjectIssuesResponse.getBody();

        System.out.println("Get Project Issues Response: " + Arrays.toString(projectIssues));

        // 이슈 생성
        IssueDTO issueDTO = new IssueDTO();
        System.out.println("Enter issue title: ");
        String issueTitle = scanner.next();
        System.out.println("Enter issue reporter_id: ");
        int reporterId = scanner.nextInt();
        System.out.println("Enter issue description: ");
        String description = scanner.next();
        issueDTO.setTitle(issueTitle);
        issueDTO.setReporter_id(reporterId);
        issueDTO.setDescription(description);
        issueDTO.setId(1);
        issueDTO.setProject_id(createdProjectEntity.getId());

        IssueEntity createdIssueEntity = projectService.createIssue(issueDTO);
        System.out.println("Created Issue: " + createdIssueEntity);


    }
}


