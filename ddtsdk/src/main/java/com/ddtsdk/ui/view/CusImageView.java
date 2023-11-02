package com.ddtsdk.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;


public class CusImageView extends ImageView {

    private final int pic_width = 536;
    private final int pic_heigth = 252;
    /*圆角的半径，依次为左上角xy半径，右上角，右下角，左下角*/
    private float[] rids = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,};
    private int mRadius = 20; // 默认0
    private boolean mIsAllRadius = true;// 是否四角为圆角
    private boolean mIsTopRadius;

    public CusImageView(Context context) {
        super(context);
        initRidsArray(context);
    }

    public CusImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initRidsArray(context);
    }

    public CusImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initRidsArray(context);
    }

    private void initRidsArray(Context context){
        if(mIsAllRadius) {
            rids[0] = mRadius;
            rids[1] = mRadius;
            rids[2] = mRadius;
            rids[3] = mRadius;
            rids[4] = mRadius;
            rids[5] = mRadius;
            rids[6] = mRadius;
            rids[7] = mRadius;
        }else {
            if (mIsTopRadius){
                rids[0] = mRadius;
                rids[1] = mRadius;
                rids[2] = mRadius;
                rids[3] = mRadius;
            } else {
                rids[4] = mRadius;
                rids[5] = mRadius;
                rids[6] = mRadius;
                rids[7] = mRadius;
            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int heigth = MeasureSpec.getSize(heightMeasureSpec);
        Log.e("width", "widthMeasureSpec=" + width + "  heightMeasureSpec=" + ((int)(width/(pic_width * 1.0f) * pic_heigth)));

        setMeasuredDimension(width, (int) (width/(pic_width * 1.0f) * pic_heigth));
    }

    /**
     * 画图
     *
     * @param canvas
     */
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        int w = this.getWidth();
        int h = this.getHeight();
        /*向路径中添加圆角矩形。radii数组定义圆角矩形的四个圆角的x,y半径。radii长度必须为8*/
        path.addRoundRect(new RectF(0, 0, w, h), rids, Path.Direction.CW);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG| Paint.FILTER_BITMAP_FLAG));
        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}
