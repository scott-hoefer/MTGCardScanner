package com.mobilecomputing.sbarth.mtgcardscanner;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by shoefer on 11/15/2017.
 */

public class EdgeDetection extends Activity{

    static{ System.loadLibrary("opencv_java3"); }

    public void detectEdges(Bitmap bitmap) {
        Mat rgba = new Mat();
        Utils.bitmapToMat(bitmap, rgba);

        Mat edges = new Mat(rgba.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(rgba, edges, Imgproc.COLOR_BGR2GRAY, 4);
        Imgproc.Canny(edges, edges, 40, 60);
        Imgproc.blur(edges, edges, new Size(3, 3));

        // Don't do that at home or work it's for visualization purpose.
        //BitmapHelper.showBitmap(this, bitmap, imageView);
        Bitmap resultBitmap = Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(edges, resultBitmap);
        //BitmapHelper.showBitmap(this, resultBitmap, detectEdgesImageView);
        Log.i("canny edge detection","edge detection complete");
    }
}
