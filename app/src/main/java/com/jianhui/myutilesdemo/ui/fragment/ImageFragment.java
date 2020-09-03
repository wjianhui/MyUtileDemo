package com.jianhui.myutilesdemo.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.jianhui.myutilesdemo.R;

/**
 * @author Administrator
 * 2020/09/03 0003 9:56
 */
public class ImageFragment extends BaseListFragment {

    private static final String TAG_LOG = ImageFragment.class.getSimpleName();


    public static ImageFragment newInstance(int index) {
        ImageFragment imageFragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TAG_LOG, index);
        imageFragment.setArguments(bundle);
        return imageFragment;
    }

    @Override
    protected int findView() {
        return R.layout.fragment_image;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData() throws NullPointerException {

    }
}
