package ru.skillbox.paymentservice.service;

import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.paymentservice.domain.User;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {
    User getUserByUsername(String username);
    User getUser(Long id);
    List<User> getUsers();
    User createUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    boolean exists(Long id);
    boolean checkBalance(String username);
    User addAmount(Long id, BigDecimal amount);
    User addAmount(String username, BigDecimal amount);
    User deleteAmount(Long id, BigDecimal amount);
    User deleteAmount(String username, BigDecimal amount);
}
