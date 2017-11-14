package com.mobilecomputing.sbarth.mtgcardscanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    File image;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = "temp"; //new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",         /* suffix */
            storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void dispatchTakePictureIntent() {
        Toast.makeText(this, "Ready to dispatch intent to take a picture", Toast.LENGTH_SHORT).show();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
//            File photoFile = null;
            try {
//                photoFile = createImageFile();
                createImageFile();
            } catch (IOException e) {
                // Error occurred while creating the File
                e.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (image != null) {
                try {
                    Uri photoURI = FileProvider.getUriForFile(this, "com.mobilecomputing.sbarth.mtgcardscanner", image);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
        Toast.makeText(this, "Ready to process image", Toast.LENGTH_SHORT).show();

    }

    public int[][][] processImage(File f) throws Exception {
        int picw = 200;
        int pich = 285;
        int[][][] ch = new int[4][4][4];
        Bitmap image = BitmapFactory.decodeFile(f.getPath());
//        while (true) {
//            if (image == null) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            else break;
//        }
        for(int x = 0; x < picw ; x++)
            for(int y = 0; y < pich ; y++) {
                int pixel = image.getPixel(x, y);
                int red = Color.red(pixel);
                int blue = Color.blue(pixel);
                int green = Color.green(pixel);
                int alpha = Color.alpha(pixel);
//                int alpha = (color & 0xff000000) >> 24;
//                int red = (color & 0x00ff0000) >> 16;
//                int green = (color & 0x0000ff00) >> 8;
//                int blue = color & 0x000000ff;
                ch[red / 64][green / 64][blue / 64]++;
            }
        for(int i = 0; i < ch.length; i++)
            for(int j = 0; j < ch[i].length; j++)
                for(int p = 0; p < ch[i][j].length; p++)
                    Log.i("processing image", "t[" + i + "][" + j + "][" + p + "] = " + ch[i][j][p]);
        Toast.makeText(this, "Finished processing image", Toast.LENGTH_SHORT).show();
        return ch;
    }

    private void resizeImage(File f) {
        Bitmap currentImage;
        if (f != null) {
            while (true) {
                currentImage = BitmapFactory.decodeFile(f.getAbsolutePath());
                if (currentImage == null) {
                    try {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                            }
                        }, 1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                else break;
            }
            Bitmap resized = Bitmap.createScaledBitmap(currentImage, 200, 285, true);
            try {
                FileOutputStream fos = new FileOutputStream(f);
                resized.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(this, "image resized", Toast.LENGTH_SHORT).show();
    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        // handle the camera request returns and handle back button in camera.
//
//        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_CANCELED) {
//            Toast toast = Toast.makeText(this, "Canceled, no picture taken.", 1000);
//            toast.show();
//            return;
//        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnTakePic = (Button) findViewById(R.id.btnTakePic);
        btnTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
                resizeImage(image);
                try {
                    processImage(image);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
