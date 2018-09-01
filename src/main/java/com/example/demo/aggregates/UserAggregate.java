package com.example.demo.aggregates;

import lombok.*;

/**
 * 9/2/2018
 *
 * @author Mohammad Al-Najjar (Mx NINJA)
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAggregate {

    private Integer id;
    private String username;
    private int age;

}
