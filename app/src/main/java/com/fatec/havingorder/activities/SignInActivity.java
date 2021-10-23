package com.fatec.havingorder.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.fatec.havingorder.R;
import com.fatec.havingorder.models.User;
import com.fatec.havingorder.services.AuthenticationService;
import com.fatec.havingorder.services.ToastService;
import com.fatec.havingorder.services.UserService;

public class SignInActivity extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtPassword;

    private final ToastService toastService = new ToastService(SignInActivity.this);

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
            toastService.showToast(getString(R.string.emptyFields));

        } else {
            (new UserService()).getUser(email).addOnCompleteListener(getUserTask -> {
                if (getUserTask.isSuccessful()) {
                    if (getUserTask.getResult() != null && getUserTask.getResult().exists()) {
                        (new AuthenticationService()).signIn(email, password).addOnCompleteListener(this, signInTask -> {
                            if (signInTask.isSuccessful()) {
                                AuthenticationService.setLoggedUser(getUserTask.getResult().toObject(User.class));
                                toastService.showToast(getString(R.string.signinSuccess));
                                goToActivity(OrdersActivity.class);

                            } else toastService.showToast(getString(R.string.signinFailure));
                        });

                    } else toastService.showToast(getString(R.string.signinFailure));

                } else toastService.showErrorFromTask(getString(R.string.getUserError), getUserTask);
            });
        }
    }

    public void goToActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        finish();
    }
}