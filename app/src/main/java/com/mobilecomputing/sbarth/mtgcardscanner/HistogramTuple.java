package com.mobilecomputing.sbarth.mtgcardscanner;

/**
 * Created by sam barth on 11/30/2017.
 */

public class HistogramTuple {

    private String name;
    private int[][][] hist;

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
        for (int i = 0; i < hist.length; i++) {
            for (int j = 0 ; j < hist[0].length; j++) {
                for(int k = 0 ; j < hist[0][0].length; k++)
                    sb.append("    [" + i + "][" + j + "][" + k + "]" + " = " + hist[i][j][k] + "\n");
            }
        }
        return sb.toString();
    }
}
