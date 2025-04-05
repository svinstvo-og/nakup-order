package nakup.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import nakup.order.model.OrderItem;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ReserveItemsResponse {
    @JsonProperty("user-id")
    private Long userId;
    @JsonProperty("product-id")
    private Long productId;
    private Long quantity;

    public ReserveItemsResponse(OrderItem item) {
        this.productId = item.getProductId();
        this.quantity = item.getQuantity();
    }
}
