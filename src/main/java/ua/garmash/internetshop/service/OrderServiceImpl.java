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
    @Transactional
    public Order saveOrderFromDto(OrderDto orderDto) {
        Order order = orderMapper.toOrder(orderDto);
        return orderRepository.save(orderMapper.toOrder(orderDto));
    }
    @Override
    public OrderDto getOrderById(Long id) {
        return orderMapper.fromOrder(orderRepository.findById(id).orElse(null));
    }

    @Override
    public List<OrderDto> getOrdersByUserName(String userName) {
        User user = userService.findByName(userName);
        if (user == null) {
            return new ArrayList<>();
        }
        List<Order> orderList = user.getOrders();
        List<OrderDto> orderDtoList = orderMapper.fromOrderList(orderList);
        return orderDtoList;
    }

    @Override
    @Transactional
    public void delOrderById(Long id) {
        orderRepository.deleteById(id);
    }
}