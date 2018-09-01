package com.example.demo.adapter.respository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * 9/2/2018
 *
 * @author Mohammad Al-Najjar (Mx NINJA)
 */

@Repository
public interface UsersDAO extends MongoRepository<UserProjection, Integer> {


}
