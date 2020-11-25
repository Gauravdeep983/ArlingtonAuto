package com.example.carrental.UserJcode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carrental.AdminJcode.UserDbOperations;
import com.example.carrental.NavigationHelper;
import com.example.carrental.R;
import com.example.carrental.RMJcode.CarDbOperations;
import com.example.carrental.RMJcode.ReservationDbOperations;
import com.example.carrental.SessionHelper;
import com.google.android.material.snackbar.Snackbar;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class ReservationSummary extends AppCompatActivity {
    public NavigationHelper navigationHelper;
    public SessionHelper session;
    public UserDbOperations userDbOperations;
    public ReservationDbOperations reservationDbOperations;
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
    Button cancelReservationBtn;
    Button editBtn;
    Button saveBtn;
    public CarDbOperations carDbOperations;
    boolean isMember;
    double finalCost = 0.0;
    double baseCost = 0.0;
    ArrayList<String> carDetails;
    ArrayList<String> reservationDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_reservation_summary);
        session = new SessionHelper(this);
        navigationHelper = new NavigationHelper(this);
        userDbOperations = new UserDbOperations(this);
        backbtn = (ImageButton) findViewById(R.id.backbutton);
        homebutton = (ImageButton) findViewById(R.id.homebutton);
        logoutbutton = (ImageButton) findViewById(R.id.logoutbutton);
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
        cancelReservationBtn = (Button) findViewById(R.id.deletereservation);
        editBtn = (Button) findViewById(R.id.edit);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        carDbOperations = new CarDbOperations(this);
        reservationDbOperations = new ReservationDbOperations(this);
        isMember = Boolean.parseBoolean(session.getMembershipStatus());
        final double gpsRate;
        final double onstarRate;
        final double siriusxmRate;

        // Get data sent from search vehicle screen
        Bundle args = getIntent().getBundleExtra("args");
        reservationDetails = (ArrayList<String>) args.getSerializable("reservation");
        carDetails = carDbOperations.GetcardetailsfromDB(reservationDetails.get(0));
        String reservation = getIntent().getStringExtra("reservationnumber");
        UpdateUI(reservation);
        baseCost = getIntent().getDoubleExtra("baseCost", 0.0);
        //noOfDays = getIntent().getIntExtra("noOfDays", 0);
        totalCost.setText("$" + reservationDetails.get(4));

        gpsRate = Double.parseDouble(carDetails.get(6).substring(1));
        onstarRate = Double.parseDouble(carDetails.get(7).substring(1));
        siriusxmRate = Double.parseDouble(carDetails.get(8).substring(1));
        // Default cost + tax
        totalCost.setText("$" + round(baseCost, 2));
        gps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    baseCost += gpsRate;
                } else {
                    baseCost -= gpsRate;
                }
                totalCost.setText("$" + Double.toString(round(baseCost, 2)));
            }
        });

        onstar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    baseCost += onstarRate;
                } else {
                    baseCost -= onstarRate;
                }
                totalCost.setText("$" + Double.toString(round(baseCost, 2)));
            }
        });

        siriusxm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    baseCost += siriusxmRate;
                } else {
                    baseCost -= siriusxmRate;
                }
                totalCost.setText("$" + Double.toString(round(baseCost, 2)));
            }
        });

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

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set edit to disabled and save to visible
                editBtn.setVisibility(View.GONE);
                saveBtn.setVisibility(View.VISIBLE);

                // Enable checkboxes
                gps.setEnabled(true);
                onstar.setEnabled(true);
                siriusxm.setEnabled(true);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edit visible and save invisible
                editBtn.setVisibility(View.VISIBLE);
                saveBtn.setVisibility(View.GONE);

                // Confirm from user
                AlertDialog.Builder builder = new AlertDialog.Builder(ReservationSummary.this);
                builder.setMessage("Do you want to update this reservation?");
                builder.setTitle("Are you sure?");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Update details in the db //TODO Update not working sometimes (testing pending)
                        reservationDbOperations.updateReservation(reservationnumber.getText().toString(), Double.parseDouble(totalCost.getText().toString().substring(1)), gps.isChecked(), onstar.isChecked(), siriusxm.isChecked());

                        // Successfully updated toast
                        Snackbar.make(findViewById(android.R.id.content), "Reservation Updated", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        // Update UI
                        UpdateUI(reservationnumber.getText().toString());
                        gps.setEnabled(false);
                        onstar.setEnabled(false);
                        siriusxm.setEnabled(false);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        cancelReservationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Confirm from user
                AlertDialog.Builder builder = new AlertDialog.Builder(ReservationSummary.this);
                builder.setMessage("Do you want to cancel this reservation?");
                builder.setTitle("Are you sure?");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel current reservation
                        reservationDbOperations.cancelReservation(reservationnumber.getText().toString());

                        // Toast for "Reservation Cancelled"
                        Toast.makeText(getApplicationContext(), "Reservation Cancelled", Toast.LENGTH_LONG).show();
                        // Redirect to homepage
                        navigationHelper.GotoHomeScreen("User");
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
     }
        });


    }

    public void UpdateUI(String reservationID) {
        ArrayList<String> reservationDetails = carDbOperations.GetreservationdetailsfromDB(reservationID);
        reservationnumber.setText(reservationDetails.get(0));
        carName.setText(reservationDetails.get(1));
        capacity.setText(reservationDetails.get(2));
        startDateText.setText(reservationDetails.get(3));
        endDateText.setText(reservationDetails.get(4));
        gps.setChecked(Boolean.parseBoolean(reservationDetails.get(5)));
        onstar.setChecked(Boolean.parseBoolean(reservationDetails.get(6)));
        siriusxm.setChecked(Boolean.parseBoolean(reservationDetails.get(7)));
        totalCost.setText("$" + reservationDetails.get(8));
        PassengerName.setText(reservationDetails.get(9));
    }

    private double calculateFinalCost(double baseCost, boolean isMember) {
        double result = 0.0;
        // Add tax
        result = baseCost + (0.0825 * baseCost);
        if (isMember) {
            // Add discount
            result = result - (result * 0.1);
        }
        return round(result, 2);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}