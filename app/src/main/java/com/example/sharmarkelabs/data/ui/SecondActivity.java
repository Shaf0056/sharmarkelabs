package com.example.sharmarkelabs.data.ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sharmarkelabs.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class SecondActivity extends AppCompatActivity {
    Button CallNum;
    Intent nextPage;
    Button cam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent fromPrevious = getIntent();
        String EmailAddress = fromPrevious.getStringExtra("EmailAddress");

        CallNum = findViewById(R.id.button3);
        cam = findViewById(R.id.button4);
        TextView textView = findViewById(R.id.textView);
        Button changePic = findViewById(R.id.button4);
        ImageView img = findViewById(R.id.imageView);
        textView.setText("Welcome back " + EmailAddress);
        EditText PhoneNum = findViewById(R.id.editTextPhone2);
        CallNum.setOnClickListener(clk -> {
            String number = PhoneNum.getText().toString();
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + number));
            startActivity(call);
        });

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

            @Override

            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() == Activity.RESULT_OK) {

                    Intent data = result.getData();
                    Bitmap thumbnail = data.getParcelableExtra("data");
                    img.setImageBitmap(thumbnail);

                    FileOutputStream fOut = null;

                    try {
                        fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);

                        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                        fOut.flush();
                        fOut.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }

            }

        });
        File file = new File(getFilesDir(), "Picture.png");
        if (file.exists()) {
            Bitmap theImage = BitmapFactory.decodeFile(file.getAbsolutePath());
            img.setImageBitmap(theImage);

        }

        cam.setOnClickListener(clk -> {
            cameraResult.launch(cameraIntent);

        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        EditText phoneNumber = findViewById(R.id.editTextPhone2);
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String newPhoneNumber = phoneNumber.getText().toString();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("PhoneNumber", newPhoneNumber);
        editor.apply();
    }
}