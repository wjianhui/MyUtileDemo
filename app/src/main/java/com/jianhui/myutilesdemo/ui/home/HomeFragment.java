package com.jianhui.myutilesdemo.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.jianhui.myutilesdemo.R;
import com.jianhui.myutilesdemo.ui.adapter.SectionsPagerAdapter;

public class HomeFragment extends Fragment {


    private TabLayout mainHomeTabLayout;
    private ViewPager mainHomeViewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initView(root);
        return root;
    }


    private void initView(View root) {
        mainHomeTabLayout = root.findViewById(R.id.main_home_tabLayout);
        mainHomeViewPager = root.findViewById(R.id.main_home_viewPager);

        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), getContext());
        mainHomeViewPager.setAdapter(pagerAdapter);
        mainHomeTabLayout.setupWithViewPager(mainHomeViewPager);


    }
}