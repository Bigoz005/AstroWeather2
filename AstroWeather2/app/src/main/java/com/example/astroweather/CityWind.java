package com.example.astroweather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CityWind extends Fragment {

    View view;
    TextView speed;
    TextView direction;
    TextView humidity;
    TextView visibility;
    boolean loop = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_citywind, container, false);
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
        speed = view.findViewById(R.id.speed);
        direction = view.findViewById(R.id.direction);
        humidity = view.findViewById(R.id.humidity);
        visibility = view.findViewById(R.id.visibility);
    }

    public void update() {
        speed.setText(Settings.speed);
        direction.setText(Settings.direction);
        humidity.setText(Settings.humidity);
        visibility.setText(Settings.visibility);
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
