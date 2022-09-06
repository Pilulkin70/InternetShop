package ua.garmash.internetshop.service;

import org.springframework.stereotype.Service;
import ua.garmash.internetshop.repository.ProductRepository;
import ua.garmash.internetshop.dto.CartDetailDto;
import ua.garmash.internetshop.dto.CartDto;
import ua.garmash.internetshop.model.*;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final Cart cart;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final OrderService orderService;
    private final BasketService basketService;

    public CartServiceImpl(Cart cart, ProductRepository productRepository, UserService userService, OrderService orderService, BasketService basketService) {
        this.cart = cart;
        this.productRepository = productRepository;
        this.userService = userService;
        this.orderService = orderService;
        this.basketService = basketService;
    }

    @Override
    public Cart addProductToCart(Long productId) {
        cart.getProducts().merge(productRepository.getOne(productId), 1L, (v1, v2) -> v1 + v2);
        return cart;
    }

    @Override
    public void delProductFromCartById(Long productId) {
        cart.getProducts().remove(productRepository.getOne(productId));
    }

    @Override
    public void updateCartProductAmount(Long productId, long amountDif) {
        cart.getProducts().computeIfPresent(productRepository.getOne(productId),
                (key, value) -> (value + amountDif) > 1 ? value + amountDif : 1L);
    }

    @Override
    public void clearCart() {
        cart.getProducts().clear();
    }

    @Override
    public void saveCartToBasket(User user) {
        basketService.saveCartToBasket(user, cart);
    }

    @Override
    public void loadCartFromBasket(User user) {
        Map<Product, Long> items = basketService.getItemsFromBasket(user);
        if (items.size() != 0) {
            clearCart();
            cart.getProducts().putAll(items);
            basketService.deleteBasket(user);
        }
    }

    @Override
    public CartDto getCartDto() {

        CartDto cartDto = new CartDto();

        List<CartDetailDto> cartDetailDtoList = new ArrayList<>(cart.getProducts().size());
        cart.getProducts().entrySet().stream().forEach(entry -> {
            CartDetailDto detail = new CartDetailDto(entry.getKey());
            detail.setAmount(Double.valueOf(entry.getValue()));
            detail.setSum(entry.getValue() * entry.getKey().getPrice());
            cartDetailDtoList.add(detail);
        });

        cartDto.setCartDetails(cartDetailDtoList);
        cartDto.aggregate();

        return cartDto;
    }

    @Override
    @Transactional
    public Long commitCartToOrder(String username) {
        User user = userService.findByName(username);
        if (user == null) {
            user = new User();
            user.setId(0L);
        }

        Order order = new Order();
        order.setStatus(OrderStatus.NEW);
        order.setUser(user);

        Map<Product, Long> productWithAmount = cart.getProducts();
        List<OrderDetail> orderDetails = productWithAmount.entrySet().stream()
                .map(pair -> new OrderDetail(order, pair.getKey(), pair.getValue()))
                .collect(Collectors.toList());

        BigDecimal total = new BigDecimal(orderDetails.stream()
                .map(detail -> detail.getPrice().multiply(detail.getAmount()))
                .mapToDouble(BigDecimal::doubleValue).sum());

        order.setDetails(orderDetails);
        order.setSum(total);
        order.setAddress("none");

        Order lastOrder = orderService.saveOrder(order);
        cart.getProducts().clear();
        return lastOrder.getId();
    }
}
