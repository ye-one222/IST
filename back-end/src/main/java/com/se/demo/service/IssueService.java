package com.se.demo.service;
import com.se.demo.dto.ChangeIssueStateRequest;
import com.se.demo.dto.IssueDTO;
import com.se.demo.entity.IssueEntity;
import com.se.demo.entity.MemberEntity;
import com.se.demo.repository.IssueRepository;
import com.se.demo.repository.MemberRepository;
import com.se.demo.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Getter
@Setter
public class IssueService {
    public final IssueRepository issueRepository;
    public final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    public IssueEntity createIssue(IssueDTO issueDTO){
        //System.out.println("PROJECTID:"+issueDTO.getProject_id());
        IssueEntity issueEntity = IssueEntity.toIssueEntity(issueDTO, projectRepository);
        issueRepository.save(issueEntity);
        return issueEntity;
    }

    public IssueDTO findById(Integer id){
        Optional<IssueEntity> optionalIssueEntity = issueRepository.findById(id);
        if(optionalIssueEntity.isPresent()){
            IssueEntity issueEntity = optionalIssueEntity.get();
            return IssueDTO.toIssueDTO(issueEntity);
        }else {
            return null;
        }
    }

    public List<IssueDTO> findMyIssues(Integer user_id){
        Optional<List<IssueEntity>> optionalIssueEntities = Optional.ofNullable(issueRepository.findByReporterIdOrAssigneeIdOrPlId(user_id, user_id, user_id));
        if(optionalIssueEntities.isPresent()){
            List<IssueEntity> issueEntities = optionalIssueEntities.get();
            //map으로 엔티티리스트를 DTO리스트로 변경
            List<IssueDTO> issueDTOs = issueEntities.stream()
                    .map(IssueDTO::toIssueDTO)
                    .collect(Collectors.toList());
            return issueDTOs;
        }else{
            return null;
        }
    }

    public IssueDTO updateIssue(IssueDTO issueDTO){
        IssueEntity issueEntity = IssueEntity.toIssueEntity(issueDTO, projectRepository);
        issueRepository.save(issueEntity);
        return IssueDTO.toIssueDTO(issueEntity);
    }

    public List<IssueDTO> search(String keyword) {
        //title에 keyword가 포함된 issueEntity List 반환
        List<IssueEntity> issueEntityList = new ArrayList<>(issueRepository.findByTitleContaining(keyword));

        //reporter nickname과 keyword가 같은 issueEntity List 반환
        MemberEntity reporterEntity = memberRepository.findByNickname(keyword)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member Nickname"));
        List<IssueEntity> reporterIssues = issueRepository.findByReporterId(reporterEntity.getUser_id());
        issueEntityList.addAll(reporterIssues);

        //assignee nickname과 keyword가 같은 issueEntity List 반환
        MemberEntity assigneeEntity = memberRepository.findByNickname(keyword)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member Nickname"));
        List<IssueEntity> assigneeIssues = issueRepository.findByAssigneeId(assigneeEntity.getUser_id());
        issueEntityList.addAll(assigneeIssues);


        //중복 제거
        issueEntityList = issueEntityList.stream()
                .distinct()
                .collect(Collectors.toList());

        return issueEntityList.stream()
                .map(IssueDTO::toIssueDTO)
                .collect(Collectors.toList());
    }
}