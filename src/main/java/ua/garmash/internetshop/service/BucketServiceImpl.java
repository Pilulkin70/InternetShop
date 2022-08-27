package ua.garmash.internetshop.service;


import org.springframework.stereotype.Service;
import ua.garmash.internetshop.dao.BucketRepository;
import ua.garmash.internetshop.dao.ProductRepository;
import ua.garmash.internetshop.dto.BucketDetailDto;
import ua.garmash.internetshop.dto.BucketDto;
import ua.garmash.internetshop.model.*;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BucketServiceImpl implements BucketService {

    private final BucketRepository bucketRepository;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final OrderService orderService;

    public BucketServiceImpl(BucketRepository bucketRepository,
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
    public Bucket createBucket(User user, Long productId) {
        Bucket bucket = new Bucket();
        if (user == null) {
            return bucket;
        }
        bucket.setUser(user);
        bucket.setItems(Collections.singletonMap(productRepository.getOne(productId), 1L));
        return bucketRepository.save(bucket);
    }

    @Override
    @Transactional
    public void addItem(Bucket bucket, Long productId) {
        bucket.getItems().merge(productRepository.getOne(productId), 1L, (v1, v2) -> v1 + v2);
        bucketRepository.save(bucket);
    }

    @Override
    public BucketDto getBucketByUser(String name) {
        User user = userService.findByName(name);
        if (user == null || user.getBucket() == null) {
            return new BucketDto();
        }

        BucketDto bucketDto = new BucketDto();

        List<BucketDetailDto> bucketDetailDtoList = new ArrayList<>(user.getBucket().getItems().size());
        user.getBucket().getItems().entrySet().stream().forEach(entry -> {
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
    public Long commitBucketToOrder(String username) {
        User user = userService.findByName(username);
        if (user == null) {
            throw new RuntimeException("User is not found");
        }
        Bucket bucket = user.getBucket();
        if (bucket == null || bucket.getItems().isEmpty()) {
            return null;
        }

        Order order = new Order();
        order.setStatus(OrderStatus.NEW);
        order.setUser(user);

        Map<Product, Long> productWithAmount = bucket.getItems();
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
        bucket.getItems().clear();
        bucketRepository.save(bucket);
        return lastOrder.getId();
    }

    @Override
    @Transactional
    public void delItemsById(String userName, Long productId) {
        User user = userService.findByName(userName);
        if (user == null) {
            throw new RuntimeException("User is not found");
        }
        Bucket bucket = user.getBucket();
        bucket.getItems().remove(productRepository.getOne(productId));
//        bucket.getItems().computeIfPresent(productRepository.getOne(productId), (k, v) -> v > 1 ? v - 1 : null);
        bucketRepository.save(bucket);
    }

    @Override
    @Transactional
    public void updateAmount(String userName, Long productId, final long amountDif) {
        User user = userService.findByName(userName);
        if (user == null) {
            throw new RuntimeException("User is not found");
        }
        Bucket bucket = user.getBucket();
        bucket.getItems().computeIfPresent(productRepository.getOne(productId),
                (key, value) -> (value + amountDif) > 1 ? value + amountDif : 1L);
        bucketRepository.save(bucket);
    }

    @Override
    @Transactional
    public void clearBucket(String userName) {
        User user = userService.findByName(userName);
        if (user == null) {
            throw new RuntimeException("User is not found");
        }
        Bucket bucket = user.getBucket();
        bucket.getItems().clear();
        bucketRepository.save(bucket);

    }
}