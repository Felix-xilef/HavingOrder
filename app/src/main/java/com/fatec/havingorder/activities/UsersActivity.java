package com.fatec.havingorder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fatec.havingorder.R;
import com.fatec.havingorder.models.User;
import com.fatec.havingorder.services.ToastService;
import com.fatec.havingorder.services.UserService;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends ActivityWithActionBar {

    private final ToastService toastService = new ToastService(UsersActivity.this);

    private final UserService userService = new UserService();

    private List<User> users = new ArrayList<>();

    private LinearLayout userEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        super.setCustomActionBar();

        userEntries = findViewById(R.id.userEntries);

        getUsers();

        // Setting the filter onChange listener
        ((TextInputEditText) findViewById(R.id.txtFilterContent)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) filterUsers(editable.toString());
                else inflateUsers(users);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btnUsers) {
            // It's the same page so do nothing
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void reload(View view) {
        getUsers();
    }

    public void filterUsers(String filter) {
        filter = filter.toLowerCase();
        List<User> filteredUsers = new ArrayList<>();

        for (User user : users) {
            if (user.getEmail().toLowerCase().contains(filter) || user.getName().toLowerCase().contains(filter))
                filteredUsers.add(user);
        }

        inflateUsers(filteredUsers);
    }

    public void getUsers() {
       userService.getUsers().addOnCompleteListener(task -> {
           if (task.isSuccessful() && task.getResult() != null) {
               users = task.getResult().toObjects(User.class);

               inflateUsers(users);

           } else toastService.showErrorFromTask(R.string.getUserError, task);
       });
    }

    public void inflateUsers(List<User> users) {
        userEntries.removeAllViews();

        for (User user : users) {
            View userEntry = getLayoutInflater().inflate(R.layout.user_entry, userEntries, false);

            userEntry.findViewById(R.id.userEntry).setContentDescription(user.getEmail());

            ((TextView) userEntry.findViewById(R.id.lblUserName)).setText(user.getName());

            ((TextView) userEntry.findViewById(R.id.lblUserEmail)).setText(user.getEmail());

            ((TextView) userEntry.findViewById(R.id.lblUserType)).setText(user.getType().getDescription());
            ((TextView) userEntry.findViewById(R.id.lblUserType)).setTextColor(
                    user.getType().isClient() ? getColor(R.color.orange) : getColor(R.color.light_green)
            );

            userEntries.addView(userEntry);
        }
    }

    public void goToAddEditUserWithContent(View view) {
        Intent intent = new Intent(this, AddEditUserActivity.class);
        intent.putExtra("userEmail", view.getContentDescription());

        startActivity(intent);
    }

    public void goToAddEditUser(View view) {
        Intent intent = new Intent(this, AddEditUserActivity.class);

        startActivity(intent);
    }
}