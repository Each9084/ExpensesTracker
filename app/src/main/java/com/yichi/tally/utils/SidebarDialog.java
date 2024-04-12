package com.yichi.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.yichi.tally.AboutActivity;
import com.yichi.tally.HistoryActivity;
import com.yichi.tally.R;
import com.yichi.tally.SettingActivity;
import com.yichi.tally.monthlyChartActivity;

//(moreDialog)
public class SidebarDialog extends Dialog implements View.OnClickListener{

    private Button btn_about;
    private Button btn_setting;
    private Button btn_history;
    private Button btn_info;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sidebar);

        btn_about = findViewById(R.id.btn_dialog_sidebar_about);
        btn_setting = findViewById(R.id.btn_dialog_sidebar_setting);
        btn_history = findViewById(R.id.btn_dialog_sidebar_history);
        btn_info = findViewById(R.id.btn_dialog_sidebar_info);
        iv_back = findViewById(R.id.iv_dialog_siderbar_back);

        btn_about.setOnClickListener(this);
        btn_setting.setOnClickListener(this);
        btn_history.setOnClickListener(this);
        btn_info.setOnClickListener(this);
        iv_back.setOnClickListener(this);

    }

    public SidebarDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onClick(View v) {
        int id =v.getId();
        Intent intent = new Intent();
        if (id ==R.id.iv_dialog_siderbar_back) {
            cancel();
        } else if (id ==R.id.btn_dialog_sidebar_about) {
            intent.setClass(getContext(), AboutActivity.class);
            getContext().startActivity(intent);
        }else if (id ==R.id.btn_dialog_sidebar_setting) {
            intent.setClass(getContext(), SettingActivity.class);
            getContext().startActivity(intent);
        }else if (id ==R.id.btn_dialog_sidebar_history) {
            intent.setClass(getContext(), HistoryActivity.class);
            getContext().startActivity(intent);

        }else if (id ==R.id.btn_dialog_sidebar_info) {//账单图表
            intent.setClass(getContext(), monthlyChartActivity.class);
            getContext().startActivity(intent);
        }
        cancel();
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
