package com.fatec.havingorder.services;

import com.fatec.havingorder.models.User;
import com.fatec.havingorder.models.UserType;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

public class UserService {
    private final CollectionReference db = FirebaseFirestore.getInstance().collection("users");

    public Task<QuerySnapshot> getUsers() {
        return db.get();
    }

    public Task<DocumentSnapshot> getUser(String email) {
        return db.document(email).get();
    }

    public void save(User user) {
        db.document(user.getEmail()).set(user.toDBEntry());
    }
}
