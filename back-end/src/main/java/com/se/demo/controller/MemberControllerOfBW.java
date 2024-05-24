package com.se.demo.controller;

import com.se.demo.dto.MemberDTOOfBW;
import com.se.demo.service.MemberServiceOfBW;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberControllerOfBW {
    //의존성주입 - 해당 객체에 대한 정보가 없어도 사용가능한거
    //하나를 상속한 여러클래스의 동일한
    private final MemberServiceOfBW memberServiceOfBW;
    @Bean
    @GetMapping("/")   //이 url에서 get요청 올때
    public String signupForm(){
        return "indexBw";   //indexBw html 가져와라
    }

    @PostMapping("/")   //이 url에서 post요청올떄
    public String signup(MemberDTOOfBW memberDTOOfBW){
        System.out.println(memberDTOOfBW);
        memberServiceOfBW.signup(memberDTOOfBW);
        return "login";
    }
}
