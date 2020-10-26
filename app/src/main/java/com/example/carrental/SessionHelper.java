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
        String username = prefs.getString("username", "");
        return username;
    }

    public String getloggedInUserType() {
        String userType = prefs.getString("userType", "");
        return userType;
    }

    public void setSessionUsername(String uname) {
        session.putString("username", uname);
        session.commit();
    }

    public void setSessionUserType(String utype) {
        session.putString("userType", utype);
        session.commit();
    }

    public void setMembershipStatus(Boolean isMember) {
        session.putString("membershipStatus", isMember.toString());
        session.commit();
    }
    public String getMembershipStatus() {
        String userType = prefs.getString("membershipStatus", "");
        return userType;
    }

    public void setRevokeStatus(Boolean isRevoked) {
        session.putString("revokeStatus", isRevoked.toString());
        session.commit();
    }
    public String getRevokeStatus() {
        String isRevoked = prefs.getString("revokeStatus", "");
        return isRevoked;
    }


}
