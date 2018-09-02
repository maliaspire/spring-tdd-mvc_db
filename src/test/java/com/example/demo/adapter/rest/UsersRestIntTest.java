package com.example.demo.adapter.rest;

import com.example.demo.DemoApplication;
import com.example.demo.adapter.respository.UserMongoRepository;
import com.example.demo.adapter.respository.UserProjection;
import com.example.demo.aggregates.UserAggregate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = DemoApplication.class)
@AutoConfigureMockMvc
public class UsersRestIntTest {

    @Autowired
    private MockMvc mockMvc;

    private UserAggregate aggregate = new UserAggregate(null, "test", 10);
    private UserProjection projection1 = new UserProjection("1", "test", 10);
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void whenSaveNewUser() throws Exception {

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(aggregate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("username").value("test"))
                .andExpect(jsonPath("age").value(10));
    }

    @Test
    public void whenGetUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/id/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("username").value("test user"))
                .andExpect(jsonPath("id").value(2))
                .andExpect(jsonPath("age").value(28));
    }

    @Test
    public void whenNotFoundUserReturnNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/id/1"))
                .andExpect(status().isNotFound());

    }

    @Test
    public void whenGetAllEmptyUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void whenGetAllUsers() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("test user1"))
                .andExpect(jsonPath("$[0].age").value(28))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].username").value("test user2"))
                .andExpect(jsonPath("$[1].age").value(25));
    }

}
