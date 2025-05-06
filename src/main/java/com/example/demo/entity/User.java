package com.example.demo.entity;

import com.example.demo.entity.enumtype.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tb_user")
public class User extends RepresentationModel<User> implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private Double fines;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Loan> loan;

    public User(){

    }

    public User (String login, String email, String phone, String password, Role role){
        this.role = role;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.fines = 0.0;
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == Role.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_EMPLOYEE"), new SimpleGrantedAuthority("ROLE_CLIENT"));
        }else if(this.role == Role.EMPLOYEE){
            return List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE"), new SimpleGrantedAuthority("ROLE_CLIENT"));
        }else {
            return List.of(new SimpleGrantedAuthority("ROLE_CLIENT"));
        }
    }
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return "";
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Double getFines() {
        return fines;
    }

    public void setFines(Double fines) {
        this.fines = fines;
    }

    @JsonIgnore
    public List<Loan> getLoan() {
        return loan;
    }

    public void setLoan(List<Loan> loan) {
        this.loan = loan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
