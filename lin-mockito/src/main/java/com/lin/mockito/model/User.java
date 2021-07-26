package com.lin.mockito.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class User {

    private int id;

    private String name;

    private int age;

    private int gather;

}
