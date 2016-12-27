package com.example.jiyang.jyview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by jiyang on 16-12-27.
 * 直方图
 */

public class HistogramView extends View {
    private ArrayList<HistogramData> datas;
    private int[] mColors = {Color.BLUE, Color.DKGRAY, Color.CYAN, Color.RED, Color.GREEN};

    private Paint paint;
    private Path path;
    private Rect rect;
    private int mWidth = 50;    //直方图宽
    private int w;              //画布宽
    private int h;              //画布高

    public HistogramView(Context context) {
        super(context);
        init();
    }

    public HistogramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HistogramView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        paint.setTextSize(20);
    }

    public void setData(ArrayList<HistogramData> data) {
        this.datas = data;
        if (null == data || data.size() == 0) {
            return;
        }
        float sum = 0;
        for (int i = 0; i < data.size(); i++) {
            sum += data.get(i).value;
        }
        for (int i = 0; i < data.size(); i++) {
            HistogramData histogramData = data.get(i);
            histogramData.persentage = histogramData.value / sum;
            Log.i("TAG", histogramData.name + ":" + String.valueOf(histogramData.persentage));
        }

        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画坐标系
        canvas.drawLine(10, h - 10, w - 10, h - 10, paint);
        canvas.drawLine(10, h - 10, 10, 10, paint);

        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        canvas.drawPoint(10, h - 10, paint);
        paint.setStrokeWidth(mWidth);
        for (int i = 0; i < datas.size(); i++) {
            HistogramData histogramData = datas.get(i);
            paint.setColor(mColors[i % mColors.length]);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawLine(
                    (i + 1) * mWidth, h - 10,
                    (i + 1) * mWidth, h - 10 - histogramData.persentage * h,
                    paint);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawText(histogramData.name, (i + 1) * mWidth, h - 10, paint);
        }
    }
}
