package com.example.carrental.RMJcode;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.carrental.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CarDbOperations {



    public Context context;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    public CarDbOperations(Context cntx) {
        this.context = cntx;
        //db related
        mDBHelper = new DatabaseHelper(context);
        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (
                SQLException mSQLException) {
            throw mSQLException;
        }
    }


    //method to search for a user in DB, return a listoflist [[userType,userSummary,username],[userType,userSummary,username]]
    public List<ArrayList<String>> SearchAllcars(String carname) {
             return FindcarsinDB(carname);
    }


    public List<ArrayList<String>> FindcarsinDB(String identifier) {
        List<ArrayList<String>> Allcars = new ArrayList<ArrayList<String>>();
        Allcars.clear();
        Cursor cursor = mDb.rawQuery("select car_name FROM car", null);
        if (cursor.getCount() > 0) {
            String query = "Select * from car where car_name like '%" + identifier + "%' order by car_name ASC ";
            cursor = mDb.rawQuery(query, null);
            if (cursor.getCount() <= 0) {
                //no user
                cursor.close();

            } else {
                cursor.moveToFirst();
                try {
                    do {
                        ArrayList<String> eachcar = new ArrayList<String>();
                        eachcar.add(cursor.getString(cursor.getColumnIndex("car_name")));
                        eachcar.add("CAR NAME - "+cursor.getString(cursor.getColumnIndex("car_name"))+"\nCAR NUMBER - "+
                                cursor.getString(cursor.getColumnIndex("car_number")) + "\nCAR CAPACITY - " +
                                cursor.getString(cursor.getColumnIndex("capacity")) + "\nWEEKDAY RATE - $" +
                                cursor.getString(cursor.getColumnIndex("weekday_rate")) + "\nGPS RATE - $" +
                                cursor.getString(cursor.getColumnIndex("gps_rate")));
                        Allcars.add(eachcar);
                    }
                    while (cursor.moveToNext());
                } finally {
                    cursor.close();
                }
            }
        }
        return Allcars;
    }



    public ArrayList<String> GetcardetailsfromDB(String carname) {
        ArrayList<String> eachcar = new ArrayList<String>();
        Cursor cursor = mDb.rawQuery("select car_name FROM car", null);
        if (cursor.getCount() > 0) {
            String query = "Select * from car where car_name = '"+carname+"'";
            cursor = mDb.rawQuery(query, null);
            if (cursor.getCount() <= 0) {
                //no user
                cursor.close();

            } else {
                cursor.moveToFirst();
                try {
                    do {
                            eachcar.add(cursor.getString(cursor.getColumnIndex("car_number")));
                            eachcar.add(cursor.getString(cursor.getColumnIndex("car_name")));
                            eachcar.add(cursor.getString(cursor.getColumnIndex("capacity")));
                            eachcar.add("$"+cursor.getString(cursor.getColumnIndex("weekday_rate")));
                            eachcar.add("$"+cursor.getString(cursor.getColumnIndex("weekend_rate")));
                            eachcar.add("$"+cursor.getString(cursor.getColumnIndex("weekly_rate")));
                            eachcar.add("$"+cursor.getString(cursor.getColumnIndex("gps_rate")));
                            eachcar.add("$"+cursor.getString(cursor.getColumnIndex("onstar_rate")));
                            eachcar.add("$"+cursor.getString(cursor.getColumnIndex("siriusxm_rate")));
                    }
                    while (cursor.moveToNext());
                } finally {
                    cursor.close();
                }
            }
        }
        return eachcar;
    }

    public ArrayList<String> GetreservationdetailsfromDB(String reserveID) {
        ArrayList<String> eachcar = new ArrayList<String>();
        Cursor cursor;
            String query = "Select * from car_reservation where reservation_number = '"+reserveID+"'";
            cursor = mDb.rawQuery(query, null);
            if (cursor.getCount() <= 0) {
                //no user
                cursor.close();

            } else {
                cursor.moveToFirst();
                try {
                    do {
                        eachcar.add(cursor.getString(cursor.getColumnIndex("reservation_number")));
                        eachcar.add(cursor.getString(cursor.getColumnIndex("car_name")));
                        eachcar.add(cursor.getString(cursor.getColumnIndex("occupant_capacity")));
                        eachcar.add(cursor.getString(cursor.getColumnIndex("start_date")));
                        eachcar.add(cursor.getString(cursor.getColumnIndex("end_date")));
                        eachcar.add(cursor.getString(cursor.getColumnIndex("gps_selected")));
                        eachcar.add(cursor.getString(cursor.getColumnIndex("onstar_selected")));
                        eachcar.add(cursor.getString(cursor.getColumnIndex("siriusxm_selected")));
                        eachcar.add(cursor.getString(cursor.getColumnIndex("total_cost")));
                    }
                    while (cursor.moveToNext());
                } finally {
                    cursor.close();
                }
            }

        return eachcar;
    }
}
