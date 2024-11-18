package com.auction.controllerTest;

import com.auction.controller.UserController;
import com.auction.entities.User;
import com.auction.exception.GlobalExceptionHandler;
import com.auction.exception.UserNotFoundException;
import com.auction.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllUsers() throws Exception {
        when(userService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(userService, times(1)).findAll();
    }

    @Test
    public void testGetUserById_Success() throws Exception {
        User user = new User();
        user.setId(1);
        user.setName("John Doe");

        when(userService.findById(1)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(userService, times(1)).findById(1);
    }

    @Test
    public void testGetUserById_NotFound() throws Exception {
        when(userService.findById(1)).thenThrow(new UserNotFoundException(1));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findById(1);
    }

    @Test
    public void testAddNewUser() throws Exception {
        User user = new User();
        user.setId(1);
        user.setName("John Doe");

        when(userService.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users/addNewUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(userService, times(1)).save(any(User.class));
    }

    @Test
    public void testDeleteUser_Success() throws Exception {
        User user = new User();
        user.setId(1);

        when(userService.deleteById(1)).thenReturn(user);

        mockMvc.perform(delete("/users/removeUser/{userId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(userService, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteUser_NotFound() throws Exception {
        int userId = 1;

        doThrow(new UserNotFoundException(userId)).when(userService).deleteById(userId);

        mockMvc.perform(delete("/users/removeUser/{userId}", userId))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).deleteById(userId);
    }

    @Test
    public void testUpdateUser_Success() throws Exception {
        int userId = 1;
        User user = new User();
        user.setName("Updated User");

        when(userService.updateUser(eq(userId), any(User.class))).thenReturn(user);

        mockMvc.perform(put("/users/updateUser/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated User"));

        verify(userService, times(1)).updateUser(eq(userId), any(User.class));
    }

    @Test
    public void testUpdateUser_NotFound() throws Exception {
        int userId = 1;
        User user = new User();
        user.setName("Updated User");

        when(userService.updateUser(eq(userId), any(User.class))).thenThrow(new UserNotFoundException(userId));

        mockMvc.perform(put("/users/updateUser/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).updateUser(eq(userId), any(User.class));
    }
}
