package com.example.carrental.RMJcode;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.carrental.AdminJcode.UserDbOperations;
import com.example.carrental.NavigationHelper;
import com.example.carrental.R;
import com.example.carrental.SessionHelper;
import com.example.carrental.UserJcode.SearchVehicle;

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

public class ViewAvailableCars extends AppCompatActivity {

    public NavigationHelper navigationHelper;
    ImageButton backButton;
    ImageButton logoutButton;
    public SessionHelper session;
    String userType = null;
    EditText startTime;
    EditText startDate;
    EditText endTime;
    EditText endDate;
    TimePickerDialog picker;
    DatePickerDialog dpicker;
    Button searchbtn;
    List<ArrayList<String>> allCars = null;
    public UserDbOperations userDbOperations;
    ScrollView scroller;
    LinearLayout scrollList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_available_cars);
        session = new SessionHelper(this);
        userDbOperations = new UserDbOperations(this);
        userType = session.getloggedInUserType();
        backButton = (ImageButton) findViewById(R.id.backbutton);
        logoutButton = (ImageButton) findViewById(R.id.logoutbutton);
        navigationHelper = new NavigationHelper(ViewAvailableCars.this);
        startTime = (EditText) findViewById(R.id.startTime);
        startDate = (EditText) findViewById(R.id.startDate);
        endDate = (EditText) findViewById(R.id.endDate);
        endTime = (EditText) findViewById(R.id.endTime);
        searchbtn = (Button) findViewById(R.id.btnSearch);
        scrollList = (LinearLayout) findViewById(R.id.scrollList);
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


        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(ViewAvailableCars.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                                try {

                                    Date start_date = dateFormat.parse(startDate.getText().toString());
                                    int day= start_date.getDay();
                                    if(!checkArlingtonTimings(hourOfDay,minute,day))
                                    {
                                        Toast.makeText(getApplicationContext(), "Please select Arlington Auto Timings", Toast.LENGTH_SHORT).show();
                                        //ShowPopUp();
                                        startTime.setText("");
                                    }
                                    else
                                    {
                                        startTime.setText(hourOfDay + ":" + minute);
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

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
                picker = new TimePickerDialog(ViewAvailableCars.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                                try {
                                    Date end_date = dateFormat.parse(endDate.getText().toString());
                                    int day= end_date.getDay();
                                    if(!checkArlingtonTimings(hourOfDay,minute,day))
                                    {
                                        Toast.makeText(getApplicationContext(), "Please select Arlington Auto Timings", Toast.LENGTH_SHORT).show();
                                       // ShowPopUp();
                                        endTime.setText("");
                                    }
                                    else
                                    {
                                        endTime.setText(hourOfDay + ":" + minute);
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
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
                dpicker = new DatePickerDialog(ViewAvailableCars.this, new DatePickerDialog.OnDateSetListener() {
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
                dpicker = new DatePickerDialog(ViewAvailableCars.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDate.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
                    }
                }, year, month, day);
                dpicker.getDatePicker().setMinDate(System.currentTimeMillis());
                dpicker.show();

            }
        });



        searchbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
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
                                ViewAvailableVehicles( dateFrom, dateTo);
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
    private void ViewAvailableVehicles( String startDate, String endDate) {
        //clearing whatever is in scroll list first
        scrollList.removeAllViewsInLayout();
        // search for cars
        final ArrayList<String> userInputs = new ArrayList<String>();
//        userInputs.add(0, Integer.toString(capacity));
        userInputs.add(0, startDate);
        userInputs.add(1, endDate);
        allCars = userDbOperations.ViewAvailableVehicles( startDate, endDate);
        for (final ArrayList<String> car : allCars) {
            final String carName = car.get(0);
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
            ImageView carpic = new ImageView(this);
            LinearLayout.LayoutParams Imageviewparams = new LinearLayout.LayoutParams(dpToPx(50), dpToPx(50));
            Imageviewparams.setMargins(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
            Imageviewparams.gravity = Gravity.CENTER;
            carpic.setLayoutParams(Imageviewparams);

            carpic.setBackgroundResource(R.drawable.carspic);
            carpic.setContentDescription("CarImage");
            linearItem.addView(carpic);

            //properties for TextView
            TextView userSummaryTextObject = new TextView(this);
            LinearLayout.LayoutParams txtparms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.7f);
            txtparms.setMargins(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
            userSummaryTextObject.setLayoutParams(txtparms);
            // get no of days
            final int noOfDays = getDaysBetweenDates(startDate, endDate);
            String displayText = "Car Name - " + carName.trim() + "\nCar Number - " + carNumber + "\nCar Occupant Capacity - " + maxCapacity + "\nCost Per Day - $"+weekdayRate+"\nCar Status - Available" ;
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
                    navigationHelper.GotoViewSelectedCar(carName);
                }
            });
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigationHelper.GotoViewSelectedCar(carName);
                }
            });
        }

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

    //check if selected timings are according to Arlington Auto timings
    public boolean checkArlingtonTimings(int hour,int minute, int day)  {

        switch(day){
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                if((hour>=8 && hour<20)|| (hour==20 && minute==0))
                {
                    return true;
                }
                break;

            case 0:
                if((hour>=12 && hour<17)|| (hour==17 && minute==0))
                {
                    return true;
                }
                break;
            case 6:
                if((hour>=8 && hour<17)|| (hour==17 && minute==0))
                {
                    return true;
                }
                break;
            default:
                return false;

        }

        return false;
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