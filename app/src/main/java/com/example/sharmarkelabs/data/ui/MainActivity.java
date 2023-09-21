package com.example.sharmarkelabs.data.ui;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

import com.example.sharmarkelabs.data.data.MainViewModel;
import com.example.sharmarkelabs.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding variableBinding;
    private MainViewModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        variableBinding.mybutton.setOnClickListener(click ->
        {
            model.editString.postValue(variableBinding.myedittext.getText().toString());
        });
        model.editString.observe(this, s -> {
            variableBinding.text1.setText("Your edit text has: " + s);
        });

        model.isSelected.observe(this, selected -> {
            variableBinding.CheckBox.setChecked(selected);
            variableBinding.RadioButton.setChecked(selected);
            variableBinding.Switch.setChecked(selected);

            String message = "The value is now: " + selected;
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        });
        variableBinding.CheckBox.setOnCheckedChangeListener((CheckBox, isChecked)-> {
            model.isSelected.postValue(variableBinding.CheckBox.isChecked());
        });

        variableBinding.RadioButton.setOnCheckedChangeListener((RadioButton, isChecked)-> {
            model.isSelected.postValue(variableBinding.RadioButton.isChecked());
        });

        variableBinding.Switch.setOnCheckedChangeListener((Switch, isChecked)-> {
            model.isSelected.postValue(variableBinding.Switch.isChecked());
        });

        variableBinding.ImageView.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "ImageView clicked", Toast.LENGTH_SHORT).show();
        });
        variableBinding.ImageButton.setOnClickListener(view -> {
            int width = view.getWidth();
            int height = view.getHeight();
            String message = "The width = " + width + " and height = " + height;
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        });

    }

}










