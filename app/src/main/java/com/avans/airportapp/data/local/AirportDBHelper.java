package com.avans.airportapp.data.local;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class AirportDBHelper extends SQLiteAssetHelper {

    private static final String DB_NAME = "airports.sqlite";
    private static final int DB_VERSION = 1;

    public AirportDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public Cursor getAirports() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"icao", "name", "longitude", "latitude", "elevation", "iso_country", "municipality"};
        String sqlTable = "airports";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);
        c.moveToFirst();
        return c;
    }

}
