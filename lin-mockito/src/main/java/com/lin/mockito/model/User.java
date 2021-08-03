package com.lin.mockito.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class User {

    private long userId;

    private String username;

    private int isLock;

    private String city;

}
