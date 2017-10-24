package com.avans.airportapp.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avans.airportapp.R;
import com.avans.airportapp.data.DataManager;
import com.avans.airportapp.data.local.AirportDBHelper;
import com.avans.airportapp.data.model.Airport;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.jwang123.flagkit.FlagKit;

import com.google.android.gms.maps.GoogleMap;

import timber.log.Timber;

public class DetailActivity extends AppCompatActivity implements DetailView, OnMapReadyCallback {

    private DetailPresenter presenter;
    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    private ImageView imageISO;

    private TextView airportName;
    private TextView airportElevation;
    private TextView airportCoords;
    private TextView airportMunicipality;

    private LinearLayout detailContainer;
    private SwitchCompat detailSwitch;

    private Toolbar toolbar;

    static final LatLng EHAM = new LatLng(52.3105386,4.7682744);

    public static Intent getStartIntent(Context context, String icao) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(AirportDBHelper.ICAO, icao);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageISO = (ImageView) findViewById(R.id.detail_image_iso_flag);

        detailContainer = (LinearLayout) findViewById(R.id.detail_container_stats);
        detailSwitch = (SwitchCompat) findViewById(R.id.detail_switch_details);
        detailSwitch.setOnCheckedChangeListener((compoundButton, checked) -> {
            detailContainer.setVisibility(checked ? View.VISIBLE : View.GONE);
        });

        airportName = (TextView) findViewById(R.id.detail_text_name);
        airportElevation = (TextView) findViewById(R.id.detail_text_elevation);
        airportCoords = (TextView) findViewById(R.id.detail_text_coordinates);
        airportMunicipality = (TextView) findViewById(R.id.detail_text_municipality);

        presenter = new DetailPresenter(DataManager.instance(this), this);
        presenter.loadAirport(
                getIntent().getStringExtra(AirportDBHelper.ICAO)
        );

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        MapsInitializer.initialize(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showAirport(Airport airport) {
        Timber.d(airport.toString());
        try {
            imageISO.setImageDrawable(FlagKit.drawableWithFlag(this, airport.getIsoCountry().toLowerCase()));
        } catch (Resources.NotFoundException e) {
            imageISO.setBackgroundColor(ContextCompat.getColor(this, R.color.colorSectionBackground));
        }

        getSupportActionBar().setTitle(airport.getIcao());

        airportName.setText(airport.getName());
        airportCoords.setText(getString(R.string.detail_coords, airport.getLongitude(), airport.getLatitude()));

        airportElevation.setVisibility(airport.getElevation().isEmpty() ? View.GONE : View.VISIBLE);
        airportElevation.setText(getString(R.string.detail_elevation, airport.getElevation()));

        airportMunicipality.setVisibility(airport.getMunicipality().isEmpty() ? View.GONE : View.VISIBLE);
        airportMunicipality.setText(getString(R.string.detail_municipality, airport.getMunicipality()));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;

        PolygonOptions polygonOptions = new PolygonOptions()
                .clickable(true)
                .add(new LatLng(51.586662, 4.791969),
                        new LatLng( 51.584434, 4.793528),
                        new LatLng( 51.587474, 4.795993),
                        new LatLng(51.586662, 4.791969));

        // Set polygon dingen
        Polygon polygon = this.googleMap.addPolygon(polygonOptions);
        polygon.setFillColor(Color.argb(100, 255, 255, 255));
        polygon.setStrokeColor(Color.RED);
        polygon.setStrokeWidth(10.0f);

        googleMap.addMarker(new MarkerOptions().position(EHAM).title("EHAM Marker").snippet("EHAM"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(EHAM, 10));

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    @Override
    public void onResume() {
        mapFragment.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapFragment.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapFragment.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapFragment.onLowMemory();
    }

}
