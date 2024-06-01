package com.se.demo.controller;

import com.se.demo.dto.MemberDTO;
import com.se.demo.entity.MemberEntity;
import com.se.demo.service.MemberService;
import com.se.demo.utils.ApiUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user") //나중에 맞춰서 바꾸자
public class MemberController {
    private final MemberService memberService;

    //sign up
    /*@Bean
    @GetMapping("/")   //이 백url에서 get요청 올때
    public String signupForm(){
        return "home";   //indexBw html 가져와라
    }*/

    /*@PostMapping("/signup")   //이 백url에서 post요청올떄
    public int signup(MemberDTO memberDTO){
        //System.out.println(memberDTO);
        return memberService.toMemberDTO(memberService.signup(memberDTO)).getUser_id();
    }*/
    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody MemberDTO memberDTO) {
        if(memberService.checkId(memberDTO.getNickname())){
            int userId = MemberDTO.toMemberDTO(memberService.signup(memberDTO)).getUser_id();
            //JSON 형식으로 반환하려고
            Map<String, Integer> response = new HashMap<>();
            response.put("id", userId);
            return ResponseEntity.ok(response);
        }
        else{
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "ID already in use");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    /*@PostMapping("/check")
    public ResponseEntity<?> check(@RequestBody MemberDTO memberDTO){
        boolean isAvailable = memberService.checkId(memberDTO.getId());
        return ResponseEntity.ok(ApiUtils.success(isAvailable));
    }*/

    /*@GetMapping(value = {"", "/"})
    public String home(Model model, @SessionAttribute(name="userNickname", required = false) String userNickname){
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        MemberEntity loginMemberEntity = memberService.getLoginUserById(userNickname);

        if(loginMemberEntity != null){
            model.addAttribute("nickname", loginMemberEntity.getNickname()); //원래 코드에서는 nickname을 저장하는데 필요없지 않나
        }

        return "home";
    }*/

    /*@PostMapping("/login")
    public Mem login(@ModelAttribute MemberDTO memberDTO, BindingResult bindingResult, HttpServletRequest httpServletRequest, Model model){
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
        return "session-login";
    }*/

    /*MemberDTO만 return하는 버전
    @PostMapping("/login")
    public ResponseEntity<?> login(@ModelAttribute MemberDTO memberDTO, BindingResult bindingResult, HttpServletRequest httpServletRequest, Model model) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        MemberEntity memberEntity = memberService.login(memberDTO);

        // 로그인 아이디나 비밀번호 틀린 경우 error return함
        if (memberEntity == null) {
            bindingResult.reject("loginFail", "로그인 아이디 또는 비밀번호가 틀렸습니다.");
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "로그인 아이디 또는 비밀번호가 틀렸습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getAllErrors().forEach(error -> {
                String field = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(field, errorMessage);
            });
            return ResponseEntity.badRequest().body(errors);
        }

        // 로그인 성공 => 세션 생성

        // 세션 생성 전 기존의 세션 파기
        httpServletRequest.getSession().invalidate();
        HttpSession session = httpServletRequest.getSession(true); // session이 없으면 생성
        // 세션에 userNickname 넣어줌
        session.setAttribute("userNickname", memberEntity.getNickname());
        session.setMaxInactiveInterval(1800); // session을 30분동안 유지

        return ResponseEntity.ok(memberService.toMemberDTO(memberEntity));
    }*/

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberDTO memberDTO, BindingResult bindingResult, HttpServletRequest httpServletRequest, Model model) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        MemberEntity memberEntity = memberService.login(memberDTO);

        // 로그인 아이디나 비밀번호 틀린 경우 error return함
        if (memberEntity == null) {
            bindingResult.reject("loginFail", "로그인 아이디 또는 비밀번호가 틀렸습니다.");
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "로그인 아이디 또는 비밀번호가 틀렸습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getAllErrors().forEach(error -> {
                String field = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(field, errorMessage);
            });
            return ResponseEntity.badRequest().body(errors);
        }

        // 로그인 성공 => 세션 생성

        // 세션 생성 전 기존의 세션 파기
        httpServletRequest.getSession().invalidate();
        HttpSession session = httpServletRequest.getSession(true); // session이 없으면 생성
        // 세션에 userNickname 넣어줌
        session.setAttribute("userNickname", memberEntity.getNickname());
        session.setMaxInactiveInterval(1800); // session을 30분동안 유지

        // 세션 정보만 포함하는 응답 객체 생성
        Map<String, String> response = new HashMap<>();
        response.put("session_id", session.getId());
        response.put("user_id", String.valueOf(memberEntity.getUser_id()));

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{user_nickname}")
    public int getUser(@PathVariable("user_nickname") String userNickname) {
        return memberService.findByNickname(userNickname);
    }

}

