package ua.garmash.internetshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.garmash.internetshop.model.Product;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDetailDto {
    private Long productId;
    private String category;
    private String vendorCode;
    private String title;
    private String description;
    private Double price;
    private Double amount;
    private Double sum;

    public CartDetailDto(Product product) {
        this.productId = product.getId();
        this.category = product.getCategory().getTitle();
        this.title = product.getTitle();
        this.description = product.getDescription();
        this.vendorCode = product.getVendorCode();
        this.price = product.getPrice();
        this.amount = 1.0;
        this.sum = product.getPrice();
    }
}
