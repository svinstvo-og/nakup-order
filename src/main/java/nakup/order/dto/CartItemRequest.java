package nakup.order.dto;

import lombok.*;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CartItemRequest {
    private Long userId;
    private Long productId;
    private Long quantity;
    private Double unitPrice;
}
