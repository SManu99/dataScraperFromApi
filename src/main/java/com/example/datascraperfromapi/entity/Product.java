package com.example.datascraperfromapi.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String brand;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double price;
    private String category;
    private String imageUrl;

    private Integer stockQuantity;
}
