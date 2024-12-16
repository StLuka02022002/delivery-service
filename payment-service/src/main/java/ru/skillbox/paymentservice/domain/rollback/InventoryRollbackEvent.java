package ru.skillbox.paymentservice.domain.rollback;

import lombok.Builder;
import lombok.Data;
import ru.skillbox.paymentservice.domain.Event;

import java.math.BigDecimal;

@Data
@Builder
public class InventoryRollbackEvent implements Event {

    public static final String EVENT = "inventory-rollback";

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
