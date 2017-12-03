package com.mobilecomputing.sbarth.mtgcardscanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SearchResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_results);
        Button btnReturnMain = (Button) findViewById(R.id.btnReturnMain);
        btnReturnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchResults.this, MainActivity.class);
                startActivity(intent);
            }
        });


//        setContentView(R.layout.activity_search_results);
        Toast.makeText(this, "Creating Histogram...", Toast.LENGTH_SHORT).show();
        Bitmap bmp = null;
        String filename = getIntent().getStringExtra("image");
        try {
            FileInputStream inStream = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(inStream);
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ImagePreprocessor.processImage(bmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
