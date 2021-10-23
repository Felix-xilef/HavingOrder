package com.fatec.havingorder.activities;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fatec.havingorder.R;
import com.fatec.havingorder.models.Order;
import com.fatec.havingorder.others.DateTextFormatter;
import com.fatec.havingorder.services.OrderService;
import com.fatec.havingorder.services.ToastService;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends ActivityWithActionBar {

    private final ToastService toastService = new ToastService(OrdersActivity.this);

    private final OrderService orderService = new OrderService();

    private List<Order> orders = new ArrayList<>();

    private LinearLayout orderEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        super.setCustomActionBar();

        orderEntries = findViewById(R.id.orderEntries);

        getOrders();

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
                if (!editable.toString().isEmpty()) filterOrders(editable.toString());
                else inflateOrders(orders);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btnOrders) {
            // It's the same page so do nothing
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void filterOrders(String filter) {
        filter = filter.toLowerCase();
        List<Order> filteredOrders = new ArrayList<>();

        for (Order order : orders) {
            if (
                order.getDescription().toLowerCase().contains(filter) ||
                String.valueOf(order.getPrice()).contains(filter) ||
                DateTextFormatter.dateToString(order.getStartDate()).contains(filter)
            )
                filteredOrders.add(order);
        }

        inflateOrders(filteredOrders);
    }

    public void getOrders() {
        orderService.getOrders().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                orders = task.getResult().toObjects(Order.class);

                inflateOrders(orders);

            } else toastService.showErrorFromTask(R.string.getOrderError, task);
        });
    }

    public void inflateOrders(List<Order> orders) {
        orderEntries.removeAllViews();

        for (Order order : orders) {
            View orderEntry = getLayoutInflater().inflate(R.layout.order_entry, orderEntries, false);

            orderEntry.findViewById(R.id.orderEntry).setContentDescription(order.getId());

            ((TextView) orderEntry.findViewById(R.id.lblOrderDescription)).setText(order.getDescription());

            ((TextView) orderEntry.findViewById(R.id.lblOrderStartDate)).setText(DateTextFormatter.dateToString(order.getStartDate()));

            ((TextView) orderEntry.findViewById(R.id.lblOrderPrice)).setText(String.valueOf(order.getPrice()));

            ((TextView) orderEntry.findViewById(R.id.lblOrderStatus)).setText(order.getStatus().getDescription());
            ((TextView) orderEntry.findViewById(R.id.lblOrderStatus)).setTextColor(getColor(
                    order.getStatus().isOpen() ? R.color.light_blue : (
                        order.getStatus().isClosed() ? R.color.light_green : R.color.light_red
                    )
            ));

            orderEntries.addView(orderEntry);
        }
    }

    public void goToAddEditOrderWithContent(View view) {
        Intent intent = new Intent(this, AddEditOrderActivity.class);
        intent.putExtra("orderId", view.getContentDescription());

        startActivity(intent);
    }

    public void goToAddEditOrder(View view) {
        Intent intent = new Intent(this, AddEditOrderActivity.class);
        startActivity(intent);
    }
}