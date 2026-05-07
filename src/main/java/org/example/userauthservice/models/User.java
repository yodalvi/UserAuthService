package org.example.userauthservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name="Users")
public class User extends BaseModel{

    private String name;
    private String emailId;
    private String password;
    private String phoneNumber;
    @ManyToMany
    private List<Role> roles = new ArrayList<>();
}
