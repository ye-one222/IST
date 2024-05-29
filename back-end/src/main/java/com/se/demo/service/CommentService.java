package com.se.demo.service;

import com.se.demo.dto.AddCommentRequest;
import com.se.demo.dto.CommentResponse;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service

public class CommentService {


    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final IssueRepository issueRepository; // IssueRepository를 주입 받아야 함




   public CommentResponse save(AddCommentRequest request, String nickName, int issueId) {
        IssueEntity issueEntity = null;
        //Optional<MemberEntity> member = MemberRepository.findByNickname(nickName);
        Optional<MemberEntity> member = memberRepository.findByNickname(nickName);
        if (member.isEmpty()) {
            throw new IllegalArgumentException("Invalid user nickname");
        }

        IssueEntity issue = issueRepository.findById(issueEntity);
        member.orElseThrow(() -> new IllegalArgumentException("Issue not found with id: " + issueId));

        CommentEntity commentEntity = (CommentEntity) request.toEntity(issue);
        commentEntity.setCreaterId(member.get());


        //commentEntity.setIssue(issue);
        CommentEntity savedComment = (CommentEntity) commentRepository.save(commentEntity);


        return new CommentResponse(savedComment);
    }

    /*public CommentResponse save(CommentEntity comment) {
        CommentEntity savedComment = (CommentEntity) commentRepository.save(comment);
        return new CommentResponse(savedComment);
    }*/





    public List<IssueEntity> search(String Istate) {
        // 검색 로직 구현
        // 예: return issueRepository.findByState(Istate);
        return issueRepository.findByState(Istate);
    }


    @Transactional(readOnly = true)
    public List<CommentResponse> findAllByIssueId(int id) {
        List<CommentEntity> comments = commentRepository.findByIssueId(id);
        //Long issueId = null;
        return comments.stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
    }
}