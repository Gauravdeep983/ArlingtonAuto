package com.example.carrental;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carrental.AdminJcode.MainActivityAdmin;
import com.example.carrental.RMJcode.MainActivityRM;
import com.example.carrental.UserJcode.MainActivityUser;

import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //this is my line where there is already something on git
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
    Spinner spinner;
    Switch membership;
    Button loginbutton;
    TextView registerlink;
    LinearLayout linearlayout;

    TableLayout maintable;
    Button registerbtn;

    //db related
    SharedPreferences sharedpreferences;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //db related
        mDBHelper = new DatabaseHelper(this);
        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        // clearing previous session
        sharedpreferences  = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor session = sharedpreferences.edit();
        session.clear().commit();

       /* try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }
*/
        // db end
        username = (EditText)findViewById(R.id.textboxUsername);
        password = (EditText)findViewById(R.id.textboxPassword);
        email = (EditText)findViewById(R.id.email);
        lastname = (EditText)findViewById(R.id.lastname);
        firstname = (EditText)findViewById(R.id.firstname);
        studentid = (EditText)findViewById(R.id.studentid);
        phonenumber = (EditText)findViewById(R.id.phonenumber);
        address = (EditText)findViewById(R.id.address);
        city = (EditText)findViewById(R.id.city);
        state = (EditText)findViewById(R.id.state);
        zipcode = (EditText)findViewById(R.id.zipcode);
        spinner = (Spinner) findViewById(R.id.role);
        membership = (Switch)findViewById(R.id.membership);
        linearlayout = (LinearLayout)findViewById(R.id.ll);
        loginbutton = (Button)findViewById(R.id.loginbutton);
        registerlink = (TextView)findViewById(R.id.registerbutton);


        maintable = (TableLayout)findViewById(R.id.maintable);
        registerbtn = (Button)findViewById(R.id.registerbtn);



        // Spinner element


        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("User");
        categories.add("Admin");
        categories.add("Rental Manager");

        // Creating adapter for spinner
        // ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,  R.layout.spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.dropdown);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        linearlayout.startAnimation(AnimationUtils.loadAnimation(this,R.anim.slide_up));

       //click on login button
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(loginbutton.getCurrentTextColor() == Color.parseColor("#D3D1D1"))
                {
                    loginbutton.setTextColor(Color.parseColor("#ffffff"));
                }
                else{
                    loginbutton.setTextColor(Color.parseColor("#D3D1D1"));
                }

                // 2 lines for closing the keyboard
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

                // entered uname and password is empty
                if (username.getText().toString().trim().equals("")) {
                    username.setError("Username is required!");
                } else if (password.getText().toString().trim().equals("")) {
                    password.setError("Password is required!");
                }

                // username and password not empty, validate the database results and redirect the user
                else {

                    //check with db
                    sharedpreferences  = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
                    Cursor cursor = mDb.rawQuery("select username FROM user", null);
                    if (cursor.getCount() > 0) {
                        String query = "Select * from user where username = '" + username.getText().toString().trim() + "' and password = '" + password.getText().toString().trim() + "'";
                        cursor = mDb.rawQuery(query, null);
                        if (cursor.getCount() <= 0) {
                            Toast.makeText(getApplicationContext(), "Invalid Username or Password", Toast.LENGTH_LONG).show();
                            username.setText("");
                            password.setText("");
                            cursor.close();
                        } else {
                            String data = "User";
                            if (cursor.moveToFirst()) {
                                data = cursor.getString(cursor.getColumnIndex("role"));
                            }
                            cursor.close();
                            //saving a session for a logged in user in the form of (key,value) pair (username, "")
                            SharedPreferences.Editor session = sharedpreferences.edit();
                            session.putString("username", username.getText().toString().trim());
                            session.putString("userType", data);
                            session.commit();

                            ForwardUsertoUI(data);


                        }
                    }
                }


            }

        });

        registerbtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(loginbutton.getCurrentTextColor() == Color.parseColor("#D3D1D1"))
                {
                    loginbutton.setTextColor(Color.parseColor("#ffffff"));
                }
                else{
                    loginbutton.setTextColor(Color.parseColor("#D3D1D1"));
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
                }
                // username and password not empty
                else {

                    //check with db
                    sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
                    Cursor cursor = mDb.rawQuery("select username FROM user", null);
                    if (cursor.getCount() > 0) {
                        String query = "Select * from user where username = '" + username.getText().toString().trim() + "'";
                        cursor = mDb.rawQuery(query, null);
                        if (cursor.getCount() > 0) {
                            Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_LONG).show();
                            username.setText("");

                            cursor.close();
                        } else {

 String insert_query = "insert into user (username, password, uta_id, last_name, first_name, phone, email, address, city, state, zip, role, club_membership, is_revoked) " +
         "values ('"+ username.getText().toString().trim() +"','"+password.getText().toString().trim()+"','"+studentid.getText().toString().trim()+"','" +
         lastname.getText().toString().trim()+"','"+firstname.getText().toString().trim()+"','"+phonenumber.getText().toString().trim()+"','"+email.getText().toString().trim()+"','" +
         address.getText().toString().trim()+"','"+city.getText().toString().trim()+"','"+state.getText().toString().trim()+"','"+zipcode.getText().toString().trim()+"','"+spinner.getSelectedItem().toString().trim()+"','"+
         getMembership() +"','0' ) ";
 System.out.println(insert_query);
                            mDb.execSQL(insert_query);
                            Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_LONG).show();
                            cursor.close();
                            String userType =  spinner.getSelectedItem().toString().trim();
                            //saving a session for a logged in user in the form of (key,value) pair (username, "")
                            SharedPreferences.Editor session = sharedpreferences.edit();
                            session.putString("username", username.getText().toString().trim());
                            session.putString("userType", userType);
                            session.commit();

                            ForwardUsertoUI(userType);
                        }
                    }
                }

                registerbtn.setTextColor(Color.parseColor("#D3D1D1"));
            }
        });

        linearlayout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);

            }
        } );

        maintable.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);

            }
        } );


        registerlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerlink.setTextSize(16);
                registerbtn.setVisibility(View.VISIBLE);
                registerlink.setVisibility(View.INVISIBLE);
               TableRow tr3 =  (TableRow)findViewById(R.id.row3);
               tr3.setVisibility(View.VISIBLE);

                TableRow tr4 =  (TableRow)findViewById(R.id.row4);
                tr4.setVisibility(View.VISIBLE);

                TableRow tr5 =  (TableRow)findViewById(R.id.row5);
                tr5.setVisibility(View.VISIBLE);

                TableRow tr6 =  (TableRow)findViewById(R.id.row6);
                tr6.setVisibility(View.VISIBLE);

                TableRow tr7 =  (TableRow)findViewById(R.id.row7);
                tr7.setVisibility(View.VISIBLE);

                TableRow tr8 =  (TableRow)findViewById(R.id.row8);
                tr8.setVisibility(View.VISIBLE);
            }
        });
 } //end of Oncreate

    private void ForwardUsertoUI(String data) {
        loginbutton.setBackgroundColor(Color.parseColor("#607d8b"));
        if (data.equalsIgnoreCase("User")) {
            Intent loginIntent = new Intent(RegistrationActivity.this, MainActivityUser.class);
            ActivityOptions options =
                    ActivityOptions.makeCustomAnimation(RegistrationActivity.this, android.R.anim.fade_in, android.R.anim.fade_out);

            startActivity(loginIntent, options.toBundle());
        }
        else if (data.equalsIgnoreCase("Rental Manager")) {
            Intent loginIntent = new Intent(RegistrationActivity.this, MainActivityRM.class);
            ActivityOptions options =
                    ActivityOptions.makeCustomAnimation(RegistrationActivity.this, android.R.anim.fade_in, android.R.anim.fade_out);

            startActivity(loginIntent, options.toBundle());  }

        else if (data.equalsIgnoreCase("Admin"))
        {
            Intent loginIntent = new Intent(RegistrationActivity.this, MainActivityAdmin.class);
            ActivityOptions options =
                    ActivityOptions.makeCustomAnimation(RegistrationActivity.this, android.R.anim.fade_in, android.R.anim.fade_out);

            startActivity(loginIntent, options.toBundle());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        if(!item.equals("User"))
        {
        membership.setEnabled(false);
        }
        else
        {
            membership.setEnabled(true);
        }


    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
        finish();

    }

    public String getMembership() {
        return membership.isChecked()?"1":"0";
    }
}