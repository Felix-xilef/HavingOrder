package com.fatec.havingorder.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fatec.havingorder.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private EditText txtEmail;
    private EditText txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        if (auth.getCurrentUser() != null) {
            goToNextActivity();
        }

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
    }

    public void SignIn(View view) {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(SignInActivity.this, R.string.emptyFields, Toast.LENGTH_SHORT).show();

        } else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(SignInActivity.this, R.string.signinSuccess, Toast.LENGTH_SHORT).show();
                    goToNextActivity();

                } else {
                    Toast.makeText(SignInActivity.this, R.string.signinFailure, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void goToNextActivity() {
        Intent intent = new Intent(this, OrdersActivity.class);
        startActivity(intent);
        finish();
    }
}