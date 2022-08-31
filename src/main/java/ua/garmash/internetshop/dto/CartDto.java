package ua.garmash.internetshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {
	private long amountProducts;
	private double sum;
	private List<CartDetailDto> cartDetails = new ArrayList<>();

	public void aggregate(){
		this.amountProducts = cartDetails.size();
		this.sum = cartDetails.stream()
				.map(CartDetailDto::getSum)
				.mapToDouble(Double::doubleValue)
				.sum();
	}
}
