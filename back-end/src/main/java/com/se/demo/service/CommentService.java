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
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Builder

public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final IssueRepository issueRepository; // IssueRepository를 주입 받아야 함

    public CommentResponse save(AddCommentRequest request, String nickName, int issueId) {
        //IssueEntity issueEntity = null;
        //member 조회
        Optional<MemberEntity> member = MemberRepository.findByNickname(nickName);
        if (member.isEmpty()) {
            throw new IllegalArgumentException("Invalid user nickname");
        }

        //이슈 조회
        Optional<IssueEntity> issue = issueRepository.findById(issueId);
        member.orElseThrow(() -> new IllegalArgumentException("Issue not found with id: " + issueId));

        //CommentEntity commentEntity = (CommentEntity) request.toEntity(issue);
        //commentEntity.setCreaterId(member.get());
        //commentEntity.setIssue(issue);


        CommentEntity commentEntity = CommentEntity.builder().build();
        CommentEntity savedComment = (CommentEntity) commentRepository.save(commentEntity);

        System.out.println("Saved Comment: " + savedComment);


        return new CommentResponse(savedComment);
    }



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