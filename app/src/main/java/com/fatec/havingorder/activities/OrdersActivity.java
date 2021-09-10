package com.fatec.havingorder.activities;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.fatec.havingorder.R;

public class OrdersActivity extends ActivityWithActionBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        super.setCustomActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnOrders:
                // It's the same page so do nothing
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goToAddEditOrder(View view) {
        Intent intent = new Intent(this, AddEditOrderActivity.class);
        startActivity(intent);
    }
}