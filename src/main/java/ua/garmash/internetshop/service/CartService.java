package ua.garmash.internetshop.service;

import ua.garmash.internetshop.dto.CartDto;
import ua.garmash.internetshop.model.Cart;
import ua.garmash.internetshop.model.User;

public interface CartService {
    Cart addProductToCart(Long productId);
    void delProductFromCartById(Long productId);
    void updateCartProductAmount(Long productId, final long amountDif);
    void clearCart();
    void saveCartToBasket(User user);
    void loadCartFromBasket(User user);
    CartDto getCartDto();
    Long commitCartToOrder(String username);
}
