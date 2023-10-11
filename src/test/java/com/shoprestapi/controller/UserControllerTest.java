package com.shoprestapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoprestapi.exception.UserNotFoundException;
import com.shoprestapi.model.User;
import com.shoprestapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
class UserControllerTest {
    private static final String END_POINT_PATH = "/api/users";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService service;

    @Test
    public void testAddShouldReturn400BadRequest() throws Exception {
        User newUser = new User();

        String requestBody = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post(END_POINT_PATH).contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print())
        ;
    }

    @Test
    public void testAddShouldReturn201Created() throws Exception {
        User newUser = new User();
        newUser.setName("Bob");
        newUser.setEmail("bob@email.com");
        newUser.setPassword("bob123");

        // thanReturn(newUser.id(1L))
        Mockito.when(service.create(newUser)).thenReturn(newUser);

        String requestBody = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post(END_POINT_PATH).contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andDo(print())
        ;

    }

    @Test
    public void testGetShouldReturn404NotFound() throws Exception {
        Long userId = 123L;
        String requestURI = END_POINT_PATH + "/" + userId;

        Mockito.when(service.getById(userId)).thenThrow(UserNotFoundException.class);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testGetShouldReturn200OK() throws Exception {
        Long userId = 123L;
        String requestURI = END_POINT_PATH + "/" + userId;
        String email = "bob@email.com";

        User user = new User();
        user.setName("Bob");
        user.setEmail("bob@email.com");
        user.setPassword("bob123");

        Mockito.when(service.getById(userId)).thenReturn(user);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.email", is(email)))
                .andDo(print());
    }

    @Test
    public void testListShouldReturn204NoContent() throws Exception {
        Mockito.when(service.getAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void testListShouldReturn200OK() throws Exception {
        User user1 = new User();
        user1.setName("Bob");
        user1.setEmail("bob@email.com");
        user1.setPassword("bob123");

        User user2 = new User();
        user2.setName("Jack");
        user2.setEmail("jack@email.com");
        user2.setPassword("jack123");

        List<User> listUser = List.of(user1, user2);

        Mockito.when(service.getAll()).thenReturn(listUser);

        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].email", is("bob@email.com")))
                .andExpect(jsonPath("$[1].email", is("jack@email.com")))
                .andDo(print());
    }

    @Test
    public void testEditShouldReturn404NotFound() throws Exception {
        Long nonExistentUserId = 999L;

        Mockito.when(service.edit(Mockito.eq(nonExistentUserId), Mockito.any(User.class)))
                .thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(MockMvcRequestBuilders.put(END_POINT_PATH + "/" + nonExistentUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new User())))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testEditShouldReturn400BadRequest() throws Exception {
        User user = new User();
        user.setName("Jack");
        user.setEmail("jackemail.com.///////");
        user.setPassword("jack123");

        Long userId = user.getId();
        String requestURI = END_POINT_PATH + "/" + userId;

        String requestBody = objectMapper.writeValueAsString(user);

        mockMvc.perform(patch(requestURI).contentType("application/json").content(requestBody))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testEditShouldReturn200OK() throws Exception {

        User userToEdit = new User();
        userToEdit.setId(123L);
        userToEdit.setName("Jack");
        userToEdit.setEmail("jack@email.com");
        userToEdit.setPassword("jack123");

        Mockito.when(service.edit(123L, userToEdit)).thenReturn(userToEdit);

        String requestBody = objectMapper.writeValueAsString(userToEdit);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testDeleteShouldReturn404NotFound() throws Exception {
        Long userId = 123L;
        String requestURI = END_POINT_PATH + "/" + userId;

        Mockito.doThrow(UserNotFoundException.class).when(service).delete(userId);;

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testDeleteShouldReturn200OK() throws Exception {
        Long userId = 123L;
        String requestURI = END_POINT_PATH + "/" + userId;

        Mockito.doNothing().when(service).delete(userId);

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isOk())
                .andDo(print());
    }
}