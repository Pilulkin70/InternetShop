package ua.garmash.internetshop.service;

import ua.garmash.internetshop.dto.CategoryDto;
import ua.garmash.internetshop.model.Category;

import java.util.List;


public interface CategoryService {

    void save(Category category);

    List<CategoryDto> findAll();
}
