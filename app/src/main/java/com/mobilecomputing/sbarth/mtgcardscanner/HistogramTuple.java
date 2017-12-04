package com.mobilecomputing.sbarth.mtgcardscanner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 * Created by sam barth on 11/30/2017.
 */

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

    public int getDifference(HistogramTuple other) {
        return ImagePreprocessor.compareImages(hist, other.getHistogram());
    }

    public int getDifference(int[][][] otherhist) {
        return ImagePreprocessor.compareImages(hist, otherhist);
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

    public static ArrayList<HistogramTuple> rank(HistogramTuple search, Collection<HistogramTuple> database) {
        ArrayList<HistogramTuple> result = new ArrayList();
        for (HistogramTuple item : database) {
            item.delta = ImagePreprocessor.compareImages(item.hist, search.hist);
            result.add(item);
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
