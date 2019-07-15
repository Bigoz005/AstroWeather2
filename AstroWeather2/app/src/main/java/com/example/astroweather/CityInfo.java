package com.example.astroweather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class CityInfo extends Fragment {

    LayoutInflater inflater;
    View view;
    TextView name;
    TextView coordinates;
    TextView temperature;
    TextView pressure;
    TextView description;
    boolean loop = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cityinfo, container, false);
        this.inflater = inflater;
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
        coordinates = view.findViewById(R.id.coordinates);
        temperature = view.findViewById(R.id.temperature);
        pressure = view.findViewById(R.id.pressure);
        description = view.findViewById(R.id.description);

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

    public void update(){
        name.setText(Settings.name);
        coordinates.setText(Settings.lon + "   " + Settings.lat);
        temperature.setText(Settings.temp);
        pressure.setText(Settings.pressure);
        description.setText(Settings.description);
    }
}
