package nakup.order.controller;

import nakup.order.dto.CartItemRequest;
import nakup.order.dto.OrderResponce;
import nakup.order.dto.UpdatePaymentRequest;
import nakup.order.model.Order;
import nakup.order.model.OrderItem;
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
    public OrderResponce postOrder(@RequestBody List<CartItemRequest> requests) {
        return new OrderResponce(orderService.buildOrder(requests));
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

    @PostMapping("/update/payment")
    public void updatePaymentDetails(@RequestBody UpdatePaymentRequest request) throws BadRequestException {
        Order order = orderService.validate(request.getOrderId());

        if (request.getSuccess() == null) {
            throw new BadRequestException();
        }

        orderService.updatePayment(order, request);
    }
}
