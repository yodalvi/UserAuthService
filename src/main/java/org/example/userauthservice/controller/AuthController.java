package org.example.userauthservice.controller;

import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthservice.dtos.LoginRequestDto;
import org.example.userauthservice.dtos.SignUpRequestDto;
import org.example.userauthservice.dtos.UserDto;
import org.example.userauthservice.dtos.ValidateTokenRequestDto;
import org.example.userauthservice.models.Role;
import org.example.userauthservice.models.User;
import org.example.userauthservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto){
        User user = authService.signUp(signUpRequestDto.getName(),signUpRequestDto.getEmail()
                ,signUpRequestDto.getPassword(),signUpRequestDto.getPhoneNumber());

        UserDto userDto = from(user);

        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto){
        Pair<User, String> response = authService.login(loginRequestDto.getEmail(),loginRequestDto.getPassword());
        User u = response.a;
        String token = response.b;
        UserDto userDto = from(u);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE, token);
        return new ResponseEntity<>(userDto, headers, HttpStatus.OK);

    }

    @PostMapping("/validateToken")
    public boolean validateToken(@RequestBody ValidateTokenRequestDto validateTokenRequestDto){

        return  authService.validateToken(validateTokenRequestDto.getToken());
    }

//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(@RequestBody ValidateTokenRequestDto validateTokenRequestDtouserDto){
//          authService.logout(validateTokenRequestDtouserDto.getToken());
//          return ResponseEntity.ok("Logged Out Successfully");
//    }

    public UserDto from(User user){
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmailId());
        userDto.setId(user.getId());
        List<String> rolesString = new ArrayList<>();
        for(Role role : user.getRoles()){
            rolesString.add(role.getValue());
        }
        userDto.setRoles(rolesString);
        return  userDto;
    }

}
