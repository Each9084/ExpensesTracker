package com.yichi.expenseTracker.fragment_record;

import android.graphics.Color;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.yichi.expenseTracker.bean.BarChartBean;
import com.yichi.expenseTracker.database.DBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class expensesChartFragment extends FoundationChartFragment {
    int category = 0;

    @Override
    public void onResume() {
        super.onResume();
        loadData(year,month,category);
    }

    @Override
    protected int getCategory() {
        return category;
    }

    //设置坐标轴显示的数据(setAxisData)
    @Override
    protected void setAxisData(int year, int month) {
        //柱状图数据的集合(sets)
        List<IBarDataSet> list = new ArrayList<>();
        //获取这个月每天的支出总金额(list)
        List<BarChartBean> sumList = DBManager.getSumAmountOneDayOfMonth(year, month, category);
        //本月没有消费
        if (sumList.size()==0) {
            bc_item.setVisibility(View.GONE);
            tv_chart.setVisibility(View.VISIBLE);
        }else {
            //有消费
            bc_item.setVisibility(View.VISIBLE);
            tv_chart.setVisibility(View.GONE);

            //即使可能只有几天，但柱子还要画 创建一个BarEntry集合 表示柱状图的显示内容
            //设置有多少根柱子 初始化柱子 添加到柱状图里(barEntries1)
            List<BarEntry> barEntries = new ArrayList<>();
            //最多31根柱子
            for (int i = 0; i < 31; i++ ) {
                //i表示位置 0.0f高度
                BarEntry barEntry = new BarEntry(i, 0.0f);
                barEntries.add(barEntry);
            }
            // 那些有 哪些添加
            for (int i = 0; i < sumList.size(); i++) {
                //得到每一个对象(itemBean) 柱子信息
                BarChartBean bean = sumList.get(i);
                //获取日期 那一天进行了消费
                int day = bean.getDay();
                //根据天数 获取x轴的位置(xIndex)
                int xPosition = day-1;
                //通过这个位置，得到我们刚才初始化的entries对象
                BarEntry barEntry = barEntries.get(xPosition);
                //设置y轴大小 设置为价格高度
                barEntry.setY(bean.getAllAmount());
            }

            //(barDataSet1)将 barEntries封装到dataset当中 进行一个整体的封装
            BarDataSet barDataSet = new BarDataSet(barEntries,"");
            // 值的颜色
            // 使用颜色数组来给每个柱子分配颜色
            int[] colors = new int[barEntries.size()];
            float threshold = 50f; // 设置一个阈值，您可以根据需求调整这个值
            for (int i = 0; i < barEntries.size(); i++) {
                // 根据柱子的值来决定颜色
                if (barEntries.get(i).getY() < threshold) {
                    colors[i] = Color.parseColor("#58D68D"); // 数值较低时的颜色
                } else {
                    colors[i] = Color.parseColor("#EC7063"); // 数值较高时的颜色
                }
            }
            barDataSet.setColors(colors);
            /**待升级 设置图例 barDataSet.setLabel("蔬菜");**/
            barDataSet.setValueFormatter(new IndexAxisValueFormatter(){
                @Override
                public String getFormattedValue(float value) {
                    if (value==0) {
                        return "";
                    }
                    return value+ "";
                }
            });
            list.add(barDataSet);

            BarData barData = new BarData(list);
            barData.setBarWidth(0.4f); // 设置柱状图的宽度
            bc_item.setData(barData); // 将数据设置到图表

// 在这里添加移除X轴网格线的代码
            XAxis xAxis = bc_item.getXAxis();
            xAxis.setDrawGridLines(false); // 移除X轴的垂直网格线

// 添加动画和触摸反馈的设置
            bc_item.animateY(1000);
            bc_item.setTouchEnabled(true);
            bc_item.setDragEnabled(true);
            bc_item.setScaleEnabled(true);



        }

        bc_item.animateY(1000);
        bc_item.setTouchEnabled(true);
        bc_item.setDragEnabled(true);
        bc_item.setScaleEnabled(true);

    }
    //设置子类y轴（setYAxis）
    @Override
    protected void setYinChild(int year,int month) {
        //获取本月收入最高的一天诶多少，将他设定为y轴的最大值
        float maxMoneyDayOfMonth = DBManager.getMaxMoneyDayOfMonth(year, month, category);
        /**
         * 待升级 未来应是 +10 一下 151取 161
         * **/
        //向上取整 151.6 取 152
        float maxTopResult =  (float)Math.ceil(maxMoneyDayOfMonth);
        //设置y轴数据
        YAxis axisRight = bc_item.getAxisRight();
        //设置y轴最大值
        axisRight.setAxisMaximum(maxTopResult);
        axisRight.setAxisMinimum(0f);

        axisRight.setDrawGridLines(false); // 移除网格线
        axisRight.setDrawAxisLine(false); // 移除轴线
        //不显示右边的y轴
        axisRight.setEnabled(false);

        YAxis axisLeft = bc_item.getAxisLeft();
        axisLeft.setAxisMaximum(maxTopResult);
        axisLeft.setAxisMinimum(0f);
        axisLeft.setEnabled(false);
        axisLeft.setDrawGridLines(false); // 移除网格线
        axisLeft.setDrawAxisLine(false); // 移除轴线
        //设置y轴的标签大小axisLeft.setTextSize(15f);

        //默认不显示图例
        Legend legend = bc_item.getLegend();
        legend.setEnabled(false);


    }

    //设置下半部分ListView也会发生改变
    @Override
    public void setDate(int year, int month) {
        super.setDate(year, month);
        loadData(year,month,category);
    }
}