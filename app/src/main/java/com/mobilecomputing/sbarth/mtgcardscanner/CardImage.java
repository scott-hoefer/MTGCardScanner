package com.mobilecomputing.sbarth.mtgcardscanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CardImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_image);

        String name;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                name = extras.getString("cardName");
            } else {
                name = "Error";
            }
        } else {
            name = (String) savedInstanceState.getSerializable("CardName");
        }

        TextView t = (TextView) findViewById(R.id.nameTextView);
        t.setText(name);
    }
}
