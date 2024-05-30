package com.se.demo.service;

import com.se.demo.dto.CommentDTO;
import com.se.demo.dto.IssueDTO;
import com.se.demo.entity.CommentEntity;
import com.se.demo.entity.IssueEntity;
import com.se.demo.entity.MemberEntity;
import com.se.demo.repository.CommentRepository;
import com.se.demo.repository.IssueRepository;
import com.se.demo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service

public class CommentService {
    @Autowired

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final IssueRepository issueRepository; // IssueRepository를 주입 받아야 함

    public CommentEntity save(CommentDTO request, String nickName, int issueId) {
        MemberEntity creater = memberRepository.findByNickname(nickName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member Nickname"));

        IssueEntity issueEntity = issueRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid issue ID"));
        IssueDTO issueDTO = IssueService.toIssueDTO(issueEntity);
        issueDTO.getComments().add(request);

        request.setCreated_date(LocalDateTime.now());

        CommentEntity commentEntity = toCommentEntity(request, issueEntity, creater);
        commentRepository.save(commentEntity);
        return commentEntity;
    }

    public List<IssueEntity> search(String Istate) {
        // 검색 로직 구현
        // 예: return issueRepository.findByState(Istate);
        return issueRepository.findByState(Istate);
    }


    @Transactional(readOnly = true)
    public List<CommentDTO> findAllByIssueId(int id) {
        List<CommentEntity> comments = commentRepository.findByIssueId(id);
        //Long issueId = null;
        return comments.stream()
                .map(CommentDTO::new)
                .collect(Collectors.toList());
    }

    public CommentEntity toCommentEntity(CommentDTO commentDTO, IssueEntity issueEntity, MemberEntity createrEntity){
        CommentEntity commentEntity = new CommentEntity();

        commentEntity.setId(commentDTO.getId());
        commentEntity.setDescription(commentDTO.getDescription());
        commentEntity.setCreaterId(createrEntity);
        commentEntity.setIssue(issueEntity);
        commentEntity.setCreatedDate(commentDTO.getCreated_date());

        return commentEntity;
    }

    public CommentDTO toCommentDTO(CommentEntity commentEntity) {
        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setId(commentEntity.getId());
        commentDTO.setDescription(commentEntity.getDescription());
        commentDTO.setCreated_date(commentEntity.getCreatedDate());
        commentDTO.setIssue_id(commentEntity.getIssue().getId());
        commentDTO.setCreater_id(commentEntity.getCreaterId().getUser_id());

        return commentDTO;
    }
}