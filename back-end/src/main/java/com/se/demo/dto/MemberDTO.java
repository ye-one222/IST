package com.se.demo.dto;

import com.se.demo.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
 private int user_id;

 private String nickname;

 private String password;

 public String getId() {
  return String.valueOf(user_id);
 }

 private List<ProjectDTO> projects = new ArrayList<>();

 public MemberDTO(int user_id, String nickname, String password) {
  this.user_id = user_id;
  this.nickname = nickname;
  this.password = password;
 }

 /*public static MemberEntity toEntity(MemberDTO memberDTO) {
  MemberEntity memberEntity = new MemberEntity();
  memberEntity.setUser_id(memberDTO.getUser_id());
  memberEntity.setNickname(memberDTO.getNickname());
  memberEntity.setPassword(memberDTO.getPassword());
  return memberEntity;
 }*/
 public static MemberDTO toMemberDTO(MemberEntity memberEntity) {
  MemberDTO memberDTO = new MemberDTO();
  memberDTO.setUser_id(memberEntity.getUser_id());
  memberDTO.setNickname(memberEntity.getNickname());
  memberDTO.setPassword(memberEntity.getPassword());
  //memberDTO.setProjects(toProjectDTOList(memberEntity.getProjects())); 이거 있어야 나의 project의 member의 project 정보가 올바르게 나오는데, 이거 실행하면 스택오버플로우 발생해서.. 그리고 내 동료의 project들을 내가 굳이 알아야 할 필요는 없잖앙
  return memberDTO;
 }
}
/*
class AdminDTOOfBW extends MemberDTOOfBW{
}*/