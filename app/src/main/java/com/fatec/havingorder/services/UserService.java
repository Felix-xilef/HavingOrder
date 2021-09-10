package com.fatec.havingorder.services;

import com.fatec.havingorder.models.User;
import com.fatec.havingorder.models.UserType;

public class UserService {
    private final User[] users = {
            new User(0, "Marquinhos Dev", "marq@server.com", "11965654848", new UserType(1)),
            new User(1, "Marquinhos Cliente", "marqC@server.com", "11915754948", new UserType(2)),
            new User(2, "Dev test", "dev@server.com", "11966332015", new UserType(1))
    };

    public User getUser(int id) {
        return users[id];
    }
}
