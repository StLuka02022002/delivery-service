package ru.skillbox.deliveryservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.skillbox.deliveryservice.domain.StatusDto;

@Service
@RequiredArgsConstructor
public class OrderClient {

    private final RestTemplate restTemplate;

    public boolean updateOrderStatus(long orderId, StatusDto statusDto) {
        String url = "http://ORDER-SERVICE/api/order/" + orderId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<StatusDto> request = new HttpEntity<>(statusDto, headers);

        try {
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PATCH, request, Void.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception ex) {
            System.err.println("Failed to update order status: " + ex.getMessage());
            return false;
        }
    }
}
