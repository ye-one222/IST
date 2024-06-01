package com.se.demo.service;
import com.se.demo.dto.ChangeIssueStateRequest;
import com.se.demo.dto.IssueAnalysisDTO;
import com.se.demo.dto.IssueDTO;
import com.se.demo.dto.ResponseIssueDTO;
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
import java.util.Objects;
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
        //여기서 유저디티오에서 플젝아이디 뽑고 그걸로 플젝 레포에서 리더 아이디값 받아오고 그걸로 이슈에 setPl_id넣기
        //projectRepository.findById(issueDTO.getProject_id()).get().getLeader_id();
        System.out.println("issueDTO.getProject_id() = "+issueDTO.getProject_id());
        int pl_id = projectRepository.findById(issueDTO.getProject_id())
                .orElseThrow(() -> new RuntimeException("해당 프로젝트 아이디가 디비에 없음")).getLeader_id();

        System.out.println("pl id = "+pl_id);
        issueDTO.setPl_id(pl_id);

        IssueEntity issueEntity = IssueEntity.toIssueEntity(issueDTO, projectRepository);
        issueRepository.save(issueEntity);
        return issueEntity;
    }

    public ResponseIssueDTO findById(Integer id){
        Optional<IssueEntity> optionalIssueEntity = issueRepository.findById(id);
        if(optionalIssueEntity.isPresent()){
            IssueEntity issueEntity = optionalIssueEntity.get();
            //이슈엔티티를 그대로 바꾸고
            IssueDTO issueDTO = IssueDTO.toIssueDTO(issueEntity);
            ResponseIssueDTO responseIssueDTO = new ResponseIssueDTO(issueDTO);
            //여기서 받아온 이슈디티오 에서 픽서랑 어사이니 아이디가지고 멤버 디티오 받아오고
            Optional<MemberEntity> assignMemberEntity = memberRepository.findById(issueDTO.getAssignee_id());
            Optional<MemberEntity> reporterMemberEntity = memberRepository.findById(issueDTO.getReporter_id());

            //그 멤버 디티오에서 닉네임 추출해서 responseIssue에 set 함수로 넣기
            assignMemberEntity.ifPresent(entity -> responseIssueDTO.setAssignee_nickname(entity.getNickname()));
            reporterMemberEntity.ifPresent(entity -> responseIssueDTO.setReporter_nickname(entity.getNickname()));
            return responseIssueDTO;
        }else {
            return null;
        }
    }

    public List<ResponseIssueDTO> findMyIssues(Integer user_id){
        Optional<List<IssueEntity>> optionalIssueEntities = Optional.ofNullable(issueRepository.findByReporterIdOrAssigneeIdOrPlId(user_id, user_id, user_id));
        if(optionalIssueEntities.isPresent()){
            List<IssueEntity> issueEntities = optionalIssueEntities.get();
            //map으로 엔티티리스트를 DTO리스트로 변경
            List<ResponseIssueDTO> responseIssueDTOList = issueEntities.stream()
                    .map(issueEntity -> {
                        ResponseIssueDTO responseIssueDTO = new ResponseIssueDTO(IssueDTO.toIssueDTO(issueEntity));
                        // 여기서 fixer_nickname과 assignee_nickname을 설정할 수 있음
                        String reporterNickname = memberRepository.findById(issueEntity.getReporterId())
                                .orElseThrow(() -> new RuntimeException("해당 리포터가 디비에 없음")).getNickname();
                        responseIssueDTO.setReporter_nickname(reporterNickname);
                        //여기서 assignee_nickname
                        //상태가 new인지 확인하는 로직 필요하네
                        if(!Objects.equals(issueEntity.getState(), "new")){
                            String assigneeNickname = memberRepository.findById(issueEntity.getAssigneeId())
                                    .orElseThrow(() -> new RuntimeException("해당 담당자가 디비에 없음")).getNickname();
                            responseIssueDTO.setAssignee_nickname(assigneeNickname);
                        }
                        return responseIssueDTO;
                    })
                    .collect(Collectors.toList());
            return responseIssueDTOList;
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

    //통계분석
    //모든 이슈 서치하면서 각 달마다 개수 세기
  /*  public IssueAnalysisDTO countAnalysis(Integer user_id){
        //repo -> get all issue Entity
        IssueAnalysisDTO issueAnalysisDTO = new IssueAnalysisDTO();
        //내 이슈entity 찾기
        List<IssueEntity> issueEntityList = issueRepository.findByReporterIdOrAssigneeIdOrPlId(user_id, user_id, user_id);
        for(IssueEntity issueEntity : issueEntityList){
            if(issueEntity.getDate())
        }
        return issueAnalysisDTO;
    }*/


}