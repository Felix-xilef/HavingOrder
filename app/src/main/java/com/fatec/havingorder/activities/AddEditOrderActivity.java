package com.fatec.havingorder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.fatec.havingorder.R;

public class AddEditOrderActivity extends ActivityWithActionBar implements AdapterView.OnItemSelectedListener {

    private boolean isEditing;

    private Spinner statusSpinner;
    private Spinner clientsSpinner;
    private static final String[] statusItems = {"Aberto", "Finalizado", "Cancelado"};
    private static final String[] clientItems = {"Usuário1", "Usuário2", "Usuário3", "Usuário4"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_order);

        super.setCustomActionBar();

        statusSpinner = (Spinner) findViewById(R.id.statusSpinner);
        clientsSpinner = (Spinner) findViewById(R.id.clientsSpinner);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(AddEditOrderActivity.this, android.R.layout.simple_spinner_item, statusItems);
        ArrayAdapter<String> clientsAdapter = new ArrayAdapter<String>(AddEditOrderActivity.this, android.R.layout.simple_spinner_item, clientItems);

        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);
        statusSpinner.setOnItemSelectedListener(this);

        clientsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clientsSpinner.setAdapter(clientsAdapter);
        clientsSpinner.setOnItemSelectedListener(this);

        Intent intent = getIntent();
        String orderId = intent.getStringExtra("orderId");

        isEditing = orderId != null && !orderId.isEmpty();

        if (isEditing) {
            getOrder(orderId);
            super.setActionBarTitle(R.string.editOrder);

        } else super.setActionBarTitle(R.string.addOrder);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void saveOrder(View view) {
        if (isEditing) finish();
        else finish();
    }

    public void getOrder(String orderId) {

    }
}