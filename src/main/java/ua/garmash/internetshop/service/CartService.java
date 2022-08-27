package ua.garmash.internetshop.service;

import ua.garmash.internetshop.dto.BucketDto;
import ua.garmash.internetshop.model.Cart;

public interface CartService {
    Cart addToCart(Long productId);
    void delProductFromCartById(Long productId);
    void updateCartProductAmount(Long productId, final long amountDif);
    void clearCart();
    BucketDto getCartDto();
    Long commitCartToOrder(String username);
}
