package ru.skillbox.paymentservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ru.skillbox.paymentservice.domain.User;
import ru.skillbox.paymentservice.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(UserServiceImpl.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User(null, "testUser", "test@mail.com", BigDecimal.valueOf(1000));
        userRepository.save(user);
    }

    @Test
    public void getUserByUsernameSuccess() {
        User result = userService.getUserByUsername("testUser");
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        assertEquals(BigDecimal.valueOf(1000), result.getAmount());
    }

    @Test
    public void getUserByUsernameNotFound() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                userService.getUserByUsername("unknownUser"));
        assertEquals("User with username = unknownUser not found", exception.getMessage());
    }

    @Test
    public void addAmountSuccess() {
        User result = userService.addAmount(user.getId(), BigDecimal.valueOf(500));
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(1500), result.getAmount());
    }

    @Test
    public void deleteAmountSuccess() {
        User result = userService.deleteAmount(user.getId(), BigDecimal.valueOf(500));
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(500), result.getAmount());
    }

    @Test
    public void deleteAmountNegativeResult() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.deleteAmount(user.getId(), BigDecimal.valueOf(1500)));
        assertEquals("Amount must be more zero", exception.getMessage());

        User result = userService.getUser(user.getId());
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(1000), result.getAmount());
    }


    @Test
    public void createUserAndModifyAmount() {
        User newUser = new User(null, "newUser", "newuser@mail.com", BigDecimal.valueOf(1000));
        User createdUser = userService.createUser(newUser);
        assertNotNull(createdUser);
        assertEquals(BigDecimal.valueOf(1000), createdUser.getAmount());

        User userAfterAddingAmount = userService.addAmount(createdUser.getId(), BigDecimal.valueOf(500));
        assertNotNull(userAfterAddingAmount);
        assertEquals(BigDecimal.valueOf(1500), userAfterAddingAmount.getAmount());

        User userAfterDeletingAmount = userService.deleteAmount(createdUser.getId(), BigDecimal.valueOf(300));
        assertNotNull(userAfterDeletingAmount);
        assertEquals(BigDecimal.valueOf(1200), userAfterDeletingAmount.getAmount());
    }


    @Test
    public void updateUserSuccess() {
        User updatedUser = new User(null, "updatedUser", "updated@mail.com", BigDecimal.valueOf(2000));
        User result = userService.updateUser(user.getId(), updatedUser);
        assertNotNull(result);
        assertEquals("updatedUser", result.getUsername());
        assertEquals("updated@mail.com", result.getEmail());
        assertEquals(BigDecimal.valueOf(2000), result.getAmount());
    }

    @Test
    public void updateUserNotFound() {
        User updatedUser = new User(null, "updatedUser", "updated@mail.com", BigDecimal.valueOf(2000));
        User result = userService.updateUser(999L, updatedUser);
        assertNull(result);
    }

    @Test
    public void deleteUserSuccess() {
        userService.deleteUser(user.getId());
        assertFalse(userRepository.existsById(user.getId()));
    }

    @Test
    public void getUserByIdSuccess() {
        User result = userService.getUser(user.getId());
        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
    }

    @Test
    public void getUserByIdNotFound() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                userService.getUser(999L));
        assertEquals("User with id = 999 not found", exception.getMessage());
    }
}
