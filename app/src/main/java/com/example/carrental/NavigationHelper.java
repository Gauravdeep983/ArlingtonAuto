package com.example.carrental;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AppComponentFactory;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.carrental.UserJcode.MainActivityUser;
import com.example.carrental.UserJcode.viewmyprofile.U_ViewProfile;


public class NavigationHelper {



    public Context context;
    public NavigationHelper(Context cntx)
    {
        this.context = cntx;
    }
    public void logout(){

        Intent loginIntent = new Intent(context, RegistrationActivity.class);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out);

        context.startActivity(loginIntent, options.toBundle());

        }

    public void GotoHomeScreen(){

        Intent loginIntent = new Intent(context, MainActivityUser.class);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out);

        context.startActivity(loginIntent, options.toBundle());

    }

    public void GotoViewProfile()
    {
        Intent loginIntent = new Intent(context, U_ViewProfile.class);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out);

        context.startActivity(loginIntent, options.toBundle());
    }


}



