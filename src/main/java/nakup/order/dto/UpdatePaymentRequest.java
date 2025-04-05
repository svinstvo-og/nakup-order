package nakup.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UpdatePaymentRequest {
    @JsonProperty("payment-id")
    Long paymentId;
    @JsonProperty("order-id")
    Long orderId;
    
    Boolean success;
}
