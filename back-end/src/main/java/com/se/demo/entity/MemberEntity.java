package com.se.demo.entity;

import com.se.demo.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class MemberEntity {
    //DB(mysql)와 소통하는 애, 즉 서버에서 찾거나 저장할때 사용
    //진짜 디비테이블이랑 연관됨
    //그래서 똑같이 pk unique지정 해줄수 있음

    @Id //pk값이라고 알려줌
    @Column(nullable = false)   //하나의 컬럼이다??
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동 아이디 생성해준대
    private int user_id;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column
    private String password;

    //Dto를 Entity로
    public static MemberEntity toMemberEntityBW(MemberDTO memberDTOOfBW) {
        MemberEntity memberEntityOfBW = new MemberEntity();
        memberEntityOfBW.setUser_id(memberDTOOfBW.getUser_id());
        memberEntityOfBW.setNickname(memberDTOOfBW.getNickname());
        memberEntityOfBW.setPassword(memberDTOOfBW.getPassword());
        return memberEntityOfBW;
    }
}

