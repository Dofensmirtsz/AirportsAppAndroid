package com.avans.airportapp.ui.main;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.avans.airportapp.R;
import com.avans.airportapp.data.DataManager;
import com.avans.airportapp.data.model.Airport;
import com.avans.airportapp.ui.main.adapter.AirportsAdapter;
import com.avans.airportapp.ui.other.SimpleSectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity implements MainView {

    private MainPresenter presenter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.airports_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        presenter = new MainPresenter(DataManager.instance(this), this);
        presenter.loadAirports();
    }

    @Override
    public void showAirports(Cursor data) {
        Airport[] airports = new Airport[data.getCount()];
        for (int i = 0; i < data.getCount() ; i++) {
            airports[i] = new Airport(
                    data.getString(data.getColumnIndex("icao")),
                    data.getString(data.getColumnIndex("name")),
                    data.getString(data.getColumnIndex("longitude")),
                    data.getString(data.getColumnIndex("latitude")),
                    data.getString(data.getColumnIndex("elevation")),
                    data.getString(data.getColumnIndex("iso_country")),
                    data.getString(data.getColumnIndex("municipality"))
                    );
            data.moveToNext();
        }

        Arrays.sort(airports, (a1, a2) -> a1.getIsoCountry().compareTo(a2.getIsoCountry()));

        List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();

        for (int i = 0; i < airports.length ; i++) {
            Airport airport1 = airports[i];
            Airport airport2 = ((i + 1) < airports.length) ? airports[i + 1] : airports[i];

            if(!airport1.getIsoCountry().equals(airport2.getIsoCountry())) {
                sections.add(new SimpleSectionedRecyclerViewAdapter.Section(i, airport1.getIsoCountry()));
            }
        }
        AirportsAdapter baseAdapter = new AirportsAdapter(data);

        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        SimpleSectionedRecyclerViewAdapter sectionAdapter = new SimpleSectionedRecyclerViewAdapter(this, R.layout.item_airport_section, R.id.text_iso_country, baseAdapter);
        sectionAdapter.setSections(sections.toArray(dummy));

        recyclerView.setAdapter(sectionAdapter);
    }
}
