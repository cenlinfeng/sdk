package com.ddtsdk.ui.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ddtsdk.ui.activity.KLPokerActivity;
import com.ddtsdk.utils.ToastUtils;
import com.ddtsdk.utils.Utils;

import java.util.List;


public class LuckPokerView extends LinearLayout {
    private TimeChooseView mItemFirst;
    private TimeChooseView mItemSecond;
    private TimeChooseView mItemThirdly;
    private TimeChooseView mItemFourthly;
    private TimeChooseView mItemFifth;
    private int mEndCount;

    private static final int MSG_DO_STOP = 0x04;
    private long mLastDisplayTime = 0;
    private static onStopEndListener mOnStopEndListener;


    private boolean mStarted;
    private Handler handler;
    private Activity mContext;


    public LuckPokerView(Context context) {
        super(context);
        init(context);
    }

    public LuckPokerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LuckPokerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void start() {
        if (mStarted) {
            return;
        }
        mStarted = true;

        mEndCount = 0;

        mItemFirst.start(50);
        mItemSecond.start(50);
        mItemThirdly.start(50);
        mItemFourthly.start(50);
        mItemFifth.start(50);

    }

    public void stop(int one,int two,int three,int four,int five) {
        if (!mStarted) {
            return;
        }

        mItemFirst.stop(one, 5000);
        mItemSecond.stop(two, 5500);
        mItemThirdly.stop(three, 6000);
        mItemFourthly.stop(four, 6500);
        mItemFifth.stop(five, 7000);
    }


    private void init(Context context) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        mContext = (Activity) context;

        mItemFirst = buildItem();
        mItemSecond = buildItem();
        mItemThirdly = buildItem();
        mItemFourthly = buildItem();
        mItemFifth = buildItem();
        addView(mItemFirst, buildLayoutParam(true));
        addView(mItemSecond, buildLayoutParam(false));
        addView(mItemThirdly, buildLayoutParam(false));
        addView(mItemFourthly, buildLayoutParam(false));
        addView(mItemFifth, buildLayoutParam(false));
    }

    private LayoutParams buildLayoutParam(boolean first) {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 3, 0);
        if (!first) {
            params.leftMargin = Utils.dp2px(getContext(), 2);
        }
        params.topMargin = Utils.dp2px(getContext(), 4);
        return params;
    }

    private TimeChooseView buildItem() {
        TimeChooseView item = new TimeChooseView(getContext());
        item.setAutoScrollEndListener(new TimeChooseView.AutoScrollEndListener() {
            @Override
            public void onScrollEnd() {
                mEndCount++;
                if (mEndCount == getChildCount()) {
                    onShaiEnd();
                }
            }
        });
        return item;
    }


    private void onShaiEnd() {
        mStarted = false;
        if (mOnStopEndListener != null) {
            mOnStopEndListener.onStopEnd();
        }
    }

    public interface onStopEndListener {
        void onStopEnd();
    }

    public static void setOnStopEndListener(onStopEndListener listener) {
        mOnStopEndListener = listener;
    }
}
