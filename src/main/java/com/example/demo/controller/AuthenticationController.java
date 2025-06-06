package com.example.demo.controller;

import com.example.demo.entity.AuthenticationDTO;
import com.example.demo.entity.Email;
import com.example.demo.entity.RegisterRecord;
import com.example.demo.entity.User;
import com.example.demo.messaging.EmailProducer;
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
    @Autowired
    EmailProducer emailProducer;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(data.name(), data.password()));
        String token = tokenGenerator.generateToken((User)auth.getPrincipal());
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterRecord data) {
        Email email = new Email();
        if(userRepository.findByName(data.name()) != null) {
            email.setEmail(userRepository.findByName(data.name()).getEmail());
            email.setSubject("Your account is at risk!");
            email.setText("Someone tried to register an account with this email... ");
            emailProducer.sendEmail(email);
            return ResponseEntity.ok().build();
        }
        User newUser = new User(data.name(),data.email(),data.phone(),new BCryptPasswordEncoder().encode(data.password()), data.role());
        userRepository.save(newUser);
        email.setEmail(newUser.getEmail());
        email.setSubject("Your registration is complete!");
        email.setText("Thanks for your registration!, enjoy all the benefits of our libary!");
        emailProducer.sendEmail(email);
        return ResponseEntity.ok().build();
    }
}
