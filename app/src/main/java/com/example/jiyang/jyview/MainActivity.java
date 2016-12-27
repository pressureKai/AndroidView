package com.example.jiyang.jyview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<HistogramData> histogramDatas = new ArrayList<>();
        histogramDatas.add(new HistogramData(10, "10"));
        histogramDatas.add(new HistogramData(30, "30"));
        histogramDatas.add(new HistogramData(50, "50"));
        histogramDatas.add(new HistogramData(70, "70"));
        histogramDatas.add(new HistogramData(90, "90"));
        HistogramView histogramView = (HistogramView) findViewById(R.id.h);
        histogramView.setData(histogramDatas);
    }
}
