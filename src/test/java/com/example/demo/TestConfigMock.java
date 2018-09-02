package com.example.demo;

import com.example.demo.adapter.respository.UserMongoRepository;
import com.example.demo.adapter.respository.UsersDAO;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

/**
 * 9/2/2018
 *
 * @author Mohammad Ali
 */

@TestConfiguration
public class TestConfigMock {

    @MockBean
    private UsersDAO usersDAO;

    @Bean
    public UserMongoRepository getUserMongoRepository() {
        return new UserMongoRepository(usersDAO);
    }

}
