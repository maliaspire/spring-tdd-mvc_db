package com.example.demo.adapter.rest;

import com.example.demo.adapter.respository.UserMongoRepository;
import com.example.demo.adapter.respository.UserProjection;
import com.example.demo.aggregates.UserAggregate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UsersRest.class)
public class UsersRestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserMongoRepository userMongoRepository;

    private String id = ObjectId.get().toHexString();
    private String name = "Test user";
    private int age = 10;
    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void whenSaveNewUser() throws Exception {
        given(userMongoRepository.save(any(UserAggregate.class))).willReturn(new UserProjection(id, name, age));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(new UserAggregate(null, name, age))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    public void whenGetUser() throws Exception {
        given(userMongoRepository.getUserById(any())).willReturn(Optional.of(new UserProjection(id, name, age)));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/id/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("username").value(name))
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("age").value(age));
    }

    @Test
    public void whenNotFoundUserReturnNotFoundException() throws Exception {
        given(userMongoRepository.getUserById(any())).willReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/users/id/{id}", ObjectId.get().toHexString()))
                .andExpect(status().isNotFound());

    }

    @Test
    public void whenGetAllEmptyUsers() throws Exception {
        given(userMongoRepository.getAll()).willReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void whenGetAllUsers() throws Exception {
        List<UserProjection> list = new ArrayList<>();
        list.add(new UserProjection(id, name, age));
        given(userMongoRepository.getAll()).willReturn(list);

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].username").value(name))
                .andExpect(jsonPath("$[0].age").value(age));
    }

    @Test
    public void whenDeleteAllUsers() throws Exception {
        mockMvc.perform(delete("/users"))
                .andExpect(status().isOk());
        verify(userMongoRepository).deleteAll();
    }

}
