package com.example.carrental.UserJcode;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carrental.AdminJcode.UserDbOperations;
import com.example.carrental.NavigationHelper;
import com.example.carrental.R;
import com.example.carrental.SessionHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    Button searchbtn;
    LinearLayout scrollList;
    public UserDbOperations userDbOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_vehicle_user);
        session = new SessionHelper(this);
        userType = session.getloggedInUserType();
        backButton = (ImageButton) findViewById(R.id.backbutton);
        logoutButton = (ImageButton) findViewById(R.id.logoutbutton);
        navigationHelper = new NavigationHelper(SearchVehicle.this);
        startTime = (EditText) findViewById(R.id.startTime);
        startDate = (EditText) findViewById(R.id.startDate);
        endDate = (EditText) findViewById(R.id.endDate);
        endTime = (EditText) findViewById(R.id.endTime);
        increaseButton = (ImageButton) findViewById(R.id.increasebutton);
        decreaseButton = (ImageButton) findViewById(R.id.decreasebutton);
        capacity = (EditText) findViewById(R.id.capacity);
        searchbtn = (Button) findViewById(R.id.btnSearch);
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
                //Adds +1 hour to the current time
                cldr.add(Calendar.HOUR_OF_DAY, 1);
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
                dpicker = new DatePickerDialog(SearchVehicle.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startDate.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
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
                dpicker = new DatePickerDialog(SearchVehicle.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDate.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
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
                if (current_value < 9) {
                    capacity.setText(Integer.toString(current_value + 1));
                }
            }
        });

        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current_value = Integer.parseInt(capacity.getText().toString());
                if (current_value > 1) {
                    capacity.setText(Integer.toString(current_value - 1));
                }
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                int selectedCapacity = Integer.parseInt(capacity.getText().toString());
                String start_date = startDate.getText().toString();
                String end_date = endDate.getText().toString();
                String start_time = startTime.getText().toString();
                String end_time = endTime.getText().toString();

                String dateFrom = mergeDateTime(start_date, start_time);
                String dateTo = mergeDateTime(end_date, end_time);
                searchVehicles(selectedCapacity, dateFrom, dateTo);
            }
        });

    }

    private void searchVehicles(int capacity, String startDate, String endDate) {
        //clearing whatever is in scroll list first
        //scrollList.removeAllViewsInLayout();
        List<ArrayList<String>> allCars = userDbOperations.SearchVehicles(capacity, startDate, endDate);
        for (final ArrayList<String> car : allCars) {
            String carName = car.get(0);
            String carNumber = car.get(1);
            String maxCapacity = car.get(2);
            String weekdayRate = car.get(3);
            String weekendRate = car.get(4);
            String weeklyRate = car.get(5);
            String gpsRate = car.get(6);
            String onstarRate = car.get(7);
            String siriusxmRate = car.get(8);
        }
    }


    private String mergeDateTime(String date, String time) {
        return date.concat(" " + time + ":00");
    }

}