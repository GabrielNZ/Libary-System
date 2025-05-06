package com.example.demo.entity;

import com.example.demo.entity.enumtype.Role;

public record RegisterRecord(String login, String email, String phone, String password, Role role) {
}
