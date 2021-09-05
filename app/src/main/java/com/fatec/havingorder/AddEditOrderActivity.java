package com.fatec.havingorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AddEditOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_order);

        Toolbar appToolBar = (Toolbar) findViewById(R.id.appToolbar);
        setSupportActionBar(appToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.nav_items, menu);

        return true;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getSupportActionBar().openOptionsMenu();
                return true;

            case R.id.btnOrders:
                Intent intentOrders = new Intent(this, OrdersActivity.class);
                startActivity(intentOrders);
                return true;

            case R.id.btnUsers:
                Intent intentUsers = new Intent(this, UsersActivity.class);
                startActivity(intentUsers);
                return true;

            case R.id.btnLogout:
                Intent intentLogout = new Intent(this, SignInActivity.class);
                startActivity(intentLogout);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}