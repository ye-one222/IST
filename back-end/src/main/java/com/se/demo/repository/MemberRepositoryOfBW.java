package com.se.demo.repository;

import com.se.demo.entity.MemberEntityOfBW;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepositoryOfBW extends JpaRepository<MemberEntityOfBW,Long> {
    //DB에서 받은 쌩정보를 entity를 통해 바꿔주는 느낌인가
    //Jpa레포지토리를 상속하면 여러가지 매소드를 바로 사용 가능함(디비와 소통)

}
