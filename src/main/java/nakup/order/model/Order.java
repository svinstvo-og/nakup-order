package nakup.order.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
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
    private Long id;

    private Long userId;

    private Timestamp placedAt;
    private Timestamp deliveredAt;
    private Timestamp paidAt;
    private Timestamp cancelledAt;

    private String status;
    private Double totalPrice;

    private Long paymentId;

    @OneToMany //mappedBy = "order"
    private List<OrderItem> items;
}
