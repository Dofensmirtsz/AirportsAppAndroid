package com.avans.airportapp.data.model;


public class Airport {

    private String icao;
    private String name;
    private String longitude;
    private String latitude;
    private String elevation;
    private String isoCountry;
    private String municipality;

    public Airport(String icao, String name, String longitude, String latitude, String elevation, String isoCountry, String municipality) {
        this.icao = icao;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.elevation = elevation;
        this.isoCountry = isoCountry;
        this.municipality = municipality;
    }

    public String getIcao() {
        return icao;
    }

    public String getName() {
        return name;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getElevation() {
        return elevation;
    }

    public String getIsoCountry() {
        return isoCountry;
    }

    public String getMunicipality() {
        return municipality;
    }
}
