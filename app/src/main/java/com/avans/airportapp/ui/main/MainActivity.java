package com.avans.airportapp.ui.main;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.avans.airportapp.R;
import com.avans.airportapp.data.DataManager;
import com.avans.airportapp.data.local.AirportDBHelper;
import com.avans.airportapp.ui.main.adapter.AirportsAdapter;
import com.avans.airportapp.ui.other.SimpleSectionedRecyclerViewAdapter;

import java.util.ArrayList;
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
        List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();

        for (int i = 0; i < data.getCount(); i++) {
            data.moveToPosition(i);
            String isoCountry1 = data.getString(data.getColumnIndex(AirportDBHelper.ISO_COUNTRY));
            data.moveToPosition((i + 1) < data.getCount() ? i + 1 : i);
            String isoCountry2 = data.getString(data.getColumnIndex(AirportDBHelper.ISO_COUNTRY));

            if(!isoCountry1.equals(isoCountry2)) {
                sections.add(new SimpleSectionedRecyclerViewAdapter.Section(i, isoCountry1));
            }
        }
        AirportsAdapter baseAdapter = new AirportsAdapter(data);

        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        SimpleSectionedRecyclerViewAdapter sectionAdapter = new SimpleSectionedRecyclerViewAdapter(this, R.layout.item_airport_section, R.id.text_iso_country, baseAdapter);
        sectionAdapter.setSections(sections.toArray(dummy));

        recyclerView.setAdapter(sectionAdapter);
    }
}
