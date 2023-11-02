package com.ddtsdk.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

import com.ddtsdk.constants.AppConstants;

@SuppressLint("AppCompatCustomView")
public class KLTextView extends TextView {

    public KLTextView(Context context) {
        super(context);
        initView();
    }

    public KLTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public KLTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        GradientDrawable drawable = (GradientDrawable) getBackground();
        drawable.setColor(Color.parseColor(AppConstants.colorPrimary));
    }
}
