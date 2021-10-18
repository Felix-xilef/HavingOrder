package com.fatec.havingorder.services;

import com.fatec.havingorder.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthenticationService {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private static User loggedUser = new User();

    public static User getLoggedUser() {
        return loggedUser;
    }

    public static Task<DocumentSnapshot> setLoggedUser(String email) {
        return FirebaseFirestore.getInstance().collection("users").document(email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                loggedUser = task.getResult().toObject(User.class);
            }
        });
    }

    public Task<AuthResult> signIn(String email, String password) {
        return auth.signInWithEmailAndPassword(email, password);
    }

    public void signOut() {
        auth.signOut();
    }

    public Task<AuthResult> createUser(String email, String password) {
        return auth.createUserWithEmailAndPassword(email, password);
    }

    public void removeUser(String userTokenId) {

    }
}
