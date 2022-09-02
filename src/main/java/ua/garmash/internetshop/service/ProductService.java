package ua.garmash.internetshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.garmash.internetshop.dto.ProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAll(Pageable pageable);
    Page getPage();
    List<ProductDto> getByKeyword(String searchString);
    void addToUserCart(Long productId);

    void addProduct(ProductDto dto);

    ProductDto getById(Long id);

    void delProductById(Long id);

    void invertAvailabilityById(Long id);

    List<ProductDto> findAllByCategoryId(Long categoryId);

    List<ProductDto> findAllByBrandId(Long brandId);

    void updateById(Long productId, ProductDto productDto);
}
