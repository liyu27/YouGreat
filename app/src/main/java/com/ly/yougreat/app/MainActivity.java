package com.ly.yougreat.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ly.yougreat.app.view.YouGreatBitmap;
import com.ly.yougreat.app.view.YouGreatNum;

public class MainActivity extends AppCompatActivity {


    LinearLayout mLinearLayout;
    LinearLayout mActiveView;

    YouGreatBitmap mGoodJobView;
    YouGreatNum mGoodJobView_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);


        mLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mLinearLayout.setLayoutParams(layoutParams);
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView textView = new TextView(this);
        textView.setText("测试一行字符串");
        mLinearLayout.addView(textView);

        mActiveView = new LinearLayout(this);
        LinearLayout.LayoutParams activeLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mActiveView.setLayoutParams(activeLayoutParams);
        mActiveView.setGravity(Gravity.CENTER_HORIZONTAL);
//        mActiveView.setBackgroundColor(Color.MAGENTA);
        mLinearLayout.addView(mActiveView);

        mGoodJobView = new YouGreatBitmap(this);
        mGoodJobView_num = new YouGreatNum(this);
        mActiveView.addView(mGoodJobView);
        mActiveView.addView(mGoodJobView_num);

        mActiveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoodJobView.toggle(Global.gIsSelected);
                mGoodJobView_num.toggle(Global.gIsSelected);
                Global.gIsSelected = !Global.gIsSelected;
            }
        });

//        mActiveView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                IyLog.i("000 is selected = " + Global.gIsSelected);
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        mGoodJobView.toggle(Global.gIsSelected);
//                        mGoodJobView_num.toggle(Global.gIsSelected);
//                        Global.gIsSelected = !Global.gIsSelected;
//                        IyLog.i("999 is selected = " + Global.gIsSelected);
//                        break;
//                }
//                return false;
//            }
//        });

        setContentView(mLinearLayout);
    }
}
