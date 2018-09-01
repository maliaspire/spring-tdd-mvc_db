package com.example.demo.adapter.rest;

import com.example.demo.adapter.respository.UserMongoRepository;
import com.example.demo.adapter.respository.UserProjection;
import com.example.demo.aggregates.UserAggregate;
import com.example.demo.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * 9/1/2018
 *
 * @author Mohammad Al-Najjar (Mx NINJA)
 */

@RestController
@RequestMapping("users")
public class UsersRest {

    private UserMongoRepository userMongoRepository;

    @Autowired
    public UsersRest(UserMongoRepository userMongoRepository) {
        this.userMongoRepository = userMongoRepository;
    }

    @GetMapping("/id/{id}")
    public UserProjection getById(@PathVariable("id") Integer id) {
        Optional<UserProjection> optional = userMongoRepository.getUserById(id);
        if (!optional.isPresent()) {
            throw new UserNotFoundException();
        }
        return optional.get();
    }

    @GetMapping
    public List<UserProjection> getAll() {
        return userMongoRepository.getAll();
    }

    @PostMapping
    public UserProjection save(@RequestBody UserAggregate aggregate) {
        return userMongoRepository.save(aggregate);
    }

}
