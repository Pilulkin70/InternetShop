package ua.garmash.internetshop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.garmash.internetshop.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
