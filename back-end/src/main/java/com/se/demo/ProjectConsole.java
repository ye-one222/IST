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
        RestTemplate restTemplate = new RestTemplate();
        ConfigurableApplicationContext context = SpringApplication.run(com.se.demo.ProjectConsole.class, args);
        ProjectService projectService = context.getBean(ProjectService.class);
        MemberService memberService = context.getBean(MemberService.class);
        // MemberRepository memberRepository = context.getBean(MemberRepository.class); // 이 부분은 불필요해 보입니다.

        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(1);
        Scanner scanner = new Scanner(System.in);

        System.out.println("프로젝트 생성");
        System.out.println("Enter project Title: ");
        String title = scanner.next();
        projectEntity.setTitle(title);

        System.out.println("Enter project Leader id: ");
        // String leaderNickname = scanner.next(); // 이 부분은 불필요해 보입니다.
        int leaderId = scanner.nextInt(); // 리더 ID를 입력 받습니다.
        projectEntity.setLeader_id(leaderId);

        List<MemberEntity> memberEntities = new ArrayList<>();
        System.out.println("Enter the number of members: ");
        int numberOfMembers = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (int i = 0; i < numberOfMembers; i++) {
            MemberEntity memberEntity = new MemberEntity();
            System.out.println("Enter nickname for member " + (i + 1) + ": ");
            String nickname = scanner.next();
            memberEntity.setNickname(nickname);
            memberEntities.add(memberEntity);
        }
        projectEntity.setMembers(memberEntities);

        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(projectEntity.getId());
        projectDTO.setTitle(title);
        projectDTO.setLeader_id(leaderId);
        //projectDTO.setMembers(memberEntities);

        //System.out.println("Created Project DTO : " + projectDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<ProjectDTO> requestEntity = new HttpEntity<>(projectDTO, headers);

        //System.out.println("Created ProjectDTO: " + projectDTO);

        ResponseEntity<ProjectDTO> createProjectResponse = restTemplate.exchange(baseUrl + "/create", HttpMethod.POST, requestEntity, ProjectDTO.class);
        ProjectDTO createdProject = createProjectResponse.getBody();
        assert createdProject != null;
        //생성된 프로젝트 정보 출력
        ProjectEntity createdProjectEntity = projectService.save(projectDTO);
        System.out.println("Create Project Response: ");
        System.out.println("ID: " + createdProjectEntity.getId());
        System.out.println("Title: " + createdProjectEntity.getTitle());
        System.out.println("Leader ID: " + createdProjectEntity.getLeader_id());
        List<MemberDTO> members = createdProject.getMembers();
        //System.out.print("Members: ");
        //for (MemberDTO member : members) {
        //    System.out.print(member.getNickname() + ", ");
        //}
        System.out.println("프로젝트 생성 완료");

        // 프로젝트 상세 정보 조회
        //ResponseEntity<ProjectDTO> getProjectResponse = restTemplate.getForEntity(baseUrl + "/" + projectDTO.getId(), ProjectDTO.class);
       // ProjectDTO retrievedProjectDTO = getProjectResponse.getBody();
        //System.out.println("Retrieved Project: ");
        //System.out.println("ID: " + retrievedProjectDTO.getId());
        //System.out.println("Title: " + retrievedProjectDTO.getTitle());
        //System.out.println("Leader ID: " + retrievedProjectDTO.getLeader_id());
        //List<MemberDTO> members1 = retrievedProjectDTO.getMembers();
        //System.out.print("Members: ");
        //for (MemberDTO member : members1) {
        //    System.out.print(member.getNickname() + ", ");
        //}
        //System.out.println();

        //사용자의 프로젝트 조회
        System.out.println("사용자의 프로젝트 조회");
        System.out.println("Enter your user ID: ");
        int userId = scanner.nextInt();
        List<ResponseProjectDTO> myProjects = projectService.findByUserId(userId);
        System.out.println("My Projects: ");
        for (ResponseProjectDTO project : myProjects) {
            System.out.println("ID: " + project.getProjectDTO());
            //System.out.println("Title: " + project.getTitle());
            System.out.println("Leader nickname: " + project.getLeader_nickname());
            System.out.println("프로젝트 조회 완료");
            //System.out.println("Members: " + project.getMembers());
            // 추가적으로 필요한 프로젝트 정보 출력
        }


        ResponseEntity<IssueDTO[]> getProjectIssuesResponse = restTemplate.getForEntity(baseUrl + "/" + createdProjectEntity.getId() + "/issues", IssueDTO[].class);
        IssueDTO[] projectIssues = getProjectIssuesResponse.getBody();

        System.out.println("Get Project Issues Response: " + Arrays.toString(projectIssues));

        System.out.println("이슈 생성");
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
        System.out.println("이슈 생성 완료");

    }
}


