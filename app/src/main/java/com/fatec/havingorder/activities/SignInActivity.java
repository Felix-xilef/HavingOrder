package com.fatec.havingorder.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fatec.havingorder.R;
import com.fatec.havingorder.services.AuthenticationService;

public class SignInActivity extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
    }

    public void SignIn(View view) {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(SignInActivity.this, R.string.emptyFields, Toast.LENGTH_SHORT).show();

        } else {
            (new AuthenticationService()).signIn(email, password).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    AuthenticationService.setLoggedUser(email).addOnCompleteListener(setUserTask -> {
                        if (setUserTask.isSuccessful()) {
                            Toast.makeText(SignInActivity.this, R.string.signinSuccess, Toast.LENGTH_SHORT).show();

                            goToActivity(OrdersActivity.class);

                        } else Toast.makeText(SignInActivity.this, R.string.setLoggedUserError, Toast.LENGTH_SHORT).show();
                    });

                } else Toast.makeText(SignInActivity.this, R.string.signinFailure, Toast.LENGTH_SHORT).show();
            });
        }
    }

    public void goToActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        finish();
    }
}