package com.jianhui.myutilesdemo.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author Administrator
 * 2020/09/03 0003 9:54
 */
public abstract class BaseListFragment extends Fragment {

    private static final String TAG_LOG = "BaseListFragment";

    /**
     * 初始化界面组件
     */
    protected abstract int findView();

    /**
     * 初始化方法
     */
    protected abstract void initView(View view);


    /**
     * 初始化数据
     */
    protected abstract void initData() throws NullPointerException;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(findView(), container, false);
        initView(view);
        initData();
        return view;
    }
}
