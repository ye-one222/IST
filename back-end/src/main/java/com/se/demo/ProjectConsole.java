package com.se.demo;

import com.se.demo.controller.ProjectController;
import com.se.demo.dto.ProjectDTO;
import com.se.demo.service.ProjectService;




public class ProjectConsole {

    public static void main(String[] args) {
        // ProjectDTO 객체 생성
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(1);
        projectDTO.setTitle("Sample Project");
        projectDTO.setLeader_id(123);

        // Members 추가
        MemberDTO member1 = new MemberDTO();
        member1.setId(1);
        member1.setName("John Doe");

        MemberDTO member2 = new MemberDTO();
        member2.setId(2);
        member2.setName("Jane Smith");

        List<MemberDTO> members = new ArrayList<>();
        members.add(member1);
        members.add(member2);

        projectDTO.setMembers(members);

        // ProjectEntity로 변환
        ProjectEntity projectEntity = projectDTO.toEntity();

        // 생성된 ProjectEntity 확인
        System.out.println("Converted ProjectEntity: " + projectEntity);
        ProjectController projectController = new ProjectController(new ProjectService());

        // 프로젝트 컨트롤러
        ProjectDTO projectDTO = new ProjectDTO();
        // projectDTO에 필요한 정보를 채워넣음
        ProjectDTO createdProject = projectController.createProject(projectDTO);

        // 생성된 프로젝트 정보를 출력
        System.out.println("Created Project: " + createdProject);

    }
}
