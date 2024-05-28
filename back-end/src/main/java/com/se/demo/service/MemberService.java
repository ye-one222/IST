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
        MemberEntity memberEntity = MemberEntity.toMemberEntityBW(memberDTO);
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

    public MemberDTO toMemberDTO(MemberEntity memberEntity) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setUser_id(memberEntity.getUser_id());
        memberDTO.setNickname(memberEntity.getNickname());
        memberDTO.setPassword(memberEntity.getPassword());
        //memberDTO.setProjects(toProjectDTOList(memberEntity.getProjects())); 이거 있어야 나의 project의 member의 project 정보가 올바르게 나오는데, 이거 실행하면 스택오버플로우 발생해서.. 그리고 내 동료의 project들을 내가 굳이 알아야 할 필요는 없잖앙
        return memberDTO;
    }

    //nickname(String)을 입력받아 memberEntity를 return함
    public MemberEntity getLoginUserById(String userNickname){
        if(userNickname == null) return null;

        //Optional<MemberEntity> optionalMemberEntityOfYW = MemberRepository.findByNickname(userNickname);
        Optional<MemberEntity> optionalMemberEntityOfYW = memberRepository.findByNickname(userNickname);
        if(optionalMemberEntityOfYW.isEmpty()) return null;

        return optionalMemberEntityOfYW.get();
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

    public boolean checkId(String id) {
        return memberRepository.existsById(Integer.parseInt(id));
    }

}
