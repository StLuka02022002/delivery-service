package ru.skillbox.paymentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.paymentservice.domain.User;
import ru.skillbox.paymentservice.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public User getUserByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(()->
                new EntityNotFoundException("User with username = " + username + " not found"));
    }

    @Override
    public User getUser(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User with id = " + id + " not found"));
    }

    @Override
    public List<User> getUsers() {
        return repository.findAll();
    }

    @Override
    public User createUser(User user) {
        return repository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        if (exists(id)) {
            user.setId(id);
            return repository.save(user);
        }
        return null;
    }

    @Override
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean exists(Long id) {
        return repository.existsById(id);
    }

    @Override
    public boolean checkBalance(String username) {

        return false;
    }

    @Override
    @Transactional
    public User addAmount(Long id, BigDecimal amount) {
        User user = getUser(id);
        BigDecimal result = user.getAmount().add(amount);
        user.setAmount(result);
        return repository.save(user);
    }

    @Override
    public User addAmount(String username, BigDecimal amount) {
        User user = getUserByUsername(username);
        BigDecimal result = user.getAmount().add(amount);
        user.setAmount(result);
        return repository.save(user);
    }

    @Override
    @Transactional
    public User deleteAmount(Long id, BigDecimal amount) {
        User user = getUser(id);
        return deleteAmount(user,amount);
    }

    @Override
    @Transactional
    public User deleteAmount(String username, BigDecimal amount) {
        User user = getUserByUsername(username);
        return deleteAmount(user,amount);
    }

    private User deleteAmount(User user, BigDecimal amount){
        BigDecimal result = user.getAmount().subtract(amount);
        if(result.compareTo(BigDecimal.ZERO)<0){
            throw new IllegalArgumentException("Amount must be more zero");
        }
        user.setAmount(result);
        return repository.save(user);
    }
}
