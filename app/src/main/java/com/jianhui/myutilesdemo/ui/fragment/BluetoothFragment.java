package com.jianhui.myutilesdemo.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jianhui.myutilesdemo.R;

/**
 * @author Administrator
 * 2020/09/03 0003 9:47
 */
public class BluetoothFragment extends BaseListFragment {

    private static final String TAG_LOG = BluetoothFragment.class.getSimpleName();


    public static BluetoothFragment newInstance(int index) {
        BluetoothFragment bluetoothFragment = new BluetoothFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TAG_LOG, index);
        bluetoothFragment.setArguments(bundle);
        return bluetoothFragment;

    }


    @Override
    protected int findView() {
        return R.layout.fragment_bluetooth;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData() throws NullPointerException {

    }
}
