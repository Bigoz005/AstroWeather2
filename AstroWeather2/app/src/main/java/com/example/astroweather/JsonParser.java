package com.example.astroweather;


import android.os.AsyncTask;

import org.json.*;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonParser extends AsyncTask {

    @Override
    protected Object doInBackground(Object[] objects){
        JSONObject obj = null;
        String miasto= Settings.cities.get(Settings.index);
        String units= Settings.units;
        String appkey="&appid=6cbb4d139a4ccc8e697a07432728584c";
        String url="https://api.openweathermap.org/data/2.5/weather?q="+miasto +appkey
                + units;
        String url2="https://api.openweathermap.org/data/2.5/forecast?q="+miasto+appkey+units;
        try {
            System.out.println(url);
            obj = readJsonFromUrl(url);
            if(obj.getString("cod").equals("404")){

            }
            else {
                Settings.lon = obj.getJSONObject("coord").getString("lon");
                Settings.lat = obj.getJSONObject("coord").getString("lat");
                Settings.temp = obj.getJSONObject("main").getString("temp")+ Settings.units1;
                Settings.pressure = obj.getJSONObject("main").getString("pressure") +" hPa";
                Settings.name = obj.getString("name");
                Settings.speed = obj.getJSONObject("wind").getString("speed") + Settings.units2;
                try{
                Settings.direction = obj.getJSONObject("wind").getString("deg");
                }catch (Exception e){}
                Settings.humidity = obj.getJSONObject("main").getString("humidity")+ " %";
                Settings.visibility =String.valueOf(Double.parseDouble(obj.getString("visibility"))/100)+" %";


                JSONArray arr = obj.getJSONArray("weather");
                Settings.description = arr.getJSONObject(0).getString("description");
                Settings.image= arr.getJSONObject(0).getString("icon");

                System.out.println(obj.getJSONObject("coord").getString("lon"));
                obj = readJsonFromUrl(url2);
                arr = obj.getJSONArray("list");
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
                String currentdate = simpleDateFormat.format(date);
                int index = 0;
                for (int i = 0; i < arr.length(); i++) {
                    if (arr.getJSONObject(i).getString("dt_txt").split(" ")[1].equals("12:00:00")) {
                        if (arr.getJSONObject(i).getString("dt_txt").split(" ")[0].equals(currentdate)) {
                            index = i + 8;
                            break;
                        } else {
                            index = i;
                            break;
                        }

                    }
                }
                Settings.tomorrow = arr.getJSONObject(index).getString("dt_txt").split(" ")[0];
                Settings.temperature1 = arr.getJSONObject(index).getJSONObject("main").getString("temp")+ Settings.units1;
                Settings.pressure1 = arr.getJSONObject(index).getJSONObject("main").getString("pressure").substring(0,4)+"hPa";

                Settings.dayAfterTomorrow = arr.getJSONObject(index + 8).getString("dt_txt").split(" ")[0];
                Settings.temperature2 = arr.getJSONObject(index + 8).getJSONObject("main").getString("temp")+ Settings.units1;
                Settings.pressure2 = arr.getJSONObject(index+8).getJSONObject("main").getString("pressure").substring(0,4)+"hPa";

                Settings.twoDaysAfterTomorrow = arr.getJSONObject(index + 16).getString("dt_txt").split(" ")[0];
                Settings.temperature3 = arr.getJSONObject(index + 16).getJSONObject("main").getString("temp")+ Settings.units1;
                Settings.pressure3 = arr.getJSONObject(index+16).getJSONObject("main").getString("pressure").substring(0,4)+"hPa";

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

}
