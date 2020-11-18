package com.example.carrental.RMJcode;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.carrental.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ReservationDbOperations {

    public Context context;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    public ReservationDbOperations(Context cntx) {
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

    public List<ArrayList<String>> ViewReservations( String startDate, String endDate) {
        String sql =  "select * from Car_Reservation where start_date between '"+startDate+"' and '"+endDate+"' AND end_date between '"+startDate+"' and '"+endDate+"' ORDER BY start_date ASC, car_name ASC";
        System.out.println(sql);
        return ViewReservationsInDB(sql);
    }

    public List<ArrayList<String>> ViewReservationsInDB(String sql) {
        List<ArrayList<String>> allReservations = new ArrayList<ArrayList<String>>();
        allReservations.clear();
        Cursor cursor = null;
        String query = sql;
        cursor = mDb.rawQuery(query, null);
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            try {
                do {
                    ArrayList<String> eachReservation = new ArrayList<String>();
                    eachReservation.add(cursor.getString(cursor.getColumnIndex("car_name")));
                    eachReservation.add(cursor.getString(cursor.getColumnIndex("occupant_capacity")));
                    eachReservation.add(cursor.getString(cursor.getColumnIndex("start_date")));
                    eachReservation.add(cursor.getString(cursor.getColumnIndex("end_date")));
                    eachReservation.add(cursor.getString(cursor.getColumnIndex("total_cost")));
                    eachReservation.add(cursor.getString(cursor.getColumnIndex("reservation_number")));
                    allReservations.add(eachReservation);
                }
                while (cursor.moveToNext());
            } finally {
                cursor.close();
            }

        }
        return allReservations;
    }


}
