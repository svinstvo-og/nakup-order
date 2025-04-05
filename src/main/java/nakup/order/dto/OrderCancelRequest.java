package nakup.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class OrderCancelRequest {
    @JsonProperty("order-id")
    private Long orderId;
    @JsonProperty("user-id")
    private Long userId;
}
