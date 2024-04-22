package com.yichi.expenseTracker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yichi.tally.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理历史账单界面的弹出日历对话框，点击日历表，弹出对话框，当中的GridView对应的适配器
 **/
public class CalendarAdapter extends BaseAdapter {
    Context context;
    List<String> mDats;// 这个我们自己设置
    public int year;
    public int selectPosition = -1;

    public void setYear(int year) {
        this.year = year;
        //原来的数据应该清空
        mDats.clear();
        // 加载新的数据
        loadYearAndMonth(year);
        //数据源发生改变 提示adapter进行更新
        notifyDataSetChanged();
    }

    public CalendarAdapter(Context context, int year) {
        this.context = context;
        this.year = year;
        mDats = new ArrayList<>();
        //i从1开始 表示月份 加载年份和月份的数据
        loadYearAndMonth(year);
    }

    private void loadYearAndMonth(int year) {
        for (int i = 1; i < 13; i++) {
            //年+月
            String data = year + "/" + i;
            mDats.add(data);
        }
    }

    @Override
    public int getCount() {
        return mDats.size();
    }

    @Override
    public Object getItem(int position) {
        return mDats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_calendardialog_gv, parent, false);
        TextView textView = convertView.findViewById(R.id.tv_item_calendardialog_gv);
        textView.setText(mDats.get(position));
        //点击不同 显示不同背景
        textView.setBackgroundResource(R.color.grey_f3f3f3);
        textView.setTextColor(Color.BLACK);
        if (position == selectPosition) {
            textView.setBackgroundResource(R.color.blue_00BCD4);
            textView.setTextColor(Color.WHITE);
        }
        return convertView;
    }
}
