package com.example.datascraperfromapi.controller;

import com.example.datascraperfromapi.entity.Product;
import com.example.datascraperfromapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/magazin")
    public String afiseazaPaginaMagazin(Model model) {
         var listaProduse = productRepository.findAll();

         model.addAttribute("produse", listaProduse);

         return "index";
    }
}
