package com.fatec.havingorder.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserType {

    private int id;

    private String description;

    public UserType() {
    }

    public UserType(int id) {
        this();

        this.id = id;

        if (id == 1) this.description = "Desenvolvedor";
        else this.description = "Cliente";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isValid() {
        return id == 1 || id == 2;
    }

    public boolean isClient() {
        return id != 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserType userType = (UserType) o;
        return id == userType.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserType{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }

    public Map<String, Object> toDBEntry() {
        Map<String, Object> dbEntry = new HashMap<>();

        dbEntry.put("id", id);
        dbEntry.put("description", description);

        return dbEntry;
    }
}
