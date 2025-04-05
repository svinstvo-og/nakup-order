package nakup.order.service;

import jakarta.transaction.Transactional;
import nakup.order.dto.CartItemRequest;
import nakup.order.model.Order;
import nakup.order.model.OrderItem;
import nakup.order.repository.OrderItemRepository;
import nakup.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional
    public Order buildOrder(List<CartItemRequest> requests) {
        Order order = new Order();
        List<OrderItem> orderItems = new ArrayList<>();
        Double totalPrice = 0.0;

        if (requests == null || requests.isEmpty()) {
            throw new RuntimeException("requests is null or empty");
        }

        for (CartItemRequest request : requests) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(request.getProductId());
            orderItem.setQuantity(request.getQuantity());
            orderItem.setUnitPrice(request.getUnitPrice());
            orderItem.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            totalPrice += request.getUnitPrice();

            orderItems.add(orderItem);

            orderItemRepository.save(orderItem);
        }

        order.setItems(orderItems);
        order.setStatus("PENDING");
        order.setPlacedAt(LocalDateTime.now());
        order.setUserId(requests.get(0).getUserId());
        order.setTotalPrice(totalPrice);

        orderRepository.save(order);

        return order;
    }

}
