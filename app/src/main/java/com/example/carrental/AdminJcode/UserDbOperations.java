package com.example.carrental.AdminJcode;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.carrental.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class UserDbOperations {

    public Context context;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    public UserDbOperations(Context cntx) {
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
    public List<ArrayList<String>> SearchAllUser(String lastname) {

       /* List<ArrayList<String>> Allusers = new ArrayList<ArrayList<String>>();
        for(int i=0;i<10;i++) {
            ArrayList<String> eachuser = new ArrayList<String>();
            eachuser.add("Admin");
            eachuser.add("oxk5567\n4699200738\nOmkar Kyatham\nkyathamomkar@gmail.com");
            eachuser.add("oxk5567"+i);
            Allusers.add(eachuser);
        }  */
        return FindUsersinDB(lastname);
    }


    public List<ArrayList<String>> FindUsersinDB(String identifier) {
        List<ArrayList<String>> Allusers = new ArrayList<ArrayList<String>>();
        Allusers.clear();
        Cursor cursor = mDb.rawQuery("select username FROM user", null);
        if (cursor.getCount() > 0) {
            String query = "Select * from user where last_name like '%" + identifier + "%' order by last_name asc, first_name asc";
            cursor = mDb.rawQuery(query, null);
            if (cursor.getCount() <= 0) {
                //no user
                cursor.close();

            } else {
                cursor.moveToFirst();
                try {
                    do {
                        ArrayList<String> eachuser = new ArrayList<String>();
                        eachuser.add(cursor.getString(cursor.getColumnIndex("role")));
                        eachuser.add("USERNAME - "+cursor.getString(cursor.getColumnIndex("username")) + "\nUTA ID - " +
                                cursor.getString(cursor.getColumnIndex("uta_id")) + "\nNAME - " +
                                cursor.getString(cursor.getColumnIndex("first_name")) + " " +
                                cursor.getString(cursor.getColumnIndex("last_name")) + "\nEMAIL - " +
                                cursor.getString(cursor.getColumnIndex("email")));
                        eachuser.add(cursor.getString(cursor.getColumnIndex("username")));
                        Allusers.add(eachuser);
                    }
                    while (cursor.moveToNext());
                } finally {
                    cursor.close();
                }
            }
        }
        return Allusers;
    }

    public List<ArrayList<String>> SearchVehicles(int capacity, String startDate, String endDate) {
        String sql = "SELECT * FROM car " +
                "WHERE car_name NOT IN (SELECT car_name FROM car_reservation WHERE '"+startDate+"' BETWEEN start_date AND end_date OR '"+ endDate+"' BETWEEN start_date AND end_date " +
                "OR start_date between '"+startDate+"' AND '"+endDate+"' OR end_date between '"+startDate+"' AND '"+endDate+"')" +
                "AND capacity >= " + capacity + " ORDER BY weekly_rate ASC";
        System.out.println(sql);
        return FindVehiclesInDB(sql);
    }

    public List<ArrayList<String>> ViewAvailableVehicles(String startDate, String endDate) {
        String sql = "SELECT * FROM car " +
                "WHERE car_name NOT IN (SELECT car_name FROM car_reservation WHERE '"+startDate+"' BETWEEN start_date AND end_date OR '"+ endDate+"' BETWEEN start_date AND end_date " +
                "OR start_date between '"+startDate+"' AND '"+endDate+"' OR end_date between '"+startDate+"' AND '"+endDate+"')" +
                " ORDER BY car_name ASC";

        return FindVehiclesInDB(sql);
    }


    public List<ArrayList<String>> FindVehiclesInDB(String sql) {
        List<ArrayList<String>> allVehicles = new ArrayList<ArrayList<String>>();
        allVehicles.clear();
        Cursor cursor = null;
        String query = sql;
        cursor = mDb.rawQuery(query, null);

        cursor.moveToFirst();
        try {
            do {
                ArrayList<String> eachVehicle = new ArrayList<String>();
                eachVehicle.add(cursor.getString(cursor.getColumnIndex("car_name")));
                eachVehicle.add(cursor.getString(cursor.getColumnIndex("car_number")));
                eachVehicle.add(cursor.getString(cursor.getColumnIndex("capacity")));
                eachVehicle.add(cursor.getString(cursor.getColumnIndex("weekday_rate")));
                eachVehicle.add(cursor.getString(cursor.getColumnIndex("weekend_rate")));
                eachVehicle.add(cursor.getString(cursor.getColumnIndex("weekly_rate")));
                eachVehicle.add(cursor.getString(cursor.getColumnIndex("gps_rate")));
                eachVehicle.add(cursor.getString(cursor.getColumnIndex("onstar_rate")));
                eachVehicle.add(cursor.getString(cursor.getColumnIndex("siriusxm_rate")));
                allVehicles.add(eachVehicle);
            }
            while (cursor.moveToNext());
        } finally {
            cursor.close();
        }


        return allVehicles;
    }

    public void InsertReservationDetails(String reservation_number, String username, String car_name, String start_date, String end_date, int occupant_capacity, double total_cost, boolean gps_selected, boolean onstar_selected, boolean siriusxm_selected, Boolean is_cancelled) {
        String query = "INSERT INTO car_reservation (reservation_number,username,car_name,start_date,end_date,occupant_capacity,total_cost,gps_selected,siriusxm_selected,onstar_selected,is_cancelled)"
                + "VALUES(" +
                "'"+reservation_number+"'," +
                "'"+username+"'," +
                "'"+car_name+"'," +
                "'"+start_date+"'," +
                "'"+end_date+"'," +
                "'"+occupant_capacity+"'," +
                "'"+total_cost+"'," +
                "'"+gps_selected+"'," +
                "'"+siriusxm_selected+"'," +
                "'"+onstar_selected+"'," +
                ""+is_cancelled+"" +");";
        Cursor cursor = null;
        System.out.println(query);
        cursor = mDb.rawQuery(query, null);
        int temp = cursor.getCount();
        cursor.close();
    }


}
