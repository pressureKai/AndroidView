package com.example.jiyang.jyview.bean;

/**
 * Created by jiyang on 16-12-27.
 * 直方图数据
 */

public class HistogramData {
    public int value;
    public String name;

    public float persentage;
    public float y;
    public float x;
    public int color;

    public HistogramData(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public HistogramData() {
    }
}
