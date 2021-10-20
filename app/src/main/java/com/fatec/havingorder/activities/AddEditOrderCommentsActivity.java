package com.fatec.havingorder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.fatec.havingorder.R;
import com.fatec.havingorder.models.Order;
import com.fatec.havingorder.services.OrderService;
import com.fatec.havingorder.services.ToastService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddEditOrderCommentsActivity extends ActivityWithActionBar {

    private final OrderService orderService = new OrderService();

    private Order order = new Order();

    private final ToastService toastService = new ToastService(AddEditOrderCommentsActivity.this);

    private RecyclerView commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_order_comments);

        super.setCustomActionBar();

        commentList = findViewById(R.id.commentList);

        Intent intent = getIntent();
        String orderId = intent.getStringExtra("orderId");

        if (orderId == null || orderId.isEmpty()) {
            toastService.showToast(R.string.getOrderError);
            finish();

        } else {
            orderService.getOrder(orderId).addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                    order = task.getResult().toObject(Order.class);
                    loadComments(order);

                } else {
                    toastService.showErrorFromTask(R.string.getOrderError, task);
                    finish();
                }
            });
        }
    }

    public void loadComments(Order order) {
        commentList.removeAllViews();

        for (String comment : order.getComments()) {
            View commentEntry = getLayoutInflater().inflate(R.layout.order_comment_entry, commentList, false);

            ((TextInputEditText) commentEntry.findViewById(R.id.txtCommentContent)).setText(comment);

            commentList.addView(commentEntry);
        }
    }

    public void addComment(View view) {

    }

    public void cancel(View view) {
        finish();
    }

    public void save(View view) {

    }
}