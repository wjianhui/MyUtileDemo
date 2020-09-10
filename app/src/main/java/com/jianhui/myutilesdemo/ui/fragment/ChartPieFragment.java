package com.jianhui.myutilesdemo.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.jianhui.myutilesdemo.R;
import com.jianhui.myutilesdemo.ui.utiles.DateToolUtiles;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * 2020/09/07 0007 14:40
 */
public class ChartPieFragment extends BaseListFragment {

    private static final String TAG_LOG = ChartPieFragment.class.getSimpleName();
    private PieChart chartPie;
    List<PieEntry> list;


    public static ChartPieFragment newInstance(int index) {
        ChartPieFragment chartPieFragment = new ChartPieFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TAG_LOG, index);
        chartPieFragment.setArguments(bundle);
        return chartPieFragment;

    }

    @Override
    protected int findView() {
        return R.layout.fragment_chart_pie;
    }

    @Override
    protected void initView(View view) {
        chartPie = view.findViewById(R.id.chart_pie);
        int windowsHeight = DateToolUtiles.getWindowsHeight(requireActivity());
        int helght = windowsHeight / 2;
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) chartPie.getLayoutParams();
        layoutParams.height = helght;
        chartPie.setLayoutParams(layoutParams);

        //是否允许手势滑动
        chartPie.setTouchEnabled(false);


    }

    @Override
    protected void initData() throws NullPointerException {


        initChatPie();

    }

    private void initChatPie() {

        list = new ArrayList<>();

        list.add(new PieEntry(50, "A"));
        list.add(new PieEntry(80, "B"));
        list.add(new PieEntry(40, "C"));
        list.add(new PieEntry(10, "D"));

        PieDataSet pieDataSet = new PieDataSet(list, "");
        PieData pieData = new PieData(pieDataSet);
        chartPie.setData(pieData);

        chartPie.setBackgroundColor(Color.WHITE);

        chartPie.setExtraOffsets(0, 10, 0, 10);

        //设置各个数据的颜色
        pieDataSet.setColors(Color.RED, Color.BLUE, Color.YELLOW, Color.MAGENTA);
        //实体扇形的空心圆的半径   设置成0时就是一个圆 而不是一个环
        chartPie.setHoleRadius(70);
        //中间半透明白色圆的半径    设置成0时就是隐藏
        chartPie.setTransparentCircleRadius(0);
        //设置中心圆的颜色
        chartPie.setHoleColor(Color.WHITE);
        //设置中心部分的字  （一般中间白色圆不隐藏的情况下才设置）
        chartPie.setCenterText("本周访问数\n20");
        //设置中心字的字体颜色
        chartPie.setCenterTextColor(Color.RED);
        //设置中心字的字体大小

        chartPie.setCenterTextSize(16);
        //设置描述的字体大小
        chartPie.setEntryLabelTextSize(13);
        //设置描述的字体颜色
        chartPie.setEntryLabelColor(Color.BLACK);
        //设置数据的字体大小
        pieDataSet.setValueTextSize(18);


        //设置描述的位置
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueLinePart1Length(0.3f);//设置描述连接线长度
        //设置数据的位置
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueLinePart2Length(0.5f);//设置数据连接线长度
        //设置两根连接线的颜色
        pieDataSet.setValueLineColor(Color.BLACK);

        //对于右下角一串字母的操作
        chartPie.getDescription().setEnabled(false);                  //是否显示右下角描述
        chartPie.getDescription().setText("这是修改那串英文的方法");    //修改右下角字母的显示
        chartPie.getDescription().setTextSize(20);                    //字体大小
        chartPie.getDescription().setTextColor(Color.RED);             //字体颜色

        //图例
        Legend legend = chartPie.getLegend();
        legend.setEnabled(true);    //是否显示图例
        legend.setXEntrySpace(30);
        //图例的位置
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);


        //数据更新
        chartPie.notifyDataSetChanged();
        chartPie.invalidate();

        //动画（如果使用了动画可以则省去更新数据的那一步）
        chartPie.animateY(500); //在Y轴的动画  参数是动画执行时间 毫秒为单位
//        line.animateX(2000); //X轴动画
//        line.animateXY(2000,2000);//XY两轴混合动画


    }
}
