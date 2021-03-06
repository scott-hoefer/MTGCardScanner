package com.mobilecomputing.sbarth.mtgcardscanner;

/**
 * CardImage.java
 *
 * Sam Barth, Scott Hoefer, Cole Petersen
 */

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;


public class CardImage extends AppCompatActivity {

    /**
     * onCreate:
     * Initializes the Card Image activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_image);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        final String name;
        int rank = 0;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                name = extras.getString("cardName");
                rank = extras.getInt("pos");
            } else {
                name = "Error";
            }
        } else {
            name = (String) savedInstanceState.getSerializable("CardName");
            rank = (int) savedInstanceState.getSerializable("pos");
        }

        TextView t = (TextView) findViewById(R.id.nameTextView);
        t.setText("Rank " + (rank+1));

        setImage(name);

        Button addToLibBtn = (Button) findViewById(R.id.btnAddToLib);
        addToLibBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCard(name);
            }
        });
    }

    /**
     * addCard:
     * Adds a card to the library.
     */
    private void addCard(String name) {
        String filename = "userLib.txt";
        FileOutputStream os;
        String separator = System.getProperty("line.separator");
        try {
            os = openFileOutput(filename, Context.MODE_APPEND);
            OutputStreamWriter osw = new OutputStreamWriter(os);
            osw.append(name);
            osw.append(separator);
            osw.flush();
            osw.close();
            os.close();
            Toast.makeText(this,"Card added to library", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * setImage:
     * Displays image of a card in the library.
     */
    private void setImage(String filename) {
        ImageView iv = (ImageView) findViewById(R.id.cardImageView);
        try {
            InputStream is = getAssets().open(filename);
            Drawable d = Drawable.createFromStream(is, null);
            iv.setImageDrawable(d);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * onConfigurationChanged:
     * Sets orientation to Portrait.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
