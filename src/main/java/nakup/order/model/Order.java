package nakup.order.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "orders")
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

    @OneToMany //mappedBy = "order"
    List<OrderItem> items;
}
