package com.fatec.havingorder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fatec.havingorder.R;
import com.fatec.havingorder.models.User;
import com.fatec.havingorder.models.UserType;
import com.fatec.havingorder.services.AuthenticationService;
import com.fatec.havingorder.services.UserService;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

public class AddEditUserActivity extends ActivityWithActionBar implements AdapterView.OnItemSelectedListener {

    private boolean isEditing;

    private final UserService userService = new UserService();

    private final AuthenticationService authService = new AuthenticationService();

    private User user = new User();

    private EditText name;
    private EditText email;
    private EditText phone;
    private EditText password;

    private Spinner userTypeSpinner;
    private static final String[] userTypes = {"Desenvolvedor", "Cliente"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_user);

        super.setCustomActionBar();

        userTypeSpinner = findViewById(R.id.cmbUserType);
        ArrayAdapter<String> userTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userTypes);

        userTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(userTypeAdapter);
        userTypeSpinner.setOnItemSelectedListener(this);

        name = findViewById(R.id.txtNameContent);
        email = findViewById(R.id.txtEmailContent);
        phone = findViewById(R.id.txtPhoneContent);
        password = findViewById(R.id.txtPasswordContent);

        Intent intent = getIntent();
        String userEmail = intent.getStringExtra("userEmail");

        isEditing = userEmail != null && !userEmail.isEmpty();

        if (isEditing) {
            getUser(userEmail);
            super.setActionBarTitle(R.string.editUser);
            findViewById(R.id.btnRemove).setVisibility(View.VISIBLE);

        } else super.setActionBarTitle(R.string.addUser);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        user.setType(new UserType(position + 1));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void save(View view) {
        user.setEmail(email.getText().toString());
        user.setName(name.getText().toString());
        user.setPhone(phone.getText().toString());
        user.setPassword(password.getText().toString());

        if (user.isValid() && (isEditing || (user.getPassword() != null && !user.getPassword().isEmpty()))) {
            if (!isEditing) {
                authService.createUser(user.getEmail(), user.getPassword()).addOnCompleteListener(userTask -> {
                    if (userTask.isSuccessful() && userTask.getResult() != null && userTask.getResult().getUser() != null) {
                        userTask.getResult().getUser().getIdToken(true).addOnCompleteListener(tokenTask -> {
                           if (tokenTask.isSuccessful() && tokenTask.getResult() != null) {
                               user.setUserToken(tokenTask.getResult().getToken());
                               saveUser(user);

                           } else showErrorFromTask(getString(R.string.saveUserError), tokenTask);
                        });

                    } else showErrorFromTask(getString(R.string.saveUserError), userTask);
                });

            } else saveUser(user);

        } else showToast(getString(R.string.emptyFields));
    }

    public void saveUser(User user) {
        userService.save(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                showToast(getString(R.string.saveUserSuccess));
                finish();

            } else showErrorFromTask(getString(R.string.getUserError), task);
        });
    }

    public void removeUser(View view) {
        JSONObject postData = new JSONObject();

        try {
            postData.put("idToken", user.getUserToken());

        } catch (JSONException e) {
            showToast(getString(R.string.removeUserError) + e.getMessage());
            return;
        }

        Volley.newRequestQueue(this).add(new JsonObjectRequest(
                Request.Method.POST,
                authService.getRemoveUrl(),
                postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        userService.remove(user).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                showToast(getString(R.string.removeUserSuccess));
                                finish();

                            } else showErrorFromTask(getString(R.string.removeUserError), task);
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showToast(getString(R.string.removeUserError) + error.getMessage());
                    }
                }
        ));
    }

    public void getUser(String userEmail) {
        userService.getUser(userEmail).addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                user = task.getResult().toObject(User.class);

                if (user != null) {
                    name.setText(user.getName());
                    email.setText(user.getEmail());
                    phone.setText(user.getPhone());
                    userTypeSpinner.setSelection(user.getType().getId() - 1);

                } else showErrorFromTask(getString(R.string.getUserError), task);

            } else showErrorFromTask(getString(R.string.getUserError), task);
        });
    }

    private void showErrorFromTask(String message, Task task) {
        showToast(message + (task.getException() != null ? task.getException().toString() : ""));
    }

    private void showToast(String message) {
        Toast.makeText(
                AddEditUserActivity.this,
                message,
                Toast.LENGTH_SHORT
        ).show();
    }
}