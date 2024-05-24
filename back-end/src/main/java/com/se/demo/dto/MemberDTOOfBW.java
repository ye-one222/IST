package com.se.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberDTOOfBW {
    //view와 소통

 private int user_id;

 private String nickname;

 private String password;

}

/*
class AdminDTOOfBW extends MemberDTOOfBW{
}*/