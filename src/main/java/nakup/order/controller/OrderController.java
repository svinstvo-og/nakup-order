package nakup.order.controller;

import nakup.order.dto.*;
import nakup.order.model.Order;
import nakup.order.repository.OrderRepository;
import nakup.order.service.OrderService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping
    public List<ReserveItemsResponse> postOrder(@RequestBody List<CartItemRequest> requests) {
        Order order = orderService.buildOrder(requests);
        return orderService.reserveItems(order);
    }

    //TODO: admin access only
    @GetMapping("/all")
    public List<OrderResponce> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponce> orderResponces = new ArrayList<>();
        for (Order order : orders) {
            orderResponces.add(new OrderResponce(order));
        }
        return orderResponces;
    }

    @PutMapping("payment")
    public void updatePaymentDetails(@RequestBody UpdatePaymentRequest request) throws BadRequestException {
        Order order = orderService.validate(request.getOrderId());

        if (request.getSuccess() == null) {
            throw new BadRequestException();
        }

        if (order.getCancelledAt() != null) {
            throw new BadRequestException("Order is already cancelled");
        }

        orderService.updatePayment(order, request);
    }

    //TODO: verified user can cancel only own orders
    @PutMapping("/cancel")
    public void cancelOrder(@RequestBody OrderCancelRequest request) throws BadRequestException {
        Order order = orderService.validate(request.getOrderId());
        orderService.cancelOrder(order);
    }

    @GetMapping
    public OrderResponce getOrderById(@RequestBody OrderCancelRequest request) {
        return new OrderResponce(orderService.validate(request.getOrderId()));
    }
}
