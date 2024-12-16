package ru.skillbox.paymentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.paymentservice.client.OrderClient;
import ru.skillbox.paymentservice.domain.*;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final long payTime = (long) (2000 * Math.random() + 400);
    private final double payCriteria = 0.85;

    private final UserService userService;
    private final OrderClient orderClient;

    public PaymentEvent processPayment(OrderKafkaDto event) {
        if (pay(event.getUsername(), event.getCost(), event.getCount())) {
            PaymentEvent paymentEvent = PaymentEvent.toPaymentEvent(event);
            StatusDto status = new StatusDto(OrderStatus.PAID, ServiceName.PAYMENT_SERVICE, "Pay order");
            orderClient.updateOrderStatus(event.getId(), status);
            return paymentEvent;
        } else {
            StatusDto status = new StatusDto(OrderStatus.PAYMENT_FAILED, ServiceName.PAYMENT_SERVICE, "Failed pay order");
            orderClient.updateOrderStatus(event.getId(), status);
            return null;
        }
    }

    private boolean pay(String username, BigDecimal coast, Integer count) {
        try {
            User user = userService.deleteAmount(username, coast.multiply(BigDecimal.valueOf(count)));
            return user != null;
        } catch (Exception e) {
            return false;
        }
    }
}
