package com.mobilecomputing.sbarth.mtgcardscanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Scanner;

public class SearchResults extends AppCompatActivity {

    ListView ranking;
    Button btnReturnMain;
    String filename;
    ArrayAdapter adapter;
    Bitmap bmp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_results);
        btnReturnMain = (Button) findViewById(R.id.btnReturnMain);
        btnReturnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchResults.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ranking = (ListView) findViewById(R.id.rankingsListView);

//        setContentView(R.layout.activity_search_results);
        Toast.makeText(this, "Creating Histogram...", Toast.LENGTH_SHORT).show();
        filename = getIntent().getStringExtra("image");
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

        displayRankings();
    }

    private void displayRankings() {
//        InputStream i = getResources().openRawResource(getResources().getIdentifier("CN2-cardart-chist", "carddata", getPackageName()));
//        Uri u = Uri.parse("android.resource://mobilecomputing.sbarth.mtgcardscanner/raw/cn2_cardart_chist.csv");
//        Log.i("uri path", u.getPath());
        Scanner sc = new Scanner(getResources().openRawResource(R.raw.cn2_cardart_chist));
        ArrayList<HistogramTuple> arr =  ImagePreprocessor.getHistogramRanking(bmp, sc);
        adapter = new ArrayAdapter(SearchResults.this, R.layout.rankings_text_view, arr);
        ranking.setAdapter(adapter);
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
