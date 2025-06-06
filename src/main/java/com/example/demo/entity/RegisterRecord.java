package com.example.demo.entity;

import com.example.demo.entity.enumtype.Role;

public record RegisterRecord(String name, String email, String phone, String password, Role role) {
}
