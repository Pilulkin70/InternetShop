package ua.garmash.internetshop.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ua.garmash.internetshop.dto.OrderDetailDto;
import ua.garmash.internetshop.model.OrderDetail;

import java.util.List;

@Mapper
public interface OrderDetailMapper {
    OrderDetailMapper MAPPER = Mappers.getMapper(OrderDetailMapper.class);

    OrderDetail toOrderDetail(OrderDetailDto dto);

    @InheritInverseConfiguration
    OrderDetailDto fromOrderDetail(OrderDetail orderDetail);

    List<OrderDetail> toOrderDetailList(List<OrderDetailDto> orderDetailDtos);

    List<OrderDetailDto> fromOrderDetailList(List<OrderDetail> orders);
}