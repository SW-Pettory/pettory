package com.pettory.pettory.counseling.answer.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Integer userId;
    private String userEmail;
    private String userNickname;
    private String userRole;
    private String userHospitalInfo;
    private String userHospitalName;
}
