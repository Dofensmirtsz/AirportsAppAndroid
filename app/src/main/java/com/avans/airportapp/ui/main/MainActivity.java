package com.avans.airportapp.ui.main;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.avans.airportapp.R;
import com.avans.airportapp.data.DataManager;
import com.avans.airportapp.data.local.AirportDBHelper;
import com.avans.airportapp.ui.main.adapter.AirportsAdapter;
import com.avans.airportapp.ui.other.SimpleSectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

public class MainActivity extends AppCompatActivity implements MainView, SearchView.OnQueryTextListener {

    private MainPresenter presenter;
    private RecyclerView recyclerView;
    private VerticalRecyclerViewFastScroller scroller;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.airports_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        scroller = (VerticalRecyclerViewFastScroller) findViewById(R.id.airports_recyclerview_fastscroller);
        scroller.setRecyclerView(recyclerView);

        recyclerView.addOnScrollListener(scroller.getOnScrollListener());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Airports");

        presenter = new MainPresenter(DataManager.instance(this), this);
        presenter.loadAirports();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        super.onCreateOptionsMenu(menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView =
                new SearchView(getSupportActionBar().getThemedContext());
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(searchView);
        searchView.setOnQueryTextListener(this);
        return true;
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
                sections.add(new SimpleSectionedRecyclerViewAdapter.Section(i, isoCountry2));
            }
        }
        AirportsAdapter baseAdapter = new AirportsAdapter(data);

        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        SimpleSectionedRecyclerViewAdapter sectionAdapter = new SimpleSectionedRecyclerViewAdapter(this, R.layout.item_airport_section, R.id.text_iso_country, baseAdapter);
        sectionAdapter.setSections(sections.toArray(dummy));

        recyclerView.setAdapter(sectionAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        presenter.searchAirports(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        if(query.isEmpty()) {
            presenter.loadAirports();
        }
        return false;
    }
}
