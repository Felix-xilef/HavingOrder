package com.fatec.havingorder.services;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;

public class ToastService {

    private final Context context;

    public ToastService(Context context) {
        this.context = context;
    }

    public void showErrorFromTask(int messageId, Task task) {
        showErrorFromTask(context.getString(messageId), task);
    }

    public void showErrorFromTask(String message, Task task) {
        showToast(message + (task.getException() != null ? task.getException().getMessage() : ""));
    }

    public void showToast(int messageId) {
        showToast(context.getString(messageId));
    }

    public void showToast(String message) {
        Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }
}
