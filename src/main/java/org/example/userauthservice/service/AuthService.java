package org.example.userauthservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthservice.models.Role;
import org.example.userauthservice.models.Session;
import org.example.userauthservice.models.State;
import org.example.userauthservice.models.User;
import org.example.userauthservice.repo.RoleRepository;
import org.example.userauthservice.repo.SessionRepository;
import org.example.userauthservice.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.sound.midi.Soundbank;
import java.util.*;

@Service
public class AuthService implements  IAuthSerivce{
    @Autowired
    private final SecretKey secretKey;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public User signUp(String name, String email, String password, String phoneNumber) {

        Optional<User> userOptional = userRepository.findByEmailId(email);

        if(userOptional.isPresent()){
            throw new RuntimeException("User with email already exists");
        }

        User user = new User();
        user.setName(name);
        user.setCreatedAt(new Date());
        user.setEmailId(email);
        user.setState(State.ACTIVE);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setPhoneNumber(phoneNumber);

        Role role = null;
        Optional<Role> roleOptional = roleRepository.findRoleByValue("NON_ADMIN");
        if(roleOptional.isEmpty()){
            role = new Role();
            role.setValue("NON_ADMIN");
            role.setState(State.ACTIVE);
            role.setCreatedAt(new Date());
            roleRepository.save(role);
        }else{
            role = roleOptional.get();
        }

        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);

        return  userRepository.save(user);
    }


    @Override
    public Pair<User, String> login(String email, String password) {

        //get user
        Optional<User> userOptional = userRepository.findByEmailId(email);
        if(userOptional.isEmpty()){
            throw   new RuntimeException("User with email id not found");
        }

        User user = userOptional.get();
        if( !bCryptPasswordEncoder.matches(password,user.getPassword())){
            throw   new RuntimeException("Incorrect Password");
        }

        //Generate Token
        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", user.getId());
        List<String> rolesString = new ArrayList<>();
        for(Role role : user.getRoles()){
            rolesString.add(role.getValue());
        }
        payload.put("permissions", rolesString);

        Long currentTimeMillis = System.currentTimeMillis();

        payload.put("iat", currentTimeMillis);
        payload.put("exp", currentTimeMillis+100000);
        payload.put("issued_by","siteOwner");

        String token = Jwts.builder().claims(payload).signWith(secretKey).compact();

        Session session = new Session();
        session.setCreatedAt(new Date());
        session.setToken(token);
        session.setState(State.ACTIVE);
        session.setUser(user);
        sessionRepository.save(session);

        return new Pair<>(user,token);

    }

    @Override
    public boolean validateToken(String token) {

        Optional<Session> sessionOptional = sessionRepository.findByToken(token);
        if(sessionOptional.isEmpty()){
            return false;
        }

        JwtParser jwtParser =  Jwts.parser().verifyWith(secretKey).build();
        Claims claims = jwtParser.parseSignedClaims(token).getPayload();

        Long expiry = (Long) claims.get("exp");
        Long currentTime = System.currentTimeMillis();
        System.out.println("Expiry: " + expiry);
        System.out.println("CurrentTime: " + currentTime);
        if(currentTime > expiry){
            Session session = sessionOptional.get();
            session.setState(State.INACTIVE);
            sessionRepository.save(session);
            return false;
        }
        return  true;
    }

//    @Override
//    public String logout(String token) {
//        return "";
//    }
}
