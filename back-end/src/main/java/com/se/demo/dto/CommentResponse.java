package com.se.demo.dto;

import com.se.demo.entity.CommentEntity;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@Data

public class CommentResponse {

    private Integer id;
    private String comment;
    private LocalDateTime createdDate;
    private String nickName;
    private Integer userId;

    // 엔티티에서 DTO로 변환
    public CommentResponse(CommentEntity commentEntity) {
        this.id = commentEntity.getId();
        this.comment = commentEntity.getDescription(); // 'comment' 대신 'description' 필드 사용
        this.createdDate = commentEntity.getCreatedDate();
        this.nickName = commentEntity.getCreaterId().getNickname();
        this.userId = commentEntity.getCreaterId().getUser_id();
    }

    public CommentResponse(Object o) {
    }
}