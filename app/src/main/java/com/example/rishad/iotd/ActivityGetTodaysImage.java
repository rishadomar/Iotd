package com.example.rishad.iotd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ActivityGetTodaysImage extends AppCompatActivity {
    private TextView mTextResult = null;

    class ImageData {
        public String base = null;
        public String url = null;
        public String filename = null;
        public String description = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_todays_image);

        mTextResult = (TextView) findViewById(R.id.textResult);
        Button buttonDone = (Button) findViewById(R.id.bDone);
        buttonDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (hasTodaysFile()) {
            mTextResult.setText("Today's file already exists");
        } else {
            makeApiCall();
        }
    }

    private Boolean hasTodaysFile() {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        File dir = getFilesDir();
        File[] subFiles = dir.listFiles();
        if (subFiles != null) {
            for (File file : subFiles) {
                String fileDate = new SimpleDateFormat("yyyy-MM-dd").format(file.lastModified());
                if (today.equals(fileDate)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void makeApiCall() {

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=en-US";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: " + response.substring(0,500));
                        ImageData imageData = getImageData(response);
                        imageData.base = "http://www.bing.com";
                        mTextResult.setText("Getting: " + imageData.description);
                        setCurrentImage(imageData);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextResult.setText("That didn't work! status code = " + error.networkResponse.statusCode);
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void setCurrentImage(ImageData imageData) {
        final ImageView imageView = (ImageView) findViewById(R.id.imageDisplay);
        ImageDownloader imageDownLoader = new ImageDownloader(imageView, imageData.filename);
        imageDownLoader.execute(imageData.base + "/" + imageData.url);
    }

    private ImageData getImageData(String jsonResponse) {
        try {
            ImageData imageData = new ImageData();
            JSONObject reader = new JSONObject(jsonResponse);
            JSONArray imagesArray = reader.getJSONArray("images");
            JSONObject imagesObject = imagesArray.getJSONObject(0);
            imageData.url = imagesObject.getString("url");
            imageData.description = imagesObject.getString("copyright");
            imageData.filename = new File(imagesObject.getString("url")).getName();
            return imageData;
        } catch (JSONException e) {
            return null;
        }
    }

    private class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        String mFilename;

        public ImageDownloader(ImageView bmImage, String filename) {
            this.bmImage = bmImage;
            this.mFilename = filename;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("MyApp", e.getMessage());
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            FileOutputStream out = null;
            try {
                out = openFileOutput(mFilename, Context.MODE_PRIVATE);
                result.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    mTextResult.setText("Yay done");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
