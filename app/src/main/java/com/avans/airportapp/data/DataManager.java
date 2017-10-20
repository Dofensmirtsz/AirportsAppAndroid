package com.avans.airportapp.data;


import android.content.Context;

import com.avans.airportapp.data.local.AirportDBHelper;
import com.avans.airportapp.data.local.PreferencesHelper;

public class DataManager {

    private static DataManager instance;
    private PreferencesHelper preferencesHelper;
    private AirportDBHelper dbHelper;

    public static DataManager instance(Context context) {
        if (instance == null) {
            instance = new DataManager(context);
        }
        return instance;
    }

    private DataManager(Context context) {
        preferencesHelper = PreferencesHelper.instance(context);
        dbHelper = new AirportDBHelper(context);
    }

    public PreferencesHelper getPreferencesHelper() {
        return preferencesHelper;
    }

    public AirportDBHelper getDBHelper() {
        return dbHelper;
    }
}
