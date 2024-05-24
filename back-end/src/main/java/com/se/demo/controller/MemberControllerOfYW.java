package com.se.demo.controller;

import com.se.demo.dto.MemberDTOOfYW;
import com.se.demo.entity.MemberEntityOfYW;
import com.se.demo.service.MemberServiceOfYW;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/session-login") //나중에 맞춰서 바꾸자
public class MemberControllerOfYW { //SessionLoginController
    private final MemberServiceOfYW memberServiceOfYW;

    @GetMapping(value = {"", "/"})
    public String home(Model model, @SessionAttribute(name="userNickname", required = false) String userNickname){
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        MemberEntityOfYW loginMemberEntityOfYW = memberServiceOfYW.getLoginUserById(userNickname);

        if(loginMemberEntityOfYW != null){
            model.addAttribute("nickname", loginMemberEntityOfYW.getNickname()); //원래 코드에서는 nickname을 저장하는데 필요없지 않나
        }

        return "home";
    }

    @GetMapping("/login")
    public String loginPage(Model model){
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");
        model.addAttribute("memberDTOOfYW", new MemberDTOOfYW());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTOOfYW memberDTOOfYW, BindingResult bindingResult, HttpServletRequest httpServletRequest, Model model){
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        MemberEntityOfYW memberEntityOfYW = memberServiceOfYW.login(memberDTOOfYW);

        //로그인 아이디나 비밀번호 틀린 경우 error return함
        if(memberEntityOfYW == null){
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
        session.setAttribute("userNickname", memberEntityOfYW.getNickname());
        session.setMaxInactiveInterval(1800); //session을 30분동안 유지

        //sessionList.put(session.getId(), session);
        return "redirect:/session-login";
    }
}
