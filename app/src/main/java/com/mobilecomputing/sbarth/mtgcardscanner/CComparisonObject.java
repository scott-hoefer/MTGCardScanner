package com.mobilecomputing.sbarth.mtgcardscanner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by sam barth on 12/4/2017.
 */

public class CComparisonObject {
    private int c;
    private int[][][] hist;
    private int[][][][] j1hist;
    private ArrayList<Tuple_RGB_bin> bins = new ArrayList();

    CComparisonObject(int c, HistogramTuple hist) {
        this.c = c;
        this.hist = hist.getHistogram();
        setBins();
    }

    CComparisonObject(int c, int[][][] hist) {
        this.c = c;
        this.hist = hist;
        setBins();
    }

    CComparisonObject(int c, int[][][][] j1hist) {
        this.c = c;
        this.j1hist = j1hist;
        setBinsJHist();
    }

    public ArrayList<Tuple_RGB_bin> getBins() {
        return this.bins;
    }

    private void setBinsJHist() {
        ArrayList<Tuple_RGB_bin> sorted = new ArrayList();
        for (int i = 0; i < j1hist.length ; i++) {
            for (int j = 0; j < j1hist[0].length ; j++) {
                for (int k = 0; k < j1hist[0][0].length ; k++) {
                    for (int l = 0 ; l < j1hist[0][0][0].length ; l++) {
                        sorted.add(new Tuple_RGB_bin(i, j, k, l));
                    }
                }
            }
        }
        sorted.sort( Collections.reverseOrder(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Tuple_RGB_bin) o1).compareTo((Tuple_RGB_bin) o2);
            }}));
        for (int i = 0 ; i < c ; i++) {
            bins.add(sorted.get(i));
        }
    }

    private void setBins() {
        ArrayList<Tuple_RGB_bin> sorted = new ArrayList();
        for (int i = 0; i < hist.length ; i++) {
            for (int j = 0; j < hist[0].length ; j++) {
                for (int k = 0; k < hist[0][0].length ; k++) {
                    sorted.add(new Tuple_RGB_bin(i, j, k));
                }
            }
        }
        sorted.sort( Collections.reverseOrder(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Tuple_RGB_bin) o1).compareTo((Tuple_RGB_bin) o2);
            }}));
        for (int i = 0 ; i < c ; i++) {
            bins.add(sorted.get(i));
        }
    }


}
