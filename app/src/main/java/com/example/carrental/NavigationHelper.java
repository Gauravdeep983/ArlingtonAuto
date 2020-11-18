package com.example.carrental;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.carrental.AdminJcode.Admin_searchallusers;
import com.example.carrental.AdminJcode.MainActivityAdmin;
import com.example.carrental.AdminJcode.ViewUser;
import com.example.carrental.RMJcode.MainActivityRM;
import com.example.carrental.RMJcode.RM_ReservationSummary;
import com.example.carrental.RMJcode.Viewselectedcar;
import com.example.carrental.UserJcode.MainActivityUser;
import com.example.carrental.UserJcode.ReservationSummary;
import com.example.carrental.UserJcode.SearchVehicle;
import com.example.carrental.UserJcode.ViewSelectedVehicle;
import com.example.carrental.UserJcode.viewmyprofile.U_ViewProfile;

import java.io.Serializable;
import java.util.ArrayList;


public class NavigationHelper {

    public Context context;

    public NavigationHelper(Context cntx) {
        this.context = cntx;
    }

    public void logout() {

        Intent loginIntent = new Intent(context, RegistrationActivity.class);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out);

        context.startActivity(loginIntent, options.toBundle());

    }

    public void GotoHomeScreen(String userType) {
        if (userType.equalsIgnoreCase("User")) {
            Intent loginIntent = new Intent(context, MainActivityUser.class);
            ActivityOptions options =
                    ActivityOptions.makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out);

            context.startActivity(loginIntent, options.toBundle());
        } else if (userType.equalsIgnoreCase("Rental Manager")) {
            Intent loginIntent = new Intent(context, MainActivityRM.class);
            ActivityOptions options =
                    ActivityOptions.makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out);

            context.startActivity(loginIntent, options.toBundle());
        } else if (userType.equalsIgnoreCase("Admin")) {
            Intent loginIntent = new Intent(context, MainActivityAdmin.class);
            ActivityOptions options =
                    ActivityOptions.makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out);

            context.startActivity(loginIntent, options.toBundle());
        }


    }

   public void GotoSearchVehicleScreen()
    {
        Intent searchVehicleIntent = new Intent(context, SearchVehicle.class);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out);

        context.startActivity(searchVehicleIntent, options.toBundle());
    }

    public void GotoViewProfile() {
        Intent loginIntent = new Intent(context, U_ViewProfile.class);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out);

        context.startActivity(loginIntent, options.toBundle());
    }
    public void GotoUserProfile(String username) {
        Intent loginIntent = new Intent(context, ViewUser.class);
        loginIntent.putExtra("username",username);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out);

        context.startActivity(loginIntent, options.toBundle());
    }



    public void GotoSearchallUsers() {
        Intent loginIntent = new Intent(context, Admin_searchallusers.class);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out);

        context.startActivity(loginIntent, options.toBundle());
    }

    public void GoToReservationDetails(ArrayList<String> carDetails, ArrayList<String> userInputs, double baseCost, int noOfDays) {
        Intent i = new Intent(context, ViewSelectedVehicle.class);
        Bundle args = new Bundle();
        args.putSerializable("car", (Serializable) carDetails);
        args.putSerializable("userInputs", userInputs);
        i.putExtra("args", args);
        i.putExtra("noOfDays", noOfDays);
        i.putExtra("baseCost", baseCost);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out);

        context.startActivity(i, options.toBundle());
    }


    public void GotoViewSelectedCar(String carname)
    {
        Intent i = new Intent(context, Viewselectedcar.class);
        i.putExtra("carname", carname);
        ActivityOptions options = ActivityOptions.makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out);
        context.startActivity(i, options.toBundle());
    }

    public void GotoReservationSummary(String reservationnumber)
    {
        Intent i = new Intent(context, ReservationSummary.class);
        i.putExtra("reservationnumber", reservationnumber);
        ActivityOptions options = ActivityOptions.makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out);
        context.startActivity(i, options.toBundle());
    }

    public void GotoReservationRM(String reservationnumber)
    {
        Intent i = new Intent(context, RM_ReservationSummary.class);
        i.putExtra("reservationnumber", reservationnumber);
        ActivityOptions options = ActivityOptions.makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out);
        context.startActivity(i, options.toBundle());
    }

}



