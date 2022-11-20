package com.example.lepizzadelivery.models;

import com.google.android.libraries.places.api.model.Place;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Objects;

public class Address implements Serializable {

    String name;
    Double lat;
    Double lng;

    public Address(Place place) {
        this.name = place.getName();
        this.lat = Objects.requireNonNull(place.getLatLng()).latitude;
        this.lng = place.getLatLng().longitude;
    }

    public Address(String name, Double lat, Double lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public Double getDistance(Address address){
        double R = 6371e3; // metres
        double lat1 = this.lat * Math.PI/180; // φ, λ in radians
        double lat2 = address.getLat() * Math.PI/180;
        double difLats = (lat2-lat1) * Math.PI/180;
        double difLngs = (address.getLng()-this.lng) * Math.PI/180;

        double a = Math.sin(difLats/2) * Math.sin(difLats/2) +
                        Math.cos(lat1) * Math.cos(lat2) *
                                Math.sin(difLngs/2) * Math.sin(difLngs/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double d = R * c/1000; // in Km
        return d;
    }

    public static String format(Double distance, int decimalPlaces){
        DecimalFormat df = new DecimalFormat((decimalPlaces == 1) ? "#.#" : "#.##");
        return df.format(distance);
    }

    public String getName() {return name;}
    public Double getLat() {return lat;}
    public Double getLng() {return lng;}

    @Override
    public String toString() {
        return "Address{" +
                "name='" + name + '\n' +
                ", lat=" + lat + '\n' +
                ", lng=" + lng + '\n' +
                '}';
    }
}
