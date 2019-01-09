package com.ly.yougreat.app.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ly.yougreat.app.IyLog;
import com.ly.yougreat.app.R;
import com.ly.yougreat.app.Utils;


public class YouGreatBitmap extends View {

    private int duration = 200;

    private float shiningAlpha;
    private float shiningScale;
    private float handScale = 1.0f;

    private boolean isSelected;

    private Bitmap unselected;
    private Bitmap selected;
    private Bitmap shining;

    private Paint bitmapPaint;

    public YouGreatBitmap(Context context) {
        this(context, null);
    }

    public YouGreatBitmap(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YouGreatBitmap(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        this.setBackgroundColor(Color.BLUE);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Resources resources = getResources();
        unselected = BitmapFactory.decodeResource(resources, R.mipmap.ic_messages_like_unselected);
        selected = BitmapFactory.decodeResource(resources, R.mipmap.ic_messages_like_selected);
        shining = BitmapFactory.decodeResource(resources, R.mipmap.ic_messages_like_selected_shining);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unselected.recycle();
        selected.recycle();
        shining.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //高度默认限定为bitmap的高度加上上下margin各10dp
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(selected.getHeight() + Utils.dpToPx(getContext(), 20), MeasureSpec.EXACTLY);
        //宽度默认为bitmap的宽度加上左右margin各10dp，文字的宽度和文字右侧10dp
        widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (selected.getWidth() + Utils.dpToPx(getContext(), 5)), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = getHeight();
        int centerY = height / 2;
        Bitmap handBitmap = isSelected ? selected : unselected;
        int handBitmapWidth = handBitmap.getWidth();
        int handBitmapHeight = handBitmap.getHeight();

        //画小手
        int handTop = (height - handBitmapHeight) / 2;
        canvas.save();
        canvas.scale(handScale, handScale, handBitmapWidth / 2, centerY);
        canvas.drawBitmap(handBitmap, 0, handTop, bitmapPaint);
        canvas.restore();

        //画shining
        int shiningTop = handTop - shining.getHeight() + Utils.dpToPx(getContext(), 7);//手动加上6dp的margin
        bitmapPaint.setAlpha((int) (255 * shiningAlpha));
        canvas.save();
        canvas.scale(shiningScale, shiningScale, handBitmapWidth / 2, handTop);
        canvas.drawBitmap(shining, 2, shiningTop, bitmapPaint);
        canvas.restore();
        //恢复bitmapPaint透明度
        bitmapPaint.setAlpha(255);
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
        IyLog.i("isTouch = " + isTouch);
        isSelected = !isTouch;
        if (isSelected) {
            ObjectAnimator handScaleAnim = ObjectAnimator.ofFloat(this, "handScale", 1f, 0.8f, 1f);
            handScaleAnim.setDuration(duration);

            ObjectAnimator shiningAlphaAnim = ObjectAnimator.ofFloat(this, "shiningAlpha", 0f, 1f);
            shiningAlphaAnim.setDuration(duration);

            ObjectAnimator shiningScaleAnim = ObjectAnimator.ofFloat(this, "shiningScale", 0f, 1f);
            shiningScaleAnim.setDuration(duration);

            AnimatorSet set = new AnimatorSet();
            set.playTogether(handScaleAnim, shiningAlphaAnim, shiningScaleAnim);
            set.start();

//            ObjectAnimator shiningAlphaAnim1 = ObjectAnimator.ofFloat(this, "shiningAlpha", 0f, 1f, 0f);
//            shiningAlphaAnim1.setDuration(500);
//            AnimatorSet set1 = new AnimatorSet();
//            set1.play(shiningAlphaAnim1);
//            set1.start();

        } else {
            ObjectAnimator handScaleAnim = ObjectAnimator.ofFloat(this, "handScale", 1f, 0.8f, 1f);
            handScaleAnim.setDuration(duration);
            handScaleAnim.start();

            setShiningAlpha(0);
        }
    }

    @Keep
    public void setShiningAlpha(float shiningAlpha) {
        this.shiningAlpha = shiningAlpha;
        invalidate();
    }

    @Keep
    public void setShiningScale(float shiningScale) {
        this.shiningScale = shiningScale;
        invalidate();
    }

    @Keep
    public void setHandScale(float handScale) {
        this.handScale = handScale;
        invalidate();
    }
}
