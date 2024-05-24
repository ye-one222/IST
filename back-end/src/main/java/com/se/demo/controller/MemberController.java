package com.se.demo.controller;

import com.se.demo.dto.MemberDTO;
import com.se.demo.entity.MemberEntity;
import com.se.demo.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/session-login") //나중에 맞춰서 바꾸자
public class MemberController {
    private final MemberService memberService;

    //sign up
    @Bean
    @GetMapping("/")   //이 백url에서 get요청 올때
    public String signupForm(){
        return "home";   //indexBw html 가져와라
    }

    @PostMapping("/signup")   //이 백url에서 post요청올떄
    public String signup(MemberDTO memberDTO){
        System.out.println(memberDTO);
        memberService.signup(memberDTO);
        return "login";
    }

    @GetMapping(value = {"", "/"})
    public String home(Model model, @SessionAttribute(name="userNickname", required = false) String userNickname){
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        MemberEntity loginMemberEntity = memberService.getLoginUserById(userNickname);

        if(loginMemberEntity != null){
            model.addAttribute("nickname", loginMemberEntity.getNickname()); //원래 코드에서는 nickname을 저장하는데 필요없지 않나
        }

        return "home";
    }

    @GetMapping("/login")
    public String loginPage(Model model){
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");
        model.addAttribute("memberDTO", new MemberDTO());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, BindingResult bindingResult, HttpServletRequest httpServletRequest, Model model){
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        MemberEntity memberEntity = memberService.login(memberDTO);

        //로그인 아이디나 비밀번호 틀린 경우 error return함
        if(memberEntity == null){
            bindingResult.reject("loginFail", "로그인 아이디 또는 비밀번호가 틀렸습니다.");
        }

        if(bindingResult.hasErrors()){
            return "login";
        }

        //로그인 성공 => 세션 생성

        //세션 생성 전 기존의 세션 파기
        httpServletRequest.getSession().invalidate();
        HttpSession session = httpServletRequest.getSession(true); //session이 없으면 생성
        //세션에 userNickname 넣어줌
        session.setAttribute("userNickname", memberEntity.getNickname());
        session.setMaxInactiveInterval(1800); //session을 30분동안 유지

        //sessionList.put(session.getId(), session);
        return "redirect:/session-login";
    }
}
