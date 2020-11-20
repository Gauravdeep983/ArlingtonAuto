package com.example.carrental.RMJcode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carrental.AdminJcode.UserDbOperations;
import com.example.carrental.NavigationHelper;
import com.example.carrental.R;
import com.example.carrental.RMJcode.CarDbOperations;
import com.example.carrental.RMJcode.Viewselectedcar;
import com.example.carrental.SessionHelper;
import com.example.carrental.UserJcode.viewmyprofile.U_ViewProfile;

import java.util.ArrayList;

public class RM_ReservationSummary extends AppCompatActivity {

    public NavigationHelper navigationHelper;
    public SessionHelper session;
    TextView reservationnumber;
    TextView carName;
    TextView PassengerName;
    TextView capacity;
    TextView startDateText;
    TextView endDateText;
    CheckBox gps;
    CheckBox onstar;
    CheckBox siriusxm;
    TextView totalCost;
    ImageButton backbtn;
    ImageButton homebutton;
    ImageButton logoutbutton;
    Button deletereservationBtn;
    public CarDbOperations carDbOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rm_reservationsummary);
        session = new SessionHelper(this);
        navigationHelper = new NavigationHelper(this);
        backbtn = (ImageButton)findViewById(R.id.backbutton);
        homebutton = (ImageButton)findViewById(R.id.homebutton);
        logoutbutton = (ImageButton)findViewById(R.id.logoutbutton);
        reservationnumber = (TextView) findViewById(R.id.reservationnumber);
        carName = (TextView) findViewById(R.id.carName);
        PassengerName = (TextView) findViewById(R.id.PassengerName);
        capacity = (TextView) findViewById(R.id.occupantCapacity);
        startDateText = (TextView) findViewById(R.id.startDate);
        endDateText = (TextView) findViewById(R.id.endDate);
        gps = (CheckBox) findViewById(R.id.gps);
        onstar = (CheckBox) findViewById(R.id.onstar);
        siriusxm = (CheckBox) findViewById(R.id.siriusxm);
        totalCost = (TextView) findViewById(R.id.totalCost);
        deletereservationBtn = (Button)findViewById(R.id.deletereservation);
        carDbOperations = new CarDbOperations(RM_ReservationSummary.this);
        String reservation = getIntent().getStringExtra("reservationnumber");
        UpdateUI(reservation);

        backbtn.setOnClickListener(new View.OnClickListener() {
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

        deletereservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RM_ReservationSummary.this);
                builder.setMessage("Do you want to delete this reservation?");
                builder.setTitle("Are you sure ?");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                        if(carDbOperations.DeleteReservationinDB(reservationnumber.getText().toString()))
                        {
                            Toast.makeText(getApplicationContext(), "Reservation Deleted", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }});
                builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    public void UpdateUI(String reservationID)
    {
        ArrayList<String> reservationDetails =  carDbOperations.GetreservationdetailsfromDB(reservationID);
        reservationnumber.setText(reservationDetails.get(0));
        carName.setText(reservationDetails.get(1));
        capacity.setText(reservationDetails.get(2));
        startDateText.setText(reservationDetails.get(3));
        endDateText.setText(reservationDetails.get(4));
        gps.setChecked(Boolean.parseBoolean(reservationDetails.get(5)));
        onstar.setChecked(Boolean.parseBoolean(reservationDetails.get(6)));
        siriusxm.setChecked(Boolean.parseBoolean(reservationDetails.get(7)));
        totalCost.setText("$" +reservationDetails.get(8));
        PassengerName.setText(reservationDetails.get(9));
    }

}