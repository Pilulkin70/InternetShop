package ua.garmash.internetshop.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ua.garmash.internetshop.dto.CategoryDto;
import ua.garmash.internetshop.model.Category;

import java.util.List;

@Mapper
public interface CategoryMapper {
    CategoryMapper MAPPER = Mappers.getMapper(CategoryMapper.class);

    Category toCategory(CategoryDto dto);

    @InheritInverseConfiguration
    CategoryDto fromCategory(Category category);

    List<Category> toCategoryList(List<CategoryDto> categoryDtos);

    List<CategoryDto> fromCategoryList(List<Category> categories);
}
