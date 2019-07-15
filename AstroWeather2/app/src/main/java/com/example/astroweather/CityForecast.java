package com.example.astroweather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CityForecast extends Fragment {


    View view;
    TextView name;
    TextView tomorrow;
    TextView dayAfterTomorrow;
    TextView twoDaysAfterTomorrow;
    TextView temperature1;
    TextView pressure1;
    TextView temperature2;
    TextView pressure2;
    TextView temperature3;
    TextView pressure3;
    boolean loop = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cityforecast, container, false);
        init();
        go();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loop = true;
        go();
    }

    @Override
    public void onStop() {
        super.onStop();
        loop = false;
    }

    public void init() {
        name = view.findViewById(R.id.name);

        tomorrow = view.findViewById(R.id.day1);
        dayAfterTomorrow = view.findViewById(R.id.day2);
        twoDaysAfterTomorrow = view.findViewById(R.id.day3);

        temperature1 = view.findViewById(R.id.temp1);
        temperature2 = view.findViewById(R.id.temp2);
        temperature3 = view.findViewById(R.id.temp3);

        pressure1 = view.findViewById(R.id.pressure1);
        pressure2 = view.findViewById(R.id.pressure2);
        pressure3 = view.findViewById(R.id.pressure3);
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

    public void update() {

        name.setText(Settings.name);

        tomorrow.setText(Settings.tomorrow);
        dayAfterTomorrow.setText(Settings.dayAfterTomorrow);
        twoDaysAfterTomorrow.setText(Settings.twoDaysAfterTomorrow);

        temperature1.setText(Settings.temperature1);
        temperature2.setText(Settings.temperature2);
        temperature3.setText(Settings.temperature3);

        pressure1.setText(Settings.pressure1);
        pressure2.setText(Settings.pressure2);
        pressure3.setText(Settings.pressure3);
    }

}
