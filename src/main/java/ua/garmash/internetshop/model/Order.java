package ua.garmash.internetshop.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
	private static final String SEQ_NAME = "order_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
	@SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	@CreationTimestamp
	private LocalDateTime created;
	@UpdateTimestamp
	private LocalDateTime changed;
	private BigDecimal sum;
	private String address;
	private String recipient;
	private String phone;
	private String email;
	@Enumerated(EnumType.STRING)
	private PaymentOptions payment;
	@Enumerated(EnumType.STRING)
	private DeliveryOptions delivery;
	@OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
	private List<OrderDetail> details;
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
}
