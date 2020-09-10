package com.jianhui.myutilesdemo.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
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

        LineData lineData = generateDataLine(1);

        lineChart.setOnChartValueSelectedListener(this);
        lineChart.getDescription().setEnabled(false);
        lineChart.setBackgroundColor(Color.WHITE);

        IndexAxisValueFormatter valueFormatter = new IndexAxisValueFormatter();
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTypeface(mTfLight);
        xAxis.setGranularity(1f);
//        xAxis.setValueFormatter(valueFormatter);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);


        YAxis leftAxis = lineChart.getAxisLeft();
//        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(5, false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.enableGridDashedLine(5f, 10f, 20f);

        YAxis rightAxis = lineChart.getAxisRight();
//        rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(5, false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        rightAxis.setDrawAxisLine(true);
        rightAxis.setEnabled(false);

        lineChart.setData(lineData);
        lineChart.animateX(750);

    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return Line data
     */
    private LineData generateDataLine(int cnt) {

        ArrayList<Entry> values1 = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            values1.add(new Entry(i, (int) (Math.random() * 50) + 50));
        }

        LineDataSet d1 = new LineDataSet(values1, "New DataSet " + cnt + ", (1)");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);
        d1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

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
}
