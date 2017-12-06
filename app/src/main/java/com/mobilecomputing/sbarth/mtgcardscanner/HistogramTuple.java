package com.mobilecomputing.sbarth.mtgcardscanner;

/**
 * HistogramTuple.java
 *
 * Sam Barth, Scott Hoefer, Cole Petersen
 *
 * Created by sam barth on 11/30/2017.
 */

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class HistogramTuple {

    private String name;
    private int[][][] hist;
    private int delta;

    HistogramTuple(String name, int[][][] hist) {
        this.name = name;
        this.hist = hist;
    }

    public String getName() {
        return name;
    }

    public int[][][] getHistogram() {
        return hist;
    }

    /*
    public int getDifference(HistogramTuple other) {
        return ImagePreprocessor.compareImages(hist, other.getHistogram());
    }
    */

    /*
    public int getDifference(int[][][] otherhist) {
        return ImagePreprocessor.compareImages(hist, otherhist);
    }
    */

    /**
     * toString:
     * Converts delta or histogram to a String.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name + ":\n");
        if ((Integer) delta != null) { sb.append(" , delta: " + Integer.toString(delta)); }
        else {
            for (int i = 0; i < hist.length; i++) {
                for (int j = 0; j < hist[0].length; j++) {
                    for (int k = 0; k < hist[0][0].length; k++)
                        sb.append("    [" + i + "][" + j + "][" + k + "]" + " = " + hist[i][j][k] + "\n");
                }
            }
        }
        return sb.toString();
    }

    public void setDelta(int d) {this.delta = d;}

    public int getDelta() {return delta;}

    /**
     * rank:
     * Takes a captured histogram and returns a sorted ArrayList of database histograms, from most
     * to least similar.
     */
    public static ArrayList<HistogramTuple> rank(HistogramTuple search, Collection<HistogramTuple> database) {
        ArrayList<HistogramTuple> result = new ArrayList();
        for (HistogramTuple reference : database) {
            reference.setDelta(ImagePreprocessor.compareImages(search.hist, reference.hist, 7));
//            Log.i("referencedelta", Integer.toString(reference.getDelta()));
            result.add(reference);
        }
        result.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Integer) ((HistogramTuple) o1).delta).compareTo((Integer) ((HistogramTuple) o2).delta);
            }

        });
        return result;
    }

    public static ArrayList<HistogramTuple> rank(int[][][] search, Collection<HistogramTuple> database) {
        return HistogramTuple.rank(new HistogramTuple("", search), database);
    }
}
