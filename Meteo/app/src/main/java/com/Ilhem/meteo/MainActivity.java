package com.Ilhem.meteo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
EditText seletreg;
    String City = "Tunis";
    String nameIcon = "10d";
    String Key = "2a20d2a71c5a0737f88861d43949d630";
    TextView txtCity,txtTime,txtValue,txtValueFeelLike,txtValueHumidity,txtValueVision;
    ImageView imgIcon;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seletreg=findViewById(R.id.rech);
        b1=findViewById(R.id.button);

        txtCity = findViewById(R.id.txtCity);

        txtTime = findViewById(R.id.txtTime);

        txtValue = findViewById(R.id.txtValue);

        txtValueFeelLike = findViewById(R.id.txtTitleFeelLike);

        txtValueHumidity = findViewById(R.id.txtValueHumidity);

        txtValueVision = findViewById(R.id.txtValueVision);

        imgIcon = findViewById(R.id.imgIcon);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String     Cityy = seletreg.getText().toString();
                loading(Cityy);

            }
        });






    }

    public class DownloadTask extends AsyncTask<String, Void , String> {
        @Override
        protected String doInBackground(String... strings) {

            String result = "";

            URL url;

            HttpURLConnection httpURLConnection;

            InputStream inputStream;

            InputStreamReader inputStreamReader;

            try {

                url = new URL(strings[0]);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                inputStream = httpURLConnection.getInputStream();

                inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while(data != -1) {

                    result += (char) data;

                    data = inputStreamReader.read();

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }
    }

    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;

            URL url;

            HttpURLConnection httpURLConnection;

            InputStream inputStream;

            try {
                Log.i("LINK",strings[0]);
                url = new URL(strings[0]);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                inputStream = httpURLConnection.getInputStream();

                bitmap = BitmapFactory.decodeStream(inputStream);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }
    }
    public void loading(String city) {
        String url="http://api.openweathermap.org/data/2.5/weather?q="+city+"&units=metric&appid=d6d5be667f30c9e4914aa7b85786546e";

        DownloadTask downloadTask = new DownloadTask();

        try {

            String result = "abc";

            result = downloadTask.execute(url).get();

            Log.i("Result:",result);

            JSONObject jsonObject = new JSONObject(result);

            JSONObject main = jsonObject.getJSONObject("main");

            String temp = main.getString("temp");


            String humidity = main.getString("humidity");

            String feel_like = main.getString("feels_like");

            String visibility = jsonObject.getString("visibility");

            nameIcon = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");

            Long time = jsonObject.getLong("dt");

            String sTime = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.ENGLISH)
                    .format(new Date(time * 1000));

            txtCity.setText(city);

            txtValue.setText(temp + "°");
txtTime.setText(sTime);
            txtValueVision.setText(visibility);

            txtValueHumidity.setText(humidity);

            txtValueFeelLike.setText(feel_like);

            DownloadImage downloadImage = new DownloadImage();

            String urlIcon = " https://openweathermap.org/img/wn/"+ nameIcon +"@2x.png";

            Bitmap bitmap = downloadImage.execute(urlIcon).get();

            imgIcon.setImageBitmap(bitmap);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
   }