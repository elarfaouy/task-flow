package com.youcode.taskflow.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserDto implements Serializable {
    private Long id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;
    private Integer jetons;
    private RoleDto role;
}
