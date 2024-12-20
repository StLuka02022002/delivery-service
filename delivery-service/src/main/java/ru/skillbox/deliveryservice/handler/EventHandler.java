package ru.skillbox.deliveryservice.handler;

import ru.skillbox.deliveryservice.domain.Event;

public interface EventHandler<T extends Event, R extends Event> {

    R handleEvent(T event);
}
