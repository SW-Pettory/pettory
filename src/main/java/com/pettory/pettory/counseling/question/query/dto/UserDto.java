package com.pettory.pettory.counseling.question.query.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Integer userId;
    private String userEmail;
    private String userNickname;
    private String userRole;
}
