package com.example.jiyang.jyview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<ViewData> viewDatas = new ArrayList<>();
        viewDatas.add(new ViewData(100, "C"));
        viewDatas.add(new ViewData(80, "Java"));
        viewDatas.add(new ViewData(60, "Python"));
        viewDatas.add(new ViewData(40, "C++"));
        viewDatas.add(new ViewData(20, "R"));

        MyView myView = (MyView) findViewById(R.id.myView);
        myView.setData(viewDatas);
    }
}
