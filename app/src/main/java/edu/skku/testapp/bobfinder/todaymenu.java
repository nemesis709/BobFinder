package edu.skku.testapp.bobfinder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

public class todaymenu extends AppCompatActivity {

    String Result_Store;
    String Result_Store_id;
    String Result_Store_location;
    TextView food_name;
    String URL;
    String URL_LOC;
    String cat;
    String pep;
    String prc;
    double MYLAT;
    double MYLNG;
    LocationManager locationManager;
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            URL="something";
            HttpTask httpTask_food = new HttpTask();
            try {
                httpTask_food.execute().get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onStatusChanged(String Provider, int stauts, Bundle extraws){

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(checkPermission()){
            requestLocation();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todaymenu);
        TextView food_cat= findViewById(R.id.foodcat);
        Intent value=getIntent();

        cat=value.getStringExtra("Cat");
        pep=value.getStringExtra("People");
        prc=value.getStringExtra("Price");
        prc=prc.replace(",","");
        int price_i = Integer.parseInt(prc);
        int people_i = Integer.parseInt(pep);
        food_cat.setText(cat);


        Button show_btn = findViewById(R.id.button1);

        show_btn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), menuboard.class);
            intent.putExtra("Store",Result_Store);
            intent.putExtra("ID",Result_Store_id);
            intent.putExtra("LOC",Result_Store_location);
            intent.putExtra("MYLAT",MYLAT);
            intent.putExtra("MYLNG",MYLNG);
            intent.putExtra("price",price_i);
            intent.putExtra("people",people_i);
            startActivity(intent);
        });

        Button rtn_btn = findViewById(R.id.button2);

        rtn_btn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
    }

    class HttpTask extends AsyncTask<String,String,String> {
        ProgressDialog asyncDialog = new ProgressDialog(todaymenu.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setCancelable(false);
            asyncDialog.setCanceledOnTouchOutside(false);
            asyncDialog.setMessage("로딩중!");
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL loc= new URL(URL);
                HttpURLConnection http= (HttpURLConnection) loc.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                Log.d("background cat:",cat);
                OutputStream os = http.getOutputStream();
                String POST="type="+cat+"&price="+prc+"people"+pep;
                os.write(POST.getBytes(StandardCharsets.UTF_8));
                os.flush();
                os.close();
                http.setConnectTimeout(10*1000);
                http.setReadTimeout(10*1000);

                BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8));
                StringBuffer stringBuffer = new StringBuffer();
                String line;
                StringBuilder result = new StringBuilder();
                while((line = reader.readLine())!=null){
                    result.append(line);
                }

                stringBuffer.append(result);
                publishProgress(stringBuffer.toString());


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            food_name=findViewById(R.id.foodname);
            JSONObject json;
            try {
                json = new JSONObject(values[0]);
                JSONArray j1 = json.getJSONArray("data");
                JSONObject j2 = j1.getJSONObject(0);
                String result_store = (String) j2.get("STORE_NAME");
                int r_id = (int) j2.get("STORE_NO");
                String result_id = String.valueOf(r_id);
                String result_location = (String) j2.get("STORE_LOCATION");
                Result_Store=result_store;
                Result_Store_id=result_id;

                URL_LOC="https://maps.googleapis.com/maps/api/geocode/json?address="+result_location+"&key=AIzaSyDH62BVTuLtnIW78SgZ93jB4J2ji_YqPp8";
                food_name.setText(result_store);
                HttpTask2 httpTask_loc = new HttpTask2();
                httpTask_loc.execute().get();
            } catch (JSONException | InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            asyncDialog.dismiss();
        }
    }


    class HttpTask2 extends AsyncTask<String,String,String>{
        ProgressDialog asyncDialog = new ProgressDialog(todaymenu.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setCancelable(false);
            asyncDialog.setCanceledOnTouchOutside(false);
            asyncDialog.setMessage("로딩중!");
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL loc= new URL(URL_LOC);
                HttpURLConnection http= (HttpURLConnection) loc.openConnection();
                http.setRequestMethod("GET");
                http.setConnectTimeout(10*1000);
                http.setReadTimeout(10*1000);

                BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder stringBuffer = new StringBuilder();
                String line;
                StringBuilder result = new StringBuilder();
                while((line = reader.readLine())!=null){
                    result.append(line);
                }
                JSONObject json;
                json = new JSONObject(result.toString());
                JSONArray j1 = json.getJSONArray("results");
                JSONObject j2 = j1.getJSONObject(0);
                JSONObject j3 = j2.getJSONObject("geometry");
                JSONObject j4 = j3.getJSONObject("location");
                stringBuffer.append(j4);
                publishProgress(stringBuffer.toString());


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Result_Store_location=values[0];
            Log.d("loc result",Result_Store_location);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            asyncDialog.dismiss();
        }
    }

    private boolean checkPermission(){
        return (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) && (checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1213) {
            if (!checkPermission()) {
                finish();
            }
            else{
                requestLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void requestLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

        assert locationManager != null;
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            MYLAT = location.getLatitude();
            MYLNG = location.getLongitude();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if(locationManager != null)
            locationManager.removeUpdates(locationListener);

    }
}