package ua.garmash.internetshop.service;

import ua.garmash.internetshop.dto.ProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAll();
    List<ProductDto> getByKeyword(String searchString);
    void addToUserBucket(Long productId, String username);
    void addToUserCart(Long productId);

    void addProduct(ProductDto dto);

    ProductDto getById(Long id);

    void delProductById(Long id);

    void invertAvailabilityById(Long id);

    List<ProductDto> findAllByCategoryId(Long categoryId);

    void save(Long productId, ProductDto productDto);
}
