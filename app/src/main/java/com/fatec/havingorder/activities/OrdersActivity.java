package com.fatec.havingorder.activities;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fatec.havingorder.R;
import com.fatec.havingorder.models.OrderStatus;
import com.fatec.havingorder.models.User;
import com.fatec.havingorder.models.UserType;
import com.fatec.havingorder.services.AuthenticationService;
import com.fatec.havingorder.services.SpinnerService;
import com.fatec.havingorder.models.Order;
import com.fatec.havingorder.Utils.DateTextFormatter;
import com.fatec.havingorder.services.OrderService;
import com.fatec.havingorder.services.ToastService;
import com.fatec.havingorder.services.UserService;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OrdersActivity extends ActivityWithActionBar implements AdapterView.OnItemSelectedListener {

    private final ToastService toastService = new ToastService(OrdersActivity.this);
    private final SpinnerService spinnerService = new SpinnerService();
    private final OrderService orderService = new OrderService();

    private List<Order> orders = new ArrayList<>();

    private LinearLayout orderEntries;

    private TextInputEditText txtFilterContent;

    private Spinner statusSpinner;
    private Spinner clientSpinner;

    private final List<String> statusItems = Arrays.asList(
            "Todos",
            OrderStatus.OPEN_DESCRIPTION,
            OrderStatus.CLOSED_DESCRIPTION,
            OrderStatus.CANCELED_DESCRIPTION
    );

    private List<String> clientNames = new ArrayList<>();

    private List<User> clients = new ArrayList<>();

    private String statusFilter = "Todos";
    private String textFilter = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        super.setCustomActionBar();

        orderEntries = findViewById(R.id.orderEntries);
        txtFilterContent = findViewById(R.id.txtFilterContent);
        statusSpinner = findViewById(R.id.statusSpinner);
        clientSpinner = findViewById(R.id.clientsSpinner);

        spinnerService.setSpinnerUp(statusSpinner, statusItems, this);

        if (AuthenticationService.getLoggedUser().getType() == null || AuthenticationService.getLoggedUser().getType().isClient()) {
            clients = Collections.singletonList(AuthenticationService.getLoggedUser());
            clientNames = Collections.singletonList(AuthenticationService.getLoggedUser().getName());
            spinnerService.setSpinnerUp(clientSpinner, clientNames, this);
            getOrders(AuthenticationService.getLoggedUser());

        } else {
            findViewById(R.id.lblClient).setVisibility(View.VISIBLE);
            clientSpinner.setVisibility(View.VISIBLE);

            (new UserService()).getUsers(new UserType(2)).addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    clients = task.getResult().toObjects(User.class);

                    clientNames.clear();
                    for (User client : clients) clientNames.add(client.getName());

                    spinnerService.setSpinnerUp(clientSpinner, clientNames, this);

                } else toastService.showErrorFromTask(R.string.getUserError, task);
            });
        }

        // Setting the filter onChange listener
        txtFilterContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                textFilter = editable.toString().toLowerCase();

                filterOrders();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.statusSpinner) {
            statusFilter = statusItems.get(position);
            filterOrders();

        } else if (parent.getId() == R.id.clientsSpinner) {
            statusSpinner.setSelection(0);
            txtFilterContent.setText("");
            getOrders(clients.get(position));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void reload(View view) {
        statusSpinner.setSelection(0);
        txtFilterContent.setText("");
        getOrders(clients.get(clientSpinner.getSelectedItemPosition()));
    }

    public void filterOrders() {
        List<Order> filteredOrders = new ArrayList<>();

        for (Order order : orders) {
            if (
                (statusFilter.equals("Todos") || order.getStatus().getDescription().equals(statusFilter)) &&
                (
                    textFilter.isEmpty() ||
                    order.getDescription().toLowerCase().contains(textFilter) ||
                    String.valueOf(order.getPrice()).contains(textFilter) ||
                    DateTextFormatter.dateToString(order.getStartDate()).contains(textFilter)
                )
            ) {
                filteredOrders.add(order);
            }
        }

        inflateOrders(filteredOrders);
    }

    public void getOrders(User client) {
        orderService.getOrders(client).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                orders = task.getResult().toObjects(Order.class);

                inflateOrders(orders);

            } else toastService.showErrorFromTask(R.string.getOrderError, task);
        });
    }

    public void inflateOrders(List<Order> orders) {
        float totalAmount = 0f;
        orderEntries.removeAllViews();

        for (Order order : orders) {
            totalAmount += order.getPrice();

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

        String amount = getString(R.string.orderAmount) + totalAmount;
        ((TextView) findViewById(R.id.txtAmount)).setText(amount);
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