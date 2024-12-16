package ru.skillbox.paymentservice.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import ru.skillbox.paymentservice.client.OrderClient;
import ru.skillbox.paymentservice.domain.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class PaymentServiceTest {

    @Autowired
    @MockBean
    private UserService userService;

    @Autowired
    @MockBean
    private OrderClient orderClient;

    @Autowired
    private PaymentService paymentService;

    @Configuration
    @ComponentScan(basePackageClasses = {PaymentService.class})
    public static class TestConf {
    }

    @Test
    public void processPaymentSuccess() {
        OrderKafkaDto orderEvent = new OrderKafkaDto(
                1L,
                OrderStatus.REGISTERED.toString(),
                "2024-12-16T10:00:00",
                "2024-12-16T10:30:00",
                "123 Yellow St",
                "456 Green St",
                BigDecimal.valueOf(100),
                2,
                "testUser"
        );
        User updatedUser = new User(1L, "testUser", "test@mail.com", BigDecimal.valueOf(800));

        when(userService.deleteAmount(eq("testUser"), eq(BigDecimal.valueOf(200))))
                .thenReturn(updatedUser);

        PaymentEvent result = paymentService.processPayment(orderEvent);

        assertNotNull(result);
        assertEquals(orderEvent.getId(), result.getOrderId());
        assertEquals(orderEvent.getUsername(), result.getUsername());
        assertEquals(orderEvent.getCost().multiply(BigDecimal.valueOf(orderEvent.getCount())),
                result.getCost().multiply(BigDecimal.valueOf(result.getCount())));

        verify(userService, times(1)).deleteAmount("testUser", BigDecimal.valueOf(200));
        verify(orderClient, times(1)).updateOrderStatus(eq(orderEvent.getId()), any(StatusDto.class));
    }

    @Test
    public void processPaymentFailure() {
        OrderKafkaDto orderEvent = new OrderKafkaDto(
                1L,
                OrderStatus.REGISTERED.toString(),
                "2024-12-16T10:00:00",
                "2024-12-16T10:30:00",
                "123 Yellow St",
                "456 Green St",
                BigDecimal.valueOf(100),
                2,
                "testUser"
        );

        when(userService.deleteAmount(eq("testUser"), eq(BigDecimal.valueOf(2*100))))
                .thenThrow(new RuntimeException("Insufficient funds"));

        PaymentEvent result = paymentService.processPayment(orderEvent);

        assertNull(result);
        verify(userService, times(1)).deleteAmount("testUser", BigDecimal.valueOf(2*100));
        verify(orderClient, times(1)).updateOrderStatus(eq(orderEvent.getId()), argThat(status ->
                status.getStatus() == OrderStatus.PAYMENT_FAILED
                        && status.getServiceName() == ServiceName.PAYMENT_SERVICE));
    }

    @Test
    public void payTrue() {
        String username = "testUser";
        BigDecimal cost = BigDecimal.valueOf(100);
        Integer count = 2;

        User updatedUser = new User(1L, "testUser", "test@mail.com", BigDecimal.valueOf(800));
        when(userService.deleteAmount(eq(username), eq(cost.multiply(BigDecimal.valueOf(count)))))
                .thenReturn(updatedUser);

        boolean result = paymentService.processPayment(
                new OrderKafkaDto(
                        3L,
                        OrderStatus.REGISTERED.toString(),
                        "2024-12-16T10:00:00",
                        "2024-12-16T10:30:00",
                        "123 Yellow St",
                        "456 Green St",
                        cost,
                        count,
                        username
                )) != null;

        assertTrue(result);
        verify(userService, times(1))
                .deleteAmount(eq(username), eq(cost.multiply(BigDecimal.valueOf(count))));
    }

    @Test
    public void payFalse() {
        String username = "testUser";
        BigDecimal cost = BigDecimal.valueOf(200);
        Integer count = 1;

        when(userService.deleteAmount(eq(username), eq(cost.multiply(BigDecimal.valueOf(count)))))
                .thenThrow(new RuntimeException("Insufficient funds"));

        boolean result = paymentService.processPayment(
                new OrderKafkaDto(
                        4L,
                        OrderStatus.REGISTERED.toString(),
                        "2024-12-16T13:00:00",
                        "2024-12-16T13:30:00",
                        "123 Yellow St",
                        "456 Green St",
                        cost,
                        count,
                        username
                )) == null;

        assertTrue(result);
        verify(userService, times(1))
                .deleteAmount(eq(username), eq(cost.multiply(BigDecimal.valueOf(count))));
    }
}
