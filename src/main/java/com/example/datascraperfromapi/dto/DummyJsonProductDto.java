package com.example.datascraperfromapi.dto;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DummyJsonProductDto {
    private String title;
    private String description;
    private Double price;
    private String category;
    private String brand;
    private String thumbnail;
}

