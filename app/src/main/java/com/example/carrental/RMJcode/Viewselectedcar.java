package com.example.carrental.RMJcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carrental.AdminJcode.Admin_searchallusers;
import com.example.carrental.AdminJcode.UserDbOperations;
import com.example.carrental.NavigationHelper;
import com.example.carrental.R;
import com.example.carrental.SessionHelper;
import com.example.carrental.UserJcode.viewmyprofile.U_ViewProfile;

import org.w3c.dom.Text;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Viewselectedcar extends AppCompatActivity {
    ImageButton backbutton;
    ImageButton homebutton;
    ImageButton logoutbutton;
    public SessionHelper session;
    public NavigationHelper navigationHelper;
    String sessionUsername = null;
    String userType= null;
    public CarDbOperations carDbOperations;
    TextView carnam;
    TextView carNumber;
    TextView carCapacity;
    TextView Weekdayrate;
    TextView Weekendrate;
    TextView Weekrate;
    TextView gpsrate;
    TextView onstarrate;
    TextView siriusxmrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rm_viewselectedcar);
        session = new SessionHelper(this);
        sessionUsername = session.getloggedInUsername();
        carDbOperations = new CarDbOperations(Viewselectedcar.this);
        userType = session.getloggedInUserType();
        navigationHelper = new NavigationHelper(Viewselectedcar.this);
        carnam = (TextView)findViewById(R.id.carName);
        carNumber = (TextView)findViewById(R.id.carNumber);
        carCapacity = (TextView)findViewById(R.id.carcapacity);
        Weekdayrate = (TextView)findViewById(R.id.WeekdayRate);
        Weekendrate = (TextView)findViewById(R.id.WeekendRate);
        Weekrate   = (TextView)findViewById(R.id.WeekRate);
        gpsrate     = (TextView)findViewById(R.id.gpsRate);
        onstarrate     = (TextView)findViewById(R.id.onstarrate);
        siriusxmrate     = (TextView)findViewById(R.id.siriusxmrate);


        backbutton = (ImageButton)findViewById(R.id.backbutton);
        homebutton = (ImageButton)findViewById(R.id.homebutton);
        logoutbutton = (ImageButton)findViewById(R.id.logoutbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationHelper.GotoHomeScreen(session.getloggedInUserType());
            }
        });

        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationHelper.logout();
            }
        });
        Intent myIntent = getIntent();
        String selectedcar =  myIntent.getStringExtra("carname");

        UpdateUI(selectedcar);


    }

    public void UpdateUI(String carname)
    {
        ArrayList<String> cardetails =  carDbOperations.GetcardetailsfromDB(carname);
        carNumber.setText(cardetails.get(0));
        carnam.setText(cardetails.get(1));
        carCapacity.setText(cardetails.get(2));
        Weekdayrate.setText(cardetails.get(3));
        Weekendrate.setText(cardetails.get(4));
        Weekrate.setText(cardetails.get(5));
        gpsrate.setText(cardetails.get(6));
        onstarrate.setText(cardetails.get(7));
        siriusxmrate.setText(cardetails.get(8));

    }
}