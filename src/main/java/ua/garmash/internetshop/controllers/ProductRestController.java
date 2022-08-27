package ua.garmash.internetshop.controllers;

import org.springframework.web.bind.annotation.*;
import ua.garmash.internetshop.dto.ProductDto;
import ua.garmash.internetshop.service.ProductService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductRestController {

    private final ProductService productService;


    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ProductDto getById(@PathVariable Long id){
        return productService.getById(id);
    }

    @PostMapping
    public void addProduct(@RequestBody ProductDto dto){
        productService.addProduct(dto);
    }

}
