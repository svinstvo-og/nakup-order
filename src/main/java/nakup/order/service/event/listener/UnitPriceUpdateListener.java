package nakup.order.service.event.listener;

import nakup.order.model.event.UnitPriceUpdatedEvent;
import nakup.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UnitPriceUpdateListener {

    @Autowired
    private OrderService orderService;

    private static final String TOPIC = "unit-price-update";

    @KafkaListener(topics = TOPIC, groupId = "product-service", properties = {"spring.json.value.default.type=nakup.order.model.event.UnitPriceUpdatedEvent"})
    public void unitPriceUpdated(UnitPriceUpdatedEvent event) {
        System.out.println("Received event: " + event);
        orderService.updatePrice(event.orderId(), event.unitPrices());
        System.out.println("Prices updated");
    }
}
