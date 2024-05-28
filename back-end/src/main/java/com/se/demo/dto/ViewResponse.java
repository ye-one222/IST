package com.se.demo.dto;

import com.se.demo.entity.IssueEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class ViewResponse {

    private List<CommentResponse> comments;

    // 이슈에 코멘트 추가
    /*public ViewResponse(IssueEntity issue) {
        this.comments = issue.getComments().stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
    }*/
}