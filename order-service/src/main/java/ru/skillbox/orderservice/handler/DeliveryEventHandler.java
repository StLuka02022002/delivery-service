package ru.skillbox.orderservice.handler;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;
import ru.skillbox.orderservice.domain.DeliveryEvent;
import ru.skillbox.orderservice.handler.binding.DeliveryOrderProcessorBinding;

@Service
@EnableBinding(DeliveryOrderProcessorBinding.class)
public class DeliveryEventHandler implements EventHandler<DeliveryEvent> {

    @StreamListener(DeliveryEvent.EVENT)
    public void handleEvent(DeliveryEvent event) {
        System.out.println("Order deliver");
    }
}
