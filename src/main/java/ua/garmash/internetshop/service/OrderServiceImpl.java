package ua.garmash.internetshop.service;

import org.springframework.stereotype.Service;
import ua.garmash.internetshop.dao.OrderRepository;
import ua.garmash.internetshop.dto.OrderDto;
import ua.garmash.internetshop.mapper.OrderDetailMapper;
import ua.garmash.internetshop.mapper.OrderMapper;
import ua.garmash.internetshop.model.Order;
import ua.garmash.internetshop.model.User;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper = OrderMapper.MAPPER;
    private final OrderDetailMapper orderDetailMapper = OrderDetailMapper.MAPPER;

    private final OrderRepository orderRepository;
    private final UserService userService;

    public OrderServiceImpl(OrderRepository orderRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public Order saveOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        return savedOrder;
    }

    @Override
    public OrderDto getOrderById(Long id) {
        return orderMapper.fromOrder(orderRepository.findById(id).orElse(null));
    }

    @Override
    public List<OrderDto> getOrdersByUser(String userName) {
        User user = userService.findByName(userName);
        if (user == null) {
            return new ArrayList<>();
        }

//        List<Order> orderList = orderRepository.findAllByUserId(user.getId());
        List<Order> orderList = user.getOrders();
        List<OrderDto> orderDtoList = orderMapper.fromOrderList(orderList);
/*
        List<OrderDto> orderDtoList = new ArrayList<>(orderList.size());
        for (Order order : orderList) {
            orderDtoList.add(orderMapper.fromOrder(order));
            orderDtoList.get(orderDtoList.size() - 1).
                    setOrderDetails(orderDetailMapper.fromOrderDetailList(order.getDetails()));

        }
*/

/*
        OrderDto orderDto = new OrderDto();
        Map<Long, OrderDetailDto> mapByProductId = new HashMap<>();

        List<Product> products = user.getBucket().getProducts();
        for (Product product : products) {
            BucketDetailDto detail = mapByProductId.get(product.getId());
            if (detail == null) {
                mapByProductId.put(product.getId(), new BucketDetailDto(product));
            } else {
                detail.setAmount(detail.getAmount() + 1.0);
                detail.setSum(detail.getSum() + product.getPrice());
            }
        }

        bucketDto.setBucketDetails(new ArrayList<>(mapByProductId.values()));
        bucketDto.aggregate();*/

        return orderDtoList;
    }

}