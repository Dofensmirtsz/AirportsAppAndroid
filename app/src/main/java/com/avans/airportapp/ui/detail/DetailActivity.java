package com.avans.airportapp.ui.detail;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avans.airportapp.R;
import com.avans.airportapp.data.DataManager;
import com.avans.airportapp.data.local.AirportDBHelper;
import com.avans.airportapp.data.model.Airport;

import com.avans.airportapp.util.AirportMapUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jwang123.flagkit.FlagKit;

import com.google.android.gms.maps.GoogleMap;

import java.util.List;

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

    private Airport selectedAirport;

    static final LatLng EHAM = new LatLng(52.3105386,4.7682744);
    private LatLng selectedLatLng;

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

        selectedAirport = airport;
        getSupportActionBar().setTitle(airport.getIcao());

        airportName.setText(airport.getName());
        airportCoords.setText(getString(R.string.detail_coords, airport.getLongitude(), airport.getLatitude()));

        airportElevation.setVisibility(airport.getElevation().isEmpty() ? View.GONE : View.VISIBLE);
        airportElevation.setText(getString(R.string.detail_elevation, airport.getElevation()));

        airportMunicipality.setVisibility(airport.getMunicipality().isEmpty() ? View.GONE : View.VISIBLE);
        airportMunicipality.setText(getString(R.string.detail_municipality, airport.getMunicipality()));

        try {
            selectedLatLng = new LatLng(Double.parseDouble(airport.getLatitude()), Double.parseDouble(airport.getLongitude()));
        } catch (NumberFormatException e) {
            Timber.e(e);
            onBackPressed();
            Toast.makeText(getApplicationContext(), "INVALID LAT/LON VALUE", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (selectedLatLng != null) {
            googleMap.addMarker(new MarkerOptions().position(EHAM).title("EHAM").snippet("Schiphol"));
            googleMap.addMarker(new MarkerOptions().position(selectedLatLng).title(selectedAirport.getIcao()).snippet(selectedAirport.getName()));

            LatLng middle = new LatLng((EHAM.latitude + selectedLatLng.latitude) / 2, (EHAM.longitude + selectedLatLng.longitude) / 2);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(middle,0));

            PolygonOptions polygonOptions = new PolygonOptions().clickable(true).add(EHAM, selectedLatLng);

            Polygon polygon = this.googleMap.addPolygon(polygonOptions);
            polygon.setFillColor(Color.argb(255, 0, 0, 0));
            polygon.setStrokeWidth(5.0f);

            Polyline polyline = googleMap.addPolyline((new PolylineOptions()
                    .add(EHAM,selectedLatLng)
                    .width(5.0f)
                    .color(Color.RED)
                    .geodesic(true)));

            Marker marker = googleMap.addMarker(new MarkerOptions().position(EHAM).title("AIRPLANE").icon(BitmapDescriptorFactory.fromResource(R.drawable.vliegtuig)));
            AirportMapUtil.animateMarker(marker, selectedLatLng, polyline.getPoints(), new LatLngInterpolator.Spherical());
        }

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
    public void onLowMemory() {
        super.onLowMemory();
        mapFragment.onLowMemory();
    }
}
