package ru.skillbox.paymentservice.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import ru.skillbox.paymentservice.domain.OrderKafkaDto;
import ru.skillbox.paymentservice.domain.PaymentEvent;
import ru.skillbox.paymentservice.domain.rollback.PaymentRollbackEvent;
import ru.skillbox.paymentservice.handler.binding.PaymentProcessorBinding;
import ru.skillbox.paymentservice.service.PaymentService;

@Service
@EnableBinding(PaymentProcessorBinding.class)
public class OrderCreatedEventHandler implements EventHandler<OrderKafkaDto, PaymentEvent> {
    private final PaymentService paymentService;

    @Autowired
    @Qualifier(PaymentRollbackEvent.EVENT)
    private MessageChannel outputRollback;

    @Autowired
    public OrderCreatedEventHandler(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @StreamListener(OrderKafkaDto.EVENT)
    @SendTo(PaymentEvent.EVENT)
    public PaymentEvent handleEvent(OrderKafkaDto event) {
        try {
            PaymentEvent paymentEvent = paymentService.processPayment(event);
            if (paymentEvent == null) {
                sendRollbackEvent(event);
            }
            return paymentEvent;
        } catch (Exception e) {
            sendRollbackEvent(event);
            throw e;
        }
    }

    private void sendRollbackEvent(OrderKafkaDto event) {
        PaymentRollbackEvent rollbackEvent = PaymentRollbackEvent.toPaymentRollbackEvent(event);
        outputRollback.send(MessageBuilder.withPayload(rollbackEvent).build());
    }
}
