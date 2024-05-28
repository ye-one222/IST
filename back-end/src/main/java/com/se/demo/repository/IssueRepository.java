package com.se.demo.repository;

import com.se.demo.entity.IssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssueRepository  extends JpaRepository<IssueEntity, Integer> {
    List<IssueEntity> findByReporterIdOrAssigneeIdOrPlId(Integer reporterId, Integer assigneeId, Integer plId);

    IssueEntity findById(IssueEntity issue);
    List<IssueEntity> findByState(String istate);
}