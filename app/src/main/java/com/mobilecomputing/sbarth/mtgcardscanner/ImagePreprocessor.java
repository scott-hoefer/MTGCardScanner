package com.mobilecomputing.sbarth.mtgcardscanner;

/**
 * ImageProcessor.java
 *
 * Sam Barth, Scott Hoefer, Cole Petersen
 *
 * Created by sam barth on 11/30/2017.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImagePreprocessor {

    static int CARD_WIDTH = 265;
    static int CARD_HEIGHT = 370;

    /**
     * compareImages:
     * Compares captured image histogram with database image histogram and returns total difference.
     */

    // for all histogram bins
    public static int compareImages(int[][][] compImg, int[][][] baseImg) {
        int delta = 0;
        for (int i = 0; i < compImg.length; i++) {
            for (int j = 0; j < compImg[0].length; j++) {
                for (int k = 0; k < compImg[0][0].length; k++) {
                    delta += java.lang.Math.abs(baseImg[i][j][k] - compImg[i][j][k]);
                }
            }
        }
        return delta;
    }

    // for c most populated bins for captured image
    public static int compareImages(int[][][] searchHist, int[][][] referenceHist, int c) {
        CComparisonObject co = new CComparisonObject(c, searchHist);
        return compareImages(searchHist, referenceHist, co);
    }

    // for c most populated bins for captured image
    public static int compareImages(int[][][] searchHist, int[][][] referenceHist, CComparisonObject c) {
        int delta = 0;
        ArrayList<Tuple_RGB_bin> bins = c.getBins();
        for (Tuple_RGB_bin bin : bins) {
            int r = bin.getR();
            int b = bin.getB();
            int g = bin.getG();
            delta += java.lang.Math.abs(referenceHist[r][g][b] - searchHist[r][g][b]);
        }
        return delta;
    }

    public static int compareImages(int[][][][] searchHist, int[][][][] referenceHist, int c) {
        CComparisonObject co = new CComparisonObject(c, searchHist);
        return compareImages(searchHist, referenceHist, co);
    }

    public static int compareImages(int[][][][] searchHist, int[][][][] referenceHist, CComparisonObject c) {
        int delta = 0;
        ArrayList<Tuple_RGB_bin> bins = c.getBins();
        for (Tuple_RGB_bin bin : bins) {
            int r = bin.getR();
            int b = bin.getB();
            int g = bin.getG();
            int ed = bin.getEd();
            delta += java.lang.Math.abs(referenceHist[r][g][b][ed] - searchHist[r][g][b][ed]);
        }
        return delta;
    }

//    public static int compareImages(int[][][] searchHist, int[][][] referenceHist, int c) {
//        int delta = 0;
//        for ( bin : searchHist)
//    }

    /**
     * readHistogramCSV:
     * Returns ArrayList of each image histogram in database.
     */
    public static ArrayList<HistogramTuple> readHistogramCSV(Scanner sc) {
        ArrayList<HistogramTuple> result = new ArrayList();
        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] split = line.split(",", 65);
            int[][][] hist = new int[4][4][4];
            int tracker = 1;
            for (int i = 0; i < hist.length; i++) {
                for (int j = 0; j < hist[0].length; j++) {
                    for (int k = 0; k < hist[0][0].length; k++) {
                        hist[i][j][k] = Integer.parseInt(split[tracker]);
                        tracker++;
                    }
                }
            }
            result.add(new HistogramTuple(split[0], hist));
        }
        return result;
    }

    public static ArrayList<HistogramTuple> readJ1HistogramCSV(Scanner sc) {
        ArrayList<HistogramTuple> result = new ArrayList();
        while (sc.hasNext()) {
            String line = sc.nextLine();
            String[] split = line.split(",", 321);
            int[][][][] hist = new int[4][4][4][5];
            int tracker = 1;
            for (int i = 0; i < hist.length; i++) {
                for (int j = 0; j < hist[0].length; j++) {
                    for (int k = 0; k < hist[0][0].length; k++) {
                        for (int l = 0; l < hist[0][0][0].length ; l++) {
                            hist[i][j][k][l] = Integer.parseInt(split[tracker]);
                            tracker++;
                        }
                    }
                }
            }
            result.add(new HistogramTuple(split[0], hist));
        }
        return result;
    }

    public static int[][][] processImage(File f) throws Exception {
        int[][][] ch = new int[4][4][4];
        Bitmap image = BitmapFactory.decodeFile(f.getPath());
        for (int x = 0; x < image.getWidth(); x++)
            for (int y = 0; y < image.getHeight(); y++) {
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
//        for(int i = 0; i < ch.length; i++)
//            for(int j = 0; j < ch[i].length; j++)
//                for(int p = 0; p < ch[i][j].length; p++)
//                    Log.i("processing image", "t[" + i + "][" + j + "][" + p + "] = " + ch[i][j][p]);
        return ch;
    }

    /**
     * getHistogramRanking:
     * Takes a captured image and returns a sorted ArrayList of database images, from most to least
     * similar.
     */
    public static ArrayList<HistogramTuple> getHistogramRanking(Bitmap bmp, Scanner sc) {
        ArrayList<HistogramTuple> unsorted;
        unsorted = readHistogramCSV(sc);
        ArrayList<HistogramTuple> sorted = null;
        try {
            sorted = HistogramTuple.rank(processImage(bmp), unsorted);
        } catch (Exception ex) {
            System.err.println("There was a problem opening the image file");
            System.err.println(ex.toString());
        }
        return sorted;
    }

    public static ArrayList<HistogramTuple> getJ1HistogramRanking(Bitmap bmp, Scanner sc) {
        ArrayList<HistogramTuple> unsorted = new ArrayList();
        unsorted = readJ1HistogramCSV(sc);
        ArrayList<HistogramTuple> sorted = null;
        try {
            sorted = HistogramTuple.rank(processImageToJH1(bmp), unsorted);
        } catch (Exception ex) {
            System.err.println("There was a problem opening the image file");
            System.err.println(ex.toString());
        }
        return sorted;
    }

    public static void resizeImage(File f) {
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

                } else break;
            }
            Bitmap resized = Bitmap.createScaledBitmap(currentImage, 200, 285, true);
            EdgeDetection ed = new EdgeDetection();
            ed.detectEdges(currentImage);
            try {
                FileOutputStream fos = new FileOutputStream(f);
                resized.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * resizeImage:
     * Takes a Bitmap and returns the same image scaled differently.
     */
    public static Bitmap resizeImage(Bitmap currentImage, int w, int h) {
        Bitmap resized = Bitmap.createScaledBitmap(currentImage, w, h, true);
        return resized;
    }

    /**
     * processImage:
     * Takes a Bitmap and returns its color histogram.
     */
    public static int[][][] processImage(Bitmap image) throws Exception {
        int picw = image.getWidth();
        int pich = image.getHeight();
        int[][][] ch = new int[4][4][4]; // R, G, and B with 4 bins each
        for(int x = 0; x < picw ; x++)
            for(int y = 0; y < pich ; y++) {
                int pixel = image.getPixel(x, y);
                int red = Color.red(pixel);
                int blue = Color.blue(pixel);
                int green = Color.green(pixel);
                int alpha = Color.alpha(pixel);
                ch[red / 64][green / 64][blue / 64]++; // truncate from 256 to 4 bins
            }
//        for(int i = 0; i < ch.length; i++)
//            for(int j = 0; j < ch[i].length; j++)
//                for(int p = 0; p < ch[i][j].length; p++)
//                    Log.i("histogram", "t[" + i + "][" + j + "][" + p + "] = " + ch[i][j][p]);
        return ch;
    }

    /**
     * processImageToJH1:
     * Takes a Bitmap and returns its joint histogram, using color and edge density (JH1).
     * Edge density is ratio of edge pixels to total pixels in a given window.
     */
    public static int[][][][] processImageToJH1(Bitmap image) throws Exception {
        int[][][][] ch = new int[4][4][4][5];
        Mat src = new Mat();
        Mat edgesMat = new Mat();
        Utils.bitmapToMat(image, src);
        Imgproc.Canny(src, edgesMat, 40, 120);  //get the edges of the image using Canny Edge Detection Algorithm
        Bitmap edges = image;
        Utils.matToBitmap(edgesMat, edges);
        for(int x = 0; x < image.getWidth(); x++)
            for(int y = 0; y < image.getHeight(); y++) {
                int p = image.getPixel(x, y);
                int red = Color.red(p);
                int green = Color.green(p);
                int blue = Color.blue(p);

                int numEdges = 0;
                int totalPixels = 0;
                int edgeDensity = 0;

                // find the number of edge pixels and total pixels in each window
                for (int i = x - 2 ; i < x + 3 ; i++) {
                    for (int j = y - 2 ; j < y + 3 ; j++) {
                        if (i < 0 || j < 0) { continue; }
                        else if (i >= edges.getWidth() || j >= edges.getHeight()) { continue; }
                        else {
                            int pixel = edges.getPixel(i, j);
                            if (!isBlack(pixel)) {
                                numEdges++;
                            }
                            totalPixels++;
                        }
                    }
                }

                // find edge density, then truncate into 5 bins
                if (totalPixels != 0) {
                    edgeDensity = (int) java.lang.Math.ceil((numEdges / totalPixels ) * 4);
                }
                // truncate colors from 256 to 4 bins
                ch[red / 64][green / 64][blue / 64][edgeDensity]++;
            }
        return ch;
    }

    /**
     * isBlack:
     * Returns whether or not a pixel is black (R = 0, G = 0, B = 0)
     */
    private static boolean isBlack(int pix) {
        return Color.red(pix) == 0 && Color.green(pix) == 0 && Color.blue(pix) == 0;
    }

//    public static ArrayList<ArrayList<String>> getPhashRanking(String filename, String csv) {
//        RadialHash search;
//        String result = "";
//        double sim = 0;
//        ArrayList<ArrayList<String>> sorted = new ArrayList();
//        ArrayList<ArrayList<String>> db = PhashBuilder.readCSV(csv);
//        try {
//            search = jpHash.getImageRadialHash(filename);
//            for (ArrayList<String> hash : db) {
//                double currsim = jpHash.getSimilarity(search, RadialHash.fromString(hash.get(1)));
//                if (currsim > sim ) {
//                    sim = currsim;
//                    result = hash.get(0);
//                }
//                ArrayList<String> image = new ArrayList();
//                image.add(hash.get(0));
//                image.add(Double.toString(currsim));
//                sorted.add(image);
//            }
//            Collections.sort(sorted, new Comparator<List<String>> () {
//                @Override
//                public int compare(List<String> a, List<String> b) {
//                    return ((Double) Double.parseDouble(a.get(1))).compareTo((Double) Double.parseDouble(b.get(1)));
//                }
//            });
//        } catch (IOException ex) {
//            System.err.println("couldn't pHash your search image");
//        }
//        return sorted;
//    }

    /*
    public static void writeHistogramToCSV(String cn, int[][][] hist) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new File("histograms/" + cn + ".csv"));
        StringBuilder sb = new StringBuilder();

        for (int i = 0 ; i < hist.length ; i++)
            for (int j = 0 ; j < hist[0].length ; j++)
                for (int k = 0 ; k < hist[0][0].length ; k++) {
                    sb.append(Integer.toString(i));
                    sb.append(',');
                    sb.append(Integer.toString(j));
                    sb.append(',');
                    sb.append(Integer.toString(k));
                    sb.append(',');
                    sb.append(Integer.toString(hist[i][j][k]));
                    sb.append('\n');
                    pw.write(sb.toString());
                    sb.setLength(0);
                }
        pw.close();
    }
    */

}

