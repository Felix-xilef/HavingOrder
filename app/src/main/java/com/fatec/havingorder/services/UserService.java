package com.fatec.havingorder.services;

import com.fatec.havingorder.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class UserService {

    private final CollectionReference db = FirebaseFirestore.getInstance().collection("users");

    public static User loggedUser = new User();

    public Task<DocumentSnapshot> setLoggedUser(String email) {
        return db.document(email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                loggedUser = task.getResult().toObject(User.class);
            }
        });
    }

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
