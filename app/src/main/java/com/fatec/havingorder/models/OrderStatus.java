package com.fatec.havingorder.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OrderStatus {

    private int id;

    private String description;

    public OrderStatus() {
    }

    public OrderStatus(int id) {
        this();

        this.id = id;

        if (id == 1) description = "Aberto";
        else if (id == 2) description = "Finalizado";
        else description = "Cancelado";
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
        return id == 1 || id == 2 || id == 3;
    }

    public boolean isOpen() {
        return id == 1;
    }

    public boolean isClosed() {
        return id == 2;
    }

    public boolean isCalledOff() {
        return id == 3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderStatus that = (OrderStatus) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Map<String, Object> toDBEntry() {
        Map<String, Object> dbEntry = new HashMap<>();

        dbEntry.put("id", id);
        dbEntry.put("description", description);

        return dbEntry;
    }
}
