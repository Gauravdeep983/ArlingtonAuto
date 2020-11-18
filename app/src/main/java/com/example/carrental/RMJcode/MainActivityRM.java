package com.example.carrental.RMJcode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carrental.NavigationHelper;
import com.example.carrental.R;
import com.example.carrental.SessionHelper;

public class MainActivityRM extends AppCompatActivity {


    public SessionHelper session;
    public NavigationHelper navigationHelper;
    TextView greeting;
    ImageButton logoutbutton;
    ImageButton homebutton;
    Button viewprofilebutton;
    Button searchcar;
    Button viewavailablecars;
    Button viewreservationCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rm_home);
        //initializing the UI variables here
        greeting = (TextView) findViewById(R.id.greeting);
        logoutbutton = (ImageButton) findViewById(R.id.logoutbutton);
        homebutton = (ImageButton) findViewById(R.id.homebutton);
        viewprofilebutton = (Button)findViewById(R.id.viewprofilebutton);
        searchcar = (Button)findViewById(R.id.searchcar);
        viewavailablecars = (Button)findViewById(R.id.viewavailablecars);
        viewreservationCalendar = (Button)findViewById(R.id.viewreservationcalendar);

        session = new SessionHelper(this);
        navigationHelper = new NavigationHelper(MainActivityRM.this);
        greeting.setText("HELLO, " + session.getloggedInUsername());


        //calling logout function on HomebarNavigation class
        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationHelper.logout();

            }
        });

        //calling gotoHome function on HomebarNavigation class
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationHelper.GotoHomeScreen(session.getloggedInUserType());

            }
        });

        viewprofilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationHelper.GotoViewProfile();
            }
        });

        searchcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ui = new Intent(MainActivityRM.this, SearchCars.class);
                startActivity(ui);
            }
        });

        viewavailablecars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ui = new Intent(MainActivityRM.this, ViewAvailableCars.class);
                startActivity(ui);
            }
        });

        viewreservationCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ui = new Intent(MainActivityRM.this, ViewReservationCalendar.class);
                startActivity(ui);
            }
        });
    }
}
