package com.bootteam.springsecuritywebfluxotp.service.mapper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class ApiResponseDTO {

    @JsonProperty("data")
    private Object data;

    @JsonProperty("message")
    private String message;

}
