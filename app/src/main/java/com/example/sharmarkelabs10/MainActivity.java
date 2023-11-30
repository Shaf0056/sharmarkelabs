package com.example.sharmarkelabs10;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.example.sharmarkelabs10.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    protected String cityName;
    protected String apiKey;
    protected RequestQueue queue = null;
    String description = null;
    String iconName = null;
    double current = 0;
    double max = 0;
    double min = 0;
    int humidity = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(this);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.btnForecast.setOnClickListener(click -> {
            cityName = binding.etCity.getText().toString();
            apiKey = "7e943c97096a9784391a981c4d878b22";
            String url = null;
            try {
                url = "https://api.openweathermap.org/data/2.5/weather?q="
                        + URLEncoder.encode(cityName, "UTF-8")
                        + "&appid=" + apiKey + "&units=metric";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (response) -> {
                        try {
                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject position0 = weatherArray.getJSONObject(0);
                            description = position0.getString("description");
                            iconName = position0.getString("icon");
                            JSONObject mainObject = response.getJSONObject("main");
                            current = mainObject.getDouble("temp");
                            min = mainObject.getDouble("temp_min");
                            max = mainObject.getDouble("temp_max");
                            humidity = mainObject.getInt("humidity");
                            String imageUrl = "http://openweathermap.org/img/w/"+ iconName + ".png";
                            ImageRequest imgReq = new ImageRequest(imageUrl, (bitmap) -> {
                                try {
                                    FileOutputStream fOut = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                    fOut.flush();
                                    fOut.close();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                runOnUiThread(() -> {
                                    binding.icon.setImageBitmap(bitmap);
                                    binding.icon.setVisibility(View.VISIBLE);
                                });
                            }, 1024, 1024, ImageView.ScaleType.CENTER, null,
                                    (error) -> {});
                            queue.add(imgReq);
                            runOnUiThread(() -> {
                                binding.temp.setText("The current temperature is " + current);
                                binding.temp.setVisibility(View.VISIBLE);
                                binding.maxTemp.setText("The max temperature is " + max);
                                binding.maxTemp.setVisibility(View.VISIBLE);
                                binding.minTemp.setText("The min temperature is " + min);
                                binding.minTemp.setVisibility(View.VISIBLE);
                                binding.humidity.setText("The humidity is " + humidity);
                                binding.humidity.setVisibility(View.VISIBLE);
                                binding.description.setText(description);
                                binding.description.setVisibility(View.VISIBLE);
//                                binding.icon.setImageBitmap(bitmap);
//                                binding.icon.setVisibility(View.VISIBLE);
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    (error) -> {});
            queue.add(request);
        });
    }
}