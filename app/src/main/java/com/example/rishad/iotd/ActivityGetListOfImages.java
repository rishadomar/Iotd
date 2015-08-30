package com.example.rishad.iotd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

public class ActivityGetListOfImages extends Activity {

    private TextView mText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_images);

        mText = (TextView) findViewById(R.id.textListOfImages);

        Button buttonDoneListOfImages = (Button) findViewById(R.id.bDoneListOfImages);
        buttonDoneListOfImages.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });


        File dir = getFilesDir();
        File[] subFiles = dir.listFiles();
        String fileNames = "Files: ";
        if (subFiles != null) {
            for (File file : subFiles) {
                fileNames += file.getName() + ", ";
            }
        }
        mText.setText(fileNames);
    }

}
