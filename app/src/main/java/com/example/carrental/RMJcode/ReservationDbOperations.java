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

    public List<ArrayList<String>> ViewReservations(String startDate, String endDate) {
        String sql = "select * from Car_Reservation where start_date between '" + startDate + "' and '" + endDate + "' AND end_date between '" + startDate + "' and '" + endDate + "' ORDER BY start_date ASC, car_name ASC";
        System.out.println(sql);
        return ViewReservationsInDB(sql);
    }

    public List<ArrayList<String>> ViewReservationsInDB(String sql) {
        List<ArrayList<String>> allReservations = new ArrayList<ArrayList<String>>();
        allReservations.clear();
        Cursor cursor = null;
        String query = sql;
        cursor = mDb.rawQuery(query, null);
        if (cursor.getCount() > 0) {
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

    public void InsertReservationDetails(String reservation_number, String username, String car_name, String start_date, String end_date, int occupant_capacity, double total_cost, boolean gps_selected, boolean onstar_selected, boolean siriusxm_selected, Boolean is_cancelled) {
        String query = "INSERT INTO car_reservation (reservation_number,username,car_name,start_date,end_date,occupant_capacity,total_cost,gps_selected,siriusxm_selected,onstar_selected,is_cancelled)"
                + "VALUES(" +
                "'" + reservation_number + "'," +
                "'" + username + "'," +
                "'" + car_name + "'," +
                "'" + start_date + "'," +
                "'" + end_date + "'," +
                "'" + occupant_capacity + "'," +
                "'" + total_cost + "'," +
                "'" + gps_selected + "'," +
                "'" + siriusxm_selected + "'," +
                "'" + onstar_selected + "'," +
                "'" + is_cancelled + "'" + ");";
        Cursor cursor = null;
        System.out.println(query);
        cursor = mDb.rawQuery(query, null);
        int temp = cursor.getCount();
        cursor.close();
    }

    public List<ArrayList<String>> ViewReservationHistory(String startDate, String endDate, String username) {
        String sql = "select * from Car_Reservation where start_date >= '" + startDate + "' AND end_date <= '" + endDate + "' AND username = '" + username + "' AND is_cancelled != 'true' ORDER BY start_date ASC, car_name ASC";

        return ViewReservationsInDB(sql);
    }


    public void updateReservation(String reservationNo, double finalCost, boolean gpsSelected, boolean onStarSelected, boolean siriusSelected) {
        String query = "UPDATE Car_Reservation\n" +
                "   SET total_cost = '" + finalCost + "',\n" +
                "       gps_selected = '" + gpsSelected + "',\n" +
                "       siriusxm_selected = '" + siriusSelected + "',\n" +
                "       onstar_selected = '" + onStarSelected + "'\n" +
                " WHERE reservation_number = '" + reservationNo + "'";
        Cursor cursor;
        cursor = mDb.rawQuery(query, null);
        int temp = cursor.getCount();
        cursor.close();
    }

    public void cancelReservation(String reservationNo) {
        String query = "UPDATE Car_Reservation\n" +
                "   SET is_cancelled = 'true'\n" +
                " WHERE reservation_number = '" + reservationNo + "'";
        Cursor cursor;
        cursor = mDb.rawQuery(query, null);
        int temp = cursor.getCount();
        cursor.close();
    }
}
