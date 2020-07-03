package com.example.quake;

import java.net.URL;

public class details {

    private double magnitude;
    private String place;
    private long date;
    private String url;
    details(double mag,String place,long date,String url){
        this.magnitude=mag;
        this.place=place;
        this.date=date;
        this.url=url;
    }
    public double get_mag(){ return magnitude;}

    public String get_place(){ return place;}

    public long get_date(){ return date;}

    public String get_url(){ return url;}
}
