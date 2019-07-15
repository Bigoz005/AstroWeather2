package com.example.astroweather;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int TABLET_DP = 600;

    boolean loop = true;
    TextView timer;
    ViewPager mPager;
    SlidePagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Settings.iteration == 0) {
            loadalways();
        }
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        timer = findViewById(R.id.timer);

        new JsonParser().execute("");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!connect()) {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            load();
        } else {
            save();
        }
        mPager = findViewById(R.id.ViewPager);
        mPagerAdapter = new SlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        clock();

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
        clock();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                setUpCustomDialog();
                return true;
            case R.id.city:
                setUpCustomDialog2();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void setUpCustomDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog);

        final EditText longitude = dialog.findViewById(R.id.longitude);
        final EditText latitude = dialog.findViewById(R.id.latitude);
        Button enter = dialog.findViewById(R.id.enter);
        Spinner mySpinner = dialog.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spiner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter);
        mySpinner.setOnItemSelectedListener(this);
        enter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (Double.valueOf(longitude.getText().toString()) > -180 && Double.valueOf(longitude.getText().toString()) < 180 && Double.valueOf(latitude.getText().toString()) > -90 && Double.valueOf(latitude.getText().toString()) < 90) {
                        Settings.latitude = Double.valueOf(latitude.getText().toString());
                        Settings.longitude = Double.valueOf(longitude.getText().toString());
                        dialog.dismiss();
                    }
                } catch (Exception e) {

                }
            }
        });
        dialog.show();
    }

    public void setUpCustomDialog2() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog2);

        final EditText city = dialog.findViewById(R.id.city);
        Button enter = dialog.findViewById(R.id.enter);
        Spinner spinner = dialog.findViewById(R.id.spinner);
        final Switch switch1 = dialog.findViewById(R.id.switch1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Settings.cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if (!connect()) {
            spinner.setEnabled(false);
            switch1.setEnabled(false);
            enter.setEnabled(false);
            city.setEnabled(false);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int id, long position) {
                Settings.index = (int) position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        enter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Settings.updateIterator = 1;
                String temp = city.getText().toString();
                if (temp.equals("")) {
                } else {
                    for (int i = Settings.numberofCities; i >= 0; i-- ){
                        if (Settings.hashSet.add(temp) == false){
                            Toast.makeText(getApplicationContext(), "Already on list", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Settings.numberofCities++;
                            Settings.cities.add(temp);
                            Settings.index = Settings.numberofCities;
                        }
                    }
                }
                if (switch1.isChecked()) {
                    Settings.units = "&units=imperial";
                    Settings.units1 = " F";
                    Settings.units2 = " miles/h";

                } else {
                    Settings.units = "&units=metric";
                    Settings.units1 = " C";
                    Settings.units2 = " m/s";
                }
                new JsonParser().execute("");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                save();

                dialog.dismiss();

            }
        });
        Settings.updateIterator = 10;

        dialog.show();
    }

    public void clock() {
        new Thread(new Runnable() {

            public void run() {

                while (loop) {

                    final Date date = new Date();
                    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            timer.setText(String.valueOf(simpleDateFormat.format(date)));

                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                Settings.updateIterator = 10;
                break;
            case 1:
                Settings.updateIterator = 15;
                break;
            case 2:
                Settings.updateIterator = 30;
                break;
            case 3:
                Settings.updateIterator = 45;
                break;
            case 4:
                Settings.updateIterator = 60;
                break;
            case 5:
                Settings.updateIterator = 90;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static boolean isTablet(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = context.getResources().getDisplayMetrics().density;
        float dpWidth = outMetrics.widthPixels / density;
        return dpWidth >= TABLET_DP;
    }

    public void save() {
        SharedPreferences myPreferences
                = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor myEditor = myPreferences.edit();

        for (int i = 0; i <= Settings.numberofCities; i++) {
            myEditor.putString(("miasto" + i), Settings.cities.get(i));
            System.out.println(i + "= " + Settings.cities.get(i));
        }
        myEditor.putString("numberofCities", String.valueOf(Settings.numberofCities));
        myEditor.putString("name", Settings.name);
        myEditor.putString("lat", Settings.lat);
        myEditor.putString("lon", Settings.lon);
        myEditor.putString("temp", Settings.temp);
        myEditor.putString("pressure", Settings.pressure);
        myEditor.putString("description", Settings.description);
        myEditor.putString("image", Settings.image);

        myEditor.putString("speed", Settings.speed);
        myEditor.putString("direction", Settings.direction);
        myEditor.putString("humidity", Settings.humidity);
        myEditor.putString("visibility", Settings.visibility);


        myEditor.putString("tomorrow", Settings.tomorrow);
        myEditor.putString("temperature1", Settings.temperature1);
        myEditor.putString("pressure1", Settings.pressure1);
        myEditor.putString("dayAfterTomorrow", Settings.dayAfterTomorrow);
        myEditor.putString("temperature2", Settings.temperature2);
        myEditor.putString("pressure2", Settings.pressure2);
        myEditor.putString("twoDaysAfterTomorrow", Settings.twoDaysAfterTomorrow);
        myEditor.putString("temperature3", Settings.temperature3);
        myEditor.putString("pressure3", Settings.pressure3);

        myEditor.commit();
        load();
    }

    public void load() {
        SharedPreferences myPreferences
                = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        Settings.name = myPreferences.getString("name", " ");
        Settings.lat = myPreferences.getString("lat", " ");
        Settings.lon = myPreferences.getString("lon", " ");
        Settings.temp = myPreferences.getString("temp", " ");
        Settings.pressure = myPreferences.getString("pressure", " ");
        Settings.description = myPreferences.getString("description", " ");
        Settings.image = myPreferences.getString("image", " ");

        Settings.speed = myPreferences.getString("speed", " ");
        Settings.direction = myPreferences.getString("direction", " ");
        Settings.humidity = myPreferences.getString("humidity", " ");
        Settings.visibility = myPreferences.getString("visibility", " ");

        Settings.tomorrow = myPreferences.getString("tomorrow", " ");
        Settings.temperature1 = myPreferences.getString("temperature1", " ");
        Settings.pressure1 = myPreferences.getString("pressure1", " ");

        Settings.dayAfterTomorrow = myPreferences.getString("dayAfterTomorrow", " ");
        Settings.temperature2 = myPreferences.getString("temperature2", " ");
        Settings.pressure2 = myPreferences.getString("pressure2", " ");

        Settings.twoDaysAfterTomorrow = myPreferences.getString("tomorrow", " ");
        Settings.temperature3 = myPreferences.getString("temperature3", " ");
        Settings.pressure3 = myPreferences.getString("pressure3", " ");
    }

    public void loadalways() {
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        Settings.numberofCities = Integer.valueOf(myPreferences.getString("numberofCities", "0"));
        for (int i = 0; i <= Settings.numberofCities; i++) {
            Settings.hashSet.add("Lodz");
            Settings.cities.add(i, myPreferences.getString(("miasto" + i), "Lodz"));
        }
        Settings.iteration += 1;
    }

    public boolean connect() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        } else {
            return false;
        }
    }
}