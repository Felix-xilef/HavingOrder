package com.fatec.havingorder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AddEditUserActivity extends ActivityWithActionBar implements AdapterView.OnItemSelectedListener {

    private Spinner userTypeSpinner;
    private static final String[] userTypes = {"Desenvolvedor", "Cliente"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_user);

        super.setCustomActionBar();

        userTypeSpinner = (Spinner) findViewById(R.id.cmbUserType);
        ArrayAdapter<String> userTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, userTypes);

        userTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(userTypeAdapter);
        userTypeSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        switch (position) {
            case 0:
                break;

            case 1:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void saveUser(View view) {
        /* Intent intent = new Intent(this, UsersActivity.class);
        startActivity(intent); */
        finish();
    }
}