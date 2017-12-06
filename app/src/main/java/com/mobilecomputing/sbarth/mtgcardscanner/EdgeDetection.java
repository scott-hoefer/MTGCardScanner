package com.mobilecomputing.sbarth.mtgcardscanner;

/**
 * EdgeDetection.java
 *
 * Sam Barth, Scott Hoefer, Cole Petersen
 *
 * Created by shoefer on 11/15/2017.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class EdgeDetection extends Activity{

    static{ System.loadLibrary("opencv_java3"); }

    public void detectEdges(Bitmap bitmap) {
        Mat rgba = new Mat();
        Utils.bitmapToMat(bitmap, rgba);
//        List<MatOfPoint> contours = new ArrayList<>();
//        Mat hierarchy = new Mat();
        Mat mask = new Mat();

        Mat edges = new Mat(rgba.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(rgba, edges, Imgproc.COLOR_BGR2GRAY, 4);
        Imgproc.Canny(edges, edges, 40, 60);
        Imgproc.blur(edges, edges, new Size(3, 3));
//        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0,0));
//        Imgproc.drawContours(mask, contours, -1, new Scalar(0, 255, 0), 1);

        // Don't do that at home or work it's for visualization purpose.
        //BitmapHelper.showBitmap(this, bitmap, imageView);
        Bitmap resultBitmap = Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888);
//        Bitmap bmp = Bitmap.createBitmap(mask.cols(), mask.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(edges, resultBitmap);
//        Utils.matToBitmap(mask, bmp);
        //BitmapHelper.showBitmap(this, resultBitmap, detectEdgesImageView);
        Log.i("canny edge detection","edge detection complete");
    }
}
