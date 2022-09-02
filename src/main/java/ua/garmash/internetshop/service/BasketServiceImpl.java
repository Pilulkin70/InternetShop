package ua.garmash.internetshop.service;


import org.springframework.stereotype.Service;
import ua.garmash.internetshop.repository.BasketRepository;
import ua.garmash.internetshop.model.*;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class BasketServiceImpl implements BasketService {

    private final BasketRepository basketRepository;

    public BasketServiceImpl(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    @Override
    @Transactional
    public void deleteBasket(User user) {
        if (user != null) {
            Basket basket = user.getBasket();
            if (basket != null) {
                basketRepository.delete(basket);
            }
        }
    }

    @Override
    @Transactional
    public void saveCartToBasket(User user, Cart cart) {
        if (user != null && cart != null) {
            Basket basket = user.getBasket();
            if (basket == null) {
                basket = new Basket();
                basket.setUser(user);
            }
            basket.getItems().clear();
            basket.setItems(cart.getProducts());
            basketRepository.save(basket);
        }
    }

    @Override
    public Map<Product, Long> getItemsFromBasket(User user) {
        Map<Product, Long> items = new HashMap<>();
        if (user != null) {
            Basket basket = user.getBasket();
            if (basket != null) {
                items = basket.getItems();
            }
        }
        return items;
    }
}