package com.example.carrental;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionHelper {

    public SharedPreferences prefs;
    SharedPreferences.Editor session;
    public SessionHelper(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = cntx.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        session = prefs.edit();
    }

    public String getloggedInUsername() {
        String username = prefs.getString("username","");
        return username;
    }
    public String getloggedInUserType() {
        String userType = prefs.getString("userType","");
        return userType;
    }

    public void setSessionUsername(String uname)
    {
        session.putString("username", uname);
        session.commit();
    }

    public void setSessionUserType(String utype)
    {
        session.putString("userType", utype);
        session.commit();
    }


}
