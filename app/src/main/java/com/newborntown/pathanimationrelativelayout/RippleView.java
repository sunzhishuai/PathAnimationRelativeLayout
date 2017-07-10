package com.newborntown.pathanimationrelativelayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Graceful Sun on 17/7/8.
 * E-mail itzhishuaisun@sina.com
 */

public class RippleView extends View implements ValueAnimator.AnimatorUpdateListener {

    private Bitmap bitmap;
    private Bitmap scaledBitmap;
    private Paint paint;
    private int meshWidth = 10, meshHeight = 10;
    private float[] sourcePoints;
    private float[] tempPoints;
//    private int rippleRadius = 300; //波纹半径
    private int rippleWidth = 35; //波纹宽度
    private float downX;
    private float downy;

    public RippleView(Context context) {
        this(context, null);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        bitmap = ((BitmapDrawable) context.getResources().getDrawable(R.mipmap.belle3)).getBitmap();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        scaledBitmap = Bitmap.createScaledBitmap(bitmap, widthSize, heightSize, false);
        initPoint(scaledBitmap);
    }

    private void initPoint(Bitmap scaledBitmap) {
        int xPoints = scaledBitmap.getWidth() / meshWidth + 1;
        int yPoints = scaledBitmap.getHeight() / meshHeight + 1;
        sourcePoints = new float[xPoints * yPoints * 2];
        tempPoints = new float[xPoints * yPoints * 2];
        ArrayList<Point> contextPoints = new ArrayList<>();
        for (int i = 0; i < yPoints; i++) {
            for (int j = 0; j < xPoints; j++) {
                int i1 = j * meshWidth;
                int i2 = i * meshHeight;
                contextPoints.add(new Point(i1, i2));
            }
        }
        for (int i = 0, j = 0; i < contextPoints.size(); i++, j = j + 2) {
            Point point = contextPoints.get(i);
            sourcePoints[j] = point.x;
            sourcePoints[j + 1] = point.y;
            tempPoints[j] = point.x;
            tempPoints[j + 1] = point.y;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmapMesh(scaledBitmap, scaledBitmap.getWidth() / meshWidth,
                scaledBitmap.getHeight() / meshHeight, tempPoints, 0, null, 0, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downX = event.getX();
            downy = event.getY();
            double v = Math.sqrt(Math.pow(getMeasuredWidth(), 2) + Math.pow(getMeasuredHeight(), 2)) ;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(rippleWidth, ((float) v));
            valueAnimator.setDuration(2000);
            valueAnimator.addUpdateListener(this);
            valueAnimator.start();
        }
        return super.onTouchEvent(event);
    }

    private void wasp(float centerX, float centery,float Radius) {
        for (int i = 0; i < tempPoints.length; i = i + 2) {
            float tempPointX = tempPoints[i];
            float tempPointY = tempPoints[i + 1];
            float length = getLength(centerX, centery, tempPointX, tempPointY);
            if (length > Radius - rippleWidth / 2 && length < Radius + rippleWidth / 2) {
                double cos = Math.cos((length - Radius) / rippleWidth * Math.PI / 2) * 10;
                float v = tempPointY - centery;
                if (v == 0) {
                    v = 1 / Integer.MAX_VALUE;
                }
                double antTan = Math.atan(Math.abs(tempPointX - centerX) / Math.abs(v));
                double dy = Math.sin(antTan) * cos;
                double dx = Math.cos(antTan) * cos;
                tempPoints[i] = (float) (sourcePoints[i] + dx);
                tempPoints[i + 1] = (float) (sourcePoints[i + 1] + dy);
            } else {
                tempPoints[i] = sourcePoints[i];
                tempPoints[i + 1] = sourcePoints[i + 1];
            }
        }
        invalidate();
    }

    private float getLength(float originX, float originY, float moveX, float moveY) {
        return (float) Math.sqrt(Math.pow(originX - moveX, 2) + Math.pow(originY - moveY, 2));
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float animatedValue = (float) animation.getAnimatedValue();
        wasp(downX, downy,animatedValue);
    }
}
