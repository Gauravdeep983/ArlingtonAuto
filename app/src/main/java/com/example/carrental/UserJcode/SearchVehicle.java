package com.example.carrental.UserJcode;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carrental.NavigationHelper;
import com.example.carrental.R;
import com.example.carrental.SessionHelper;

import java.util.Calendar;

public class SearchVehicle extends AppCompatActivity {

    public NavigationHelper navigationHelper;
    ImageButton backButton;
    ImageButton logoutButton;
    ImageButton increaseButton;
    ImageButton decreaseButton;
    public SessionHelper session;
    String userType = null;
    EditText startTime;
    EditText startDate;
    EditText endTime;
    EditText endDate;
    TimePickerDialog picker;
    DatePickerDialog dpicker;
    EditText capacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_vehicle_user);
        session = new SessionHelper(this);
        userType = session.getloggedInUserType();
        backButton = (ImageButton)findViewById(R.id.backbutton);
        logoutButton = (ImageButton)findViewById(R.id.logoutbutton);
        navigationHelper = new NavigationHelper(SearchVehicle.this);
        startTime = (EditText)findViewById(R.id.startTime);
        startDate = (EditText)findViewById(R.id.startDate);
        endDate   = (EditText)findViewById(R.id.endDate);
        endTime   = (EditText)findViewById(R.id.endTime);
        increaseButton = (ImageButton)findViewById(R.id.increasebutton);
        decreaseButton = (ImageButton)findViewById(R.id.decreasebutton);
        capacity  = (EditText)findViewById(R.id.capacity);
        // Back button click
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationHelper.logout();
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(SearchVehicle.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                startTime.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(SearchVehicle.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                               endTime.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                final Calendar cldr = Calendar.getInstance();
                int month = cldr.get(Calendar.MONTH);
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                dpicker = new DatePickerDialog(SearchVehicle.this,new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                startDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                dpicker.getDatePicker().setMinDate(System.currentTimeMillis());
                dpicker.show();

            }
        });


        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                final Calendar cldr = Calendar.getInstance();
                int month = cldr.get(Calendar.MONTH);
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                dpicker = new DatePickerDialog(SearchVehicle.this,new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                endDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                dpicker.getDatePicker().setMinDate(System.currentTimeMillis());
                dpicker.show();

            }
        });

        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current_value = Integer.parseInt(capacity.getText().toString());
               if (current_value < 9){
                        capacity.setText( Integer.toString(current_value+1));
                }
            }
        });

        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current_value = Integer.parseInt(capacity.getText().toString());
                if (current_value > 1){
                    capacity.setText( Integer.toString(current_value-1));
                }
            }
        });
    }


}