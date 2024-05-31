package com.se.demo;

import com.se.demo.dto.IssueDTO;
import com.se.demo.entity.IssueEntity;
import com.se.demo.entity.ProjectEntity;
import com.se.demo.repository.IssueRepository;
import com.se.demo.repository.MemberRepository;
import com.se.demo.repository.ProjectRepository;
import com.se.demo.service.IssueService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class IssueTrackingApplicationTests {
    private IssueRepository issueRepository;
    private ProjectRepository projectRepository;
    private MemberRepository memberRepository;
    private IssueService issueService;
    IssueDTO issueDTO = new IssueDTO();

/*
    @BeforeEach
    void setUp() {
        issueService = new IssueService(issueRepository, projectRepository, memberRepository);
        //issueDTO set get 테스트 확인 후
        issueDTO.setTitle("This is Test Title");
        issueDTO.setDescription("This is Test Description");
        issueDTO.setReporter_id(1);
        issueDTO.setProject_id(1);
        issueDTO.setPl_id(1);

    }
    @Test
    public void testIssueCreate(){

        IssueEntity createdIssueEntity = issueService.createIssue(issueDTO);

        assertNotNull(createdIssueEntity);
        //위의 Null 체크를 하면 첫번째 멤버 변수는 null 체크가 됨
        //그 다음 멤버변수인 title의 check 진행
        //assertEquals(1,createdIssueEntity.getId());
        //assertEquals("This is Test Title", createdIssueEntity.getTitle());

    }
*/
}
