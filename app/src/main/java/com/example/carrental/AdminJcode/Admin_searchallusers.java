package com.example.carrental.AdminJcode;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carrental.NavigationHelper;
import com.example.carrental.R;
import com.example.carrental.SessionHelper;
import java.util.ArrayList;
import java.util.List;

public class Admin_searchallusers extends AppCompatActivity {


    ImageButton backbutton;
    ImageButton logoutbutton;
    LinearLayout scrollList;
    EditText lastname;
    Button searchuserbutton;
    ScrollView scroller;
    public SessionHelper session;
    public NavigationHelper navigationHelper;
    public UserDbOperations userDbOperations;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_searchallusers);

        session = new SessionHelper(this);
        navigationHelper = new NavigationHelper(Admin_searchallusers.this);
        userDbOperations = new UserDbOperations(Admin_searchallusers.this);
        backbutton = (ImageButton)findViewById(R.id.backbutton);
        logoutbutton = (ImageButton)findViewById(R.id.logoutbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationHelper.logout();
            }
        });
        scroller = (ScrollView)findViewById(R.id.scroller);
        lastname = (EditText)findViewById(R.id.lastname);
        searchuserbutton = (Button)findViewById(R.id.searchuserbutton);
        scrollList = (LinearLayout)findViewById(R.id.scrollList);

        UpdateUserList();
        searchuserbutton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                //getting details from db
                 UpdateUserList();
            }
        });

        //start searching the user when the text is being written
        lastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                UpdateUserList();
            }
        });
        //closing the keyboard when user is scrolling the list
        scroller.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(scroller.getWindowToken(),0);

            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void UpdateUserList() {

        //clearing whatever is in scroll list first
        scrollList.removeAllViewsInLayout();

        List<ArrayList<String>> allusers =  userDbOperations.SearchAllUser(lastname.getText().toString().trim());

        for (final ArrayList<String> user : allusers) {
            String userRole = user.get(0);
            String userSummary = user.get(1);
            final String username = user.get(2);

            LinearLayout linearItem = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(dpToPx(5),dpToPx(5),dpToPx(5),dpToPx(0));
            linearItem.setLayoutParams(params);
            linearItem.setOrientation(LinearLayout.HORIZONTAL);
            linearItem.setBackgroundResource(R.drawable.rounded_edge);
            linearItem.setClickable(true);
            linearItem.setFocusable(true);
            linearItem.setId(View.generateViewId());
            //properties for Imageview
            ImageView userpic = new ImageView(this);
            LinearLayout.LayoutParams Imageviewparams = new LinearLayout.LayoutParams(dpToPx(50),dpToPx(50));
            Imageviewparams.setMargins(dpToPx(10),dpToPx(10),dpToPx(10),dpToPx(10));
            Imageviewparams.gravity = Gravity.CENTER;
            userpic.setLayoutParams(Imageviewparams);
            if(userRole.equalsIgnoreCase("Admin")){
                userpic.setBackgroundResource(R.mipmap.adminpic);
            }
            else if(userRole.equalsIgnoreCase("Rental Manager")){
                userpic.setBackgroundResource(R.drawable.personincircle);
            }
            else {
                userpic.setBackgroundResource(R.drawable.personinorange);
            }
            userpic.setContentDescription("UserImage");
            linearItem.addView(userpic);

            //properties for TextView
            TextView userSummaryTextObject = new TextView(this);
            LinearLayout.LayoutParams txtparms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.7f);
            txtparms.setMargins(dpToPx(10),dpToPx(10),dpToPx(10),dpToPx(10));
            userSummaryTextObject.setLayoutParams(txtparms);
            userSummaryTextObject.setText(userSummary.toString().trim());
            userSummaryTextObject.setTextSize(15);
            userSummaryTextObject.setTypeface(Typeface.create("sans-serif-medium",Typeface.NORMAL));
            linearItem.addView(userSummaryTextObject);

            //properties for Button
            Button btn = new Button(this);
            LinearLayout.LayoutParams btnparams = new LinearLayout.LayoutParams(dpToPx(30),dpToPx(30));
            btnparams.setMargins(dpToPx(0),dpToPx(0),dpToPx(30),dpToPx(0));
            btnparams.gravity = Gravity.CENTER;
            btn.setLayoutParams(btnparams);
            btn.setBackgroundResource(R.drawable.backbuttongrey);
            btn.setRotation(180f);
            linearItem.addView(btn);
            btn.setVisibility(View.INVISIBLE);
            if(!userRole.equalsIgnoreCase("Admin")) {
                btn.setVisibility(View.VISIBLE);
                //we'll add what action needs to be done when user clicks on one of these items
                linearItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navigationHelper.GotoUserProfile(username);

                    }
                });

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //route the user to a specific screen and pass the username as parameter. Ex: GoToUserProfile(username);
                        navigationHelper.GotoUserProfile(username);

                    }
                });
            }
            //adding the linearitem to main linear or scroll view
            scrollList.addView(linearItem);
        }

    }
//method used to change dp to pixel. since android takes only pixels
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}