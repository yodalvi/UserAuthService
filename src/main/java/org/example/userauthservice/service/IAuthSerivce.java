package org.example.userauthservice.service;

import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthservice.models.User;

public interface IAuthSerivce {

     User signUp(String name, String email, String password, String phoneNumber);

     Pair<User,String> login(String email, String password);

     boolean validateToken(String token);

//     String logout(String token);
}
