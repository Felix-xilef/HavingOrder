package com.fatec.havingorder.models;

import com.fatec.havingorder.services.AuthenticationService;

import java.util.Date;
import java.util.Objects;

public class Comment {

    private String id;

    private Date date;

    private String description;

    public Comment() {
        id = AuthenticationService.getLoggedUser().hashCode() + String.valueOf(System.currentTimeMillis());
        date = new Date();
    }

    public Comment(String description) {
        this();
        this.description = description;
    }

    public Comment(String id, Date date, String description) {
        this.id = id;
        this.date = date;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", description='" + description + '\'' +
                '}';
    }
}
