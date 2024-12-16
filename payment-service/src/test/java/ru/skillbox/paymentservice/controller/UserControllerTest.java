package ru.skillbox.paymentservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skillbox.paymentservice.domain.User;
import ru.skillbox.paymentservice.service.UserService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Configuration
    @ComponentScan(basePackageClasses = {UserController.class})
    public static class TestConf {
    }

    private User user;
    private List<User> users;

    @BeforeEach
    public void setUp() {
        user = new User(1L, "testUser", "poshta1@mail.ru", BigDecimal.valueOf(1000));
        users = Collections.singletonList(user);
    }

    @Test
    public void getUsers() throws Exception {
        Mockito.when(userService.getUsers()).thenReturn(users);
        mvc.perform(get("/payment/users"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(user.getUsername())));
    }

    @Test
    public void getUserById() throws Exception {
        Mockito.when(userService.getUser(1L)).thenReturn(user);
        mvc.perform(get("/payment/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(user.getUsername())));
    }

    @Test
    public void getUserByUsername() throws Exception {
        Mockito.when(userService.getUserByUsername("testUser")).thenReturn(user);
        mvc.perform(get("/payment/users/username/testUser"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(user.getUsername())));
    }

    @Test
    public void getUserByIdNotFound() throws Exception {
        Mockito.when(userService.getUser(999L)).thenReturn(null);
        mvc.perform(get("/payment/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getUserByUsernameNotFound() throws Exception {
        Mockito.when(userService.getUserByUsername("unknownUser")).thenReturn(null);
        mvc.perform(get("/payment/users/username/unknownUser"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createUser() throws Exception {
        User newUser = new User(null, "newUser", "poshta2@mail.ru", BigDecimal.valueOf(500));
        Mockito.when(userService.createUser(newUser)).thenReturn(user);
        mvc.perform(
                        post("/payment/users")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{" +
                                        "\"username\":\"newUser\"," +
                                        "\"email\":\"poshta2@mail.ru\"," +
                                        "\"amount\":500}"))
                .andExpect(status().isCreated());
    }

    @Test
    @DirtiesContext
    public void updateUser() throws Exception {
        User updatedUser = new User(null, "updatedUser", "poshta3@mail.ru", BigDecimal.valueOf(2000));
        User returnUser = new User(1L, "updatedUser", "poshta3@mail.ru", BigDecimal.valueOf(2000));
        Mockito.when(userService.updateUser(1L, updatedUser)).thenReturn(returnUser);
        mvc.perform(
                        put("/payment/users/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{" +
                                        "\"username\":\"updatedUser\"," +
                                        "\"email\":\"poshta3@mail.ru\"," +
                                        "\"amount\":2000}"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);
        mvc.perform(delete("/payment/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void addAmount() throws Exception {
        User updatedUser = new User(1L, "testUser", "poshta4@mail.ru", BigDecimal.valueOf(1500));
        Mockito.when(userService.addAmount(1L, BigDecimal.valueOf(500))).thenReturn(updatedUser);
        mvc.perform(
                        put("/payment/users/1/addAmount?amount=500"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteAmount() throws Exception {
        User updatedUser = new User(1L, "testUser", "pochta5@mail.ru", BigDecimal.valueOf(500));
        Mockito.when(userService.deleteAmount(1L, BigDecimal.valueOf(500))).thenReturn(updatedUser);
        mvc.perform(
                        put("/payment/users/1/deleteAmount?amount=500"))
                .andExpect(status().isOk());
    }
}
