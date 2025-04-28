package nakup.order.service.event.listener;

import nakup.order.model.event.ItemsReservedEvent;
import nakup.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ItemsReservedListener {

    @Autowired
    OrderService orderService;

    @KafkaListener(topics = "items-reserved", groupId = "order-service", properties = {"spring.json.value.default.type=nakup.order.model.event.ItemsReservedEvent"})
    public void handleOrderPlaced(ItemsReservedEvent event) {
        System.out.println("Accepted an 'items reserved' event: " + event);
        orderService.updateReservation(event.orderId(), event.success());
    }
}
