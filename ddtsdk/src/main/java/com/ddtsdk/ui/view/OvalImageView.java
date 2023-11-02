package com.ddtsdk.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * Created by CZG on 2018/11/15.
 * *用来显示不规则图片（圆角矩形图片）
 * 两种情况：1 四角圆角 allradius，2 左上和右上圆角 topradius
 * （默认 7dp）
 *
 */

public class OvalImageView extends ImageView {


    /*圆角的半径，依次为左上角xy半径，右上角，右下角，左下角*/
    private float[] rids = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,};
    private int mRadius = 20; // 默认0
    private boolean mIsAllRadius = true;// 是否四角为圆角
    private boolean mIsTopRadius;

    public OvalImageView(Context context) {
        super(context);
        initRidsArray(context);
    }

    public OvalImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initRidsArray(context);
    }

    public OvalImageView(Context context, AttributeSet attrs, int defStyle) {
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
