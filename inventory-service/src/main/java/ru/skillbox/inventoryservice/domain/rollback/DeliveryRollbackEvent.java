package ru.skillbox.inventoryservice.domain.rollback;

import lombok.Builder;
import lombok.Data;
import ru.skillbox.inventoryservice.domain.Event;

import java.math.BigDecimal;

@Data
@Builder
public class DeliveryRollbackEvent implements Event {

    public static final String EVENT = "delivery-rollback";

    private Long orderId;

    private Integer count;

    private String status;

    private String creationTime;

    private String modifiedTime;

    private String departureAddress;

    private String destinationAddress;

    private BigDecimal cost;

    private String username;

    @Override
    public String getEvent() {
        return EVENT;
    }
}
