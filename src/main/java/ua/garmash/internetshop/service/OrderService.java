package ua.garmash.internetshop.service;


import ua.garmash.internetshop.dto.OrderDto;
import ua.garmash.internetshop.model.Order;

import java.util.List;

public interface OrderService {
    Order saveOrder(Order order);
    Order saveOrderFromDto(OrderDto orderDto);
    OrderDto getOrderById(Long id);
    List<OrderDto> getOrdersByUser(String userName);
    void delOrderById(Long id);
}
