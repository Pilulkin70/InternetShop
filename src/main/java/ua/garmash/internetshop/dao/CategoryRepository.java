package ua.garmash.internetshop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.garmash.internetshop.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
//    Category findByTitle(String name);
}
