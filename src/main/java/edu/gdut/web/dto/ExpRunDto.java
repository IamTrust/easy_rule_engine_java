package edu.gdut.web.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ExpRunDto {
    private Long expId;
    private Map<String, Object> params;
}
