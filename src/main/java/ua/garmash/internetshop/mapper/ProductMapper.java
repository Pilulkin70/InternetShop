package ua.garmash.internetshop.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import ua.garmash.internetshop.dto.ProductDto;
import ua.garmash.internetshop.model.Product;

import java.util.List;

@Mapper
public interface ProductMapper {
	ProductMapper MAPPER = Mappers.getMapper(ProductMapper.class);

	Product toProduct(ProductDto dto);

	@InheritInverseConfiguration
	ProductDto fromProduct(Product product);

	List<Product> toProductList(List<ProductDto> productDtos);

	List<ProductDto> fromProductList(List<Product> products);
}