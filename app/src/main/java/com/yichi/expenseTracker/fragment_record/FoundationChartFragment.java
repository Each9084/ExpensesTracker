package com.yichi.expenseTracker.fragment_record;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.yichi.tally.R;
import com.yichi.expenseTracker.adapter.CategoryItemAdapter;
import com.yichi.expenseTracker.bean.CategoryItemBean;
import com.yichi.expenseTracker.database.DBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * (BaseChartFragment))
 *  是incomeChartFragment和expensesChartFragment的父类
 */
abstract public class FoundationChartFragment extends Fragment {
    ListView lv_chart;
    //数据源
    List<CategoryItemBean> mDatas;
     int year;
     int month;
    private CategoryItemAdapter categoryItemAdapter;
    //代表柱状图的控件(barchart)
    BarChart bc_item;
    //若果没有收支情况，现实的情况
    TextView tv_chart;
    protected PieChart pcCategory;

    protected abstract int getCategory();

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_income_chart, container, false);
        lv_chart = view.findViewById(R.id.lv_fragment_income_chart);
        //获取activity传递的数据 我们就知道哪一年 哪一月的信息了
        Bundle bundle = getArguments();
        year = bundle.getInt("year");
        month = bundle.getInt("month");
        //给ListView设置数据源 初始化数据源
        mDatas = new ArrayList<>();
        //设置适配器(ChartItemAdapter) 传入context以及空的数据源
        categoryItemAdapter = new CategoryItemAdapter(getContext(), mDatas);
        lv_chart.setAdapter(categoryItemAdapter);
        //添加头布局(addHeaderView)
        setTitleListView();


        initPieChart();
        return view;
    }

    protected void initPieChart() {
        pcCategory.getDescription().setEnabled(false);
        pcCategory.setExtraOffsets(5, 10, 5, 5);
        pcCategory.setDrawHoleEnabled(true);
        pcCategory.setHoleColor(Color.WHITE);
        pcCategory.setTransparentCircleColor(Color.WHITE);
        pcCategory.setTransparentCircleAlpha(110);
        pcCategory.setEntryLabelTextSize(12f);
        pcCategory.setEntryLabelColor(Color.BLACK);
        updatePieChart(year, month);
    }

    protected void updatePieChart(int year, int month) {
        List<CategoryItemBean> categories = DBManager.getCategoryMoneyByYearAndMonth(year, month, getCategory());
        List<PieEntry> entries = new ArrayList<>();
        for (CategoryItemBean item : categories) {
            entries.add(new PieEntry(item.getTotalAmount(), item.getCategory()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Category Distribution");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);
        pcCategory.setData(data);
        pcCategory.invalidate();
    }

    //添加头布局(addLVHeaderView)
    protected void setTitleListView() {
        //将布局 转换成view对象(headerView)
        View titleView = getLayoutInflater().inflate(R.layout.item_chart_fragment_title,null);
        //将view添加到头布局
        lv_chart.addHeaderView(titleView);
        //查找头布局当中包含的控件(barChart)
        bc_item = titleView.findViewById(R.id.bc_item_chart_fragment_title);
        tv_chart = titleView.findViewById(R.id.tv_item_chart_fragment_title);
        pcCategory = titleView.findViewById(R.id.pc_category_chart);
        //设定柱状图 不显示描述信息
        bc_item.getDescription().setEnabled(false);
        //设置柱状图的内边距
        bc_item.setExtraOffsets(20,20,20,20);
        //设置坐标轴
        setAxis(year,month);

        //设置坐标轴现实的数据(setAxisData)
        setAxisData(year,month);

    }
    //设置坐标轴显示的数据(setAxisData)
    protected abstract void setAxisData(int year, int month);

    //设置柱状图x 坐标轴的显示 包括x，y 方法必须重新，因为x轴相同，y轴不同
    protected void setAxis(int year, int month) {
        //设置x轴 得到x轴信息
        XAxis xAxis = bc_item.getXAxis();
        //设置x轴显示位置 下方
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // 设置绘制该轴的网格线
        xAxis.setDrawGridLines(true);
        //设置label数量 一个月最多31天
        xAxis.setLabelCount(31);
        //x轴标签大小
        xAxis.setTextSize(12f);
        //设置x轴现实的值的格式
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                //(val)
                int index = (int) value;
                //如果 第一列1号
                if (index==0) {
                    return month+getString(R.string.point1);//显示index.1
                }
                //月中
                if (index == 14){
                    return month+getString(R.string.point15);
                }

                //根据不同月份 显示做后一天的位置
                if (month==2){
                    if (index==27) {
                        return month+getString(R.string.point28);
                    }
                } else if (month==1||month==3||month==5||month==7||month==8||month==10||month==12) {
                    if (index == 31) {
                        return month+getString(R.string.point31);
                    }
                } else {
                    if (index == 29) {
                        return month+getString(R.string.point30);
                    }
                }

                //其他位置显示空
                return "";
            }
        });
        //设置标签对x轴的偏移量，垂直方向
        xAxis.setYOffset(10);

        //设定y轴在子类的设置(setYAxis)
        setYinChild(year, month);

    }

    //设置y轴，因为最高的坐标不确定，所以在子类中设置
    protected abstract void setYinChild(int year,int month);


    //改变当前年和月
     public void setDate(int year,int month){
        this.year = year;
        this.month = month;

         //每次点击会调用 清空柱状图当中的数据
         bc_item.clear();
         //重新绘制 柱状图
         bc_item.invalidate();
         //设置x轴 y轴坐标
         setAxis(year, month);
         //设置x轴，y轴数据
         setAxisData(year, month);
     };
    //设置下半部分ListView也会发生改变
    public void loadData(int year,int month,int category) {
        List<CategoryItemBean> list = DBManager.getCategoryMoneyByYearAndMonth(year, month, category);
        mDatas.clear();
        mDatas.addAll(list);
        categoryItemAdapter.notifyDataSetChanged();
    }
}