package ua.garmash.internetshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.garmash.internetshop.model.Brand;
import ua.garmash.internetshop.model.Category;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
	private Long id;
	private String vendorCode;
	private String title;
	private String description = "";
	private String imgUrl = "";
	private Double price;
	private Boolean available;
	private Category category;
	private Brand brand;
}
