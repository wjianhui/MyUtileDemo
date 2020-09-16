package com.jianhui.myutilesdemo.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.jianhui.myutilesdemo.R;

import java.util.ArrayList;

/**
 * @author Administrator
 * 2020/09/08 0008 9:51
 */
public class ChartLineFragment extends BaseListFragment implements OnChartValueSelectedListener {

    private static final String TAG_LOG = ChartPieFragment.class.getSimpleName();
    private LineChart lineChart;


    public static ChartLineFragment newInstance(int index) {
        ChartLineFragment chartLineFragment = new ChartLineFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TAG_LOG, index);
        chartLineFragment.setArguments(bundle);
        return chartLineFragment;

    }

    @Override
    protected int findView() {
        return R.layout.fragment_chart_line;
    }

    @Override
    protected void initView(View view) {
        lineChart = view.findViewById(R.id.fragment_line_chart);

    }

    @Override
    protected void initData() throws NullPointerException {

        initLineChart();

    }

    private void initLineChart() {

        LineData lineData = generateDataLine();

        lineChart.setOnChartValueSelectedListener(this);
        lineChart.getDescription().setEnabled(false);
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.getDescription().setPosition(200, 100);
        lineChart.getAxisRight().setEnabled(false);

        //图例
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);
        //图例样式：有圆点，正方形，短线 几种样式
        legend.setForm(Legend.LegendForm.CIRCLE);
        // 图例显示的位置：如下2行代码设置图例显示在左下角
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        // 图例的排列方式：水平排列和竖直排列2种
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        // 图例距离x轴的距离
        legend.setXEntrySpace(10f);
        //图例距离y轴的距离
        legend.setYEntrySpace(10f);
        //图例的大小
        legend.setFormSize(7f);
        // 图例描述文字大小
        legend.setTextSize(10);


        IndexAxisValueFormatter valueFormatter = new IndexAxisValueFormatter(xValuesProcess());
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setYOffset(5);
        xAxis.setValueFormatter(valueFormatter);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);


        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setXOffset(15);
        leftAxis.setLabelCount(10, false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.enableGridDashedLine(5f, 10f, 20f);

//        YAxis rightAxis = lineChart.getAxisRight();
//        rightAxis.setTypeface(mTf);
//        rightAxis.setLabelCount(5, false);
//        rightAxis.setDrawGridLines(false);
//        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        rightAxis.setDrawAxisLine(true);
//        rightAxis.setEnabled(false);

        lineChart.setData(lineData);
        lineChart.animateX(750);

    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return Line data
     */
    private LineData generateDataLine() {

        ArrayList<Entry> values1 = new ArrayList<>();

        values1.add(new Entry(0, 1));
        values1.add(new Entry(1, 50));
        values1.add(new Entry(2, 80));
        values1.add(new Entry(3, 100));
        values1.add(new Entry(4, 150));
        values1.add(new Entry(5, 90));
        values1.add(new Entry(6, 60));


        LineDataSet d1 = new LineDataSet(values1, "");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(2.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);
        d1.setMode(LineDataSet.Mode.CUBIC_BEZIER);


//        ArrayList<Entry> values2 = new ArrayList<>();
//        for (int i = 0; i < 12; i++) {
//            values2.add(new Entry(i, values1.get(i).getY() - 30));
//        }
//        LineDataSet d2 = new LineDataSet(values2, "New DataSet " + cnt + ", (2)");
//        d2.setLineWidth(2.5f);
//        d2.setCircleRadius(4.5f);
//        d2.setHighLightColor(Color.rgb(244, 117, 117));
//        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
//        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
//        d2.setDrawValues(false);

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
//        sets.add(d2);

        return new LineData(sets);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }


    /**
     * x轴数据处理
     *
     * @return x轴数据
     */
    private static String[] xValuesProcess() {
        String[] week = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

//        String[] weekValues = new String[7];
//        Calendar calendar = Calendar.getInstance();
//        int currentWeek = calendar.get(Calendar.DAY_OF_WEEK);
//
//        for (int i = 6; i >= 0; i--) {
//            weekValues[i] = week[currentWeek - 1];
//            if (currentWeek == 1) {
//                currentWeek = 7;
//            } else {
//                currentWeek -= 1;
//            }
//        }
//        return weekValues;

        return week;

    }


}
