package com.ddtsdk.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.model.protocol.params.AppListParams;
import com.ddtsdk.common.network.request.HttpRequestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 获取用户手机应用列表。第三方应用。
 * Created by a5706 on 2018/5/28.
 */

public class GetAppInfoUtil {

    private static GetAppInfoUtil sAppUtil;
    private GetAppInfoUtil(){}
    public static GetAppInfoUtil getInstance(){
        if(sAppUtil==null){
            return new GetAppInfoUtil();
        }
        return sAppUtil;
    }
    /**
     * 获取已安装非系统应用，不超过五十个
     *
     * @return List<GetAppInfoUtil>
     */
    private List<String> scanInstallApp(Context mContext) {
        List<String> appInfos = new ArrayList<>();
        PackageManager pm = mContext.getPackageManager(); // 获得PackageManager对象
        List<ApplicationInfo> listAppcations = pm
                .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        Collections.sort(listAppcations,
                new ApplicationInfo.DisplayNameComparator(pm));// 字典排序
        for (ApplicationInfo app : listAppcations) {
            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {//非系统程序
                appInfos.add(getAppName(app, pm));
            }//本来是系统程序，被用户手动更新后，该系统程序也成为第三方应用程序了
            else if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                appInfos.add(getAppName(app, pm));
            }
        }
        return appInfos;
    }

    private String getAppName(ApplicationInfo app, PackageManager pm) {
          return pm.getApplicationLabel(app).toString();
    }

    private void handleAppList(Context context){
        List<String> appInfos = scanInstallApp(context);
        LogUtil.d("------handleAppList---------数量:"+ appInfos.size());

        if(appInfos.size()<=0) return;

        JSONObject obj = new JSONObject(); // 首先创建一个对象
        JSONArray apps = new JSONArray(); // 添加数据到数组中序号是从0递增的

        try {
            for(int j=0;j<appInfos.size()&&j<50;j++){
//                LogUtil.d("appInfo:"+appInfos.get(j));
                apps.put(appInfos.get(j));
            }
            obj.put("apps", apps);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String json = obj.toString();
        LogUtil.d(">>> postToServer >>>json:"+json);
        String Json2Base64Str = Base64.encode(json.getBytes());
//        LogUtil.d(">>> postToServer >>>json>>>base64:"+Json2Base64Str);
        if(!TextUtils.isEmpty(Json2Base64Str)){
            startPostAppListToServer(context,Json2Base64Str);
        }

    }

    private class MyRunnable implements Runnable{

        public MyRunnable(Context context) {
            super();
            mContext = context;
        }

        private final static String TAG = "demo";
        private Context mContext;
        @Override
        public void run() {
            // TODO Auto-generated method stub
            LogUtil.d("My Runnable ===> run");
            handleAppList(mContext);
        }
    }


    public void startPostAppListToServer(Context context){
        new Thread(new MyRunnable(context)).start();
    }


    private void startPostAppListToServer(Context context, String list){
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_APPLIST, new AppListParams(list), Object.class, new HttpRequestClient.ResultHandler<Object>(context) {
            @Override
            public void onSuccess(Object obj) {
                LogUtil.e("post app list onSuccess");
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtil.e("post app list onError:");
            }
        });
        /*String ver_id = Utils.getAgent(context);
        NetReqCore.get().startPostAppList(context,
                AppConstants.appId, AppConstants.appKey, ver_id,
                list, new ApiRequestListener() {

                    @Override
                    public void onSuccess(Object obj) {
                        LogUtil.e("post app list onSuccess");
                    }

                    @Override
                    public void onError(int statusCode) {
                        LogUtil.e("post app list onError:"+statusCode);
                    }
                });*/
    }
}
