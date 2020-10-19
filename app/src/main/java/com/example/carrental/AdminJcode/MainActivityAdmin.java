package com.example.carrental.AdminJcode;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carrental.NavigationHelper;
import com.example.carrental.R;
import com.example.carrental.SessionHelper;

import java.nio.file.Files;

public class MainActivityAdmin extends AppCompatActivity {

    public SessionHelper session;
    public NavigationHelper navigationHelper;
    TextView greeting;
    ImageButton logoutbutton;
    ImageButton homebutton;
    Button viewprofilebutton;
    Button searchusers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home);
        //initializing the UI variables here
        greeting = (TextView)findViewById(R.id.greeting);
        logoutbutton = (ImageButton)findViewById(R.id.logoutbutton);
        homebutton = (ImageButton)findViewById(R.id.homebutton);
        viewprofilebutton = (Button)findViewById(R.id.viewprofilebutton);
        searchusers = (Button)findViewById(R.id.searchusers);

        session = new SessionHelper(this);
        navigationHelper = new NavigationHelper(MainActivityAdmin.this);
        greeting.setText("HELLO, "+ session.getloggedInUsername());


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


        searchusers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationHelper.GotoSearchallUsers();
            }
        });


    }
}
