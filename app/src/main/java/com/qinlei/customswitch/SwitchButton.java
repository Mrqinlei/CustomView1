package com.qinlei.customswitch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by ql on 2017/4/10.
 */

public class SwitchButton extends View {
    public interface switchCallBack {
        void callback(boolean isLeft);
    }

    private switchCallBack switchCallBack;

    private boolean isLeft = true;
    private boolean isAnim = false;//是否正在动画中

    private String leftString = "小视频";
    private String rightString = "直播间";

    private int animationDuration = 150;

    private Paint textPaint;
    private int textSize = 12;
    //文本颜色
    private int textNormalColor = Color.WHITE;
    private int textPressColor = Color.LTGRAY;

    private int trackStartX = 0;
    private int trackStartY = 0;

    private Paint thumpPaint;
    private Paint trackPaint;

    private int width;
    private int height;

    public SwitchButton(Context context) {
        super(context);
    }

    public SwitchButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public boolean isLeft() {
        return isLeft;
    }

    public void setSwitchCallBack(SwitchButton.switchCallBack switchCallBack) {
        this.switchCallBack = switchCallBack;
    }

    private void init() {
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setColor(Color.WHITE);
        int defaultTextSize = spToPx(textSize);
        textPaint.setTextSize(defaultTextSize);

        thumpPaint = new Paint();
        thumpPaint.setAntiAlias(true);
        thumpPaint.setStyle(Paint.Style.FILL);
        thumpPaint.setColor(Color.argb(255, 255, 98, 98));

        trackPaint = new Paint();
        trackPaint.setAntiAlias(true);
        trackPaint.setStyle(Paint.Style.FILL);
        trackPaint.setColor(Color.argb(255, 211, 211, 211));
    }

    private int spToPx(int textSize) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                textSize, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0, height = 0;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        }
        this.width = width;
        this.height = height;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制轨迹
        RectF trackRect = new RectF(0, 0, width, height);
        canvas.drawRoundRect(trackRect, height / 2, height / 2, trackPaint);

        //绘制滑块
        RectF thumbRect = new RectF(trackStartX, trackStartY, trackStartX + width / 2 + height / 3, height);
        canvas.drawRoundRect(thumbRect, height / 2, height / 2, thumpPaint);

        //获取文本的大小
        Rect leftRect = new Rect();
        textPaint.getTextBounds(leftString, 0, leftString.length(), leftRect);
        Rect RightRect = new Rect();
        textPaint.getTextBounds(rightString, 0, rightString.length(), RightRect);
        //绘制文本
        if (clickLeftDown == 1) {
            textPaint.setColor(textPressColor);
            canvas.drawText(leftString, width / 4 - leftRect.width() / 2 + height / 6,
                    height / 2 + leftRect.height() / 2, textPaint);

            textPaint.setColor(textNormalColor);
            canvas.drawText(rightString, width * 3 / 4 - RightRect.width() / 2 - height / 6,
                    height / 2 + RightRect.height() / 2, textPaint);
        } else if (clickLeftDown == 0) {
            textPaint.setColor(textNormalColor);
            canvas.drawText(leftString, width / 4 - leftRect.width() / 2 + height / 6,
                    height / 2 + leftRect.height() / 2, textPaint);

            textPaint.setColor(textPressColor);
            canvas.drawText(rightString, width * 3 / 4 - RightRect.width() / 2 - height / 6,
                    height / 2 + RightRect.height() / 2, textPaint);
        } else {
            textPaint.setColor(textNormalColor);
            canvas.drawText(leftString, width / 4 - leftRect.width() / 2 + height / 6,
                    height / 2 + leftRect.height() / 2, textPaint);
            canvas.drawText(rightString, width * 3 / 4 - RightRect.width() / 2 - height / 6,
                    height / 2 + RightRect.height() / 2, textPaint);
        }
    }

    int clickLeftDown = -1;
    int clickLeftUp = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < width / 2) {
                    clickLeftDown = 1;
                } else {
                    clickLeftDown = 0;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (event.getX() < width / 2) {
                    clickLeftUp = 1;
                } else {
                    clickLeftUp = 0;
                }
                if (clickLeftDown == 1 && clickLeftUp == 1) {
                    toLeft();
                    if (switchCallBack != null) {
                        switchCallBack.callback(true);
                    }
                } else if (clickLeftDown == 0 && clickLeftUp == 0) {
                    toRight();
                    if (switchCallBack != null) {
                        switchCallBack.callback(false);
                    }
                } else {
//                    Toast.makeText(getContext(), "无效点击", Toast.LENGTH_SHORT).show();
                }
                clickLeftDown = -1;
                clickLeftUp = -1;
                invalidate();
                break;
        }
        return true;
    }

    public void toRight() {
        if (!isLeft || isAnim) {//滑块在右边和动画中返回
            return;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, width / 2 - height / 3);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(animationDuration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                trackStartX = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isAnim = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isLeft = !isLeft;
                isAnim = false;
            }
        });
        valueAnimator.start();
    }

    public void toLeft() {
        if (isLeft || isAnim) {//滑块在左边和动画中返回
            return;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofInt(width / 2 - height / 3, 0);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(animationDuration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                trackStartX = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isAnim = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isLeft = !isLeft;
                isAnim = false;
            }
        });
        valueAnimator.start();
    }
}
