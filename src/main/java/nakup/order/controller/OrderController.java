package nakup.order.controller;

import nakup.order.dto.CartItemRequest;
import nakup.order.dto.OrderResponce;
import nakup.order.model.Order;
import nakup.order.model.OrderItem;
import nakup.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public OrderResponce postOrder(@RequestBody List<CartItemRequest> requests) {

        for (CartItemRequest request : requests) {
            System.out.println(request.getQuantity());
        }

        return new OrderResponce(orderService.buildOrder(requests));
    }
}
