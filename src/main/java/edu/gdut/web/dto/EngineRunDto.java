package edu.gdut.web.dto;

import lombok.Data;

import java.util.Map;

@Data
public class EngineRunDto {
    private String exp;
    private Map<String, Object> params;
}
