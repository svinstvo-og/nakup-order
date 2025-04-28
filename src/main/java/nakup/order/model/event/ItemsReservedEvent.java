package nakup.order.model.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public record ItemsReservedEvent(
        @JsonProperty("user-id")
        Long userId,
        @JsonProperty("order-id")
        Long orderId,
        @JsonProperty("created-at")
        Timestamp createdAt,
        boolean success
) { }
