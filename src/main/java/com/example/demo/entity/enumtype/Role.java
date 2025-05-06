package com.example.demo.entity.enumtype;

public enum Role {
    CLIENT("client"), EMPLOYEE("employee"), ADMIN("admin");

    public String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
