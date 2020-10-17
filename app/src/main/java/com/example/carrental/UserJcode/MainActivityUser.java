package com.example.carrental.UserJcode;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.carrental.NavigationHelper;
import com.example.carrental.R;
import com.example.carrental.SessionHelper;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivityUser extends AppCompatActivity {


  public SessionHelper session;
  public NavigationHelper navigationHelper;
    TextView greeting;
    ImageButton logoutbutton;
    ImageButton homebutton;
    Button viewprofilebtn;
    Button viewmyreservation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home);  //set UI to this java files
        //initializing the UI variables here
        greeting = (TextView)findViewById(R.id.greeting);
        logoutbutton = (ImageButton)findViewById(R.id.logoutbutton);
        homebutton = (ImageButton)findViewById(R.id.homebutton);
        viewprofilebtn = (Button)findViewById(R.id.viewprofilebutton);
        viewmyreservation = (Button)findViewById(R.id.viewreservationbutton);

        session = new SessionHelper(this);
        navigationHelper = new NavigationHelper(MainActivityUser.this);
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

        viewprofilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationHelper.GotoViewProfile();
            }
        });


        viewmyreservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
      }
        });

    }
}


