package ua.garmash.internetshop.service;

import org.springframework.stereotype.Service;
import ua.garmash.internetshop.dao.CategoryRepository;
import ua.garmash.internetshop.dto.CategoryDto;
import ua.garmash.internetshop.mapper.CategoryMapper;
import ua.garmash.internetshop.model.Category;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper = CategoryMapper.MAPPER;

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public void save(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoryMapper.fromCategoryList(categoryRepository.findAll());
    }
}
