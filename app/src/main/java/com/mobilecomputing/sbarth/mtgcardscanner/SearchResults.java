package com.mobilecomputing.sbarth.mtgcardscanner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SearchResults extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
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
            processImage(bmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int[][][] processImage(Bitmap image) throws Exception {
        int picw = image.getWidth();
        int pich = image.getHeight();
        int[][][] ch = new int[4][4][4];
        //Bitmap image = BitmapFactory.decodeFile(f.getPath());
        for(int x = 0; x < picw ; x++)
            for(int y = 0; y < pich ; y++) {
                int pixel = image.getPixel(x, y);
                int red = Color.red(pixel);
                int blue = Color.blue(pixel);
                int green = Color.green(pixel);
                int alpha = Color.alpha(pixel);
                ch[red / 128][green / 128][blue / 128]++;
            }
        for(int i = 0; i < ch.length; i++)
            for(int j = 0; j < ch[i].length; j++)
                for(int p = 0; p < ch[i][j].length; p++)
                    Log.i("histogram", "t[" + i + "][" + j + "][" + p + "] = " + ch[i][j][p]);
        Toast.makeText(this, "Finished processing image", Toast.LENGTH_SHORT).show();
        return ch;
    }
}
