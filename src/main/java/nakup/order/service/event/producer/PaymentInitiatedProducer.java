package nakup.order.service.event.producer;

import nakup.order.model.event.OrderCreatedEvent;
import nakup.order.model.event.PaymentInitiatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentInitiatedProducer {
    private static final String TOPIC = "payment-initiated";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PaymentInitiatedProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPaymentInitializedEvent(PaymentInitiatedEvent event) {
        System.out.println("Sending payment-initialized event: " + event);
        kafkaTemplate.send(TOPIC, event);
    }
}
