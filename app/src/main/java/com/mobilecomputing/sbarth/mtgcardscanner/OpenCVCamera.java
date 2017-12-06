package com.mobilecomputing.sbarth.mtgcardscanner;

/**
 * OpenCVCamera.java:
 *
 * Sam Barth, Scott Hoefer, Cole Petersen
 */

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OpenCVCamera extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static String TAG = "OpenCVCamera";
    JavaCameraView javaCameraView;
    Mat mRgba;
    Rect rect;
    BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS: {
                    javaCameraView.enableView();
                    break;
                }
                default: {
                    super.onManagerConnected(status);
                    break;
                }

            }
            super.onManagerConnected(status);
        }
    };

    static {
        if (OpenCVLoader.initDebug()) {
            Log.i(TAG, "OpenCV loaded successfully");
        }
        else {
            Log.i(TAG, "OpenCV not loaded");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_cvcamera);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        javaCameraView = (JavaCameraView) findViewById(R.id.java_camera_view);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        javaCameraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screenClick();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (javaCameraView != null) {
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (javaCameraView != null) {
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            Log.i(TAG, "OpenCV loaded successfully");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else {
            Log.i(TAG, "OpenCV not loaded");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_2, this, mLoaderCallback);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat result = new Mat();
        Mat mask = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        mRgba = inputFrame.rgba();
        Imgproc.Canny(mRgba, result, 40, 120);
        Imgproc.GaussianBlur(result, result, new Size(9,9), 2, 2);
        Imgproc.findContours(result, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0,0));
        Imgproc.drawContours(mask, contours, -1, new Scalar(0, 255, 0), 1);
        hierarchy.release();

        Imgproc.rectangle(mRgba, new Point(300,100), new Point((300 + 3*370), (100 + 3*265)), new Scalar(0, 255, 0, 255), 6);
        Imgproc.rectangle(mRgba, new Point(423,160), new Point((423 + 3*164), (160 + 3*224)), new Scalar(255, 0, 0, 255), 6);

        for ( int contourIdx=0; contourIdx < contours.size(); contourIdx++ )
        {
            // Minimum size allowed for consideration
            MatOfPoint2f approxCurve = new MatOfPoint2f();
            MatOfPoint2f contour2f = new MatOfPoint2f( contours.get(contourIdx).toArray() );
            //Processing on mMOP2f1 which is in type MatOfPoint2f
            double approxDistance = Imgproc.arcLength(contour2f, true)*0.02;
            Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);

            //Convert back to MatOfPoint
            MatOfPoint points = new MatOfPoint( approxCurve.toArray() );

            // Get bounding rect of contour
            rect = Imgproc.boundingRect(points);

            //Imgproc.rectangle(mRgba, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(255, 0, 0, 255), 3);
            Log.i("rect pts", "pt 1 x:" + rect.x + " pt 1 y: " + rect.y );
            Log.i("rect pts width", "rect width: " + rect.width + " rect height: " + rect.height );
        }
//        Bitmap card = Bitmap.createBitmap(result.cols(), result.rows(), Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(result, card);
        return mRgba;
    }

    public void screenClick(){
        //Mat cardMat = new Mat(mRgba, rect);
        //Bitmap cardBmp = Bitmap.createBitmap(cardMat.cols(), cardMat.rows(), Bitmap.Config.ARGB_8888);
        //Utils.matToBitmap(cardMat, cardBmp);
        Rect r = new Rect(423, 160, 492, 672);
        Mat cm = new Mat(mRgba, r);

        Bitmap bmp = Bitmap.createBitmap(cm.cols(), cm.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(cm, bmp);
        bmp = ImagePreprocessor.resizeImage(bmp, 164, 224);

        String filename = "cardImage.jpg";
        try {
            FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            //clean up
            stream.close();
            bmp.recycle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(OpenCVCamera.this, SearchResults.class);
        intent.putExtra("image", filename);
        startActivity(intent);
    }
}
