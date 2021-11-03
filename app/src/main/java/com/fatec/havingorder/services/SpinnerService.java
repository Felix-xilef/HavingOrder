package com.fatec.havingorder.services;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

public class SpinnerService {

    public void setSpinnerUp(Spinner spinner, List<String> options, AdapterView.OnItemSelectedListener listener) {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(spinner.getContext(), android.R.layout.simple_spinner_item, options);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(listener);
    }
}
