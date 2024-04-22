package com.yichi.expenseTracker.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.yichi.expenseTracker.sidebar.SettingActivity;
import com.yichi.expenseTracker.sidebar.AboutActivity;
import com.yichi.expenseTracker.sidebar.HistoryActivity;
import com.yichi.tally.R;
import com.yichi.expenseTracker.monthlyChartActivity;

public class SidebarDialog extends Dialog implements View.OnClickListener {

    private TextView btn_about, btn_setting, btn_history, btn_info;
    private Switch switchTheme; // 添加主题切换开关

    public SidebarDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 对话框不显示标题
        setContentView(R.layout.dialog_sidebar);

        // 初始化控件
        btn_about = findViewById(R.id.tv_dialog_sidebar_about);
        btn_setting = findViewById(R.id.tv_dialog_sidebar_setting);
        btn_history = findViewById(R.id.tv_dialog_sidebar_history);
        btn_info = findViewById(R.id.tv_dialog_sidebar_info);
        switchTheme = findViewById(R.id.switch_theme); // 获取主题切换的开关控件

        // 设置监听器
        btn_about.setOnClickListener(this);
        btn_setting.setOnClickListener(this);
        btn_history.setOnClickListener(this);
        btn_info.setOnClickListener(this);

        // 设置主题切换开关的监听器
        switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (getContext() instanceof Activity) {
                Activity activity = (Activity) getContext();
                if (isChecked) {
                    activity.setTheme(R.style.AppTheme_Night); // 使用夜间主题
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    activity.setTheme(R.style.Theme_Tally); // 使用日间主题
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                activity.recreate(); // 重新创建Activity来应用主题更改
            }

        });

        setDialogSize(); // 设置对话框的大小和位置
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = new Intent();
        if (id == R.id.tv_dialog_sidebar_about) {
            intent.setClass(getContext(), AboutActivity.class);
        } else if (id == R.id.tv_dialog_sidebar_setting) {
            intent.setClass(getContext(), SettingActivity.class);
        } else if (id == R.id.tv_dialog_sidebar_history) {
            intent.setClass(getContext(), HistoryActivity.class);
        } else if (id == R.id.tv_dialog_sidebar_info) {
            intent.setClass(getContext(), monthlyChartActivity.class);
        }
        getContext().startActivity(intent);
        dismiss(); // 关闭对话框
    }

    public void setDialogSize(){
        //获取当前窗口对象
        Window window = getWindow();
        //获取窗口对象的参数(wlp)
        WindowManager.LayoutParams attributes = window.getAttributes();
        //获取屏幕宽度
        Display display = window.getWindowManager().getDefaultDisplay();
        // 对话框窗口为屏幕窗口
        attributes.height = display.getHeight();
        attributes.gravity = Gravity.START | Gravity.TOP;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(attributes);
    }
}
