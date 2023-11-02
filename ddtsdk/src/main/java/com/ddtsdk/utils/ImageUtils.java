package com.ddtsdk.utils;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.regex.Pattern;

/**
 * Created by CZG on 2020/5/26.
 */

public class ImageUtils {

    public static void loadImage(Fragment fragment, ImageView view, String url) {
        if (isImgUrl(url)){
            Glide.with(fragment).load(url).into(view);
        }
    }

    public static void loadGit(ImageView view, int picid) {
        Glide.with(view.getContext()).load(picid).asGif().into(view);
    }

    public static void loadGit(final ImageView view, String url) {
            Glide.with(view.getContext())
                    .load(url)
                    .asGif()
                    .centerCrop()
                    .into(view);
    }

    public static void loadImage(ImageView view, String url) {
        if (isImgUrl(url)){
            Glide.with(view.getContext()).load(url).into(view);
        }
    }

    public static void loadImage(final ImageView view, String url, int placeholder) {
        if (isImgUrl(url)){
            Glide.with(view.getContext())
                    .load(url)
                    .asBitmap()
                    .placeholder(placeholder)
                    .error(placeholder)
                    .centerCrop()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            view.setImageBitmap(resource);
                        }
                    });
        }
    }

    public static Drawable getColorDrawable(Context context, String drawableStr, String colorStr) {
        Drawable drawable = context.getResources().getDrawable(resourceId(context,drawableStr,"mipmap"));
        @ColorInt int color = Color.parseColor(colorStr);

        Bitmap mBitmap = ((BitmapDrawable)drawable).getBitmap();
        Bitmap mAlphaBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas mCanvas = new Canvas(mAlphaBitmap);
        Paint mPaint = new Paint();

        mPaint.setColor(color);
        //从原位图中提取只包含alpha的位图
        Bitmap alphaBitmap = mBitmap.extractAlpha();
        //在画布上（mAlphaBitmap）绘制alpha位图
        mCanvas.drawBitmap(alphaBitmap, 0, 0, mPaint);

        return new BitmapDrawable(context.getResources(),mAlphaBitmap);
    }

    public static Drawable tintDrawable(Context context, String drawableStr, String colorStr) {
        Drawable drawable = context.getResources().getDrawable(resourceId(context,drawableStr,"mipmap"));
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        @ColorInt int color = Color.parseColor(colorStr);
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }

    public static int resourceId(Context context,String name, String type) {
        return context.getResources().getIdentifier(name, type, context.getPackageName());
    }

    /**
     * 判断一个url是否为图片url
     *
     * @param url
     * @return
     */
    public static boolean isImgUrl(String url) {
        if (url == null || url.trim().length() == 0)
            return false;
        return Pattern.compile(".*?(gif|jpeg|png|jpg|bmp)").matcher(url).matches();
    }

    //加载长图
    /*public static void loadLongImage(Context context, String url, final SubsamplingScaleImageView image, int screenWidth) {
        final int maxWidth;
        if (screenWidth <= 0) {
            maxWidth = image.getWidth();
        } else {
            maxWidth = screenWidth;
        }
        Glide.with(context).load(url).downloadOnly(new SimpleTarget<File>() {

            @Override
            public void onResourceReady(File resource, Transition<? super File> transition) {
                BitmapFactory.Options option = new BitmapFactory.Options();
                option.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(resource.getPath(), option);
                float scale;
                int width = option.outWidth;
                int height = option.outHeight;
                ViewGroup.LayoutParams para = image.getLayoutParams();

                para.width = maxWidth;
                para.height = maxWidth * height / width;
                image.setLayoutParams(para);
                scale = ((float) maxWidth) / ((float) width);

                image.setMinScale(scale);//最小显示比例
                image.setMaxScale(scale);//最大显示比例（太大了图片显示会失真，因为一般微博长图的宽度不会太宽）
                // 将保存的图片地址给SubsamplingScaleImageView,这里注意设置ImageViewState设置初始显示比例
                image.setImage(ImageSource.uri(Uri.fromFile(resource)), new ImageViewState(scale, new PointF(0, 0), 0));
            }
        });
    }*/
}
