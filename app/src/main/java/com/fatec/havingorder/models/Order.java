package com.fatec.havingorder.models;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Order {

    private String id;

    private String description;

    private Calendar startDate;

    private Calendar endDate;

    private float price;

    private OrderStatus status;

    private User client;

    private String[] comments;

    public Order() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public String[] getComments() {
        return comments;
    }

    public void setComment(String[] comments) {
        this.comments = comments;
    }

    public boolean isValid() {
        return id != null && !id.isEmpty() &&
                description != null && !description.isEmpty() &&
                startDate != null &&
                endDate != null &&
                status.getClass() == OrderStatus.class && status.isValid() &&
                client.getClass() == User.class && client.isValid() &&
                comments != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", price=" + price +
                ", status=" + status +
                ", client=" + client +
                ", comments=" + Arrays.toString(comments) +
                '}';
    }

    public Map<String, Object> toDBEntry() {
        Map<String, Object> dbEntry = new HashMap<>();

        dbEntry.put("id", id);
        dbEntry.put("description", description);
        dbEntry.put("startDate", startDate);
        dbEntry.put("endDate", endDate);
        dbEntry.put("price", price);
        dbEntry.put("status", status.toDBEntry());
        dbEntry.put("client", client);
        dbEntry.put("comment", comments);

        return dbEntry;
    }
}
