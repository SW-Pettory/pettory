package com.pettory.pettory.chat.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class SelectChattingEntityResponse {
    private int httpStatus;
    private String message;
    private Map<String, Object> results;
}
