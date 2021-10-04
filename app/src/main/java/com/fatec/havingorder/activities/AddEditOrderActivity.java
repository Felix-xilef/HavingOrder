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
import com.fatec.havingorder.others.DateTextFormatter;
import com.fatec.havingorder.services.OrderService;
import com.fatec.havingorder.services.UserService;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Calendar;

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
    private static final String[] clientItems = {"Usuário1", "Usuário2", "Usuário3", "Usuário4"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_order);

        super.setCustomActionBar();

        statusSpinner = (Spinner) findViewById(R.id.statusSpinner);
        clientSpinner = (Spinner) findViewById(R.id.clientsSpinner);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(AddEditOrderActivity.this, android.R.layout.simple_spinner_item, statusItems);
        ArrayAdapter<String> clientsAdapter = new ArrayAdapter<String>(AddEditOrderActivity.this, android.R.layout.simple_spinner_item, clientItems);

        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);
        statusSpinner.setOnItemSelectedListener(this);

        clientsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clientSpinner.setAdapter(clientsAdapter);
        clientSpinner.setOnItemSelectedListener(this);

        description = findViewById(R.id.txtDescriptionContent);
        startDate = findViewById(R.id.txtStartDateContent);
        endDate = findViewById(R.id.txtEndDateContent);
        price = findViewById(R.id.txtPriceContent);

        Intent intent = getIntent();
        String orderId = intent.getStringExtra("orderId");

        isEditing = orderId != null && !orderId.isEmpty();

        if (isEditing) {
            getOrder(orderId);
            super.setActionBarTitle(R.string.editOrder);
            findViewById(R.id.btnRemove).setVisibility(View.VISIBLE);

        } else super.setActionBarTitle(R.string.addOrder);

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
        if (v.getId() == R.id.statusSpinner) order.setStatus(new OrderStatus(position + 1));
        // else order.setClient();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void saveOrder(View view) {
        System.out.println("\n|\n|\tstartDate -> " + startDate.getText().toString() + "\n|");
        System.out.println("\n|\n|\tstartDate -> " + endDate.getText().toString() + "\n|");

        /*order.setDescription(description.getText().toString());
        order.setStartDate(startDate.getText().toString());
        order.setEndDate(endDate.getText().toString());
        order.setPrice(Float.parseFloat(price.getText().toString()));

        if (order.isValid()) {
            orderService.save(order);

            Toast.makeText(AddEditOrderActivity.this, R.string.saveOrderSuccess, Toast.LENGTH_SHORT).show();

            finish();

        } else Toast.makeText(AddEditOrderActivity.this, R.string.emptyFields, Toast.LENGTH_SHORT).show();*/
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
                    // clientSpinner.setSelection(order.getClient());

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