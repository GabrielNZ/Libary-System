package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.entity.enumtype.Role;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE') or hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        List<User> list = userService.findAll();
        if (!list.isEmpty()) {
            for (User user : list) {
                user.add(linkTo(methodOn(UserController.class).findById(user.getId())).withSelfRel());
            }
        }
        return ResponseEntity.ok().body(list);
    }

    @PreAuthorize("#id == authentication.principal.id or hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_EMPLOYEE')")  //para o cliente so poder ver seu proprio id, ou entao o adm ver tudo.
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        User user = userService.findById(id);
        user.add(linkTo(methodOn(UserController.class).findAll()).withSelfRel());
        return ResponseEntity.ok().body(user);
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE') or hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE') or hasAuthority('ROLE_ADMIN')")
    //Caso seja necessario atualizar um cadastro
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok().body(userService.update(id, user));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}/role")
    public ResponseEntity<User> updateUserRole(@PathVariable Long id, @RequestBody Role role) {
        return ResponseEntity.ok().body(userService.updateRole(id, role));
    }
}
