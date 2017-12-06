package com.mobilecomputing.sbarth.mtgcardscanner;

/**
 * Library.java
 *
 * Sam Barth, Scott Hoefer, Cole Petersen
 */

import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Library extends AppCompatActivity {
    @Override

    /**
     * onCreate:
     * Initializes the Library activity.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout1);
        String usersCards = getCards("userLib.txt");
        getImages(usersCards, layout);
    }

    /**
     * getCards:
     * Returns a String of scanned cards.
     */
    private String getCards(String filename) {
        int ch;
        StringBuffer fileContent = new StringBuffer("");
        FileInputStream fis;
        try {
            fis = openFileInput( filename );
            try {
                while( (ch = fis.read()) != -1)
                    fileContent.append((char)ch);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String data = new String(fileContent);
        Log.i("fileContents", data);
        return data;
    }

    /**
     * getImages:
     * Displays images of scanned cards.
     */
    private void getImages(String cards, LinearLayout layout) {
        String usersCardsList[] = cards.split("\\r\\n|\\n|\\r");
        for (String cardName : usersCardsList) {
            ImageView i = new ImageView(this);
            i.setAdjustViewBounds(true);
            try {
                InputStream is = getAssets().open(cardName);
                Drawable d = Drawable.createFromStream(is, null);
                i.setImageDrawable(d);
                layout.addView(i);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
