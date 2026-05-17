package com.example.datascraperfromapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

 @Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DummyJsonResponse {
    private List<DummyJsonProductDto> products;
}
