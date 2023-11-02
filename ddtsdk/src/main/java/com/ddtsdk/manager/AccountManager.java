package com.ddtsdk.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ddtsdk.utils.GsonUtils;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.model.data.BaseUserData;
import com.ddtsdk.model.protocol.bean.LoginMessage;
import com.ddtsdk.model.data.UserData;
import com.ddtsdk.utils.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CZG on 2020/4/15.
 */
public class AccountManager {
    private static AccountManager sInstance;
    private UserData mUserData;         //登录用户
    private List<BaseUserData> mHistoryUserList = new ArrayList<>();    //登录历史记录
    private Context mContext;

    private AccountManager(Context context) {
        mContext = context;
    }

    public static AccountManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AccountManager(context);
        }
        return sInstance;
    }

    public static void setLoginMessage(LoginMessage loginMessage){
        if (loginMessage == null){
            Log.e("1342635","JAGSDCHASKGCZfdGCJKZJSA");
            return;
        }
        AppConstants.platform_pay = loginMessage.getPlatform_cash();
        AppConstants.userName = loginMessage.getUname();
        AppConstants.uid = loginMessage.getUid();
        AppConstants.isautonym = loginMessage.getIsautonym();
        AppConstants.forceautonym = loginMessage.getForceautonym();
        AppConstants.isnonage = loginMessage.getIsnonage();
        AppConstants.gametoken = loginMessage.getGametoken();
        AppConstants.time = loginMessage.getTime();
        AppConstants.userUrl = loginMessage.getUserurl();
        AppConstants.orderUrl = loginMessage.getOrderurl();
        AppConstants.serviceUrl = loginMessage.getService();
        AppConstants.libaourl = loginMessage.getLibaourl();
        AppConstants.valid = loginMessage.getValid();
        AppConstants.isolduser = loginMessage.getIsolduser();
        AppConstants.dologin_h5 = loginMessage.getDologin_h5();
        AppConstants.platformUrl = loginMessage.getFloat_url();

        if(AppConstants.reg_is_type ==1){
            AppConstants.is_adv = AppConstants.reg_is_adv;
            AppConstants.source = AppConstants.reg_source;
            AppConstants.adv_url = AppConstants.reg_adv_url;
            AppConstants.adv_img = AppConstants.reg_adv_img;
            AppConstants.reg_is_type=0;
        } else {
            AppConstants.is_adv = loginMessage.getAdv_info().getIs_adv();
            AppConstants.source = loginMessage.getAdv_info().getSource();
            AppConstants.adv_url = loginMessage.getAdv_info().getAdv_url();
            AppConstants.adv_img = loginMessage.getAdv_info().getAdv_img();
        }

        Log.e("CCCYHWA","Is_adv="+AppConstants.is_adv+",Source="+AppConstants.source+",adv_url="+AppConstants.adv_url+",adv_img="+AppConstants.adv_img);
    }

    public void setUserData(LoginMessage loginMessage) {
        if (loginMessage == null || TextUtils.isEmpty(loginMessage.getUid())){
            return;
        }
        mUserData = new UserData();
        mUserData.setUname(loginMessage.getUname());
        mUserData.setPwd(loginMessage.getPwd());
        mUserData.setUid(loginMessage.getUid());
        mUserData.setLastLoginTime(System.currentTimeMillis());
        mUserData.setLibaourl(loginMessage.getLibaourl());
        mUserData.setOrderurl(loginMessage.getOrderurl());
        mUserData.setService(loginMessage.getService());
        mUserData.setUserurl(loginMessage.getUserurl());

        String json = GsonUtils.getGson().toJson(mUserData);
        SharedPreferenceUtil.getInstance(mContext).setString(SharedPreferenceUtil.USER_CACHE, json);
    }

    //登录历史记录(存储三条数据)
    public void addHistoryUserData(String uid,String uname,String pwd,String phone){
        BaseUserData userData = new BaseUserData();
        userData.setUname(uname);
        userData.setPwd(pwd);
        userData.setUid(uid);
        userData.setPhone(phone);
        if (!TextUtils.isEmpty(phone) && phone.length() == 11){
            userData.setLoginType(BaseUserData.PHONE);
        } else {
            userData.setLoginType(BaseUserData.PASSWORD);
        }
        userData.setLastLoginTime(System.currentTimeMillis());
        if (mHistoryUserList.size() == 0){
            String historyCache = SharedPreferenceUtil.getInstance(mContext).getString(SharedPreferenceUtil.HISTORY_USER_CACHE, "");
            if (!TextUtils.isEmpty(historyCache) ){
                mHistoryUserList.clear();
                mHistoryUserList.addAll(GsonUtils.getInstance().fromJsonArray(historyCache, BaseUserData.class));
            }
        }
        for (int i = 0; i < mHistoryUserList.size(); i++) {
            if (TextUtils.equals(mHistoryUserList.get(i).getUid(),uid)){
                mHistoryUserList.remove(i);
                break;
            }
            if (TextUtils.equals(mHistoryUserList.get(i).getPhone(),"hb")){
                mHistoryUserList.remove(i);
                break;
            }
        }
        if (mHistoryUserList.size() > 2){
            mHistoryUserList.remove(mHistoryUserList.size()-1);
        }
        mHistoryUserList.add(0,userData);
        String historyListJson = GsonUtils.getInstance().toJson(mHistoryUserList);
        SharedPreferenceUtil.getInstance(mContext).setString(SharedPreferenceUtil.HISTORY_USER_CACHE,historyListJson);
    }

    public void deleteHBHistory(){
        if (mHistoryUserList.size() == 0){
            String historyCache = SharedPreferenceUtil.getInstance(mContext).getString(SharedPreferenceUtil.HISTORY_USER_CACHE, "");
            if (!TextUtils.isEmpty(historyCache)){
                mHistoryUserList.clear();
                mHistoryUserList.addAll(GsonUtils.getInstance().fromJsonArray(historyCache,BaseUserData.class));
            }
        }
        for (int i = 0; i < mHistoryUserList.size(); i++) {
            if (TextUtils.equals(mHistoryUserList.get(i).getPhone(),"hb")){
                mHistoryUserList.remove(i);
                String historyListJson = GsonUtils.getInstance().toJson(mHistoryUserList);
                SharedPreferenceUtil.getInstance(mContext).setString(SharedPreferenceUtil.HISTORY_USER_CACHE,historyListJson);
                break;
            }
        }
    }

    public List<BaseUserData> getHistoryUserList() {
        if (mHistoryUserList.size() == 0){
            String historyCache = SharedPreferenceUtil.getInstance(mContext).getString(SharedPreferenceUtil.HISTORY_USER_CACHE, "");
            if (!TextUtils.isEmpty(historyCache)){
                mHistoryUserList.clear();
                mHistoryUserList.addAll(GsonUtils.getInstance().fromJsonArray(historyCache,BaseUserData.class));
            }
        }
        if (mHistoryUserList.size() > 3){
            mHistoryUserList.remove(mHistoryUserList.size()-1);
        }
        return mHistoryUserList;
    }

    public boolean hasUserHistory(){
        if (mHistoryUserList.size() == 0){
            String historyCache = SharedPreferenceUtil.getInstance(mContext).getString(SharedPreferenceUtil.HISTORY_USER_CACHE, "");
            if (!TextUtils.isEmpty(historyCache)){
                mHistoryUserList.clear();
                mHistoryUserList.addAll(GsonUtils.getInstance().fromJsonArray(historyCache,BaseUserData.class));
            }
        }
        return mHistoryUserList.size() > 0;
    }

    public UserData getUser() {
        if (mUserData == null) {
            String cache = SharedPreferenceUtil.getInstance(mContext).getString(SharedPreferenceUtil.USER_CACHE, "");
            if (!TextUtils.isEmpty(cache) && cache.length() > 0) {
                mUserData =  GsonUtils.getInstance().fromJson(cache, UserData.class);
            }
        }
        return mUserData;
    }

    public static void logout(){
        /*AppConstants.userName = "";
        AppConstants.uid = "";
        AppConstants.isautonym = 0;
        AppConstants.forceautonym = 0;
        AppConstants.isnonage = 0;
        AppConstants.gametoken = "";
        AppConstants.time = "";
        AppConstants.userUrl = "";
        AppConstants.orderUrl = "";
        AppConstants.libaourl = "";
        AppConstants.valid = "";
        AppConstants.isolduser = 1;*/
        AppConstants.uid = "";
		AppConstants.userName = "";
		AppConstants.gametoken = "";
		AppConstants.time = "";
		/*AppConstants.Sessid = "";
		AppConstants.Token = "";
		AppConstants.ver_id = "";*/
		AppConstants.appId = 0;
		AppConstants.appKey = "";

        AppConstants.is_adv = 0;
        AppConstants.source = 0;
        AppConstants.adv_url = "";
        AppConstants.adv_img = "";

    }
}
