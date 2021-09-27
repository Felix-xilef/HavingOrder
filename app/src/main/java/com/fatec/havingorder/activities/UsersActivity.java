package com.fatec.havingorder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
       userService.getUsers().addOnCompleteListener(task -> {
           if (task.isSuccessful() && task.getResult() != null) {
               users = task.getResult().toObjects(User.class);

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

           } else {
               Toast.makeText(
                       UsersActivity.this,
                       getText(R.string.getUserError) + (task.getException() != null ? task.getException().toString() : ""),
                       Toast.LENGTH_SHORT
               ).show();
           }
       });
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