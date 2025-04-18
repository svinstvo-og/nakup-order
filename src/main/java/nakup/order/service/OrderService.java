package nakup.order.service;

import jakarta.transaction.Transactional;
import nakup.order.dto.CartItemRequest;
import nakup.order.dto.ReserveItemsResponse;
import nakup.order.dto.UpdatePaymentRequest;
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
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public Order validate(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty()) {
            throw new RuntimeException("Order not found");
        }
        return order.get();
    }

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

    @Transactional
    public void updatePayment(Order order, UpdatePaymentRequest request) {

        order.setPaymentId(request.getPaymentId());
        if (request.getSuccess()) {
            order.setStatus("PENDING SHIPPING DETAILS");
            order.setPaidAt(LocalDateTime.now());
            System.out.println("pending shipping details");
        }
        else {
            order.setStatus("PAYMENT_FAILED");
            System.out.println("payment failed");
            //order.setCancelledAt(LocalDateTime.now());
        }
    }

    public List<ReserveItemsResponse> reserveItems(Order order) {
        List<ReserveItemsResponse> reserveItems = new ArrayList<>();
        List<OrderItem> orderItems = order.getItems();

        for (OrderItem orderItem : orderItems) {
            ReserveItemsResponse reserve = new ReserveItemsResponse(orderItem);
            reserve.setUserId(order.getUserId());
            reserveItems.add(reserve);
        }

        return reserveItems;
    }

    @Transactional
    public void cancelOrder(Order order) {
        order.setStatus("CANCELLED");
        order.setCancelledAt(LocalDateTime.now());
    }
}
