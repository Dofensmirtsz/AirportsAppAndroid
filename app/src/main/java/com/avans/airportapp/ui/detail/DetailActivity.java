package com.avans.airportapp.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.avans.airportapp.R;
import com.avans.airportapp.data.DataManager;
import com.avans.airportapp.data.local.AirportDBHelper;
import com.avans.airportapp.data.model.Airport;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.jwang123.flagkit.FlagKit;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import timber.log.Timber;

public class DetailActivity extends AppCompatActivity implements DetailView, OnMapReadyCallback {

    private DetailPresenter presenter;
    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    private ImageView imageISO;

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

        imageISO = (ImageView) findViewById(R.id.image_iso_flag);

        presenter = new DetailPresenter(DataManager.instance(this), this);
        presenter.loadAirport(
                getIntent().getStringExtra(AirportDBHelper.ICAO)
        );

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        MapsInitializer.initialize(this);
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


        // Set listeners for click events.
//        this.googleMap.setOnPolylineClickListener(this);
//        this.googleMap.setOnPolygonClickListener(this);

        // Add marker


        googleMap.addMarker(new MarkerOptions().position(EHAM).title("EHAM Marker").snippet("EHAM"));

//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(EHAM, 13);
//        googleMap.animateCamera(cameraUpdate);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(EHAM, 10));

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

//        googleMap.getUiSettings().setAllGesturesEnabled(true);
//        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
//        googleMap.getUiSettings().
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
