package ru.skillbox.deliveryservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.deliveryservice.client.OrderClient;
import ru.skillbox.deliveryservice.domain.*;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final OrderClient orderClient;

    @Override
    public DeliveryEvent processDelivery(InventoryEvent event) {
        if (deliveryToAddress(event.getDepartureAddress(), event.getDestinationAddress())) {
            DeliveryEvent deliveryEvent = DeliveryEvent.toDeliveryEvent(event);
            StatusDto status = new StatusDto(OrderStatus.DELIVERED, ServiceName.DELIVERY_SERVICE, "Delivered order");
            orderClient.updateOrderStatus(event.getOrderId(), status);
            return deliveryEvent;
        } else {
            StatusDto status = new StatusDto(OrderStatus.DELIVERY_FAILED, ServiceName.DELIVERY_SERVICE, "Failed get order from delivery");
            orderClient.updateOrderStatus(event.getOrderId(), status);
            return null;
        }
    }

    public boolean deliveryToAddress(String departureAddress, String destinationAddress) {
        try {
            Thread.sleep((long) (Math.random() * 2000 +
                    10L * (departureAddress.length() + destinationAddress.length())));
            return Math.random() < 0.85;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
