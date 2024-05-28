package com.se.demo.dto;

public class LoginResponse {
    private MemberDTO member;
    private String sessionId;
    private String userNickname;

    // 생성자, getter 및 setter
    public LoginResponse(MemberDTO member, String sessionId, String userNickname) {
        this.member = member;
        this.sessionId = sessionId;
        this.userNickname = userNickname;
    }

    public MemberDTO getMember() {
        return member;
    }

    public void setMember(MemberDTO member) {
        this.member = member;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }
}
