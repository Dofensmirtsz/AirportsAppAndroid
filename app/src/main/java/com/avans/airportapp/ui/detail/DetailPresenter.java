package com.avans.airportapp.ui.detail;


import com.avans.airportapp.data.DataManager;

public class DetailPresenter {

    private DetailView view;
    private DataManager dataManager;

    public DetailPresenter(DataManager dataManager, DetailView view) {
        this.view = view;
        this.dataManager = dataManager;
    }

    public void loadAirport(String icao) {
        view.showAirport(dataManager.getDBHelper().getAirport(icao));
    }

}
