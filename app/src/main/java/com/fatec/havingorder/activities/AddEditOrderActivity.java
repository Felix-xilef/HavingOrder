package com.fatec.havingorder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.fatec.havingorder.R;
import com.fatec.havingorder.models.Order;
import com.fatec.havingorder.models.OrderStatus;
import com.fatec.havingorder.models.User;
import com.fatec.havingorder.models.UserType;
import com.fatec.havingorder.Utils.DateTextFormatter;
import com.fatec.havingorder.services.AuthenticationService;
import com.fatec.havingorder.services.OrderService;
import com.fatec.havingorder.services.SpinnerService;
import com.fatec.havingorder.services.ToastService;
import com.fatec.havingorder.services.UserService;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class AddEditOrderActivity extends ActivityWithActionBar implements AdapterView.OnItemSelectedListener {

    private boolean isEditing;

    private final ToastService toastService = new ToastService(AddEditOrderActivity.this);

    private final OrderService orderService = new OrderService();

    private final SpinnerService spinnerService = new SpinnerService();

    private Order order = new Order();

    private EditText description;
    private EditText startDate;
    private EditText endDate;
    private EditText price;

    private Spinner statusSpinner;
    private Spinner clientSpinner;

    private final List<String> statusItems = Arrays.asList(
            OrderStatus.OPEN_DESCRIPTION,
            OrderStatus.CLOSED_DESCRIPTION,
            OrderStatus.CANCELED_DESCRIPTION
    );

    private static List<String> clientNames = new ArrayList<>();
    private List<User> clients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_order);

        super.setCustomActionBar();

        // Getting the views
        description = findViewById(R.id.txtDescriptionContent);
        startDate = findViewById(R.id.txtStartDateContent);
        endDate = findViewById(R.id.txtEndDateContent);
        price = findViewById(R.id.txtPriceContent);
        statusSpinner = findViewById(R.id.statusSpinner);
        clientSpinner = findViewById(R.id.clientsSpinner);

        // Setting statusSpinner up
        spinnerService.setSpinnerUp(statusSpinner, statusItems, this);

        if (AuthenticationService.getLoggedUser().getType() == null || AuthenticationService.getLoggedUser().getType().isClient()) {
            clients = Collections.singletonList(AuthenticationService.getLoggedUser());
            clientNames = Collections.singletonList(AuthenticationService.getLoggedUser().getName());

            setActivityUp();

            if (!isEditing) order.setClient(AuthenticationService.getLoggedUser());

        } else {
            findViewById(R.id.lblClient).setVisibility(View.VISIBLE);
            clientSpinner.setVisibility(View.VISIBLE);

            (new UserService()).getUsers(new UserType(2)).addOnCompleteListener(task -> {

                // Setting clientSpinner up
                if (task.isSuccessful() && task.getResult() != null) {
                    clients = task.getResult().toObjects(User.class);

                    clientNames.clear();
                    for (User client : clients) clientNames.add(client.getName());

                    setActivityUp();

                } else toastService.showErrorFromTask(R.string.getUserError, task);
            });
        }

        startDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                DateTextFormatter.format(startDate, charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        endDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                DateTextFormatter.format(endDate, charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void setActivityUp() {
        spinnerService.setSpinnerUp(clientSpinner, clientNames, this);

        // Verifying scope
        Intent intent = getIntent();
        String orderId = intent.getStringExtra("orderId");

        isEditing = orderId != null && !orderId.isEmpty();

        if (isEditing) {
            getOrder(orderId);
            super.setActionBarTitle(R.string.editOrder);
            findViewById(R.id.btnRemove).setVisibility(View.VISIBLE);
            findViewById(R.id.btnComments).setVisibility(View.VISIBLE);

        } else super.setActionBarTitle(R.string.addOrder);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        if (parent.getId() == R.id.statusSpinner) order.setStatus(new OrderStatus(position + 1));
        else if (parent.getId() == R.id.clientsSpinner) order.setClient(clients.get(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public Date stringToDate(String text) throws ParseException {
        if (text.trim().length() == 10) return DateTextFormatter.stringToDate(text);
        else return null;
    }

    public void saveOrder(View view) {
        try {
            order.setStartDate(stringToDate(startDate.getText().toString()));
            order.setEndDate(stringToDate(endDate.getText().toString()));

        } catch (ParseException e) {
            toastService.showToast(getString(R.string.saveOrderError) + ": " + e.getMessage());
        }

        order.setDescription(description.getText().toString());
        order.setPrice(Float.parseFloat(price.getText().toString()));

        if (!isEditing) order.generateId();

        if (order.isValid()) {
            orderService.save(order).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    toastService.showToast(R.string.saveOrderSuccess);

                    finish();

                } else toastService.showToast(R.string.emptyFields);
            });

        } else toastService.showToast(R.string.emptyFields);
    }

    public void removeOrder(View view) {
        orderService.remove(order).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                toastService.showToast(R.string.removeOrderSuccess);
                finish();

            } else toastService.showErrorFromTask(R.string.removeOrderError, task);
        });
    }

    public void getOrder(String orderId) {
        orderService.getOrder(orderId).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                order = task.getResult().toObject(Order.class);

                if (order != null) {
                    description.setText(order.getDescription());
                    startDate.setText(DateTextFormatter.dateToString(order.getStartDate()));
                    endDate.setText(DateTextFormatter.dateToString(order.getEndDate()));
                    price.setText(String.valueOf(order.getPrice()));
                    statusSpinner.setSelection(order.getStatus().getId() - 1);
                    clientSpinner.setSelection(clients.indexOf(order.getClient()));

                } else toastService.showErrorFromTask(R.string.getOrderError, task);

            } else toastService.showErrorFromTask(R.string.getOrderError, task);
        });
    }

    public void goToAddEditOrderCommentsActivity(View view) {
        Intent intent = new Intent(this, AddEditOrderCommentsActivity.class);
        intent.putExtra("orderId", order.getId());

        startActivity(intent);
    }
}