package com.example.rishad.iotd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonGetListOfImages = (Button) findViewById(R.id.bGetListOfImages);
        buttonGetListOfImages.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityGetListOfImages.class);
                startActivity(intent);
            }
        });


        Button buttonGetTodaysImage = (Button) findViewById(R.id.bGetTodaysImage);
        buttonGetTodaysImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityGetTodaysImage.class);
                startActivity(intent);
            }
        });


        Button buttonViewImages = (Button) findViewById(R.id.bViewImages);
        buttonViewImages.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityViewImages.class);
                startActivity(intent);
            }
        });


        Button buttonDeleteImages = (Button) findViewById(R.id.bDeleteImages);
        buttonDeleteImages.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityDeleteImages.class);
                startActivity(intent);
            }
        });
    }

}
