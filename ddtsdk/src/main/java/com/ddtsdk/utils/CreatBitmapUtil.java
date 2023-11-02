package com.ddtsdk.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ddtsdk.constants.AppConfig;

import java.io.File;
import java.io.FileOutputStream;

import static android.view.View.DRAWING_CACHE_QUALITY_HIGH;

/**
 * 生成用户信息的截图并保存。(文件名含时间戳)
 *
 * 保存完成通知手机相册更新。
 *
 * Created by a5706 on 2018/4/18.
 */

public class CreatBitmapUtil {

    private volatile static CreatBitmapUtil uniqueInstance;
    private Context mContext;

    private CreatBitmapUtil(Context context){
        this.mContext = context;
    }
    public static CreatBitmapUtil getInstance(Context context){
        if(uniqueInstance == null){
            synchronized(CreatBitmapUtil.class){
                if(uniqueInstance == null){
                    uniqueInstance = new CreatBitmapUtil(context);
                }
            }
        }
        return uniqueInstance;
    }

    //保存到本地并弹窗提示位置
    public void saveUserBitmap(String name,String pwd){
        long timestamp = System.currentTimeMillis();
        Bitmap bitmap = createTextImage(name,pwd);
        if (bitmap != null) {
            try {
                // 新建一个文件(若未存在)
                // 图片文件路径
                String filePath = creatFile() + "kl_pic_"+timestamp+".png";
                File file = new File(filePath);
                FileOutputStream os = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
                addMediaStore(mContext,file,filePath);
                showMsg("保存图片成功");
            } catch (Exception e) {
                LogUtil.e("截图失败:"+e.toString());
                e.printStackTrace();
            }
        }
    }

    /**
     * @param context
     * @param targetFile 要保存的照片文件
     * @param path  要保存的照片的路径地址
     */
    private void addMediaStore(Context context, File targetFile, String path) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues newValues = new ContentValues(5);
        newValues.put(MediaStore.Images.Media.DISPLAY_NAME, targetFile.getName());
        newValues.put(MediaStore.Images.Media.DATA, targetFile.getPath());
        newValues.put(MediaStore.Images.Media.DATE_MODIFIED, System.currentTimeMillis() / 1000);
        newValues.put(MediaStore.Images.Media.SIZE, targetFile.length());
        newValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, newValues);
        MediaScannerConnection.scanFile(context, new String[]{path}, null, null);//刷新相册
    }

    //绘制内容
    private Bitmap createTextImage(String name, String pwd) {
        int width = 380;
        int height=300;
        int txtSize=12;
        int logoId = AppConfig.resourceId(mContext,"kl_icon_top_bar","mipmap");
        Bitmap logo= BitmapFactory.decodeResource(mContext.getResources(), logoId);
        Bitmap logo_s = small(logo);
        Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(txtSize);
        Canvas canvas = new Canvas(bm);
        canvas.drawColor(Color.WHITE);
        //绘制logo
        canvas.drawBitmap(logo_s,95,20,paint);
        //提示语
        String tips1 = "请妥善保管您的账号和密码，升级为手机账号或绑定手机能更好地";
        String tips2 = "保护您的账号安全和获得VIP专属礼遇服务。如有疑问，亦可添加";
        String tips3 = "官方客服QQ：1507287451";
        //tips1的绘制起始x、y坐标
        int posX = 20;
        int posY = 210;
        //开始绘制tips
        canvas.drawText(tips1, posX, posY, paint);
        canvas.drawText(tips2, posX, posY+15, paint);
        canvas.drawText(tips3, posX, posY+30, paint);
        //绘制两个圆角矩形

        Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint1.setColor(Color.GRAY);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(1);
        //第一个圆角矩形
        int posY1 = 95;  //y轴起点
        RectF rect1 = new RectF(posX,posY1,width-posX,posY1+30);//左上角，右下角
        canvas.drawRoundRect(rect1,15,15,paint1);//@NonNull RectF rect, float rx, float ry, @NonNull Paint paint
        //第二个圆角矩形
        int posY2 = posY1+50;  //y轴起点
        RectF rect2 = new RectF(posX,posY2,width-posX,posY2+30);
        canvas.drawRoundRect(rect2,15,15,paint1);

        //填充一键注册的账号密码
        int posXX1 = posX+20;
        int posYY1 = posY1+20;
        int posYY2 = posY2+20;
        canvas.drawText("账号:"+name, posXX1, posYY1, paint);
        canvas.drawText("密码:"+pwd, posXX1, posYY2, paint);
        return bm;
    }

    //缩放logo
    private Bitmap small(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(0.8f,0.8f); //长和宽放大缩小的比例
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return resizeBmp;
    }

    private void showMsg(final String msg) {
        ((Activity)mContext).runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    //创建并返回文件夹名
    private String creatFile() {
        String SDPATH= Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator;
        String ddtFile = SDPATH+"ddtFile"+ File.separator;
        //生成文件夹
        File file = null;
        try {
            file = new File(ddtFile);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            LogUtil.e("CreatBitmapUtil----creatFile---error:"+e);
        }
        return ddtFile;
    }

    private void setText(ViewGroup view, String account, String pwd){
        for (int i = 0; i < view.getChildCount(); i++){
            if (view.getChildAt(i) instanceof TextView){
                if ((((TextView) view.getChildAt(i)).getText().toString().contains("账号"))){
                    ((TextView) view.getChildAt(i)).setText("账号:" + account);
                }
                if ((((TextView) view.getChildAt(i)).getText().toString().contains("密码"))){
                    ((TextView) view.getChildAt(i)).setText("密码:" + pwd);
                }
            }
            if (view.getChildAt(i) instanceof ViewGroup){
                setText(view, account, pwd);
            }
        }
    }

    private Bitmap createViewImage(View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(DRAWING_CACHE_QUALITY_HIGH);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    //保存view到本地并弹窗提示位置
    public void saveUserViewBitmap(View view){
        long timestamp = System.currentTimeMillis();
        Bitmap bitmap = createViewImage(view);
        if (bitmap != null) {
            try {
                // 新建一个文件(若未存在)
                // 图片文件路径
                String filePath = creatFile() + "kl_pic_"+timestamp+".png";
                File file = new File(filePath);
                FileOutputStream os = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                os.close();
                view.destroyDrawingCache();
                addMediaStore(mContext,file,filePath);
                showMsg("保存图片成功");
            } catch (Exception e) {
                Log.e("截图失败:", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void updatePic(Context context, File file){
        //通知相册更新
        MediaStore.Images.Media.insertImage(context.getContentResolver(), BitmapFactory.decodeFile(file.getAbsolutePath()), file.getName(), null);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }
}
