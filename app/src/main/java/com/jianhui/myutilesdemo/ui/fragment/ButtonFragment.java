package com.jianhui.myutilesdemo.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.jianhui.myutilesdemo.R;
import com.jianhui.myutilesdemo.ui.utiles.CircleProgressBar;

/**
 * @author Administrator
 * 2020/09/16 0016 9:47
 */
public class ButtonFragment extends BaseListFragment implements View.OnClickListener {
    private static final String TAG_LOG = ButtonFragment.class.getSimpleName();

    private CircleProgressBar progress_circular;

    public static ButtonFragment newInstance(int index) {
        ButtonFragment buttonFragment = new ButtonFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TAG_LOG, index);
        buttonFragment.setArguments(bundle);
        return buttonFragment;

    }


    @Override
    protected int findView() {
        return R.layout.fragment_button;
    }

    @Override
    protected void initView(View view) {
        progress_circular = view.findViewById(R.id.progress_circular);
        progress_circular.setOnClickListener(this);

    }

    @Override
    protected void initData() throws NullPointerException {

    }

    @Override
    public void onClick(View view) {

    }
}
