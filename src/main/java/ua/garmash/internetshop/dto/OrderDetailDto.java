package ua.garmash.internetshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.garmash.internetshop.model.Product;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDto {
//    private String title;
//    private Long productId;
    private BigDecimal amount;
    private Double price;
    private ProductDto product;


/*    public OrderDetailDto(Product product, BigDecimal amount) {
        this.title = product.getTitle();
        this.productId = product.getId();
        this.price = product.getPrice();
        this.amount = amount;
    }*/
}
