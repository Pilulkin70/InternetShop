package ua.garmash.internetshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.garmash.internetshop.model.DeliveryOptions;
import ua.garmash.internetshop.model.OrderStatus;
import ua.garmash.internetshop.model.PaymentOptions;
import ua.garmash.internetshop.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private User user;
    private LocalDateTime created;
    private LocalDateTime changed;
    private Double sum;
    private String address;
    private String recipient;
    private String phone;
    private String email;
    private PaymentOptions payment;
    private DeliveryOptions delivery;
    private OrderStatus status;
    private List<OrderDetailDto> details;
}
