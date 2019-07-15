package com.example.astroweather;

import com.astrocalculator.AstroCalculator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.astrocalculator.AstroDateTime;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Sun extends Fragment {

    boolean loop=true;
    AstroCalculator astroCalculator;
    AstroCalculator.Location location;
    View view;
    TextView sunrise = null;
    TextView azimutSunrise =null;
    TextView sunset =null;
    TextView azimutSunset =null;
    TextView twilight =null;
    TextView dawn =null;


    public Sun() {
        // Required empty public constructor
    }

    public void setup(){

        sunrise = view.findViewById(R.id.wschod);
        azimutSunrise = view.findViewById(R.id.azimutSunrise);
        sunset = view.findViewById(R.id.zachod);
        azimutSunset = view.findViewById(R.id.azimutSunset);
        twilight = view.findViewById(R.id.twilight);
        dawn = view.findViewById(R.id.dawn);
    }

    public void update(){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM yyyy HH mm ss");
        String[] currentDate = simpleDateFormat.format(date).split(" ");
        AstroDateTime astroDateTime = new AstroDateTime();
        astroDateTime.setTimezoneOffset(1);
        astroDateTime.setDaylightSaving(true);
        astroDateTime.setDay(Integer.valueOf(currentDate[0]));
        astroDateTime.setMonth(Integer.valueOf(currentDate[1]));
        astroDateTime.setYear(Integer.valueOf(currentDate[2]));
        astroDateTime.setHour(Integer.valueOf(currentDate[3]));
        astroDateTime.setMinute(Integer.valueOf(currentDate[4]));
        astroDateTime.setSecond(Integer.valueOf(currentDate[5]));
        location = new AstroCalculator.Location(Settings.latitude, Settings.longitude);
        astroCalculator = new AstroCalculator(astroDateTime,location);

        String[] temp;
        temp= astroCalculator.getSunInfo().getSunrise().toString().split(" ");
        sunrise.setText(temp[1]);
        double temp1= astroCalculator.getSunInfo().getAzimuthRise();
        azimutSunrise.setText(String.valueOf(temp1).substring(1,7));
        temp= astroCalculator.getSunInfo().getSunset().toString().split(" ");
        sunset.setText(temp[1]);
        temp1 = astroCalculator.getSunInfo().getAzimuthSet();
        azimutSunset.setText(String.valueOf(temp1).substring(1,7));
        temp= astroCalculator.getSunInfo().getTwilightMorning().toString().split(" ");
        dawn.setText(temp[1]);
        temp= astroCalculator.getSunInfo().getTwilightEvening().toString().split(" ");
        twilight.setText(temp[1]);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sun, container, false);
        setup();
        go();
        return view;
    }
    @Override
    public void onStop() {
        super.onStop();
        loop=false;
    }

    @Override
    public void onResume() {
        super.onResume();
        loop=true;
        go();
    }

    public void go(){
        new Thread(new Runnable() {

            public void run() {
                int upiterator= Settings.updateIterator +1;
                while (loop) {
                    if(Settings.updateIterator <upiterator) {
                        upiterator=1;
                        getActivity().runOnUiThread(new Runnable() {

                            @Override public void run() {
                                update();
                                //Toast.makeText(getContext(),"ref",Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    upiterator++;
                }
            }
        }).start();

    }
}
