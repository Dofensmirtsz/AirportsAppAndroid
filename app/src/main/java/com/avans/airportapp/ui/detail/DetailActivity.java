package com.avans.airportapp.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.avans.airportapp.R;
import com.avans.airportapp.data.DataManager;
import com.avans.airportapp.data.local.AirportDBHelper;
import com.avans.airportapp.data.model.Airport;
import com.jwang123.flagkit.FlagKit;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import timber.log.Timber;

public class DetailActivity extends AppCompatActivity implements DetailView {

    private DetailPresenter presenter;
    private GoogleMap map;
    private ImageView imageISO;

    public static Intent getStartIntent(Context context, String icao) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(AirportDBHelper.ICAO, icao);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageISO = (ImageView) findViewById(R.id.image_iso_flag);

        presenter = new DetailPresenter(DataManager.instance(this), this);
        presenter.loadAirport(
                getIntent().getStringExtra(AirportDBHelper.ICAO)
        );
    }

    @Override
    public void showAirport(Airport airport) {
        Timber.d(airport.toString());
        try {
            imageISO.setImageDrawable(FlagKit.drawableWithFlag(this, airport.getIsoCountry().toLowerCase()));
        } catch (Resources.NotFoundException e) {
            imageISO.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSectionBackground));
        }
    }
}
