package github.stefanji.views.histogram;

/**
 * Created by jiyang on 16-12-27.
 * 直方图数据
 */

public class HistogramData {
    public int value;       //数值
    public String name;     //文字描述

    public float persentage;    //占数据总数的百分比
    public float y;         //在直方图上的x轴坐标
    public float x;         //在直方图上的y轴坐标
    public int color;       //填充颜色

    public HistogramData(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public HistogramData() {
    }
}
