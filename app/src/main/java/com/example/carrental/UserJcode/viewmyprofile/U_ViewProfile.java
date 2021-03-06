package com.example.carrental.UserJcode.viewmyprofile;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carrental.DatabaseHelper;
import com.example.carrental.NavigationHelper;
import com.example.carrental.R;
import com.example.carrental.RegistrationActivity;
import com.example.carrental.SessionHelper;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class U_ViewProfile extends AppCompatActivity {
    Spinner spinner;
    Switch membership;
    EditText username;
    EditText password;
    EditText email;
    EditText lastname;
    EditText firstname;
    EditText studentid;
    EditText phonenumber;
    EditText address;
    EditText city;
    EditText state;
    EditText zipcode;
    Button updatebutton;
    Button ignorebutton;
    LinearLayout linearlayout;
    TableLayout newmaintable;
    ImageButton backbutton;
    ImageButton homebutton;
    ImageButton logoutbutton;
    public SessionHelper session;
    public NavigationHelper navigationHelper;

    //db related

    SharedPreferences sharedpreferences;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    String sessionUsername = null;
    String userType= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewmyprofile);

        session = new SessionHelper(this);
        navigationHelper = new NavigationHelper(U_ViewProfile.this);

        sessionUsername = session.getloggedInUsername();
        userType = session.getloggedInUserType();

        //db related
        mDBHelper = new DatabaseHelper(this);
        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        username = (EditText) findViewById(R.id.newusername);
        password = (EditText) findViewById(R.id.newpassword);
        email = (EditText) findViewById(R.id.newemail);
        lastname = (EditText) findViewById(R.id.newlastname);
        firstname = (EditText) findViewById(R.id.newfirstname);
        studentid = (EditText) findViewById(R.id.newstudentid);
        phonenumber = (EditText) findViewById(R.id.newphonenumber);
        address = (EditText) findViewById(R.id.newaddress);
        city = (EditText) findViewById(R.id.newcity);
        state = (EditText) findViewById(R.id.newstate);
        zipcode = (EditText) findViewById(R.id.newzipcode);
        spinner = (Spinner) findViewById(R.id.newrole);
        membership = (Switch) findViewById(R.id.newmembership);
        updatebutton = (Button) findViewById(R.id.updateprofilebutton);
        ignorebutton = (Button) findViewById(R.id.ignorechanges);
        linearlayout = (LinearLayout) findViewById(R.id.llviewprofile);
        newmaintable = (TableLayout) findViewById(R.id.newmaintable);
        backbutton = (ImageButton)findViewById(R.id.backbutton);
        homebutton = (ImageButton)findViewById(R.id.homebutton);
        logoutbutton = (ImageButton)findViewById(R.id.logoutbutton);
        homebutton.setVisibility(View.INVISIBLE);

        //spinner code

        //spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("User");
        categories.add("Admin");
        categories.add("Rental Manager");

        // Creating adapter for spinner
        // ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_blacktext, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.dropdown);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        newmaintable.setVisibility(View.VISIBLE);
        newmaintable.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up));
        updatebutton.setVisibility(View.VISIBLE);
        updatebutton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_left));
        ignorebutton.setVisibility(View.VISIBLE);
        ignorebutton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_right));
        spinner.setEnabled(false);
        if (userType.equalsIgnoreCase("user")) {
            spinner.setSelection(((ArrayAdapter<String>) spinner.getAdapter()).getPosition("User"));
        }
        else if (userType.equalsIgnoreCase("rental manager")) {
            spinner.setSelection(((ArrayAdapter<String>) spinner.getAdapter()).getPosition("Rental Manager"));
            membership.setEnabled(false);
            membership.setVisibility(View.GONE);
        }
        else {
            spinner.setSelection(((ArrayAdapter<String>) spinner.getAdapter()).getPosition("Admin"));
            membership.setEnabled(true);
            //disabling everything because admin cannot change these inputs
            username.setEnabled(false);
            password.setEnabled(false);
            studentid.setEnabled(false);
            lastname.setEnabled(false);
            firstname.setEnabled(false);
            address.setEnabled(false);
            phonenumber.setEnabled(false);
            email.setEnabled(false);
            city.setEnabled(false);
            state.setEnabled(false);
            zipcode.setEnabled(false);

            membership.setVisibility(View.GONE);
            updatebutton.setVisibility(View.GONE);
            ignorebutton.setVisibility(View.GONE);
        }

        //linear layout click event
        linearlayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

            }
        });


        //fetch initial data from db
        GetUserDetailsFromDb(sessionUsername);
        //ignore button clicked
        ignorebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ignorebutton.getCurrentTextColor() == Color.parseColor("#020202")) {
                    ignorebutton.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    ignorebutton.setTextColor(Color.parseColor("#020202"));
                }
                GetUserDetailsFromDb(sessionUsername);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
        });

        //update profile click listener
        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updatebutton.getCurrentTextColor() == Color.parseColor("#020202")) {
                    updatebutton.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    updatebutton.setTextColor(Color.parseColor("#020202"));
                }
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

                if (username.getText().toString().trim().equals("")) {
                    username.setError("Username is required!");
                } else if (password.getText().toString().trim().equals("")) {
                    password.setError("Password is required!");
                } else if (email.getText().toString().trim().equals("")) {
                    email.setError("email is required!");
                } else if (lastname.getText().toString().trim().equals("")) {
                    lastname.setError("lastname is required!");
                } else if (firstname.getText().toString().trim().equals("")) {
                    firstname.setError("firstname is required!");
                } else if (studentid.getText().toString().trim().equals("")) {
                    studentid.setError("studentid is required!");
                } else if (phonenumber.getText().toString().trim().equals("")) {
                    phonenumber.setError("phonenumber is required!");
                } else if (address.getText().toString().trim().equals("")) {
                    address.setError("address is required!");
                } else if (city.getText().toString().trim().equals("")) {
                    city.setError("city is required!");
                } else if (state.getText().toString().trim().equals("")) {
                    state.setError("state is required!");
                } else if (zipcode.getText().toString().trim().equals("")) {
                    zipcode.setError("zipcode is required!");
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(U_ViewProfile.this);
                    builder.setMessage("Do you want to update ?");
                    builder.setTitle("Are you sure ?");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,int which)
                        {
                            UpdateProfileinDB(sessionUsername);
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
            }
        });


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


    }
    private void UpdateProfileinDB(String sUsername) {

        ContentValues cv = new ContentValues();
        cv.put("username",username.getText().toString().trim()); //These Fields should be your String values of actual column names
        cv.put("password",password.getText().toString().trim());
        cv.put("uta_id",studentid.getText().toString().trim());
        cv.put("last_name", lastname.getText().toString().trim());
        cv.put("first_name",firstname.getText().toString().trim());
        cv.put("phone",phonenumber.getText().toString().trim());
        cv.put("email",email.getText().toString().trim());
        cv.put("address",address.getText().toString().trim());
        cv.put("city",city.getText().toString().trim());
        cv.put("state",state.getText().toString().trim());
        cv.put("zip",zipcode.getText().toString().trim());
        cv.put("role",spinner.getSelectedItem().toString().trim());
        cv.put("club_membership", getMembership());   //1 - true, 0 -false
        //cv.put("is_revoked", "false");


        mDb.update("user", cv, "username='"+sUsername+"'", null);


        //Toast.makeText(getActivity(), "Account Details Updated", Toast.LENGTH_LONG).show();
        Snackbar.make(findViewById(android.R.id.content), "Account Details Updated", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        GetUserDetailsFromDb(username.getText().toString().trim());
        //saving a session for a logged in user in the form of (key,value) pair (username, "")

        session.setSessionUsername(username.getText().toString().trim());
        session.setSessionUserType(spinner.getSelectedItem().toString().trim());

        sessionUsername= session.getloggedInUsername();
        userType= session.getloggedInUserType();
    }

    //fetch initial data from db
    public void GetUserDetailsFromDb(String sessionUsername) {
        Cursor cursor = mDb.rawQuery("select username FROM user", null);
        if (cursor.getCount() > 0) {
            String query = "Select * from user where username = '" + sessionUsername.toString().trim()+"'";
            cursor = mDb.rawQuery(query, null);
            if (cursor.getCount() <= 0) {
                Intent loginIntent = new Intent(this, RegistrationActivity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(loginIntent, options.toBundle());
                cursor.close();
            } else {

                if (cursor.moveToFirst()) {
                    username.setText(cursor.getString(cursor.getColumnIndex("username")));
                    password.setText(cursor.getString(cursor.getColumnIndex("password")));
                    studentid.setText(cursor.getString(cursor.getColumnIndex("uta_id")));
                    lastname.setText(cursor.getString(cursor.getColumnIndex("last_name")));
                    firstname.setText(cursor.getString(cursor.getColumnIndex("first_name")));
                    address.setText(cursor.getString(cursor.getColumnIndex("address")));
                    phonenumber.setText(cursor.getString(cursor.getColumnIndex("phone")));
                    email.setText(cursor.getString(cursor.getColumnIndex("email")));
                    city.setText(cursor.getString(cursor.getColumnIndex("city")));
                    state.setText(cursor.getString(cursor.getColumnIndex("state")));
                    zipcode.setText(cursor.getString(cursor.getColumnIndex("zip")));
                    spinner.setSelection(((ArrayAdapter<String>)spinner.getAdapter()).getPosition(cursor.getString(cursor.getColumnIndex("role"))));
                    membership.setChecked("true".equals(cursor.getString(cursor.getColumnIndex("club_membership"))));

                }
                cursor.close();

            }
        }

    }
    public String getMembership() {
        return membership.isChecked()?"true":"false";
    }

}
