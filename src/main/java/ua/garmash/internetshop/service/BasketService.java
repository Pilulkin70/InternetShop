package ua.garmash.internetshop.service;

import ua.garmash.internetshop.dto.CartDto;
import ua.garmash.internetshop.model.Basket;
import ua.garmash.internetshop.model.User;

public interface BasketService {
    Basket createBasket(User user, Long productId);

    void addItem(Basket basket, Long productIds);

    void delItemsById(String userName, Long productId);

    Basket getBasketByUser(String userName);

    void clearBasket(String userName);
}
