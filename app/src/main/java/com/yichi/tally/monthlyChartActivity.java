package com.yichi.tally;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yichi.tally.adapter.ChartViewPagerAdapter;
import com.yichi.tally.database.DBManager;
import com.yichi.tally.fragment_record.IncomeChartFragment;
import com.yichi.tally.fragment_record.OutcomeFragment;
import com.yichi.tally.fragment_record.expensesChartFragment;
import com.yichi.tally.utils.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class monthlyChartActivity extends AppCompatActivity implements View.OnClickListener {


    private Button btn_expenses;
    private Button btn_income;
    private TextView tv_time;
    private TextView tv_expenses;
    private TextView tv_income;
    private ViewPager vp_month_chart;
    private ImageView iv_back;
    private int year;
    private int month;
    int selectPosition = -1;
    int selectMonth = -1;
    private ImageView iv_calendar;
    List<Fragment> chartFragmentList;
    private IncomeChartFragment incomeChartFragment;
    private ChartViewPagerAdapter chartAdapter;
    private expensesChartFragment expensesChartFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_chart);

        btn_expenses = findViewById(R.id.btn_month_chart_expenses);
        btn_income = findViewById(R.id.btn_month_chart_income);
        iv_calendar = findViewById(R.id.iv_calendar_monthly_chart_activity);
        iv_back = findViewById(R.id.iv_back_monthly_chart_activity);
        tv_time = findViewById(R.id.tv_time_monthly_chart_activity);
        tv_expenses = findViewById(R.id.tv_expenses_monthly_chart_activity);
        tv_income = findViewById(R.id.tv_income_monthly_chart_activity);
        vp_month_chart = findViewById(R.id.vp_month_chart);



        btn_expenses.setOnClickListener(this);
        btn_income.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_calendar.setOnClickListener(this);
        initTime();
        initDatas(year,month);
        initFragment();//收入支出两个fragment
        // 滑动页面 是 按钮也会跟着变(setVPSelectListener();)
        setSlideListener();


    }

    private void setSlideListener() {
        vp_month_chart.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 如果不需要处理滚动事件，可以不做任何操作
            }

            @Override
            public void onPageSelected(int position) {
                changeButton(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // 如果不需要处理滚动状态改变事件，可以不做任何操作
            }
        });
    }


    private void initFragment() {
      chartFragmentList = new ArrayList<>();
      //添加fragment对象
        incomeChartFragment = new IncomeChartFragment();
        expensesChartFragment = new expensesChartFragment();

        //添加数据到fragment当中
        Bundle bundle = new Bundle();
        bundle.putInt("year",year);
        bundle.putInt("month",month);
        incomeChartFragment.setArguments(bundle);
        expensesChartFragment.setArguments(bundle);
        //将fragment添加到数据源当中 在这里setDate 然后load 实现点击其他月 刷新数据
        chartFragmentList.add(expensesChartFragment);
        chartFragmentList.add(incomeChartFragment);
        //使用适配器 我们就能得到adapter对象(chartVPAdapter)
        chartAdapter = new ChartViewPagerAdapter(getSupportFragmentManager(), chartFragmentList);
        //(chartVP)
        vp_month_chart.setAdapter(chartAdapter);
        //将fragment加载到activity当中



    }

    /**待升级 未来抽出**/
    //初始化时间
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
    }

    //初始化统计 某年某月的统计数据数据(initStatisitics)
    private void initDatas(int year,int month) {
        //收入总钱数(inMoneyOneMonth)
        float sumIncomeMoney = DBManager.getSumMoneyOneMonth(year, month, 1);
        //支出总钱数(outMoneyOneMonth)
        float sumExpensesMoney = DBManager.getSumMoneyOneMonth(year, month, 0);
        //收入多少笔(incountItemOneMonth)
        int numberOfIncome = DBManager.getNumberOfItemOneMonth(year, month, 1);
        //支出多少笔(outcountItemOneMonth)
        int numberOfExpenses = DBManager.getNumberOfItemOneMonth(year, month, 0);

        tv_time.setText(year+"."+month+" "+getString(R.string.invoice));
        /**
         * 待升级：未来应该加入判断 1个账单 为单数
         * **/
        tv_income.setText(getString(R.string.there_are)+" "
                + numberOfIncome+" "+getString(R.string.incomes_here)
                + " ,"+getString(R.string.gbp)+" "+sumIncomeMoney);

        tv_expenses.setText(getString(R.string.there_are)+" "
                + numberOfExpenses+" "+getString(R.string.incomes_here)
                + " ,"+getString(R.string.gbp)+" "+sumExpensesMoney);

    }

    //改变button样式 expenses-0 income-1(setButtonStyle)
    private void changeButton(int category) {
        //支出被点击
        if (category==0) {
            btn_expenses.setBackgroundResource(R.drawable.main_recordbtn_background);
            btn_expenses.setTextColor(Color.WHITE);
            btn_income.setBackgroundResource(R.drawable.dialog_button_background);
            btn_income.setTextColor(Color.BLACK);

        } else if (category == 1) {
            btn_income.setBackgroundResource(R.drawable.main_recordbtn_background);
            btn_income.setTextColor(Color.WHITE);
            btn_expenses.setBackgroundResource(R.drawable.dialog_button_background);
            btn_expenses.setTextColor(Color.BLACK);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_back_monthly_chart_activity) {

            finish();
        } else if (id == R.id.iv_calendar_monthly_chart_activity) {
            showCalendarDialog();
        } else if (id == R.id.btn_month_chart_expenses) {
                changeButton(0);
                vp_month_chart.setCurrentItem(0);

        }else if (id == R.id.btn_month_chart_income) {
                changeButton(1);
                vp_month_chart.setCurrentItem(1);
        }
    }

    //显示日历对话框
    private void showCalendarDialog() {
        CalendarDialog dialog = new CalendarDialog(this, selectPosition, selectMonth);
        dialog.show();
        dialog.setDialogSize();
        //这个接口回调 日期改变 既改变了选中的位置1,2 又改变了上半部分显示的年月3
        // 下半部分ListView也会发生改变 4,5
        dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
            @Override
            public void onRefresh(int selectPosition, int year, int month) {
                monthlyChartActivity.this.selectPosition = selectPosition;//1
                monthlyChartActivity.this.selectMonth = month;//2
                initDatas(year,month);//3
                //改变fragment里的年和月
                incomeChartFragment.setDate(year,month);//4
                expensesChartFragment.setDate(year, month);//5
            }
        });

    }


}