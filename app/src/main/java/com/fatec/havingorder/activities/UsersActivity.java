package com.fatec.havingorder.activities;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.fatec.havingorder.R;

public class UsersActivity extends ActivityWithActionBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        super.setCustomActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnUsers:
                // It's the same page so do nothing
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goToAddEditUserWithContent(View view) {
        Intent intent = new Intent(this, AddEditUserActivity.class);
        intent.putExtra("userId", Integer.parseInt(view.getContentDescription().toString()));

        startActivity(intent);
    }

    public void goToAddEditUser(View view) {
        Intent intent = new Intent(this, AddEditUserActivity.class);

        startActivity(intent);
    }
}