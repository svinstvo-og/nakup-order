package nakup.order.service.event.listener;

import nakup.order.model.event.OrderFormedEvent;
import nakup.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderPostListener {

    @Autowired
    OrderService orderService;

    @KafkaListener(topics = "order-formed", groupId = "order-service", properties = {"spring.json.value.default.type=nakup.order.model.event.OrderFormedEvent"})
    public void handleOrderPlaced(OrderFormedEvent event) {
        System.out.println("Accepted an order placed event: " + event);
        orderService.buildOrderFromEvent(event);
    }
}
