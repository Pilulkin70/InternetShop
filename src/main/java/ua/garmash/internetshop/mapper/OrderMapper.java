package ua.garmash.internetshop.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ua.garmash.internetshop.dto.OrderDto;
import ua.garmash.internetshop.dto.ProductDto;
import ua.garmash.internetshop.model.Order;
import ua.garmash.internetshop.model.Product;

import java.util.List;

@Mapper
public interface OrderMapper {
	OrderMapper MAPPER = Mappers.getMapper(OrderMapper.class);

	Order toOrder(OrderDto dto);

	@InheritInverseConfiguration
	OrderDto fromOrder(Order order);

	List<Order> toOrderList(List<OrderDto> orderDtos);

	List<OrderDto> fromOrderList(List<Order> orders);

}