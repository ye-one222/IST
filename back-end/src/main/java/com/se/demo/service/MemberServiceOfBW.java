package com.se.demo.service;

import com.se.demo.dto.MemberDTOOfBW;
import com.se.demo.entity.MemberEntityOfBW;
import com.se.demo.repository.MemberRepositoryOfBW;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceOfBW {
    //로그인을 포함한 대부분의 로직을 처리하네 레포지토리 생성해서 로직실행
    private final MemberRepositoryOfBW memberRepositoryOfBW;

    //회원가입 (조건 확인 X)
    public void signup(MemberDTOOfBW memberDTOOfBW){
        MemberEntityOfBW memberEntityOfBW = MemberEntityOfBW.toMemberEntityBW(memberDTOOfBW);
        memberRepositoryOfBW.save(memberEntityOfBW);
    }
}
