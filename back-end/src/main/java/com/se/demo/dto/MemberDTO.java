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

 public static MemberEntity toEntity(MemberDTO memberDTO) {
  MemberEntity memberEntity = new MemberEntity();
  memberEntity.setUser_id(memberDTO.getUser_id());
  memberEntity.setNickname(memberDTO.getNickname());
  memberEntity.setPassword(memberDTO.getPassword());
  return memberEntity;
 }
}
/*
class AdminDTOOfBW extends MemberDTOOfBW{
}*/