package com.example.astroweather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class Moon extends Fragment {

    boolean loop = true;
    View view;
    AstroCalculator astroCalculator;
    AstroCalculator.Location location;

    TextView moonrise = null;
    TextView moonset = null;
    TextView newMoon = null;
    TextView full = null;
    TextView faze = null;
    TextView day = null;


    public Moon() {
        // Required empty public constructor
    }

    public void setup() {

        moonrise = view.findViewById(R.id.wschod);
        moonset = view.findViewById(R.id.zachod);
        newMoon = view.findViewById(R.id.newMoon);
        full = view.findViewById(R.id.full);
        faze = view.findViewById(R.id.faze);
        day = view.findViewById(R.id.day);
    }

    public void update() {
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
        astroCalculator = new AstroCalculator(astroDateTime, location);

        String[] text = astroCalculator.getMoonInfo().getMoonrise().toString().split(" ");
        moonrise.setText(text[1]);
        text = astroCalculator.getMoonInfo().getMoonset().toString().split(" ");
        moonset.setText(text[1]);
        text = astroCalculator.getMoonInfo().getNextNewMoon().toString().split(" ");
        newMoon.setText(text[0]);
        text = astroCalculator.getMoonInfo().getNextFullMoon().toString().split(" ");
        full.setText(text[0]);
        faze.setText(String.valueOf(astroCalculator.getMoonInfo().getIllumination()).substring(0, 6));

        double temp = Double.valueOf(currentDate[0]) - Double.valueOf(text[0].substring(0, 2)) - 2;

        if (temp < 0) {
            temp += 29.531;
            day.setText(String.valueOf(temp));
        } else {
            day.setText(String.valueOf(temp));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_moon, container, false);
        setup();
        go();
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        loop = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        loop = true;
        go();
    }

    public void go() {
        new Thread(new Runnable() {

            public void run() {
                int upIterator = Settings.updateIterator + 1;
                while (loop) {
                    if (Settings.updateIterator < upIterator) {
                        upIterator = 1;
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                update();
                            }
                        });
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    upIterator++;
                }
            }
        }).start();
    }
}
