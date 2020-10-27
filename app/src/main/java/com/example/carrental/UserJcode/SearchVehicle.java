package com.example.carrental.UserJcode;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.carrental.AdminJcode.UserDbOperations;
import com.example.carrental.NavigationHelper;
import com.example.carrental.R;
import com.example.carrental.SessionHelper;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
    TableLayout tablemain;
    TimePickerDialog picker;
    DatePickerDialog dpicker;
    EditText capacity;
    Button searchbtn;
    List<ArrayList<String>> allCars = null;
    public UserDbOperations userDbOperations;
    ScrollView scroller;
    LinearLayout scrollList;
    LinearLayout revokeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_vehicle_user);
        session = new SessionHelper(this);
        userDbOperations = new UserDbOperations(this);
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
        scrollList = (LinearLayout) findViewById(R.id.scrollList);
        revokeContainer = (LinearLayout) findViewById(R.id.revokeContainer);
        scroller = (ScrollView) findViewById(R.id.scroller);
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
        tablemain = (TableLayout) findViewById(R.id.tableMain);

        // Check if revoked or not
        String isRevoked = session.getRevokeStatus();
        if (isRevoked.equals("true")) {
            tablemain.setVisibility(View.GONE);
            scroller.setVisibility(View.GONE);
            revokeContainer.setVisibility(View.VISIBLE);
        }

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
                if (!(start_date.isEmpty() | end_date.isEmpty() | start_time.isEmpty() | end_time.isEmpty())) {
                    scrollList.removeAllViewsInLayout();
                    if(getDaysBetweenDates(dateFrom, dateTo)>0)
                    {
                        try {
                            if (validateTime() > 0) {
                                searchVehicles(selectedCapacity, dateFrom, dateTo);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "End Time is before Start Time", Toast.LENGTH_SHORT).show();
                            }
                        }catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "End Date is before Start Date", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please fill all fields correctly", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void searchVehicles(final int capacity, String startDate, String endDate) {
        //clearing whatever is in scroll list first
        scrollList.removeAllViewsInLayout();
        // search for cars
        final ArrayList<String> userInputs = new ArrayList<String>();
        userInputs.add(0, Integer.toString(capacity));
        userInputs.add(1, startDate);
        userInputs.add(2, endDate);
        allCars = userDbOperations.SearchVehicles(capacity, startDate, endDate);
        // get no of days
        final int noOfDays = getDaysBetweenDates(startDate, endDate);
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

            LinearLayout linearItem = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(dpToPx(5), dpToPx(5), dpToPx(5), dpToPx(0));
            linearItem.setLayoutParams(params);
            linearItem.setOrientation(LinearLayout.HORIZONTAL);
            linearItem.setBackgroundResource(R.drawable.rounded_edge);
            linearItem.setClickable(true);
            linearItem.setFocusable(true);
            linearItem.setId(View.generateViewId());
            //properties for Imageview
            ImageView userpic = new ImageView(this);
            LinearLayout.LayoutParams Imageviewparams = new LinearLayout.LayoutParams(dpToPx(50), dpToPx(50));
            Imageviewparams.setMargins(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
            Imageviewparams.gravity = Gravity.CENTER;
            userpic.setLayoutParams(Imageviewparams);

            userpic.setBackgroundResource(R.drawable.carspic);
            userpic.setContentDescription("carImage");
            linearItem.addView(userpic);

            //properties for TextView
            TextView userSummaryTextObject = new TextView(this);
            LinearLayout.LayoutParams txtparms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.7f);
            txtparms.setMargins(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
            userSummaryTextObject.setLayoutParams(txtparms);

            // get base cost
            final double baseCost = calculateBaseCost(noOfDays, startDate, endDate, weekdayRate, weekendRate, weeklyRate);
            String displayText = carName.trim() + " - Car Number: " + carNumber + "\nCapacity: " + capacity + ", Cost: $" + baseCost;
            userSummaryTextObject.setText(displayText);

            userSummaryTextObject.setTextSize(15);
            userSummaryTextObject.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
            linearItem.addView(userSummaryTextObject);

            //properties for Button
            Button btn = new Button(this);
            LinearLayout.LayoutParams btnparams = new LinearLayout.LayoutParams(dpToPx(30), dpToPx(30));
            btnparams.setMargins(dpToPx(0), dpToPx(0), dpToPx(30), dpToPx(0));
            btnparams.gravity = Gravity.CENTER;
            btn.setLayoutParams(btnparams);
            btn.setBackgroundResource(R.drawable.backbuttongrey);
            btn.setRotation(180f);
            linearItem.addView(btn);
            //adding the linearitem to main linear or scroll view
            scrollList.addView(linearItem);


            linearItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigationHelper.GoToReservationDetails(car, userInputs, baseCost, noOfDays);
                }
            });

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigationHelper.GoToReservationDetails(car, userInputs, baseCost, noOfDays);
                }
            });
        }

    }

    private double calculateBaseCost(int noOfDays, String startDate, String endDate, String weekdayRate, String weekendRate, String weeklyRate) {
        double result = 0.0;
        //TODO Greater than 7 days?
        if (noOfDays >= 7) {
            result = Double.parseDouble(weeklyRate);
        } else {
            Date dateFrom = null, dateTo = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm");
            try {
                // Convert string to date format
                dateFrom = dateFormat.parse(startDate);
                dateTo = dateFormat.parse(endDate);

                // Get date list
                List<Date> dateList = new ArrayList<Date>();
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(dateFrom);

                while (calendar.getTime().before(dateTo)) {
                    Date date = calendar.getTime();
                    dateList.add(date);
                    calendar.add(Calendar.DATE, 1);
                }
                // Count weekdays, weekends
                int weekdayCount = 0, weekendCount = 0;
                for (Date date : dateList) {
                    if (date.getDay() == 0 || date.getDay() == 6) {
                        weekendCount += 1;
                    } else {
                        weekdayCount += 1;
                    }
                }
                // Calculate cost
                result = weekdayCount * Double.parseDouble(weekdayRate) + weekendCount * Double.parseDouble(weekendRate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private String mergeDateTime(String date, String time) {
        return date.concat(" " + time + ":00");
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private int getDaysBetweenDates(String startDate, String endDate) {
        int numberOfDays = 0;
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM-dd-yyyy HH:mm:ss");
        DateTime start = formatter.parseDateTime(startDate);
        DateTime end = formatter.parseDateTime(endDate);
        numberOfDays = Days.daysBetween(start.toLocalDate(), end.toLocalDate()).getDays();
        if (numberOfDays == 0) {
            return 1;
        }
        return numberOfDays;
    }

    public long validateTime() throws ParseException {
        String start_date = startDate.getText().toString();
        String end_date = endDate.getText().toString();
        String start_time = startTime.getText().toString()+ ":00";
        String end_time = endTime.getText().toString()+ ":00";
        if(start_date.equalsIgnoreCase(end_date))
        {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            Date date1 = format.parse(start_time);
            Date date2 = format.parse(end_time);
            long difference = date2.getTime() - date1.getTime();
            return difference/1000;
        }

        return 1;
    }
}