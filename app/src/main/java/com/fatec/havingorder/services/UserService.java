package com.fatec.havingorder.services;

import com.fatec.havingorder.models.User;
import com.fatec.havingorder.models.UserType;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    private final CollectionReference db = FirebaseFirestore.getInstance().collection("users");

    public Task<QuerySnapshot> getUsers() {
        return db.get();
    }

    public Task<QuerySnapshot> getUsers(UserType type) {
        Map<String, Object> typeMap = new HashMap<>();

        typeMap.put("id", type.getId());
        typeMap.put("description", type.getDescription());

        return db.whereEqualTo("type", typeMap).get();
    }

    public Task<DocumentSnapshot> getUser(String email) {
        return db.document(email).get();
    }

    public Task<Void> save(User user) {
        return db.document(user.getEmail()).set(user.toDBEntry());
    }

    public Task<Void> remove(User user) {
        return db.document(user.getEmail()).delete();
    }
}
