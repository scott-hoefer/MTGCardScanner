package com.mobilecomputing.sbarth.mtgcardscanner;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 * Created by sam barth on 11/30/2017.
 */

public class HistogramTuple {

    private String name;
    private int[][][] hist = null;
    private int[][][][] j1hist = null;
    private int delta;

    HistogramTuple(String name, int[][][] hist) {
        this.name = name;
        this.hist = hist;
    }

    HistogramTuple(String name, int[][][][] j1hist) {
        this.name = name;
        this.j1hist = j1hist;
    }

    public String getName() {
        return name;
    }

    public int[][][] getHistogram() {
        return hist;
    }

    public int[][][][] getJ1Histogram(){
        return j1hist;
    }

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


    public static ArrayList<HistogramTuple> rank(HistogramTuple search, Collection<HistogramTuple> database) {
        ArrayList<HistogramTuple> result = new ArrayList();
        for (HistogramTuple reference : database) {
            reference.setDelta(ImagePreprocessor.compareImages(search.hist, reference.hist, 7));
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

    public static ArrayList<HistogramTuple> rankJ1(HistogramTuple search, Collection<HistogramTuple> database) {
        ArrayList<HistogramTuple> result = new ArrayList();
        for (HistogramTuple reference : database) {
            reference.setDelta(ImagePreprocessor.compareImages(search.hist, reference.hist, 7));
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

    public static ArrayList<HistogramTuple> rank(int[][][][] search, Collection<HistogramTuple> database) {
        return HistogramTuple.rankJ1(new HistogramTuple("", search), database);
    };
}
