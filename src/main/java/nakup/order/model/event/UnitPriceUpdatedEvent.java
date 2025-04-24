package nakup.order.model.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

public record UnitPriceUpdatedEvent(
        @JsonProperty("order-id") Long orderId,
        @JsonProperty("user-id") Long userId,
        @JsonProperty("unit-prices") HashMap<Long, Double> unitPrices) {
}

