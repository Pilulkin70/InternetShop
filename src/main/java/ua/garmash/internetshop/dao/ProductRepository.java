package ua.garmash.internetshop.dao;

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

/*    @Query(value="SELECT * FROM products WHERE to_tsvector(vendor_code) || to_tsvector(title) || to_tsvector(description) @@ plainto_tsquery(?1%)", nativeQuery=true)
//    @Query("SELECT p FROM products p")
    List<Product> findFullTextSearch(String searchString);*/

    @Query(value = "select * from products p where upper(p.vendor_code) like %:keyword% or upper(p.title) like %:keyword% or upper(p.description) like %:keyword%", nativeQuery = true)
    List<Product> findByKeyword(@Param("keyword") String keyword);
}
