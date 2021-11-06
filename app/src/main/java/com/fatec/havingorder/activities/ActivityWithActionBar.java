package com.fatec.havingorder.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.fatec.havingorder.R;
import com.fatec.havingorder.services.AuthenticationService;

public abstract class ActivityWithActionBar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        if (AuthenticationService.getLoggedUser().getType() == null || AuthenticationService.getLoggedUser().getType().isClient()) {
            getMenuInflater().inflate(R.menu.nav_items_client, menu);

        } else {
            getMenuInflater().inflate(R.menu.nav_items_dev, menu);
        }

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
                (new AuthenticationService()).signOut();
                Intent intentLogout = new Intent(this, SignInActivity.class);
                startActivity(intentLogout);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void setCustomActionBar() {
        Toolbar appToolBar = findViewById(R.id.appToolbar);
        setSupportActionBar(appToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    protected void setActionBarTitle(int textId) {
        getSupportActionBar().setTitle(textId);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }
}
