package com.yichi.expenseTracker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yichi.expenseTracker.adapter.CalendarAdapter;
import com.yichi.expenseTracker.database.DBManager;
import com.yichi.expenseTracker.bean.TransactionBean;
import com.yichi.tally.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//设置Calender弹出的gridview
public class CalendarDialog extends Dialog implements View.OnClickListener {

    ImageView iv_back;
    GridView gv_dialog_calendar;
    LinearLayout ll_dialog_calendar;
    //有多少年 就放在这里面(hsvViewList)
    List<TextView>topTimeViewList;
    List<Integer>yearList;
    //正在被点击的年份位置（selectPos）
    int selectPosition= -1;
    List<TransactionBean>mDatas;
    private CalendarAdapter calendarAdapter;
    int selectMonth = -1;
    //点击后刷新gridview数据
    public interface  OnRefreshListener{
        public  void onRefresh(int selectPosition,int year,int month);
    }

    OnRefreshListener onRefreshListener;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;

    }

    //selectDialogYearPosition selectDialogMonthPosition记住之前年月选择的位置
    public CalendarDialog(@NonNull Context context,int selectDialogYearPosition,int selectDialogMonthPosition) {
        super(context);
        this.selectPosition = selectDialogYearPosition;
        this.selectMonth = selectDialogMonthPosition;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calendar);
        iv_back = findViewById(R.id.iv_dialog_calendar_back);
        gv_dialog_calendar = findViewById(R.id.gv_dialog_calendar);
        //(dialog_calendar_layout  hsvLauout)
        ll_dialog_calendar = findViewById(R.id.ll_dialog_calendar);
        iv_back.setOnClickListener(this);
        //这个方法是向横向的scrollView中添加view方法
        addViewToLayout();
        mDatas = new ArrayList<>();
        initGridView();
        //设置GridView当中 每一个tem的点击事件
        setGridViewListener();
    }

    private void setGridViewListener() {
        gv_dialog_calendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //被选中的位置 发生了变化
                calendarAdapter.selectPosition = position;
                calendarAdapter.notifyDataSetInvalidated();
                int month = position+1;
                int year = calendarAdapter.year;
                //获取到被选中的年份和月份 利用接口回调能进行内容修改
                onRefreshListener.onRefresh(selectPosition,year,month);
                cancel();
            }
        });
    }

    private void initGridView() {
        //获取哪一年被选中了 (selYear)
        int selectYearPosition = yearList.get(this.selectPosition);
        //getContext 传入数据源
        calendarAdapter = new CalendarAdapter(getContext(), selectYearPosition);
        //如果为默认值-1 则为当前月份
        if (selectMonth == -1) {
            /**待升级 现在在反复调用Calendar 获取时间**/
            int month = Calendar.getInstance().get(Calendar.MONTH);
            calendarAdapter.selectPosition = month;
        }else {
            calendarAdapter.selectPosition = selectMonth-1;
        }
        // 设置哪一项是变蓝的
        gv_dialog_calendar.setAdapter(calendarAdapter);

    }

    //对topTimeViewList和yearList进行初始化
    private void addViewToLayout() {
        //将添加进入线性布局的TextView进行统一管理的集合
        topTimeViewList = new ArrayList<>();
        //获取数据库中获取了多少个年份
        yearList= DBManager.getYearList();
        //如果数据库没有数据，就添加今年
        if (yearList.size()==0) {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            yearList.add(year);
        }

        //如果有 便利年份 有几年 就向scrollView当中添加几个view
        for (int i = 0; i < yearList.size(); i++) {
            int year = yearList.get(i);
            //将刚才写好的item布局 转换成view对象
            View view = getLayoutInflater().inflate(R.layout.item_calendardialog_toptime,null);
            ll_dialog_calendar.addView(view); //将view添加到布局当中
            //找到TextView便于管理
            TextView tv_topTime = view.findViewById(R.id.tv_item_calendardialog_toptime);
            tv_topTime.setText(year+"");
            //有的会变颜色 所以统一管理
            topTimeViewList.add(tv_topTime);
        }
        //初始化是最后一个位置
        if (selectPosition == -1) {
            // 让当前选中的是最近的年份 刚开始肯定是最新的一年 人性化
            selectPosition = topTimeViewList.size()-1;
        }
        //将最后一个设置为选中状态（changeTvBg）
        changeLastItemSelected(selectPosition);
        //设置一下每一个view的监听事件
        setTopTimeClickListener();
    }

    //给横向的scrollView当中每一个TextView日期 设置点击事件
    private void setTopTimeClickListener() {
        for (int i = 0; i < topTimeViewList.size(); i++) {
            TextView textView = topTimeViewList.get(i);
            //(position)
            int selector = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeLastItemSelected(selector);
                    selectPosition = selector;
                    //获取被选中的年份,然后下面的GridView显示数据源会发生变化 通过被选中的位置知道选中的是哪一年
                    int year = yearList.get(selectPosition);
                    calendarAdapter.setYear(year);
                }
            });
        }
    }

    //传入被选中的位置，改变此位置上的背景和颜色（changeTvBg）
    private void changeLastItemSelected(int selectPosition) {
        //先把所有设置一遍
        for (int i = 0; i < topTimeViewList.size(); i++) {
            TextView textView = topTimeViewList.get(i);
            textView.setBackgroundResource(R.drawable.dialog_button_background);
            textView.setTextColor(Color.BLACK);
        }
        //然后在设置 被选中的
        TextView selectTextView = topTimeViewList.get(selectPosition);
        selectTextView.setBackgroundResource(R.drawable.main_recordbtn_background);
        selectTextView.setTextColor(Color.WHITE);


    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_dialog_calendar_back) {
            cancel();
        }
    }

    public void setDialogSize(){
        //获取当前窗口对象
        Window window = getWindow();
        //获取窗口对象的参数(wlp)
        WindowManager.LayoutParams attributes = window.getAttributes();
        //获取屏幕宽度
        Display display = window.getWindowManager().getDefaultDisplay();
        // 对话框窗口为屏幕窗口
        attributes.width=(int)(display.getWidth());
        attributes.gravity = Gravity.TOP;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(attributes);
    }
}
