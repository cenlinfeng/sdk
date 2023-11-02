package com.ddtsdk.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;

/**
 * Created by czg on 2020/4/17.
 */

public class LifeCycleUtils {

    public static boolean checkActivityLifeCycle(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if(activity.isDestroyed() || activity.isFinishing()){
                return false;
            }else {
                return true;
            }
        }else {
            return activity.getWindow().isActive();
        }
    }

    public static boolean checkFragmentLifeCycle(Fragment fragment){
        return !fragment.isDetached();
    }

    public static boolean checkV4FragmentLifeCycle(android.support.v4.app.Fragment v4fragment){
        return !v4fragment.isDetached();
    }

    public static boolean checkContextLifeCycle(Context context){

        if(context instanceof Activity){
            Activity activity = (Activity) context;
            return checkActivityLifeCycle(activity);
        }else {// other such as ApplicationContext
            return true;
        }

    }

}
