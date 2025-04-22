package nakup.order.model.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.List;

public record OrderFormedEvent(
        @JsonProperty("user-id")
        Long userId,
        @JsonProperty("created-at")
        Timestamp createdAt,
        @JsonProperty("order-items")
        List<OrderItem> orderItems
) { }
