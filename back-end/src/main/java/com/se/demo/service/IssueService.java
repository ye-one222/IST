package com.se.demo.service;
import com.se.demo.dto.IssueDTO;
import com.se.demo.entity.IssueEntity;
import com.se.demo.repository.IssueRepository;
import com.se.demo.repository.ProjectRepository;
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
    public final ProjectRepository projectRepository;

    public IssueEntity createIssue(IssueDTO issueDTO){
        System.out.println("PROJECTID:"+issueDTO.getProject_id());
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

        issueDTO.setProject_id(issueEntity.getProject().getId());

        return issueDTO;
    }

   /* public static IssueDTO toIssueDTO(IssueEntity issueEntity){
        return new IssueDTO(issueEntity);
    }*/

    //public static IssueEntity toIssueEntity(IssueDTO issueDTO) {
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

    // 이슈 상태를 assigned로 변경하는 메서드
    public boolean changeIssueStateToAssigned(int issueId, int userId, String state) {

        return true; // 임시로 true를 반환하도록 설정하였습니다.
    }

    public boolean changeIssueStateToResolved(int issueId, int userId, String state) {
        return true;
    }

    public boolean changeIssueStateToClosed(int issueId, int userId, String state) {
        return true;
    }

    public boolean changeIssueStateToReopened(int issueId, int userId, String state) {
        return true;
    }
}