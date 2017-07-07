package com.newborntown.pathanimationrelativelayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Graceful Sun on 17/7/7.
 * E-mail itzhishuaisun@sina.com
 */

public class PathAnimationRelativeLayout extends ViewGroup implements
        ValueAnimator.AnimatorUpdateListener {
    private static final String TAG = "PathAnimationRelativeLa";
    private Path mPath = new Path();
    private long time = 2000;
    private Interpolator interpolator = new LinearInterpolator();
    float[] point = new float[2];
    float[] tans = new float[2];
    private PathMeasure pathMeasure;
    private ValueAnimator valueAnimator;
    private boolean isRotationLightPath;
    private List<View> pathViews = new ArrayList<>();
    private boolean isFirstLauout = true;
    public PathAnimationRelativeLayout(Context context) {
        this(context, null);
    }

    public PathAnimationRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathAnimationRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int warpContentWidth = 0;
        int warpContentHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) childView.getLayoutParams();
            if (layoutParams.isUsePath()) {
                pathViews.add(childView);
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            } else {
                warpContentWidth = Math.max(warpContentWidth, childView.getMeasuredWidth());
                warpContentHeight = Math.max(warpContentHeight, childView.getMeasuredHeight());
            }
        }
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = warpContentWidth;
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = warpContentHeight;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mPath, paint);
        super.dispatchDraw(canvas);
    }

    float degrees = 0;
    float preDegrees = 0;
    int count = 0;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(isFirstLauout)
            return;
        for (int i = 0; i < pathViews.size(); i++) {
            View childPathView = pathViews.get(i);
            int measuredWidth = childPathView.getMeasuredWidth();
            int measuredHeight = childPathView.getMeasuredHeight();
            int left = (int) (point[0] - measuredWidth / 2);
            int right = (int) (point[1] - measuredHeight / 2);
            if (isRotationLightPath) {
                if (tans[0] != 0) {
                    float tan = tans[1] / tans[0];
                    degrees = (float) Math.toDegrees(Math.atan(tan));
                    if (Math.abs(preDegrees - degrees) > 90) {
                        count++;
                    }
                    preDegrees = degrees;
                }
                childPathView.setRotation((degrees + count * 180) % 360);
            }
            childPathView.layout(left, right, left + measuredWidth, right + measuredHeight);
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float animatedValue = (float) animation.getAnimatedValue();
        pathMeasure.getPosTan(animatedValue, point, tans);
        requestLayout();
    }

    public Path getPath() {
        return mPath;
    }

    public void setPath(Path mPath) {
        this.mPath = mPath;

    }

    public void start() {
        isFirstLauout = false;
        pathMeasure = new PathMeasure(mPath, false);
        float length = pathMeasure.getLength();
        valueAnimator = ValueAnimator.ofFloat(0, length);
        valueAnimator.addUpdateListener(this);
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.setDuration(time);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.start();
        invalidate();
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Interpolator getInterpolator() {
        return interpolator;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    public boolean isRotationLightPath() {
        return isRotationLightPath;
    }

    public void setRotationLightPath(boolean rotationLightPath) {
        isRotationLightPath = rotationLightPath;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return super.checkLayoutParams(p);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return super.generateLayoutParams(p);
    }

    private class LayoutParams extends RelativeLayout.LayoutParams {

        private boolean usePath;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.animation_path);
            usePath = a.getBoolean(R.styleable.animation_path_use_path, false);
            a.recycle();
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public boolean isUsePath() {
            return usePath;
        }

        public void setUsePath(boolean usePath) {
            this.usePath = usePath;
        }
    }


}
