package com.example.demo.adapter.respository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

/**
 * 9/2/2018
 *
 * @author Mohammad Al-Najjar (Mx NINJA)
 */

@RunWith(MockitoJUnitRunner.class)
public class UserMongoRepositoryTest {

    @Mock
    private UsersDAO usersDAO;

    private UserMongoRepository repository;

    @Before
    public void setup() {
        repository = new UserMongoRepository(usersDAO);
    }

    @Test
    public void whenFindById() {
        given(usersDAO.findById(1)).willReturn(Optional.of(new UserProjection(1, "test user 1", 28)));

        Optional<UserProjection> optional = repository.getUserById(1);

        assertTrue(optional.isPresent());
        assertEquals(1, optional.get().getId());
        assertEquals("test user 1", optional.get().getUsername());
        assertEquals(28, optional.get().getAge());
    }


    @Test
    public void whenUserNotFound() {
        given(usersDAO.findById(1)).willReturn(Optional.empty());

        Optional<UserProjection> optional = repository.getUserById(1);

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
        list.add(new UserProjection(1, "test user 1", 16));
        list.add(new UserProjection(2, "test user 2", 20));
        given(usersDAO.findAll()).willReturn(list);

        List<UserProjection> usersList = repository.getAll();
        assertEquals(2, usersList.size());
        UserProjection userProjection = usersList.get(0);
        assertEquals(1, userProjection.getId());
        assertEquals("test user 1", userProjection.getUsername());
        assertEquals(16, userProjection.getAge());
        System.out.println(list);
    }

}
