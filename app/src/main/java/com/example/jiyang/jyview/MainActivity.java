package com.example.jiyang.jyview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import github.stefanji.jumpwater.JumpWater;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final JumpWater jumpWater = findViewById(R.id.jump_water);
        findViewById(R.id.btn_jump).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpWater.jump();
            }
        });
    }
}
