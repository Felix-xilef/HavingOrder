package com.fatec.havingorder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.fatec.havingorder.R;
import com.fatec.havingorder.models.User;
import com.fatec.havingorder.models.UserType;
import com.fatec.havingorder.services.UserService;

public class AddEditUserActivity extends ActivityWithActionBar implements AdapterView.OnItemSelectedListener {

    private UserService userService = new UserService();

    private int userId;
    private User user = new User();

    private EditText name;
    private EditText email;
    private EditText phone;

    private TextView lblAddEditUser;

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

        name = (EditText) findViewById(R.id.txtNameContent);
        email = (EditText) findViewById(R.id.txtEmailContent);
        phone = (EditText) findViewById(R.id.txtPhoneContent);

        lblAddEditUser = (TextView) findViewById(R.id.lblAddEditUser);

        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1);

        if (userId > -1) {
            getUser(userId);

            lblAddEditUser.setText(getString(R.string.editUser));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        user.setType(new UserType(position + 1));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void saveUser(View view) {
        finish();
    }

    public void getUser(int id) {
        user = userService.getUser(id);

        name.setText(user.getName());
        email.setText(user.getEmail());
        phone.setText(user.getPhone());
        userTypeSpinner.setSelection(user.getType().getId() - 1);
    }
}