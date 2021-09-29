package com.fatec.havingorder.services;

import com.fatec.havingorder.models.Order;
import com.fatec.havingorder.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class OrderService {
    private final CollectionReference db = FirebaseFirestore.getInstance().collection("orders");

    public Task<QuerySnapshot> getOrders() {
        return db.get();
    }

    public Task<DocumentSnapshot> getOrder(String id) {
        return db.document(id).get();
    }

    public void save(Order order) {
        db.document(order.getId()).set(order.toDBEntry());
    }
}
