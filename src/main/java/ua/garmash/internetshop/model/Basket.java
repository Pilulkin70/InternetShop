package ua.garmash.internetshop.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "baskets")
public class Basket {
    private static final String SEQ_NAME = "basket_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ElementCollection
    @Column(name = "amount", nullable = false)
    @MapKeyJoinColumn(name = "product_id")
    private Map<Product, Long> items = new HashMap<>();
}
