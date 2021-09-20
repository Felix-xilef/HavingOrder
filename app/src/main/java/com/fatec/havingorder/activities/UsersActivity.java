package com.fatec.havingorder.activities;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
           View userEntry = getLayoutInflater().inflate(R.layout.user_entry, userEntries, false);

           userEntry.findViewById(R.id.userEntry).setId(user.getId());

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