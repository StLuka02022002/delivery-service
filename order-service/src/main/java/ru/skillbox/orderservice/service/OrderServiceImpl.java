package ru.skillbox.orderservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.orderservice.controller.OrderNotFoundException;
import ru.skillbox.orderservice.domain.*;
import ru.skillbox.orderservice.repository.OrderRepository;

import java.util.Optional;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final KafkaService kafkaService;

    private final OrderProcessor orderProcessor;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, KafkaService kafkaService, OrderProcessor orderProcessor) {
        this.orderRepository = orderRepository;
        this.kafkaService = kafkaService;
        this.orderProcessor = orderProcessor;
    }

    @Transactional
    @Override
    public Optional<Order> addOrder(OrderDto orderDto, String username) {
        Order newOrder = new Order(
                orderDto.getDepartureAddress(),
                orderDto.getDestinationAddress(),
                orderDto.getDescription(),
                orderDto.getCost(),
                orderDto.getCount(),
                OrderStatus.REGISTERED
        );
        newOrder.addStatusHistory(newOrder.getStatus(), ServiceName.ORDER_SERVICE, "Order created");
        Order order = orderRepository.save(newOrder);
        orderProcessor.process(order,username);
        //kafkaService.produce(OrderKafkaDto.toKafkaDto(order,username));
        return Optional.of(order);
    }

    @Transactional
    @Override
    public void updateOrderStatus(Long id, StatusDto statusDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        if (order.getStatus() == statusDto.getStatus()) {
            log.info("Request with same status {} for order {} from service {}", statusDto.getStatus(), id, statusDto.getServiceName());
            return;
        }
        order.setStatus(statusDto.getStatus());
        order.addStatusHistory(statusDto.getStatus(), statusDto.getServiceName(), statusDto.getComment());
        Order resultOrder = orderRepository.save(order);
        //kafkaService.produce(OrderKafkaDto.toKafkaDto(resultOrder));
    }
}
