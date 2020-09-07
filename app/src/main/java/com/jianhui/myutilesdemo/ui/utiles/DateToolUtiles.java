package com.jianhui.myutilesdemo.ui.utiles;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * @author Administrator
 * 2020/09/07 0007 17:33
 */
public class DateToolUtiles {


    /**
     * 获取屏幕宽度
     */
    public static int getWindowsWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param activity
     * @return
     */
    public static int getWindowsHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

}
