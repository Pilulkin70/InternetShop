package ua.garmash.internetshop.service;

import ua.garmash.internetshop.dto.CartDto;
import ua.garmash.internetshop.model.Cart;

public interface CartService {
    Cart addToCart(Long productId);
    void delProductFromCartById(Long productId);
    void updateCartProductAmount(Long productId, final long amountDif);
    void clearCart();
    void saveCart();
    void loadCart();
    CartDto getCartDto();
    Long commitCartToOrder(String username);
}
