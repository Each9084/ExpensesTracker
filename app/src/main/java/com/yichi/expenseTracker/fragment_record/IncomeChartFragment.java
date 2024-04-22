package com.yichi.expenseTracker.fragment_record;

import android.graphics.Color;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.yichi.expenseTracker.database.DBManager;
import com.yichi.tally.R;
import com.yichi.expenseTracker.bean.BarChartBean;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class IncomeChartFragment extends FoundationChartFragment {
    int category = 1;
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
                Log.i("ning", "初始化柱子：第" +i+"根");
                barEntries.add(barEntry);
            }
            // 那些有 哪些添加
            for (int i = 0; i < sumList.size(); i++) {
                //得到每一个对象(itemBean) 柱子信息
                BarChartBean bean = sumList.get(i);
                Log.d("ning", ""+sumList.size());
                Log.d("ning", "第 " + i +"次循环，sumList总金额为 "+ "为:"+bean.getAllAmount());
                //获取日期 那一天进行了消费
                int day = bean.getDay();
                Log.d("ning", "消费的天为"+"第 " + day + "天");
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
            barDataSet.setValueTextColor(R.color.blue_00BCD4);
            //大小
            barDataSet.setValueTextSize(8f);
            //柱子的颜色
            barDataSet.setColor(Color.GREEN);
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
            //柱子宽度
            barData.setBarWidth(0.2f);
            //
            bc_item.setData(barData);
        }
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
        //不显示右边的y轴
        axisRight.setEnabled(false);

        YAxis axisLeft = bc_item.getAxisLeft();
        axisLeft.setAxisMaximum(maxTopResult);
        axisLeft.setAxisMinimum(0f);
        axisLeft.setEnabled(false);
        //设置y轴的标签大小axisLeft.setTextSize(15f);

        //默认不显示图例
        Legend legend = bc_item.getLegend();
        legend.setEnabled(false);


    }

    //继承父类方法，再去调用loadDate
    //设置下半部分ListView也会发生改变
    @Override
    public void setDate(int year, int month) {
        super.setDate(year, month);
        loadData(year,month,category);

    }
}