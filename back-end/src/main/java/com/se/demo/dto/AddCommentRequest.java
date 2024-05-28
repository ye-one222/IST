package com.se.demo.dto;

import com.se.demo.entity.IssueEntity;
import com.se.demo.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.stream.events.Comment;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.se.demo.entity.CommentEntity; // 본인의 Comment 엔티티를 임포트


@NoArgsConstructor
@Getter
@AllArgsConstructor
@Data

public class AddCommentRequest {
    private MemberEntity createrId;
    private String description;
    private final String createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    private Integer id;
    private int issueId;

    //private Long issueId;

    public Comment toEntity(IssueEntity issue){
        CommentEntity build = CommentEntity.builder()
                .createrId(new MemberEntity())
                .description(this.description)
                .createdDate(LocalDateTime.now())

                .issue(issue)
                .build();
        return (Comment) build;


    }



}