package com.ly.yougreat.app.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ly.yougreat.app.R;
import com.ly.yougreat.app.Utils;


public class YouGreatNum extends View {

    private int goodNum;
    private int textMoveHeight;//文字上下移动的距离的上限
    private int duration = 200;

    private float textDy;//文字上下移动的动态值
    private float textAlpha;
    private float[] widths;

    private boolean isSelected;

    private Bitmap bitmap_exp;
    private Rect textBounds;

    private Paint textPaint;
    private Paint oldTextPaint;

    public YouGreatNum(Context context) {
        this(context, null);
    }

    public YouGreatNum(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YouGreatNum(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.YouGreatNum);
        goodNum = a.getInt(R.styleable.YouGreatNum_num, 2022);
        a.recycle();

        textBounds = new Rect();
        widths = new float[6];//默认支持到6位数

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        oldTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        //初始化文字相关配置
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(Utils.spToPx(getContext(), 14));
        oldTextPaint.setColor(Color.GRAY);
        oldTextPaint.setTextSize(Utils.spToPx(getContext(), 14));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Resources resources = getResources();
        bitmap_exp = BitmapFactory.decodeResource(resources, R.mipmap.ic_messages_like_unselected);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        bitmap_exp.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //高度默认限定为bitmap的高度加上上下margin各10dp
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(bitmap_exp.getHeight() + Utils.dpToPx(getContext(), 20), MeasureSpec.EXACTLY);
        //宽度默认为bitmap的宽度加上左右margin各10dp，文字的宽度和文字右侧10dp
        String s = String.valueOf(goodNum);
        float textWidth = textPaint.measureText(s, 0, s.length());
        widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (bitmap_exp.getWidth() + textWidth + Utils.dpToPx(getContext(), 30)), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = getHeight();

        String value = String.valueOf(goodNum);
        String oldValue;
        if (isSelected) {
            oldValue = String.valueOf(goodNum - 1);
        } else {
            oldValue = String.valueOf(goodNum + 1);
        }
        int length = value.length();
        //获取文字绘制的坐标
        textPaint.getTextBounds(value, 0, length, textBounds);
        int textY = height / 2 - (textBounds.top + textBounds.bottom) / 2;
        int textX = 0;//手动加上10dp的margin
        if (length != oldValue.length() || textMoveHeight == 0) {
            //直接绘制文字 没找到即刻App里面对这种情况的处理效果
            canvas.drawText(value, textX, textY, textPaint);
            return;
        }
        //把文字拆解成一个一个的字符
        textPaint.getTextWidths(value, widths);
        char[] chars = value.toCharArray();
        char[] oldChars = oldValue.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (oldChars[i] == chars[i]) {
                textPaint.setAlpha(255);
                canvas.drawText(String.valueOf(chars[i]), textX, textY, textPaint);
            } else {
                if (isSelected) {
                    oldTextPaint.setAlpha((int) (255 * (1 - textAlpha)));
                    canvas.drawText(String.valueOf(oldChars[i]), textX, textY - textMoveHeight + textDy, oldTextPaint);
                    textPaint.setAlpha((int) (255 * textAlpha));
                    canvas.drawText(String.valueOf(chars[i]), textX, textY + textDy, textPaint);
                } else {
                    oldTextPaint.setAlpha((int) (255 * (1 - textAlpha)));
                    canvas.drawText(String.valueOf(oldChars[i]), textX, textY + textMoveHeight + textDy, oldTextPaint);
                    textPaint.setAlpha((int) (255 * textAlpha));
                    canvas.drawText(String.valueOf(chars[i]), textX, textY + textDy, textPaint);
                }
            }
            textX += widths[i];
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
//        super.setOnClickListener(l);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                toggle();
//                break;
//        }
//        return super.onTouchEvent(event);
        return false;
    }

    public void toggle(boolean isTouch) {
        isSelected = !isTouch;
        if (isSelected) {
            ++goodNum;
        } else {
            --goodNum;
        }
        setGoodNum();
    }

    @Keep
    public void setTextAlpha(float textAlpha) {
        this.textAlpha = textAlpha;
        invalidate();
    }

    @Keep
    public void setTextDy(float textDy) {
        this.textDy = textDy;
        invalidate();
    }

    public void setGoodNum() {
        float startY;
        textMoveHeight = Utils.dpToPx(getContext(), 20);
        if (isSelected) {
            startY = textMoveHeight;
        } else {
            startY = -textMoveHeight;
        }

        ObjectAnimator textInAlphaAnim = ObjectAnimator.ofFloat(this, "textAlpha", 0f, 1f);
        textInAlphaAnim.setDuration(duration);
        ObjectAnimator dyAnim = ObjectAnimator.ofFloat(this, "textDy", startY, 0);
        dyAnim.setDuration(duration);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(textInAlphaAnim, dyAnim);
        set.start();
    }
}
