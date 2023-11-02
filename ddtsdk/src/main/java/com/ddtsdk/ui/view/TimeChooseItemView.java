package com.ddtsdk.ui.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ddtsdk.mylibrary.R;
import com.ddtsdk.utils.LogUtil;


public class TimeChooseItemView extends AppCompatImageView {
    private int mHeight;
    private int mNumber;

    public TimeChooseItemView(Context context) {
        super(context);
        init();
    }

    public TimeChooseItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimeChooseItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public int getData() {
        return mNumber;
    }

    public void setData(int number) {
        mNumber = number;

        setImageResource(getResId(number));
    }

    protected void setItemHeight(int height) {
        mHeight = height;
    }

    protected void setItemSelected(boolean selected) {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(mHeight, View.MeasureSpec.EXACTLY));
    }

    private void init() {
    }

    //分片switch造成动画毫秒级延迟问题
    private @DrawableRes
    int getResId(int number) {
        if (number >=0 && number <= 12){
            return getBlock(number);
        }

        if (number >= 13 && number<=25){
            return getClubs(number);
        }

        if (number >= 26 && number<=38){
            return getHeart(number);
        }

        if (number >= 39 && number<=51){
            return getSpade(number);
        }

//        Log.e("tag","<-1");
        return R.drawable.kl_poker_bg;
    }


    private int getBlock(int index) {
        switch (index) {
            case 0:
                return R.drawable.block_2;
            case 1:
                return R.drawable.block_3;
            case 2:
                return R.drawable.block_4;
            case 3:
                return R.drawable.block_5;
            case 4:
                return R.drawable.block_6;
            case 5:
                return R.drawable.block_7;
            case 6:
                return R.drawable.block_8;
            case 7:
                return R.drawable.block_9;
            case 8:
                return R.drawable.block_10;
            case 9:
                return R.drawable.block_j;
            case 10:
                return R.drawable.block_q;
            case 11:
                return R.drawable.block_k;
            case 12:
                return R.drawable.block_a;
        }
        return -1;
    }

    private int getClubs(int index) {
        switch (index) {
            case 13:
                return R.drawable.clubs_2;
            case 14:
                return R.drawable.clubs_3;
            case 15:
                return R.drawable.clubs_4;
            case 16:
                return R.drawable.clubs_5;
            case 17:
                return R.drawable.clubs_6;
            case 18:
                return R.drawable.clubs_7;
            case 19:
                return R.drawable.clubs_8;
            case 20:
                return R.drawable.clubs_9;
            case 21:
                return R.drawable.clubs_10;
            case 22:
                return R.drawable.clubs_j;
            case 23:
                return R.drawable.clubs_q;
            case 24:
                return R.drawable.clubs_k;
            case 25:
                return R.drawable.clubs_a;
        }
        return -1;
    }

    private int getHeart(int index) {
        switch (index) {
            case 26:
                return R.drawable.heart_2;
            case 27:
                return R.drawable.heart_3;
            case 28:
                return R.drawable.heart_4;
            case 29:
                return R.drawable.heart_5;
            case 30:
                return R.drawable.heart_6;
            case 31:
                return R.drawable.heart_7;
            case 32:
                return R.drawable.heart_8;
            case 33:
                return R.drawable.heart_9;
            case 34:
                return R.drawable.heart_10;
            case 35:
                return R.drawable.heart_j;
            case 36:
                return R.drawable.heart_q;
            case 37:
                return R.drawable.heart_k;
            case 38:
                return R.drawable.heart_a;
        }
        return -1;
    }

    private int getSpade(int index) {
        switch (index) {
            case 39:
                return R.drawable.spade_2;
            case 40:
                return R.drawable.spade_3;
            case 41:
                return R.drawable.spade_4;
            case 42:
                return R.drawable.spade_5;
            case 43:
                return R.drawable.spade_6;
            case 44:
                return R.drawable.spade_7;
            case 45:
                return R.drawable.spade_8;
            case 46:
                return R.drawable.spade_9;
            case 47:
                return R.drawable.spade_10;
            case 48:
                return R.drawable.spade_j;
            case 49:
                return R.drawable.spade_q;
            case 50:
                return R.drawable.spade_k;
            case 51:
                return R.drawable.spade_a;

        }
        return -1;
    }


}
