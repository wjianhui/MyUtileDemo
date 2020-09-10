package com.jianhui.myutilesdemo.ui.utiles;

import android.app.Activity;
import android.util.DisplayMetrics;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

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

    public static void mergeFile(String[] stcs, String des){

        try {
            FileOutputStream out = new FileOutputStream(des);
            for (String path : stcs){
                FileInputStream in = new FileInputStream(path);
                byte[] buff = new byte[4096];
                int len;
                while ((len=in.read(buff))>0){
                    out.write(buff, 0, len);
                    out.flush();
                }
                in.close();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
