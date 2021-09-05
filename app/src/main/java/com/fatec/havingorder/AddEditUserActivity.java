package com.fatec.havingorder;

import android.os.Bundle;

public class AddEditUserActivity extends ActivityWithActionBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_user);

        super.setCustomActionBar();
    }
}