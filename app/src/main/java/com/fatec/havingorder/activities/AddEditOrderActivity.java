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
import android.widget.Toast;

import com.fatec.havingorder.R;
import com.fatec.havingorder.models.Order;
import com.fatec.havingorder.models.OrderStatus;
import com.fatec.havingorder.models.User;
import com.fatec.havingorder.models.UserType;
import com.fatec.havingorder.others.DateTextFormatter;
import com.fatec.havingorder.services.OrderService;
import com.fatec.havingorder.services.UserService;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddEditOrderActivity extends ActivityWithActionBar implements AdapterView.OnItemSelectedListener {

    private boolean isEditing;

    private final OrderService orderService = new OrderService();

    private Order order = new Order();

    private EditText description;
    private EditText startDate;
    private EditText endDate;
    private EditText price;

    private Spinner statusSpinner;
    private Spinner clientSpinner;

    private static final String[] statusItems = {"Aberto", "Finalizado", "Cancelado"};

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
        statusSpinner = (Spinner) findViewById(R.id.statusSpinner);
        clientSpinner = (Spinner) findViewById(R.id.clientsSpinner);

        // Setting statusSpinner up
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(AddEditOrderActivity.this, android.R.layout.simple_spinner_item, statusItems);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);
        statusSpinner.setOnItemSelectedListener(this);

        (new UserService()).getUsers(new UserType(2)).addOnCompleteListener(task -> {

            // Setting clientSpinner up
            if (task.isSuccessful() && task.getResult() != null) {
                clients = task.getResult().toObjects(User.class);

                for (User client : clients) clientNames.add(client.getName());

                ArrayAdapter<String> clientsAdapter = new ArrayAdapter<>(AddEditOrderActivity.this, android.R.layout.simple_spinner_item, clientNames);
                clientsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                clientSpinner.setAdapter(clientsAdapter);
                clientSpinner.setOnItemSelectedListener(this);

            } else Toast.makeText(AddEditOrderActivity.this, R.string.getUserError, Toast.LENGTH_SHORT).show();

            // Verifying scope
            Intent intent = getIntent();
            String orderId = intent.getStringExtra("orderId");

            isEditing = orderId != null && !orderId.isEmpty();

            if (isEditing) {
                getOrder(orderId);
                super.setActionBarTitle(R.string.editOrder);
                findViewById(R.id.btnRemove).setVisibility(View.VISIBLE);

            } else super.setActionBarTitle(R.string.addOrder);
        });

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        if (parent.getId() == R.id.statusSpinner) order.setStatus(new OrderStatus(position + 1));
        else if (parent.getId() == R.id.clientsSpinner) order.setClient(clients.get(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void saveOrder(View view) {
        try {
            order.setStartDate(DateTextFormatter.stringToCalendar(startDate.getText().toString()));
            order.setEndDate(DateTextFormatter.stringToCalendar(endDate.getText().toString()));

        } catch (ParseException e) {
            Toast.makeText(
                    AddEditOrderActivity.this,
                    getString(R.string.saveOrderError) + ": " + e.getMessage(),
                    Toast.LENGTH_SHORT
            ).show();
        }

        order.setDescription(description.getText().toString());
        order.setPrice(Float.parseFloat(price.getText().toString()));

        //order.setComment();

        order.generateId();

        if (order.isValid()) {
            orderService.save(order).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(AddEditOrderActivity.this, R.string.saveOrderSuccess, Toast.LENGTH_SHORT).show();

                    finish();

                } else {
                    System.out.println("\n|\n|\tError -> " + task.getException() + "\n|");
                    Toast.makeText(AddEditOrderActivity.this, R.string.emptyFields, Toast.LENGTH_SHORT).show();
                }
            });

        } else Toast.makeText(AddEditOrderActivity.this, R.string.emptyFields, Toast.LENGTH_SHORT).show();
    }

    public void removeOrder(View view) {
        orderService.remove(order);
    }

    public void getOrder(String orderId) {
        orderService.getOrder(orderId).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                order = task.getResult().toObject(Order.class);

                if (order != null) {
                    description.setText(order.getDescription());
                    startDate.setText(order.getStartDate().toString());
                    endDate.setText(order.getEndDate().toString());
                    price.setText(String.valueOf(order.getPrice()));
                    statusSpinner.setSelection(order.getStatus().getId() - 1);
                    clientSpinner.setSelection(clients.indexOf(order.getClient()));

                    // Set comments

                } else showGetUserError(task);

            } else showGetUserError(task);
        });
    }

    private void showGetUserError(Task<DocumentSnapshot> task) {
        Toast.makeText(
                AddEditOrderActivity.this,
                getText(R.string.getOrderError) + (task.getException() != null ? task.getException().toString() : ""),
                Toast.LENGTH_SHORT
        ).show();
    }
}