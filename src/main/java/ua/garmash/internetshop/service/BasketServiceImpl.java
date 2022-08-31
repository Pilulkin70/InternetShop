package ua.garmash.internetshop.service;


import org.springframework.stereotype.Service;
import ua.garmash.internetshop.dao.BucketRepository;
import ua.garmash.internetshop.dao.ProductRepository;
import ua.garmash.internetshop.dto.CartDetailDto;
import ua.garmash.internetshop.dto.CartDto;
import ua.garmash.internetshop.model.*;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class BasketServiceImpl implements BasketService {

    private final BucketRepository bucketRepository;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final OrderService orderService;

    public BasketServiceImpl(BucketRepository bucketRepository,
                             ProductRepository productRepository,
                             UserService userService,
                             OrderService orderService) {
        this.bucketRepository = bucketRepository;
        this.productRepository = productRepository;
        this.userService = userService;
        this.orderService = orderService;
    }

    @Override
    @Transactional
    public Basket createBasket(User user, Long productId) {
        Basket basket = new Basket();
        if (user == null) {
            return basket;
        }
        basket.setUser(user);
        basket.setItems(Collections.singletonMap(productRepository.getOne(productId), 1L));
        return bucketRepository.save(basket);
    }

    @Override
    @Transactional
    public void addItem(Basket basket, Long productId) {
        basket.getItems().merge(productRepository.getOne(productId), 1L, (v1, v2) -> v1 + v2);
        bucketRepository.save(basket);
    }

    @Override
    public Basket getBasketByUser(String name) {
        User user = userService.findByName(name);
        if (user == null || user.getBasket() == null) {
            return new Basket();
        }

        Basket basket = user.getBasket();

/*        List<CartDetailDto> cartDetailDtoList = new ArrayList<>(user.getBasket().getItems().size());
        user.getBasket().getItems().entrySet().stream().forEach(entry -> {
            CartDetailDto detail = new CartDetailDto(entry.getKey());
            detail.setAmount(Double.valueOf(entry.getValue()));
            detail.setSum(entry.getValue() * entry.getKey().getPrice());
            cartDetailDtoList.add(detail);
        });

        basket.setBasketDetails(cartDetailDtoList);
        basket.aggregate();*/

        return basket;
    }

    @Override
    @Transactional
    public void delItemsById(String userName, Long productId) {
        User user = userService.findByName(userName);
        if (user == null) {
            throw new RuntimeException("User is not found");
        }
        Basket basket = user.getBasket();
        basket.getItems().remove(productRepository.getOne(productId));
        bucketRepository.save(basket);
    }

    @Override
    @Transactional
    public void clearBasket(String userName) {
        User user = userService.findByName(userName);
        if (user == null) {
            throw new RuntimeException("User is not found");
        }
        Basket basket = user.getBasket();
        basket.getItems().clear();
        bucketRepository.save(basket);
    }
}