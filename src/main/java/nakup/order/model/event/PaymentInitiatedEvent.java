package nakup.order.model.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentInitiatedEvent(
        @JsonProperty("order-id")
        Long orderId,
        @JsonProperty("user-id")
        Long userId,
        double amount){
}
