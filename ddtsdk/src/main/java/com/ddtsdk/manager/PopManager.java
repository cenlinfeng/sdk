package com.ddtsdk.manager;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.ddtsdk.KLSDK;
import com.ddtsdk.KLSDKClient;
import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.model.protocol.bean.PopMsg;
import com.ddtsdk.model.protocol.params.NoDataParams;
import com.ddtsdk.utils.CheckApkExistUtils;
import com.ddtsdk.view.PopupPicDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 弹窗管理类，czh， 2021/5/14
 */
public class PopManager {
    static int type = 0;  //0：普通弹窗， 1：跳转盒子弹窗，2：代金券弹窗
    static String picUrl = "";
    static PopMsg mPopMsg = null;
    static boolean isShowing = false;

    private PopManager(){}
    public static void show(){
        isShowing = false;
        //AppConstants.source == 1 代表是代金券弹窗
       if (AppConstants.source == 1){
           type = 2;
           picUrl = AppConstants.adv_img;
           showDialog();
//           picUrl = "http://api.ddt.lynaqi.com/Public/images/tipCouponsImg-pc.png";
       }
       else{
           HttpRequestClient.sendPostRequest(ApiConstants.ACTION_SDK_POP, new NoDataParams(), PopMsg.class, new HttpRequestClient.ResultHandler<PopMsg>(KLSDK.getInstance().getContext()) {
               @Override
               public void onSuccess(PopMsg popMsg) {
//                   popMsg.setDirection("0");
//                   List<String> lss = new ArrayList<>();
//                   lss.add("https://down.oss.ykjgame.com/new.down.ddt.ddtugame.com/gamepacks/1855/1855_ttsdk_1855_782.apk");
//                   popMsg.setPackdownurl(lss);
//                   List<String> pn = new ArrayList<>();
//                   pn.add("com.ttsdk.qchy.qcw");
//                   popMsg.setPackname(pn);
//                   popMsg.setImage("http://api.ddt.lynaqi.com/Public/images/tipCouponsImg-pc.png");
//                   popMsg.setImage2("http://api.ddt.lynaqi.com/Public/images/tipCouponsImg-pc.png");
//                   popMsg.setUrl("https://m.qichugame.com/news/pingtai/454.html");
//                   popMsg.setMtype("4");
//                   popMsg.setMid("10");
                   mPopMsg = popMsg;
                   if (TextUtils.isEmpty(popMsg.getMtype())){
                       type = 0;
                       Log.e("CCCYHWA",AppConstants.adv_img+"======");
                       if (!TextUtils.isEmpty(AppConstants.adv_img)) picUrl = AppConstants.adv_img;
                   }else {
                       type = 1;
                       if ("0".equals(popMsg.getDirection())) picUrl = popMsg.getImage();
                       else picUrl = popMsg.getImage2();
                   }
                   showDialog();
               }

               @Override
               public void onFailure(Throwable t) {
                   type = 0;
                   if (!TextUtils.isEmpty(AppConstants.adv_img)) picUrl = AppConstants.adv_img;
                   showDialog();
               }
           });
       }
    }

    private static void showDialog(){
        if (!TextUtils.isEmpty(picUrl) && !isShowing) {
            isShowing = true;
            Log.e("CCCYHWA",picUrl+"=======picUrl");
            new PopupPicDialog(KLSDK.getInstance().getContext(), picUrl, type, mPopMsg).show();
            picUrl = "";
        }
        else {
                if (!TextUtils.isEmpty(AppConstants.init_adv_img) && !isShowing) {
                    AppConstants.is_adv   = AppConstants.init_is_adv;
                    AppConstants.adv_url=AppConstants.init_adv_url;
                    AppConstants.adv_img= AppConstants.init_adv_img;
                        if(AppConstants.init_is_adv == 1){
                            isShowing = true;
                            new PopupPicDialog(KLSDK.getInstance().getContext(),AppConstants.adv_img, 0, null).show();
                        }
                }
        }
    }
}
