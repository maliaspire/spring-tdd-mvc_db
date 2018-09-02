package com.example.demo.adapter.rest;

import com.example.demo.DemoApplication;
import com.example.demo.adapter.respository.UserProjection;
import com.example.demo.aggregates.UserAggregate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = DemoApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UsersRestIntTest {

    @Autowired
    private MockMvc mockMvc;

    private static String id;
    private static String name = "Test user";
    private static int age = 10;
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void order1WhenSaveNewUser() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new UserAggregate(null, name, age))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(name))
                .andExpect(jsonPath("$.age").value(age))
                .andDo(result1 -> {
                    String jsonStr = result1.getResponse().getContentAsString();
                    UserProjection userProjection = mapper.readValue(jsonStr, UserProjection.class);
                    id = userProjection.getId();
                });

    }

    @Test
    public void order2WhenGetUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/id/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("username").value(name))
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("age").value(age));
    }

    @Test
    public void order3WhenNotFoundUserReturnNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/id/{id}", ObjectId.get().toHexString()))
                .andExpect(status().isNotFound());

    }

    @Test
    public void order4whenGetAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].username").value(name))
                .andExpect(jsonPath("$[0].age").value(age));
    }

    @Test
    public void order5WhenDeleteAllUsers() throws Exception {
        mockMvc.perform(delete("/users"))
                .andExpect(status().isOk());
    }

    @Test
    public void order6WhenGetAllEmptyUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

}
