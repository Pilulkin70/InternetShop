package ua.garmash.internetshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.garmash.internetshop.model.OrderDetail;
import ua.garmash.internetshop.model.OrderStatus;

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
	private LocalDateTime created;
	private LocalDateTime changed;
	private Double sum;
	private String address;
	private OrderStatus status;
	private List<OrderDetailDto> details;
}
