package com.ddtsdk.utils;

import android.content.Context;
import android.util.Log;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WeChatUtils {
    /**
     * 获取微信返回的Code,上传给服务端
     */

   public static String wc_appid = "";

   public static IWXAPI api;

   private static WeChatUtils weChatUtils;

   private WeChatUtils(){}

   public static WeChatUtils getInstance(){
       if (weChatUtils == null){
           weChatUtils = new WeChatUtils();
       }
       return weChatUtils;
   }

   public void wechaBind(Context context, String appid){
       wc_appid = appid;
       api = WXAPIFactory.createWXAPI(context, wc_appid,false);
       api.registerApp(wc_appid);
       Log.e("WithDrawWebDialog", "wc_appid=" + wc_appid);
       wechatLogin();
   }

   private void wechatLogin(){
       //如果有安装则直接发起请求
       final SendAuth.Req req = new SendAuth.Req();
       //scope是应用授权作用域，如获取用户个人信息则填写snsapi_userinfo
       req.scope = "snsapi_userinfo";
       //state用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击）
       // 建议第三方带上该参数，可设置为简单的随机数加session进行校验
       req.state = "wechat_sdk_ddtgame";
       //发起请求
       api.sendReq(req);
   }
}
