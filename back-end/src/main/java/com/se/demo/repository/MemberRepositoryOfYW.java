package com.se.demo.repository;

import com.se.demo.entity.MemberEntityOfYW;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepositoryOfYW extends JpaRepository<MemberEntityOfYW, Integer> {
    //boolean
    Optional<MemberEntityOfYW> findByLoginNickname(String loginNickname);
    Optional<MemberEntityOfYW> findByNickname(String nickname);
}
