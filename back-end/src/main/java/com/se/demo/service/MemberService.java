package com.se.demo.service;

import com.se.demo.dto.MemberDTO;
import com.se.demo.entity.MemberEntity;
import com.se.demo.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    public final MemberRepository memberRepository;

    //회원가입 (조건 확인 X)
    public MemberEntity signup(MemberDTO memberDTO){
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        return memberRepository.save(memberEntity);
    }

    //login
    public MemberEntity login(MemberDTO req){ //LoginRequest
        //Optional<MemberEntity> optionalMember = MemberRepository.findByNickname(req.getNickname());
        Optional<MemberEntity> optionalMember = memberRepository.findByNickname(req.getNickname());

        //loginId와 일치하는 사용자가 없으면 null return
        if(optionalMember.isEmpty()){
            return null;
        }

        MemberEntity memberEntity = optionalMember.get();

        //저장된 password와 입력된 password가 다르면 null return
        if(!memberEntity.getPassword().equals(req.getPassword())){
            return null;
        }

        return memberEntity;
    }

    //nickname(String)을 입력받아 memberEntity를 return함
    public MemberEntity getLoginUserById(String userNickname){
        if(userNickname == null) return null;

        //Optional<MemberEntity> optionalMemberEntityOfYW = MemberRepository.findByNickname(userNickname);
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByNickname(userNickname);
        if(optionalMemberEntity.isEmpty()) return null;

        return optionalMemberEntity.get();
    }

    //loginNickname(String)을 입력받아 memberEntity를 return함
    //로그인한 상태인지 확인?
    public MemberEntity getLoginUserByLoginNickname(String nickname){
        if(nickname == null) return null;

        //Optional<MemberEntity> optionalMemberEntity = MemberRepository.findByNickname(nickname);
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByNickname(nickname);
        if(optionalMemberEntity.isEmpty()) return null;

        return optionalMemberEntity.get();
    }

    public boolean checkId(String nickname) {
        try {
            System.out.println(nickname);
            if(memberRepository.existsByNickname(nickname)){
                return false;
            }
            else{
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //해당 nickname을 가진 user의 id를 반환
    public int findByNickname(String nickname){
        MemberEntity memberEntity = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user nickname"));
        return memberEntity.getUser_id();
    }

}
