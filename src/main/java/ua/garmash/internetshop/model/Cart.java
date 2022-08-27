package ua.garmash.internetshop.model;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.Map;

@Data
@SessionScope
@Component
public class Cart {
    private Map<Product, Long> products = new HashMap<>();
}
