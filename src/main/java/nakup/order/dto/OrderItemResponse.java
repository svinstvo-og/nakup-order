package nakup.order.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class OrderItemResponse {
    @JsonProperty("product-id")
    private Long productId;
    private Integer quantity;
}

