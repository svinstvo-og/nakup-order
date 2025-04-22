package nakup.order.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;
import nakup.order.model.Order;
import nakup.order.model.OrderItem;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class OrderResponce {
    Long id;

    Long userId;

    Timestamp placedAt;
    Timestamp deliveredAt;
    Timestamp paidAt;
    Timestamp cancelledAt;

    String status;
    Double totalPrice;

    Long paymentId;

    List<Long> itemIds;

    public OrderResponce(Order order) {
        this.id = order.getId();
        this.userId = order.getUserId();
        this.placedAt = order.getPlacedAt();
        this.deliveredAt = order.getDeliveredAt();
        this.paidAt = order.getPaidAt();
        this.cancelledAt = order.getCancelledAt();
        this.status = order.getStatus();
        this.totalPrice = order.getTotalPrice();
        this.paymentId = order.getPaymentId();

        this.itemIds = new ArrayList<>();

        for (OrderItem item : order.getItems()) {
            itemIds.add(item.getId());
        }
    }
}
