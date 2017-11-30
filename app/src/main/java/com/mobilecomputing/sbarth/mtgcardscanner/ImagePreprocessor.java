package com.mobilecomputing.sbarth.mtgcardscanner;

/**
 * Created by sam barth on 11/30/2017.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author barts_000
 */
public class ImagePreprocessor {

    static int CARD_WIDTH = 200;
    static int CARD_HEIGHT = 285;

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

    public static ArrayList<HistogramTuple> getHistogramRanking(String filename, String csv) {
        ArrayList<HistogramTuple> sorted = new ArrayList();

        return sorted;
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

}

