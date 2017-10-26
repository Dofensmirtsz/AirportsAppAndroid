package com.avans.airportapp.data.local;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.avans.airportapp.data.model.Airport;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class AirportDBHelper extends SQLiteAssetHelper {

    private static final String DB_NAME = "airports.sqlite";
    private static final int DB_VERSION = 1;
    private static final String SQL_TABLE = "airports";

    //COLUMN NAMES
    public static final String
            ICAO = "icao",
            NAME = "name",
            LONGITUDE = "longitude",
            LATITUDE = "latitude",
            ELEVATION = "elevation",
            ISO_COUNTRY = "iso_country",
            MUNICIPALITY = "municipality";

    private SQLiteDatabase database;

    public AirportDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        database = getReadableDatabase();
    }

    public Cursor getAirports() {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(SQL_TABLE);

        String[] sqlSelect = {ICAO, NAME, ISO_COUNTRY};
        Cursor c = qb.query(database, sqlSelect, null, null, null, null, ISO_COUNTRY);
        c.moveToFirst();
        return c;
    }

    public Airport getAirport(String icao) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(SQL_TABLE);

        Cursor c = qb.query(database,
                new String[]{ICAO, NAME, LONGITUDE, LATITUDE, ELEVATION, ISO_COUNTRY, MUNICIPALITY},
                ICAO + "='" + icao + "'",
                null,
                null,
                null,
                null);

        c.moveToFirst();

        return new Airport(
                c.getString(c.getColumnIndex(ICAO)),
                c.getString(c.getColumnIndex(NAME)),
                c.getString(c.getColumnIndex(LONGITUDE)),
                c.getString(c.getColumnIndex(LATITUDE)),
                c.getString(c.getColumnIndex(ELEVATION)),
                c.getString(c.getColumnIndex(ISO_COUNTRY)),
                c.getString(c.getColumnIndex(MUNICIPALITY))
        );
    }

    public Cursor search(String query) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(SQL_TABLE);

        Cursor c = qb.query(database,
                new String[]{ICAO, NAME, LONGITUDE, LATITUDE, ELEVATION, ISO_COUNTRY, MUNICIPALITY},
                "name LIKE '%" + query +"%' " +
                    "OR municipality LIKE '%" + query + "%' " +
                    "OR icao LIKE '%" + query + "%'",
                null,
                null,
                null,
                ISO_COUNTRY);

        c.moveToFirst();
        return c;
    }
}
