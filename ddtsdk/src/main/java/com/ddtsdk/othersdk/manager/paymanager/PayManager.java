package com.ddtsdk.othersdk.manager.paymanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.ddtsdk.KLSDK;
import com.ddtsdk.common.network.request.HttpRequestClient;
import com.ddtsdk.constants.ApiConstants;
import com.ddtsdk.constants.AppConfig;
import com.ddtsdk.constants.AppConstants;
import com.ddtsdk.log.LogTag;
import com.ddtsdk.log.LogUtils;
import com.ddtsdk.model.protocol.params.NoDataParams;
import com.ddtsdk.network.ApiException;
import com.ddtsdk.othersdk.manager.AdManager;
import com.ddtsdk.othersdk.manager.EventFlag;
import com.ddtsdk.othersdk.manager.bean.AbnormalPayBean;
import com.ddtsdk.othersdk.manager.params.PayParam;
import com.ddtsdk.utils.Base64;
import com.ddtsdk.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;


public class PayManager {
    final static String filename = "pay_list_billno";
    final String keyCache = "cacheOrderid";
    static PayManager payManager = null;
    static SharedPreferences sharedata = null;
    static Map<String, String> map = null;
    private String cOrderId = "";

    private PayManager() {
    }

    public static PayManager getInstance() {
        if (payManager == null) {
            payManager = new PayManager();
            sharedata = KLSDK.getInstance().getContext()
                    .getSharedPreferences(filename, 0);
            map = new HashMap<>();
        }
        return payManager;
    }

    public void saveData(String orderid, String amount) {
        SharedPreferences.Editor edit = sharedata.edit();
        StringBuilder data = new StringBuilder();
        String cache = getData(keyCache);
        if (!TextUtils.isEmpty(cache)) {
            data.append(cache);
        }
        data.append(orderid)
                .append("||")
                .append(amount)
                .append("&&");
        edit.putString(keyCache, data.toString());
        edit.apply();
    }

    private String getData(String name) {
        return sharedata.getString(name, "");
    }

    private void removeData(String name, String orderid) {
        String cache = getData(name);
        if (TextUtils.isEmpty(cache)) return;

        StringBuilder sb = new StringBuilder();
        String[] list = cache.split("&&");
        for (String temp : list) {
            if (!temp.contains(orderid)) {
                sb.append(temp).append("&&");
            }
        }
        SharedPreferences.Editor edit = sharedata.edit();
        edit.putString(name, sb.toString());
        edit.apply();
    }

    /**
     * @param orderid 订单号
     * @param price   充值金额（单位：元）
     */
    public void newestPay(String orderid, String price) {

        if (cOrderId.equals(orderid) || AdManager.getInstance().noOpenPayReport()) return;
        if (price.contains(".")) {
            price = String.valueOf((int) Float.parseFloat(price));
        }
        cOrderId = orderid;
        LogUtils.getInstance().superLog(1, LogTag.ADTAG, null,
                "orderid:" + orderid, "price:" + price);
//        saveData(orderid, price);
        map.put(orderid, price);
        payResult(orderid);
    }


    /**
     * @param orderid 订单号
     */
    private void payResult(final String orderid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * 3);
                    queryResultFromServer(toJson(orderid));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * @param
     */
    public void cacheOrderid() {
        if (AdManager.getInstance().noOpenPayReport()) return;
        cacheQuery();
    }

    public void cacheQuery() {
        String cache = getData(keyCache);
        if (!TextUtils.isEmpty(cache)) payResult(cache);
    }

    public void queryResultFromServer(final String ordarray) {
        String orderid_list = Base64.encode(ordarray.getBytes());
        PayParam payParam = new PayParam();
        payParam.setAppid(AppConstants.appId);
        payParam.setToken(AppConstants.Token);
        payParam.setSessionid(AppConstants.Sessid);
        payParam.setOrd(orderid_list);
        LogUtils.getInstance().superLog(1, LogTag.ADTAG, null,
                "orderid:" + orderid_list);
        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_QUERY_PAY, payParam, List.class, new HttpRequestClient.ResultHandler<List<Map<String, Object>>>(KLSDK.getInstance().getContext()) {
            @Override
            public void onSuccess(List<Map<String, Object>> list) {
                for (Map<String, Object> result : list) {
                    Log.i("TAG", "onSuccess: " + result);
                    Bundle bundle = new Bundle();
                    bundle.putString(EventFlag.AMOUNT, map.get(result.get("order_id").toString()));
                    bundle.putString(EventFlag.ORDERID, result.get("order_id").toString());
                    bundle.putString(EventFlag.MSG, "2");
                    if ((boolean) result.get("status")) {
                        bundle.putString(EventFlag.STATUS, "success");
                        bundle.putString(EventFlag.EXTRA, "订单成功");
                        AdManager.getInstance().pay(KLSDK.getInstance().getContext(), bundle);
                        AdManager.getInstance().logPayReport(KLSDK.getInstance().getContext(), bundle);
                    } else {
                        LogUtil.e("头条 订单未完成");
                        bundle.putString(EventFlag.STATUS, "error");
                        bundle.putString(EventFlag.EXTRA, "订单未完成");
//                        AdManager.getInstance().pay(KLSDK.getInstance().getContext(), bundle);
                        AdManager.getInstance().logPayReport(KLSDK.getInstance().getContext(), bundle);
                    }
                    removeData(keyCache, result.get("order_id").toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                try {
                    JSONArray jsonArray = new JSONArray(ordarray);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String oid = jsonArray.getJSONObject(i).getString("oid");
                        Bundle bundle = new Bundle();
                        bundle.putString(EventFlag.AMOUNT, map.get(oid) == null ? "" : map.get(oid));
                        bundle.putString(EventFlag.ORDERID, oid);
                        bundle.putString(EventFlag.MSG, "2");
                        bundle.putString(EventFlag.STATUS, "error");
                        bundle.putString(EventFlag.EXTRA, t.getMessage());
                        AdManager.getInstance().logPayReport(KLSDK.getInstance().getContext(), bundle);
                        if (t instanceof ApiException) {
                            removeData(keyCache, oid);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String toJson(String orderid) {
        JSONArray jsonArray = new JSONArray();
        if (!orderid.contains("&&")) {
            try {
                JSONObject object = new JSONObject();
                object.put("oid", orderid);
                jsonArray.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            for (String val : orderid.split("&&")) {
                if (!TextUtils.isEmpty(val)) {
                    try {
                        String id = val.substring(0, val.indexOf("||"));
                        JSONObject object = new JSONObject();
                        object.put("oid", id);
                        map.put(id, val.substring((val.indexOf("||") + 2)));
                        jsonArray.put(object);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return jsonArray.toString();
    }

//    private void abnormalPayRequest(final Bundle bundle) {
//        if (!(AdManager.getInstance().abnormalAdReport() || AppConfig.adType == 3)) {
//            AdManager.getInstance().pay(KLSDK.getInstance().getContext(), bundle);
//            return;
//        }
//        HttpRequestClient.sendPostRequest(ApiConstants.ACTION_PAY_REPORT, new NoDataParams(), AbnormalPayBean.class, new HttpRequestClient.ResultHandler<AbnormalPayBean>(KLSDK.getInstance().getContext()) {
//            @Override
//            public void onSuccess(AbnormalPayBean bean) {
//                try {
//                    abnormalPay(bean, bundle);
//                } catch (Exception e) {
//                    Log.e("abnormalPayRequest", "msg=" + e.getMessage());
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                if (t instanceof ApiException) {
//                    AdManager.getInstance().pay(KLSDK.getInstance().getContext(), bundle);
//                }
//            }
//        });
//    }

//    private void abnormalPay(AbnormalPayBean bean, final Bundle bundle) {
//        if (bean.getControl_status() == 0) {
//            AdManager.getInstance().pay(KLSDK.getInstance().getContext(), bundle);
//        } else {
//            if (bean.getControl_type().equals("increment")) {
//                AdManager.getInstance().pay(KLSDK.getInstance().getContext(), bundle);
//                int addCount = Integer.parseInt(bean.getThreshold_value());
//                List<String> amoutList = bean.getAmount();
//                int l_len = amoutList.size();
//                for (int i = 0; i < addCount; i++) {
//                    bundle.putString(EventFlag.AMOUNT, i >= l_len ? amoutList.get(l_len) : amoutList.get(i));
//                    bundle.putString(EventFlag.EXTRA, "订单成功,增量");
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                Thread.sleep(1000 * 10);
//                                AdManager.getInstance().pay(KLSDK.getInstance().getContext(), bundle);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }).start();
//                }
//            } else {
//                bundle.putString(EventFlag.EXTRA, "订单成功,缩量");
//                AdManager.getInstance().logPayReport(KLSDK.getInstance().getContext(), bundle);
//            }
//        }
//    }
}
