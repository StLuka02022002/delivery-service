package ru.skillbox.orderservice.handler;

import ru.skillbox.orderservice.domain.Event;

public interface EventHandler<T extends Event> {

    void handleEvent(T event);
}
