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
        IssueEntity issueEntity = toIssueEntity(issueDTO);
        issueRepository.save(issueEntity);
        return issueEntity;
    }

    public IssueDTO findById(Integer id){
        Optional<IssueEntity> optionalIssueEntity = issueRepository.findById(id);
        if(optionalIssueEntity.isPresent()){
            IssueEntity issueEntity = optionalIssueEntity.get();
            return IssueService.toIssueDTO(issueEntity);
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
                    .map(IssueService::toIssueDTO)
                    .collect(Collectors.toList());
            return issueDTOs;
        }else{
            return null;
        }
    }

    public IssueDTO updateIssue(IssueDTO issueDTO){
        IssueEntity issueEntity = toIssueEntity(issueDTO);
        issueRepository.save(issueEntity);
        return IssueService.toIssueDTO(issueEntity);
    }


    public static IssueDTO toIssueDTO(IssueEntity issueEntity){
        IssueDTO issueDTO = new IssueDTO();
        issueDTO.setId(issueEntity.getId());
        issueDTO.setTitle((issueEntity.getTitle()));
        issueDTO.setDate(issueEntity.getDate());
        issueDTO.setDescription(issueEntity.getDescription());
        issueDTO.setPriority(issueEntity.getPriority());
        issueDTO.setAssignee_id(issueEntity.getAssigneeId());
        issueDTO.setFixer_id(issueEntity.getFixerId());
        issueDTO.setPl_id(issueEntity.getPlId());
        issueDTO.setReporter_id(issueEntity.getReporterId());
        issueDTO.setState(issueEntity.getState());

        //issueDTO.setProject_id(issueEntity.getProject().getId());
        if (issueEntity.getProject() != null) {
            issueDTO.setProject_id(issueEntity.getProject().getId());
        }

        return issueDTO;
    }


    public IssueEntity toIssueEntity(IssueDTO issueDTO) { //static 빼도 되나여
        IssueEntity issueEntity = new IssueEntity();
        issueEntity.setId(issueDTO.getId());
        issueEntity.setTitle(issueDTO.getTitle());
        issueEntity.setDescription(issueDTO.getDescription());
        issueEntity.setReporterId(issueDTO.getReporter_id());
        issueEntity.setFixerId(issueDTO.getFixer_id());
        issueEntity.setAssigneeId(issueDTO.getAssignee_id());
        issueEntity.setPriority(issueDTO.getPriority());
        issueEntity.setState(issueDTO.getState());
        issueEntity.setPlId(issueDTO.getPl_id());

        issueEntity.setProject(projectRepository.findById(issueDTO.getProject_id())
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID")));;

        return issueEntity;
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
                .map(IssueService::toIssueDTO)
                .collect(Collectors.toList());
    }
}