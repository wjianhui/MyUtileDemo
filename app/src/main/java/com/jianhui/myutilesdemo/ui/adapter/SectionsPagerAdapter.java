package com.jianhui.myutilesdemo.ui.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.jianhui.myutilesdemo.R;
import com.jianhui.myutilesdemo.ui.fragment.BluetoothFragment;
import com.jianhui.myutilesdemo.ui.fragment.ButtonFragment;
import com.jianhui.myutilesdemo.ui.fragment.ChartLineFragment;
import com.jianhui.myutilesdemo.ui.fragment.ChartPieFragment;
import com.jianhui.myutilesdemo.ui.fragment.ImageFragment;
import com.jianhui.myutilesdemo.ui.fragment.VideoFragment;

/**
 * @author Administrator
 * 2020/09/03 0003 9:39
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_name_bluetooth,
            R.string.tab_name_image, R.string.tab_name_video, R.string.tab_name_chat_pir,
            R.string.tab_name_chat_line, R.string.tab_name_button};
    private final Context mContext;

    public SectionsPagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return BluetoothFragment.newInstance(position);
        } else if (position == 1) {
            return ImageFragment.newInstance(position);
        } else if (position == 3) {
            return ChartPieFragment.newInstance(position);
        } else if (position == 2) {
            return VideoFragment.newInstance(position);
        } else if (position == 4) {
            return ChartLineFragment.newInstance(position);
        } else if (position == 5) {
            return ButtonFragment.newInstance(position);
        } else {
            return BluetoothFragment.newInstance(position);
        }
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}
