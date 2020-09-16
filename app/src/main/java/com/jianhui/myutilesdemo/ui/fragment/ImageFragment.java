package com.jianhui.myutilesdemo.ui.fragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import com.jianhui.myutilesdemo.R;
import com.jianhui.myutilesdemo.ui.utiles.CircleProgressBar;

/**
 * @author Administrator
 * 2020/09/03 0003 9:56
 */
public class ImageFragment extends BaseListFragment implements View.OnClickListener {

    private static final String TAG_LOG = ImageFragment.class.getSimpleName();

    private TextView textToolHtml;



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
        textToolHtml = view.findViewById(R.id.text_tool_html);



    }

    @Override
    protected void initData() throws NullPointerException {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }

    }
}
