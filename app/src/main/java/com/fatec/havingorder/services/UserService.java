package com.fatec.havingorder.services;

import com.fatec.havingorder.models.User;
import com.fatec.havingorder.models.UserType;

import java.util.Arrays;
import java.util.List;

public class UserService {
    private List<User> users = Arrays.asList(
            new User(1, "Marquinhos Dev", "marq@server.com", "11965654848", new UserType(1)),
            new User(2, "Marquinhos Cliente", "marqC@server.com", "11915754948", new UserType(2)),
            new User(3, "Dev test", "dev@server.com", "11966332015", new UserType(1))
    );

    public List<User> getUsers() {
        return users;
    }

    public User getUser(int id) {
        for (User user : users) {
            if (user.getId() == id) return user;
        }

        return null;
    }

    public void saveUser(User user) {
        for (User userInArray : users) {
            if (userInArray.equals(user)) {
                userInArray = user;
                return;
            }
        }

        users.add(user);
    }
}
