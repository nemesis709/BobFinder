package edu.skku.testapp.bobfinder;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

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
import java.util.ArrayList;

public class menuboard extends AppCompatActivity {

    String Store;
    String Number;
    String Location;
    String URL;
    TextView menu1;
    TextView menu2;
    TextView menu3;
    TextView price1;
    TextView price2;
    TextView price3;
    double MYLAT;
    double MYLNG;
    int Stand_People;
    int Stand_Price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuboard);
        Intent result=getIntent();
        Store=result.getStringExtra("Store");
        Number=result.getStringExtra("ID");
        Location=result.getStringExtra("LOC");
        MYLAT=result.getDoubleExtra("MYLAT",0);
        MYLNG=result.getDoubleExtra("MYLNG",0);
        Stand_People=result.getIntExtra("people",1);
        Stand_Price=result.getIntExtra("price",8000);

        URL="something";
        HttpTask httpTask_menu = new HttpTask();
        httpTask_menu.execute();


        Button map_btn = findViewById(R.id.map);

        map_btn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), openmap.class);
            intent.putExtra("LOC",Location);
            intent.putExtra("Store",Store);
            intent.putExtra("MYLNG",MYLNG);
            intent.putExtra("MYLAT",MYLAT);
            startActivity(intent);

        });

        Button rtn_btn = findViewById(R.id.home);

        rtn_btn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
    }

    class HttpTask extends AsyncTask<String,String,String> {

        ProgressDialog asyncDialog = new ProgressDialog(menuboard.this);

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
                http.setRequestMethod("POST"); //전송방식
                http.setDoOutput(true); //데이터를 쓸 지 설정
                http.setDoInput(true); //데이터를 읽어올지 설정
                OutputStream os = http.getOutputStream();
                String POST="storeno="+Number;
                os.write(POST.getBytes(StandardCharsets.UTF_8)); // 출력 스트림에 출력.
                os.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
                os.close();
                http.setConnectTimeout(10*1000);
                http.setReadTimeout(10*1000);

                BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder stringBuffer = new StringBuilder();
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
            menu1=findViewById(R.id.menu1);
            menu2=findViewById(R.id.menu2);
            menu3=findViewById(R.id.menu3);
            price1=findViewById(R.id.price1);
            price2=findViewById(R.id.price2);
            price3=findViewById(R.id.price3);
            JSONObject json;
            try {
                json = new JSONObject(values[0]);
                JSONArray j1 = json.getJSONArray("data");

                ArrayList<String> NAMELIST = new ArrayList<> ();
                ArrayList<String> PRICELIST = new ArrayList<> ();

                for(int i=0;i<j1.length();i++){
                    JSONObject Obj = j1.getJSONObject(i);
                    String name = (String) Obj.get("MENU_NAME");
                    String price = (String) Obj.get("MENU_PRICE");
                    price = price.replace(",","");
                    int price_i = Integer.parseInt(price);
                    if(((price_i>Stand_Price*0.8)&&(price_i<=Stand_Price))||((price_i>Stand_Price*Stand_People*0.8)&&(price_i<=Stand_Price*Stand_People))){
                        NAMELIST.add(name);
                        PRICELIST.add(price);
                    }
                }

                Log.d("Stand1", NAMELIST.get(0));
                Log.d("Size", String.valueOf(NAMELIST.size()));
                JSONObject m1 = j1.getJSONObject(0);
                JSONObject m2 = j1.getJSONObject(1);
                JSONObject m3 = j1.getJSONObject(2);
                String n1 = (String) m1.get("MENU_NAME");
                String p1 = (String) m1.get("MENU_PRICE");
                String n2 = (String) m2.get("MENU_NAME");
                String p2 = (String) m2.get("MENU_PRICE");
                String n3 = (String) m3.get("MENU_NAME");
                String p3 = (String) m3.get("MENU_PRICE");

                try {
                    menu1.setText(NAMELIST.get(0));
                    price1.setText(PRICELIST.get(0));
                }catch (Exception e){
                    menu1.setText(n1);
                    price1.setText(p1);
                }

                try {
                    menu2.setText(NAMELIST.get(1));
                    price2.setText(PRICELIST.get(1));
                }catch (Exception e){
                    menu2.setText(n2);
                    price2.setText(p2);
                }

                try {
                    menu3.setText(NAMELIST.get(2));
                    price3.setText(PRICELIST.get(2));
                }catch(Exception e){
                    menu3.setText(n3);
                    price3.setText(p3);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            asyncDialog.dismiss();
        }

    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        HttpTask httpTask = new HttpTask();
        httpTask.execute();
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onPause() {
        super.onPause();
    }
}

