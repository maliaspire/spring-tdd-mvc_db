package com.example.demo.adapter.respository;

import com.example.demo.aggregates.UserAggregate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * 9/1/2018
 *
 * @author Mohammad Al-Najjar (Mx NINJA)
 */


@Component
public class UserMongoRepository {

    private UsersDAO usersDAO;

    @Autowired
    public UserMongoRepository(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    public Optional<UserProjection> getUserById(Integer id) {
        return usersDAO.findById(id);
    }

    public List<UserProjection> getAll() {
        return usersDAO.findAll();
    }

    public UserProjection save(UserAggregate aggregate) {
        return usersDAO.save(convertToProjection(aggregate));
    }

    private UserProjection convertToProjection(UserAggregate aggregate) {
        return new UserProjection(aggregate.getId(), aggregate.getUsername(), aggregate.getAge());
    }
}
