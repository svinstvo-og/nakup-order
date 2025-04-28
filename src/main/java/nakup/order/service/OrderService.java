package nakup.order.service;

import jakarta.transaction.Transactional;
import nakup.order.dto.CartItemRequest;
import nakup.order.dto.ReserveItemsResponse;
import nakup.order.dto.UpdatePaymentRequest;
import nakup.order.model.Order;
import nakup.order.model.OrderItem;
import nakup.order.model.event.OrderCreatedEvent;
import nakup.order.model.event.OrderFormedEvent;
import nakup.order.model.event.PaymentInitiatedEvent;
import nakup.order.repository.OrderItemRepository;
import nakup.order.repository.OrderRepository;
import nakup.order.service.event.producer.OrderEventPublisher;
import nakup.order.service.event.producer.PaymentInitiatedProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Autowired
    private PaymentInitiatedProducer paymentInitiatedProducer;

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
        order.setPlacedAt(Timestamp.valueOf(LocalDateTime.now()));
        order.setUserId(requests.get(0).getUserId());
        order.setTotalPrice(totalPrice);

        orderRepository.save(order);

        return order;
    }

    @Transactional
    public Order buildOrderFromEvent(OrderFormedEvent event) {
        Order order = new Order();
        List<OrderItem> orderItems = new ArrayList<>();

        if (event.items().isEmpty()) {
            throw new RuntimeException("Order items is null or empty");
        }

        OrderItem orderItem;
        for (Long productId: event.items().keySet()) {
            orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(productId);
            orderItem.setQuantity(event.items().get(productId).longValue());
            orderItem.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            orderItems.add(orderItem);

            orderItemRepository.save(orderItem);
        }

        order.setItems(orderItems);
        order.setStatus("PENDING");
        order.setPlacedAt(Timestamp.valueOf(LocalDateTime.now()));
        order.setUserId(event.userId());

        orderRepository.save(order);
        createCreatedEvent(order);
        return order;
    }

    public void createCreatedEvent(Order order) {
        HashMap<Long, Integer> items = new HashMap<>();

        for (OrderItem item: order.getItems()) {
            items.put(item.getProductId(), item.getQuantity().intValue());
        }

        orderEventPublisher.publishOrderCreatedEvent(new OrderCreatedEvent(order.getUserId(), order.getId(), order.getPlacedAt(), items));
    }

    @Transactional
    public void updatePayment(Order order, UpdatePaymentRequest request) {

        order.setPaymentId(request.getPaymentId());
        if (request.getSuccess()) {
            order.setStatus("PENDING SHIPPING DETAILS");
            order.setPaidAt(Timestamp.valueOf(LocalDateTime.now()));
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
        order.setCancelledAt(Timestamp.valueOf(LocalDateTime.now()));
    }

    @Transactional
    public void updatePrice(Long orderId, HashMap<Long, Double> prices) {
        Order order = validate(orderId);
        double totalPrice = 0.0;
        List<OrderItem> items = order.getItems();

        for (OrderItem item: items) {
            //System.out.println(item.getProductId() + ": " + prices.get(item.getProductId()));
            if (prices.get(item.getProductId()) == null) {
                throw new RuntimeException("unit price for product: " + item.getProductId() + " is null");
            }
            item.setUnitPrice(prices.get(item.getProductId()));
            item.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            totalPrice += item.getUnitPrice() * item.getQuantity();
        }

        order.setItems(items);
        order.setTotalPrice(totalPrice);

        System.out.println("Total price: " + totalPrice);

        if (Objects.equals(order.getStatus(), "PENDING")) {
            order.setStatus("PENDING RESERVATION");
        }
        if (Objects.equals(order.getStatus(), "PENDING TOTAL EVALUATION")) {
            order.setStatus("PENDING PAYMENT INITIATION");

            paymentInitiatedProducer.publishPaymentInitializedEvent(new PaymentInitiatedEvent(
                    orderId,
                    order.getUserId(),
                    totalPrice
            ));
        }
    }

    @Transactional
    public void updateReservation(Long orderId, boolean success) {
        Order order = validate(orderId);

        if (success) {
            if (order.getTotalPrice() == null) {
                order.setStatus("PENDING TOTAL EVALUATION");
            }
            else {
                order.setStatus("PENDING PAYMENT INITIATION");

                paymentInitiatedProducer.publishPaymentInitializedEvent(new PaymentInitiatedEvent(
                        orderId,
                        order.getUserId(),
                        order.getTotalPrice()
                ));
            }
        }
        else {
            throw new RuntimeException("Reservation failed");
        }
    }
}