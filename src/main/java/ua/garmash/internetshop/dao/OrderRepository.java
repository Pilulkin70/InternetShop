package ua.garmash.internetshop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.garmash.internetshop.model.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

//    List<Order> findAllByUserId(Long id);
}
