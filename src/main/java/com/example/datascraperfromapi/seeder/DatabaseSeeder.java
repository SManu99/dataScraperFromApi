package com.example.datascraperfromapi.seeder;

import com.example.datascraperfromapi.dto.DummyJsonResponse;
import com.example.datascraperfromapi.entity.Product;
import com.example.datascraperfromapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {


        if (productRepository.count() == 0) {
            System.out.println("Start ... ");

            RestTemplate restTemplate = new RestTemplate();
            String url = "https://dummyjson.com/products/category/smartphones";
            // "https://dummyjson.com/products/category/laptops"

            DummyJsonResponse response = restTemplate.getForObject(url, DummyJsonResponse.class);

            if (response != null && response.getProducts() != null) {
                List<Product> productsToSave = response.getProducts().stream().map(dto -> {
                    Product product = new Product();
                    product.setName(dto.getTitle());
                    product.setDescription(dto.getDescription());
                    product.setPrice(dto.getPrice());
                    product.setCategory("Smartphone"); //   dto.getCategory()
                    product.setBrand(dto.getBrand());
                    product.setImageUrl(dto.getThumbnail());
                    product.setStockQuantity(20);
                    return product;
                }).collect(Collectors.toList());

                productRepository.saveAll(productsToSave);
                System.out.println("Import complet " + productsToSave.size());
            }
        }

        {
            System.out.println("Produsele exista deja în baza de date. Nu facem import.");
        }
    }
}






