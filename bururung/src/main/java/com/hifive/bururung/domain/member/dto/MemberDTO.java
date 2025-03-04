package com.hifive.bururung.domain.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter 
@ToString
public class MemberDTO {
    private Long memberId;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String postCode;
    private String address;
    private String detailAddress;
    private String joinDate; 
    private String role;
    private String status;
}