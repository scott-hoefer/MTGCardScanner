package com.mobilecomputing.sbarth.mtgcardscanner;

/**
 * Tuple_RGB_bin.java
 *
 * Sam Barth, Scott Hoefer, Cole Petersen
 *
 * Created by sam barth on 12/4/2017.
 */

public class Tuple_RGB_bin implements Comparable {
    private int r_value, g_value, b_value, ed;
    Tuple_RGB_bin(int r_value, int g_value, int b_value) {
        this.r_value = r_value;
        this.g_value = g_value;
        this.b_value = b_value;
    }

    Tuple_RGB_bin(int r_value, int g_value, int b_value, int ed) {
        this.r_value = r_value;
        this.g_value = g_value;
        this.b_value = b_value;
        this.ed = ed;
    }

    int getR() {
        return r_value;
    }
    int getG() {
        return g_value;
    }
    int getB() {
        return b_value;
    }
    int getEd() { return ed; }

    /**
     * compareTo:
     * Returns whether the sum of an objects R, G, and B values is less than (-1), greater than
     * (+1), or equal to (0) that of another.
     */
    @Override
    public int compareTo(Object o) {
        int sumThis = r_value + g_value + b_value;
        int sumOther = ((Tuple_RGB_bin) o).getR() + ((Tuple_RGB_bin) o).getG() + ((Tuple_RGB_bin) o).getB();
        if (sumThis < sumOther) {return -1;}
        else if (sumThis == sumOther) {return 0;}
        else {return 1;}
    }


}
