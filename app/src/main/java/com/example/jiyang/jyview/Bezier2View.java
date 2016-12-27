package com.example.jiyang.jyview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by jiyang on 16-12-27.
 * 贝塞尔二阶曲线
 */

public class Bezier2View extends View {
    public Bezier2View(Context context) {
        super(context);
        init();
    }

    public Bezier2View(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Bezier2View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Path path;
    private Paint paint;
    private Point start, end, control;
    private int centerX, centerY;
    private MyHandler myHandler;

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        start = new Point(0, 0);
        end = new Point(0, 0);
        control = new Point(0, 0);
        path = new Path();
        myHandler = new MyHandler(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        start.x = centerX - 200;
        start.y = centerY;
        end.x = centerX + 200;
        end.y = centerY;
        control.x = centerX;
        control.y = centerY - 100;
        new Thread(new Runnable() {
            @Override
            public void run() {
                int count = centerY - 100;
                while (true) {
                    if (count < centerY * 2)
                        count += 10;
                    else
                        count = 0;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = Message.obtain();
                    message.obj = count;
                    myHandler.sendMessage(message);
                }
            }
        }).start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画点
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);
        canvas.drawColor(Color.BLACK);
        canvas.drawPoint(centerX, centerY, paint);
        canvas.drawPoint(start.x, start.y, paint);
        canvas.drawPoint(end.x, end.y, paint);
        canvas.drawPoint(control.x, control.y, paint);
        //画路径
        path.reset();
        path.moveTo(start.x, start.y);
        path.quadTo(control.x, control.y, end.x, end.y);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(10);
        canvas.drawPath(path, paint);
        //画辅助线
        paint.setStrokeWidth(5);
        paint.setColor(Color.GRAY);
        canvas.drawLine(control.x, control.y, start.x, start.y, paint);
        canvas.drawLine(control.x, control.y, end.x, end.y, paint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        control.x = (int) event.getX();
        control.y = (int) event.getY();
        invalidate();
        return true;
    }

    static class MyHandler extends Handler {
        WeakReference<Bezier2View> weakReference;
        Bezier2View bezier2View;

        MyHandler(Bezier2View bezier2View) {
            this.weakReference = new WeakReference<>(bezier2View);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (bezier2View == null) bezier2View = weakReference.get();
            bezier2View.control.y = (int) msg.obj;
            bezier2View.invalidate();
        }
    }
}
