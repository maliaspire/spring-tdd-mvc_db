package com.example.demo.adapter.respository;

import com.example.demo.aggregates.UserAggregate;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * 9/2/2018
 *
 * @author Mohammad Al-Najjar (Mx NINJA)
 */

@RunWith(SpringRunner.class)
public class UserMongoRepositoryTest {

    @MockBean
    private UsersDAO usersDAO;

    private UserMongoRepository repository;

    private String id = ObjectId.get().toHexString();
    private String name = "Test user";
    private int age = 10;

    @Before
    public void setup() {
        repository = new UserMongoRepository(usersDAO);
    }

    @Test
    public void whenDeleteUserWithId() {
        repository.deleteById(id);
        verify(usersDAO).deleteById(id);
    }

    @Test
    public void whenDeleteAll() {
        repository.deleteAll();
        verify(usersDAO).deleteAll();
    }

    @Test
    public void whenSaveUser() {

        given(usersDAO.save(any(UserProjection.class))).willReturn(new UserProjection(id, name, age));

        UserProjection projection = repository.save(new UserAggregate(null, name, age));
        assertNotNull(projection);
        assertEquals(id, projection.getId());
        assertEquals(name, projection.getUsername());
        assertEquals(age, projection.getAge());
    }

    @Test
    public void whenFindById() {
        given(usersDAO.findById(any())).willReturn(Optional.of(new UserProjection(id, name, age)));

        Optional<UserProjection> optional = repository.getUserById(id);

        assertTrue(optional.isPresent());
        assertEquals(id, optional.get().getId());
        assertEquals(name, optional.get().getUsername());
        assertEquals(age, optional.get().getAge());
    }

    @Test
    public void whenUserNotFound() {
        given(usersDAO.findById(any())).willReturn(Optional.empty());

        Optional<UserProjection> optional = repository.getUserById(any());

        assertFalse(optional.isPresent());
    }

    @Test
    public void whenFindAllEmpty() {
        given(usersDAO.findAll()).willReturn(Collections.emptyList());
        List<UserProjection> usersList = repository.getAll();
        assertEquals(usersList, Collections.emptyList());
    }

    @Test
    public void whenFindAll() {
        List<UserProjection> list = new ArrayList<>();
        list.add(new UserProjection(id, name, age));
        given(usersDAO.findAll()).willReturn(list);

        List<UserProjection> usersList = repository.getAll();
        assertEquals(1, usersList.size());
        UserProjection userProjection = usersList.get(0);
        assertEquals(id, userProjection.getId());
        assertEquals(name, userProjection.getUsername());
        assertEquals(age, userProjection.getAge());
    }

}
