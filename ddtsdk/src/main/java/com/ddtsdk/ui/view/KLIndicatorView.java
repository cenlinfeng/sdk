package com.ddtsdk.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.ddtsdk.constants.AppConstants;

public class KLIndicatorView extends View {

    public KLIndicatorView(Context context) {
        super(context);
        initIndicatorView();
    }

    public KLIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initIndicatorView();
    }

    public KLIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initIndicatorView();
    }

    private void initIndicatorView() {
        GradientDrawable drawable = (GradientDrawable) getBackground();
        int[] colors = {Color.WHITE, Color.parseColor(AppConstants.colorPrimary)};
        drawable.setColors(colors);
    }
}
