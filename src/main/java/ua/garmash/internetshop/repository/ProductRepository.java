package ua.garmash.internetshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.garmash.internetshop.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Override
    Page<Product> findAll(Pageable pageable);

    List<Product> findAllByCategoryId(Long categoryId);

    List<Product> findAllByBrandId(Long brandId);

    @Query(value = "select * from products p where upper(p.vendor_code) like %:keyword% or upper(p.title) like %:keyword% or upper(p.description) like %:keyword%", nativeQuery = true)
    List<Product> findByKeyword(@Param("keyword") String keyword);
}
