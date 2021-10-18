package com.fatec.havingorder.models;

import androidx.annotation.NonNull;

import com.google.protobuf.Any;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class User {

    private String name;

    private String email;

    private String phone;

    private UserType type;

    private String password;

    private String userToken;

    public User() {
    }

    public User(String name, String email, String phone, UserType type) {
        this();
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.type = type;
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

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
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
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", type=" + type +
                '}';
    }

    public Map<String, Object> toDBEntry() {
        Map<String, Object> dbEntry = new HashMap<>();

        dbEntry.put("name", name);
        dbEntry.put("email", email);
        dbEntry.put("phone", phone);
        dbEntry.put("type", type.toDBEntry());
        dbEntry.put("userToken", userToken);

        return dbEntry;
    }
}
