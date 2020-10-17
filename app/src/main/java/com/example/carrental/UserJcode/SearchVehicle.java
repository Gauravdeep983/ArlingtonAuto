package com.example.carrental.UserJcode;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carrental.NavigationHelper;
import com.example.carrental.R;
import com.example.carrental.SessionHelper;

public class SearchVehicle extends AppCompatActivity {

    public NavigationHelper navigationHelper;
    Button backButton;
    public SessionHelper session;
    String userType = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_vehicle_user);
        session = new SessionHelper(this);
        userType = session.getloggedInUserType();
        backButton = (Button) findViewById(R.id.btnBack);
        navigationHelper = new NavigationHelper(SearchVehicle.this);

        // Back button click
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationHelper.GotoHomeScreen(userType);
            }
        });
    }


}