package com.se.demo.service;
import com.se.demo.dto.IssueDTO;
import com.se.demo.entity.IssueEntity;
import com.se.demo.repository.IssueRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

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

    public IssueEntity createIssue(IssueDTO issueDTO){

        IssueEntity issueEntity = toIssueEntity(issueDTO);
        //레포에 함수가 있는지 알아봐야게따 save가 insert하는거네
        issueRepository.save(issueEntity);
        return issueEntity;
    }

    public IssueDTO findbyId(Integer id){
        Optional<IssueEntity> optionalIssueEntity = issueRepository.findById(id);
        if(optionalIssueEntity.isPresent()){
            IssueEntity issueEntity = optionalIssueEntity.get();
            return IssueService.toIssueDTO(issueEntity);
        }else {
            return null;
        }
    }

    /*public List<IssueDTO> findMyIssues(Integer user_id){
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
    }*/



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
        return issueDTO;
    }

   /* public static IssueDTO toIssueDTO(IssueEntity issueEntity){
        return new IssueDTO(issueEntity);
    }*/

    public static IssueEntity toIssueEntity(IssueDTO issueDTO) {
        IssueEntity issueEntity = new IssueEntity();
        issueEntity.id = issueDTO.getId();
        issueEntity.title = issueDTO.getTitle();
        issueEntity.description = issueDTO.getDescription();
        issueEntity.reporterId = issueDTO.getReporter_id();

        issueEntity.fixerId = issueDTO.getFixer_id();
        issueEntity.assigneeId = issueDTO.getAssignee_id();
        issueEntity.priority = issueDTO.getPriority();
        issueEntity.state = issueDTO.getState();
        issueEntity.plId = issueDTO.getPl_id();
        return issueEntity;
    }
}