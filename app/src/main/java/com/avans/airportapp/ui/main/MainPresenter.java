package com.avans.airportapp.ui.main;


import com.avans.airportapp.data.DataManager;

public class MainPresenter {

    private MainView view;
    private DataManager dataManager;

    public MainPresenter(DataManager dataManager, MainView view) {
        this.view = view;
        this.dataManager = dataManager;
    }

    public void loadAirports() {
        view.showAirports(dataManager.getDBHelper().getAirports());
    }

}
