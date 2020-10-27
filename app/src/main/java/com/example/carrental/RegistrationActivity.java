package com.example.carrental;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carrental.AdminJcode.MainActivityAdmin;
import com.example.carrental.RMJcode.MainActivityRM;
import com.example.carrental.UserJcode.MainActivityUser;

import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //Comment, test from xyz
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
    TableRow tr3;
    TableRow tr4;
    TableRow tr5;
    TableRow tr6;
    TableRow tr7;
    TableRow tr8;
    Boolean pswdvisible = false;


    //db related
    SharedPreferences sharedpreferences;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;


    @SuppressLint("ClickableViewAccessibility")
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
        sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor session = sharedpreferences.edit();
        session.clear().apply();

       /* try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }
*/
        // db end
        username = (EditText) findViewById(R.id.textboxUsername);
        password = (EditText) findViewById(R.id.textboxPassword);
        email = (EditText) findViewById(R.id.email);
        lastname = (EditText) findViewById(R.id.lastname);
        firstname = (EditText) findViewById(R.id.firstname);
        studentid = (EditText) findViewById(R.id.studentid);
        phonenumber = (EditText) findViewById(R.id.phonenumber);
        address = (EditText) findViewById(R.id.address);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        zipcode = (EditText) findViewById(R.id.zipcode);
        spinner = (Spinner) findViewById(R.id.role);
        membership = (Switch) findViewById(R.id.membership);
        linearlayout = (LinearLayout) findViewById(R.id.ll);
        loginbutton = (Button) findViewById(R.id.loginbutton);
        registerlink = (TextView) findViewById(R.id.registerbutton);
        pswdvisible = false;

        maintable = (TableLayout) findViewById(R.id.maintable);
        registerbtn = (Button) findViewById(R.id.registerbtn);
        tr3 = (TableRow) findViewById(R.id.row3);
        tr4 = (TableRow) findViewById(R.id.row4);
        tr5 = (TableRow) findViewById(R.id.row5);
        tr6 = (TableRow) findViewById(R.id.row6);
        tr7 = (TableRow) findViewById(R.id.row7);
        tr8 = (TableRow) findViewById(R.id.row8);

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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.dropdown);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        linearlayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up));

        //click on login button
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (loginbutton.getCurrentTextColor() == Color.parseColor("#D3D1D1")) {
                    loginbutton.setTextColor(Color.parseColor("#ffffff"));
                } else {
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
                    sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
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
                            boolean isMember = false;
                            boolean isRevoked = false;
                            String membershipStatus;
                            String revokeStatus;
                            if (cursor.moveToFirst()) {
                                data = cursor.getString(cursor.getColumnIndex("role"));
                                membershipStatus = cursor.getString(cursor.getColumnIndex("club_membership"));
                                revokeStatus = cursor.getString(cursor.getColumnIndex("is_revoked"));
                                if (membershipStatus.equals("true")) {
                                    isMember = true;
                                }
                                if (revokeStatus.equalsIgnoreCase("true")) {
                                    isRevoked = true;
                                }
                            }
                            cursor.close();
                            //saving a session for a logged in user in the form of (key,value) pair (username, "")
                            SharedPreferences.Editor session = sharedpreferences.edit();
                            session.putString("username", username.getText().toString().trim());
                            session.putString("userType", data);
                            session.putString("membershipStatus", Boolean.toString(isMember));
                            session.putString("revokeStatus", Boolean.toString(isRevoked));
                            session.apply();

                            ForwardUsertoUI(data);


                        }
                    }
                }


            }

        });

        registerbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (registerbtn.getCurrentTextColor() == Color.parseColor("#D3D1D1")) {
                    registerbtn.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    registerbtn.setTextColor(Color.parseColor("#D3D1D1"));
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
                    if (membership.isChecked()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                        builder.setMessage("You have selected Club Membership");
                        builder.setTitle("Enter the valid code");
                        builder.setIcon(R.drawable.passkey);
                        builder.setCancelable(true);
                        final EditText input = new EditText(RegistrationActivity.this);
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        builder.setView(input);

                        builder.setPositiveButton("Validate", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (input.getText().toString().equalsIgnoreCase("1234")) {
                                    Toast.makeText(getApplicationContext(), "Membership activated", Toast.LENGTH_SHORT).show();
                                    InsertUserinDB();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Invalid code entered", Toast.LENGTH_SHORT).show();
                                    membership.setChecked(false);
                                }

                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                membership.setChecked(false);
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    } else {
                        InsertUserinDB();
                    }

                }

                registerbtn.setTextColor(Color.parseColor("#D3D1D1"));
            }
        });

        linearlayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

            }
        });

        maintable.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

            }
        });

        membership.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(membership.getRootView().getApplicationWindowToken(), 0);

            }
        });

        registerlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerlink.setTextSize(16);
                registerbtn.setVisibility(View.VISIBLE);
                registerlink.setVisibility(View.INVISIBLE);
                tr3.setVisibility(View.VISIBLE);
                tr4.setVisibility(View.VISIBLE);
                tr5.setVisibility(View.VISIBLE);
                tr6.setVisibility(View.VISIBLE);
                tr7.setVisibility(View.VISIBLE);
                tr8.setVisibility(View.VISIBLE);
            }
        });

        password.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (password.getRight() - password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if (pswdvisible.equals(false)) {
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            pswdvisible = true;
                        } else {
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            pswdvisible = false;
                        }
                        return true;
                    }
                }
                return false;
            }
        });

    } //end of Oncreate

    private void InsertUserinDB() {
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
                        "values ('" + username.getText().toString().trim() + "','" + password.getText().toString().trim() + "','" + studentid.getText().toString().trim() + "','" +
                        lastname.getText().toString().trim() + "','" + firstname.getText().toString().trim() + "','" + phonenumber.getText().toString().trim() + "','" + email.getText().toString().trim() + "','" +
                        address.getText().toString().trim() + "','" + city.getText().toString().trim() + "','" + state.getText().toString().trim() + "','" + zipcode.getText().toString().trim() + "','" + spinner.getSelectedItem().toString().trim() + "','" +
                        getMembership() + "','false' ) ";
                System.out.println(insert_query);
                mDb.execSQL(insert_query);
                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_LONG).show();
                cursor.close();

                tr3.setVisibility(View.GONE);
                tr4.setVisibility(View.GONE);
                tr5.setVisibility(View.GONE);
                tr6.setVisibility(View.GONE);
                tr7.setVisibility(View.GONE);
                tr8.setVisibility(View.GONE);
                //ForwardUsertoUI(userType);
            }
        }
    }


    private void ForwardUsertoUI(String data) {
        loginbutton.setBackgroundColor(Color.parseColor("#607d8b"));
        if (data.equalsIgnoreCase("User")) {
            Intent loginIntent = new Intent(RegistrationActivity.this, MainActivityUser.class);
            ActivityOptions options =
                    ActivityOptions.makeCustomAnimation(RegistrationActivity.this, android.R.anim.fade_in, android.R.anim.fade_out);

            startActivity(loginIntent, options.toBundle());
        } else if (data.equalsIgnoreCase("Rental Manager")) {
            Intent loginIntent = new Intent(RegistrationActivity.this, MainActivityRM.class);
            ActivityOptions options =
                    ActivityOptions.makeCustomAnimation(RegistrationActivity.this, android.R.anim.fade_in, android.R.anim.fade_out);

            startActivity(loginIntent, options.toBundle());
        } else if (data.equalsIgnoreCase("Admin")) {
            Intent loginIntent = new Intent(RegistrationActivity.this, MainActivityAdmin.class);
            ActivityOptions options =
                    ActivityOptions.makeCustomAnimation(RegistrationActivity.this, android.R.anim.fade_in, android.R.anim.fade_out);

            startActivity(loginIntent, options.toBundle());
        }
    }

    //when user selects the role, hide the membership accordingly and hide the keyboard
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(membership.getRootView().getApplicationWindowToken(), 0);
        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        if (!item.equals("User")) {
            membership.setEnabled(false);
        } else {
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
        return membership.isChecked() ? "true" : "false";
    }
}