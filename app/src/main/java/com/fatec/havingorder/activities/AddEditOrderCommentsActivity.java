package com.fatec.havingorder.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.fatec.havingorder.R;
import com.fatec.havingorder.models.Comment;
import com.fatec.havingorder.models.Order;
import com.fatec.havingorder.others.DateTextFormatter;
import com.fatec.havingorder.services.OrderService;
import com.fatec.havingorder.services.ToastService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class AddEditOrderCommentsActivity extends ActivityWithActionBar {

    private final OrderService orderService = new OrderService();

    private Order order = new Order();

    private final ToastService toastService = new ToastService(AddEditOrderCommentsActivity.this);

    private LinearLayout commentList;

    private TextInputEditText txtCommentContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_order_comments);

        super.setCustomActionBar();

        super.setActionBarTitle(R.string.lblComment);

        commentList = findViewById(R.id.commentList);
        txtCommentContent = findViewById(R.id.txtCommentContent);

        Intent intent = getIntent();
        String orderId = intent.getStringExtra("orderId");

        if (orderId == null || orderId.isEmpty()) {
            toastService.showToast(R.string.getOrderError);
            finish();

        } else {
            orderService.getOrder(orderId).addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                    order = task.getResult().toObject(Order.class);

                    if (order.getComments() == null) order.setComments(new ArrayList<>());

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

        for (Comment comment : order.getComments()) {
            inflateComment(comment);
        }
    }

    public void addComment(View view) {
        if (txtCommentContent.getText() != null && txtCommentContent.getText().length() > 0) {
            Comment newComment = new Comment(txtCommentContent.getText().toString());

            inflateComment(newComment);

            order.getComments().add(newComment);

        } else toastService.showToast(R.string.emptyFields);
    }

    public void removeComment(View view) {
        for (Comment comment : order.getComments()) {
            if (comment.getId().equals(((View) view.getParent()).getContentDescription().toString())) {
                order.getComments().remove(comment);
                break;
            }
        }

        System.out.println("\n|\n|\tcomments -> " + order.getComments() + "\n|");

        commentList.removeView((View) view.getParent().getParent());
    }

    public void cancel(View view) {
        finish();
    }

    public void save(View view) {
        orderService.save(order).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                toastService.showToast(R.string.saveOrderSuccess);
                finish();

            } else toastService.showErrorFromTask(R.string.saveOrderError, task);
        });
    }

    private void inflateComment(Comment comment) {
        View commentEntry = getLayoutInflater().inflate(R.layout.order_comment_entry, commentList, false);

        commentEntry.findViewById(R.id.commentEntry).setContentDescription(comment.getId());

        ((TextView) commentEntry.findViewById(R.id.txtDate)).setText(
                getString(R.string.date).concat(DateTextFormatter.dateToString(comment.getDate()))
        );

        ((TextView) commentEntry.findViewById(R.id.txtComment)).setText(comment.getDescription());

        commentList.addView(commentEntry);
    }
}