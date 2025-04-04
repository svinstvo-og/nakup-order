package nakup.order.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "order")
public class Order {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;

    Long userId;

    LocalDateTime placedAt;
    LocalDateTime deliveredAt;
    LocalDateTime paidAt;
    LocalDateTime cancelledAt;

    String status;
    Double totalPrice;

    Long paymentId;

    @OneToMany(mappedBy = "order")
    List<OrderItem> items;
}
