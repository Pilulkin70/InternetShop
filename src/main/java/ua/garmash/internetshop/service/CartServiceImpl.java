package ua.garmash.internetshop.service;

import org.springframework.stereotype.Service;
import ua.garmash.internetshop.dao.ProductRepository;
import ua.garmash.internetshop.dto.BucketDetailDto;
import ua.garmash.internetshop.dto.BucketDto;
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


    public CartServiceImpl(Cart cart, ProductRepository productRepository, UserService userService, OrderService orderService) {
        this.cart = cart;
        this.productRepository = productRepository;
        this.userService = userService;
        this.orderService = orderService;
    }

    @Override
    public Cart addToCart(Long productId) {
        cart.getProducts().merge(productRepository.getOne(productId), 1L, (v1, v2) -> v1 + v2);
        return cart;
    }

    @Override
    public void delProductFromCartById(Long productId) {
        cart.getProducts().remove(productRepository.getOne(productId));
    }

    @Override
    public void updateCartProductAmount(Long productId, final long amountDif) {
        cart.getProducts().computeIfPresent(productRepository.getOne(productId),
                (key, value) -> (value + amountDif) > 1 ? value + amountDif : 1L);
    }

    @Override
    public void clearCart() {
        cart.getProducts().clear();
    }

    @Override
    public BucketDto getCartDto() {

        BucketDto bucketDto = new BucketDto();

        List<BucketDetailDto> bucketDetailDtoList = new ArrayList<>(cart.getProducts().size());
        cart.getProducts().entrySet().stream().forEach(entry -> {
            BucketDetailDto detail = new BucketDetailDto(entry.getKey());
            detail.setAmount(Double.valueOf(entry.getValue()));
            detail.setSum(entry.getValue() * entry.getKey().getPrice());
            bucketDetailDtoList.add(detail);
        });

        bucketDto.setBucketDetails(bucketDetailDtoList);
        bucketDto.aggregate();

        return bucketDto;
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
