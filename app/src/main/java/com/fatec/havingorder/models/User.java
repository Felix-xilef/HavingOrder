package com.fatec.havingorder.models;

import androidx.annotation.NonNull;

import java.util.Objects;

public class User {

    private int id;

    private String name;

    private String email;

    private String phone;

    private UserType type;

    private String password;

    public User() {
    }

    public User(int id, String name, String email, String phone, UserType type) {
        this();
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.type = type;
    }

    public User(int id, String name, String email, String phone, UserType type, String password) {
        this(id, name, email, phone, type);
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isValid() {
        return name != null && !name.isEmpty() &&
                email != null && !email.isEmpty() &&
                phone != null && !phone.isEmpty() &&
                type.getClass() == UserType.class && type.isValid();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", type=" + type +
                '}';
    }
}
