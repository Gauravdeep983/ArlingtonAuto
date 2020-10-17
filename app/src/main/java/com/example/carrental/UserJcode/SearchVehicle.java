package com.example.carrental.UserJcode;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carrental.NavigationHelper;
import com.example.carrental.R;

public class SearchVehicle extends AppCompatActivity {

    public NavigationHelper navigationHelper;
    Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_vehicle_user);
        backButton = (Button)findViewById(R.id.btnBack);
        navigationHelper = new NavigationHelper(SearchVehicle.this);

        // Back button click
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationHelper.GotoHomeScreen();
            }
        });
    }



}