package com.example.sharmarkelabs.data.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.sharmarkelabs.R;

public class MainActivity extends AppCompatActivity {

    Button loginButton ;
    EditText emailtext;
    Intent nextPage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w( "MainActivity", "In onCreate() - Loading Widgets" );
        loginButton = findViewById(R.id.button);
        emailtext = findViewById(R.id.editTextTextEmailAddress);
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String emailAddress = prefs.getString("LoginName", "");
        emailtext.setText(emailAddress);

        loginButton.setOnClickListener( clk-> {
            String EmailAddress = emailtext.getText().toString();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("LoginName", EmailAddress);
            editor.apply();
            nextPage = new Intent(MainActivity.this,SecondActivity.class);
            nextPage.putExtra("EmailAddress",emailtext.getText().toString());
            startActivity( nextPage);
        });}
    @Override
    protected void onStart() {
        super.onStart();
        Log.w("MainActivity", "onStart() - The activity is now visible on screen");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume() - The activity has focus and the user can interact with it");

    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "In onPause() - The application no longer responds to user input");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.w("MainActivity","In onStop() - The application is no longer visible.");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w("MainActivity", "onDestroy() - The activity is being destroyed");

    }








        }













