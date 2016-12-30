package com.example.jiyang.jyview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.jiyang.jyview.DiyView.HistogramView;
import com.example.jiyang.jyview.bean.HistogramData;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private HistogramView histogramView;    //直方图View
    private ArrayList<HistogramData> histogramDatas;   //数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        histogramView = (HistogramView) findViewById(R.id.h);
        histogramView.setVisibility(View.INVISIBLE);
        histogramDatas = new ArrayList<>();
        histogramDatas.add(new HistogramData(30, "30"));
        histogramDatas.add(new HistogramData(50, "50"));
        histogramDatas.add(new HistogramData(70, "70"));
        histogramDatas.add(new HistogramData(90, "90"));

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                histogramView.setVisibility(View.VISIBLE);
                histogramView.setData(histogramDatas);
                v.setVisibility(View.INVISIBLE);
            }
        });

    }
}
