package ua.garmash.internetshop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.garmash.internetshop.model.Basket;

public interface BasketRepository extends JpaRepository<Basket, Long> {
}

