package com.se.demo.repository;

import com.se.demo.entity.CommentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository<Comment> extends JpaRepository<CommentEntity, Integer> {
    @Autowired
    List<CommentEntity> findByIssueId(int id);

}
