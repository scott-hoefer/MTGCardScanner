package com.mobilecomputing.sbarth.mtgcardscanner;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class CardInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_info);

        String query = "";
        String cardList[];
        String name;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                name = extras.getString("cardName");
                query = extras.getString("query");
                cardList = extras.getStringArray("cardList");
            } else {
                name = "Error";
            }
        } else {
            name = (String) savedInstanceState.getSerializable("CardName");
            query = (String) savedInstanceState.getSerializable("query");
            cardList = savedInstanceState.getStringArray("cardList");
        }

        Button searchBtn = (Button) findViewById(R.id.btnSearchCard);
        final String finalQuery = query;
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, finalQuery); // query contains search string
                startActivity(intent);
            }
        });

        setImage(name);
    }

    private void setImage(String filename) {
        ImageView iv = (ImageView) findViewById(R.id.cardInfoImageView);
        try {
            InputStream is = getAssets().open(filename);
            Drawable d = Drawable.createFromStream(is, null);
            iv.setImageDrawable(d);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
