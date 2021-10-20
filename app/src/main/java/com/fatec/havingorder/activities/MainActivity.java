package com.fatec.havingorder.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.fatec.havingorder.R;
import com.fatec.havingorder.models.User;
import com.fatec.havingorder.services.AuthenticationService;
import com.fatec.havingorder.services.UserService;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        Handler hadler = new Handler();
        hadler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (auth.getCurrentUser() != null) {
                    (new UserService()).getUser(auth.getCurrentUser().getEmail()).addOnCompleteListener(getUserTask -> {
                        if (getUserTask.isSuccessful() && getUserTask.getResult() != null && getUserTask.getResult().exists()) {
                            AuthenticationService.setLoggedUser(getUserTask.getResult().toObject(User.class));
                            goToActivity(OrdersActivity.class);

                        } else {
                            auth.signOut();
                            goToActivity(SignInActivity.class);
                        }
                    });

                } else goToActivity(SignInActivity.class);
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }

    private void goToActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        finish();
    }
}