package ua.garmash.internetshop.service;

import ua.garmash.internetshop.model.Cart;
import ua.garmash.internetshop.model.Product;
import ua.garmash.internetshop.model.User;

import java.util.Map;

public interface BasketService {

    void deleteBasket(User user);

    void saveCartToBasket(User user, Cart cart);

    Map<Product, Long> getItemsFromBasket(User user);
}
