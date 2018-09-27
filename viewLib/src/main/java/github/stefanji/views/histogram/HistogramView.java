package github.stefanji.views.histogram;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;


import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by jiyang on 16-12-27.
 * 递增直方图
 */

public class HistogramView extends View {
    private int[] mColors = {Color.BLUE, Color.DKGRAY, Color.CYAN, Color.RED, Color.GREEN};
    private ArrayList<HistogramData> datas;
    private Paint paint;
    private int mWidth = 70;    //直方图宽
    private int width2 = 20;    //直方图间距
    private int w;              //画布宽
    private int h;              //画布高
    private MyHandler myHandler;

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

    /**
     * 初始化设置画笔
     */
    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        paint.setTextSize(24);
        w = h = 0;
    }

    /**
     * 根据数据计算每个直方图的百分比
     *
     * @param data 数据
     */
    public void setData(ArrayList<HistogramData> data) {
        this.datas = data;
        if (null == datas || datas.size() == 0) {
            return;
        }
        myHandler = new MyHandler(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //计算数据总和
                float sum = 0;
                for (int i = 0; i < datas.size(); i++) {
                    sum += datas.get(i).value;
                }
                //计算数据占总数的百分比
                for (int i = 0; i < datas.size(); i++) {
                    HistogramData histogramData = datas.get(i);
                    histogramData.persentage = histogramData.value / sum;
                }
                //计算单列数据高度，并缓慢增加
                float startX, endY;
                for (int i = 0; i < datas.size(); i++) {
                    HistogramData histogramData = datas.get(i);
                    startX = (i + 1) * mWidth + i * width2;
                    endY = HistogramView.this.h - histogramData.persentage * HistogramView.this.h;
                    histogramData.color = mColors[i % mColors.length];
                    //注意：画布的坐标是左上角为原点，所以是
                    // 从 画布高-10 为起点, 数据高度 为终点
                    // 递减
                    for (int j = HistogramView.this.h; j >= endY; j--) {
                        histogramData.x = startX;
                        histogramData.y = j;
                        //通知UI更新
                        myHandler.sendEmptyMessage(0);
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w - 10;
        this.h = h - 10;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        canvas.drawLine(10, h, w, h, paint);      //画坐标系X轴
        canvas.drawLine(10, h, 10, 10, paint);    //画坐标系Y轴

        paint.setStrokeWidth(mWidth);
        paint.setStyle(Paint.Style.STROKE);
        if (null == datas) {
            return;
        }
        for (int i = 0; i < datas.size(); i++) {
            HistogramData histogramData = datas.get(i);
            paint.setColor(histogramData.color);
            canvas.drawLine(histogramData.x, h, histogramData.x, histogramData.y,
                    paint);

            //直方图顶部文字
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawText(histogramData.name, histogramData.x - mWidth / 4, histogramData.y - 10, paint);
        }
    }

    static class MyHandler extends Handler {
        WeakReference<HistogramView> weakReference;
        HistogramView histogramView;

        MyHandler(HistogramView histogramView) {
            weakReference = new WeakReference<>(histogramView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            histogramView = weakReference.get();
            if (histogramView == null) {
                return;
            }
            histogramView.invalidate();
        }
    }
}
