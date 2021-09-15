package com.fatec.havingorder.activities;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.fatec.havingorder.R;
import com.fatec.havingorder.models.User;
import com.fatec.havingorder.services.UserService;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends ActivityWithActionBar {

    private final UserService userService = new UserService();

    private List<User> users = new ArrayList<>();

    private LinearLayout userEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        super.setCustomActionBar();

        userEntries = (LinearLayout) findViewById(R.id.userEntries);

        getUsers();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btnUsers) {
            // It's the same page so do nothing
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getUsers() {
       users = userService.getUsers();

       for (User user : users) {
           View userEntry = LayoutInflater.from(this).inflate(R.layout.user_entry, null);
           userEntry.setId(user.getId());
           userEntry.setOnClickListener(this::goToAddEditUser);

           userEntries.addView(userEntry);
           System.out.println("\n|\n|\t" + userEntry.getId() + "\n|\n");
       }
    }

    public void goToAddEditUserWithContent(View view) {
        System.out.println("\n|\n|\t" + view.getId() + "\n|\n");
        Intent intent = new Intent(this, AddEditUserActivity.class);
        intent.putExtra("userId", view.getId());

        startActivity(intent);
    }

    public void goToAddEditUser(View view) {
        Intent intent = new Intent(this, AddEditUserActivity.class);

        startActivity(intent);
    }
}