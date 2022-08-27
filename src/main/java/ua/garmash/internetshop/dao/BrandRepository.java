package ua.garmash.internetshop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.garmash.internetshop.model.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
