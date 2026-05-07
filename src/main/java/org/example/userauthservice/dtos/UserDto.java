package org.example.userauthservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.userauthservice.models.Role;

import java.util.List;

@Getter
@Setter
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private List<String> roles;

}
