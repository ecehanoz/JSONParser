package com.example.ecehanoz.jsonparser;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView text;
    EditText isim;
    Button goster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView)findViewById(R.id.textView);
        isim = (EditText)findViewById(R.id.editText);
        goster = (Button)findViewById(R.id.button);

        goster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HavaDurumu().execute();
            }
        });

    }

    private class HavaDurumu extends AsyncTask<String,Void,String> {
        int tempNo;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings){
            String result="";
            URL url;
            HttpURLConnection httpURLConnection;
            try{
                url=new URL(strings[0]);
                httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();
                while(data>0){
                    char character = (char) data;
                    result += character;
                    data = inputStreamReader.read();
                }
                return result;

            }
            catch (Exception e){
                return null;
            }
        }
        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            try{
                getRates();
                JSONObject jsonObject = new JSONObject(s);
                JSONArray listArray = jsonObject.getJSONArray("list");
                JSONObject firstObj = listArray.getJSONObject(0);
                JSONObject mainObj = firstObj.getJSONObject("main");
                tempNo = mainObj.getInt("temp");
                text.setText(tempNo - 273 +" \u2103");

            }catch(Exception e){

            }
        }
    }
    public void getRates(){
        HavaDurumu havaDurumu = new HavaDurumu();
        try {
            String url = "http://api.openweathermap.org/data/2.5/find?q="+isim.getText()+"&appid=b5d36679a9e4dbcb266c7a7f24fdd94e";
            havaDurumu.execute(url);
        }catch (Exception e){

        }

    }
}
