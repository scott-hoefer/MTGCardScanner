package com.mobilecomputing.sbarth.mtgcardscanner;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Library extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout1);
        String usersCards = getCards("userLib.txt");
        getImages(usersCards, layout);
    }

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

    private void getImages(String cards, LinearLayout layout) {
        final String usersCardsList[] = cards.split("\\r\\n|\\n|\\r");
        List<String> eliminateDuplicatePics = new ArrayList<>();
        for (final String cardName : usersCardsList) {
            ImageView i = new ImageView(this);
            i.setAdjustViewBounds(true);
            final String query = cardName.substring(0, cardName.length()-4) + " mtg";
            i.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cardClicked(query, usersCardsList, cardName);
                }
            });
            try {
                if (!eliminateDuplicatePics.contains(cardName)) {
                    InputStream is = getAssets().open(cardName);
                    Drawable d = Drawable.createFromStream(is, null);
                    i.setImageDrawable(d);
                    layout.addView(i);
                    is.close();
                    eliminateDuplicatePics.add(cardName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void cardClicked(String query, String cardList[], String name) {
        Intent intent = new Intent(Library.this, CardInfo.class);
        intent.putExtra("query", query);
        intent.putExtra("cardList", cardList);
        intent.putExtra("cardName", name);
        Log.i("cardName", name);
        startActivity(intent);
    }
}
