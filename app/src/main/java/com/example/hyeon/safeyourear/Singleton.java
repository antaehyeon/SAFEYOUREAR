package com.example.hyeon.safeyourear;

/**
 * Created by HYEON on 2017-02-25.
 */

public class Singleton {

    // SINGLETON VARIABLES
    private static Singleton singletonInstance;

    // SINGLETON FUNCTION
    public static Singleton getInstance() {
        if(singletonInstance == null) {
            singletonInstance = new Singleton();
        }
        return singletonInstance;
    }

    // VARIABLES
    float [] freqLeftData = new float[7];
    float [] freqRightData = new float[7];




    // GET
    public float[] getFreqLeftData() {
        return freqLeftData;
    }

    public float[] getFreqRightData() {
        return freqRightData;
    }


    // SET
    public void setFreqLeftData(int a, int b, int c, int d, int e, int f, int g){
        freqLeftData[0] = a;
        freqLeftData[1] = b;
        freqLeftData[2] = c;
        freqLeftData[3] = d;
        freqLeftData[4] = e;
        freqLeftData[5] = f;
        freqLeftData[6] = g;
    }

    public void setFreqRightData(int a, int b, int c, int d, int e, int f, int g) {
        freqRightData[0] = a;
        freqRightData[1] = b;
        freqRightData[2] = c;
        freqRightData[3] = d;
        freqRightData[4] = e;
        freqRightData[5] = f;
        freqRightData[6] = g;
    }





}
