package com.jianhui.myutilesdemo;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.jianhui.myutilesdemo.ui.bean.PermissionBean;
import com.jianhui.myutilesdemo.ui.utiles.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

public class Permission {

    private static volatile Permission singleton;

    private Permission() {
    }

    public static Permission getInstance() {
        if (singleton == null) {
            synchronized (Permission.class) {
                if (singleton == null) {
                    singleton = new Permission();
                }
            }
        }
        return singleton;
    }


    //获取权限
    public boolean permissionInit(Context context) {
        final ArrayList<PermissionBean> mPermissionList = new ArrayList<>();
        mPermissionList.add(new PermissionBean(Manifest.permission.WRITE_EXTERNAL_STORAGE, "程序需要缓存一些数据在SD卡，否则无法运行."));
        mPermissionList.add(new PermissionBean(Manifest.permission.CAMERA, "程序需要使用摄像头权限用来音视频通话，否则无法运行.") );
        mPermissionList.add(new PermissionBean(Manifest.permission.RECORD_AUDIO, "程序需要录音权限用来音视频通话，否则无法运行."));
        mPermissionList.add(new PermissionBean(Manifest.permission.READ_PHONE_STATE, "程序需要电话监听权限用来音视频通话，否则无法运行."));

        boolean isOk = PermissionUtils.checkPermission(context, new PermissionUtils.PermissionUtilsInter() {
            @Override
            public List<PermissionBean> getApplyPermissions() {
                return mPermissionList;
            }

            @Override
            public AlertDialog.Builder getTipAlertDialog() {
                return null;
            }

            @Override
            public Dialog getTipDialog() {
                return null;
            }

            @Override
            public AlertDialog.Builder getTipAppSettingAlertDialog() {
                return null;
            }

            @Override
            public Dialog getTipAppSettingDialog() {
                return null;
            }
        });

        return isOk;

    }
}