package com.se.demo.service;

import com.se.demo.dto.MemberDTOOfYW;
import com.se.demo.entity.MemberEntityOfYW;
import com.se.demo.repository.MemberRepositoryOfYW;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceOfYW {
    private final MemberRepositoryOfYW memberRepositoryOfYW;

    //로그인 기능
    public MemberEntityOfYW login(MemberDTOOfYW req){ //LoginRequest
        Optional<MemberEntityOfYW> optionalMember = memberRepositoryOfYW.findByNickname(req.getNickname());

        //loginId와 일치하는 사용자가 없으면 null return
        if(optionalMember.isEmpty()){
            return null;
        }

        MemberEntityOfYW memberEntityOfYW = optionalMember.get();

        //저장된 password와 입력된 password가 다르면 null return
        if(!memberEntityOfYW.getPassword().equals(req.getPassword())){
            return null;
        }

        return memberEntityOfYW;
    }

    //nickname(String)을 입력받아 memberEntity를 return함
    public MemberEntityOfYW getLoginUserById(String userNickname){
        if(userNickname == null) return null;

        Optional<MemberEntityOfYW> optionalMemberEntityOfYW = memberRepositoryOfYW.findByNickname(userNickname);
        if(optionalMemberEntityOfYW.isEmpty()) return null;

        return optionalMemberEntityOfYW.get();
    }

    //loginNickname(String)을 입력받아 memberEntity를 return함
    //로그인한 상태인지 확인?
    public MemberEntityOfYW getLoginUserByLoginNickname(String nickname){
        if(nickname == null) return null;

        Optional<MemberEntityOfYW> optionalMemberEntityOfYW = memberRepositoryOfYW.findByNickname(nickname);
        if(optionalMemberEntityOfYW.isEmpty()) return null;

        return optionalMemberEntityOfYW.get();
    }
}
