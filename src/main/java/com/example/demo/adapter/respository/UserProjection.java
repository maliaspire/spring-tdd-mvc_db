package com.example.demo.adapter.respository;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.validation.constraints.NotNull;

/**
 * 9/1/2018
 *
 * @author Mohammad Al-Najjar (Mx NINJA)
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "Users")
public class UserProjection {

    @Id
    @GeneratedValue
    private String id;

    @NotNull
    private String username;

    @NotNull
    private int age;


}
