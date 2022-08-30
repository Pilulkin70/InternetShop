package ua.garmash.internetshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.garmash.internetshop.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
