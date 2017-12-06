package com.mobilecomputing.sbarth.mtgcardscanner;

/**
 * SearchResults.java
 *
 * Sam Barth, Scott Hoefer, Cole Petersen
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class SearchResults extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView ranking;
    Button btnReturnMain;
    String filename;
    ArrayAdapter adapter;
    Bitmap bmp = null;

    /**
     * onCreate:
     * Initializes the Search Results activity.
     */
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
        ranking.setOnItemClickListener(this);

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

        displayColorHistogramRankings();
    }

    /**
     * displayRankings:
     * Displays an ArrayList of cards, sorted from most to least similar to captured image.
     */
    private void displayColorHistogramRankings() {
        Scanner sc = new Scanner(getResources().openRawResource(R.raw.ixl_cardart_chist));
        ArrayList<HistogramTuple> arr =  ImagePreprocessor.getHistogramRanking(bmp, sc);
        adapter = new ArrayAdapter(SearchResults.this, R.layout.rankings_text_view, arr);
        ranking.setAdapter(adapter);
        sc.close();
    }

    private void displayJ1HistogramRankings() {
        Scanner sc = new Scanner(getResources().openRawResource(R.raw.ixl_cardart_j1hist));
        ArrayList<HistogramTuple> arr =  ImagePreprocessor.getJ1HistogramRanking(bmp, sc);
        adapter = new ArrayAdapter(SearchResults.this, R.layout.rankings_text_view, arr);
        ranking.setAdapter(adapter);
        sc.close();
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

    /**
     * onItemClick:
     * Opens the selected card from the ArrayList
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(SearchResults.this, CardImage.class);
        HistogramTuple ht = (HistogramTuple) ranking.getItemAtPosition(position);
        String cardName = ht.getName();
        intent.putExtra("cardName", cardName);
        intent.putExtra("pos", position);
        startActivity(intent);
    }
}
