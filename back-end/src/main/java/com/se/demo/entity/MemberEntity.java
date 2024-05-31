package com.se.demo.entity;

import com.se.demo.dto.MemberDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany(mappedBy = "members") // 연결테이블과 연관있음을 표시
    private List<ProjectEntity> projects = new ArrayList<>();

    //Dto를 Entity로
<<<<<<< HEAD
    public static MemberEntity toMemberEntityBW(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setUser_id(memberDTO.getUser_id());
        memberEntity.setNickname(memberDTO.getNickname());
        memberEntity.setPassword(memberDTO.getPassword());
        return memberEntity;
=======
    public static MemberEntity toMemberEntity(MemberDTO memberDTOOfBW) {
        MemberEntity memberEntityOf = new MemberEntity();
        memberEntityOf.setUser_id(memberDTOOfBW.getUser_id());
        memberEntityOf.setNickname(memberDTOOfBW.getNickname());
        memberEntityOf.setPassword(memberDTOOfBW.getPassword());
        return memberEntityOf;
>>>>>>> 08706729e8c16330e9c6718763f2981c98372624
    }
}

