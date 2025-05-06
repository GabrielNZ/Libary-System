package com.example.demo.controller;

import com.example.demo.entity.AuthenticationDTO;
import com.example.demo.entity.RegisterRecord;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.TokenGenerator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    public AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenGenerator tokenGenerator;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(data.login(), data.password()));
        tokenGenerator.generateToken((User)auth.getPrincipal());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterRecord data) {
        if(userRepository.findByName(data.login()) != null) return ResponseEntity.ok().build();
        User newUser = new User(data.login(),data.email(),data.phone(),new BCryptPasswordEncoder().encode(data.login()),data.role());
        userRepository.save(newUser);
        return ResponseEntity.ok().build();
    }
}
