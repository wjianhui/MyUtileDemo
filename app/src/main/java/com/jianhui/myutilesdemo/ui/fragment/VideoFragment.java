package com.jianhui.myutilesdemo.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jianhui.myutilesdemo.R;

/**
 * @author Administrator
 * 2020/09/03 0003 9:56
 */
public class VideoFragment extends BaseListFragment {

    private static final String TAG_LOG = VideoFragment.class.getSimpleName();

    public static VideoFragment newInstance(int index) {
        VideoFragment videoFragment = new VideoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TAG_LOG, index);
        videoFragment.setArguments(bundle);
        return videoFragment;
    }

    @Override
    protected int findView() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData() throws NullPointerException {

    }
}
