package com.avans.airportapp.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.avans.airportapp.R;
import com.avans.airportapp.data.local.AirportDBHelper;

public class DetailActivity extends AppCompatActivity {

    public static Intent getStartIntent(Context context, String icao) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(AirportDBHelper.ICAO, icao);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }
}
